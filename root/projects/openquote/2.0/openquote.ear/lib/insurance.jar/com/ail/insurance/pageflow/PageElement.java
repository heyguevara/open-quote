/* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.ail.insurance.pageflow;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Attribute;
import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.Identified;
import com.ail.core.Type;
import com.ail.core.command.VelocityServiceError;
import com.ail.insurance.pageflow.portlet.QuotationPortlet;
import com.ail.insurance.pageflow.render.RenderArgumentImpl;
import com.ail.insurance.pageflow.render.RenderService.RenderCommand;
import com.ail.insurance.pageflow.util.ErrorText;
import com.ail.insurance.pageflow.util.HelpText;
import com.ail.insurance.pageflow.util.I18N;
import com.ail.insurance.pageflow.util.QuotationCommon;
import com.ail.insurance.pageflow.util.QuotationContext;

/**
 * Base class for all UI elements. Base properties common to all elements are implemented here along
 * with implementations of life-cycle methods.
 */
public abstract class PageElement extends Type implements Identified, Comparable<PageElement> {
    private static final long serialVersionUID = 1L;
    private static Pattern OUTLINE_FORMAT=Pattern.compile("^[0-9.]*$");
    
    /** Separator used in the {@link #id} property */ 
    protected static final char ID_SEPARATOR='-';
    private transient PropertyChangeSupport changes=new PropertyChangeSupport(this);

    /** Id which is unique within the element's container */
    protected String id;

    /** 
     * Optional order indicator in outline style (e.g. "1" "1.2" "1.2.1"). Container elements use this
     * to order their elements during rendering 
     */
    protected String order;
    
    /**
     * JXPath binding related to the context of this element. The expression always relates to some part 
     * of the {@link com.ail.openquote.Policy Policy} model. Most containers pass their element's
     * some sub part of the Policy to work with. For example, the RowScroller element passes each of
     * it's elements a context pointing to the thing they are 'Scrolling' over.
     */
	protected String binding;

    /** Optional class to be referred to by style sheets */
    protected String styleClass;

    /** Hints to the UI rendering engine specifying details of how this field should be rendered. The values supported
     * are specific to the type of attribute being rendered. */ 
    private String renderHint;
    
    /** Optional ref for the elements presentation layer */
    protected String ref;
    
    /** Optional help texts for the elements presentation layer */
    protected HelpText helpText;
    protected HelpText hintText;
    protected List<ErrorText> errorText;
	
    /**
     * List of action associated with this element.
     */
    private ArrayList<Action> action;

    /**
     * An optional XPath expression. If defined the expression is evaluated against the quotation
     * immediately before the action is to be executed. The action will only be executed if the expression 
     * returns true (i.e. <code>(Boolean)model.xpathGet(condition)==true</code> 
     */
    private String condition;
    
    /** The fixed title to be displayed with the answer */
    private String title;

    /**
     * Default constructor
     */
    public PageElement() {
        super();
        action = new ArrayList<Action>();
        errorText=new ArrayList<ErrorText>();;
    }
    
    public PageElement(String condition) {
    	this.condition=condition;
    }
	
    /**
     * The fixed title to be displayed with the answer. This method returns the raw title without
     * expanding embedded variables (i.e. xpath references like ${person/firstname}).
     * @see #getExpandedTitle(Type)
     * @return value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @see #getTitle()
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the title with all variable references expanded. References are expanded with 
     * reference to the models passed in. Relative xpaths (i.e. those starting ./) are
     * expanded with respect to <i>local</i>, all others are expanded with respect to
     * <i>root</i>. 
     * @param root Model to expand references with respect to.
     * @param local Model to expand local references (xpaths starting ./) with respect to.
     * @return Title with embedded references expanded or null if no title is defined
     * @since 1.1
     */
    public String formattedTitle(RenderArgumentImpl args) {
        if (getTitle()!=null) {
            return i18n(expand(getTitle(), QuotationContext.getPolicy(), args.getModelArgRet()));
        }
        else {
            return null;
        }
    }

    protected boolean conditionIsMet(Type model) {
        return condition==null || (Boolean)model.xpathGet(condition)==true;
    }
    
    /**
     * The property change listener is primarily provided for listening
     * to changes to the {@link #getOrder() order} property. The {@link com.ail.insurance.pageflow.util.OrderedLinkedList}
     * uses this listener to detect changes to element's order properties.
     * @param l Listener
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }
    
    /**
     * Get the model binding if any. The model binding is an xpath expression
     * which binds this page element to some part of the model. In the case of
     * elements held within a {@link Repeater} (e.g. a {@link RowScroller}) the
     * binding is partial. The Repeater is bound to a collection in the 
     * model, and the PageElements within the Repeater are bound relative to the
     * Repeater.
     * @return binding XPath expression
	 */
    public String getBinding() {
		return binding;
	}
	
    /**
     * Set the model binding for this page element.
     * @see #getBinding()
     * @param binding Page element's model binding
     */
    public void setBinding(String binding) {
		this.binding = binding;
	}
	
    /**
     * Elements must have an ID associated with them, by default the system will generate an ID
     * for each element as it is created, but these should be considered as unreliable as they
     * change each time the system is restarted.
     * The IDs are important for
     * @return Element's ID
     */
    public String getId() {
		return id;
	}
	
    /**
     * @see #getId()
     * @param id Page element's ID
     */
    public void setId(String id) {
		this.id = id;
	}
    
    /**
     * Apply the specified ID to this element (see {@link #getId()}/{@link #setId(String)}). The ID
     * should only be applied if one is not already defined. By default, the elements of a PageFlow
     * may be defined with or without IDs, however for page actions also need to be able to 
     * identify the element they relate to. For this reason, once the PageFlow is created, this 
     * method is invoked in the {@link PageFlow} object in order to generate IDs for all elements.
     * @param id
     */
    public void applyElementId(String id) {
		if (this.id==null) {
			setId(id);
		}
	}

	/**
     * Get the style class if any - for this page element. 
     * @return style class name
     */
    public String getStyleClass() {
        return styleClass;
    }
	
    /**
     * Set the style class for this page element.
     * @see #getStyleClass()
     * @param styleClass Page element's style class.
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
	
    /**
     * Get the ref if any - for this page element. 
     * @return ref 
     */
    public String getRef() {
        return ref;
    }
	
    /**
     * Set the ref for this page element.
     * @see #getRef()
     * @param ref Page element's ref.
     */
    public void setRef(String ref) {
        this.ref = ref;
    }    
	
    /** 
     * Hints to the UI rendering engine specifying details of how this field should be rendered. The values supported
     * are specific to the type of attribute being rendered.  
     * @return the renderHint
     */
    public String getRenderHint() {
        return renderHint;
    }

    /**
     * @see #getRenderHint()
     * @param renderHint the renderHint to set
     */
    public void setRenderHint(String renderHint) {
        this.renderHint = renderHint;
    }

    /**
     * Get the hints list if any - for this page element. 
     * @return hint 
     */
    public HelpText getHintText(){
    	return hintText;
    }
    
    /**
     * Set the hints for this page element.
     * @see #getHint()
     * @param hint Page element's hint.
     */
    public void setHintText(HelpText hintText){
    	this.hintText = hintText;
    }
    
    /**
     * Get the help if any - for this page element. 
     * @return help 
     */
    public HelpText getHelpText(){
    	return helpText;
    }
    
    /**
     * Set the help for this page element.
     * @see #getHelp()
     * @param note Page element's help.
     */
    public void setHelpText(HelpText helpText){
    	this.helpText = helpText;
    }
    
    /**
     * Get the error list if any - for this page element. 
     * @return alert 
     */
    public List<ErrorText> getErrorText(){
    	return errorText;
    }
    
    /**
     * Set the error list for this page element.
     * @see #getAlert()
     * @param alert Page element's error.
     */
    public void setErrorText(List<ErrorText> errorText){
    	this.errorText = errorText;
    }
    
    /**
     * Fetch the errors (from this PageElement's error list) which are applicable to an error ID.
     * A page element may define any number of errors, each with an optional ID. When the render
     * process detects a validation error, it inspects the element's error list to determine what
     * to render and how to render it. Within a page element, the same error ID may be defined
     * more than once, leading to more than one action being taken.
     * @param errorId Error to lookup in the element's error list.
     * @return List of matching errors, may be zero length. Never null.
     */
    List<ErrorText> fetchErrors(String errorId) {
    	List<ErrorText> ret=new ArrayList<ErrorText>();
    	
    	for(ErrorText err: errorText) {
    		if (err.getError()==null || err.getError().equals(errorId)) {
    			ret.add(err);
    		}
    	}
    	
    	return ret;
    }
    
    /**
     * A list of the actions associated with this element. An action is some processing which
     * takes place in response to a life-cycle even, or a page event (e.g. pressing a button).
     * @return List of Actions for this element.
     */
    public ArrayList<Action> getAction() {
        return action;
    }

    /**
     * @see #getAction()
     * @param action Actions to be associated with this element.
     */
    public void setAction(ArrayList<Action> action) {
        this.action = action;
    }

    /**
     * get the page element's condition
     * @see #condition
     * @return current value of property
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Set the page element's condition.
     * @see #condition
     * @param condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Life-cycle method invoked at the beginning of the response handling process. UI components
     * are expected to use this event to update their model state with respect to the values
     * passed back from the page in the <code>request</code> parameter.
     * @param request
     * @param response
     * @param model
     * @return potentially modified model
     */
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        // default implementation does nothing.
    	return model;
    }
	
    /**
     * Life-cycle method invoked following {@link #applyRequestValues(ActionRequest, ActionResponse, Type)}. 
     * Components use this as an opportunity to validate their model state.
     * Note: This step is skipped if the request is marked as {@link QuotationPortlet#immediate(ActionRequest) immediate}
     * @param request
     * @param response
     * @param model
     * @return true if any validation errors are found, false otherwise.
     */
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
	    return false;
    }
	
    /**
     * Life-cycle method invoked following {@link #applyRequestValues(ActionRequest, ActionResponse, Type)}. This 
     * event is only invoked if the request is marked as {@link QuotationCommon#immediate(ActionRequest) immediate}, or
     * {@link #processValidations(ActionRequest, ActionResponse, Type)} returned false - indicating that there are no
     * page errors.
     * @param request
     * @param response
     * @param model
     * @return potentially modified model
     */
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        for(Action a: action) {
            model=a.processActions(request, response, model);
        }
        
        return model;
    }

    /**
     * This event is fired just prior to {@link #renderResponse(RenderRequest, RenderResponse, Type)} to give components
     * the chance to generate page level output before the portlet page's main form opens. For example, components might
     * add javascript code to the start of the portlet using this event. Note: 
     * As we're in a portlet environment this method does not write to HTML &lt;HEAD&gt; itself, it simply outputs at the beginning
     * of the portlet.  
     * @param request
     * @param response
     * @param model
     * @throws IllegalStateException
     * @throws IOException
     */
    public Type renderPageHeader(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        // default implementation does nothing.
        return model;
    }
    
    /**
     * This event is fired just after {@link #renderResponse(RenderRequest, RenderResponse, Type)} to give components
     * the chance to generate page level output after the portlet page's main form closes. 
     * @param request
     * @param response
     * @param model
     * @throws IllegalStateException
     * @throws IOException
     */
    public Type renderPageFooter(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        // default implementation does nothing.
        return model;
    }
    
    /**
     * This is the last event fired in the request/response process. Elements are expected to render themselves at this point. 
     * @param request
     * @param response
     * @param model
     * @throws IllegalStateException
     * @return potentially modified model
     * @throws IOException
     */
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        // by default only process actions.
        for(Action a: action) {
            model=a.renderResponse(request, response, model);
        }
        
        return model;
    }

    /**
     * Page elements are considered to be the same by id if their id's match and are not empty, or
     * if their values of {@link #order} match.
     */
    public boolean compareById(Object that) {
        if ((that instanceof PageElement)) {
            PageElement thatPageElement=(PageElement)that;
            
            return (this.id!=null && this.id.equals(thatPageElement.id));
        }
        else {
            return false;
        }
    }
    
    /**
     * The comparison of PageElements is based on the value of their respective {@link #order} fields. 
     * <ol><li>If the values of <i>order</i> are identical, a zero is returned indicating that they should be considered 
     * to be same.</li>
     * <li>If both have outline style IDs (e.g. "1.1", "1.1.3" etc) - that is to say they match the
     * regular expression '^[0-9.]$' - then they are compared and ordered as they would be in the context
     * of a document (1.1 comes before 1.2; 10.5 comes after 10.).</li>
     * <li>As a fall back, a simple {@link String#compareTo(String)} is used.</li></ol>
     */
    public int compareTo(PageElement that) {
        if (that.order==null) {
            return 1;
        }
        else if (this.order.equals(that.order)) {
            return 0;
        }
        else if (OUTLINE_FORMAT.matcher(this.order).matches() && OUTLINE_FORMAT.matcher(that.order).matches()) {
            String[] saThis=this.order.split("\\.");
            String[] saThat=that.order.split("\\.");

            try {
                for(int i=0 ; i<saThis.length ; i++) {
                    if (!saThis[i].equals(saThat[i])) {
                        return Integer.parseInt(saThis[i]) - Integer.parseInt(saThat[i]);
                    }
                }
                
                return -1;
            }
            catch(NoSuchElementException e) {
                return 1;
            }
        }
        else {
            return this.order.compareTo(that.order);
        }
    }

    public boolean equals(Object that) {
        if (that instanceof PageElement) {
            return this.id.equals(((PageElement)that).id);
        }
        else {
            return false;
        }
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        String oldOrder=this.order;
        this.order = order;
        changes.firePropertyChange("order", oldOrder, order);
    }
    
    public String i18n(String key) {
        return I18N.i18n(key);
    }
    
    public String i18n(String key, Object... args) {
        String format = I18N.i18n(key);
        Formatter formatter = new Formatter();
        String ret = formatter.format(format, args).toString();
        formatter.close();
        return ret;
    }
    
    /**
     * @param commandName The name of the render command
     * @param request Portlet request
     * @param response Portlet response
     * @param model The type representing the data to be rendered
     * @param onChange javascript to be attached to any onchange event
     * @param onLoad javascript to be attached to any onload event
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    protected Type executeTemplateCommand(String commandName, RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        return executeTemplateCommand(commandName, request, response, model, "");
    }

    /**
     * @param commandName The name of the render command
     * @param request Portlet request
     * @param response Portlet response
     * @param model The type representing the data to be rendered
     * @param rowContext Unique string representing the row being rendered
     * @param onChange javascript to be attached to any onchange event
     * @param onLoad javascript to be attached to any onload event
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    protected Type executeTemplateCommand(String commandName, RenderRequest request, RenderResponse response, Type model, String rowContext) throws IllegalStateException, IOException {
        if (!conditionIsMet(model)) {
            return model;
        }

        RenderCommand command = buildRenderCommand(commandName, request, response, model, rowContext);

        return invokeRenderCommand(command);
    }
    
    /**
     * Build, but do not execute, a render command
     * @param commandName The name of the render command
     * @param request Portlet request
     * @param response Portlet response
     * @param model The type representing the data to be rendered
     * @param rowContext Unique string representing the row being rendered
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    protected RenderCommand buildRenderCommand(String commandName, RenderRequest request, RenderResponse response, Type model, String rowContext) throws IllegalStateException, IOException {
        RenderCommand command = buildRenderCommand(commandName, request, response, model);

        command.setRowContextArg(rowContext);
        
        return command;
    }
    
    /**
     * Build, but do not execute, a render command
     * @param commandName The name of the render command
     * @param request Portlet request
     * @param response Portlet response
     * @param model The type representing the data to be rendered
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    protected RenderCommand buildRenderCommand(String commandName, RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        RenderCommand command=null;

        command=QuotationContext.getCore().newCommand(commandName, 
                QuotationContext.getRenderer().getMimeType(),
                RenderCommand.class);
        
        command.setRequestArg(request);
        command.setResponseArgRet(response);
        command.setModelArgRet(model);
        command.setPolicyArg(QuotationContext.getPolicy());
        command.setPageElementArg(this);
        command.setWriterArg(response.getWriter());
        command.setStyleClassArg(getStyleClass());
        command.setRefArg(getRef());
        command.setCoreArg(QuotationContext.getCore().getCore());
        command.setRenderHintArg(getRenderHint());
        
        return command;
    }
    
    protected Type invokeRenderCommand(RenderCommand command) {
        try {
            command.invoke();
            return command.getModelArgRet();
        } catch(VelocityServiceError e) {
            throw e;
        } catch (BaseException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private static void lookupErrorTranslation(String error, StringBuffer errors, List<ErrorText> errorList) {
        boolean errorFound=false;
        
        if (errorList.size()!=0) {
            for(ErrorText e: errorList) {
                if (error.equals(e.getError())) {
                    if (errors.length()!=0) {
                        errors.append(", ");
                    }
                    errors.append(e.getText());
                    errorFound=true;
                }
            }
        }

        if (!errorFound) {
            errors.append(error);
        }
    }
    
    /**
     * Find all the the errors (if any) associated with an element in a model, and return them.
     * @param model The model to look in for the error
     * @return The error message, or "&nbsp;" (an empty String) if no message is found.
     */
    public String findErrors(Type model, PageElement element) {
        return findError("", model, element);
    }

    /**
     * Find the error(s) (if any) associated with an element in a model, and return them.
     * @param errorFilter Which errors to return
     * @param model The model to look in for the error
     * @return The error message, or "&nbsp;" (an empty String) if no message is found.
     */
    public String findError(String errorFilter, Type model, PageElement element) {
        StringBuffer error=new StringBuffer();

        if (model!=null) {
            for(Attribute attr: model.getAttribute()) {
                if (attr.getId().startsWith("error."+errorFilter)) {
                    lookupErrorTranslation(attr.getValue(), error, element.getErrorText());
                }
            }
        }
        
        return (error.length()==0) ? "&nbsp;" : error.toString();
    }
    
    /**
     * return true if the specified object has a specific error marker associated with it.
     * @see #hasErrorMarkers(Type)
     * @param id Name of error marker to look for
     * @param model Object to check for the error marker
     * @return true if the error marker is found, false otherwise (including if model==null).
     */
    public boolean hasErrorMarker(String id, Type model) {
        if (model!=null) {
            for(Attribute a: model.getAttribute()) {
                if(a.getId().startsWith("error."+id)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Return true if the specified object has any UI error markers associated with it. UI 
     * components use the conversion of attaching error attributes to model element to indicate
     * validation failures. This method will return true if it finds any such attributes
     * associated with the specified object.
     * @param model Object to check for error markers
     * @return true if error markers are found, false otherwise (including if model==null).
     */
    public boolean hasErrorMarkers(Type model) {
        if (model!=null) {
            for(Attribute a: model.getAttribute()) {
                if(a.getId().startsWith("error.")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Product URLs (using the "product" protocol) are only accessible within the OpenQuote
     * server, by virtue of {@link com.ail.code.urlhandler.product.Handler Handler}. This 
     * method converts a product URL into a form which can be used externally.
     * @param url URL to be converted
     * @return External URL
     * @throws MalformedURLException 
     */
    public URL convertProductUrlToExternalForm(URL productUrl) throws MalformedURLException {
        return new URL("http", productUrl.getHost(), productUrl.getPort(), "/alfresco/download/direct?path=/Company%20Home/Product"+productUrl.getPath()); 
    }
    
    /**
     * Products frequently refer to content from their Registry or Pageflows by "relative" URLs. This method
     * expands relative URLs into absolute product URLs - i.e. a URL using the "product:" protocol. A relative URL 
     * is one that starts with "~/", where "~" is shorthand for the product's home location. None relative URLs are
     * returned without modification.
     * @param url URL to be checked and expanded if necessary
     * @param request Associated request
     * @param productTypeId Product to be used in the expanded URL
     * @return Expanded URL if it was relative, URL as passed in otherwise.
     */
    public String expandRelativeUrlToProductUrl(String url, RenderRequest request, String productTypeId) {
        if (url.startsWith("~/")) {
            return "product://"+request.getServerName()+":"+request.getServerPort()+"/"+productTypeId.replace('.', '/')+url.substring(1); 
        }
        else {
            return url;
        }
    }
    
    /**
     * Utility method to expand 'variables' embedded in a string with respect to a model. Variables
     * are in the form '${&lt;xpath&gt;}', where xpath is an expression compatible with JXPath. The 
     * xpath expression is evaluated against a <i>model</i> and the result placed into the string returned.</p>
     * <p>Two models are supported: <code>root</code> and <code>local</code>. XPath expressions starting with '.' 
     * are evaluated against <code>local</code>; all others are evaluated against <code>root</code>.</p>
     * For example: if the <i>src</i> value of <b>"Your quote number is: ${/quoteNumber}"</b> is passed in with a
     * <i>model</i> containing value of 'FQ1234' in it's <code>quoteNumber</code> property; this method would
     * return <b>"Your quote number is: FQ1234"</b>.
     * @param src Source string containing embedded variables
     * @param root Any xpath expression not starting with '.' is evaluated against this instance
     * @param local Any xpath expression starting with '.' is evaluated against this instance
     * @return A string matching <i>src</i> but with variable references expanded.
     * @see #expand(String, Type)
     */
    public static String expand(String src, Type root, Type local) {
        if (src!=null) { 
            int tokenStart, tokenEnd;
            StringBuilder buf=new StringBuilder(src);
    
            do {
                tokenStart=buf.indexOf("${");
                tokenEnd=buf.indexOf("}", tokenStart);
                
                if (tokenStart>=0 && tokenEnd>tokenStart) {
                    String val=null;
                    
                    try {
                        if (buf.charAt(tokenStart+2)=='.') {
                            val=(String)local.xpathGet(buf.substring(tokenStart+2, tokenEnd));
                        }
                        else {
                            val=(String)root.xpathGet(buf.substring(tokenStart+2, tokenEnd));
                        }
                    }
                    catch(Throwable t) {
                        // ignore this - the 'if val==null' below will handle the problem.
                    }
    
                    if (val==null) {
                        val="<b>could not resolve: "+buf.substring(tokenStart+2, tokenEnd)+"</b>";                    
                    }
                    
                    buf.replace(tokenStart, tokenEnd+1, val);
                }
            } while(tokenStart>=0 && tokenEnd>=0);
            
            return buf.toString();
        }
        else {
            return null;
        }
    }
    
    /**
     * Utility method to expand 'variables' embedded in a string with respect to a model. Variables
     * are in the form '${&lt;xpath&gt;}', where xpath is an expression compatible with JXPath. The 
     * xpath expression is evaluated against <i>root</i> and the result placed into the string returned.</p>
     * For example: if the <i>src</i> value of <b>"Your quote number is: ${/quoteNumber}"</b> is passed in with a
     * <i>model</i> containing value of 'FQ1234' in it's <code>quoteNumber</code> property; this method would
     * return <b>"Your quote number is: FQ1234"</b>.
     * @param src Source string containing embedded variables
     * @param model All xpath expressions are evaluated against this object
     * @return A string matching <i>src</i> but with variable references expanded.
     * @see #expand(String, Type, Type)
     */
    public static String expand(String src, Type model) {
        StringWriter writer = new StringWriter();
        BufferedReader reader = null;
        
        try {
            URL url=new URL(src);
            reader=new BufferedReader(new InputStreamReader(url.openStream()));
            
            for(String line=reader.readLine() ; line!=null ; line=reader.readLine()) {
                writer.write(expand(line, model, model));
            }
        }
        catch(Exception e) {
            new CoreProxy().logError("Failed to read input stream.", e);
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                new CoreProxy().logError("Failed to read input stream.", e);
            }
        }
        
        return writer.toString();
    }
}
