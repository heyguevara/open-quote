<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ page isELIgnored="false" %>


<span><b>${n:i18n("EDITROLESFORUSER")}: "${n:out("username")}"</b>
   <br/>
   <br/>
   <table>
      <tr>
         <th>${n:i18n("ROLESAVAILABLE")}</th>
         <th/>
         <th>${n:i18n("ASSIGNEDROLES")}</th>
      </tr>
      <form name="<portlet:namespace/>editRoles" method="post"
            action="<portlet:actionURL><portlet:param name="op" value="addRolesToUser"/></portlet:actionURL>">
         <input type="hidden" name="userid" value="${n:out("userid")}"/>

         <input type="hidden" name="usernamefilter" value="${n:out("usernamefilter")}"/>
         <input type="hidden" name="offset" value="${n:out("offset")}"/>
         <input type="hidden" name="usersperpage" value="${n:out("usersperpage")}"/>

         <n:iterate ctx="userRoles">
            <input type="hidden" name="assignedRoles" value="${n:out("userRoles.name")}"/>
         </n:iterate>

         <tr>
            <td style="vertical-align:top;">
               <select name="rolesToAdd" size="6" multiple="true">
                  <n:iterate ctx="allRoles">
                     <option value="${n:out("allRoles.name")}">${n:out("allRoles.displayname")}</option>
                  </n:iterate>
               </select>
            </td>
            <td>
               <input name="addRoles" class="portlet-form-button" type="submit" value="${n:i18n("ROLE_ADD")}"/>
               <br/>
               <input name="removeRoles" class="portlet-form-button" type="submit" value="${n:i18n("ROLE_REMOVE")}"/>
            </td>
            <td style="vertical-align:top;">
               <select name="selectedRoles" size="6" multiple="true">
                  <n:iterate ctx="userRoles">
                     <option value="${n:out("userRoles.name")}">${n:out("userRoles.displayname")}</option>
                  </n:iterate>
               </select>
            </td>
         </tr>


      </form>

      <tr>
         <td><br/></td>
      </tr>
      <tr>
         <td colspan="3">
            <input name="Save" class="portlet-form-button" id="Assign" type="submit"
                   onclick="document.forms['<portlet:namespace/>editRoles'].submit();"
                   value="${n:i18n("ASSIGNROLES")}"/>
            &nbsp;
            <input name="Cancel" class="portlet-form-button" id="Cancel" type="submit"
                   onclick="document.forms['<portlet:namespace/>cancel'].submit();"
                   value="${n:i18n("Cancel")}"/>
      </tr>
      <form
         name="<portlet:namespace/>cancel"
         action="<portlet:actionURL windowState="normal">
            <portlet:param name="op" value="showListUsers"/>
            <portlet:param name="usernamefilter" value="${n:out('usernamefilter')}"/>
            <portlet:param name="usersperpage" value="${n:out('usersperpage')}"/>
            <portlet:param name="offset" value="${n:out('offset')}"/>
         </portlet:actionURL>"
         method="post"/>
   </table>
</span>
