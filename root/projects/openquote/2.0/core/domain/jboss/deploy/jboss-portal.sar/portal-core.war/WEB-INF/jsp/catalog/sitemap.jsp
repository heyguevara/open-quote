<%@ page import="org.jboss.portlet.JBossRenderResponse" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.jboss.portal.api.node.PortalNode" %>
<%@ page import="org.jboss.portal.api.node.PortalNodeURL" %>
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<portlet:defineObjects/>

<%
   String contextPath = request.getContextPath();
   PortalNode parent = (PortalNode)request.getAttribute("parentNode");
   JBossRenderResponse jresponse = (JBossRenderResponse)renderResponse;
%>
<style>

   <!--
   #foldheader {
      margin-left: -10px;
      padding: 0px 0px 0px 0px;
      cursor: pointer;
      cursor: hand;
      list-style-image: url( <%= contextPath %> /images/catalog/bullet.gif );
   }

   #foldinglist {
      cursor: pointer;
      cursor: hand;
      list-style-position: outside;
      margin-left: 0px;
      padding: 0px 0px 0px 0px;
      vertical-align: top;
      list-style-image: url( <%= contextPath %> /images/catalog/T.gif )
   }

   #pagenosub {
      margin-left: -10px;
      list-style-image: url( <%= contextPath %> /images/catalog/bullet.gif )
   }

   /
   /
   -->
</style>

<table cellspacing="2" cellpadding="5" border="0">
   <%
      int colCount = 4;
      PortalNode rootNode = parent.getRoot();
      for (Iterator i = rootNode.getChildren().iterator(); i.hasNext();)
      {
         if (colCount % 4 == 0) // create new row every 4 columns
         {
   %>
   <tr>
      <%
         }
         PortalNode parentSibling = (PortalNode)i.next();
         for (Iterator j = parentSibling.getChildren().iterator(); j.hasNext();)
         {
            PortalNode child = (PortalNode)j.next();
            if (child.getType() == PortalNode.TYPE_PAGE)
            {
               colCount++;
               PortalNodeURL childURL = jresponse.createRenderURL(child);
      %>
      <td valign="top" class="portlet-section-body">
         <table width="100%" cellpadding="0" cellspacing="0">
            <tr>
               <td colspan="2">
                  <a href="<%= childURL %>"><b><%= child.getName() %>
                  </b></a>
               </td>
            </tr>

            <%
               for (Iterator k = child.getChildren().iterator(); k.hasNext();)
               {
                  PortalNode subChild = (PortalNode)k.next();
                  if (subChild.getType() == PortalNode.TYPE_PAGE)
                  {
                     // child page
                     PortalNodeURL subchildURL = jresponse.createRenderURL(subChild);
            %>
            <tr>
               <td><img src="<%= contextPath %>/images/catalog/T.gif"/></td>
               <td><a href="<%= subchildURL %>"><%= subChild.getName() %>
               </a></td>
            </tr>
            <%
                  }
               }// for children
            %>
            <%
               }
            %>
         </table>
         <%

            } // for parents
            if(colCount % 3 == 0)
            {

         %>
   </tr>
   <%
         }
      }
   %>
</table>