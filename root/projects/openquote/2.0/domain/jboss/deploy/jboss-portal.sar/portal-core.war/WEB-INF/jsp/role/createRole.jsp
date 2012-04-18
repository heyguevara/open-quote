<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ page isELIgnored="false" %>
<portlet:defineObjects/>

<form name="createRole" action="<portlet:actionURL><portlet:param name="op" value="createRole"/></portlet:actionURL>"
      method="post">
   <table border="0" class="portlet-font" cellspacing="0" cellpadding="2">
      <tr>
         <td colspan="2" class="portlet-section-alternate">
            <img border="0" src="<%= renderRequest.getContextPath() %>/images/role/role.gif" align="absmiddle"/>&nbsp;
            ${n:i18n("ROLE_CREATE")}</td>
      </tr>
      <tr>
         <td class="portlet-section-body">${n:i18n("ROLE_NAME")}:</td>
         <td class="portlet-section-body"><input class="portlet-form-input-field" type="text" name="rolename"
                                                 value="${param["rolename"]}" size="21" maxlength="25"> <n:error
            key="rolename_error"/></td>
      </tr>
      <tr>
         <td class="portlet-section-body">${n:i18n("ROLE_DISPLAYNAME")}:</td>
         <td class="portlet-section-body"><input class="portlet-form-input-field" type="text" name="roledisplayname"
                                                 value="${param["roledisplayname"]}" size="21" maxlength="25"> <n:error
            key="roledisplayname_error"/></td>
      </tr>
      <tr>
         <td colspan="2" align="center">
            <input name="createRole" type="submit" value="${n:i18n("ROLE_CREATE")}" class="portlet-form-button">
         </td>
      </tr>
   </table>
</form>
