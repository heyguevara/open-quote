<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<link rel="stylesheet" type="text/css" href='<%= request.getContextPath()+"/stylesheet.css" %>'>
<f:view>
	<h:form>
		<h:panelGrid id="p1" width="100%" columns="1">
			<h:panelGrid id="p2" width="100%" headerClass="portlet-section-header" columns="1">
				<f:facet name="header">
					<h:outputText value="Configure Admin :: Editor :: #{form.selected.namespace}"/>
				</f:facet>
			</h:panelGrid>
			
			<h:panelGrid id="p4" columns="3" width="100%" styleClass="portlet-section-body" columnClasses="iconbar,iconbar,iconbar">
				<h:commandLink id="backToList" action="#{mgr.backToListAction}">
					<h:graphicImage alt="back to list" url="/images/back.gif"/>
					<f:verbatim><br/></f:verbatim>
					<h:outputText value="Back to list"/>
				</h:commandLink> 
				<h:commandLink id="reset" action="#{mgr.clearCacheAction}">
					<h:graphicImage alt="clear cache" url="/images/clear.gif"/>
					<f:verbatim><br/></f:verbatim>
					<h:outputText value="Clear cache"/>
				</h:commandLink> 
				<h:commandLink id="history" action="#{mgr.historyFromEditorAction}">
					<h:graphicImage alt="download" url="/images/history.gif"/>
					<f:verbatim><br/></f:verbatim>
					<h:outputText value="History"/>
				</h:commandLink> 
			</h:panelGrid>
			
			<h:panelGrid id="p5" width="100%" columns="1" align="center" columnClasses="iconbar">
				<h:inputTextarea rows="20" value="#{form.configurationXML}" styleClass="texteditor"/>
				<h:panelGroup rendered="#{!form.editorDisabled}">
					<h:commandButton value="Save" action="#{mgr.editorSaveAction}" styleClass="portlet-form-input-field"/>
					<h:commandButton value="Reset" action="#{mgr.editorResetAction}" styleClass="portlet-form-input-field"/>
				</h:panelGroup>
			</h:panelGrid>
		</h:panelGrid>
	</h:form>
</f:view>
