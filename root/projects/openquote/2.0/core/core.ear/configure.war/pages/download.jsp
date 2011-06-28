<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" href='<%= request.getContextPath()+"/stylesheet.css" %>'/>
<f:view>
	<h:form>
		<h:panelGrid id="dlpg1" columns="1" rendered="#{!mgr.isWindowMaximized}" width="100%">
			<h:panelGrid id="dlpg2" columns="2" styleClass="portlet-section-alternate" width="100%">
				<h:graphicImage alt="" url="/images/download.gif"/>
				<h:outputText value="Configuration download"/>
			</h:panelGrid>
			<h:panelGrid id="dlpg3" columns="1" width="100%" styleClass="portlet-section-body" columnClasses="iconbar">
				<h:commandLink id="download" action="#{mgr.maximizeDownloadAction}">
					<h:graphicImage alt="download" url="/images/download.gif"/>
					<f:verbatim><br/></f:verbatim>
					<h:outputText value="Download configuration"/>
				</h:commandLink> 
			</h:panelGrid>
		</h:panelGrid>
		
		<h:panelGrid rendered="#{mgr.isWindowMaximized}" id="dlpg5" width="100%" columns="1">
			<h:panelGrid id="dlpg6" width="100%" headerClass="portlet-section-header" columns="1">
				<f:facet name="header">
					<h:outputText value="Configure Admin :: Configuration download"/>
				</f:facet>
			</h:panelGrid>
			
			<h:panelGrid id="dlpg7" width="100%" columns="1">
				<h:dataTable width="100%" value="#{form.namespaces}" var="namespace" headerClass="portlet-section-alternate" columnClasses="portlet-section-body,portlet-section-body,portlet-section-body,portlet-section-body">
					<h:column>
						<f:facet name="header">
							<h:outputText value="Selected?"/>
						</f:facet>
						<h:selectBooleanCheckbox id="sel" value="#{namespace.lock}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Configuration Namespace"/>
						</f:facet>
						<h:commandLink id="editText" action="#{mgr.editLatestAction}">
							<h:outputText value="#{namespace.namespace}"/>
						</h:commandLink>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Date Last Modified"/>
						</f:facet>
						<h:outputText value="#{namespace.validFrom}">
							<f:convertDateTime type="date" pattern="MMM dd, yyyy hh:mm:ss.SSS a"/>
						</h:outputText>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Last Modified By"/>
						</f:facet>
						<h:outputText value="#{namespace.who}"/>
					</h:column>
				</h:dataTable>
			</h:panelGrid>

			<h:panelGrid id="dlpg20" columns="1" width="100%" columnClasses="iconbar">
				<h:commandButton id="package" action="#{mgr.downloadSelectedAction}" value="Build car file" styleClass="portlet-form-input-field" />
			</h:panelGrid>

			<h:panelGrid id="dlpg9" rendered="#{form.carFileAvailable}" columns="1" width="100%" styleClass="portlet-section-body" columnClasses="iconbar">
				<h:outputLink value="/configure/downloadCarFileServlet?carFile=#{form.carFileName}">
					<h:graphicImage alt="download" url="/images/file.gif"/>
					<f:verbatim><br/></f:verbatim>
					<h:outputText value="Download car"/>
				</h:outputLink>
			</h:panelGrid>

		</h:panelGrid>
	</h:form>
</f:view>
