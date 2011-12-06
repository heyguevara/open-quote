/* Copyright Applied Industrial Logic Limited 2005. All rights Reserved */
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

package com.ail.core.configure.server;

import com.ail.annotation.CommandDefinition;
import com.ail.core.command.Command;

/**
 * Arg interface for the GetNamesapcesHisotyr entry point. The entry point takes one
 * argument: a namespace's name, and returns one result: a collection of 
 * {@link com.ail.core.configure.ConfigurationSummary ConfigurationSummary}
 * objects representing the namespace's history.
 */
@CommandDefinition(defaultServiceClass=GetNamespacesHistoryService.class)
public interface GetNamespacesHistoryCommand extends Command, GetNamespacesHistoryArgument {
 }
