<%@ page import="org.jboss.portal.api.node.PortalNode" %>
<%@ page import="org.jboss.portal.api.node.PortalNodeURL" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<portlet:defineObjects/>

<%
   String contextPath = request.getContextPath();
   String parent = (String)request.getAttribute("parentNode");
   HashMap parents = (HashMap)request.getAttribute("parents");
   HashMap parentChildMap = (HashMap)request.getAttribute("pchild");
   HashMap parentSiblingMap = (HashMap)request.getAttribute("psib");
%>
<style>

   <!--
   .foldinglist {
      list-style-position: outside;
      margin-left: 20px;
      vertical-align: top;
      list-style-image: url( <%= contextPath %> /images/catalog/bullet.gif );
   }

   .pagenosub {
      margin-top: 5px;
      margin-left: 0px;
      font-weight: bold;
      list-style-image: url( <%= contextPath %> /images/catalog/bullet.gif );
   }

   /
   /
   -->
</style>

<%
   String output = new String();
   for (Iterator i = parents.keySet().iterator(); i.hasNext();)
   {
      String name = (String)i.next();
      PortalNodeURL nodeURL = (PortalNodeURL)parents.get(name);
      output = "<a href=\"" + nodeURL.toString() + "\">" + name + "</a> &gt; " + output;
   }
   output += parent;
%>

<div class="portlet-area-body">
    
<div class="portlet-form-field-label"><%= output %>
</div>

<ul id="foldinglist">
   <li class="pagenosub">
      <div class="portlet-form-field-label"><%= parent %>
      </div>
   </li>
   <%
      for (Iterator i = parentChildMap.keySet().iterator(); i.hasNext();)
      {
         String childName = (String)i.next();
         PortalNodeURL childURL = (PortalNodeURL)parentChildMap.get(childName);
   %>
   <li class="foldinglist">
      <div class="portlet-form-field-label"><a
         href="<%= childURL.toString() %>"><%= childName %>
      </a></div>
   </li>
   <%
      }

      for (Iterator j = parentSiblingMap.keySet().iterator(); j.hasNext();)
      {
         String siblingName = (String)j.next();
         PortalNodeURL siblingURL = (PortalNodeURL)parentSiblingMap.get(siblingName);
   %>
   <li class="pagenosub">
      <div class="portlet-form-field-label"><a href="<%= siblingURL.toString() %>"><%= siblingName %>
      </a></div>
   </li>
   <%
      }
   %>
</ul>
</div>