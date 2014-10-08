<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<link rel="stylesheet" type="text/css" href='<%= request.getContextPath()+"/stylesheet.css" %>'>
<f:view>
	<h:form>
		<h:panelGrid id="PG1" columns="1" width="100%">
			<h:panelGrid id="p4" columns="2" width="100%" styleClass="portlet-section-body" columnClasses="iconbar,iconbar">
				<h:commandLink id="resetAllProducts" action="#{catalog.resetAllProductsAction}">
					<h:graphicImage alt="reset all" url="/images/reset.gif"/>
					<f:verbatim><br/></f:verbatim>
					<h:outputText value="Reset all products"/>
				</h:commandLink> 
				<h:commandLink id="registerNewProductAction" action="#{catalog.registerNewProductAction}">
					<h:graphicImage alt="register product" url="/images/edit.gif"/>
					<f:verbatim><br/></f:verbatim>
					<h:outputText value="Register new product"/>
				</h:commandLink> 
			</h:panelGrid>

		  <h:panelGrid id="PG1_2" columns="1" width="100%">
	      <f:verbatim>
	   		Click on the product name to view the product's properties. The property view also allows 
	   		you to remove the product from the catalog.
	      </f:verbatim>
		  </h:panelGrid>
		  
		  <h:panelGrid id="PG1_3" columns="1" width="100%">
			  <h:dataTable width="100%" value="#{catalog.products}" var="product" headerClass="portlet-section-alternate" columnClasses="portlet-section-body,portlet-section-body">
				  <h:column>
						<h:commandLink id="clearProductAction" action="#{catalog.clearProductCacheAction}">
							<h:graphicImage alt="reset" url="/images/clear.gif"/>
						</h:commandLink>
				  </h:column>
				  <h:column>
						<h:panelGrid id="PG_1_3_1" columns="1" rowClasses="portlet-section-body,portlet-font-dim">
							<h:commandLink id="initiateQuotationAction" action="#{catalog.displayPropertiesAction}">
								<h:outputText value="#{product.name}"/>
							</h:commandLink>
							<h:outputText value="#{product.value}"/>
						</h:panelGrid>
				  </h:column>
			  </h:dataTable>
			</h:panelGrid>

		  <h:panelGrid id="PG1_4" columns="1" width="100%">
	      <f:verbatim>
	   		 Products show in italic are used as a basis to create other products and are not quotable in themselves. 
	      </f:verbatim>
		  </h:panelGrid>
		  
	  </h:panelGrid>
	</h:form>
</f:view>