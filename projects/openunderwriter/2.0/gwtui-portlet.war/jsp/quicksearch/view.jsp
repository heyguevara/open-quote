<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<portlet:defineObjects />

	<fmt:setBundle basename="com.ail.ui.client.i18n.Messages" var="lang"/>

	<table class="gui-cell-border" width="100%">
		<tr class="gui-cell-border">
			<td class="gui-title-text gui-standard-cell"><fmt:message key="quickSearch" bundle="${lang}"/></td>
		</tr>
		<tr>
			<td class="gui-standard-text gui-standard-cell"><fmt:message key="enterQuoteNumber" bundle="${lang}"/></td>
		</tr>
		<tr>
			<td class="gui-standard-text gui-standard-cell">
				<span id="gui-quicksearch-search-field-container"></span>
				<span id="gui-quicksearch-search-button-container"></span>
			</td>
		</tr>
		<tr>
			<td id="gui-quicksearch-error-label-container" class="gui-standard-text" align="center"></td>
		</tr>
	</table>

