<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ page isELIgnored="false" %>

<portlet:defineObjects/>

<h2>CMS Portlet - Help</h2>

<h2>Introduction</h2>

<p class="portlet-font">The CMS Portlet displays content from the file store inside a portlet window, or, in the case of
   binary content, outside of the portlet
   window altogether.</p>

<p class="portlet-font">To modify how this portlet behaves, please
   <a href="<portlet:renderURL portletMode="edit"></portlet:renderURL>">click here.</a></p>
