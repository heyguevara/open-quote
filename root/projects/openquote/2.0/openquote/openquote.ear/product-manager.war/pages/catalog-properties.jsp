<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<link rel="stylesheet" type="text/css" href='<%= request.getContextPath()+"/stylesheet.css" %>'>
<f:view>
	<h:messages showDetail="true" showSummary="false"/>
	<h:form id="properties">
		<h:panelGrid id="PG1" columns="1" width="100%">
			<h:panelGrid id="PG1_1" columns="2" width="100%" columnClasses="property-narrow-column,property-wide-column">
				<h:outputLabel for="form_location"><h:outputText value="Location:"/></h:outputLabel>
				<h:panelGrid columns="2" width="100%">
					<h:inputText id="form_location" value="#{catalog.productName}" styleClass="texteditor"/>
					<h:message for="form_location" style="color:red;" showSummary="true" showDetail="false"/>
				</h:panelGrid>
			</h:panelGrid>
			
			<f:verbatim>
				A product's location is relative to the Product space in CMS. For example, the space 
				<i>Company&nbsp;Home/Producs/AIL/Demo/MotorPlus</i>, would be shown as AIL.Demo.MotorPlus
				here.
			</f:verbatim>
			
			<h:panelGrid id="PG1_2" columns="2" width="100%" columnClasses="property-narrow-column,property-wide-column">
				<h:outputLabel for="form_description"><h:outputText value="Description:"/></h:outputLabel>
				<h:panelGrid columns="2" width="100%">
					<h:inputTextarea id="form_description" value="#{catalog.productDescription}" styleClass="texteditor" rows="5"/>
					<h:message for="form_description" showSummary="true" showDetail="false"/>
				</h:panelGrid>
			</h:panelGrid>
			
			<h:panelGrid id="PG1_3" columns="2" width="100%">
				<h:commandButton id="remove" action="#{catalog.removeProductAction}" value="Remove" disabled="#{!catalog.removable}"/>
				<h:panelGrid id="PG1_3_1" columns="1">
					<h:outputText value="Remove from Catalog."/>
					<f:verbatim>
						Remove does not delete content from CMS, it simply removes this entry from the catalog.
					</f:verbatim>
				</h:panelGrid>
			</h:panelGrid>
			

			<f:verbatim>&nbsp;</f:verbatim>

			<h:panelGrid id="PG1_4" columns="2" width="100%" columnClasses="iconbar,iconbar">
				<h:commandButton id="cancel" action="#{catalog.cancelProductPropertiesAction}" value="Cancel"/>
				<h:commandButton id="save" action="#{catalog.saveProductPropertiesAction}" value="Save"/>
			</h:panelGrid>
	  </h:panelGrid>
	</h:form>
</f:view>