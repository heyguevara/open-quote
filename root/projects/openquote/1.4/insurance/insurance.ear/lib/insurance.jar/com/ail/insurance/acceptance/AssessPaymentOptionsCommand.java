/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
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

package com.ail.insurance.acceptance;

import java.util.ArrayList;

import com.ail.financial.PaymentSchedule;
import com.ail.insurance.policy.Policy;
/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2006/04/24 21:07:59 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/acceptance/AssessPaymentOptionsCommand.java,v $
 * @stereotype command
 */
public class AssessPaymentOptionsCommand extends com.ail.core.command.Command implements AssessPaymentOptionsArg {
    private static final long serialVersionUID = 2354122733326164200L;
    private AssessPaymentOptionsArg args = null;

    public AssessPaymentOptionsCommand() {
        super();
        args = new AssessPaymentOptionsArgImp();
    }

    public void setArgs(com.ail.core.command.CommandArg arg) {
        this.args = (AssessPaymentOptionsArg)arg;
    }

    public com.ail.core.command.CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Policy getPolicyArg() {
        return args.getPolicyArg();
    }

    /**
     * {@inheritDoc}
     * @param policyArg @{inheritDoc}
     */
    public void setPolicyArg(Policy policyArg) {
        args.setPolicyArg(policyArg);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    public ArrayList<PaymentSchedule> getOptionsRet() {
        return args.getOptionsRet();
    }

    /**
     * {@inheritDoc
     * @param paymentOptionRet {@inheritDoc}
     */
    public void setOptionsRet(ArrayList<PaymentSchedule> optionsRet) {
        args.setOptionsRet(optionsRet);
    }
}
