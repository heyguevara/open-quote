package com.ail.openquote.pageflow;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

public class MockRenderResponse implements RenderResponse {
	private Locale locale;
	private PrintWriter printWriter;
	
	public MockRenderResponse(Locale locale, PrintWriter printWriter) {
		this.locale = locale;
		this.printWriter = printWriter;
	}

	public PortletURL createActionURL() {
		return null;
	}

	public PortletURL createRenderURL() {
		return null;
	}

	public void flushBuffer() throws IOException {
	}

	public int getBufferSize() {
		return 0;
	}

	public String getCharacterEncoding() {
		return null;
	}

	public String getContentType() {
		return null;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getNamespace() {
		return null;
	}

	public OutputStream getPortletOutputStream() throws IllegalStateException,
			IOException {
		return null;
	}

	public PrintWriter getWriter() throws IOException, IllegalStateException {
		return printWriter;
	}

	public boolean isCommitted() {
		return false;
	}

	public void reset() throws IllegalStateException {
	}

	public void resetBuffer() throws IllegalStateException {
	}

	public void setBufferSize(int arg0) throws IllegalStateException {
	}

	public void setContentType(String arg0) throws IllegalArgumentException {
	}

	public void setTitle(String arg0) {
	}

	public void addProperty(String arg0, String arg1)
			throws IllegalArgumentException {
	}

	public String encodeURL(String arg0) throws IllegalArgumentException {
		return null;
	}

	public void setProperty(String arg0, String arg1)
			throws IllegalArgumentException {
	}
}
