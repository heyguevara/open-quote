<%@ page import="org.jboss.portal.core.CoreConstants" %>
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ page isELIgnored="false" %>
<portlet:defineObjects/>

<div class="box" align="center">
   <table border="0" class="portlet-font" cellspacing="0" cellpadding="2">
      <tr>
         <td colspan="1">
             <span class="portlet-text">
                ${n:i18n("REGISTER_NOT_LOGGED_IN")}                 
             <br/><br/>
             <a id="register"
                href="<portlet:renderURL windowState="maximized"><portlet:param name="op" value="showRegister"/></portlet:renderURL>">${n:i18n("REGISTER_REGISTER")}</a></span>
         </td>
      </tr>
   </table>
</div>