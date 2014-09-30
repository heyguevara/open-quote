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
package com.ail.pageflow.portlet;

import static com.ail.insurance.policy.PolicyStatus.QUOTATION;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.ExceptionRecord;
import com.ail.core.ThreadLocale;
import com.ail.core.XMLString;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.pageflow.AssessmentSheetDetails;
import com.ail.pageflow.Page;
import com.ail.pageflow.PageFlow;
import com.ail.pageflow.PageFlowContext;
import com.ail.pageflow.PageFlowContextError;
import com.ail.pageflow.service.ListToOptionService.ListToOptionCommand;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

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

    private static List<String> VIEW_MODES = asList(WIZARD_MODE, XML_MODE, ASSESSMENT_SHEET_MODE, EXCEPTION_MODE);

    private CoreProxy coreProxy;
    private String statusMessage = null;
    private ListToOptionCommand listToOptionCommand;
    private AssessmentSheetDetails assessmentSheetDetails = new AssessmentSheetDetails();
    private PageFlowCommon pageFlowCommon = null;

    public SandpitPortlet() {
        coreProxy = new CoreProxy();
        listToOptionCommand = coreProxy.newCommand(ListToOptionCommand.class);
        pageFlowCommon = new PageFlowCommon();
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) {
        boolean processingComplete = false;
        PortletSession session = request.getPortletSession();
        statusMessage = null;

        try {
            coreProxy.setVersionEffectiveDateToNow();

            PageFlowContext.initialise(request, response);

            if (request.getParameter("saveAsTestcase") != null) {
                SavedPolicy q = new SavedPolicy(PageFlowContext.getPolicy());
                q.setTestCase(true);
                coreProxy.update(q);
                statusMessage = "Policy '" + q.getQuotationNumber() + "' saved as a testcase";
                processingComplete = true;
            }

            if (request.getParameter("resetQuote") != null) {
                PageFlowContext.restart();
                session.setAttribute("view", WIZARD_MODE);
                processingComplete = true;
            }

            if (request.getParameter("loadQuote") != null) {
                String selectedQuote = request.getParameter("selectedQuote");
                SavedPolicy q = (SavedPolicy) coreProxy.queryUnique("get.savedPolicy.by.quotationNumber", selectedQuote);
                if (q != null) {
                    PageFlowContext.setProductName(q.getProduct());
                    PageFlowContext.setPolicy(q.getPolicy());
                    session.setAttribute("view", WIZARD_MODE);
                    processingComplete = true;
                } else {
                    statusMessage = "Policy '" + selectedQuote + "' not found";
                }
            }

            if (!processingComplete && (request.getParameter("selectedProduct") != null || request.getParameter("selectedPageFlow") != null)) {
                String selectedProduct = request.getParameter("selectedProduct");
                String currentProduct = PageFlowContext.getProductName();

                String selectedPageFlow = request.getParameter("selectedPageFlow");
                String currentPageFlow = PageFlowContext.getPageFlowName();
                
                String selectedView = request.getParameter("selectedView");

                if (selectedProduct != null && !selectedProduct.endsWith("?")) {
                    if (!selectedProduct.equals(currentProduct)) {
                        PageFlowContext.selectProductName(selectedProduct);
                        selectedView = WIZARD_MODE;
                        processingComplete = true;
                    }
                } 

                if (selectedPageFlow!=null && !selectedPageFlow.endsWith("?")) {
                    if (!selectedPageFlow.equals(currentPageFlow)) {
                        PageFlowContext.selectPageFlowName(selectedPageFlow);
                        selectedView = WIZARD_MODE;
                        processingComplete = true;
                    }
                }

                if (!"XML".equals(getCurrentView(request)) && "XML".equals(selectedView)) {
                    session.setAttribute("policyXml", coreProxy.toXML(PageFlowContext.getPolicy()));
                    processingComplete = true;
                } else if ("XML".equals(getCurrentView(request)) && !"XML".equals(selectedView)) {
                    XMLString policyXml = new XMLString(request.getParameter("policyXml"));
                    session.setAttribute("policyXml", policyXml);
                    Policy quote = coreProxy.fromXML(Policy.class, policyXml);
                    PageFlowContext.setPolicy(quote);
                    session.removeAttribute("policyXml");
                    processingComplete = true;
                }

                session.setAttribute("view", selectedView);
            }

            if (!processingComplete && request.getParameter("selectedPage") != null) {
                String selectedPage = request.getParameter("selectedPage");
                String currentPage = PageFlowContext.getCurrentPageName();
                
                if (!selectedPage.endsWith("?")) {
                    if (!selectedPage.equals(currentPage)) {
                        PageFlowContext.setNextPageName(selectedPage);
                        processingComplete = true;
                    }
                }
            }

            if (!processingComplete && processingQuotation(request)) {
                Policy quote = PageFlowContext.getPolicy();
                int exceptionCount = (quote != null) ? quote.getException().size() : 0;

                pageFlowCommon.processAction(request, response);

                try {
                    if (PageFlowContext.getPolicy() != null && PageFlowContext.getPolicy().getException().size() != exceptionCount) {
                        session.setAttribute("view", EXCEPTION_MODE);
                    }
                } catch (IllegalStateException e) {
                    // Ignore IllegalState exceptions, they simply indicate that
                    // the Quit button has been selected in the quote - that
                    // invalidates the session which prevents
                    // getCurrentQuotation from being able to get the Policy as
                    // there is no session to get it from! AFAIK there is no way
                    // to detect that the session has been invalidated, other
                    // than to try and access it.
                }
            }
        } 
        catch (PageFlowContextError e) {
            // This is bad! We won't even be able to redirect to the exception page, so give up.
            throw e;
        }
        catch (Throwable cause) {
            Policy quote = PageFlowContext.getPolicy();

            if (quote == null) {
                quote = new Policy();
                PageFlowContext.setPolicy(quote);
            }

            quote.addException(new ExceptionRecord(cause));

            try {
                pageFlowCommon.persistQuotation(quote);
            } catch (Throwable th) {
                // Switch to the exception view for 'cause' - not th. The user is probably
                // more interested in what error cause us to be handling exceptions in the
                // first place rather than any exception we encountered while handling them.
                session.setAttribute("exception", new ExceptionRecord(cause));

                // Dump th to the console just for information.
                th.printStackTrace();
            }

            session.setAttribute("view", EXCEPTION_MODE);
        }
        finally {
            PageFlowContext.destroy();
        }
    }

    @Override
    public void doView(RenderRequest request, RenderResponse response) throws IOException {
        response.setContentType("text/html");

        try {
            PageFlowContext.initialise(request, response);

            renderDebugPanel(request, response);

            if (statusMessage != null) {
                renderSandpitMessage(response, statusMessage, "");
            } else if (processingQuotation(request)) {
                if (WIZARD_MODE.equals(getCurrentView(request))) {
                    renderPageFlowView(request, response);
                } else if (ASSESSMENT_SHEET_MODE.equals(getCurrentView(request))) {
                    renderAssessmentSheetView(request, response);
                } else if (EXCEPTION_MODE.equals(getCurrentView(request))) {
                    renderExceptionsView(request, response);
                }
            } else {
                renderSandpitMessage(response, "No product selected.", "Please select a Product and a PageFlow in the debug panel above.");
            }
        }
        catch (PageFlowContextError e) {
            // This is bad! We won't even be able to redirect to the exception page, so give up.
            throw e;
        }
        catch (Throwable t) {
            handleSandpitException(request, response, t);
        }
        finally {
            PageFlowContext.destroy();
        }
    }

    private void renderSandpitMessage(RenderResponse response, String message, String help) throws IOException {
        PrintWriter out=response.getWriter();
        out.write("<div class='sandpit-pageflow-panel'>");
        out.write("<div class='sandpit-message'>");
        out.write("<div class='sandpit-message'>"+message+"</div>");
        out.write("<div class='sandpit-message-help'>"+help+"</div>");
        out.write("</div>");
        out.write("</div>");
    }

    private void renderAssessmentSheetView(RenderRequest request, RenderResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        if (PageFlowContext.getPolicy() != null && PageFlowContext.getPolicy().getAssessmentSheet() != null) {
            out.write("<div class='sandpit-pageflow-panel'>");
            assessmentSheetDetails.renderResponse(request, response, PageFlowContext.getPolicy());
            out.write("</div>");
        } else {
            renderSandpitMessage(response, "No assessment sheet available", "The assessment sheet can only be displayed once the policy being processed has been rated.");
        }
    }

    private void renderPageFlowView(RenderRequest request, RenderResponse response) throws IOException, BaseException {
        PortletSession session = PageFlowContext.getRequest().getPortletSession();
        PrintWriter outs = response.getWriter();

        outs.write("<div class='sandpit-pageflow-panel'>");

        int exceptionCount = (PageFlowContext.getPolicy() != null) ? PageFlowContext.getPolicy().getException().size() : 0;

        pageFlowCommon.doView(request, response);

        if (PageFlowContext.getPolicy() != null && PageFlowContext.getPolicy().getException().size() != exceptionCount) {
            session.setAttribute("view", EXCEPTION_MODE);
            doView(request, response);
        }

        outs.write("</div>");
    }

    private void handleSandpitException(RenderRequest request, RenderResponse response, Throwable cause) throws IOException {
        try {
            PortletSession session=PageFlowContext.getRequest().getPortletSession();
            
            Policy policy = PageFlowContext.getPolicy();
    
            // All exceptions are associated with a quotation. However, if the
            // quote itself could not be initialised (which is quite likely if
            // the product's Policy.xml has errors) then create a dummy quote
            // here so that the sandpit's exception view can work normally.
            if (policy == null) {
                policy = new Policy();
                PageFlowContext.setPolicy(policy);
            }
    
            policy.addException(new ExceptionRecord(cause));
    
            // If we fail to persist the quote now then things must be very
            // broken. Add the details of this exception to the quote too.
            try {
                pageFlowCommon.persistQuotation(policy);
            } catch (Throwable th) {
                // ignore this, we're in the sandpit handling an error anyway,
                // capturing
                // the details of errors during error handling isn't useful.
            }
    
            session.setAttribute("view", EXCEPTION_MODE);
    
            // we've probably already written something - half formed - to the
            // output stream before the exception was thrown. Reset the buffer
            // so we can display the debug pane and exception cleanly.
            response.resetBuffer();
    
            try {
                renderDebugPanel(request, response);
            } catch (BaseException e) {
                // ignore this, we're in the sandpit handling an error anyway,
                // capturing the details of errors during error handling isn't useful.
            }
    
            renderExceptionsView(request, response);
        }
        catch(Throwable t) {
            // If all else fails, dump details to the log
            cause.printStackTrace();
        }
    }

    /**
     * Are we portlet processing a pageflow, or is it waiting to be told which
     * product and pageflow to process for?
     * 
     * @return true if we've been told which product to quote for, false
     *         otherwise.
     */
    private boolean processingQuotation(PortletRequest request) {
        return PageFlowContext.getProductName() != null && PageFlowContext.getPageFlowName() != null;
    }

    private void renderExceptionsView(RenderRequest request, RenderResponse response) throws IOException {
        Policy policy=PageFlowContext.getPolicy();
        
        if ((policy.getException()==null || policy.getException().size()==0) && request.getPortletSession().getAttribute("exception") == null) {
            renderSandpitMessage(response, "No Exceptions available", "");
            return;
        }
        
        try {
            PrintWriter out=response.getWriter();

            out.write("<div class='sandpit-pageflow-panel'>");

            out.printf("<table width='100%%'>");

            if (policy != null && policy.getException() != null && policy.getException().size() != 0) {
                // Sort exceptions into date order
                for (ExceptionRecord er : policy.getException()) {
                    renderExceptionRecord(out, er);
                }
            }

            if (request.getPortletSession().getAttribute("exception") != null) {
                ExceptionRecord er = (ExceptionRecord) request.getPortletSession().getAttribute("exception");
                renderExceptionRecord(out, er);
            }

            out.write("</table>");
            out.write("</div>");
        } catch (Exception e) {
            // okay, if we couldn't even render the exception to the user then
            // we really are stuck. The best we can do is dump something to the
            // log.
            e.printStackTrace();
        }
    }

    private void renderExceptionRecord(PrintWriter w, ExceptionRecord er) {
        DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, ThreadLocale.getThreadLocale());
        DateFormat timeFormat=new SimpleDateFormat("kk:mm:ss.SSS");
        
        // truncate at the very end (i.e. don't truncate at all) by default
        int truncateAtLine = er.getStack().size();

        w.printf("<tr><th align='left'>%s</th><th align='right'>%s %s</th></tr>", er.getReason(), dateFormat.format(er.getDate()), timeFormat.format(er.getDate()));
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

    private void renderDebugPanel(RenderRequest request, RenderResponse response) throws IOException, BaseException {
        boolean disableSaveAsTestCaseButton = true;
        boolean disableResetButton = true;
        boolean disablePageList = true;
        boolean disabledPageFlowList = true;
        
        String product = PageFlowContext.getProductName();
        Policy policy = PageFlowContext.getPolicy();
        String pageFlowName = PageFlowContext.getPageFlowName();
        PageFlow pageFlow = PageFlowContext.getPageFlow();

        disabledPageFlowList = (product == null || "?".equals(product));

        // we're processing a quote and we've actually quoted - so allow a save
        // as testcase.
        disableSaveAsTestCaseButton = (policy == null || !QUOTATION.equals(policy.getStatus()));

        // hide the pageList if we don't have both the policy and the pageflow
        disablePageList = (PageFlowContext.getPageFlow() == null);

        // if we're in a quote, enable the reset button.
        disableResetButton = (pageFlow == null);

        PrintWriter w = response.getWriter();

        w.printf("<form name='productDebug' action='%s' method='post'>", response.createActionURL());
        w.printf("<div class='sandpit-debug-panel'>");
        w.printf("<span id='navigator'>");
        w.printf("<select id='selectedProduct' name='selectedProduct' onChange='submit()' class='portlet-form-input-field'>%s</select>", pageFlowCommon.buildProductSelectOptions(product));
        w.printf("<select id='selectedPageFlow' name='selectedPageFlow' %s onChange='submit()' class='portlet-form-input-field'>%s</select>", disabledPageFlowList ? "disabled=disabled" : "", pageFlowCommon.buildPageFlowSelectOptions(product, pageFlowName));
        w.printf("<select id='selectedPage' name='selectedPage' %s onChange='submit()' class='portlet-form-input-field'>%s</select>", disablePageList ? "disabled=disabled" : "", buildPageSelectOptioon());
        w.printf("</span>");

        w.printf("<span id='load'>");
        w.printf(" <input placeholder='quote number' type='text' name='selectedQuote' class='pn-normal'/>");
        w.printf(" <input type='submit' name='loadQuote' value='Load' data-hint='enter something!' class='portlet-form-input-field'/>");
        w.printf("</span>");

        w.printf("<span id='view'>");
        w.printf("View:");
        w.printf("<select name='selectedView' onChange='submit()' class='portlet-form-input-field'>%s</select>", buildViewSelectOptions(getCurrentView(request)));
        w.printf("</span>");

        w.printf("<span id='save'><input type='checkbox' name='saveAsTestcase' %s value='saveAsTestCase' class='portlet-form-input-field'/> Test case</span>", disableSaveAsTestCaseButton ? "disabled=disabled"
                : "");

        w.printf("<span id='reset'><input id='resetQuote' type='submit' name='resetQuote' %s value='' class='portlet-form-input-field'/></span>", disableResetButton ? "disabled=disabled" : "");

        w.printf("</div>");

        // The XML editor textarea is handled as a part of the debug panel. It
        // might be nice if it were a separate UI component, but
        // a) it's so simple it isn't worth the effort; and,
        // b) it needs to be included in the debug panel's form so that
        //    switching modes also submits the XML content.
        if ("XML".equals(getCurrentView(request))) {
            renderXmlView(request, w);
        }

        w.printf("</form>");
    }

    private void renderXmlView(RenderRequest request, PrintWriter w) {
        XMLString policyXml = (XMLString) request.getPortletSession().getAttribute("policyXml");
        if (policyXml != null) {
            w.write("<div class='sandpit-pageflow-panel'>");
            w.write("<textarea id='policyXml' name='policyXml'>"+policyXml.toString()+"</textarea>");
            w.write("</div>");

            w.write("<script type='text/javascript'>");
            w.write("  var editor = CodeMirror.fromTextArea(document.getElementById('policyXml'), {");
            w.write("     mode: {name: 'xml', alignCDATA: true},");
            w.write("     lineNumbers: true");
            w.write("  });");
            w.write("</script>");
        }
    }

    /**
     * Build an html option list showing the view modes available. The
     * "current view" is selected.
     * 
     * @param view
     * @return HTML option list
     * @throws BaseException
     */
    private String buildViewSelectOptions(String view) throws BaseException {
        Collection<String> disabled=new ArrayList<String>();
        Policy policy = PageFlowContext.getPolicy();
         
        if (PageFlowContext.getProductName()==null || PageFlowContext.getPageFlowName()==null) {
            disabled.add(XML_MODE);
        }
        
        if (policy==null || policy.getAssessmentSheet()==null || policy.getAssessmentSheet().getLineCount()==0) {
            disabled.add(ASSESSMENT_SHEET_MODE);
        }
        
        // Disable Exception View unless we have a policy and that policy has
        // exceptions in it, or there is an exception attribute in the session.
        if (!(policy!=null && 
              policy.getException()!=null && 
              policy.getException().size()!=0) &&
            !(PageFlowContext.getRequest().getPortletSession().getAttribute("exception")!=null)) {
            disabled.add(EXCEPTION_MODE);
        }
        
        listToOptionCommand.setOptionsArg(VIEW_MODES);
        listToOptionCommand.setDisabledOptionsArg(disabled);
        listToOptionCommand.setExcludeUnknownArg(true);
        listToOptionCommand.setUnknownOptionArg(null);
        listToOptionCommand.setSelectedArg(view);
        listToOptionCommand.invoke();
        return listToOptionCommand.getOptionMarkupRet();
    }

    /**
     * Build an option list for the sandpit's page list. Based on the currently
     * selected pageflow and policy, populate the list with all of the pages in
     * the pageflow and mark the page that the policy is currently at as
     * selected.
     */
    private String buildPageSelectOptioon() throws BaseException {
        String selected = PageFlowContext.getCurrentPageName();
        List<String> pageNames = new ArrayList<String>();

        PageFlow pageFlow=PageFlowContext.getPageFlow();
        
        if (pageFlow != null) {
            pageNames = extract(pageFlow.getPage(), on(Page.class).getId());
        }

        if (PageFlowContext.getNextPageName()!=null) {
            selected=PageFlowContext.getNextPageName();
        }
        
        listToOptionCommand.setOptionsArg(pageNames);
        listToOptionCommand.setSelectedArg(selected);
        listToOptionCommand.setExcludeUnknownArg(false);
        listToOptionCommand.setUnknownOptionArg("Page?");
        listToOptionCommand.invoke();

        return listToOptionCommand.getOptionMarkupRet();
    }

    /**
     * Figure out which view mode we're in based on the session.
     * 
     * @param request
     * @return current view mode.
     */
    private String getCurrentView(RenderRequest request) {
        String view = (String) request.getPortletSession().getAttribute("view");
        return (view != null) ? view : VIEW_MODES.get(0);
    }

    /**
     * Figure out which view mode we're in based on the session.
     * 
     * @param request
     * @return current view mode.
     */
    private String getCurrentView(ActionRequest request) {
        String view = (String) request.getPortletSession().getAttribute("view");
        return (view != null) ? view : VIEW_MODES.get(0);
    }
}
