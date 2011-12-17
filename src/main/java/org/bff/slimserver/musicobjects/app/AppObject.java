/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.musicobjects.app;

import org.bff.slimserver.musicobjects.SlimPluginObject;

/**
 *
 * @author bfindeisen
 */
public abstract class AppObject extends SlimPluginObject {

    private String appId;
    private SlimAvailableApp app;
    
    public AppObject(SlimAvailableApp app) {
        setApp(app);
    }

    /**
     * @return the radioId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param radioId the radioId to set
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return the radio
     */
    public SlimAvailableApp getApp() {
        return app;
    }

    /**
     * @param radio the radio to set
     */
    public void setApp(SlimAvailableApp app) {
        this.app = app;
    }
}
