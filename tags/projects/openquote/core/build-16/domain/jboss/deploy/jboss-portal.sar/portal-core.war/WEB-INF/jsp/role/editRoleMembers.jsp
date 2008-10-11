<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ page isELIgnored="false" %>
<portlet:defineObjects/>

<div align="center">
   <form id="editRoleMembers"
         action="<portlet:actionURL><portlet:param name="op" value="showListEditRoleMembers"/></portlet:actionURL>"
         method="post">
      <table border="0" class="portlet-font" cellspacing="0" cellpadding="5">
         <tr>
            <td colspan="2" class="portlet-section-alternate" align="left">
               <img border="0" src="<%= renderRequest.getContextPath() %>/images/role/role.gif" align="absmiddle"/>&nbsp;
               ${n:i18n("MENU_EDITROLEMEMBERS")}</td>
         </tr>
         <tr>
            <td class="portlet-section-body">${n:i18n("ROLE_SELECTONETOMODIFY")}:</td>
            <td class="portlet-section-body">
               <select name="roleid" class="portlet-form-input-field">
                  <n:iterate ctx="role">
                     <option value="${n:out("role.id")}" ${n:out("role.selected")}>${n:out("role.displayname")}</option>
                  </n:iterate>
               </select> <n:error key="roledelete_error"/>
            </td>
         </tr>
         <tr>
            <td class="portlet-section-body">${n:i18n("LIST_USERNAMECONTAINS")}:</td>
            <td class="portlet-section-body" align="left">
               <input type="text" name="usernamefilter" value="${n:out("usernamefilter")}" size="15"
                      class="portlet-form-input-field"/>
            </td>
         </tr>
         <tr>
            <td class="portlet-section-body">${n:i18n("LIST_USERSPERPAGE")}:</td>
            <td class="portlet-section-body" align="left">
               <select name="usersperpage" class="portlet-form-input-field">
                  <option value="10">10</option>
                  <option value="20">20</option>
                  <option value="30">30</option>
                  <option value="50">50</option>
                  <option value="75">75</option>
                  <option value="100">100</option>
               </select>
            </td>
         </tr>
         <tr>
            <td colspan="2" align="center" class="portlet-section-body">
               <input type="submit" value="${n:i18n("ROLE_EDIT_MEMBERS")}" class="portlet-form-button">
            </td>
         </tr>
      </table>
   </form>
</div>
