<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ page isELIgnored="false" %>
<portlet:defineObjects/>

<div>
   <h6>${n:i18n("MENU_EDITROLE")}: ${n:out("editroledisplayname")}</h6>

   <form name="<portlet:namespace/>editRole"
         action="<portlet:actionURL><portlet:param name="op" value="editRole"/></portlet:actionURL>" method="post">
      <input type="hidden" name="roleid" value="${n:out("editroleid")}"/>

      <p>
         <label class="portlet-form-label">${n:i18n("ROLE_DISPLAYNAME")}: </label>
         <input name="roledisplayname" type="text" value="${param["editroledisplayname"]}" size="21" maxlength="25"/>
         <n:error key="roledisplayname_error"/>
      </p>
   </form>
   <form name="<portlet:namespace/>cancelEditRole"
         action="<portlet:renderURL windowState="normal"><portlet:param name="op" value="showSummary"/></portlet:renderURL>"
         method="post">
   </form>
   <p>
      <input name="Save" class="portlet-form-button" id="Save" type="submit"
             onclick="document.forms['<portlet:namespace/>editRole'].submit();"
             value="${n:i18n("SaveChanges")}"/>
      &nbsp;
      <input name="Cancel" class="portlet-form-button" id="Cancel" type="submit"
             onclick="document.forms['<portlet:namespace/>cancelEditRole'].submit();"
             value="${n:i18n("Cancel")}"/>
   </p>
</div>


