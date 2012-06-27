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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.BaseException;
import com.ail.core.Identified;
import com.ail.core.Type;
import com.ail.insurance.pageflow.portlet.QuotationPortlet;
import com.ail.insurance.pageflow.render.RenderService.RenderCommand;
import com.ail.insurance.pageflow.util.ErrorText;
import com.ail.insurance.pageflow.util.HelpText;
import com.ail.insurance.pageflow.util.QuotationCommon;
import com.ail.insurance.pageflow.util.QuotationContext;
import com.ail.insurance.pageflow.util.I18N;

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
    public void renderPageHeader(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        // default implementation does nothing.
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
        String format=I18N.i18n(key);
        return new Formatter().format(format, args).toString();
    }
    
    protected Type executeTemplateCommand(String commandName, RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        if (!conditionIsMet(model)) {
            return model;
        }
        
        try {
	    	RenderCommand command=QuotationContext.getCore().newCommand(commandName, 
	    																QuotationContext.getRenderer().getMimeType(),
	    																RenderCommand.class);
	    	command.setRequestArg(request);
	    	command.setResponseArgRet(response);
	    	command.setModelArgRet(model);
	    	command.setPolicyArg(QuotationContext.getPolicy());
	    	command.setPageElementArg(this);
	    	command.setWriterArg(response.getWriter());
			command.invoke();
	    	return command.getModelArgRet();
		} catch (BaseException e) {
			throw new IllegalStateException(e);
		}
    }
}
