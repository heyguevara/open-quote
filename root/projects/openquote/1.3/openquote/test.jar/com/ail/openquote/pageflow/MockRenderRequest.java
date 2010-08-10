package com.ail.openquote.pageflow;

import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.WindowState;

public class MockRenderRequest implements RenderRequest {
	private Locale locale;
	private PortletSession session;

	public MockRenderRequest(Locale locale, PortletSession session) {
		this.locale = locale;
		this.session = session;
	}

	public Object getAttribute(String arg0) throws IllegalArgumentException {
		return null;
	}

	public Enumeration<String> getAttributeNames() {
		return null;
	}

	public String getAuthType() {
		return null;
	}

	public String getContextPath() {
		return null;
	}

	public Locale getLocale() {
		return locale;
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getLocales() {
		return null;
	}

	public String getParameter(String arg0) throws IllegalArgumentException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Map getParameterMap() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getParameterNames() {
		return null;
	}

	public String[] getParameterValues(String arg0)
			throws IllegalArgumentException {
		return null;
	}

	public PortletSession getPortletSession() {
		return session;
	}

	public PortletSession getPortletSession(boolean arg0) {
		return session;
	}

	public PortletPreferences getPreferences() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getProperties(String arg0)
			throws IllegalArgumentException {
		return null;
	}

	public String getProperty(String arg0) throws IllegalArgumentException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getPropertyNames() {
		return null;
	}

	public String getRemoteUser() {
		return null;
	}

	public String getRequestedSessionId() {
		return null;
	}

	public String getResponseContentType() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getResponseContentTypes() {
		return null;
	}

	public String getScheme() {
		return null;
	}

	public String getServerName() {
		return null;
	}

	public int getServerPort() {
		return 0;
	}

	public Principal getUserPrincipal() {
		return null;
	}

	public boolean isRequestedSessionIdValid() {
		return false;
	}

	public boolean isSecure() {
		return false;
	}

	public boolean isUserInRole(String arg0) {
		return false;
	}

	public void removeAttribute(String arg0) throws IllegalArgumentException {
	}

	public void setAttribute(String arg0, Object arg1)
			throws IllegalArgumentException {
	}

	public PortalContext getPortalContext() {
		return null;
	}

	public PortletMode getPortletMode() {
		return null;
	}

	public WindowState getWindowState() {
		return null;
	}

	public boolean isPortletModeAllowed(PortletMode arg0) {
		return false;
	}

	public boolean isWindowStateAllowed(WindowState arg0) {
		return false;
	}
}
