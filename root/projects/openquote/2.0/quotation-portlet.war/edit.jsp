<%@page import="com.ail.insurance.pageflow.util.QuotationCommon"%>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet"%>
<jsp:useBean class="java.lang.String" id="productNameURL" scope="request" />
<jsp:useBean class="java.lang.String" id="productName" scope="request" />
<portlet:defineObjects />
<form id="<portlet:namespace />helloForm" action="<%=productNameURL%>"	method="post">
	<div class="lfr-panel-content">
		<div class="portlet-msg-info">Select a product from the list
			below to complete the configuration of this portlet. You can reconfigure
			the product at any time by returning to the portlet's Configuration
			view.</div>
	</div>
	<div class="lfr-panel-content">
		<select name="productName"><%=QuotationCommon.buildProductSelectOptions(productName)%></select>
		<input type="submit" id="productNameButton" title="Set Product" value="Set Product" />
	</div>
</form>