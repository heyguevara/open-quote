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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.xml.transform.TransformerException;

import org.alfresco.webservice.authoring.AuthoringServiceSoapBindingStub;
import org.alfresco.webservice.authoring.CheckoutResult;
import org.alfresco.webservice.content.Content;
import org.alfresco.webservice.content.ContentServiceSoapBindingStub;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLAddAspect;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.ContentFormat;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.Utils;
import org.alfresco.webservice.util.WebServiceFactory;
import org.xml.sax.SAXException;

import com.ail.core.XMLString;

/**
 * NAME<br>
 * &nbsp;&nbsp;CmsUploader - upload content into a jackrabbit repository<br/><br/>
 * 
 * SYNOPSIS<br/>
 * &nbsp;&nbsp;CmsUpload &lt;repository config file&gt; &lt;repository directory&gt; &lt;source directory&gt;<br/><br/>
 *  
 * DESCRIPTION<br/>
 * The directory structure under &lt;source directory&gt; is traversed and it's contents are compared with
 * the CMS repository. Files and folders in the repository are create and updated based on what is found. 
 * Files and folders that don't exist are created. CMS file nodes that are older then the corresponding
 * source file are updated.<br/><br/>
 * All file nodes are created in CMS for the system's default locale.</br><br/>
 * When file nodes are created, the 'mixin:versionable' property is set. Any subsequent update creates a 
 * new version. All nodes that are updated or created are labeled as "LIVE".
 */
public class CmsUploader {
    protected static final Store STORE = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
    private Hashtable<String,XMLString> cache=new Hashtable<String,XMLString>();
    
    /**
     * Constructor. Logs into CMS using the credentials supplied.
     * @param username CMS username - all operation will be carried out using this user. 
     * @param password User's password in cleartext.
     */
    public CmsUploader(String username, String password) throws Exception {
        AuthenticationUtils.startSession(username, password);
    }
    
    /**
     * Close the session down on finalize
     */
    protected void finalize() {
        AuthenticationUtils.endSession();
    }
    
    
    /**
     * Helper method to make apply the versionable aspect to a given reference
     * <p>
     * See sample 4 for more CML examples
     * 
     * @param respositoryService    the respository service
     * @param reference             the reference
     * @throws Exception
     */
    public static void cmsMakeVersionable(RepositoryServiceSoapBindingStub respositoryService, Reference reference)
        throws Exception
    {
        // Create the add aspect query object
        Predicate predicate = new Predicate(new Reference[]{reference}, null, null);
        CMLAddAspect addAspect = new CMLAddAspect(Constants.ASPECT_VERSIONABLE, null, predicate, null); 
        
        // Create the content management language query
        CML cml = new CML();
        cml.setAddAspect(new CMLAddAspect[]{addAspect});
        
        // Execute the query, which will add the versionable aspect to the node is question
        respositoryService.update(cml);
    }

    
    /**
     * Upload a file from the file system into a specified file in cms.
     * @param filePath Absolute path to file to be uploaded
     * @param cmsPath CMS space path to upload the content into (e.g. /app:company_home/cm:guest_home).
     */
    public void cmsUploadContent(String filePath, String cmsPath) throws Exception {
        String cmsParent=cmsPath;
        String childName=filePath.substring(filePath.lastIndexOf("/")+1);
        String fileParent=filePath.substring(0, filePath.lastIndexOf("/"));
        String childsQname=Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL, toQname(childName));

        // Get the content and authoring service
        RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();
        ContentServiceSoapBindingStub contentService = WebServiceFactory.getContentService();
        AuthoringServiceSoapBindingStub authoringService = WebServiceFactory.getAuthoringService();

        String mimeType=null;
        String encoding=null;
        
        try {
            try {
                mimeType=queryContentDetails(filePath, "mimeType");
                encoding=queryContentDetails(filePath, "encoding");
                
                // Happy path (no exception): the content already exists, and we're simply
                // going to create a new version. 
    
                // get a reference to the content we're updating
                Reference childReference = new Reference(STORE, null, toQname(cmsParent+"/cm:"+childName)); 
    
                // Checkout the content, placing the working document in the same folder
                Predicate itemsToCheckOut = new Predicate(new Reference[]{childReference}, null, null);
                CheckoutResult checkOutResult = authoringService.checkout(itemsToCheckOut, null);
    
                // Get a reference to the working copy
                Reference workingCopyReference = checkOutResult.getWorkingCopies()[0];
                
                // Update the content of the working copy
                ContentFormat format = new ContentFormat(mimeType, encoding);
                File file=new File(filePath);
                byte[] buff=new byte[(int) file.length()];
                FileInputStream ins=new FileInputStream(file);
                ins.read(buff);
                ins.close();
                contentService.write(workingCopyReference, Constants.PROP_CONTENT, buff, format);
                
                // Now check the working copy in with a description of the change made that will be recorded in the version history
                Predicate predicate = new Predicate(new Reference[]{workingCopyReference}, null, null);
                NamedValue[] comments = new NamedValue[]{Utils.createNamedValue("description", "The content has been updated")};
                authoringService.checkin(predicate, comments, false);
                
                System.out.println("cmsUploadContent updated '"+cmsParent+"/"+childName+"'");
            }
            catch(ContentDetailException e) {
                throw e;
            }
            catch(Throwable e) {
                String[] aspectNames=safeToStringArray(queryContentDetails(filePath, "aspect[]"));
    
                // what if this returns an empty string? 
                String[] propertyNames=safeToStringArray(queryContentDetails(filePath, "property/name[]"));
                String description=queryContentDetails(filePath, "description");
     
                //
                // The content doesn't already exist, so we have to create a new node, upload to it, then make it versionable.
                //
                UpdateResult[] updateResult; 
    
                // Create content node
                ParentReference parentReference = new ParentReference(STORE, null, toQname(cmsParent), Constants.ASSOC_CONTAINS, childsQname);
                NamedValue[] props = new NamedValue[propertyNames.length+2];
                
                props[0]=Utils.createNamedValue(Constants.PROP_NAME, childName);
                props[1]=Utils.createNamedValue(Constants.PROP_DESCRIPTION, description);
                for(int i=0 ; i<propertyNames.length ; i++) {
                        String val=queryContentDetails(filePath, "property[name='"+propertyNames[i]+"']/value");
                        props[i+2]=Utils.createNamedValue(propertyNames[i], val);
                }
                
                CMLCreate create2 = new CMLCreate("1", parentReference, null, null, null, Constants.TYPE_CONTENT, props);
                CML cml = new CML();
                cml.setCreate(new CMLCreate[]{create2});
    
                try {
                    updateResult=WebServiceFactory.getRepositoryService().update(cml);
                }
                catch(RepositoryFault re) {
                    cmsCreateSpace(cmsParent, fileParent);
                    updateResult=WebServiceFactory.getRepositoryService().update(cml);
                }
                
                // Upload content into the new node
                File file=new File(filePath);
                byte[] buff=new byte[(int) file.length()];
                FileInputStream ins=new FileInputStream(file);
                ins.read(buff);
                ins.close();
    
                ContentFormat format = new ContentFormat(mimeType, encoding);
                Content content=contentService.write(updateResult[0].getDestination(), Constants.PROP_CONTENT, buff, format);
                
                // If the ContentDetails.xml defines any aspects for this content...
                if (aspectNames.length>0) {
                    // create an array of CMLAddAspects big enough to hold all the aspects defined
                    CMLAddAspect[] aspectArray=new CMLAddAspect[aspectNames.length];
                    
                    Predicate predicate = new Predicate(new Reference[]{content.getNode()}, null, null);
                    
                    // create a CMLAddAspect for each aspect named in aspectNames
                    for(int i=0 ; i<aspectNames.length ; i++) {
                        aspectArray[i]=new CMLAddAspect(aspectNames[i], null, predicate, null);
                    }
    
                    CML vcml = new CML();
    
                    vcml.setAddAspect(aspectArray);
                    
                    repositoryService.update(vcml);
                }
    
                System.out.println("cmsUploadContent created '"+cmsParent+"/"+childName+"'");
            }
        }
        catch(ContentDetailException e) {
            System.err.println("Ignored content:"+filePath+" due to:"+e);
         }
    }
    
    /**
     * Create a new folder in CMS. If the folder already exists this method will have no effect.
     * @param parent The parent folder's qname to create the new folder.
     * @param child The new folder's name.
     * @return results of the create call, or null if the folder already existed.
     * @throws RepositoryFault
     * @throws RemoteException
     */
    public Node cmsCreateSpace(String spacePath, String folderPath) throws Exception {
        try
        {
            // If the folder already exisis just return it's node
            Reference folder = new Reference(STORE, null, toQname(spacePath)); 
            Node[] n=WebServiceFactory.getRepositoryService().get(new Predicate(new Reference[]{folder}, STORE, null));
            return n[0];
        }
        catch (Exception exception)
        {
            String spaceParent=spacePath.substring(0, spacePath.lastIndexOf('/'));
            String folderParent=folderPath.substring(0, folderPath.lastIndexOf("/"));
            String child=spacePath.substring(spacePath.lastIndexOf("/cm:")+4);
            String description=querySpaceDetails(folderPath, "description");
            
            cmsCreateSpace(spaceParent, folderParent);
            
            // The folder doesn't exist, so we'll create it. First: create the parent reference
            String childsQname=Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL, toQname(child));
            ParentReference parentReference = new ParentReference(STORE, null, toQname(spaceParent), Constants.ASSOC_CONTAINS, childsQname);

            // Create the new folder
            NamedValue[] properties = new NamedValue[]{
                    Utils.createNamedValue(Constants.PROP_NAME, child),
                    Utils.createNamedValue(Constants.PROP_DESCRIPTION, description)};            
            CMLCreate create = new CMLCreate("1", parentReference, null, null, null, Constants.TYPE_FOLDER, properties);
            CML cml = new CML();
            cml.setCreate(new CMLCreate[]{create});
            WebServiceFactory.getRepositoryService().update(cml);
            
            System.out.println("cmsCreateSpace: created '"+spaceParent+"/"+child+"'");

            return cmsCreateSpace(spacePath, folderParent);
        }
    }
    
    /*
     * Convert a string into a qname.
     */
    private String toQname(String name) {
        return name.replace(' ', '_').toLowerCase();
    }

    /**
     * Query the content details file for a specific element for the named file.
     * @param filePath File to query for
     * @param element Element to lookup for the file
     * @return Value found, or null if no valud is found.
     * @throws Exception 
     */
    private String queryContentDetails(String filePath, String element) throws ContentDetailException {
        String contentDetailFile=filePath.substring(0, filePath.lastIndexOf("/"))+"/ContentDetails.xml";
        String filename=filePath.substring(filePath.lastIndexOf("/")+1);
        String fileXpath="/contentDetails/content[@file='"+filename+"']";
        String fullXpath=fileXpath+"/"+element+"/text()";

        try {
            if (!cache.contains(contentDetailFile)) {
                XMLString detailsXml=new XMLString(new File(contentDetailFile));
                cache.put(contentDetailFile, detailsXml);
            }
    
            if (cache.get(contentDetailFile).eval(fileXpath)==null) {
                throw new ContentDetailException("ContentDetails.xml does not define '"+element+"' for '"+filePath+"'");
            }
    
            return cache.get(contentDetailFile).evalToText(fullXpath);
        }
        catch(FileNotFoundException e) {
            throw new ContentDetailException("ContentDetails.xml not found in "+contentDetailFile+" for "+filePath);
        }
        catch(IOException e) {
            throw new ContentDetailException("IOException reading ContentDetails.xml in "+contentDetailFile+" for "+filePath);
        }
        catch (SAXException e) {
            throw new ContentDetailException("XML Exception reading ContentDetails.xml in "+contentDetailFile+" for "+filePath, e);
        }
        catch (TransformerException e) {
            throw new ContentDetailException("Transformation Exception reading ContentDetails.xml in "+contentDetailFile+" for "+filePath, e);
        }
    }

    /**
     * Query the content details file for the specified folder and return the value of named element.
     * @param filePath File to query for
     * @param element Element to lookup for the file
     * @return Value found, or null if no valud is found.
     * @throws Exception 
     */
    private String querySpaceDetails(String folderPath, String element) throws ContentDetailException {
        String contentDetailFile=folderPath+"/"+"ContentDetails.xml";
        String xpath="/contentDetails/space/"+element;
 
        try {
            if (!cache.contains(contentDetailFile)) {
                XMLString s=new XMLString(new File(contentDetailFile));
                cache.put(contentDetailFile, s);
            }
            String res=cache.get(contentDetailFile).evalToText(xpath);
    
            if ("".equals(res)) {
                throw new ContentDetailException("ContentDetails.xml does not define '"+element+"' for '"+folderPath+"'");
            }
            
            return res;
        }
        catch(FileNotFoundException e) {
            throw new ContentDetailException("ContentDetails.xml not found in "+folderPath);
        }
        catch(IOException e) {
            throw new ContentDetailException("IOException reading ContentDetails.xml in "+folderPath);
        }
        catch (SAXException e) {
            throw new ContentDetailException("XML Exception reading ContentDetails.xml in "+folderPath, e);
        }
        catch (TransformerException e) {
            throw new ContentDetailException("Transformation Exception reading ContentDetails.xml in "+folderPath, e);
        }
    }

    private String[] safeToStringArray(String string) {
        if (string==null || string.length()==0) {
            return new String[0];
        }
        else {
            return string.split("[|]");
        }
    }

    /**
     * Exception thrown when an error is encountered processing a ContentDetails.xml file.
     */
    private class ContentDetailException extends Exception {
        String message=null;
        Throwable cause=null;
        
        public ContentDetailException(String message, Throwable cause) {
            this.message=message;
            this.cause=cause;
        }

        public ContentDetailException(String message) {
            this.message=message;
        }

        public Throwable getCause() {
            return cause;
        }

        public String getMessage() {
            return message;
        }

        public String toString() {
            return cause==null ? message : message+" due to:"+cause;
        }
    }
}
