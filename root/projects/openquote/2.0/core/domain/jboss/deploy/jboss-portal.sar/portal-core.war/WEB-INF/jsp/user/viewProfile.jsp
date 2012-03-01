<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ page isELIgnored="false" %>

<table cellpadding="0" cellspacing="8" border="0" class="pn-normal">

   <tr>
      <td class="portlet-form-label">${n:i18n("LIST_USERNAME")}</td>
      <td>${n:out("username")}</td>
   </tr>
   <tr>
      <td class="portlet-form-label">${n:i18n("REGISTER_GIVENNAME")}</td>
      <td>${n:out("GIVENNAME")}</td>
   </tr>
   <tr>
      <td class="portlet-form-label">${n:i18n("REGISTER_FAMILYNAME")}</td>
      <td>${n:out("FAMILYNAME")}</td>
   </tr>
   <tr>
      <td class="portlet-form-label">${n:i18n("REGISTER_FAKEEMAIL")}</td>
      <td>${n:out("FAKEEMAIL")}</td>
   </tr>
   <tr>
      <td class="portlet-form-label">${n:i18n("REGISTER_HOMEPAGE")}</td>
      <td>${n:out("HOMEPAGE")}</td>
   </tr>
   <tr>
      <td class="portlet-form-label">${n:i18n("REGISTER_LOCATION")}</td>
      <td>${n:out("LOCATION")}</td>
   </tr>
</table>

<form
   name="<portlet:namespace/>cancel"
   action="<portlet:renderURL windowState="normal"><portlet:param name="op" value="showMenu"/></portlet:renderURL>"
   method="post"/>
