package org.ail.alfresco;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.repo.security.permissions.AccessDeniedException;

/**
 * Wrapper to Alfresco's DownloadContentServlet. Alfresco's servlet throws exceptions which get dumped 
 * into the log rather than returning error codes. This wrapper deals with the most common exceptions
 * and maps them to HTTP error response codes, and does not dump them to the log. This is especially
 * necessary for us as the system frequently tries to access content that may not exist, and we don't
 * want the log to fill up with exceptions which amount to normal operation.
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
            else {
                throw e;
            }
        }
        catch(AccessDeniedException e) {
            response.sendError(401);
        }
        catch(RuntimeException e) {
            if (e.getMessage().contains("Access denied") || e.getMessage().contains("Failed to authenticate")) {
                response.sendError(401);
            }
            else {
                throw e;
            }
        }
    }
}
