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
package com.ail.core.contentbrowser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Functions;

public class ContentBrowserPortlet extends GenericPortlet {
    private static final String URL_PATTERN_REGEX = 
        "((?:href|src)\\s*=\\s*)     # Capture preliminaries in $1.  \n"
      + "(?:                         # First look for URL in quotes. \n"
      + "   ([\"\'])                 #   Capture open quote in $2.   \n"
      + "   (?!http:)                #   If it isn't absolute...     \n"
      + "   /?(.+?)                  #    ...capture URL in $3       \n"
      + "   \\2                      #   Match the closing quote     \n"
      + " |                          # Look for non-quoted URL.      \n"
      + "   (?![\"\']|http:)         #   If it isn't absolute...     \n"
      + "   /?([^\\s>]+)             #    ...capture URL in $4       \n"
      + ")";

    private static final Pattern RELATIVE_URI_PATTERN = Pattern.compile(URL_PATTERN_REGEX, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS);

    private String homePageUrl = null;
    private String homePageHost = null;
    private String homePageProtocol = null;
    private int homePagePort;
    
    @Override
    public void init(PortletConfig pconfig) throws PortletException {
        super.init(pconfig);
        homePageUrl = this.getInitParameter("homePageUrl");
        
        try {
            URL home=new URL(homePageUrl);
            homePageProtocol = home.getProtocol();
            homePageHost = home.getHost();
            homePagePort = home.getPort();
        }
        catch(MalformedURLException e) {
            throw new PortletException("Failed to initialise portlet from. The parameter 'homePageUrl' is not a valid URL");
        }
    }

    @Override
    public void processAction(final ActionRequest req, final ActionResponse resp) throws PortletException, PortletSecurityException, IOException {
       String path = req.getParameter("path");
       resp.setRenderParameter("path", path);
    }
    
    private String path2Title(String path) {
        String p=path.substring(path.lastIndexOf('/')+1);
        return p.replaceAll("%20", " ");
    }
    
    @Override
    public void doView(final RenderRequest req, final RenderResponse resp) throws PortletException, PortletSecurityException, IOException {
        String path = req.getParameter("path");

        if (path!=null) {
            path=new URL(homePageProtocol, homePageHost, homePagePort, path).toExternalForm();
        }
        else {
            path=req.getPreferences().getValue("homePageUrl", homePageUrl);
        }
        
        resp.setContentType("text/html");
        resp.setTitle(path2Title(path));

        String rawContent=Functions.loadUrlContentAsString(new URL(path));
        StringBuffer processedContent = new StringBuffer();
        
        Matcher m = RELATIVE_URI_PATTERN.matcher(rawContent);
        while (m.find()) {
            String relURI = m.group(3) != null ? m.group(3) : m.group(4);
            if (!relURI.startsWith("mailto")) {
                if ("href=".equals(m.group(1))) {
                    String absoluteURI = buildURL(resp, "/" + relURI);
                    m.appendReplacement(processedContent, "$1\"" + absoluteURI + "\"");
                }
                else if ("src=".equals(m.group(1))) {
                    String absoluteURI = new URL(homePageProtocol, req.getServerName(), req.getServerPort(), "/"+relURI).toExternalForm(); 
                    m.appendReplacement(processedContent, "$1\"" + absoluteURI + "\"");
                }
            }
        }
        m.appendTail(processedContent);
        
        resp.getWriter().write(processedContent.toString());
    }

    /**
     * If the content includes embedded links to other pages within CMS, we want to make sure that
     * clicking on them results in the window on the current portal page is updated - hence this
     * method turns the link into a request back to this portlet. Without this, clicking a link
     * would take the user out of the portal.
     */
    private String buildURL(RenderResponse resp, String path) {
        // Only modify refs to alfresco content, leave other URLs as they are
        if (path.indexOf("alfresco")>=0) {
            PortletURL url = resp.createActionURL();
            url.setParameter("path", path);
            return url.toString();
        }
        else {
            return path;
        }
    }

}
