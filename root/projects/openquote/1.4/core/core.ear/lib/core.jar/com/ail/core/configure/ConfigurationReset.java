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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.ail.core.CoreProxy;
import com.ail.core.XMLString;

/**
 * <b>Name</b><br/>
 * &nbsp;&nbsp;com.ail.core.configure.ConfigurationReset - Utility to reset a list of named configurations.
 * <br/><br/>
 * <b>Synopsis</b><br/>
 * &nbsp;&nbsp;com.ail.core.configure.ConfigurationReset [namespace1 namespace2 ... namespaceN]
 * <br/><br/>
 * <b>Description</b><br/>
 * By default (without any namespace arguments) this utility will reset the <code>com.ail.core.Core</code> and
 * <code>com.ail.core.CoreProxy</code> namespaces. Any further namespaces specified on the command line will
 * also be reset.<br/><br/>
 *
 * The details of the database to which the reset configurations should be written are specified in the form
 * defined by {@link com.ail.core.configure.AbstractConfigurationLoader AbstractConfigurationLoader}. Generally,
 * this takes the form of a collection of -D's on the command line.<br/><br/>
 * 
 * <b>Example</b><br/>
 * To reset only the default namespaces in a MySQL database using the JDBC configuration loader, you would 
 * execute the following:
 * <br/><br/>
 * <code>$java -Dcom.ail.core.configure.loader=com.ail.core.configure.JDBCConfigurationLoader \<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-Dcom.ail.core.configure.loaderParam.driver=org.gjt.mm.mysql.Driver \<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-Dcom.ail.core.configure.loaderParam.url=jdbc:mysql://localhost:3306/core_2_0 \<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-Dcom.ail.core.configure.loaderParam.user=root \<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-Dcom.ail.core.configure.loaderParam.password=bombay2000 \<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-Dcom.ail.core.configure.loaderParam.table=config \<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-Dcom.ail.core.configure.loaderParam.databaseName=core_2_0 \<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;com.ail.core.configure.ConfigurationReset
 * <br/><br/>
 * @version 1.0
 */
public class ConfigurationReset {

    private void reset(String args[]) throws Exception {
        String annotationTypeConfig=null;
        String searchPath=null;
        Collection<String> configsToReset=new ArrayList<String>();
        
        for(int i=0 ; i<args.length ; i++) {
            if ("-o".equals(args[i])) {
                annotationTypeConfig=args[++i];
            }
            else if ("-s".equals(args[i])) {
                searchPath=args[++i];
            }
            else {
                configsToReset.add(args[i]);
            }
        }

        mergeAnnotationGeneratedConfigs(searchPath, annotationTypeConfig);
        
        CoreProxy cp = new CoreProxy();
        cp.resetCoreConfiguration();
    
        cp = new CoreProxy();
        cp.resetConfiguration();
    
        for(String config: configsToReset) {
            cp.resetConfiguration(config);
        }
    }
    
    private Collection<File> findAnnotationConfigs(String searchPath) {
        Collection<File> configList=new ArrayList<File>();
        
        if (searchPath==null) {
            return configList;
        }
        
        Queue<File> folderList=new LinkedList<File>();
        
        for(String rootFolderName: searchPath.split(":")) {
            File rootFolder=new File(rootFolderName);
            if (!rootFolder.isDirectory()) {
                System.err.println("config.search.path element '"+rootFolderName+"' is not a folder.");
                System.exit(-1);
            }
            
            folderList.add(rootFolder);
        }

        while(!folderList.isEmpty()) {
            File element=folderList.remove();
            
            if (element.isDirectory()) {
                folderList.addAll(Arrays.asList(element.listFiles()));
            }
            else {
                if (element.getName().matches("AnnotatedTypes.xml")) {
                    configList.add(element);
                }
            }
        }

        return configList;
    }
    
    /**
     * Tree walk the specified folder list looking the configuration files
     * fragments which the annotations processor generates. The processor
     * itself is defined in the development project, {@link com.ail.annotation.Processor}.
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws TransformerException 
     * @throws SAXException 
     */
    private void mergeAnnotationGeneratedConfigs(String searchPath, String annotationTypeOutputFile) throws Exception {
        PrintWriter pw=new PrintWriter(annotationTypeOutputFile);

        pw.printf("<!-- This is a generated file. The types defined here are automatically create -->\n");
        pw.printf("<!-- by the build system in response to annotations in source code. Edits to   -->\n");
        pw.printf("<!-- this file will be lost.                                                   -->\n");
        pw.printf("<configuration xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://www.appliedindustriallogic.com/schemas/Configuration.xsd'>\n");
        pw.printf("  <types>");

        for(File file: findAnnotationConfigs(searchPath)) {
            XMLString xmlConfig=new XMLString(file);
            pw.print(xmlConfig.eval("configuration/types/*"));
        }

        pw.printf("  </types>");
        pw.printf("</configuration>");

        pw.close();
    }

    public static void main(String args[]) throws Exception {
        new ConfigurationReset().reset(args);
    }
}
