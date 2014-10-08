import com.ail.financial.*;
import com.ail.util.*;
import java.util.ArrayList;
import java.math.BigDecimal;
import com.ail.insurance.acceptance.AssessPaymentOptionsArgImp;
import com.ail.core.CoreProxy;

/**
 * Basic assess payment options rule. This rule always returns a single payment option: A single payment in full by card.
 */
public class AssessPaymentOptionsRule {
    /**
     * Class entry point. 
     * @param args Service arguments
     */
    public static void invoke(AssessPaymentOptionsArgImp args) {
        args.setOptionsRet(new ArrayList());

        CurrencyAmount premium=args.getPolicyArg().getTotalPremium();
        
        /* One option: Whole premium in one payment by payment card. */
        ArrayList payments=new ArrayList();
        payments.add(new MoneyProvision(1, premium, FinancialFrequency.ONE_TIME, new PaymentCard(), null, null, null));
        args.getOptionsRet().add(new PaymentSchedule(payments, "A single payment by payment card of "+premium.toString()));
    }
}

