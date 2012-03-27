package com.ail.openquote.quotationportlet;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.net.HttpURLConnection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.XMLString;

/**
 * @author matthewtomlinson
 * Test that WSRPQuotationPortlet is available
 * and that xml and html mime types selectable,
 * along with requesting a specific product.
 */

public class TestQuotationPortlet {

	private static String htmlContentType = "text/html";
	private static String xmlContentType = "text/xml";
	private static String productToRequest = "AIL.Demo.IrishBrokerPI";
	private static String urlReg = "http://localhost:8080/portal-wsrp/RegistrationService";
	private static String urlNxt = "http://localhost:8080/portal-wsrp/MarkupService";
	
	private static String productContentTest = "IBA/PIBA Professional Indemnity";

	private String registrationHandle = "";

	@Before
	public void setUp() throws Exception {
		// register consumer
		registrationHandle = registerConsumer(urlReg);
	}

	@After
	public void tearDown() throws Exception {
		// deregister consumer
		deregisterConsumer(registrationHandle, urlReg);
	}

	/**
	 * Test the XML mime type form request
	 * @throws Exception 
	 */
	@Test
	public void testXMLRequest() throws Exception{
		
		// get xml form
		String form = requestWSRPForm(registrationHandle, urlNxt, xmlContentType, productToRequest);
		boolean successProduct = form.contains(productContentTest);
		boolean successMime = form.contains(xmlContentType);

		assertTrue("\""+productContentTest+"\" Form content missing", successProduct);
		assertTrue("Mime type not "+xmlContentType, successMime);
		
	}

	/**
	 * Test the HTML mime type form request
	 * @throws Exception
	 */
    @Test
	public void testHTMLRequest() throws Exception{

		// get html form
		String form = requestWSRPForm(registrationHandle, urlNxt, htmlContentType, productToRequest);
		boolean successProduct = form.contains(productContentTest);
		boolean successMime = form.contains(htmlContentType);

		assertTrue("\""+productContentTest+"\" Form content missing", successProduct);
		assertTrue("Mime type not "+htmlContentType, successMime);
		
	}
	
	/**
	 * Register the WSRP Consumer and return the consumer handle
	 * @param urlReg WSRP producer registry URL
	 * @return consumer handle
	 * @throws IOException 
	 */
    @Test
	private String registerConsumer(String urlReg) throws IOException{

		// load consumer registration xml
		XMLString message = new XMLString(this.getClass().getResourceAsStream("RegisterWSRPConsumer.xml"));

		// register consumer
		String soapReturnReg = postMessage(urlReg, message.toStringWithoutEntityReferences(), "");
		
		// extract and return consumer handle
		String startString = ":registrationHandle>";
		int start = soapReturnReg.indexOf(startString)+startString.length();
		int end = soapReturnReg.indexOf("<", start);
		return soapReturnReg.substring(start, end);
	}
	
	/**
	 * Deregister the WSRP Consumer
	 * @param handle Registration handle
	 * @param urlReg WSRP producer registry URL
	 * @throws IOException 
	 */
    @Test
	private void deregisterConsumer(String handle, String urlReg) throws IOException{

		// load consumer deregistration xml
		XMLString message = new XMLString(this.getClass().getResourceAsStream("DeregisterWSRPConsumer.xml"));

		// set registration handle in message
		String xmlMessage = setRegistrationHandle(handle, message.toStringWithoutEntityReferences());

		// deregister consumer
		postMessage(urlReg, xmlMessage, "");
		
	}
	
	
	/**
	 * Request a WSRP form from the producer
	 * @param handle Consumer registration handle
	 * @param urlReg
	 * @param contentType
	 * @param productToRequest
	 * @return WSRP reponse
	 * @throws IOException 
	 */
    @Test
	private String requestWSRPForm(String handle, String urlReg, String contentType, String productToRequest) throws IOException{

		// load consumer registration xml
		XMLString message = new XMLString(this.getClass().getResourceAsStream("RequestWSRPForm.xml"));

		// set registration handle in message
		String xmlMessage = setRegistrationHandle(handle, message.toStringWithoutEntityReferences());

		// set mime type in message
		xmlMessage = setMimeType(contentType, xmlMessage);

		// request WSRP form
		String soapReturnReg = postMessage(urlReg, xmlMessage, productToRequest);
		
		return soapReturnReg;
	}
	
	/**
	 * Set the registration handle in a wsrp request message
	 * @param handle Registration handle
	 * @param xmlMessage wsrp request xml message
	 * @return wsrp request xml message with registration handle set
	 */
	private String setRegistrationHandle(String handle, String xmlMessage){
		return xmlMessage.replaceFirst("<ns1:registrationHandle></ns1:registrationHandle>", "<ns1:registrationHandle>"+handle+"</ns1:registrationHandle>");
	}
	
	/**
	 * Set the mime type in a wsrp request message
	 * @param mimeType content type
	 * @param xmlMessage wsrp request xml message
	 * @return wsrp request xml message with mime type set
	 */
	private String setMimeType(String mimeType, String xmlMessage){
		return xmlMessage.replaceFirst("<ns1:mimeTypes></ns1:mimeTypes>", "<ns1:mimeTypes>"+mimeType+"</ns1:mimeTypes>");
	}
	
	/**
	 * Post an xml message to a specified url
	 * @param urlString url to post message to
	 * @param xmlData message content
	 * @param product OpenQuote product to request
	 * @return response
	 * @throws IOException 
	 */
	private String postMessage(String urlString, String xmlData, String product) throws IOException{
		String result="";

    	// Create the URL and URL Connection
    	java.net.URL programUrl = new java.net.URL(urlString );
    	java.net.HttpURLConnection connection = (java.net.HttpURLConnection)programUrl.openConnection();
    			
    	((HttpURLConnection)connection).setRequestMethod("POST");
    	connection.setDoOutput(true);
    	//connection.setDoInput(true); //Only if you expect to read a response...
    	connection.setUseCaches(false); //Highly recommended...
    	connection.setRequestProperty("Content-Type", xmlContentType);
    	connection.setRequestProperty("openquote.product", product);
    	    	
    	PrintWriter output = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
    	output.println(xmlData);
    	//output.flush();
    	output.close();	
     
    	connection.connect();
    	InputStream is = connection.getInputStream();
    	InputStreamReader isr = new InputStreamReader(is);
    	BufferedReader br = new BufferedReader(isr);
     
    	String line = null;
     
    	while ((line = br.readLine()) != null) {
       		result=result+line+"\n";
    	}

    	return result;
	}

}
