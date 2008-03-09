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
package com.ail.core.configure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * A Group simply provides a container to give the Configuration file
 * more structure. It allows parameters to be grouped into sections. For
 * example:<code>
 * &lt;Configuration&gt;
 *  ...
 *  &lt;Group name="GroupName"&gt;
 *    &lt;Parameter name="myParam" value="a value"/&gt;
 *    &lt;Parameter name="myOtherParam" value="another value"/&gt;
 *  &lt;/Group&gt;
 * ...
 * &lt;/Configuration&gt;</code>
 * Note: A group may contain subgroups.
 * @version $Revision: 1.4 $
 * @state $State: Exp $
 * @date $Date: 2006/07/15 15:01:44 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/Group.java,v $
 * @stereotype type
 **/
public class Group extends Component {
    static final long serialVersionUID = -8823749275986134685L;
    private List<Parameter> parameter=null;
    private List<Group> group=null;
    private transient HashMap<String,Parameter> parameterCache=null;

	/**
     * Default constructor.
     */
    public Group() {
    }

	private List<Parameter> getParameterList() {
        if (parameter==null) {
            parameter=new ArrayList<Parameter>();
        }

        return parameter;
    }

	private void setParameterList(Collection<Parameter> parameter) {
		this.parameter=new ArrayList<Parameter>(parameter);
    }

	private List<Group> getGroupList() {
        if (group==null) {
            group=new ArrayList<Group>();
        }

        return group;
    }

    private void setGroupList(Collection<Group> group) {
        this.group=new ArrayList<Group>(group);
    }

    /**
     * Add a Parameter to this group.
     * @param vParameter Parameter to add.
     * @throws IndexOutOfBoundsException Group is "full"
     **/
    public void addParameter(Parameter vParameter) throws IndexOutOfBoundsException {
		getParameterList().add(vParameter);
    }

    /**
     * Fetch a specific Parameter by it's index in the group.
     * @param index Index of Parameter to fetch.
     * @throws IndexOutOfBoundsException index is out of range
     * @return The Parameter at 'index'.
     **/
    public Parameter getParameter(int index) throws IndexOutOfBoundsException {
		return (Parameter)getParameterList().get(index);
    }

    /**
     * Return a list of the parameters in the group as a List.
     * @return List of parameters.
     **/
    public List<Parameter> getParameter() {
        return getParameterList();
    }

    /**
     * Fetch a count of the number of properties in this group. This does not
     * include the properties in sub-groups.
     * @return Count of properties in the group.
     **/
    public int getParameterCount() {
        return getParameterList().size();
    }

    /**
     * Remove (delete) all the properties in this group. This does not
     * effect sub-groups.
     **/
    public void removeAllParameter() {
        getParameterList().clear();
    }

    /**
     * Remove (delete) a specific Parameter from this group.
     * @param index The index of the Parameter to remove.
     * @return removed Parameter, or null if index is out of range.
     **/
    public Parameter removeParameter(int index) {
        return (Parameter)getParameterList().remove(index);
    }

    /**
     * Set the Parameter at a specified index in this group, this replaces
     * the Parameter currently at that index.
     * @param index Index into the group
     * @param vParameter Parameter to be added.
     * @throws IndexOutOfBoundsException If index is out of range.
     **/
    public void setParameterAt(int index, Parameter vParameter) throws IndexOutOfBoundsException {
        getParameterList().set(index, vParameter);
	}

    /**
     * Replace all the properties in the group with those in the list provided.
     * @param parameter List of parameters
     **/
    public void setParameter(List<Parameter> parameter) {
		setParameterList(parameter);
    }

    /**
     * Add a Parameter group to this group.
     * @param vGroup Group to add.
     * @throws IndexOutOfBoundsException Group is "full"
     **/
    public void addGroup(Group vGroup) throws IndexOutOfBoundsException {
		getGroupList().add(vGroup);
    }

    /**
     * Fetch a specific group by it's index in the group.
     * @param index Index of Parameter group to fetch.
     * @throws IndexOutOfBoundsException index is out of range
     * @return The group at 'index'.
     **/
    public Group getGroup(int index) throws IndexOutOfBoundsException {
		return (Group)(getGroupList().get(index));
    }

    /**
     * Return a list of the groups in the group as an array.
     * @return Array of groups.
     **/
    public List<Group> getGroup() {
		return getGroupList();
    }

    /**
     * Fetch a count of the number of groups in this group. This does not
     * include the Parameter groups in sub-groups.
     * @return Count of Parameter groups in the group.
     **/
    public int getGroupCount() {
        return getGroupList().size();
	}

    /**
     * Remove (delete) all the groups in this group. This does not
     * effect sub-groups.
     **/
    public void removeAllGroup() {
        getGroupList().clear();
	}

    /**
     * Remove (delete) a specific group from this group.
     * @param index The index of the group to remove.
     * @return removed Group, or null if index is out of range.
     **/
    public Group removeGroup(int index) {
		return (Group)getGroupList().remove(index);
    }

    /**
     * Set the group at a specified index in this group, this replaces
     * the group currently at that index.
     * @param index Index into the group
     * @param vGroup Group to be added.
     * @throws IndexOutOfBoundsException If index is out of range.
     **/
    public void setGroup(int index, Group vGroup) throws IndexOutOfBoundsException {
		getGroupList().set(index, vGroup);
    }

    /**
     * Replace all the groups in the group with those in the array provided.
     * @param group  Array of groups
     **/
    public void setGroup(List<Group> group) {
		setGroupList(group);
    }

    /**
     * Find a group using a dot notation name.
     * @param name Dot seperated group names
     * @param group Group to start serach from
     * @return The located group, or null.
     */
    private Group getGroup(String name, Group group) {
        if (name.equals("")) {
            return group;
        }
        else {
            int dotidx=name.indexOf('.');
            String groupName=name;

            if (dotidx!=-1) {
                groupName=name.substring(0, dotidx);
            }

            for(Group g: group.getGroupList()) {
                if (g.getName().equals(groupName)) {
                    if (dotidx!=-1) {
                        return getGroup(name.substring(dotidx+1), g);
                    }
                    else {
                        return g;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Find a group using a dot seperated group name. This method
     * will search down throught the group layers of this configuration
     * to locate a group with the nested group name specified.
     * @param groupName Dot seperated group name (e.g. one.two.three).
     * @return The located group, or null if it cannot be found.
     */
    public Group findGroup(String groupName) {
        return getGroup(groupName, this);
    }

    /**
     * Cache used to speed the finding of parameters that have been returned before.
     * @return The cache
     */
    private HashMap<String,Parameter> getParameterCache() {
        if (parameterCache==null) {
            parameterCache=new HashMap<String,Parameter>();
        }
        return parameterCache;
    }

    /**
     * Find a nested parameter in this configuration. This method will
     * search throught the group structure to find the named parameter.
     * @param paramNameArg Nested name (e.g. outter.inner.parameter).
     * @return The located parameter, or null if it cannot be found.
     */
    public Parameter findParameter(String paramNameArg) {
        // If this parameter has been read (and so cached) before, then return
        // if straight from the cache - much quicker than looking for it!
        synchronized(this) {
            if (getParameterCache().containsKey(paramNameArg)) {
                return (Parameter)getParameterCache().get(paramNameArg);
            }
        }

        Group group;
        String paramName=paramNameArg;
        int dotidx=paramName.lastIndexOf('.');

        if (dotidx==-1) {
            group=this;
        }
        else {
            String groupPath=paramName.substring(0, dotidx);

            if ((group=getGroup(groupPath, this))!=null) {
                paramName=paramName.substring(dotidx+1);
            }
        }

        if (group!=null) {
            for(Parameter par: group.getParameterList()) {
                if (par.getName().equals(paramName)) {

                    // cache the result for next time
                    synchronized(this) {
                        getParameterCache().put(paramNameArg, par);
                    }

                    return par;
                }
            }
        }

        // cache the result even if it is a null - there is no point in
        // looking for a parameter that doesn't exist in future.
        synchronized(this) {
            getParameterCache().put(paramNameArg, null);
        }

        return null;
    }

    /**
     * Find the value of a parameter.
     * @param paramName The parameter to return the value of.
     * @return The value of the parameter, or null if it is undefined.
     */
    public String findParameterValue(String paramName) {
        Parameter p=findParameter(paramName);
        return p!=null ? p.getValue() : null;
    }

    /**
     * Return the value of a parameter, or a default value if the parameter is undefined.
     * @param paramName The parameter to return the value of.
     * @param defaultValue The default value.
     * @return The value of the parameter or the default value.
     */
    public String findParameterValue(String paramName, String defaultValue) {
        String ret=findParameterValue(paramName);
        return (ret!=null) ? ret : defaultValue;
    }
}
