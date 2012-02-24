/* Copyright Applied Industrial Logic Limited 2010. All rights Reserved */
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


import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceInterface;
import com.ail.core.command.Argument;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.quotation.NotifyPartyService.NotifyPartyCommand;

@ServiceInterface
public interface NotifyProposerService {

    @ServiceArgument
    public interface NotifyProposerArgument extends Argument {
        String getQuotationNumberArg();
        
        void setQuotationNumberArg(String quotationNumberArg);
        
        Policy getPolicyArg();
        
        void setPolicyArg(Policy policyArg);
    }

    @ServiceCommand
    public interface NotifyProposerCommand extends NotifyPartyCommand, NotifyProposerArgument {
    }
}
