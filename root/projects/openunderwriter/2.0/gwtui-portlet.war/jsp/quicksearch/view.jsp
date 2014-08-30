<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<portlet:defineObjects />

	<fmt:setBundle basename="com.ail.ui.client.i18n.Messages" var="lang"/>
	<h2>
		<fmt:message key="quickSearch" bundle="${lang}"/>
	</h2>

	<table align="center">
		<tr>
			<td colspan="2" style="font-weight: bold;"><fmt:message key="enterQuoteNumber" bundle="${lang}"/></td>
		</tr>
		<tr>
			<td id="searchFieldContainer"></td>
			<td id="sendButtonContainer"></td>
		</tr>
		<tr>
			<td colspan="2" style="color: red;" id="errorLabelContainer"></td>
		</tr>
	</table>

