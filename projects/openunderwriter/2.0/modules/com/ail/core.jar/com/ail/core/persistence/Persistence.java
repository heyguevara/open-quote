/* Copyright Applied Industrial Logic Limited 2002. All rights reserved. */
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

import java.util.List;

import com.ail.core.Type;
import com.ail.core.VersionException;

/**
 * Core persistence implementation interface.
 * This interface describes the methods that the Core must expose in order
 * to support persistence. The methods exposed also describe the contract
 * between this package and the Core - this package provides a Service
 * implementation for each.
 */
public interface Persistence {
	/**
     * Create a persistent copy of an object.
     * @param The object to be persisted.
     * @return The object as it was persisted
     */
	<T extends Type> T create(T object);

	/**
     * Query persistent storage for the collection of objects returned by a 
     * query. The query itself is referenced by name only. This name is
     * interpreted by the underlying persistence engine and resolved to an
     * actual query. 
     * @param queryName The name of the query to be executed.
     * @param queryArgs Arguments to be used by the query.
	 * @return The results of the query.
     * @throws VersionException The version argument is either badly defined, or relates to a version that does not exist.
     */
	List<?> query(String queryName, Object... queryArgs);

    /**
     * Query persistent storage for the single object returned by a 
     * query. The query itself is referenced by name only. This name is
     * interpreted by the underlying persistence engine and resolved to an
     * actual query. 
     * @param queryName The name of the query to be executed.
     * @param queryArgs Arguments to be used by the query.
     * @return The results of the query.
     * @throws VersionException The version argument is either badly defined, or relates to a version that does not exist.
     */
    Type queryUnique(String queryName, Object... queryArgs);

    /**
     * Load a specific instance of a type by ID. This has the same effect as a named query
     * with the following query: from &lt;type&gt; where systemId=&lt;systemId&gt;.
     * @param type The type to be loaded
     * @param systemId the systemId of the instance to load.
     * @return
     */
    <T extends Type> T load(Class<T> type, long systemId);
    
    /**
     * Update the persistent copy of an object from its in memory copy.
     * @param object The object to be written to persistent storage.
     * @return The object as persisted.
     */
    <T extends Type> T update(T object);

	/**
     * Delete one or more objects from persistent storage.
     * @param object Object to be deleted
     * @throws VersionException If the version object is badly defined, or relates to a version that does not exist.
     * @return The number of objects deleted.
     */
    void delete(Type object);

    /**
     * Initiate a persistence session. All persistence related methods need to operate within
     * the context of a session. This call generally corresponds to the start of a transaction.
     * The session is associated with the calling thread, so any persistent methods made within
     * the same thread before {@see #closePersistenceSession()} are performed within 
     * one transaction.
     */
    void openPersistenceSession();
    
    /**
     * Close the open session associated with the current thread and commit. 
     * @see #openPersistenceSession()
     */
    void closePersistenceSession();
}
