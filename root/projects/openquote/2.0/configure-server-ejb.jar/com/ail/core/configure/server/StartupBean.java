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
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import com.ail.core.CoreProxy;
import com.ail.core.configure.AbstractConfigurationLoader;

@Singleton
@Startup
public class StartupBean {
    private boolean resetRequired = false;
    private final static int RETRY_TIMEOUT_MS = 10000;
    @Resource
    private TimerService timerService;

    @PostConstruct
    public void onStartup() {
        if (configurationsHaveNotBeenReset()) {

            new ServerBean().resetCoreConfiguration();

            resetRequired = true;
        }

        setRetryTimer(RETRY_TIMEOUT_MS);
    }

    @Timeout
    public void onTimeout() {
        if (resetRequired) {
            if (configurationResetFails()) {
                setRetryTimer(RETRY_TIMEOUT_MS);
            }
        }
        else {
            if (!isProductRepoAvailable()) {
                setRetryTimer(RETRY_TIMEOUT_MS);
            }
            else {
                new CoreProxy().logInfo("OpenQuote product repository started.");
            }
        }
    }

    private boolean configurationResetFails() {
        if (isProductRepoAvailable()) {
            new ServerBean().resetAllConfigurations();
            new CoreProxy().logInfo("OpenQuote product repository started.");
            return false;
        } else {
            return true;
        }
    }

    private boolean configurationsHaveNotBeenReset() {
        return !AbstractConfigurationLoader.loadLoader().isConfigurationRepositoryCreated();
    }

    private void setRetryTimer(long intervalDuration) {
        new CoreProxy().logInfo("Product content not yet available, retrying configuration reset in " + RETRY_TIMEOUT_MS / 1000 + " seconds.");
        timerService.createSingleActionTimer(intervalDuration, new TimerConfig());
    }

    private boolean isProductRepoAvailable() {
        URL repoTestURL = null;
        InputStream testStream = null;

        CoreProxy cp = new CoreProxy();

        try {
            // Build the URL which points into the product repo
            String host = cp.getParameterValue("ProductURLHandler.Host");
            Integer port = new Integer(cp.getParameterValue("ProductURLHandler.Port"));
            repoTestURL = new URL("product", host, port, "/AIL/Base/Registry.xml");
        } catch (MalformedURLException e) {
            cp.logError("ProductURLHandler.Host and/or ProductURLHandler.Port are not configured correctly in CoreDefaultConfig.xml");
        }

        // If we can open the stream then return true - the repo is available.
        // Otherwise return false.
        try {
            testStream = repoTestURL.openStream();
            testStream.close();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
}
