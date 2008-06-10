<%@ page import="org.jboss.portal.api.PortalURL" %>
<%@ page import="org.jboss.portal.identity.User" %>

<%
   User user = (User)request.getAttribute("org.jboss.portal.header.USER");
   PortalURL dashboardURL = (PortalURL)request.getAttribute("org.jboss.portal.header.DASHBOARD_URL");
   PortalURL loginURL = (PortalURL)request.getAttribute("org.jboss.portal.header.LOGIN_URL");
   PortalURL defaultPortalURL = (PortalURL)request.getAttribute("org.jboss.portal.header.DEFAULT_PORTAL_URL");
   PortalURL adminPortalURL = (PortalURL)request.getAttribute("org.jboss.portal.header.ADMIN_PORTAL_URL");
   PortalURL editDashboardURL = (PortalURL)request.getAttribute("org.jboss.portal.header.EDIT_DASHBOARD_URL");
   PortalURL copyToDashboardURL = (PortalURL)request.getAttribute("org.jboss.portal.header.COPY_TO_DASHBOARD_URL");
   PortalURL signOutURL = (PortalURL)request.getAttribute("org.jboss.portal.header.SIGN_OUT_URL");
%>

<%
   if (user == null)
   {
%>
<script type="text/javascript">
    if (typeof isModalLoaded != 'undefined'){
        document.write('<a href=\"#\" onclick=\"alertModal(\'login-modal\',\'login-modal-msg\');return false;\">Login</a>');
    }else{
        document.write('<a href=\"<%= loginURL %>\">Login</a>');
    }
   //set the iframe src for login modal to requested URL
   var iframeSrc = '<%= loginURL %>' + '?loginheight=0';
   document.getElementById('loginIframe').src = iframeSrc;
</script>

<noscript>
      <a href="<%= loginURL %>">Login</a>
</noscript>


<%
}
else
{
%>
Logged in as: <%= user.getUserName() %><br/><br/>

<%
   if (dashboardURL != null)
   {
%>&nbsp;&nbsp;<a href="<%= dashboardURL %>">Dashboard</a>&nbsp;&nbsp;|<%
   }

   if (defaultPortalURL != null)
   {
%>&nbsp;&nbsp;<a href="<%= defaultPortalURL %>">Portal</a>&nbsp;&nbsp;|<%
   }

   if (adminPortalURL != null)
   {
%>&nbsp;&nbsp;<a href="<%= adminPortalURL %>">Admin</a>&nbsp;&nbsp;|<%
   }

   if (editDashboardURL != null)
   {
%>&nbsp;&nbsp;<a href="<%= editDashboardURL %>">Configure dashboard</a>&nbsp;&nbsp;|<%
   }

   if (copyToDashboardURL != null)
   {
%>&nbsp;&nbsp;<a href="<%= copyToDashboardURL %>">Copy to my dashboard</a>&nbsp;&nbsp;|<%
   }
%>&nbsp;&nbsp;<a href="<%= signOutURL %>">Logout</a>
<%
   }
%>
