<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ page isELIgnored="false" %>
<portlet:defineObjects/>

<p>${n:i18n("ROLE_EDIT_USER")}: ${n:out("displayname")}</p>

<hr/>

<p></p>

<table width="100%" border="0" cellspacing="0" class="portlet-table-body">
   <tr>
      <td nowrap="nowrap" class="portlet-table-header">${n:i18n("LIST_FULLNAME")}</td>
      <td nowrap="nowrap" class="portlet-table-header">${n:i18n("LIST_USERNAME")}</td>
      <td nowrap="nowrap" class="portlet-table-header">${n:i18n("LIST_ROLES")}</td>
      <td nowrap="nowrap" class="portlet-table-header">${n:i18n("LIST_ACTIONS")}</td>
   </tr>
   <n:iterate ctx="row">
      <tr class="portlet-section-body">
         <td nowrap="nowrap" class="portlet-table-text">${n:out("row.fullname")}</td>
         <td nowrap="nowrap" class="portlet-table-text">${n:out("row.username")}</td>
         <td nowrap="nowrap" class="portlet-table-text">
            <n:iterate ctx="roles">${n:out("row.roles.name")}<br/></n:iterate>
         </td>
         <td nowrap="nowrap" class="portlet-table-text"><a
            href="${n:out("row.editURL")}">${n:i18n("LIST_ACTIONEDITROLES")}</a></td>
      </tr>
   </n:iterate>
</table>

<div align="center">
   <table width="0%" border="0" cellspacing="0" cellpadding="0" class="pagination">
      <tr>
         <td nowrap>
            <ul>
               <n:if ctx="previouspage">
                  <li class="previouspage">
                     <a href="${n:out("previouspage.link")}">${n:i18n("PREVIOUSPAGE")}</a>
                  </li>
               </n:if>
               <n:if ctx="nextpage">
                  <li class="nextpage">
                     <a href="${n:out("nextpage.link")}">${n:i18n("NEXTPAGE")}</a>
                  </li>
               </n:if>
            </ul>
         </td>
      </tr>
   </table>
</div>

<br/>

<form
   name="<portlet:namespace/>editListRoleMembers"
   action="<portlet:actionURL><portlet:param name="op" value="showListEditRoleMembers"/></portlet:actionURL>"
   method="post">
   <font class="portlet-form-label">${n:i18n("MENU_EDITROLEMEMBERS")}</font><br/>
   <input type="text" name="usernamefilter" value="${n:out("usernamefilter")}" size="15"/>
   <select name="roleid" class="portlet-form-input-field">
      <n:iterate ctx="rolelist">
         <option value="${n:out("rolelist.id")}" ${n:out("rolelist.selected")}>${n:out("rolelist.displayname")}</option>
      </n:iterate>
   </select>
   <select name="usersperpage">
      <option value="10">10</option>
      <option value="20">20</option>
      <option value="30">30</option>
      <option value="50">50</option>
      <option value="75">75</option>
      <option value="100">100</option>
   </select>
   <br/>
</form>
<p>
   <input name="Find" class="portlet-form-button" id="Save" type="submit"
          onclick="document.forms['<portlet:namespace/>editListRoleMembers'].submit();"
          value="${n:i18n("Search")}"/>
   &nbsp;
   <input name="Cancel" class="portlet-form-button" id="Cancel" type="submit"
          onclick="document.forms['<portlet:namespace/>cancelEditListRoleMembers'].submit();"
          value="${n:i18n("Cancel")}"/>
</p>

<form
   name="<portlet:namespace/>cancelEditListRoleMembers"
   action="<portlet:renderURL windowState="normal"><portlet:param name="op" value="showSummary"/></portlet:renderURL>"
   method="post"/>
