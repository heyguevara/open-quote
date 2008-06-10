package com.ail.core.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import com.ail.core.CoreProxy;

/**
 * Adapted from the JBoss News Portlet to deal with long SourceForge style feed descriptions, and avoid throwing exception when a feed cannot be read.
 */
public class NewsReaderPortlet extends GenericPortlet {

    /** Compiled Template for output RSS 2.0 */
    private Templates rssFeedTransformer;

    private static final String E_XSL_UNREADABLE = "Unable to prepare XSL files.";

    /** Compile the XSL File. */
    public void init() {
        try {
            InputStream inputstream_2 = getPortletContext().getResourceAsStream("/WEB-INF/RssFeedTransformer.xsl");
            StreamSource xslSource_2 = new StreamSource(inputstream_2);
            TransformerFactory tFactory_2 = TransformerFactory.newInstance();
            this.rssFeedTransformer = tFactory_2.newTemplates(xslSource_2);
        }
        catch (TransformerConfigurationException tce) {
            new CoreProxy().logError(E_XSL_UNREADABLE, tce);
        }
    }

    protected void doView(RenderRequest request, RenderResponse response) throws IOException, PortletException {
        PrintWriter writer=null;
        
        response.setProperty("expiration-cache", request.getPreferences().getValue("expires", "180"));

        InputStream xmlInputStream = null;
        InputStream xslInputStream = null;

        String newURL = null;
        try {
            newURL = request.getParameter("newurl");
            response.setContentType("text/html");

             writer = response.getWriter();

            if (null == newURL) {
                newURL = request.getPreferences().getValue("RssXml", null);
                if (newURL==null) {
                    throw new IllegalArgumentException("Feed is not defined: Use the portlet preference 'RssXml' to define a feed URL.");
                }
            }

            xmlInputStream = new URL(newURL).openStream();
            if (xmlInputStream == null) {
                throw new IllegalArgumentException("Failed to read feed: "+newURL);
            }

            // Ready the parsers
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Transformer transformer = this.rssFeedTransformer.newTransformer();

            Source xmlSource = null;
            
            Document document = docBuilder.parse(xmlInputStream);
            transformer = this.rssFeedTransformer.newTransformer();
            xmlSource = new DOMSource(document);

            // Transform document
            StreamResult outStream = new StreamResult(writer);
            transformer.transform(xmlSource, outStream);
        }
        catch (Throwable e) {
            writer.write("Failed to retrieve News Feed ("+e.toString()+")!");
        }
        finally // close all streams
        {
            writer.close();

            if (xmlInputStream != null) {
                xmlInputStream.close();
            }

            if (xslInputStream != null) {
                xslInputStream.close();
            }
        }
    }
}