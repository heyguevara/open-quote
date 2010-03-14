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

import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/*
 * @deprecated Use Alfresco's bootstap mechanism, or smb instead.
 */
public class CmsUploaderAntTask extends Task {
    private String host;
    private String username;
    private String password;
    private String toSpace;
    private Vector<FileSet> filesets=new Vector<FileSet>();

    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }

    public void addFileset(FileSet fileset) {
        filesets.add(fileset);
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToSpace() {
        return toSpace;
    }

    public void setToSpace(String toSpace) {
        this.toSpace = toSpace;
    }

    protected void validate() {
        if (host==null || host.length()==0) {
            throw new BuildException("host not set");
        }
        if (toSpace==null || toSpace.length()==0) {
            throw new BuildException("toSpace not set");
        }
        if (filesets.size()==0) {
            throw new BuildException("at least one fileset must be defined");
        }
    }
    
    public void execute() {
        CmsUploader cmsUploader=null;
        
        validate();
        
        try {
            cmsUploader=new CmsUploader(username, password);
            
            for(FileSet fileset: filesets) {
                DirectoryScanner ds=fileset.getDirectoryScanner(getProject());
                for(String fsPathname: ds.getIncludedFiles()) {
                    String fixedPath=fsPathname.replace('\\', '/');
                    String cmPathname=(fixedPath.startsWith("/")) ? fixedPath : "/"+fixedPath;
                    cmPathname=toSpace+cmPathname.replaceAll("/", "/cm:");
                    cmPathname=cmPathname.substring(0, cmPathname.lastIndexOf('/'));               
                    cmsUploader.cmsUploadContent(ds.getBasedir().getCanonicalPath()+"/"+fixedPath, cmPathname);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new BuildException("Failed to upload content: "+e);
        }
    }
}
