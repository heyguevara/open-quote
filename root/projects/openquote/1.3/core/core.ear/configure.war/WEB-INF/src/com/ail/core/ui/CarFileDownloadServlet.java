/* Copyright Applied Industrial Logic Limited 2005. All rights reserved. */
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
package com.ail.core.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used by the configure system to download CAR (configuration archive) files.
 */
public class CarFileDownloadServlet extends HttpServlet {

    /**
     * This servlet expects to recieve one parameter: carFile, which defines the absolute path 
     * to the file to be downloaded. <br/>
     * TODO: Performing the download via an intermediate file is less than optimal. There are two better 
     * alternatives, but nither proved reliable/possible:<ul> 
     * <li>The controller (ConfigureManager) could directly write the config's byte[] into the 
     * response stream. However, the ConfigureManager is invoked from a portal which (rightly, by 
     * the portal spec) doesn't provide direct access to the response. So no go there.</li>
     * <li>The controller could write the content into the session, and this servlet could pick it 
     * up and write it to the ServletResponse. This worked, but only for about 50% of requests - the
     * other 50% didn't recieve the same session object, and hence didn't see the content. Bummer.</li>
     * </ul>
     * The second option is probably workable with more effort, but the intermediate file is simple, quick, and
     * reliable, so we can live with it for now.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/car");
        response.setHeader("Content-Disposition", "attachement;filename=\"configure.car\"");
        response.setHeader("Pragma", "private");
        response.setHeader("Cache-Control", "private");

        // create a buffer for chunking the content.
        byte[] buffer=new byte[2048];

        // Get an input stream on the specified file, and an output stream into the response.
        File carFile=new File(request.getParameter("carFile"));
        FileInputStream fis=new FileInputStream(carFile);
        OutputStream os=response.getOutputStream();

        // copy input to output via the buffer
        for(int bytes=fis.read(buffer) ; bytes>0 ; bytes=fis.read(buffer)) {
            os.write(buffer, 0, bytes);
        }
        
        // close up
        os.close();
        fis.close();

        // be tidy and delete the intermediate file.
        carFile.delete();
    }
}
