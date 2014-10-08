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

package com.ail.pageflow.service;

import java.util.ArrayList;
import java.util.Collection;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import static com.ail.pageflow.util.I18N.i18n;

/**
 * Fetch a list of the PageFlows supported by a named product
 */
@ServiceImplementation
public class ListToOptionService extends Service<ListToOptionService.ListToOptionArgument> {

    @ServiceArgument
    public interface ListToOptionArgument extends Argument {
        void setOptionsArg(Collection<?> listArg);

        Collection<?> getOptionsArg();

        void setSelectedArg(String selectedArg);

        String getSelectedArg();

        void setOptionMarkupRet(String optionMarkupRet);

        String getOptionMarkupRet();

        void setExcludeUnknownArg(boolean excludeUnknownArg);
        
        boolean getExcludeUnknownArg();
        
        void setUnknownOptionArg(String unknownOptionArg);
        
        String getUnknownOptionArg();
        
        void setDisabledOptionsArg(Collection<?> disabledOptionsArg);
        
        Collection<?> getDisabledOptionsArg();
    }

    @ServiceCommand(defaultServiceClass = ListToOptionService.class)
    public interface ListToOptionCommand extends Command, ListToOptionArgument {
    }

    @Override
    public void invoke() throws BaseException {

        if (args.getOptionsArg() == null) {
            throw new PreconditionException("args.getListArg()==null");
        }

        Collection<?> disabledOptions = args.getDisabledOptionsArg()!=null 
                ? args.getDisabledOptionsArg() 
                : new ArrayList<Object>();

        String option;
        String selected;
        String disabled;
        
        StringBuffer markup = new StringBuffer();

        if (!args.getExcludeUnknownArg() || args.getUnknownOptionArg() != null) {
            option = args.getUnknownOptionArg() != null ? args.getUnknownOptionArg() : "i18n_?";
            if (args.getSelectedArg()==null || option.equals(args.getSelectedArg())) {
                markup.append("<option disabled='yes' selected='yes' value='?'>").append(i18n(option)).append("</option>");
            }
            else {
                markup.append("<option disabled='yes' value='?'>").append(i18n(option)).append("</option>");
            }
        }
        
        for (Object p : args.getOptionsArg()) {
            option = p.toString();

            selected = option.equals(args.getSelectedArg()) ? " selected='yes' " : "";
            disabled = disabledOptions.contains(option) ? " disabled='yes' " : "";

            markup.append("<option value='").append(option).append("'").append(selected).append(disabled).append('>').append(i18n(option)).append("</option>");
        }

        args.setOptionMarkupRet(markup.toString());
    }
}