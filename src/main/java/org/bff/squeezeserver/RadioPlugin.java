/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

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
}
