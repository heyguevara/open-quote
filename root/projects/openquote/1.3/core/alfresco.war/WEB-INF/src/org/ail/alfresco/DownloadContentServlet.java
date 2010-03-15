package org.ail.alfresco;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Wrapper to Alfresco's DownloadContentServlet. Alfresco's servlet reports an error code of
 * 500 when content cannot be found. This wrapper reports a 404 instead - this avoids a lot
 * of unnecessary exceptions from being shown on the JBoss console.
 */
public class DownloadContentServlet extends org.alfresco.web.app.servlet.DownloadContentServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            super.doGet(request, response);
        }
        catch(IllegalArgumentException e) {
            if (e.getMessage().contains("Unable to resolve")) {
                response.sendError(404);
            }
        }
        catch(RuntimeException e) {
            if (e.getMessage().contains("Failed to authenticate")) {
                response.sendError(401);
            }
        }
    }
}
