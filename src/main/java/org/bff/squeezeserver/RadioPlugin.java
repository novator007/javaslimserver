/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

import org.bff.squeezeserver.domain.radio.Radio;
import org.bff.squeezeserver.domain.radio.RadioAudioDetails;
import org.bff.squeezeserver.exception.NetworkException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author bfindeisen
 */
public class RadioPlugin extends Plugin {

    private static final String PLUGIN_COMMAND = "radio";

    /**
     * Creates a new instance of RadioPlugin
     *
     * @param squeezeServer the {@link SqueezeServer} for this database
     */
    public RadioPlugin(SqueezeServer squeezeServer) {
        super(squeezeServer);
    }

    @Override
    public String getCommand() {
        return PLUGIN_COMMAND;
    }

    public void loadRadio(Radio radio, Player player) {

    }

    public Collection<Radio> getAvailableRadios() {
        List<Radio> radioList = new ArrayList<Radio>();
        return radioList;
    }

    public RadioAudioDetails getAudioDetails(Radio radio, Player player) throws NetworkException {
        RadioAudioDetails rad = new RadioAudioDetails(radio, getCommand());
        return rad;
    }
}
