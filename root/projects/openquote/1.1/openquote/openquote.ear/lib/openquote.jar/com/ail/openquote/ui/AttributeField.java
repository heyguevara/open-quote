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
package com.ail.openquote.ui;

import static com.ail.core.Attribute.YES_OR_NO_FORMAT;
import static com.ail.core.Functions.expand;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.PostconditionException;
import com.ail.core.Type;
import com.ail.core.TypeXPathException;
import com.ail.openquote.Quotation;
import com.ail.openquote.ui.util.Choice;
import com.ail.openquote.ui.util.Functions;
import com.ail.openquote.ui.util.QuotationContext;

/**
 * <p>An AttributeField represents an individual column within a {@link RowScroller}. The RowScroller itself
 * iterates over a collection of results rendering one row on the screen for each row of data in the 
 * collection. The columns in each row are represented by AttributeFields which are bound to 
 * {@link com.ail.core.Attribute Attributes} in the quotation.</p>
 * <p><img src="doc-files/AttributeField.png"/></p>
 * <p>In the example above the RowScroller is bound to a collection of vehicle assets. The current quote contains
 * two assets matching the criteria - an Alfa Romeo and a Bentley. The RowScroller contains nine AttributeFields
 * each of which is bound to a different Attribute in the assets. The AttributeField bindings are taken to be relative to
 * the binding of the RowScroller. In this case the RowScroller is bound to <code>asset[assetTypeId='Vehicle']</code>,
 * and the individual AttributeFields are bound to <code>attribute[id='Make']</code>, <code>attribute[id='Model']</code>,
 * etc</p>
 * <p>The Attribute which the AttributeField is bound to dictates the HTML form elements used to represent it, and
 * the validations that will be applied to it. For example, a "choice" attribute is rendered as a drop down list; a
 * "string" is rendered as a right justified text field.</p>
 * <p>Each AttributeField defines it's column {@link #getTitle() title}, and an optional {@link #getSubTitle() subTitle}
 * which can be used when the titles are fixed. Dynamic titles - the text of which is picked up from another part of
 * the quote - can be defined using {@link #getTitleBinding() titleBinding} and {@link #getSubTitleBinding() subTitleBinding}. 
 * It may also define javascript to be executed either onLoad (when the page is loaded); or onChange (when a fields 
 * value is changed).</p>
 * <p>The AttributeField also supports the concept of RenderHints. The rendering engine will take these hints into account
 * as it displays the field within the page. The values of hint supported are dependent on the Attribute being rendered.
 * The supported hints are listed below:</p>
 * <table>
 * <tr><th>Attribute Type</th><th>Render Hints</th></tr>
 * <tr><td>choice</td><td>"dropdown" - Display the options on a drop down menu (default)<br/>"radio" - Display the choice list as radio buttons</tr>
 * <tr><td>yesorno</td><td>"dropdown" - Display the options on a drop down menu (default)<br/>"checkbox" - Display as a checkbox<br/>"radio" - Display Yes/No as radio buttons</tr>
 * </table>
 * 
 * @see RowScroller
 * @see com.ail.core.Attribute
 * @version 1.1
 */
public class AttributeField extends PageElement {
    private static final long serialVersionUID = 7118438575837087257L;
    private static final Pattern formattedCurrencyPattern=Pattern.compile("([^0-9,.']*)([0-9,.']*)([^0-9,.']*)");

    /** The fixed title to be displayed with the answer */
    private String title;

    /** The fixed subtitle to be displayed with the answer */
    private String subTitle;

    /** A dynamic title taken from some other part of the quote instance to be displayed with the answer */
    private String titleBinding;

    /** The dynamic subtitle taken from some other part of the quote instance to be displayed with the answer */
    private String subTitleBinding;

    /** Javascript to be executed when the page loads */
    private String onLoad;

    /** Javascript to be executed when a field's value is changed */
    private String onChange;
    
    /** Hints to the UI rendering engine specifying details of how this field should be rendered. The values supported
     * are specific to the type of attribute being rendered. */ 
    private String renderHint;

	public AttributeField() {
		super();
	}

    /**
     * The fixed sub title to be displayed with the answer. This method returns the raw sub title without
     * expanding embedded variables (i.e. xpath references like ${person/firstname}).
     * @see #getExpendedSubTitle(Type)
     * @return value of title
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * @see #getSubTitle()
     * @param subTitle
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * Get the sub title with all variable references expanded. References are expanded with 
     * reference to the models passed in. Relative xpaths (i.e. those starting ./) are
     * expanded with respect to <i>local</i>, all others are expanded with respect to
     * the current quotation (from {@link QuotationContext}).
     * @param local Model to expand local references (xpaths starting ./) with respect to.
     * @return Title with embedded references expanded
     * @since 1.1
     */
    public String getExpandedSubTitle(Type local) {
    	if (getTitle()!=null) {
    		return expand(getSubTitle(), QuotationContext.getQuotation(), local);
    	}
    	// TODO Check getTitleBinding for backward compatibility only - remove for OQ2.0
    	else if (getSubTitleBinding()!=null) {
    		return local.xpathGet(getSubTitleBinding(), String.class);
    	}
    	else {
    		return null;
    	}
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
     * reference to the model passed in. Relative xpaths (i.e. those starting ./) are
     * expanded with respect to <i>local</i>, all others are expanded with respect to
     * the current quotation (from {@link QuotationContext}). 
     * @param local Model to expand local references (xpaths starting ./) with respect to.
     * @return Title with embedded references expanded
     * @since 1.1
     */
    public String getExpandedTitle(Type local) {
    	if (getTitle()!=null) {
    		return expand(getTitle(), QuotationContext.getQuotation(), local);
    	}
    	// TODO Check getTitleBinding for backward compatibility only - remove for OQ2.0
    	else if (getTitleBinding()!=null) {
    		return local.xpathGet(getTitleBinding(), String.class);
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Javascript to be executed when a field's value is changed
     * @return java script
     */
    public String getOnChange() {
        return onChange;
    }

    /**
     * @see #getOnChange()
     * @param onChange
     */
    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    /**
     * Javascript to be executed when the page loads. 
     * @return java script
     */
    public String getOnLoad() {
        return onLoad;
    }

    /**
     * @see #getOnLoad()
     * @param onLoad
     */
    public void setOnLoad(String onLoad) {
        this.onLoad = onLoad;
    }

    @Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        return applyRequestValues(request, response, model, "");
    }

    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model, String rowContext) {
        return applyAttributeValues(model, getBinding(), rowContext, request);
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        return applyAttributeValidation(model, getBinding());
    }

    @Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
	    return renderResponse(request, response, model, "");
	}

    public Type renderResponse(RenderRequest request, RenderResponse response, Type model, String rowContext) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        
        w.print(renderAttribute(model, getBinding(), rowContext, getOnChange(), getOnLoad()));
        
        return model;
    }

    @Override
    public void renderPageHeader(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        renderPageLevelResponse(request, response, model, "");
    }

    public void renderPageLevelResponse(RenderRequest request, RenderResponse response, Type model, String rowContext) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        w.print(renderAttributePageLevel(model, getBinding(), rowContext, request.getPortletSession()));
    }

    /**
     * @return the titleBinding
     * @deprecated Use {@link #getExpandedTitle(Type)} in combination with embedded xpath references within the title.
     */
    public String getTitleBinding() {
        return titleBinding;
    }

    /**
     * @param titleBinding the titleBinding to set
     * @deprecated Use {@link #getExpandedTitle(Type)} in combination with embedded xpath references within the title.
     */
    public void setTitleBinding(String titleBinding) {
        this.titleBinding = titleBinding;
    }

    /**
     * @return the subTitleBinding
     * @deprecated Use {@link #getExpendedSubTitle(Type)} in combination with embedded xpath references within the title.
     */
    public String getSubTitleBinding() {
        return subTitleBinding;
    }

    /**
     * @param subTitleBinding the subTitleBinding to set
     * @deprecated Use {@link #getExpendedSubTitle(Type)} in combination with embedded xpath references within the title.
     */
    public void setSubTitleBinding(String subTitleBinding) {
        this.subTitleBinding = subTitleBinding;
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
	 * Render an AttributeField on the UI. This is quite a common requirement throughout the classes
	 * of the ui package, so it's put here for convenience. The result of calling this method
	 * is some kind of HTML form element (input, select, textarea, etc) being returned as a String.
	 * The actual element returned depends on the specifics of the Attribute it is being rendered 
	 * for.
	 * @param data The 'model' (in MVC terms) containing the AttributeField to be rendered
	 * @param boundTo An XPath expressing pointing at the AttributeField in 'data' that we're rendering.
	 * @param rowContext If we're rendering into a scroller, this'll be the row number in xpath predicate format (e.g. "[1]"). Otherwise ""
	 * @param onChange JavaScript onChange event
	 * @param onLoad JavaScript onLoad event
	 * @return The HTML representing the attribute as a form element.
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws PostconditionException 
	 */
	protected String renderAttribute(Type model, String boundTo, String rowContext, String onChange, String onLoad) throws IllegalStateException, IOException {
		// If we're not bound to anything, output nothing.
	    if (boundTo==null) {
	        return "";
	    }
	
	    // Create the StringWriter - out output will go here.
	    StringWriter ret=new StringWriter();
	    PrintWriter w=new PrintWriter(ret);
	
	    if (boundTo==null) {
	        return "";
	    }
	
	    String id=Functions.xpathToId(rowContext+boundTo);
	    com.ail.core.Attribute attr=(com.ail.core.Attribute)model.xpathGet(boundTo);
	    String onChangeEvent=(onChange!=null) ? "onchange='"+onChange+"'" : "";
	    
		try {
	        w.printf("<table><tr><td>");
	        
	        if (attr==null) {
	            w.printf("<b>undefined: %s</b>", boundTo);
	        }
	        else {
	            if (attr.isStringType()) {
	                String size=attr.getFormatOption("size");
	                size=(size!=null) ? "size='"+size+"'" : "";                
	                w.printf("<input name=\"%s\" class='portlet-form-input-field' %s %s type='text' value='%s'/>", id, size, onChangeEvent, attr.getValue());
	            }
	            else if (attr.isNumberType()) {
	                String pattern=attr.getFormatOption("pattern");
	                String trailer=(attr.getFormat().endsWith("percent")) ? "%" : ""; 
	                int size=(pattern==null) ? 7 : pattern.length();
	                w.printf("<input name=\"%s\" class='portlet-form-input-field' style='text-align:right' size='%d' %s type='text' value='%s'/>%s", id, size, onChangeEvent, attr.getValue(), trailer);
	            }
	            else if (attr.isCurrencyType()) {
	            	String value, pre, post;

	            	// If we can get the formatted value from the currency, this will give use all the locale specific symbols
	            	// to place before or after the value. However, as Attributes are allowed to hold invalid values, we will 
	            	// adopt the fall back position of of using the value as the fields content with nothing to the left of the
	            	// field, and the currency's ISO on the right.
	            	try {
	            		Matcher m=formattedCurrencyPattern.matcher(attr.getFormattedValue());
	            		m.matches();
	            		pre=m.group(1);
	            		value=m.group(2);
	            		post=m.group(3);
	            	}
	            	catch(Throwable e) {
		            	pre="";
		            	value=attr.getValue();
		            	post=attr.getUnit();
	            	}
	                
	            	w.printf("%s<input name=\"%s\" class='portlet-form-input-field' style='text-align:right' %s size='10' type='text' value='%s'/>%s", pre, id, onChangeEvent, value, post);
	            }
	            else if (attr.isChoiceMasterType()) {
	                onLoad="loadChoiceOptions($this,$value,"+attr.getChoiceTypeName()+")";
	                onChange="updateSlaveChoiceOptions(findElementsByName('"+id+"')[0], "+attr.getChoiceTypeName()+", '"+attr.getId()+"', '"+attr.getChoiceSlave()+"');";
	                w.printf("<select name=\"%s\" onchange=\"%s\" class='pn-normal'/>", id, onChange);
	            }
	            else if (attr.isChoiceSlaveType()) {
	                onLoad="loadSlaveChoiceOptions($this,$value,"+attr.getChoiceTypeName()+",'"+attr.getChoiceMaster()+"','"+attr.getId()+"')";
	                w.printf("<select name=\"%s\" class='pn-normal'/>", id);
	            }
	            else if (attr.isChoiceType()) {
	                if (attr.getFormatOption("type")==null) {
	                	if ("radio".equals(getRenderHint())) {
	                        String[] opts=attr.getFormatOption("options").split("[|#]");

	                        for(int i=1 ; i<opts.length ; i+=2) {
	                        	if (!"?".equals(opts[i])) {
	                        		w.printf("<input type='radio' name=\"%s\" value='%s' %s>%s</input>", id, opts[i], (opts[i].equals(attr.getValue())) ? "checked='checked'" : "", opts[i]);
	                        	}
	                        }
	                	}
	                	else {
	                		w.printf("<select name=\"%s\" class='pn-normal' %s>%s</select>", id, onChangeEvent, renderEnumerationAsOptions(attr.getFormatOption("options"), attr.getValue()));
	                	}
	                }
	                else {
	                    onLoad="loadChoiceOptions($this,$value,"+attr.getChoiceTypeName()+")";
	                    w.printf("<select name=\"%s\" class='pn-normal'/>", id);
	                }
	            }
	            else if (attr.isDateType()) {
	                String dateFormat=attr.getFormatOption("pattern");
	                int size=(dateFormat==null) ? 10 : dateFormat.length();
	                w.printf("<input name=\"%s\" class='portlet-form-input-field' %s size='%d' type='text' value='%s'/>", id, onChangeEvent, size, attr.getValue());
	            }
	            else if (attr.isYesornoType()) {
	            	if ("checkbox".equals(renderHint)) {
	            		w.printf("<input name=\"%s\" type='checkbox' value='Yes' class='pn-normal' %s %s/>", id, ("Yes".equals(attr.getValue())) ? "checked='checked'" : "", onChangeEvent);
	            	}
	            	else if("radio".equals(renderHint)) {
	            		w.printf("<input name=\"%s\" type='radio' value='No' class='pn-normal' %s %s>No</input>", id, ("No".equals(attr.getValue())) ? "checked='checked'" : "", onChangeEvent);
	            		w.printf("<input name=\"%s\" type='radio' value='Yes' class='pn-normal' %s %s>Yes</input>", id, ("Yes".equals(attr.getValue())) ? "checked='checked'" : "", onChangeEvent);
	            	}
	            	else {
	            		w.printf("<select name=\"%s\" class='pn-normal' %s>%s</select>", id, onChangeEvent, renderEnumerationAsOptions(YES_OR_NO_FORMAT, attr.getValue()));
	            	}
	            }
	            else if (attr.isNoteType()) {
	                w.printf("<textarea name=\"%s\" class='portlet-form-input-field' %s rows='3' style='width:100%%'>%s</textarea>", id, onChangeEvent, attr.getValue());
	            }
	            
	            w.printf("</td><td class='portlet-msg-error'>%s</td></tr></table>", Functions.findErrors(attr));
	    
	            if (onLoad!=null) {
	                String s=onLoad;
	    
	                if (s.contains("$this")) {
	                    s=s.replace("$this", "findElementsByName(\""+id+"\")[0]");
	                }
	    
	                if (s.contains("$value")) {
	                    s=s.replace("$value", "'"+attr.getValue()+"'");
	                }
	                
	                w.printf("<script type='text/javascript'>%s</script>", s);
	            }
	        }
	        
	        return ret.toString();
		}
		catch(Throwable t) {
			throw new RenderingError("Failed to render attribute id:'"+attr.getId()+"', format:'"+attr.getFormat()+"' value:'"+attr.getValue()+"'", t);
		}
	}

	/**
	 * Validate that the values contained in the model (at a specific xpath) are valid. If
	 * errors are found, the details are added to the attribute as a sub-attribute with the
	 * id 'error'. The value of this attribute indicates the error type.
	 * @param model Model containing data to be validated
	 * @param boundTo XPath expression identifying attribute to validate.
	 * @return true if any errors are found, false otherwise.
	 */
	protected boolean applyAttributeValidation(Type model, String boundTo) {
	    // If we're not bound to anything, don't validate anything.
	    if (boundTo==null) {
	        return false;
	    }
	
	    com.ail.core.Attribute attr=(com.ail.core.Attribute)model.xpathGet(boundTo);
	    boolean error=false;
	    
	    // if there's an error there already, remove it.
	    try {
	    	Functions.removeErrorMarkers(attr);
	    }
	    catch(TypeXPathException e) {
	        // ignore this - it'll get thrown if there weren't any errors
	    }
	
	    // Check if the value is undefined or invalid and add the error marker as appropriate
	    if (attr.isUndefined() && !"no".equals(attr.getFormatOption("required"))) {
	        Functions.addError("error",  "required", attr);
	        error=true;
	    }
	    else if (!attr.isChoiceType() && attr.isInvalid()) {
	        Functions.addError("error", "invalid", attr);
	        error=true;
	    }
	
	    return error;
	}

	protected String renderAttributePageLevel(Type model, String boundTo, String rowContext, PortletSession session) throws IllegalStateException, IOException {
	    // If we're not bound to anything, output nothing.
	    if (boundTo==null) {
	        return "";
	    }
	
	    String ret="";
	    com.ail.core.Attribute attr=(com.ail.core.Attribute)model.xpathGet(boundTo);
	
	    if (attr.isChoiceType() && !attr.isChoiceSlaveType() && attr.getFormatOption("type")!=null) {
	        String optionTypeName=attr.getFormatOption("type");
	        Choice choice=(Choice)(new CoreProxy().newProductType(((Quotation)session.getAttribute("quotation")).getProductTypeId(), optionTypeName));
	        ret=choice.renderAsJavaScriptArray(optionTypeName);
	    }
	
	    return ret;
	}

	/**
	 * Check if 'request' contains a new value for the 'boundTo', and if it does, update model with
	 * the new value. If row is other than -1 it is taken to indicate the row within a scroller
	 * that boundTo relates to.
	 * @param model Model to be updated
	 * @param boundTo xpath expression pointing into 'model' at the property to be updated.
	 * @param row The row if the attribute is in a scroller, otherwise -1.
	 * @param request The request whose parameters should be checked.
	 * @return potentially modified model
	 */
	protected Type applyAttributeValues(Type model, String boundTo, String rowContext, ActionRequest request) {
	    // If we're not bound to anything, apply nothing.
	    if (boundTo!=null) {
	        String name=Functions.xpathToId(rowContext+boundTo);
	        
	        if (request.getParameter(name)!=null) {
	            model.xpathSet(boundTo+"/value", request.getParameter(name).trim());
	        }
	        else if (request.getParameter(name)==null && "checkbox".equals(getRenderHint())) {
	            model.xpathSet(boundTo+"/value", "No");
	        }
	    }
	    
	    return model;
	}

    /**
     * Render an AttributeField's choice list as a set of HTML options for use in a select element.
     * See {@link com.ail.core.Attribute} for details of the choice format.
     * @param format Choice string
     * @param selected The value of the option to show as selected, or null if no value is selected.
     * @return Option line as a string.
     */
    private String renderEnumerationAsOptions(String format, String selected) {
        StringBuffer ret=new StringBuffer();

        String[] opts=format.split("[|#]");

        for(int i=1 ; i<opts.length ; i+=2) {
            if (opts[i].equals(selected)) {
                ret.append("<option selected='yes'>"+opts[i]+"</option>");
            }
            else {
                ret.append("<option>"+opts[i]+"</option>");
            }
        }
        
        return ret.toString();
    }
}
