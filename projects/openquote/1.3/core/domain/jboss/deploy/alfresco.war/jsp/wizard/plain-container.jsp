<%--
* Copyright (C) 2005-2007 Alfresco Software Limited.

* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

* As a special exception to the terms and conditions of version 2.0 of
* the GPL, you may redistribute this Program in connection with Free/Libre
* and Open Source Software ("FLOSS") applications as described in Alfresco's
* FLOSS exception.  You should have recieved a copy of the text describing
* the FLOSS exception, and it is also available here:
* http://www.alfresco.com/legal/licensing"
--%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/alfresco.tld" prefix="a" %>
<%@ taglib uri="/WEB-INF/repo.tld" prefix="r" %>

<%@ page buffer="32kb" contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ page import="org.alfresco.web.app.Application" %>
<%@ page import="org.alfresco.web.ui.common.PanelGenerator" %>

<r:page title="<%=Application.getWizardManager().getTitle() %>">

<f:view>

<%-- load a bundle of properties with I18N strings --%>
<f:loadBundle basename="alfresco.messages.webclient" var="msg"/>

<h:form acceptcharset="UTF-8" id="wizard">

<table cellspacing="0" cellpadding="3" border="0" width="100%">
<tr>
<td width="20%" valign="top">
<% PanelGenerator.generatePanelStart(out, request.getContextPath(), "greyround", "#F5F5F5"); %>
<h:outputText styleClass="mainSubTitle" value="#{msg.steps}"/><br>
<a:modeList itemSpacing="3" iconColumnWidth="2" selectedStyleClass="statusListHighlight"
value="#{WizardManager.currentStepAsString}" disabled="true">
<a:listItems value="#{WizardManager.stepItems}" />
</a:modeList>
<% PanelGenerator.generatePanelEnd(out, request.getContextPath(), "greyround"); %>
</td>

<td width="100%" valign="top">

<a:errors message="#{WizardManager.errorMessage}" styleClass="errorMessage" />

<% PanelGenerator.generatePanelStart(out, request.getContextPath(), "white", "white"); %>
<table cellpadding="2" cellspacing="2" border="0" width="100%">
<tr>
<td class="mainSubTitle"><h:outputText value="#{WizardManager.stepTitle}" /></td>
</tr>
<tr>
<td class="mainSubText"><h:outputText value="#{WizardManager.stepDescription}" /></td>
</tr>
<tr><td class="paddingRow"></td></tr>
<tr>
<td width="100%" valign="top">
<f:subview id="wizard-body">
<jsp:include page="<%=Application.getWizardManager().getPage() %>" />
</f:subview>
</td>
</tr>
<tr><td class="paddingRow"></td></tr>
<tr>
<td><h:outputText value="#{WizardManager.stepInstructions}" /></td>
</tr>
</table>
<% PanelGenerator.generatePanelEnd(out, request.getContextPath(), "white"); %>
</td>

<td valign="top">
<% PanelGenerator.generatePanelStart(out, request.getContextPath(), "greyround", "#F5F5F5"); %>
<table cellpadding="1" cellspacing="1" border="0">
<tr>
<td align="center">
<h:commandButton id="next-button" styleClass="wizardButton"
value="#{WizardManager.nextButtonLabel}"
action="#{WizardManager.next}"
disabled="#{WizardManager.nextButtonDisabled}" />
</td>
</tr>
<tr>
<td align="center">
<h:commandButton id="back-button" styleClass="wizardButton"
value="#{WizardManager.backButtonLabel}"
action="#{WizardManager.back}"
disabled="#{WizardManager.backButtonDisabled}" />
</td>
</tr>
<tr>
<td align="center">
<h:commandButton id="finish-button" styleClass="wizardButton"
value="#{WizardManager.finishButtonLabel}"
action="#{WizardManager.finish}"
disabled="#{WizardManager.finishButtonDisabled}" />
</td>
</tr>
<tr><td class="wizardButtonSpacing"></td></tr>
<tr>
<td align="center">
<h:commandButton id="cancel-button" styleClass="wizardButton"
value="#{WizardManager.cancelButtonLabel}"
action="#{WizardManager.cancel}" immediate="true" />
</td>
</tr>
</table>
<% PanelGenerator.generatePanelEnd(out, request.getContextPath(), "greyround"); %>
</td>
</tr>
</table>

</h:form>

</f:view>

</r:page>
