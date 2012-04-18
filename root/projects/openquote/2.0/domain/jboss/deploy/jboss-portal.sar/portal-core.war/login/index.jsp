<%@ page import="org.jboss.portal.common.util.Tools"%><html>
<head/>
<body>
<form method="POST" action="j_security_check">
<%--
<%

   String redirect = request.getParameter("redirect");
   if (redirect != null)
   {
      %>
<input type="hidden" name="redirect" value="<%= Tools.createXWWWFormURLEncoded(redirect) %>"/>
      <%
   }
%>
--%>
<input type="text" name="j_username" value=""/>
<input type="password" name="j_password" value=""/>
<input type="submit"/>
</form>
</body>
</html>
