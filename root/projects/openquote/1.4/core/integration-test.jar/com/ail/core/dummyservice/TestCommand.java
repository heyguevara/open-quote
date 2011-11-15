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

package com.ail.core.dummyservice;

import com.ail.core.Attribute;
import com.ail.core.Version;
import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;

public class TestCommand extends Command implements TestArg {
    private TestArg args = null;

    public TestCommand() {
        super();
        args = new TestArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (TestArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @see #setX
     * @return value of x
     */
    public int getX() {
        return args.getX();
    }

    /**
     * {@inheritDoc}
     * @see #getX
     * @param x New value for x argument.
     */
    public void setX(int x) {
        args.setX(x);
    }

    /**
     * {@inheritDoc}
     * @see #setY
     * @return value of y
     */
    public int getY() {
        return args.getY();
    }

    /**
     * {@inheritDoc}
     * @see #getY
     * @param y New value for y argument.
     */
    public void setY(int y) {
        args.setY(y);
    }

    /**
     * {@inheritDoc}
     * @see #setR
     * @return value of r
     */
    public int getR() {
        return args.getR();
    }

    /**
     * {@inheritDoc}
     * @see #getR
     * @param r New value for r argument.
     */
    public void setR(int r) {
        args.setR(r);
    }

    public void setStringRet(String string) {
        args.setStringRet(string);
    }

    public String getStringRet() {
        return args.getStringRet();
    }

    /**
     * {@inheritDoc}
     * @see #setPreConditionFlag
     * @return value of preconditionflag
     */
    public boolean getPreConditionFlag() {
        return args.getPreConditionFlag();
    }

    /**
     * {@inheritDoc}
     * @see #getPreConditionFlag
     * @param preconditionflag New value for preconditionflag argument.
     */
    public void setPreConditionFlag(boolean preconditionflag) {
        args.setPreConditionFlag(preconditionflag);
    }

    /**
     * {@inheritDoc}
     * @see #setPostConditionFlag
     * @return value of postconditionflag
     */
    public boolean getPostConditionFlag() {
        return args.getPostConditionFlag();
    }

    /**
     * {@inheritDoc}
     * @see #getPostConditionFlag
     * @param postconditionflag New value for postconditionflag argument.
     */
    public void setPostConditionFlag(boolean postconditionflag) {
        args.setPostConditionFlag(postconditionflag);
    }

    public Attribute getDetailAttribute() {
        return args.getDetailAttribute();
    }

    public void setDetailAttribute(Attribute attribute) {
        args.setDetailAttribute(attribute);
    }

    public Version getVersionArgRet() {
        return args.getVersionArgRet();
    }

    public void setVersionArgRet(Version versionArgRet) {
        args.setVersionArgRet(versionArgRet);
    }
}
