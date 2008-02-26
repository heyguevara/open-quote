<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ page isELIgnored="false" %>

<%
   String sCurrPath = (String)request.getAttribute("createpath");
   String OP = CMSAdminConstants.OP_CONFIRM_CREATE_COLLECTION;
%>
<portlet:defineObjects/>
<link rel="stylesheet" type="text/css" href="/portal-admin/css/style.css" media="screen"/>
<div class="admin-ui">
   <br/>
   <h3 class="sectionTitle-blue">${n:i18n("TITLE_CREATECOLLCONFIRM")}</h3>
   <div class=" cms-tab-container">
      <table width="100%">
         <tr>
            <td valign="top" width="250" class="portlet-section-alternate">
               <%@ include file="folderlist.jsp" %>
            </td>
            <td class="portlet-section-body" align="left">
               <form name="pickform" action="<portlet:actionURL>
    <portlet:param name="op" value="<%= CMSAdminConstants.OP_CREATE_COLLECTION %>"/>
    <portlet:param name="createpath" value="<%= sCurrPath %>"/>
    </portlet:actionURL>" method="post">
                  <input type="hidden" name="destination" value="<%= sCurrPath %>">
                  <table>
                     <tr>
                        <td valign="bottom">
                           Destination:
                        </td>
                        <td>
                           <input DISABLED type="text" size="40" name="showdestination" value="<%= sCurrPath %>"
                                  class="portlet-form-input-field"/></td>
                     </tr>
                     <tr>
                        <td valign="bottom">${n:i18n("CMS_NAME")}:</td>
                        <td align="left"><input class="portlet-form-input-field" type="text"
                                                name="newcollectionname" size="40" maxlength="50">
                        </td>
                     </tr>
                     <tr>
                        <td valign="bottom">${n:i18n("CMS_DESCRIPTION")}:</td>
                        <td align="left"><input class="portlet-form-input-field" type="text"
                                                name="newcollectiondescription"
                                                size="40" maxlength="80">
                        </td>
                     </tr>
                     <tr>
                        <td></td>
                        <td>
                           <input class="portlet-form-button" type="submit" value="${n:i18n("CMS_CREATE")}"
                                  name="submit">
                           <input class="portlet-form-button" type="button" value="${n:i18n("CMS_CANCEL")}"
                                  name="cancel"
                                  onclick="window.location='<portlet:renderURL><portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/><portlet:param name="path" value="<%= sCurrPath %>"/></portlet:renderURL>'">
                        </td>
                     </tr>
                  </table>
               </form>
            </td>
         </tr>
      </table>
      <br/><br/>
   </div>
</div>




