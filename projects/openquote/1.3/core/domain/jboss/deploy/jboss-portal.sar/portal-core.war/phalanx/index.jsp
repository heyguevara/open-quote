<%@ taglib uri="/WEB-INF/theme-basic-lib.tld" prefix="basic" %>
<%@ page
      import="org.jboss.portal.server.PortalConstants" %>
<%@ page import="java.util.ResourceBundle"%>
<%@ page import="org.jboss.portal.theme.page.PageResult"%>
<%@ page import="org.jboss.portal.theme.LayoutConstants"%>

<%!
   /** Left region index. */
   private static final int LEFT = 0;

   /** Center region index. */
   private static final int CENTER = 1;

   private static ResourceBundle MODES;
   private static ResourceBundle WINDOW_STATES;

   static
   {
      MODES = ResourceBundle.getBundle("phalanx.Modes");
      WINDOW_STATES = ResourceBundle.getBundle("phalanx.WindowStates");
   }
%>

<%
   String contextPath = request.getContextPath();
   String left = "left";
   String center = "center";
   boolean leftIsEmpty = false;
   boolean centerIsEmpty = false;
   boolean maximized = false;

   PageResult pageResult = (PageResult)request.getAttribute(LayoutConstants.ATTR_PAGE);
   if ("maximized".equals(pageResult.getLayoutState()))
   {
      center = "maximized";
      maximized = true;
   }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
   <title><%= PortalConstants.VERSION %></title>
   <meta http-equiv="Content-Type" content="<%= response.getContentType() %>"/>
   <link rel="shortcut icon" href="<%= contextPath %>/phalanx/images/favicon.ico"/>
   <link href="<%= contextPath %>/phalanx/css/portal_style.css" rel="stylesheet" type="text/css" />
   <!--<link href="/portal-forums/subSilver/styles.css" rel="stylesheet" type="text/css" />-->
</head>

<body bgcolor="#E7E7E7">

<div align="center">
   <table cellspacing="0" cellpadding="0" width="95%">
      <tr>
         <td colspan="3"><img src="<%= contextPath %>/phalanx/images/page_border_top.gif" width="100%" height="3"></td>
      </tr>
      <tr>
         <td background="<%= contextPath %>/phalanx/images/page_border_left.gif"><img
               src="<%= contextPath %>/phalanx/images/page_border_left.gif" height="100%" width="3"></td>
         <td background="<%= contextPath %>/phalanx/images/header_bg.gif" height="126" width="100%">
            <!-- begin main table -->
            <table width="100%" cellspacing="0" cellpadding="0">
               <tr>
                  <td height="126" align="left"><img src="<%= contextPath %>/phalanx/images/logo.gif" border="0" alt="JBoss Portal - The Open Source Portal"></td>
                  <td align="right"><img src="<%= contextPath %>/phalanx/images/header_right.gif"></td>
               </tr>
               <tr>
                  <td colspan="2"><img src="<%= contextPath %>/phalanx/images/header_border_bottom.gif" width="100%" height="3"></td>
               </tr>
               <tr>
                  <td colspan="2">
                     <!-- main content space -->
                     <table width="100%" bgcolor="#FFFFFF" cellspacing="0" cellpadding="5">
                        <tr>
                           <!-- left region -->
                           <%
                              if (maximized)
                              {
                           %>
                           <td width="100%" valign="top">
                              <basic:forEachWindowInRegion region="<%= center %>">
                                 <%@ include file="window.jsp" %>
                              </basic:forEachWindowInRegion>
                           </td>
                           <% }  else { %>
                           <td width="30%" valign="top">
                           <%
                           if (!leftIsEmpty) {
                           %>
                              <basic:forEachWindowInRegion region="<%= left %>">
                                 <%@ include file="window.jsp" %>
                              </basic:forEachWindowInRegion>
                           <% } %>
                           </td>
                           <!-- center region -->
                           <td width="70%" valign="top">
                           <%
                           if (!centerIsEmpty) {
                           %>
                              <basic:forEachWindowInRegion region="<%= center %>">
                                 <%@ include file="window.jsp" %>
                              </basic:forEachWindowInRegion>
                           <% } %>
                           </td>
                           <% } %>
                        </tr>
                     </table>
                  </td>
               </tr>
            </table>
            <!-- end main table -->
         </td>
         <td background="<%= contextPath %>/phalanx/images/page_border_right.gif"><img
               src="<%= contextPath %>/phalanx/images/page_border_right.gif" height="100%" width="3"></td>
      </tr>
      <tr>
         <td colspan="3"><img src="<%= contextPath %>/phalanx/images/page_border_bottom.gif" width="100%" height="3"></td>
      </tr>
   </table>

   <!-- footer table -->
   <table cellspacing="0" cellpadding="0" border="0" width="95%">
      <tr>
         <td align="left"><div class="portlet-font-dim" style="font-size:10px">
            Copyright 2005, JBoss Inc.<br>
            Powered by <a href="http://www.jboss.com/products/jbossportal" target="_blank">JBoss Portal</a>
            </div></td>
      </tr>
   </table>
</div>
<br>
</body>
</html>