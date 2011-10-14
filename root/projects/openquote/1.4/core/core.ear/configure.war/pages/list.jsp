<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<link rel="stylesheet" type="text/css" href='<%= request.getContextPath()+"/stylesheet.css" %>'>
<f:view>
	<h:form>
	
		<h:panelGrid id="dlpg1" columns="1" rendered="#{!mgr.isWindowMaximized}" width="100%">
			<h:panelGrid id="dlpg2" columns="2" styleClass="portlet-section-alternate" width="100%">
				<h:graphicImage alt="" url="/images/download.gif"/>
				<h:outputText value="Configuration utilities"/>
			</h:panelGrid>
			<h:panelGrid id="p4_1" columns="2" width="100%" styleClass="portlet-section-body" rowClasses="iconbar">				
				<t:column width="50%">
					<h:commandLink id="resetAll" action="#{mgr.resetAllAction}">
						<h:graphicImage alt="reset all" url="/images/reset.gif"/>
						<f:verbatim><br/></f:verbatim>
						<h:outputText value="Reset all configurations"/>
					</h:commandLink> 
				</t:column>
				<t:column width="50%">
					<h:commandLink id="clear-cache" action="#{mgr.clearCacheAction}">
						<h:graphicImage alt="clear cache" url="/images/clear.gif"/>
						<f:verbatim><br/></f:verbatim>
						<h:outputText value="Clear configuration cache"/>
					</h:commandLink> 
				</t:column>
			</h:panelGrid>
		</h:panelGrid>

		<h:panelGrid rendered="#{mgr.isWindowMaximized}" id="p1" width="100%" columns="1">
			<h:panelGrid id="p2" width="100%" headerClass="portlet-section-header" columns="1">
				<f:facet name="header">
					<h:outputText value="Configure Admin :: Configuration namespaces"/>
				</f:facet>
			</h:panelGrid>
			
			<h:panelGrid id="p4" columns="2" width="100%" styleClass="portlet-section-body" columnClasses="iconbar,iconbar">
				<h:commandLink id="resetAll" action="#{mgr.resetAllAction}">
					<h:graphicImage alt="reset all" url="/images/reset.gif"/>
					<f:verbatim><br/></f:verbatim>
					<h:outputText value="Reset all configurations"/>
				</h:commandLink> 
				<h:commandLink id="clear-cache" action="#{mgr.clearCacheAction}">
					<h:graphicImage alt="clear cache" url="/images/clear.gif"/>
					<f:verbatim><br/></f:verbatim>
					<h:outputText value="Clear cache"/>
				</h:commandLink> 
			</h:panelGrid>
			
			<h:panelGrid id="p5" width="100%" columns="1">
				<h:dataTable width="100%" value="#{form.namespaces}" var="namespace" headerClass="portlet-section-alternate" columnClasses="portlet-section-body,portlet-section-body,portlet-section-body,portlet-section-body">
					<h:column>
						<f:facet name="header">
							<h:outputText value="Operations"/>
						</f:facet>
						<h:commandLink id="editIcon" action="#{mgr.editLatestAction}">
							<h:graphicImage alt="edit" url="/images/edit.gif"/>
						</h:commandLink>
						<f:verbatim>&nbsp;</f:verbatim>
						<h:commandLink id="history" action="#{mgr.historyFromListAction}">
							<h:graphicImage alt="history" url="/images/history.gif"/>
						</h:commandLink>
						<f:verbatim>&nbsp;</f:verbatim>
						<h:commandLink id="reset" action="#{mgr.resetConfigurationAction}">
							<h:graphicImage alt="reset" url="/images/reset.gif"/>
						</h:commandLink>
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
		</h:panelGrid>
	</h:form>
</f:view>
