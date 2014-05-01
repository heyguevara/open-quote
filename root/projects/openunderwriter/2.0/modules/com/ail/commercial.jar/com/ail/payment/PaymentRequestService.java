package com.ail.payment;

import java.net.URL;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceInterface;
import com.ail.core.DumpService;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.financial.CurrencyAmount;

@ServiceInterface
public class PaymentRequestService {
    
    @ServiceArgument
    public interface PaymentRequestArgument extends Argument {

        /**
         * The ID of the product this this payment is being made in relation to.
         */
        String getProductTypeIdArg();
        
        /**
         * @see #getProductTypeIdArg()
         * @param productTypeIdArg
         */
        void setProductTypeIdArg(String productTypeIdArg);
        
        /**
         * The amount to be paid
         */
        CurrencyAmount getAmountArg();

        /**
         * @see #getAmountArg()
         */
        void setAmountArg(CurrencyAmount amountArg);
        
        /**
         * The URL returned by the service which must be used to authorise the payment. The
         * client of this service will typically need to forward the user to this URL in order
         * for them to authorise the payment.
         */
        URL getAuthorisationURLRet();
        
        /**
         * @see #getAuthorisationURLRet()
         * @param authorisationURLRet
         */
        void setAuthorisationURLRet(URL authorisationURLRet);
        
        /**
         * The URL to which the user should be forwarded if the payment process is cancelled.
         */
        URL getCancelledURLArg();
        
        /**
         * @see #getCancelledURLArg()
         * @param cancelledURLArg
         */
        void setCancelledURLArg(URL cancelledURLArg);
        
        /**
         * URL to forward the user to when they payment is authorised and approved.
         */
        URL getApprovedURLArg();
        
        /**
         * @see #getApprovedURLArg()
         * @param approvedURLArg
         */
        void setApprovedURLArg(URL approvedURLArg);
        
        /**
         * Description of the transaction in terms of what is being purchased.
         */
        String getDescriptionArg();
        
        /**
         * @see #getDescriptionArg()
         */
        void setDescriptionArg(String descriptionArg);
    }
    
    @ServiceCommand(defaultServiceClass=DumpService.class)
    public interface PaymentRequestCommand extends PaymentRequestArgument, Command {
    }
}
