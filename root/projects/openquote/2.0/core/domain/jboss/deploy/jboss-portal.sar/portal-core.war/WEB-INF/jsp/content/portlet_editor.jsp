<%@ page import="org.jboss.portal.common.util.IteratorStatus" %>
<%@ page import="org.jboss.portal.core.model.instance.Instance" %>
<%@ page import="org.jboss.portal.portlet.Portlet" %>
<%@ page import="org.jboss.portal.portlet.PortletInvokerException" %>
<%@ page import="org.jboss.portal.portlet.info.MetaInfo" %>
<%@ page import="java.util.Collection" %>
<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<%
   Collection instances = (Collection)request.getAttribute("INSTANCES");
   Instance selectedInstance = (Instance)request.getAttribute("SELECTED_INSTANCE");
%>
<%@page import="org.jboss.portal.portlet.info.PortletInfo"%>
<%@page import="org.jboss.portal.core.portlet.info.CorePortletInfo"%>
<%@page import="org.jboss.portal.core.portlet.info.PortletInfoInfo"%>
<%@page import="org.jboss.portal.core.portlet.info.PortletIconInfo"%>
<%@page import="org.jboss.portal.core.ui.content.portlet.PortletContentEditorPortlet"%>
<portlet:defineObjects/>

<span class="portlet-font">Portlet instance associated to this window:</span>

<div style="height:300px; width:300px; overflow: auto; overflow-x: hidden; border: 1px solid #333;">
   <table style="width:100%;" cellspacing="0" cellpadding="0">
      <%
         for (IteratorStatus i = new IteratorStatus(instances); i.hasNext();)
         {
            Instance instance = (Instance)i.next();
            String rowClass = instance == selectedInstance ? "portlet-section-selected" : (i.getIndex() % 2 == 0 ? "portlet-section-body" : "portlet-section-alternate");
            String displayName = instance.getDisplayName().getString(renderRequest.getLocale(), true);
            if (displayName == null)
            {
               displayName = instance.getId();
            }
            
            PortletInfo info = instance.getPortlet().getInfo();

            String iconLocation = null;
            if (info instanceof CorePortletInfo)
            {
               CorePortletInfo cInfo = (CorePortletInfo)info;
               PortletInfoInfo portletInfo = cInfo.getPortletInfo();
               if (portletInfo != null && portletInfo.getPortletIconInfo() != null && portletInfo.getPortletIconInfo().getIconLocation(PortletIconInfo.SMALL) != null)
               {
                  iconLocation = portletInfo.getPortletIconInfo().getIconLocation(PortletIconInfo.SMALL);
               }
            }
            if (iconLocation == null)
            {
               iconLocation = PortletContentEditorPortlet.DEFAULT_PORTLET_ICON;
            }

      %>
      <portlet:actionURL var="url">
         <portlet:param name="content.action.select" value="true"/>
         <portlet:param name="content.uri" value="<%= instance.getId() %>"/>
      </portlet:actionURL>
      <tr class="<%= rowClass %>">
         <td>
         <img src="<%= iconLocation %>" align="middle" style="margin:0 4px 0 0"/>
         <a href="<%= url %>"><%= displayName %></a></td>
      </tr>
      <%
         }
      %>
   </table>
</div>

<%
   if (selectedInstance != null)
   {
      String displayName = selectedInstance.getDisplayName().getString(renderRequest.getLocale(), true);
      if (displayName == null)
      {
         displayName = selectedInstance.getId();
      }
%>
<div class="portlet-font">
   <div><span class="portlet-form-field-label">Portlet instance:</span><%= displayName %>
   </div>
   <%






      Portlet portlet = null;
      try
      {
         portlet = selectedInstance.getPortlet();
      }
      catch (PortletInvokerException e)
      {
         e.printStackTrace();
      }
      if (portlet != null)
      {
         MetaInfo metaInfo = portlet.getInfo().getMeta();






   %>
   <div><span
      class="portlet-form-field-label">Portlet name:</span><%= metaInfo.getMetaValue(MetaInfo.DISPLAY_NAME).getDefaultString() %>
   </div>
   <div><span
      class="portlet-form-field-label">Portlet description:</span><%= metaInfo.getMetaValue(MetaInfo.DESCRIPTION).getDefaultString() %>
   </div>
   <div><span
      class="portlet-form-field-label">Portlet title:</span><%= metaInfo.getMetaValue(MetaInfo.TITLE).getDefaultString() %>
   </div>
   <div><span
      class="portlet-form-field-label">Portlet keywords:</span><%= metaInfo.getMetaValue(MetaInfo.KEYWORDS).getDefaultString() %>
   </div>
   <%






         }
      }






   %>
