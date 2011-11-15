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
import com.ail.core.command.CommandArgImp;

public class TestArgImp extends CommandArgImp implements TestArg {
    static final long serialVersionUID = 1199346453402049909L;
    private int x;
    private int y;
    private int r;
    private String string;
    private Version versionArgRet;
    private boolean preconditionflag;
    private boolean postconditionflag;
    private Attribute detailAttribute;

    /** Default constructor */
    public TestArgImp() {
    }

    /**
     * Argument priming constructor.
     * @param x Value for X argument.
     * @param y Value for Y argument.
     */
    public TestArgImp(int x, int y) {
        this.x=x;
        this.y=y;
    }

    /**
     * {@inheritDoc}
     * @see #setX
     * @return value of x
     */
    public int getX() {
        return this.x;
    }

    /**
     * {@inheritDoc}
     * @see #getX
     * @param x New value for x argument.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * {@inheritDoc}
     * @see #setY
     * @return value of y
     */
    public int getY() {
        return this.y;
    }

    /**
     * {@inheritDoc}
     * @see #getY
     * @param y New value for y argument.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * {@inheritDoc}
     * @see #setR
     * @return value of r
     */
    public int getR() {
        return this.r;
    }

    /**
     * {@inheritDoc}
     * @see #getR
     * @param r New value for r argument.
     */
    public void setR(int r) {
        this.r = r;
    }

    public void setStringRet(String string) {
        this.string=string;
    }

    public String getStringRet() {
        return this.string;
    }

    /**
     * {@inheritDoc}
     * @see #setPreConditionFlag
     * @return value of preconditionflag
     */
    public boolean getPreConditionFlag() {
        return this.preconditionflag;
    }

    /**
     * {@inheritDoc}
     * @see #getPreConditionFlag
     * @param preconditionflag New value for preconditionflag argument.
     */
    public void setPreConditionFlag(boolean preconditionflag) {
        this.preconditionflag = preconditionflag;
    }

    /**
     * {@inheritDoc}
     * @see #setPostConditionFlag
     * @return value of postconditionflag
     */
    public boolean getPostConditionFlag() {
        return this.postconditionflag;
    }

    /**
     * {@inheritDoc}
     * @see #getPostConditionFlag
     * @param postconditionflag New value for postconditionflag argument.
     */
    public void setPostConditionFlag(boolean postconditionflag) {
        this.postconditionflag = postconditionflag;
    }

    public Attribute getDetailAttribute() {
        return detailAttribute;
    }

    public void setDetailAttribute(Attribute detailAttribute) {
        this.detailAttribute = detailAttribute;
    }

    public Version getVersionArgRet() {
        return versionArgRet;
    }

    public void setVersionArgRet(Version versionArgRet) {
        this.versionArgRet=versionArgRet;
    }
}


