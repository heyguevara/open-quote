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

package com.ail.core;

/**
 * This type represents the version details of an artifact in a live system. The version might relate to a component or entry point,
 * or to some live data like a type definition. The version information is flexible in format (hence the use of Strings). Different
 * types of artifact are likely to return different version attributes in different formats.
 * 
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/02/04 16:47:12 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/Version.java,v $
 */
public class Version extends Type {
    public static final Version LATEST = null;

    private String version = null;

    private String source = null;

    private String state = null;

    private String date = null;

    private String author = null;

    private String copyright = null;

    private String comment = null;

    /**
     * Setter to change the version number. Typically version numbers are derived from CVS, so the string may contain other
     * characters (e.g. "$revision: 2.1$").
     * 
     * @param version The version number to use.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Getter to fetch the version number.
     * 
     * @return A version number.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Setter to change the source. The "source" is the name of the versioned artifact. For source code artifacts this would be the
     * path of the source file within CVS. For other artifacts it is simply a name that helps them to be identified.
     * 
     * @param source The source name.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Getter to fetch this artifacts source.
     * 
     * @return The source name
     */
    public String getSource() {
        return source;
    }

    /**
     * Setter for the artifacts change date. This is the data when the artifacts was last changed - i.e. when this version became
     * active. For a source file artifact this is the CVS value $date: $.
     * 
     * @param date Change date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter to fetch the version's date.
     * 
     * @return A string containing the version's date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter to change this version's author. For a source code artifact this is CVSs "$author:$" tag. For other artifacts it would
     * probably represent the name of user responsible.
     * 
     * @param author Authors name
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Getter to fetch the version's author.
     * 
     * @return Author's name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Setter to set the version's copyright message.
     * 
     * @param copyright The copyright string.
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * Getter to fetch the copyright message
     * 
     * @return copyright message.
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Setter to set this version's state. This described the state that the author considers this version of the artifact to be in.
     * For a source code artifact this would be tied to CVS's "$state: $" tag, which indicates the release state of the source
     * artifact. For a dynamic runtime artifact it might be "Live", or "Development" for example.
     * 
     * @param state The new state.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Getter to fetch this version's state.
     * 
     * @return State string
     */
    public String getState() {
        return state;
    }

    /**
     * Getter to getch the comment associated with this version. The comment returned was supplied by the individual/system when the
     * new version was created.
     * 
     * @return Comment string
     */
    public String getComment() {
        return comment;
    }

    /**
     * Setter to set the comment associated with this version.
     * 
     * @param comment The comment string.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
