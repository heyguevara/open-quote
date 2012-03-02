package com.ail.openquote.pageflow;

import java.util.Enumeration;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;

import com.ail.openquote.Quotation;

public class MockPortletSession implements PortletSession {
	private Quotation quotation;
	
	public MockPortletSession(Quotation quotation) {
		this.quotation = quotation;
	}

	public Object getAttribute(String arg0) throws IllegalStateException,
			IllegalArgumentException {
		if ("quotation".equals(arg0)) {
			return quotation;
		}

		return null;
	}

	public Object getAttribute(String arg0, int arg1)
			throws IllegalStateException, IllegalArgumentException {
		return null;
	}

	public Enumeration<String> getAttributeNames() throws IllegalStateException {
		return null;
	}

	public Enumeration<String> getAttributeNames(int arg0) throws IllegalStateException {
		return null;
	}

	public long getCreationTime() throws IllegalStateException {
		return 0;
	}

	public String getId() {
		return null;
	}

	public long getLastAccessedTime() {
		return 0;
	}

	public int getMaxInactiveInterval() {
		return 0;
	}

	public PortletContext getPortletContext() {
		return null;
	}

	public void invalidate() throws IllegalStateException {
	}

	public boolean isNew() throws IllegalStateException {
		return false;
	}

	public void removeAttribute(String arg0) throws IllegalStateException,
			IllegalArgumentException {
	}

	public void removeAttribute(String arg0, int arg1)
			throws IllegalStateException, IllegalArgumentException {
	}

	public void setAttribute(String arg0, Object arg1)
			throws IllegalStateException, IllegalArgumentException {
	}

	public void setAttribute(String arg0, Object arg1, int arg2)
			throws IllegalStateException, IllegalArgumentException {
	}

	public void setMaxInactiveInterval(int arg0) {
	}
}
