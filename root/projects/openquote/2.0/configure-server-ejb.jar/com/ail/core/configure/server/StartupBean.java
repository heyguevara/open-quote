/* Copyright Applied Industrial Logic Limited 2003. All rights Reserved */
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
package com.ail.core.configure.server;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.ail.core.CoreProxy;
import com.ail.core.configure.AbstractConfigurationLoader;

@Singleton
@Startup
public class StartupBean {
    /**
     * This method will sleep the thread until the product repository is
     * available.
     */
    private void waitForProductRepoToStart() {
        URL repoTestURL = null;
        InputStream testStream = null;

        try {
            // Build the URL which points into the product repo
            CoreProxy cp = new CoreProxy();

            String host = cp.getParameterValue("ProductURLHandler.Host");
            Integer port = new Integer(cp.getParameterValue("ProductURLHandler.Port"));

            repoTestURL = new URL("product", host, port, "/AIL/Base/Registry.xml");
            
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        do {
            try {
                testStream = repoTestURL.openStream();
                testStream.close();
                return;
            } catch (Throwable e) {
                // ignore
            }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e1) {
                // ignore
            }
        } while (true);
    }

    @PostConstruct
    public void resetConfigurations() {
        // If we detect that the configuration repository does not exist, use
        // the
        // configuration server's reset methods to create it and populate it.
        if (!AbstractConfigurationLoader.loadLoader().isConfigurationRepositoryCreated()) {
            // This really should be an @EJB, but a bug in JBoss 7 prevents
            // Singleton's from accessing EJBs within a @PostConstruct method.
            ServerBean server = new ServerBean();
            server.resetCoreConfiguration();

            waitForProductRepoToStart();

            server.resetAllConfigurations();
        }
    }
}
