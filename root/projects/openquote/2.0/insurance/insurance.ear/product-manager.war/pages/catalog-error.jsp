<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<link rel="stylesheet" type="text/css" href='<%= request.getContextPath()+"/stylesheet.css" %>'>
<f:view>
	<h:form>
		<h:panelGrid id="PG1" columns="1" width="100%" columnClasses="iconbar">
			<f:verbatim>Catalog Error</f:verbatim>
			<h:outputFormat value="{0} Please try again, or contact your systems administrator">
				<f:param value="#{catalog.errorMessage}"/>
			</h:outputFormat>
			<h:commandButton id="continue" action="#{catalog.continueAction}" value="Continue"/>
	  </h:panelGrid>
	</h:form>
</f:view>