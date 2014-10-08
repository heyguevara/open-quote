<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<link rel="stylesheet" type="text/css" href='<%= request.getContextPath()+"/stylesheet.css" %>'>
<f:view>
	<h:form>
		<h:panelGrid id="p1" width="100%" columns="1">
			<h:panelGrid id="p2" width="100%" headerClass="portlet-section-header" columns="1">
				<f:facet name="header">
					<h:outputText value="Configure Admin :: Configuration history :: #{form.selected.namespace}"/>
				</f:facet>
			</h:panelGrid>
			
			<h:panelGrid id="p4" columns="1" width="100%" styleClass="portlet-section-body" columnClasses="iconbar">
					<h:commandLink id="backToList" action="#{mgr.backToListAction}">
						<h:graphicImage alt="back to list" url="/images/back.gif"/>
						<f:verbatim><br/></f:verbatim>
						<h:outputText value="Back to list"/>
					</h:commandLink> 
			</h:panelGrid>
			
			<h:panelGrid id="p5" width="100%" columns="1">
				<h:dataTable width="100%" value="#{form.history}" var="namespace" headerClass="portlet-section-alternate" columnClasses="portlet-section-body,portlet-section-body,portlet-section-body,portlet-section-body">
					<h:column>
						<f:facet name="header">
							<h:outputText value="Operations"/>
						</f:facet>
						<h:commandLink id="editIcon" action="#{mgr.viewHistoricalAction}">
							<h:graphicImage alt="edit" url="/images/view.gif"/>
						</h:commandLink>
						<f:verbatim>&nbsp;</f:verbatim>
						<h:commandLink id="history" action="#{mgr.restoreToAction}">
							<h:graphicImage alt="history" url="/images/select.gif"/>
						</h:commandLink>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Active from"/>
						</f:facet>
						<h:outputText value="#{namespace.validFrom}">
							<f:convertDateTime type="date" pattern="MMM dd, yyyy hh:mm:ss.SSS a"/>
						</h:outputText>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Active to"/>
						</f:facet>
						<h:panelGroup>
							<h:outputText value="#{namespace.validTo}" rendered="#{namespace.validTo.time!=0}">
								<f:convertDateTime type="date" pattern="MMM dd, yyyy hh:mm:ss.SSS a"/>
							</h:outputText>
							<h:outputText value="Now" rendered="#{namespace.validTo.time==0}"/>
						</h:panelGroup>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Created by"/>
						</f:facet>
						<h:outputText value="#{namespace.who}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Version"/>
						</f:facet>
						<h:outputText value="#{namespace.version}"/>
					</h:column>
				</h:dataTable>
			</h:panelGrid>
		</h:panelGrid>
	</h:form>
</f:view>
