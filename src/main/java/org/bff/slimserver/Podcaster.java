/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

/**
 * @author bfindeisen
 */
public class Podcaster extends Plugin {

    private static final String PLUGIN_COMMAND = Constants.CMD_PODCAST;

    /**
     * Creates a new instance of Database
     *
     * @param squeezeServer the {@link SqueezeServer} for this database
     */
    public Podcaster(SqueezeServer squeezeServer) {
        super(squeezeServer);
    }

    @Override
    public String getCommand() {
        return PLUGIN_COMMAND;
    }
}
