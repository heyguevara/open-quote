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
package com.ail.insurance.quotation;

import static com.ail.core.Functions.productNameToConfigurationNamespace;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.insurance.pageflow.render.RenderService.RenderCommand;
import com.ail.insurance.pageflow.util.QuotationContext;
import com.ail.insurance.policy.Broker;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.Proposer;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.insurance.quotation.FetchQuoteService.FetchQuoteCommand;

/**
 * Send a notification of an event relating to a quote to the broker associated with the product 
 */
@ServiceImplementation
public class NotifyProposerByEmailService extends Service<NotifyProposerByEmailService.NotifyProposerByEmailArgument> {
    private static final long serialVersionUID = -4915889686192216902L;
    private String configurationNamespace = null;

    /**
     * Service to notify a party of the existence of a quote. Typical implementations of this service include
     * notifying proposers, brokers and/or carriers of the creation of a new quotation. The implementation might
     * send notification by a number of channels including emails and web service calls.</p>
     * The caller may specify the quotation either by quote number or by the instance of the quote itself.
     */
    @ServiceArgument
    public interface NotifyProposerByEmailArgument extends Argument {
        String getQuotationNumberArg();
        
        void setQuotationNumberArg(String quotationNumberArg);
        
        Policy getPolicyArg();
        
        void setPolicyArg(Policy policyArg);
    }

    @ServiceCommand(defaultServiceClass=NotifyProposerByEmailService.class)
    public interface NotifyProposerByEmailCommand extends Command, NotifyProposerByEmailArgument {
    }

    /**
     * Return the product type id of the policy we're assessing the risk for as the
     * configuration namespace. The has the effect of selecting the product's configuration.
     * @return product type id
     */
    public String getConfigurationNamespace() {
        if (configurationNamespace==null) {
            return super.getConfigurationNamespace();
        }
        else {
            return configurationNamespace;
        }
    }
        
    private void setConfigurationNamespace(String configurationNamespace) {
        this.configurationNamespace=configurationNamespace;
    }
    
    /** The 'business logic' of the entry point. */
    public void invoke() throws PreconditionException, BaseException {
        setConfigurationNamespace(null);        
        
        // Fail if there's no quote number
        if (args.getQuotationNumberArg()==null || args.getQuotationNumberArg().length()==0) {
            throw new PreconditionException("args.getQuotationNumberArg()==null || args.getQuotationNumberArg().length()==0");
        }

        SavedPolicy savedPolicy=(SavedPolicy)getCore().queryUnique("get.savedPolicy.by.quotationNumber", args.getQuotationNumberArg());
        
        if (savedPolicy==null) {
            throw new PreconditionException("core.queryUnique(get.savedPolicy.by.quotationNumber, "+args.getQuotationNumberArg()+")==null");
        }
        
        // Fail if there's not broker associated with the quote
        if (savedPolicy.getPolicy().getBroker()==null) {
            throw new PreconditionException("savedQuotation.getQuotation().getBroker()==null");
        }
        
        // Fail if the broker doesn't have a quote email address
        if (savedPolicy.getPolicy().getBroker().getQuoteEmailAddress()==null) {
            throw new PreconditionException("savedQuotation.getQuotation().getBroker().getQuoteEmailAddress()==null");
        }
        
        setConfigurationNamespace(productNameToConfigurationNamespace(savedPolicy.getProduct()));

        try {
            emailNotification(savedPolicy);
        }
        catch(MessagingException e) {
        	throw new PreconditionException("Failed to send notification email to: "+getCore().getParameterValue("smtp-server", "localhost")+" for quote: "+savedPolicy.getQuotationNumber()+". "+e.toString(), e);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void emailNotification(SavedPolicy savedPolicy) throws Exception {
        Session session=null;
        MimeMessage message=null;
        MimeMultipart multipart=null;

    	QuotationContext.setPolicy(savedPolicy.getPolicy());

    	Proposer proposer=(Proposer)savedPolicy.getPolicy().getProposer();
    	Broker broker=savedPolicy.getPolicy().getBroker();
    	String smtpServer = getCore().getParameterValue("smtp-server", "localhost");
        String fromAddress = broker.getEmailAddress();
        String toAddress = proposer.getEmailAddress();
        
        // TODO Load properties from configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpServer);

        session = Session.getDefaultInstance(props, null);
        message = new MimeMessage(session);
        
        message.addRecipients(Message.RecipientType.TO, toAddress);
        message.setFrom(new InternetAddress(fromAddress));

        // Example subject: "Motor Plus Policy: QF0001"
        message.setSubject(savedPolicy.getPolicy().getProductName()+" "+
        				   savedPolicy.getPolicy().getStatus().getLongName()+": "+
        				   savedPolicy.getQuotationNumber());
             
        multipart=new MimeMultipart("mixed");

        multipart.addBodyPart(createSummaryAttachment(savedPolicy));

    	BodyPart docAttachment=createQuoteDocumentAttachment(savedPolicy);
    	if (docAttachment!=null) {
    		multipart.addBodyPart(docAttachment);
    	}

        message.setContent(multipart);
        
        Transport.send(message);
    }
    
    /**
     * Create a MimeBodyPart containing a summary of the quotation rendered as HTML.
     * @param savedPolicy Quote to render the assessment sheet for.
     * @return BodyPart containing rendered output.
     * @throws MessagingException
     * @throws BaseException 
     */
    private BodyPart createSummaryAttachment(SavedPolicy savedPolicy) throws MessagingException, BaseException {
        // Use the UI's rendered to create the HTML output - email is a kind of UI after all!
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        PrintWriter writer=new PrintWriter(baos);
        insertStyles(writer);

        RenderCommand rbqs=getCore().newCommand("ProposerQuotationSummary", "text/html", RenderCommand.class);
        rbqs.setModelArgRet(savedPolicy.getPolicy());
        rbqs.invoke();

        writer.append(rbqs.getRenderedOutputRet());
        writer.close();
        String content=new String(baos.toByteArray());
        
        BodyPart bp=new MimeBodyPart();
        bp.setHeader("Content-ID", "<summary.html>");
        bp.setContent(content, "text/html");

        return bp;
    }

    /**
     * Create a MimeBodyPart holding the quote document (PDF)
     * @return body part, or null if the quotation doc has not been generated yet.
     * @throws BaseException If the quote document fails to load.
     * @throws MessagingException
     */
    private BodyPart createQuoteDocumentAttachment(SavedPolicy quotation) throws BaseException, MessagingException {
    	// Only attached the document if it has already been generated - don't generate it
    	if (quotation.getQuotationDocument()!=null) {
	        FetchQuoteCommand cmd=getCore().newCommand(FetchQuoteCommand.class);
	        cmd.setQuotationNumberArg(args.getQuotationNumberArg());
	        cmd.invoke();
	
	        BodyPart bp = new MimeBodyPart();
	        bp.setHeader("Content-ID", "<quotation.pdf>");
	        bp.setHeader("Content-Disposition", "attachment");
	        bp.setDataHandler(new DataHandler(new ByteArrayDataSource(cmd.getDocumentRet(), "application/pdf")));
	        bp.setFileName(quotation.getQuotationNumber()+" Policy.pdf");

	        return bp;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Output an inline style sheet defining the styles user by UI components. Some of the attachments 
     * added to the email are coded to also be used within the portal - and as a result refer to styles
     * common to portlets. Adding these inline to each attachment gives the email some 'family' resemblance
     * to the online system.
     * @param writer Style will be written here.
     */
    private void insertStyles(PrintWriter writer) {
        writer.print("<style type='text/css'>");
        writer.print("<!--");
        writer.print(".portlet-section-header {");
        writer.print("    font-weight: bold;");
        writer.print("    font-family: Verdana, Arial, Helvetica, sans-serif;");
        writer.print("    color: #000;");
        writer.print("    font-size: 13px;");
        writer.print("    background-color: #CBD4E6;");
        writer.print(" }");
        writer.print(".portlet-section-subheader {");
        writer.print("    font-family: Verdana, Arial, Helvetica, sans-serif;");
        writer.print("    color: #000;");
        writer.print("    font-size: 11px;");
        writer.print("    background-color: #CBD4E6;");
        writer.print(" }");
        writer.print(".portlet-section-footer {");
        writer.print("    font-family: Verdana, Arial, Helvetica, sans-serif;");
        writer.print("    color: #000;");
        writer.print("    font-size: 8px;");
        writer.print(" }");
        writer.print(".portlet-font {");
        writer.print("    color: #000000;");
        writer.print("    font-family: Verdana, Arial, Helvetica, sans-serif;");
        writer.print("    font-size: 10px;");
        writer.print("    padding-right: 4px;");
        writer.print("    border-width: 0;");
        writer.print("}");
        writer.print(".portlet-font-dim {");
        writer.print("    color: #777777;");
        writer.print("    font-family: Verdana, Arial, Helvetica, sans-serif;");
        writer.print("     font-size: 10px;");
        writer.print(" }");
        writer.print(".portlet-section-selected {");
        writer.print("    font-size: 11px;");
        writer.print("    background-color: #CBD4E6;");
        writer.print("}");
        writer.print("-->");
        writer.print("</style>");
    }
}


