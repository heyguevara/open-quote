<%@ page import="org.jboss.portal.core.CoreConstants" %>
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ page isELIgnored="false" %>
<portlet:defineObjects/>

<span class="portlet-font"><a
   href="<portlet:renderURL windowState="normal"><portlet:param name="op" value="showMenu"/></portlet:renderURL>">${n:i18n("REGISTER_SHOWMENU")}</a></span>
<br/>

<div class="box" align="center">
   <table border="0" class="portlet-font" cellspacing="0" cellpadding="2">
      <tr>
         <td align="center">${n:i18n("REGISTER_CONFIRM")}
         </td>
      </tr>
   </table>
</div>