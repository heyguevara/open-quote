package org.szimano.alfresco;

import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;
import javax.transaction.UserTransaction;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationComponent;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.transaction.TransactionService;
import org.alfresco.web.app.servlet.AuthenticationHelper;
import org.alfresco.web.bean.LoginBean;
import org.alfresco.web.bean.repository.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.portals.bridges.portletfilter.PortletFilter;
import org.apache.portals.bridges.portletfilter.PortletFilterChain;
import org.apache.portals.bridges.portletfilter.PortletFilterConfig;
import org.springframework.web.context.WebApplicationContext;

/**
* Filters every request for the Alfresco portlet and makes sure that the
* correct user PortletSession environment exists so that the user is not asked
* to re-authenticate
* 
* <h5>Initialization Parameters</h5>
* 
* <b>alfrescoExternalAuth</b>: true/false [default true]. Configures Alfresco
* to use external authentication or not. If true, certain user controls are not
* available in Alfresco
* 
* @author Jon French
* @author Tomasz Szymanski
*/
public class JBossPortalAlfrescoPortletFilter implements PortletFilter {

   private static final Log LOG = LogFactory
         .getLog(JBossPortalAlfrescoPortletFilter.class);

   private AuthenticationService fAuthService;
   private AuthenticationComponent fAuthComponent;
   private PersonService fPersonService;
   private NodeService fNodeService;
   private TransactionService fTransactionService;

   private final static Logger log = Logger
         .getLogger(JBossPortalAlfrescoPortletFilter.class);

   private Boolean fAlfrescoExternalAuthentication = Boolean.TRUE;

   public void init(PortletFilterConfig filterConfig) throws PortletException {

      WebApplicationContext ctx = (WebApplicationContext) filterConfig
            .getPortletConfig()
            .getPortletContext()
            .getAttribute(
                  WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

      ServiceRegistry serviceRegistry = (ServiceRegistry) ctx
            .getBean(ServiceRegistry.SERVICE_REGISTRY);
      fNodeService = serviceRegistry.getNodeService();
      fTransactionService = serviceRegistry.getTransactionService();

      fAuthService = (AuthenticationService) ctx
            .getBean("authenticationService");
      fAuthComponent = (AuthenticationComponent) ctx
            .getBean("authenticationComponent");
      fPersonService = (PersonService) ctx.getBean("personService");

      String alfrescoExternalAuth = filterConfig
            .getInitParameter("alfrescoExternalAuth");

      if (alfrescoExternalAuth != null) {
         fAlfrescoExternalAuthentication = Boolean
               .valueOf(alfrescoExternalAuth);
         if (LOG.isDebugEnabled()) {
            LOG.debug("Alfresco is configured to external authentication: "
                  + fAlfrescoExternalAuthentication);
         }
      }

      if (LOG.isDebugEnabled()) {
         LOG.debug(getClass().getName() + " initialized");
      }
   }

   public void renderFilter(RenderRequest request, RenderResponse response,
         PortletFilterChain chain) throws PortletException, IOException {

      if (LOG.isDebugEnabled()) {
         LOG.debug("here in renderFilter");
      }

      try {
         ensureLogin(request);
      } catch (ServletException e) {
         throw new PortletException(e);
      }

      chain.renderFilter(request, response);
   }

   public void processActionFilter(ActionRequest request,
         ActionResponse response, PortletFilterChain chain)
         throws PortletException, IOException {

      if (LOG.isDebugEnabled()) {
         LOG.debug("here in renderFilter.");
      }

      try {
         ensureLogin(request);
      } catch (ServletException e) {
         throw new PortletException(e);
      }

      chain.processActionFilter(request, response);
   }

   public void destroy() {
   }

   @SuppressWarnings("unchecked")
private void ensureLogin(PortletRequest request) throws PortletException,
         IOException, ServletException {

      PortletSession session = request.getPortletSession();

      User user = (User) session
            .getAttribute(AuthenticationHelper.AUTHENTICATION_USER);
      Principal portalUser = request.getUserPrincipal();

      /*
       * Case 1:
       * 
       * The Alfresco user is NOT null, but the Portal user is null. In this
       * case, something happened (the user logged out)
       * 
       * Case 2:
       * 
       * The user name associated with the Alfresco session and portal session are
       * NOT the same. This can happen if the user logs out and logs in as a
       * different user.
       * 
       * In either case, I need to end the Alfresco user session.
       * 
       * I copied the code to end the Alfresco user's session from the
       * org.alfresco.web.bean.LoginBean.logout() method from Alfresco
       * community 1.3
       * 
       */

      if ((portalUser == null && user != null) || // case 1
            (portalUser != null && user != null && !portalUser.getName()
                  .equals(user.getUserName()))) // case 2
      {

         LOG.info("Disabling alfresco user.");

         // invalidate ticket and clear the Security context for this thread
         fAuthService.invalidateTicket(user.getTicket());
         fAuthService.clearCurrentSecurityContext();
         
         session.removeAttribute(AuthenticationHelper.AUTHENTICATION_USER);

         // remove all objects from our session by hand
         // we do this as invalidating the Portal session would invalidate
         // all other portlets!

         if (FacesContext.getCurrentInstance() != null
               && FacesContext.getCurrentInstance().getExternalContext() != null) {
            Map sesMap = FacesContext.getCurrentInstance()
                  .getExternalContext().getSessionMap();

            for (Object key : sesMap.keySet()) {
               sesMap.remove(key);
            }
            
            sesMap.put(AuthenticationHelper.SESSION_INVALIDATED, true);
         }
         
         Enumeration en = request.getPortletSession().getAttributeNames();
         while (en.hasMoreElements()) {
            String key = (String)en.nextElement();
            
            if (key.endsWith(AuthenticationHelper.AUTHENTICATION_USER)) {
               request.getPortletSession().removeAttribute(key);
            }
         }

         // Set the user equal to null
         
         user = null;
         
         // every render is generated twice (render + render or action + render so we can just return)
         return;
      }

      if (user == null && portalUser != null) {
         if (LOG.isDebugEnabled()) {
            LOG.debug("session user attribute is null.");
         }

         String userName = portalUser.getName();

         /*
          * Make sure the user is loaded into the PortletSession environment.
          * The code below is modeled on the Alfresco
          * org.alfresco.web.app.servlet.NTLMAuthenticationFilter
          */

         UserTransaction tx = fTransactionService.getUserTransaction();
         NodeRef homeSpaceRef = null;

         try {
            tx.begin();

            // User name should match the uid in the person entry found

            fAuthComponent.setCurrentUser(userName);
            userName = fAuthComponent.getCurrentUserName();

            // Setup User object and Home space ID etc.

            NodeRef personNodeRef = fPersonService.getPerson(userName);

            String currentTicket = fAuthService.getCurrentTicket();
            user = new User(userName, currentTicket, personNodeRef);

            homeSpaceRef = (NodeRef) fNodeService.getProperty(
                  personNodeRef, ContentModel.PROP_HOMEFOLDER);
            user.setHomeSpaceId(homeSpaceRef.getId());

            // Commit

            tx.commit();
         } catch (Throwable ex) {
            try {
               tx.rollback();
            } catch (Exception ex2) {
               log.error("Failed to rollback transaction", ex2);
            }
            if (ex instanceof RuntimeException) {
               throw (RuntimeException) ex;
            } else if (ex instanceof IOException) {
               throw (IOException) ex;
            } else if (ex instanceof ServletException) {
               throw (ServletException) ex;
            } else {
               throw new RuntimeException("Authentication setup failed",
                     ex);
            }
         }

         // Store the user

         session
               .setAttribute(AuthenticationHelper.AUTHENTICATION_USER,
                     user);

         /*
          * This parameter tells Alfresco if you are using external
          * authentication.
          * 
          * If true, one thing it will do is remove the "create user" button
          * 
          * Note that there seems to be a bug in Alfresco in that Alfresco
          * only checks for the presence/absence of the the
          * LoginBean.LOGIN_EXTERNAL_AUTH session attribute instead of its
          * value. This means that the attribute can equal Boolean.FALSE but
          * NOT display the "Create user" button.
          */
         if (Boolean.TRUE.equals(fAlfrescoExternalAuthentication)) {
            session.setAttribute(LoginBean.LOGIN_EXTERNAL_AUTH,
                  Boolean.TRUE);
         }

         // Note! If you wanted to do any fancy Locale stuff, you should do
         // it here.

         if (LOG.isDebugEnabled()) {
            LOG.debug("User logged on via PortalSSO");
         }

         return;
      } else {
         if (LOG.isDebugEnabled()) {
            LOG.debug("session user attribute is NOT null: \n\n"
                  + "username: " + user.getUserName() + "\n");
         }
      }
   }

}