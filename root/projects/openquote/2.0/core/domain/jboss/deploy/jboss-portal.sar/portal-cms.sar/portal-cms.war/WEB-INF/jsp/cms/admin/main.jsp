<%@ page import="org.jboss.portal.cms.model.File" %>
<%@ page import="org.jboss.portal.cms.model.Folder" %>
<%@ page import="org.jboss.portal.core.cms.ui.admin.CMSAdminConstants" %>
<%@ page import="java.text.Format" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.StringTokenizer" %>
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ page isELIgnored="false" %>

<portlet:defineObjects/>

<link rel="stylesheet" type="text/css" href="/portal-admin/css/style.css" media="screen"/>
<%
   String sCurrPath = (String)request.getAttribute("currpath");
   List folders = (List)request.getAttribute("folders");
   List files = (List)request.getAttribute("files");
   String createDate = "";
   String modifiedDate = "";
   Boolean manageWorkflowAccessible = (Boolean)request.getAttribute("manageWorkflowAccessible");
%>

<div class="admin-ui">
<br/>
<h3 class="sectionTitle-blue">
   ${n:i18n("CMS_MANAGE")}
</h3>
<div class="cms-tab-container">


<!-- Currently browsing -->
<ul class="objectpath">
   <li class="pathItem"><a href="<portlet:renderURL>
      <portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/>
      <portlet:param name="path" value="/"/>
      </portlet:renderURL>">Home</a></li>


   <%
      StringTokenizer parser = new StringTokenizer(sCurrPath, "/");
      String sPathBuilder = "";
      while (parser.hasMoreTokens())
      {
         String sPathChunk = parser.nextToken();
         sPathBuilder += "/" + sPathChunk;
         if (parser.hasMoreTokens())
         {
   %>
   <li class="pathSeperator"><img src="/portal-admin/img/pathSeparator.png" alt=">"></li>
   <li class="pathItem"><a href="
<portlet:renderURL>
   <portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/>
   <portlet:param name="path" value="<%= sPathBuilder %>"/>
</portlet:renderURL>
"><%= sPathChunk %>
   </a>
   </li>
   <%
   }
   else
   {
   %>
   <li class="pathSeperator"><img src="/portal-admin/img/pathSeparator.png" alt=">"></li>
   <li class="selected"><%= sPathChunk %>
   </li>
   <%
         }
      }
   %>
</ul>
<br/>
<!-- folder-level action dropdown -->
<div class="menu-container">
   <div class="menu">
      <select onchange="window.open(this.options[this.selectedIndex].value,'_top')">
         <option value="">Select Action...</option>
         <option value="<portlet:renderURL>
                     <portlet:param name="op" value="<%= CMSAdminConstants.OP_CONFIRM_CREATE_COLLECTION %>"/>
                     <portlet:param name="path" value="<%= sCurrPath %>"/>
                     </portlet:renderURL>">${n:i18n("CMS_CREATEFOLDER")}</option>
         <option value="<portlet:renderURL>
      <portlet:param name="op" value="<%= CMSAdminConstants.OP_CREATENEWTEXT %>"/>
      <portlet:param name="path" value="<%= sCurrPath %>"/>
      </portlet:renderURL>">${n:i18n("CMS_CREATEFILE")}</option>
         <option value="<portlet:renderURL>
      <portlet:param name="op" value="<%= CMSAdminConstants.OP_UPLOADCONFIRM %>"/>
      <portlet:param name="path" value="<%= sCurrPath %>"/>
      </portlet:renderURL>">${n:i18n("TITLE_UPLOAD")}</option>
         <option value="<portlet:renderURL>
      <portlet:param name="op" value="<%= CMSAdminConstants.OP_UPLOADARCHIVECONFIRM %>"/>
      <portlet:param name="path" value="<%= sCurrPath %>"/>
      </portlet:renderURL>">${n:i18n("CMS_UPLOADARCHIVE")}</option>
         <option value="<portlet:renderURL>
      <portlet:param name="op" value="<%= CMSAdminConstants.OP_EXPORTARCHIVE %>"/>
      <portlet:param name="path" value="<%= sCurrPath %>"/>
      </portlet:renderURL>">${n:i18n("CMS_EXPORTARCHIVE")}</option>
         <option value="<portlet:renderURL>
      <portlet:param name="op" value="<%= CMSAdminConstants.OP_CONFIRMSECURE %>"/>
      <portlet:param name="path" value="<%= sCurrPath %>"/>
      <portlet:param name="returnOp" value="<%= CMSAdminConstants.OP_MAIN %>"/>
      </portlet:renderURL>">${n:i18n("CMS_SECURE")}</option>
         <%
            if (manageWorkflowAccessible.booleanValue())
            {
         %>
         <option value="<portlet:renderURL>
      <portlet:param name="op" value="<%= CMSAdminConstants.OP_VIEWPENDING %>"/>
      <portlet:param name="path" value="<%= sCurrPath %>"/>
      </portlet:renderURL>">${n:i18n("CMS_APPROVAL")}</option>
         <%}%>
         <%
            if (!"/".equals(sCurrPath))
            {
         %>
         <option value="<portlet:renderURL>
      <portlet:param name="op" value="<%= CMSAdminConstants.OP_CONFIRMCOPY %>"/>
      <portlet:param name="path" value="<%= sCurrPath %>"/>
      <portlet:param name="type" value="fo"/>
      </portlet:renderURL>">${n:i18n("CMS_COPY")}</option>
         <option value="<portlet:renderURL>
      <portlet:param name="op" value="<%= CMSAdminConstants.OP_CONFIRMMOVE %>"/>
      <portlet:param name="path" value="<%= sCurrPath %>"/>
      <portlet:param name="type" value="fo"/>
      </portlet:renderURL>">${n:i18n("CMS_MOVE")}</option>
         <option value="<portlet:renderURL>
      <portlet:param name="op" value="<%= CMSAdminConstants.OP_CONFIRMDELETE %>"/>
      <portlet:param name="path" value="<%= sCurrPath %>"/>
      </portlet:renderURL>">${n:i18n("CMS_DELETE")}</option>
         <% } %>
      </select>

   </div>

</div>

<div class="search-container">
   <form method="post" action="<portlet:actionURL>
    <portlet:param name="op" value="<%= CMSAdminConstants.OP_DOSEARCH %>"/>
    </portlet:actionURL>">

      <input type="text"
             size="15"
             maxlength="80"
             name="search"
             class="portlet-form-input-field"/>
      <input type="submit" name="search" value="${n:i18n("CMS_SEARCH")}" class="portlet-form-button"/>

   </form>
</div>
<br style="clear:both"/>

<div class="file-table-container">
<%

   if (folders.size() > 0 || files.size() > 0)
   {

%>

<table width="100%" border="0" cellspacing="2" cellpadding="2">
<tr>
   <td class="portlet-section-header">${n:i18n("CMS_NAME")}</td>
   <td class="portlet-section-header">${n:i18n("CMS_ACTION")}</td>
   <td class="portlet-section-header">${n:i18n("CMS_CREATED")}</td>
   <td class="portlet-section-header">${n:i18n("CMS_MODIFIED")}</td>
</tr>

<%
   if (folders.size() > 0)
   {
      for (int i = 0; i < folders.size(); i++)
      {
         Folder folder = (Folder)folders.get(i);
%>
<tr onmouseover="this.className='portlet-section-alternate';" onmouseout="this.className='portlet-section-body';">
   <td><img
      src="<%= renderRequest.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH%>/folder.gif"
      alt="${n:i18n("CMS_FOLDER")}"
      border="0">&nbsp;<a href="<portlet:renderURL>
          <portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/>
          <portlet:param name="path" value="<%= folder.getBasePath() %>"/>
        </portlet:renderURL>"><%=
   folder.getBasePath().substring(folder.getBasePath().lastIndexOf("/") + 1, folder.getBasePath().length()) %>
   </a>
   </td>
   <td>
      <form method="POST" style="padding:0;margin:0;" action="<portlet:actionURL>
    <portlet:param name="path" value="<%= folder.getBasePath() %>"/>
    <portlet:param name="type" value="fo"/>
    <portlet:param name="dispatch" value="1"/>
   </portlet:actionURL>">
         <select name="op">
            <option value="<%= CMSAdminConstants.OP_MAIN %>">${n:i18n("CMS_VIEW")}</option>
            <option value="<%= CMSAdminConstants.OP_CONFIRMCOPY %>">${n:i18n("CMS_COPY")}</option>
            <option value="<%= CMSAdminConstants.OP_CONFIRMMOVE %>">${n:i18n("CMS_MOVE")}</option>
            <option value="<%= CMSAdminConstants.OP_CONFIRMDELETE %>">${n:i18n("CMS_DELETE")}</option>
         </select>
         <input type="submit" value="Go" name="Go" class="portlet-form-button"/>
      </form>
   </td>
   <td>
      <%
         if (folder.getCreationDate() != null)
         {
            Format formatter;
            formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
            createDate = formatter.format(folder.getCreationDate());
         }
      %>
      <%= createDate %>
   </td>
   <td>
      <%
         if (folder.getLastModified() != null)
         {
            Format formatter;
            formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
            modifiedDate = formatter.format(folder.getLastModified());
         }
      %>
      <%= modifiedDate %>
   </td>
</tr>
<%
      }
   }
%>

<%
   if (files.size() > 0)
   {
      for (int j = 0; j < files.size(); j++)
      {
         File file = (File)files.get(j);
%>
<tr onmouseover="this.className='portlet-section-alternate';" onmouseout="this.className='portlet-section-body';">
   <td><img src="<%= renderRequest.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH%>/file.gif"
            alt="${n:i18n("CMS_FILE")}"
            border="0">&nbsp;<a href="<portlet:renderURL>
          <portlet:param name="op" value="<%= CMSAdminConstants.OP_VIEWFILE %>"/>
          <portlet:param name="path"
            value="<%= file.getBasePath() %>"/>
        </portlet:renderURL>"><%=
   file.getBasePath().substring(file.getBasePath().lastIndexOf("/") + 1, file.getBasePath().length()) %>
   </a>
   </td>
   <td>
      <form method="POST" style="padding:0;margin:0;" action="<portlet:actionURL>
    <portlet:param name="path" value="<%= file.getBasePath() %>"/>
    <portlet:param name="type" value="fi"/>
    <portlet:param name="dispatch" value="1"/>
   </portlet:actionURL>">
         <select name="op">
            <option value="<%= CMSAdminConstants.OP_VIEWFILE %>">${n:i18n("CMS_VIEW")}</option>
            <option value="<%= CMSAdminConstants.OP_CONFIRMCOPY %>">${n:i18n("CMS_COPY")}</option>
            <option value="<%= CMSAdminConstants.OP_CONFIRMMOVE %>">${n:i18n("CMS_MOVE")}</option>
            <option value="<%= CMSAdminConstants.OP_CONFIRMDELETE %>">${n:i18n("CMS_DELETE")}</option>
         </select>
         <input type="submit" value="Go" name="Go" class="portlet-form-button"/>
      </form>
   </td>
   <td>
      <%
         if (file.getCreationDate() != null)
         {
            Format formatter;
            formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
            createDate = formatter.format(file.getCreationDate());
         }
      %>
      <%= createDate %>
   </td>
   <td>
      <%
         if (file.getLastModified() != null)
         {
            Format formatter;
            formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
            modifiedDate = formatter.format(file.getLastModified());
         }
      %>
      <%= modifiedDate %>
   </td>
</tr>
<%
   }
}
else
{%>

<tr>
   <td>
      <p>This folder is empty.</p>
   </td>
</tr>

<%
   }
%>
</table>
<%

   }

%>
</div>
</div>
</div>