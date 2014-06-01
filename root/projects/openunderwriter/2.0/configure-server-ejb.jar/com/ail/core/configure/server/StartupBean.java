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

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

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
    private final static int RETRY_ATTEMPS_BEFORE_WARNING = 20;
    private final static int RETRY_TIMEOUT_MS = 5000;

    private boolean resetRequired = false;
    private int resetRetryCount = 0;
    private CoreProxy coreProxy;
    @Resource
    private TimerService timerService;

    @PostConstruct
    public void onStartup() {
        coreProxy = new CoreProxy();

        if (configurationsHaveNotBeenReset()) {
            new ServerBean().resetCoreConfiguration();
            resetRequired = true;
        }

        setRetryTimer(RETRY_TIMEOUT_MS);
    }

    @Timeout
    public void onTimeout() {
        coreProxy = new CoreProxy();

        if (resetRequired) {
            if (configurationResetFails()) {
                setRetryTimer(RETRY_TIMEOUT_MS);
            }
        } else {
            if (!isProductRepoAvailable()) {
                setRetryTimer(RETRY_TIMEOUT_MS);
            } else {
                outputStartupMessage();
            }
        }
    }

    private boolean configurationResetFails() {
        if (isProductRepoAvailable()) {
            new ServerBean().resetAllConfigurations();
            outputStartupMessage();
            return false;
        } else {
            return true;
        }
    }

    private boolean configurationsHaveNotBeenReset() {
        return !AbstractConfigurationLoader.loadLoader().isConfigurationRepositoryCreated();
    }

    private void setRetryTimer(long intervalDuration) {
        if (resetRetryCount++ > RETRY_ATTEMPS_BEFORE_WARNING) {
            coreProxy.logWarning("Product content not yet available, retrying configuration reset in " + RETRY_TIMEOUT_MS / 1000 + " seconds.");
        }
        timerService.createSingleActionTimer(intervalDuration, new TimerConfig());
    }

    /**
     * Check that the product repository is online. There is a good chance that the repository will start
     * sometime after this bean. This check uses the value of CoreConfig's ProductReader.TestPath to poll
     * the repository.
     * @return true if the repository is online, false otherwise.
     */
    private boolean isProductRepoAvailable() {
        URL repoTestURL = null;
        InputStream testStream = null;

        // If we can open the stream then return true - the repo is available.
        // Otherwise return false.
        try {
            String testPath = coreProxy.getParameterValue("ProductReader.TestPath");
            repoTestURL = new URL(testPath);
            testStream = repoTestURL.openStream();
            testStream.close();
            coreProxy.logInfo("Product repository is ready.");
            return true;
        } catch (MalformedURLException e) {
            coreProxy.logError("ProductReader.TestPath is not configured correctly in CoreDefaultConfig.xml");
            return false;
        }
        catch (ConnectException e) {
            coreProxy.logDebug("Product repository is not responding to requests.");
            return false; 
        }
        catch (IOException e) {
            coreProxy.logDebug("Product repository responded to poll, but is not ready yet.");
            return false; 
        }
        finally {
            if (testStream!=null) {
                try {
                    testStream.close();
                } catch (IOException e) {
                    coreProxy.logError("Failed to close stream. This error needs to be investigated!");
                }
            }
        }
    }

    private void outputStartupMessage() {
        long upTime = ManagementFactory.getRuntimeMXBean().getUptime();

        String message = String.format("OpenUnderwriter startup completed in %d min, %d sec.", 
                TimeUnit.MILLISECONDS.toMinutes(upTime),
                TimeUnit.MILLISECONDS.toSeconds(upTime) - 
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(upTime)));

        coreProxy.logInfo(message);
    }

}
