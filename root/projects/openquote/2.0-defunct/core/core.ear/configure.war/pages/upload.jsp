<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page isELIgnored ="false" %>
<%@ page import="javax.portlet.RenderRequest,javax.portlet.PortletSession,javax.portlet.WindowState" %>
	
<portlet:defineObjects/>
	
<%
// get the "form" object from the portlet session in to a page attribute so that
// jstl can see it. 
RenderRequest pReq=(RenderRequest)request.getAttribute("javax.portlet.request");
PortletSession PSes=pReq.getPortletSession(true);
pageContext.setAttribute("form", PSes.getAttribute("form"));
pageContext.setAttribute("state", pReq.getWindowState().toString());
%>

<table width="100%">
	<c:choose>
		<c:when test="${state=='maximized'}">
			<c:set var="filenameLen" value="40"/>
			<th colspan="4" class="portlet-section-header">Configure Admin :: Configuration upload</th>
			<tr><td height="15" colspan="4"></td></tr>
		</c:when>
		<c:otherwise>
			<c:set var="filenameLen" value="15"/>
			<tr><td><table width="100%" class="portlet-section-alternate"><tr>
			<td><img src="<%= renderRequest.getContextPath()%>/images/upload.gif" align="absmiddle"/></td><td>Configuration upload</td>
			</tr></table></td></tr>
		</c:otherwise>
	</c:choose>		

	<tr class="portlet-section-body">
		<tr class="portlet-section-body">
			<td align="center" colspan="4">
				<br/>
				<form method="post"  enctype="multipart/form-data" action="<portlet:actionURL><portlet:param name="op" value="fetch"/></portlet:actionURL>">
					<input type="file" size="${filenameLen}" name="upload" value="Upload selected" class="portlet-form-input-field"/><br><br>
					<input type="submit" name="upload" value="Upload Car" class="portlet-form-input-field"/>
				</form>
			</td>
		</tr>
		
		<c:if test="${state=='maximized' && form.namespaceItems!=null}">
			<form method="post" action="<portlet:actionURL><portlet:param name="op" value="deploy"/></portlet:actionURL>">
				<tr><td height="10" colspan="4"></td></tr>
				<tr>
					<td class="portlet-section-alternate"><b>Selected?</b></td>
					<td class="portlet-section-alternate"><b>Configuration Namespace</b></td>
				</tr>
				
				<c:forEach var="namespace" items="${form.namespaceItems}">
					<tr class="portlet-section-body">
						<td><input type="checkbox" name="${namespace}" value="true" checked="checked"/></td>
						<td>${namespace}</td>
					</tr>
				</c:forEach>
								
				<tr><td height="10"></td></tr>
				
				<tr class="portlet-section-body">
					<td align="center" colspan="4">
						<input type="submit" name="deploy" value="Deploy selected" class="portlet-form-input-field"/>
					</td>
				</tr>
			</form>
		</c:if>
	</tr>            
</table>
	