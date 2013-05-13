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
package com.ail.insurance.pageflow.portlet;

import static com.ail.insurance.policy.PolicyStatus.QUOTATION;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.ExceptionRecord;
import com.ail.core.XMLString;
import com.ail.insurance.pageflow.AssessmentSheetDetails;
import com.ail.insurance.pageflow.Page;
import com.ail.insurance.pageflow.PageFlow;
import com.ail.insurance.pageflow.util.QuotationCommon;
import com.ail.insurance.pageflow.util.PageflowContext;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.SavedPolicy;

/**
 * This Portlet performs a similar function to the {@link PageFlowPortlet}, but
 * is slightly specialized to better fit into a product development environment.
 * Specializations include:
 * <ul>
 * <li>Can be started without being tied to a product.</li>
 * <li>Allows quotations to be stored as test quotes for later re-use.</li>
 * <li>Allows the quotation to be viewed (and modified) as XML at any point.</li>
 * </ul>
 */
public class SandpitPortlet extends GenericPortlet {
    private static String WIZARD_MODE = "Wizard";
    private static String XML_MODE = "XML";
    private static String ASSESSMENT_SHEET_MODE = "Assessment sheet";
    private static String EXCEPTION_MODE = "Exception";

    private static String[] VIEW_MODES = new String[] { WIZARD_MODE, XML_MODE, ASSESSMENT_SHEET_MODE, EXCEPTION_MODE };

    private AssessmentSheetDetails assessmentSheetDetails = new AssessmentSheetDetails();
    String statusMessage = null;

    public void processAction(ActionRequest request, ActionResponse response) {
        boolean processingComplete = false;

        PortletSession session = request.getPortletSession();
        statusMessage = null;

        try {
            PageflowContext.initialise(request);
            CoreProxy core=PageflowContext.getCore();

            if (request.getParameter("saveAsTestcase") != null) {
                SavedPolicy q = new SavedPolicy(PageflowContext.getPolicy());
                q.setTestCase(true);
                core.update(q);
                statusMessage = "Policy '" + q.getQuotationNumber() + "' saved as a testcase";
                processingComplete = true;
            }
            
            if (request.getParameter("resetQuote") !=null) {
                String selectedProduct=PageflowContext.getPolicy().getProductTypeId();
                PageflowContext.setPolicy(null);
                session.setAttribute("product", selectedProduct);
                session.setAttribute("view", WIZARD_MODE);
                processingComplete = true;
            }
            
            if (request.getParameter("loadQuote") != null) {
                String selectedQuote = request.getParameter("selectedQuote");
                SavedPolicy q = (SavedPolicy) core.queryUnique("get.savedPolicy.by.quotationNumber", selectedQuote);
                if (q != null) {
                    PageflowContext.setPolicy(q.getPolicy());
                    session.setAttribute("product", q.getProduct());
                    session.setAttribute("view", WIZARD_MODE);
                    processingComplete = true;
                } else {
                    statusMessage = "Policy '" + selectedQuote + "' not found";
                }
            }
            
            if (!processingComplete && request.getParameter("selectedPage") != null && PageflowContext.getPolicy() !=null ) {
                String selectedPage = request.getParameter("selectedPage");
                String currentPage = PageflowContext.getPolicy().getPage();
                if (!"?".equals(selectedPage) && !selectedPage.equals(currentPage)) {
                    PageflowContext.getPolicy().setPage(selectedPage);
                    processingComplete = true;
                }
            } 
            
            if (!processingComplete && request.getParameter("selectedProduct") != null) {
                String selectedProduct = request.getParameter("selectedProduct");
                String selectedView = request.getParameter("selectedView");
                
                if (!"?".equals(selectedProduct)) {
                    if (!selectedProduct.equals(QuotationCommon.productName(request))) {
                        PageflowContext.setPolicy(null);
                        session.setAttribute("product", selectedProduct);
                        selectedView = WIZARD_MODE;
                        processingComplete = true;
                    }
                }

                if (!"XML".equals(getCurrentView(request)) && "XML".equals(selectedView)) {
                    if (session.getAttribute("quoteXml") == null) {
                        session.setAttribute("quoteXml", core.toXML(PageflowContext.getPolicy()));
                    }
                } else if ("XML".equals(getCurrentView(request)) && !"XML".equals(selectedView)) {
                    XMLString quoteXml = new XMLString(request.getParameter("quoteXml"));
                    session.setAttribute("quoteXml", quoteXml);
                    Policy quote = (Policy) core.fromXML(Policy.class, quoteXml);
                    PageflowContext.setPolicy(quote);
                    session.removeAttribute("quoteXml");
                }

                session.setAttribute("view", selectedView);
            }
            
            if (!processingComplete && processingQuotation(request)) {
                Policy quote = PageflowContext.getPolicy();
                int exceptionCount = (quote != null) ? quote.getException().size() : 0;

                QuotationCommon.processAction(request, response);

                try {
                    if (PageflowContext.getPolicy() != null && PageflowContext.getPolicy().getException().size() != exceptionCount) {
                        session.setAttribute("view", EXCEPTION_MODE);
                    }
                } catch (IllegalStateException e) {
                    // Ignore IllegalState exceptions, they simply indicate that
                    // the Quit button has been
                    // selected in the quote - that invalidates the session
                    // which prevents getCurrentQuotation
                    // from being able to get the Policy as there is no
                    // session to get it from! AFAIK
                    // there is no way to detect that the session has been
                    // invalidated, other than to try and
                    // access it.
                }
            }
        } catch (Throwable t) {
            Policy quote = PageflowContext.getPolicy();

            if (quote == null) {
                quote = new Policy();
                PageflowContext.setPolicy(quote);
            }

            quote.addException(new ExceptionRecord(t));
            QuotationCommon.persistQuotation(quote);

            session.setAttribute("view", EXCEPTION_MODE);
        }
    }

    public void doView(RenderRequest request, RenderResponse response) throws IOException {
        response.setContentType("text/html");

        PortletSession session = request.getPortletSession();

        try {
            PageflowContext.initialise(request);
            
            renderProductDebugPanel(request, response);

            if (statusMessage != null) {
                PrintWriter out = response.getWriter();
                out.printf("<table width='100%%'><tr><td align='center'>%s</td></tr></table>", statusMessage);
            } else if (processingQuotation(request)) {
                if (WIZARD_MODE.equals(getCurrentView(request))) {
                    Policy quote = PageflowContext.getPolicy();

                    int exceptionCount = (quote != null) ? quote.getException().size() : 0;

                    QuotationCommon.doView(request, response);

                    if (PageflowContext.getPolicy().getException().size() != exceptionCount) {
                        session.setAttribute("view", EXCEPTION_MODE);
                        doView(request, response);
                    }
                } else if (ASSESSMENT_SHEET_MODE.equals(getCurrentView(request))) {
                    if (PageflowContext.getPolicy().getAssessmentSheet() != null) {
                        assessmentSheetDetails.renderResponse(request, response, PageflowContext.getPolicy());
                    } else {
                        response.getWriter().print("<table width='100%'><tr><td align='center'>No assessment sheet attached to the quotation</td></tr></table>");
                    }
                } else if (EXCEPTION_MODE.equals(getCurrentView(request))) {
                    renderQuoteExceptions(request, response, PageflowContext.getPolicy());
                }
            } else {
                PrintWriter out = response.getWriter();
                out.print("<table width='100%'><tr><td align='center'>No product selected</td></tr></table>");
            }
        } catch (Throwable t) {
            Policy quote = PageflowContext.getPolicy();

            // All exceptions are associated with a quotation. However, if the
            // quote itself could not be initialised (which is quite likely if 
            // the product's Policy.xml has errors) then create a dummy quote
            // here so that the sandpit's exception view can work normally.
            if (quote == null) {
                quote = new Policy();
                PageflowContext.setPolicy(quote);
            }

            quote.addException(new ExceptionRecord(t));

            // If we fail to persist the quote now then things must be very 
            // broken. Add the details of this exception to the quote too.
            try {
                QuotationCommon.persistQuotation(quote);
            } catch (Throwable th) {
                quote.addException(new ExceptionRecord(th));
            }

            session.setAttribute("view", EXCEPTION_MODE);

            // we've probably already written something - half formed - to the
            // output stream before the exception was thrown. Reset the buffer
            // so the doView() below gets a clean start.
            response.resetBuffer();

            renderProductDebugPanel(request, response);

            renderQuoteExceptions(request, response, PageflowContext.getPolicy());
        }
    }

    /**
     * Is the portlet doing a quote, or waiting to be told which product to
     * quote for?
     * 
     * @param session
     *            Session we're part of
     * @return true if we've been told which product to quote for, false
     *         otherwise.
     */
    private boolean processingQuotation(PortletRequest request) {
        return QuotationCommon.productName(request) != null;
    }

    private void renderQuoteExceptions(RenderRequest request, RenderResponse response, Policy quote) {
        boolean recordsOutput=false;

        try {
            PrintWriter w = response.getWriter();

            w.printf("<table width='100%%'>");

            if (quote != null && quote.getException() != null && quote.getException().size() != 0) {
                // Sort exceptions into date order
                for (ExceptionRecord er : quote.getException()) {
                    renderExceptionRecord(w, er);
                }
            } 

            if (recordsOutput) {
                response.getWriter().print("<tr><td align='center'>There is no current exception</td></tr>");
            }

            w.printf("</table>");
        } catch (Exception e) {
            // okay, if we couldn't even render the exception to the user then
            // we really
            // are stuck. The best we can do is dump something to the log.
            e.printStackTrace();
        }
    }

    private void renderExceptionRecord(PrintWriter w, ExceptionRecord er) {
        // truncate at the very end (i.e. don't truncate at all) by default
        int truncateAtLine = er.getStack().size();

        w.printf("<tr><th align='left'>%s</th><th align='right'>%s</th></tr>", er.getReason(), SimpleDateFormat.getInstance().format(er.getDate()));
        w.printf("<tr><td colspan='2'><small><pre>");

        String[] stack = er.getStack().toArray(new String[er.getStack().size()]);

        for (int i = stack.length - 1; i >= 0; i--) {
            if (stack[i].indexOf(er.getCatchingClassName()) == 0) {
                truncateAtLine = i;
                break;
            }
        }

        for (int i = 0; i < truncateAtLine; i++) {
            w.printf("%s\n", stack[i]);
        }

        if (truncateAtLine != stack.length) {
            w.printf("...");
        }

        w.printf("</pre></small></td></tr>");
    }

    private void renderProductDebugPanel(RenderRequest request, RenderResponse response) throws IOException {
        boolean disableSaveAsTestCaseButton = true;
        boolean disableResetButton = true;
        boolean disablePageList = true;
        String product = null;
        Policy policy = null;
        PageFlow pageFlow = null;
        
        
        // Decide which buttons to turn on/off - they're disabled by default
        if (processingQuotation(request)) {

            product = (String) request.getPortletSession().getAttribute("product");
            policy = PageflowContext.getPolicy();
            pageFlow = PageflowContext.getPageFlow();
            
            // we're processing a quote and we've actually quoted - so allow a
            // save as testcase.
            disableSaveAsTestCaseButton = (policy==null || !QUOTATION.equals(policy.getStatus()));
            
            // hide the pageList if we don't have both the policy and the pageflow
            disablePageList = (policy==null || pageFlow==null);
            
            // if we're in a quote, enable the reset button.
            disableResetButton = (policy==null);
        }

        PrintWriter w = response.getWriter();

        w.printf("<form name='productDebug' action='%s' method='post'>", response.createActionURL());
        w.printf("<div class='sandpit_debug_panel'>");
        w.printf("<table width='100%%' >");
        w.printf("<tr class='portlet-table-alternate'>");
        
        w.printf("<td align='center'>");
        w.printf("Product:");
        w.printf("<select name='selectedProduct' onChange='submit()' class='portlet-form-input-field'>%s</select>", QuotationCommon.buildProductSelectOptions(product));
        w.printf("</td>");
        
        w.printf("<td align='center'>");
        w.printf("Page:");
        w.printf("<select name='selectedPage' %s onChange='submit()' class='portlet-form-input-field'>%s</select>", disablePageList ? "disabled=disabled" : "", buildPageSelectOptioon(pageFlow, policy));
        w.printf("</td>");
        
        w.printf("<td align='center'><input type='submit' name='resetQuote' %s value='Reset quote' class='portlet-form-input-field'/></td>", disableResetButton ? "disabled=disabled" : "");        
        
        w.printf("<td align='center'>");
        w.printf("Load quote:");
        w.printf(" <input type='text' name='selectedQuote' class='pn-normal'/>");
        w.printf(" <input type='submit' name='loadQuote' value='Load' class='portlet-form-input-field'/>");
        w.printf("</td>");
        
        w.printf("<td align='center'>");
        w.printf("View:");
        w.printf("<select name='selectedView' onChange='submit()' class='portlet-form-input-field'>%s</select>", getViewSelectOptions(getCurrentView(request)));
        w.printf("</td>");
        
        w.printf("<td align='center'><input type='submit' name='saveAsTestcase' %s value='Save as testcase' class='portlet-form-input-field'/></td>", disableSaveAsTestCaseButton ? "disabled=disabled" : "");

        w.printf("</tr>");
        w.printf("</table>");
        w.printf("</div>");
        // The XML editor textarea is handled as a part of the debug panel. It
        // might be nice if it were a separate
        // UI component, but a) it's so simple it isn't worth the effort; and b)
        // it needs to be included in the debug
        // panel's form so that switching modes also submits the XML content.
        if ("XML".equals(getCurrentView(request))) {
            XMLString quoteXml = (XMLString) request.getPortletSession().getAttribute("quoteXml");
            if (quoteXml != null) {
                w.printf("<table width='100%%'><tr><td>");
                w.printf("<textarea name='quoteXml' rows='20' style='width:100%%'>%s</textarea>", quoteXml.toString());
                w.printf("</td></tr></table>");
            }
        }

        w.printf("</form>");
    }

    /**
     * Build an html option list showing the view modes available. The
     * "current view" is selected.
     * 
     * @param view
     * @return HTML option list
     */
    private String getViewSelectOptions(String view) {
        StringBuffer ret = new StringBuffer();

        for (String mode : VIEW_MODES) {
            if (mode.equals(view)) {
                ret.append("<option selected='yes'>" + mode + "</option>");
            } else {
                ret.append("<option>" + mode + "</option>");
            }
        }

        return ret.toString();
    }

    /**
     * Build an option list for the sandpit's page list. Based on the currently selected pageflow and
     * policy, populat the list with all of the pages in the pageflow and mark the page that the
     * policy is currently at as selected.
     * @param pageFlow
     * @param policy
     * @return
     */
    private String buildPageSelectOptioon(PageFlow pageFlow, Policy policy) {
        StringBuffer ret = new StringBuffer();

        if (policy!=null && pageFlow!=null) {
            for(Page page: pageFlow.getPage()) {
                if (policy.getPage()!=null && policy.getPage().equals(page.getId())) {
                    ret.append("<option selected='yes'>").append(page.getId()).append("</option>");
                }
                else {
                    ret.append("<option>").append(page.getId()).append("</option>");
                }
            }
        }
        
        if (ret.length()==0) {
            ret.append("<option>?</option>");
        }
        
        return ret.toString();
    }


    /**
     * Figure out which view mode we're in based on the session.
     * 
     * @param request
     * @return current view mode.
     */
    private String getCurrentView(RenderRequest request) {
        String view = (String) request.getPortletSession().getAttribute("view");
        return (view != null) ? view : VIEW_MODES[0];
    }

    /**
     * Figure out which view mode we're in based on the session.
     * 
     * @param request
     * @return current view mode.
     */
    private String getCurrentView(ActionRequest request) {
        String view = (String) request.getPortletSession().getAttribute("view");
        return (view != null) ? view : VIEW_MODES[0];
    }
}
