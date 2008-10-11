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

package com.ail.insurance.acceptance;

import java.util.ArrayList;

import com.ail.financial.PaymentSchedule;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2006/04/24 21:07:59 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/acceptance/AssessPaymentOptionsArg.java,v $
 * @stereotype arg
 */
public interface AssessPaymentOptionsArg extends com.ail.core.command.CommandArg {
    /**
     * Getter for the policyArg property. Policy to collect premium for
     * @return Value of policyArg, or null if it is unset
     */
    Policy getPolicyArg();

    /**
     * Setter for the policyArg property. 
     * @see #getPolicyArg
     * @param policyArg new value for property.
     */
    void setPolicyArg(Policy policyArg);

    /**
     * Getter returning a list of schedules, each of which is an option
     * @return List of schedule
     */
    ArrayList<PaymentSchedule> getOptionsRet();
    
    /**
     * Setter for the schedule options
     * @see #getOptionRet()
     * @param option
     */
    void setOptionsRet(ArrayList<PaymentSchedule> option);
}
