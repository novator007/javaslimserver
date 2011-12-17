/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.musicobjects.radio;

import org.bff.slimserver.musicobjects.SlimPluginObject;

/**
 *
 * @author bfindeisen
 */
public abstract class RadioObject extends SlimPluginObject {

    private String radioId;
    private SlimAvailableRadio radio;
    
    public RadioObject(SlimAvailableRadio radio) {
        setRadio(radio);
    }

    /**
     * @return the radioId
     */
    public String getRadioId() {
        return radioId;
    }

    /**
     * @param radioId the radioId to set
     */
    public void setRadioId(String radioId) {
        this.radioId = radioId;
    }

    /**
     * @return the radio
     */
    public SlimAvailableRadio getRadio() {
        return radio;
    }

    /**
     * @param radio the radio to set
     */
    public void setRadio(SlimAvailableRadio radio) {
        this.radio = radio;
    }
}
