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

package com.ail.core.persistence;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceInterface;
import com.ail.core.Type;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.core.persistence.hibernate.HibernateDeleteService;
/**
 * Arguments required by delete service
 */
@ServiceInterface
public interface DeleteService {

    /**
     * Arguments required by delete service
     */
    @ServiceArgument
    public interface DeleteArgument extends Argument {

        /**
         * Setter for the objectArg property. * @see #getObjectArg
         * @param objectArg new value for property.
         */
        void setObjectArg(Type objectArg);

        /**
         * Getter for the objectArg property. Object to persist
         * @return Value of objectArg, or null if it is unset
         */
        Type getObjectArg();
    }

    @ServiceCommand(defaultServiceClass=HibernateDeleteService.class)
    public interface DeleteCommand extends Command, DeleteArgument {}
}


