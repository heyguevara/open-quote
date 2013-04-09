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

import com.ail.core.XMLString;

/**
 * <b>Name</b><br/>
 * &nbsp;&nbsp;com.ail.core.configure.ConfigurationAggregate - Utility to aggregate annotation generated configurations
 * <br/><br/>
 * <b>Synopsis</b><br/>
 * &nbsp;&nbsp;com.ail.core.configure.ConfigurationAggregate -o &lt;output file&gt; -s &lt;search path 1&gt; -s &lt;search path 2&gt; ...
 * <br/><br/>
 * <b>Description</b><br/>
 * The annotations processor generates configuration files in response to various annotations that appear in the source
 * code. These configuration files are considered to be build time artifacts and are not references at run time. The 
 * ConfigurationAggregate scans the file system for each of the search paths defined on the command line, finding all
 * of the annotation generated configuration files and aggregating them into the single output file specified with the =o 
 * option.
 * <br/><br/>
 * <b>Example</b><br/>
 * The following would scan the files under the three directories, aggregate all the annotation generated configuration
 * files that it found and output the results to outputfile.xml.
 * <br/><br/>
 * <code>$java com.ail.core.configure.ConfigurationAgregator \<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-o ./outputfile.xml \<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-s ./directoryA \<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-s ./directoryB \<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-s ./directoryC \<br/>
 * <br/><br/>
 * @version 1.0
 */
public class ConfigurationAggregate {

    private void reset(String args[]) throws Exception {
        String annotationTypeConfig=null;
        String searchPath=null;
        
        for(int i=0 ; i<args.length ; i++) {
            if ("-o".equals(args[i])) {
                annotationTypeConfig=args[++i];
            }
            else if ("-s".equals(args[i])) {
                searchPath=args[++i];
            }
        }

        mergeAnnotationGeneratedConfigs(searchPath, annotationTypeConfig);
    }
    
    private Collection<File> findAnnotationConfigs(String searchPath) {
        Collection<File> configList=new ArrayList<File>();
        
        if (searchPath==null) {
            return configList;
        }
        
        Queue<File> folderList=new LinkedList<File>();
        
        for(String rootFolderName: searchPath.split(File.pathSeparator)) {
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
    public void mergeAnnotationGeneratedConfigs(String searchPath, String annotationTypeOutputFile) throws Exception {
        PrintWriter pw=new PrintWriter(annotationTypeOutputFile);

        Collection<File> configs=findAnnotationConfigs(searchPath);
        
        pw.printf("<!-- This is a generated file. The types defined here are automatically create -->\n");
        pw.printf("<!-- by the build system in response to annotations in source code. Edits to   -->\n");
        pw.printf("<!-- this file will be lost.                                                   -->\n");
        pw.printf("<configuration xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://www.appliedindustriallogic.com/schemas/Configuration.xsd'>\n");

        pw.printf("  <builders>\n");
        for(File file: configs) {
            XMLString xmlConfig=new XMLString(file);
            pw.print(xmlConfig.eval("configuration/builders/*"));
        }
        pw.printf("  </builders>\n");
        
        pw.printf("  <group name='JXPathExtensions'>\n");
        for(File file: configs) {
            XMLString xmlConfig=new XMLString(file);
            pw.print(xmlConfig.eval("configuration/group[@name='JXPathExtensions']/*"));
        }
        pw.printf("  </group>\n");
        
        pw.printf("  <group name='NamespacesToResetOnResetAll'>\n");
        for(File file: configs) {
            XMLString xmlConfig=new XMLString(file);
            pw.print(xmlConfig.eval("configuration/group[@name='NamespacesToResetOnResetAll']/*"));
        }
        pw.printf("  </group>\n");
        
        pw.printf("  <types>\n");
        for(File file: configs) {
            XMLString xmlConfig=new XMLString(file);
            pw.print(xmlConfig.eval("configuration/types/*"));
        }
        pw.printf("  </types>\n");
        
        pw.printf("</configuration>\n");

        pw.close();
    }

    public static void main(String args[]) throws Exception {
        new ConfigurationAggregate().reset(args);
    }
}
