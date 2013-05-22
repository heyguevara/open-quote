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
package com.ail.pageflow.render;

import static com.ail.core.Functions.productNameToConfigurationNamespace;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.insurance.policy.Broker;
import com.ail.insurance.policy.Proposer;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.insurance.quotation.FetchQuoteService.FetchQuoteCommand;
import com.ail.insurance.quotation.NotifyProposerByEmailService.NotifyProposerByEmailArgument;
import com.ail.pageflow.render.RenderService.RenderCommand;
import com.ail.pageflow.util.PageflowContext;
/**
 * Send a notification of an event relating to a quote to the broker associated with the product 
 */
@ServiceImplementation
public class NotifyProposerByEmailService extends Service<NotifyProposerByEmailArgument> {
    private static final long serialVersionUID = -4915889686192216902L;
    private String configurationNamespace = null;

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
        Authenticator authenticator=null;

    	PageflowContext.setPolicy(savedPolicy.getPolicy());

        Proposer proposer = (Proposer) savedPolicy.getPolicy().getProposer();
        Broker broker = savedPolicy.getPolicy().getBroker();
        String fromAddress = broker.getEmailAddress();
        String toAddress = proposer.getEmailAddress();

        final Properties props=getCore().getGroup("SMTPServerProperties").getParameterAsProperties();
        
        if ("true".equals(props.getProperty("mail.smtp.auth"))) {
            authenticator=new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    String username=props.getProperty("mail.smtp.user");
                    String password=props.getProperty("mail.smtp.password");
                    return new PasswordAuthentication(username, password);
                }
            };
        }

        session = Session.getInstance(props, authenticator);
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
}


