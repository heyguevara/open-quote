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
package com.ail.openquote.portlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.ExceptionRecord;
import com.ail.core.XMLString;
import com.ail.core.product.ProductDetails;
import com.ail.core.product.listproducts.ListProductsCommand;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.openquote.Quotation;
import com.ail.openquote.SavedQuotation;
import com.ail.openquote.ui.AssessmentSheetDetails;
import com.ail.openquote.ui.util.QuotationCommon;

/**
 * This Portlet performs a similar function to the {@link QuotationPortlet}, but is slightly specialized to better fit into a
 * product development environment. Specializations include:
 * <ul>
 * <li>Can be started without being tied to a product.</li>
 * <li>Allows quotations to be stored as test quotes for later re-use.</li>
 * <li>Allows the quotation to be viewed (and modified) as XML at any point.</li>
 * </ul>
 */
public class SandpitPortlet extends GenericPortlet {
	private static String WIZARD_MODE="Wizard";
	private static String XML_MODE="XML";
	private static String ASSESSMENT_SHEET_MODE="Assessment sheet";
	private static String EXCEPTION_MODE="Exception";
	
    private static String[] VIEW_MODES=new String[]{WIZARD_MODE, XML_MODE, ASSESSMENT_SHEET_MODE, EXCEPTION_MODE};
    
    private AssessmentSheetDetails assessmentSheetDetails=new AssessmentSheetDetails();
    String statusMessage=null;
    
    public void processAction(ActionRequest request, ActionResponse response) {
        PortletSession session=request.getPortletSession();
        statusMessage=null;
        
        try {
            if (request.getParameter("saveAsTestcase")!=null) {
                SavedQuotation q=new SavedQuotation(QuotationCommon.getCurrentQuotation(session));
                q.setTestCase(true);
                new CoreProxy().update(q);
                statusMessage="Quotation '"+q.getQuotationNumber()+"' saved as a testcase";
            }
            else if (request.getParameter("loadQuote")!=null) {
                String selectedQuote=request.getParameter("selectedQuote");
                SavedQuotation q=(SavedQuotation)new CoreProxy().queryUnique("get.savedQuotation.by.quotationNumber", selectedQuote);
                if (q!=null) {
                	QuotationCommon.setCurrentQuotation(session, q.getQuotation());
                    session.setAttribute("product", q.getProduct());
                	session.setAttribute("view", WIZARD_MODE);
                }
                else {
                    statusMessage="Quotation '"+selectedQuote+"' not found";
                }
            }
            else if (request.getParameter("selectedProduct")!=null) {
                String selectedProduct=request.getParameter("selectedProduct");
                String selectedView=request.getParameter("selectedView");
                
                if (selectedProduct!=null && !"?".equals(selectedProduct)) {
                    if (!selectedProduct.equals(QuotationCommon.productName(request.getPortletSession(), request.getPreferences()))) {
                    	QuotationCommon.setCurrentQuotation(session, null);
                        session.setAttribute("product", selectedProduct);
                    	selectedView=WIZARD_MODE;
                    }
                }
    
                if (!"XML".equals(getCurrentView(request)) && "XML".equals(selectedView)) {
                    if (session.getAttribute("quoteXml")==null) {
                        session.setAttribute("quoteXml", new CoreProxy().toXML(QuotationCommon.getCurrentQuotation(session)));
                    }
                }
                else if ("XML".equals(getCurrentView(request)) && !"XML".equals(selectedView)) {
                    XMLString quoteXml=new XMLString(request.getParameter("quoteXml"));
                    session.setAttribute("quoteXml", quoteXml);
                    Quotation quote=(Quotation)new CoreProxy().fromXML(Quotation.class, quoteXml);
                    QuotationCommon.setCurrentQuotation(session, quote);
                    session.removeAttribute("quoteXml");
                }
                
                session.setAttribute("view", selectedView);
            
            }
            else if (processingQuotation(request.getPortletSession(), request.getPreferences())) {
            	Quotation quote=QuotationCommon.getCurrentQuotation(session);
            	int exceptionCount = (quote!=null) ? quote.getException().size() : 0;
            	
            	QuotationCommon.processAction(request, response);
            	
            	try {
	            	if (QuotationCommon.getCurrentQuotation(session).getException().size() != exceptionCount) {
	                	session.setAttribute("view", EXCEPTION_MODE);
	            	}
            	}
            	catch(IllegalStateException e) {
            		// Ignore IllegalState exceptions, they simply indicate that the Quit button has been
            		// selected in the quote - that invalidates the session which prevents getCurrentQuotation 
            		// from being able to get the Quotation as there is no session to get it from! AFAIK 
            		// there is no way to detect that the session has been invalidated, other than to try and
            		// access it.
            	}
            }
        }
        catch(Throwable t) {
        	Quotation quote=null;
        	
        	try {
        		QuotationCommon.getCurrentQuotation(session);
        	}
        	catch(IllegalStateException e) {
        		t.printStackTrace();
        		return;
        	}
        	
        	if (quote==null) {
        		quote=new Quotation();
        		QuotationCommon.setCurrentQuotation(session, quote);
        	}

    		quote.addException(new ExceptionRecord(t));

        	session.setAttribute("view", EXCEPTION_MODE);
        }
    }

    public void doView(RenderRequest request, RenderResponse response) throws IOException {
        response.setContentType("text/html");

        renderProductDebugPanel(request, response);

        PortletSession session=request.getPortletSession();
        
        try {
            if (statusMessage!=null) {
                PrintWriter out = response.getWriter();
                out.printf("<table width='100%%'><tr><td align='center'>%s</td></tr></table>", statusMessage);
            }
            else if (processingQuotation(session, request.getPreferences())) {
                if ("Wizard".equals(getCurrentView(request))) {
                	Quotation quote=QuotationCommon.getCurrentQuotation(session);
                	int exceptionCount = (quote!=null) ? quote.getException().size() : 0;

                	QuotationCommon.doView(request, response);

                	if (QuotationCommon.getCurrentQuotation(session).getException().size() != exceptionCount) {
	                	session.setAttribute("view", EXCEPTION_MODE);
	                	doView(request, response);
                	}
                }
                else if ("Assessment sheet".equals(getCurrentView(request))) {
                    if (QuotationCommon.getCurrentQuotation(session).getAssessmentSheet()!=null) {
                        assessmentSheetDetails.renderResponse(request, response, QuotationCommon.getCurrentQuotation(session));
                    }
                    else {
                        response.getWriter().print("<table width='100%'><tr><td align='center'>No assessment sheet attached to the quotation</td></tr></table>");
                    }
                }
                else if ("Exception".equals(getCurrentView(request))) {
                	renderQuoteExceptions(request, response, QuotationCommon.getCurrentQuotation(session));
                }
            }
            else {
                PrintWriter out = response.getWriter();
                out.print("<table width='100%'><tr><td align='center'>No product selected</td></tr></table>");
            }
        }
        catch(Throwable t)  {
        	Quotation quote=QuotationCommon.getCurrentQuotation(session);
        	
        	if (quote==null) {
        		quote=new Quotation();
        		QuotationCommon.setCurrentQuotation(session, quote);
        	}

    		quote.addException(new ExceptionRecord(t));

        	session.setAttribute("view", EXCEPTION_MODE);

        	doView(request, response);
        }
    }
    
    /**
     * Is the portlet doing a quote, or waiting to be told which product to quote for?
     * 
     * @param session Session we're part of
     * @return true if we've been told which product to quote for, false otherwise.
     */
    private boolean processingQuotation(PortletSession session, PortletPreferences prefs) {
        return QuotationCommon.productName(session, prefs) != null;
    }

    private void renderQuoteExceptions(RenderRequest request, RenderResponse response, Quotation quote) {
    	try {
	    	PrintWriter w=response.getWriter();
	    	if (quote!=null && quote.getException() != null && quote.getException().size() != 0) {
	        	w.printf("<table width='100%%'>");
	
	        	for(ExceptionRecord er: quote.getException()) {
	        		// truncate at the very end (i.e. don't truncate at all) by default
	        		int truncateAtLine=er.getStack().size();
	        		
	        		w.printf("<tr><th align='left'>%s</th><th align='right'>%s</th></tr>", er.getReason(), SimpleDateFormat.getInstance().format(er.getDate()));
        			w.printf("<tr><td colspan='2'><small><pre>");
        			
        			String[] stack=er.getStack().toArray(new String[er.getStack().size()]);
        			
        			for(int i=stack.length-1 ; i>=0 ; i--) {
        				if (stack[i].indexOf(er.getCatchingClassName())==0) {
        					truncateAtLine=i;
        					break;
        				}
        			}
        			
   	        		for(int i=0 ; i<truncateAtLine ; i++) {
   	        			w.printf("%s\n", stack[i]);
	        		}
   	        		
   	        		if (truncateAtLine != stack.length) {
   	        			w.printf("...");
   	        		}
   	        		
        			w.printf("</pre></small></td></tr>");
	        	}
	
	        	w.printf("</table>");
	        }
	        else {
	            response.getWriter().print("<table width='100%'><tr><td align='center'>There is no current exception</td></tr></table>");
	        }
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private void renderProductDebugPanel(RenderRequest request, RenderResponse response) throws IOException {
        boolean disableSaveAsTestCaseButton=true;
        String product=null;
         
        // Decide which buttons to turn on/off - they're disabled by default
        if (processingQuotation(request.getPortletSession(), request.getPreferences())) {

            product=(String)request.getPortletSession().getAttribute("product");
            
            Quotation quote=QuotationCommon.getCurrentQuotation(request.getPortletSession());

            // we're processing a quote and we've actually quoted - so allow a save as testcase.
            if (quote!=null && PolicyStatus.QUOTATION.equals(quote.getStatus())) {
                disableSaveAsTestCaseButton=false;
            }
        }

        PrintWriter w = response.getWriter();

        w.printf("<form name='productDebug' action='%s' method='post'>", response.createActionURL());
        w.printf( "<table width='100%%' >");
        w.printf(  "<tr class='portlet-table-alternate'>");
        w.printf(   "<td align='center'>");
        w.printf(    "Quote for:");
        w.printf(    "<select name='selectedProduct' onChange='submit()' class='pn-normal'>%s</select>", getProductSelectOptions(product));
        w.printf(   "</td>");
        w.printf(   "<td align='center'>");
        w.printf(    "Load quote:");
        w.printf(   " <input type='text' name='selectedQuote' class='pn-normal'/>");
        w.printf(   " <input type='submit' name='loadQuote' value='Load' class='portlet-form-input-field'/>");
        w.printf(   "</td>");
        w.printf(   "<td align='center'>");
        w.printf(    "View:");
        w.printf(    "<select name='selectedView' onChange='submit()' class='pn-normal'>%s</select>", getViewSelectOptions(getCurrentView(request)));
        w.printf(   "</td>");
        w.printf(   "<td align='center'><input type='submit' name='saveAsTestcase' %s value='Save as testcase' class='portlet-form-input-field'/></td>", disableSaveAsTestCaseButton ? "disabled=disabled" : "");
        w.printf(  "</tr>");
        w.printf( "</table>");

        // The XML editor textarea is handled as a part of the debug panel. It might be nice if it were a separate
        // UI component, but a) it's so simple it isn't worth the effort; and b) it needs to be included in the debug
        // panels form so that switching modes also submits the XML content.
        if ("XML".equals(getCurrentView(request))) {
            XMLString quoteXml=(XMLString)request.getPortletSession().getAttribute("quoteXml");
            if (quoteXml!=null) {
                w.printf("<table width='100%%'><tr><td>");
                w.printf("<textarea name='quoteXml' rows='20' style='width:100%%'>%s</textarea>", quoteXml.toString());
                w.printf("</td></tr></table>");
            }
        }
        
        w.printf("</form>");
    }

    /**
     * Build an html option list showing the view modes available. The "current view" is selected.
     * @param view
     * @return HTML option list
     */
    private String getViewSelectOptions(String view) {
        StringBuffer ret=new StringBuffer();
        
        for(String mode: VIEW_MODES) {
            if (mode.equals(view)) {
                ret.append("<option selected='yes'>"+mode+"</option>");
            }
            else {
                ret.append("<option>"+mode+"</option>");
            }
        }
        
        return ret.toString();
    }
    
    /**
     * Build an HTML select option string listing all the available products.
     * @return String of products on option format. May be null.
     */
    private String getProductSelectOptions(String product) {
        try {
            ListProductsCommand lpods=(ListProductsCommand)new CoreProxy().newCommand("ListProducts");

            lpods.invoke();
            
            StringBuffer ret=new StringBuffer("<option>?</option>");
            
            for(ProductDetails p: lpods.getProductsRet()) {
                if (p.getName().equals(product)) {
                    ret.append("<option selected='yes'>"+p.getName()+"</option>");
                }
                else {
                    ret.append("<option>"+p.getName()+"</option>");
                }
            }
            
            return ret.toString();
        }
        catch (BaseException e) {
            e.printStackTrace();
            return "<option>***ERROR***</option>";
        }
    }

    /** 
     * Figure out which view mode we're in based on the session. 
     * @param request
     * @return current view mode.
     */
    private String getCurrentView(RenderRequest request) {
        String view=(String)request.getPortletSession().getAttribute("view");
        return (view!=null) ? view : VIEW_MODES[0];
    }

    /** 
     * Figure out which view mode we're in based on the session. 
     * @param request
     * @return current view mode.
     */
    private String getCurrentView(ActionRequest request) {
        String view=(String)request.getPortletSession().getAttribute("view");
        return (view!=null) ? view : VIEW_MODES[0];
    }
}
