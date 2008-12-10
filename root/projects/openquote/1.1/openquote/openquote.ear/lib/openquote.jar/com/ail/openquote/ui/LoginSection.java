/* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.ail.openquote.ui;

import static com.ail.openquote.ui.util.Functions.addError;
import static com.ail.openquote.ui.util.Functions.findError;
import static com.ail.openquote.ui.util.Functions.hasErrorMarker;
import static com.ail.openquote.ui.messages.I18N.i18n;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.portal.common.transaction.TransactionException;
import org.jboss.portal.common.transaction.TransactionManagerProvider;
import org.jboss.portal.common.transaction.Transactions;
import org.jboss.portal.identity.MembershipModule;
import org.jboss.portal.identity.Role;
import org.jboss.portal.identity.RoleModule;
import org.jboss.portal.identity.User;
import org.jboss.portal.identity.UserModule;
import org.jboss.portal.identity.UserProfileModule;

import com.ail.core.Type;
import com.ail.openquote.Proposer;
import com.ail.openquote.Quotation;
import com.ail.openquote.ui.util.Functions;

/**
 * The LoginSection page element provides a single UI section which deals with the following
 * use-cases:<ol>
 * <li>Existing user logs in, is successfully authenticated and forwarded to a secured URL; or
 * if authentication fails is forwarded to a "login failed" page. If authentication does succeed,
 * the user is forwarded to &lt;protocol&gt;://&lt;host&gt;:&lt;port&gt;/portal/auth/&lt;portalName&gt;/&lt;<b>pageName</b>&gt
 * where <b>portalName</b> and <b>pageName</b> are taken from the {@link #getForwardToPortalName() forwardToPortalName} and
 * {@link #getForwardToPageName() forwardToPageName} properties. Clicking on the "invitation link" reveals the section
 * shown below (2.); selecting the forgotten password link reveals the section shown in 3.<br/>
 * <img src="doc-files/LoginSection-1.png"/>
 * </li>
 * <li>New user creates an account. If the user's email address is already known (/quote/proposer/emailAddress!=null),
 * then the "Email address:" field is automatically populated.<br/>
 * <img src="doc-files/LoginSection-2.png"/><br/>
 * When "Create and Save" is selected, the details are validated and a new portal user is created. Validation include
 * a check for duplicate usernames. The user is then automatically logged in as the new user and forwarded to the same 
 * URL as in 1. above.<br/>
 * </li>
 * <li>Existing user requests a password reminder. On selecting "Send Reminder" the form is validated and an email
 * is sent to the specified address providing a new password. An error is displayed if "Email address" is not
 * associated with an existing user.<br/>
 * <img src="doc-files/LoginSection-3.png"/>
 * </li>
 * </ol>
 * @see SaveButtonAction
 */
public class LoginSection extends PageContainer {
	private static final long serialVersionUID = 297215265083279666L;
	private String invitationMessageText;
    private String invitationLinkText;

    /** Login button's label text. Defaults to "Login" */
    private String loginButtonLabel="i18n_login_section_login_button_label";
    
    /** Page to forward to once authentication has succeeded. */
    private String forwardToPageName=null;
    
    /** Javascript to reset the LoginSection to show the "Proposer Login".
     * @see SaveButtonAction */
    public static final String reset="showDivDisplay(\"Proposer Login\"); hideDivDisplay(\"Forgotten Password\");hideDivDisplay(\"Create Login\")";
    
    public LoginSection() {
        super();
    }


    public String getInvitationLinkText() {
        return invitationLinkText;
    }

    public void setInvitationLinkText(String invitationLinkText) {
        this.invitationLinkText = invitationLinkText;
    }

    public String getInvitationMessageText() {
        return invitationMessageText;
    }

    public void setInvitationMessageText(String invitationMessageText) {
        this.invitationMessageText = invitationMessageText;
    }

    /**
     * Text of the label to appear on the login button. This defaults to "Login".
     * @return Login button's label text
     */
    public String getLoginButtonLabel() {
        return loginButtonLabel;
    }

    /**
     * @see #getLoginButtonLabel()
     * @param loginButtonLabel Login button's label text
     */
    public void setLoginButtonLabel(String loginButtonLabel) {
        this.loginButtonLabel = loginButtonLabel;
    }

    /**
     * Name of the page to forward to if authentication is successful. The name is taken to be
     * a portal page name. On login the user is forwarded to: <p/>
     * &nbsp;&nbsp;&lt;protocol&gt;://&lt;host&gt;:&lt;port&gt;/portal/auth/&lt;portalName&gt;/&lt;<b>pageName</b>&gt;
     * @see #getForwardToPortalName()
     * @return Name of portal page to forward to.
     */
    public String getForwardToPageName() {
        return forwardToPageName;
    }

    /**
     * @see #getForwardToPageName()
     * @param pageName
     */
    public void setForwardToPageName(String pageName) {
        this.forwardToPageName = pageName;
    }
    
    /**
     * Return the name of the portal to forward to following authentication. 
     * @param response
     * @return Name of the "current" portal
     */
    private String nameOfForwardToPortal(RenderResponse response) {
        return Functions.getPortalName(response);
    }

    @Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        String op=Functions.getOperationParameters(request).getProperty("op");

        if ("Create".equals(op)) {
            String username=request.getParameter("username");
            ((Quotation)model).setUsername(username);
        }
        else if ("Save".equals(op)) {
            String username=request.getParameter("username");
            ((Quotation)model).setUsername(username);
        }
        
        return model;
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        boolean error=false;
        String op=Functions.getOperationParameters(request).getProperty("op");

        if ("Create".equals(op)) {
            Functions.removeErrorMarkers(model);

            String u=request.getParameter("username");
            String uc=request.getParameter("cusername");
            String p=request.getParameter("password");
            String pc=request.getParameter("cpassword");
            
            if (u==null || u.length()==0) {
                addError("username", i18n("i18n_required_error"), model);
                error=true;
            }
            else if (!u.equals(uc)) {
                addError("cusername", i18n("i18n_login_section_username_missmatch_error"), model);
                error=true;
            }

            if (p==null || p.length()==0) {
                addError("password", i18n("i18n_required_error"), model);
                error=true;
            }
            else if (!p.equals(pc)) {
                addError("cpassword", i18n("i18n_login_section_password_missmatch_error"), model);
                error=true;
            }
            else {
                if (isAnExistingUser(u, request)) {
                    addError("username", i18n("i18n_login_section_username_taken_error"), model);
                    error=true;                    
                }
            }

            // if any errors were found add a summary error. This doesn't ever get displayed, but
            // is used in renderResponse() to make sure the page is opened with the create form on
            // display.
            if (error) {
                addError("create", "error", model);
            }
        }
        else if ("Save".equals(op) && request.getUserPrincipal()==null) {
            // We're saving and the user isn't logged in yet.
            Functions.removeErrorMarkers(model);

            String u=request.getParameter("username");
            String p=request.getParameter("password");
            
            if (u==null || u.length()==0) {
            	addError("username", i18n("i18n_required_error"), model);
                error=true;
            }
            else if (!isAnExistingUser(u, request)) {
                addError("username", i18n("i18n_login_section_unknown_username_error"), model);
                error=true;                    
            }

            if (p==null || p.length()==0) {
                addError("password", i18n("i18n_required_error"), model);
                error=true;
            }            

            // if any errors were found add a summary error. This doesn't ever get displayed, but
            // is used in renderResponse() to make sure the page is opened with the save form on
            // display.
            if (error) {
                addError("login", "error", model);
            }
        }

        return error;
    }

    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        TransactionManager tm=null;
        Transaction tx=null;

        String op=Functions.getOperationParameters(request).getProperty("op");

        if ("Create".equals(op)) {            
            Quotation quote=(Quotation)model;
            Proposer proposer=(Proposer)quote.getProposer();
            String password=request.getParameter("password");

            // This assumes that the attributes ("UserModule" etc) have been injected into the session's context in 
            // the jboss-portal.xml portal descriptor file.
            UserModule userModule=(UserModule)request.getPortletSession().getPortletContext().getAttribute("UserModule");
            UserProfileModule userProfileModule=(UserProfileModule)request.getPortletSession().getPortletContext().getAttribute("UserProfileModule");;
            RoleModule roleModule=(RoleModule)request.getPortletSession().getPortletContext().getAttribute("RoleModule");
            MembershipModule membershipModule=(MembershipModule)request.getPortletSession().getPortletContext().getAttribute("MembershipModule");

            try {
                tm=TransactionManagerProvider.JBOSS_PROVIDER.getTransactionManager();
                tx=Transactions.applyBefore(Transactions.TYPE_REQUIRED, tm);
                User user=userModule.createUser(quote.getUsername(), password);
                userProfileModule.setProperty(user, User.INFO_USER_ENABLED, true);
                userProfileModule.setProperty(user, User.INFO_USER_NAME_FAMILY, proposer.getSurname());
                userProfileModule.setProperty(user, User.INFO_USER_NAME_GIVEN, proposer.getFirstName());
                userProfileModule.setProperty(user, User.INFO_USER_EMAIL_REAL, proposer.getEmailAddress());
                userProfileModule.setProperty(user, User.INFO_USER_LOCALE, request.getLocale().toString());
                userProfileModule.setProperty(user, User.INFO_USER_REGISTRATION_DATE, new Date());

                Set<Role> roleSet = new HashSet<Role>();
                Role role = roleModule.findRoleByName("Proposer");
                roleSet.add(role);    
                membershipModule.assignRoles(user, roleSet);

                String pageName=Functions.getOperationParameters(request).getProperty("page");
                String portalName=Functions.getOperationParameters(request).getProperty("portal");
                response.sendRedirect("/portal/auth/portal/"+portalName+"/"+pageName+"/QuoteWindow?op=save&action=1&username="+quote.getUsername()+"&password="+password);
            }
            catch (Exception e) {
                // TODO Send a support email
                e.printStackTrace();
            }
            finally {
                try {
                    Transactions.applyAfter(Transactions.TYPE_REQUIRED, tm, tx);
                }
                catch(TransactionException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (loginButtonLabel.equals(op) && request.getUserPrincipal()==null) {
            // We're performing a save and the user isn't logged in yet.
            String password=request.getParameter("password");
            String username=request.getParameter("username");

            try {
                String pageName=Functions.getOperationParameters(request).getProperty("page");
                String portalName=Functions.getOperationParameters(request).getProperty("portal");
                response.sendRedirect("/portal/auth/portal/"+portalName+"/"+pageName+"/QuoteWindow?op=save&action=1&username="+username+"&password="+password);
            }
            catch (Exception e) {
                // TODO Send a support email
                e.printStackTrace();
            }
        }
        else if ("Reminder".equals(op)) {
            // TODO Send a reminder email to the user
        }
        else {
            model=super.processActions(request, response, model);
        }
        
        return model;
    }

    @Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        Quotation quotation=(Quotation)model;
        Proposer proposer=(Proposer)quotation.getProposer();

        // Guess the username from the quotation or the proposer's email address.
        String usernameGuess=quotation.getUsername()!=null ? quotation.getUsername() : proposer.getEmailAddress();

        String lnk="<a onClick='hideDivDisplay(\"Proposer Login\");showDivDisplay(\"Create Login\");'>"+invitationLinkText+"</a>";
        
        w.printf("<table>");
        w.printf( "<tr>");
        w.printf(  "<td align='center'>");

        // Div #1: login and save the quote. Note: if the usernameGuess is a known user we'll pre-populate the form with 
        // the guess to save the user some typing; otherwise we'll leave the username field blank.
        w.printf(   "<div class='portlet-font' id='Proposer Login'>");
        w.printf(    invitationMessageText, lnk);
        w.printf(    "<form method='post' action='%s' name='loginform' id='loginForm'>", response.createActionURL());
        w.printf(     "<table>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td>"+i18n("i18n_login_section_username_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='text' name='username' id='username' value='%s'/></td>", isAnExistingUser(usernameGuess, request) ? usernameGuess : "");
        w.printf(       "<td class='portlet-msg-error'>%s</td>", findError("username", model));
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td valign='center'>"+i18n("i18n_login_section_password_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='password' name='password' id='password' value=''/></td>");
        w.printf(       "<td><a onClick='hideDivDisplay(\"Proposer Login\");showDivDisplay(\"Forgotten Password\");'>Forgotten password?</a></td>");
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td colspan='3'><input type='submit' id='loginButton' class='portlet-form-input-field' name='op=%1$s:page=%2$s:portal=%3$s' value='%1$s'/></td>", i18n(loginButtonLabel), getForwardToPageName(), nameOfForwardToPortal(response));
        w.printf(      "</tr>");
        w.printf(     "</table>");
        w.printf(    "</form>");
        w.printf(   "</div>");

        // Div #2: create a new user and save quote
        w.printf(   "<div class='portlet-font' id='Create Login'>");
        w.printf(    "Create a new account here. If you have an existing account, please <a onClick='showDivDisplay(\"Proposer Login\");hideDivDisplay(\"Create Login\");'>login here</a>.");
        w.printf(    "<form method='post' action='%s'>", response.createActionURL());
        w.printf(     "<table>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td>"+i18n("i18n_login_section_username_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='text' name='username' id='username' value='%s'/></td>",  !isAnExistingUser(usernameGuess, request) ? usernameGuess : "");
        w.printf(       "<td class='portlet-msg-error'>%s</td>", findError("username", model));
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td>"+i18n("i18n_login_section_confirm_username_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='text' name='cusername' id='cusername' value=''/></td>");
        w.printf(       "<td class='portlet-msg-error'>%s</td>", findError("cusername", model));
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td valign='center'>"+i18n("i18n_login_section_password_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='password' name='password' id='password' value=''/></td>");
        w.printf(       "<td class='portlet-msg-error'>%s</td>", findError("password", model));
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td valign='center'>"+i18n("i18n_login_section_confirm_password_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='password' name='cpassword' id='cpassword' value=''/></td>");
        w.printf(       "<td class='portlet-msg-error'>%s</td>", findError("cpassword", model));
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td colspan='3'><input type='submit' id='createLoginButton' class='portlet-form-input-field' name='op=Create:page=%s:portal=%s' value='"+i18n("i18n_login_section_create_and_save_button_label")+"'/></td>", getForwardToPageName(), nameOfForwardToPortal(response));
        w.printf(      "</tr>");
        w.printf(     "</table>");
        w.printf(    "</form>");
        w.printf(   "</div>");

        // Div #3: Send a password reminder
        w.printf(   "<div class='portlet-font' id='Forgotten Password'>");
        w.printf(    i18n("i18n_login_section_email_password_message"));
        w.printf(    "<form method='post'>");
        w.printf(     "<table>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td>"+i18n("i18n_login_section_username_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='text' name='username' id='username' value='%s'/></td>",  isAnExistingUser(usernameGuess, request) ? usernameGuess : "");
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td colspan='2'><input type='submit' id='email' class='portlet-form-input-field' name='op=Reminder' value='"+i18n("i18n_login_section_send_reminder_button_label")+"'/></td>");
        w.printf(      "</tr>");
        w.printf(     "</table>");
        w.printf(    "</form>");
        w.printf(   "</div>");

        w.printf(  "</td>");
        w.printf( "</tr>");
        w.printf("</table>");

        w.printf("<script type='text/javascript'>");

        // hide the 'create login' form unless there's an error associated with it.
        if (hasErrorMarker("create", model)) {
            w.printf( "hideDivDisplay('Forgotten Password');");
            w.printf( "hideDivDisplay('Proposer Login');");
            w.printf( "showDivDisplay('Create Login');");
        }
        else if (hasErrorMarker("login", model)) {
            w.printf( "hideDivDisplay('Create Login');");
            w.printf( "hideDivDisplay('Forgotten Password');");
            w.printf( "showDivDisplay('Proposer Login');");
        }
        else {
            w.printf( "hideDivDisplay('Create Login');");
            w.printf( "hideDivDisplay('Forgotten Password');");
            w.printf( "hideDivDisplay('Proposer Login');");
        }

        w.printf("</script>");
        
        return model;
    }

    private boolean isAnExistingUser(String username, PortletRequest request) {
        boolean ret=false;
        TransactionManager tm=null;
        Transaction tx=null;

        // check if the username is already being used.
        UserModule userModule=(UserModule)request.getPortletSession().getPortletContext().getAttribute("UserModule");

        try {
            tm=TransactionManagerProvider.JBOSS_PROVIDER.getTransactionManager();
            tx=Transactions.applyBefore(Transactions.TYPE_REQUIRED, tm);

            if (userModule.findUserByUserName(username)!=null) {
                ret=true;
            }
        }
        catch(Exception e) {
            // ignore
        }
        finally {
            try {
                Transactions.applyAfter(Transactions.TYPE_REQUIRED, tm, tx);
            }
            catch(TransactionException e) {
                e.printStackTrace();
            }
            
        }
        
        return ret;
    }
}
