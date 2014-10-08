<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<link rel="stylesheet" type="text/css" href='<%= request.getContextPath()+"/stylesheet.css" %>'>
<f:view>
	<h:form>
		<h:panelGrid id="p1" width="100%" columns="1">
			<h:panelGrid id="p2" width="100%" headerClass="portlet-section-header" columns="1">
				<f:facet name="header">
					<h:outputText value="Configure Admin :: Error"/>
				</f:facet>
			</h:panelGrid>
			
			<h:panelGrid id="p4" columns="1" width="100%" styleClass="portlet-section-body">
				<h:outputText value="#{form.errorMessage}"/>
			</h:panelGrid>
			
			<h:panelGrid id="p5" width="100%" columns="1" align="center" columnClasses="iconbar">
				<h:inputTextarea rows="20" value="#{form.errorStack}" styleClass="texteditor" disabled="true"/>
			</h:panelGrid>
		</h:panelGrid>
	</h:form>
</f:view>
