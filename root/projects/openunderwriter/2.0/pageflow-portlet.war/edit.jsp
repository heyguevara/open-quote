<%@page import="com.ail.pageflow.portlet.PageFlowCommon"%>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet"%>
<jsp:useBean class="java.lang.String" id="productNameURL" scope="request" />
<jsp:useBean class="java.lang.String" id="productName" scope="request" />
<jsp:useBean class="java.lang.String" id="pageFlowName" scope="request" />
<portlet:defineObjects />
<form class="aui-form" id="<portlet:namespace />" action="<%=productNameURL%>" method="post">
	<div class="taglib-form-navigator">
		<div class="form-section selected" style="float:none">
			<h3>PageFlow Configuration</h3>
			<fieldset class="aui-fieldset">
				<div class="aui-fieldset-content">
					<div class="lfr-panel-content">
						<div class="portlet-msg-info">Select a product and page flow
							from the lists below to complete the configuration of this
							portlet. You can reconfigure these at any time by returning to
							the portlet's Configuration view.</div>
					</div>
					<div>
						<span class="aui-field aui-field-select aui-field-menu"> 
							<span class="aui-field-content"> 
								<label class="aui-field-label" for="productName"> Product </label> 
								<span class="aui-field-element"> 
									<select class="aui-field-input aui-field-input-select aui-field-input-menu" name="productName" onchange="this.form.submit()"><%=new PageFlowCommon().buildProductSelectOptions(productName)%></select>
								</span>
							</span>
						</span> 
						<span class="aui-field aui-field-select aui-field-menu"> 
							<span class="aui-field-content"> 
								<label class="aui-field-label" for="type"> Page Flow </label> 
								<span class="aui-field-element">
						    		<select class="aui-field-input aui-field-input-select aui-field-input-menu" name="pageFlowName"><%=new PageFlowCommon().buildPageFlowSelectOptions(productName, pageFlowName)%></select>
								</span>
							</span>
						</span> 
						<span class="aui-field aui-field-select aui-field-menu"> 
							<span class="aui-field-content"> 
								<span class="aui-field-element "> 
						    		<input type="submit" id="productNameButton" title="Set Configuration" value="Set Configuration" />
								</span>
							</span>
						</span>
					</div>
				</div>
			</fieldset>
		</div>
	</div>
</form>


