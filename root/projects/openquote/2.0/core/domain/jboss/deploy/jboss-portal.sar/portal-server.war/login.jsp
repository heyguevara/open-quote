<%--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  ~ JBoss, a division of Red Hat                                             ~
  ~ Copyright 2006, Red Hat Middleware, LLC, and individual                  ~
  ~ contributors as indicated by the @authors tag. See the                   ~
  ~ copyright.txt in the distribution for a full listing of                  ~
  ~ individual contributors.                                                 ~
  ~                                                                          ~
  ~ This is free software; you can redistribute it and/or modify it          ~
  ~ under the terms of the GNU Lesser General Public License as              ~
  ~ published by the Free Software Foundation; either version 2.1 of         ~
  ~ the License, or (at your option) any later version.                      ~
  ~                                                                          ~
  ~ This software is distributed in the hope that it will be useful,         ~
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of           ~
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU         ~
  ~ Lesser General Public License for more details.                          ~
  ~                                                                          ~
  ~ You should have received a copy of the GNU Lesser General Public         ~
  ~ License along with this software; if not, write to the Free              ~
  ~ Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA       ~
  ~ 02110-1301 USA, or see the FSF site: http://www.fsf.org.                 ~
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
   <style type="text/css">
      body {
         margin: 0;
         padding: 0;
         border: 0;
         padding-top: <%=(request.getParameter("loginheight") != null ? request.getParameter("loginheight") : "300px")%>;
      }

   </style>
   <link rel="stylesheet" href="/portal-core/css/login.css" type="text/css" />
</head>
<body OnLoad="document.loginform.j_username.focus();">

<div class="login-container">

   <div class="login-header">
      <h2>JBoss Portal Login</h2>
   </div>
   <div class="login-content">
      <div class="error-message"
           style="display:<%=(request.getAttribute("org.jboss.portal.loginError") != null ? "" : "none")%>;"><%=request.getAttribute("org.jboss.portal.loginError") %>
      </div>
      <form method="POST" action="<%= response.encodeURL("j_security_check") %>" name="loginform" id="loginForm"
            target="_parent">
         <label for="j_username">
            <div class="form-field">Username:
               <input type="text" name="j_username" id="j_username" value=""/></div>
         </label>
         <label for="j_password">
            <div class="form-field bottom-field">Password:
               <input type="password" name="j_password" id="j_password" value=""/></div>
         </label>
         <input style="display:<%=(request.getParameter("loginheight") != null ? "" : "none")%>;" type="button" name="cancel" value="Cancel" class="cancel-button" onclick="window.parent.hideContentModal('login-modal');"/>
         <input style="<%=(request.getParameter("loginheight") != null ? "" : "right:10px")%>;" type="submit" name="login" value="Login" class="login-button"/>
      </form>

   </div>
</div>
</body>
</html>
