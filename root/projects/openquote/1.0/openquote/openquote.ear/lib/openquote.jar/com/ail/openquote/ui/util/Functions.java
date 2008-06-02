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
package com.ail.openquote.ui.util;

import static com.ail.core.Attribute.YES_OR_NO_FORMAT;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.portlet.ActionRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderResponse;
import javax.portlet.RenderRequest;

import com.ail.core.Attribute;
import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.core.TypeEnum;
import com.ail.core.TypeXPathException;
import com.ail.openquote.Quotation;
import com.ail.openquote.ui.util.Choice;

/**
 * This class defines a collection of functions used by the classes in {@link com.ail.openquote.ui}.
 */
public class Functions {
    private static SimpleDateFormat longFormat=new SimpleDateFormat("d MMMMM, yyyy");

    /**
     * Render a Java Enumeration as an HTML option list to be used in a select. Note that the
     * enumeration must be based on {@link TypeEnum}.
     * @param Enumeration type
     * @return Option list as a string.
     */
    public static String renderEnumerationAsOptions(Class<? extends TypeEnum> clazz) {
        return renderEnumerationAsOptions(clazz, null);
    }
    
    /**
     * Render a Java Enumeration as an HTML option list to be used in a select. Note that the
     * enumeration must be based on {@link TypeEnum}.
     * @param clazz Enumeration type
     * @param selected Enum value to show as selected.
     * @return Option list as a string.
     */
    public static String renderEnumerationAsOptions(Class<? extends TypeEnum> clazz, TypeEnum selected) {
        try {
            StringBuffer ret=new StringBuffer();
            TypeEnum[] values;
            values = (TypeEnum[])clazz.getMethod("values").invoke(null);
            
            for(TypeEnum en: values) {
                if (selected!=null && selected.equals(en)) {
                    ret.append("<option selected='yes'>"+en.longName()+"</option>");
                }
                else {
                    ret.append("<option>"+en.longName()+"</option>");
                }
            }
            
            return ret.toString();
        }
        catch(Exception e) {
            throw new AssertionError("Failed to build an option list for: "+clazz.getName()+". Cause was: "+e.toString());
        }
    }

    /**
     * Render an AttributeField's choice list as a set of HTML options for use in a select element.
     * See {@link com.ail.core.Attribute} for details of the choice format.
     * @param format Choice string
     * @return Option line as a string.
     */
    public static String renderEnumerationAsOptions(String format) {
        return renderEnumerationAsOptions(format, null);
    }

    /**
     * Render an AttributeField's choice list as a set of HTML options for use in a select element.
     * See {@link com.ail.core.Attribute} for details of the choice format.
     * @param format Choice string
     * @param selected The value of the option to show as selected, or null if no value is selected.
     * @return Option line as a string.
     */
    public static String renderEnumerationAsOptions(String format, String selected) {
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

    /**
     * Render an AttributeField on the UI. This is quite a common requirement throughout the classes
     * of the ui package, so it's put here for convenience. The result of calling this method
     * is some kind of HTML form element (input, select, textarea, etc) being returned as a String.
     * @param data The 'model' (in MVC terms) containing the AttributeField to be rendered
     * @param boundTo An XPath expressing pointing at the AttributeField in 'data' that we're rendering.
     * @param rowContext If we're rendering into a scroller, this'll be the row number in xpath predicate format (e.g. "[1]"). Otherwise ""
     * @param onChange JavaScript onChange event
     * @param onLoad JavaScript onLoad event
     * @return The HTML representing the attribute as a form element.
     * @throws IllegalStateException
     * @throws IOException
     */
    public static String renderAttribute(Type model, String boundTo, String rowContext, String onChange, String onLoad) throws IllegalStateException, IOException {
        // Create the StringWriter - out output will go here.
        StringWriter ret=new StringWriter();
        PrintWriter w=new PrintWriter(ret);

        String id=xpathToId(rowContext+boundTo);
        com.ail.core.Attribute attr=(com.ail.core.Attribute)model.xpathGet(boundTo);
        String onChangeEvent=(onChange!=null) ? "onchange='"+onChange+"'" : "";
        
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
                int size=(pattern==null) ? 7 : pattern.length();
                w.printf("<input name=\"%s\" class='portlet-form-input-field' style='text-align:right' size='%d' %s type='text' value='%s'/>", id, size, onChangeEvent, attr.getValue());
            }
            else if (attr.isCurrencyType()) {
                w.printf("<input name=\"%s\" class='portlet-form-input-field' style='text-align:right' %s size='7' type='text' value='%s'/>", id, onChangeEvent, attr.getValue());
            }
            else if (attr.isChoiceMasterType()) {
                onLoad="loadChoiceOptions($this,$value,"+attr.getChoiceTypeName()+")";
                onChange="updateSlaveChoiceOptions(document.getElementsByName('"+id+"')[0], "+attr.getChoiceTypeName()+", '"+attr.getId()+"', '"+attr.getChoiceSlave()+"');";
                w.printf("<select name=\"%s\" onchange=\"%s\" class='pn-normal'/>", id, onChange);
            }
            else if (attr.isChoiceSlaveType()) {
                onLoad="loadSlaveChoiceOptions($this,$value,"+attr.getChoiceTypeName()+",'"+attr.getChoiceMaster()+"','"+attr.getId()+"')";
                w.printf("<select name=\"%s\" class='pn-normal'/>", id);
            }
            else if (attr.isChoiceType()) {
                if (attr.getFormatOption("type")==null) {
                    w.printf("<select name=\"%s\" class='pn-normal' %s>%s</select>", id, onChangeEvent, renderEnumerationAsOptions(attr.getFormatOption("options"), attr.getValue()));
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
                w.printf("<select name=\"%s\" class='pn-normal' %s>%s</select>", id, onChangeEvent, renderEnumerationAsOptions(YES_OR_NO_FORMAT, attr.getValue()));
            }
            else if (attr.isNoteType()) {
                w.printf("<textarea name=\"%s\" class='portlet-form-input-field' %s rows='3' cols='30'>%s</textarea>", id, onChangeEvent, attr.getValue());
            }
            
            w.printf("</td><td class='portlet-msg-error'>%s</td></tr></table>", error("attribute[id='error']", attr));
    
            if (onLoad!=null) {
                String s=onLoad;
    
                if (s.contains("$this")) {
                    s=s.replace("$this", "document.getElementsByName(\""+id+"\")[0]");
                }
    
                if (s.contains("$value")) {
                    s=s.replace("$value", "'"+attr.getValue()+"'");
                }
                
                w.printf("<script type='text/javascript'>%s</script>", s);
            }
        }
        
        return ret.toString();
    }

    public static String renderAttributePageLevel(Type model, String boundTo, String rowContext, PortletSession session) throws IllegalStateException, IOException {
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
     * Validate that the value contained in the model (at a specific xpath) are valid. If
     * errors are found, the details are added to the attribute as a sub-attribute with the
     * id 'error'. The value of this attribute indicates the error type.
     * @param model Model containing data to be validated
     * @param boundTo XPath expression identifying attribute to validate.
     * @return true if any errors are found, false otherwise.
     */
    public static boolean applyAttributeValidation(Type model, String boundTo) {
        com.ail.core.Attribute attr=(com.ail.core.Attribute)model.xpathGet(boundTo);
        boolean error=false;
        
        // if there's an error there already, remove it.
        try {
            com.ail.core.Attribute errorAttr=(com.ail.core.Attribute)attr.xpathGet("attribute[id='error']");
            attr.removeAttribute(errorAttr);
        }
        catch(TypeXPathException e) {
            // ignore this - it'll get thrown if there weren't any errors
        }

        // Check if the value is undefined or invalid and add the error marker as appropriate
        if (attr.isUndefined() && !"no".equals(attr.getFormatOption("required"))) {
            attr.addAttribute(new com.ail.core.Attribute("error", "required", "string"));
            error=true;
        }
        else if (!attr.isChoiceType() && attr.isInvalid()) {
            attr.addAttribute(new com.ail.core.Attribute("error", "invalid", "string"));
            error=true;
        }

        return error;
    }
    
    /**
     * Check if 'request' contains a new value for the 'boundTo', and if it does, update model with
     * the new value. If row is other than -1 it is taken to indicate the row within a scroller
     * that boundTo relates to.
     * @param model Model to be updated
     * @param boundTo xpath expression pointing into 'model' at the property to be updated.
     * @param row The row if the attribute is in a scroller, otherwise -1.
     * @param request The request whose parameters should be checked.
     */
    public static void applyAttributeValues(Type model, String boundTo, String rowContext, ActionRequest request) {
        String name=xpathToId(rowContext+boundTo);
        
        if (request.getParameter(name)!=null) {
            model.xpathSet(boundTo+"/value", request.getParameter(name).trim());
        }
    }
    
    /** 
     * Convert an XPath expression in to a format that will be accepted as an HTML element's id.
     * The data binding mechanism used in openquote's UI is based on xpath. A field in a UI form
     * is bound to the quote object by means of the field's 'id'; as the pages are generated the
     * IDs are give the value of an xpath expression pointing into the quote model.<p>
     * However, xpath expressions may contain characters that aren't compatible with HTML IDs (one 
     * example being the single quote character). This method converts xpaths into a form that is
     * safe to be used as IDs, and is also able to be converted back into a xpath by the {@link #idToXpath(String)}
     * method. 
     * @param XPath expression
     * @return HTML Id
     */
    public static String xpathToId(String xpath) {
        return xpath.replace('\'', '#');
    }

    /**
     * @see #xpathToId(String)
     * @param HTML Id
     * @return XPath expression
     */
    public static String idToXpath(String id) {
        return id.replace('#', '\'');
    }

    /**
     * Determine if a String is empty - null or zero length
     * @param s String to check
     * @return true if 's' is empty, false otherwise.
     */
    public static boolean isEmpty(String s) {
        return (s==null || s.length()==0);
    }
    
    /**
     * Convert null strings into empty strings. When a UI component is rendering it'll frequently
     * want to render null strings. The default java behavior when you ask to output a null String
     * is to write "null" to the output - which isn't what we typically want on the UI. 
     * @param s String to check
     * @return "" if the string was null, or the value of the string if it was not.
     */
    public static String hideNull(String s) {
        return (s==null) ? "" : s;
    }

    @SuppressWarnings("unchecked")
    public static Properties getOperationParameters(ActionRequest request) {
        String nm;
        String[] parts;
        Properties params=new Properties();

        for(Enumeration<String> en=request.getParameterNames() ; en.hasMoreElements() ;) {
            nm=en.nextElement();
            if (nm.startsWith("op=")) {
                for(String param: nm.split(":")) {
                    parts=param.split("=");
                    if (parts.length==2) {
                        params.put(parts[0], parts[1]);
                    }
                }
                break;
            }
            else if ("op".equals(nm)) {
                //TODO handle things like 'op=Save:arg=immediate'
                params.put("op", request.getParameter("op"));
                break;
            }
        }
        
        return params;
    }

    /**
     * Remove error marker attributes attached to the specified object. The UI components use
     * the conversion of attaching error attributes to model element to indicate validation 
     * failures. This method strips any such markers from the object passed in. Note: It doesn't
     * attempt to walk the object tree, it will only remove markers from the object itself.
     * @param model Object to remove markers from.
     */
    public static void removeErrorMarkers(Type model) {
        ArrayList<Attribute> toDelete=new ArrayList<Attribute>();
        
        // Delete all the error attributes from the proposer. To avoid a ConcurrentModificationException
        // this is done in two stags: 1) add the error attributes to the 'toDelete' ArrayList; 2) delete
        // all the attributes in the toDelete list from the proposer.
        for(Attribute a: model.getAttribute()) {
            if(a.getId().startsWith("error.")) {
                toDelete.add(a);
            }
        }
        
        for(Attribute a: toDelete) {
            model.removeAttribute(a);
        }
    }

    /**
     * Return the name of the portal page that a render response relates to.
     * From a PageElement we don't have much information to go on if we want to
     * query the environment that the portlet we're associated with is running in.
     * In the case of the LoginSection, we need to know which portal page we're
     * deployed to in order to make the jump from the public portal to the  
     * authenticated one.
     * The action URL for non-authenticated takes this kind of form:
     *    /portal/portal/<portal-name>/<page-name>/<window-name>
     * When authenticated the same URL looks like this:
     *    /portal/auth/portal/<portal-name>/<page-name>/<window-name>
     */
    public static String getPortalPageName(RenderResponse response) {
        String[] actionUrlPart=response.createActionURL().toString().split("/");
        
        if ("auth".equals(actionUrlPart[2])) {
            return actionUrlPart[5];
        }
        else {
            return actionUrlPart[4];
        }
    }

    /**
     * Find the error (if any) associated with an element in a model.
     * @param xpath Where to look for the error
     * @param model The model to look in for the error
     * @return The error message, or "" (an empty String) if no message is found.
     */
    public static String error(String xpath, Type model) {
        try {
            return (String)model.xpathGet(xpath+"/value");
        }
        catch(TypeXPathException e) {
            return "";
        }
    }
    
    /**
     * Return a string representation of a date in "long" format. Long format is: "d MMMMM, yyyy". For example:
     * 10 November, 2007 
     * @param date
     * @return String representation of <i>date</i>
     */
    public static String longDate(Date date) {
        synchronized(longFormat) {
            return longFormat.format(date);
        }
    }

    /**
     * Products frequently refer to content from their Registry or Pageflows by "relative" URLs. This method
     * expands relative URLs into absolute product URLs, but leaves other URLs untouched. A relative URL 
     * is one that starts with "~/".
     * @param url URL to be checked and expanded if necessary
     * @param request Associated request
     * @param productTypeId Product to be used in the expanded URL
     * @return Expanded URL if it was relative, URL as passed in otherwise.
     */
    public static String expandRelativeUrl(String url, RenderRequest request, String productTypeId) {
        if (url.startsWith("~/")) {
            return "product://"+request.getServerName()+":"+request.getServerPort()+"/"+productTypeId.replace('.', '/')+url.substring(1); 
        }
        else {
            return url;
        }
    }
}
