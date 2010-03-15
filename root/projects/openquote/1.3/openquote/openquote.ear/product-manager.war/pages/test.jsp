<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<f:view>
	<h:form>
		<h:dataTable width="100%" value="#{form.products}" var="product" headerClass="portlet-section-alternate" columnClasses="portlet-section-body">
			<h:column>
				<f:facet name="header">
					<h:outputText value="Product name"/>
				</f:facet>
					<h:commandLink id="product" action="#{mgr.initiateQuotationAction}">
						<h:outputText value="#{product}"/>
					</h:commandLink>
			</h:column>
		</h:dataTable>
	</h:form>
</f:view>