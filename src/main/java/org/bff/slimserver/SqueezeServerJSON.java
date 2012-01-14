/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.exception.ConnectionException;

/**
 * @author bfindeisen
 */
public class SqueezeServerJSON extends SqueezeServer {

    public SqueezeServerJSON(String server) throws ConnectionException {
        this(server, DEFAULT_WEB_PORT);
    }

    public SqueezeServerJSON(String server, int webPort) throws ConnectionException {
        this(server, webPort, null, null);
    }

    public SqueezeServerJSON(String server, String user, String password) throws ConnectionException {
        this(server, DEFAULT_WEB_PORT, user, password);
    }

    public SqueezeServerJSON(String server, int webPort, String user, String password) throws ConnectionException {
        super(server, user, password);
    }

    /**
     * Send JSON requests forming a valid CLI command to /jsonrpc.js:
     * get the server status:
     * {"id":1,"method":"slim.request","params":["",["serverstatus"]]}
     * <p/>
     * get player status with song information:
     * {"id":1,"method":"slim.request","params":["67:1d:9b:bb:a2:85",["status","-",1,"tags:gABbehldiqtyrSuoKLN"]]}
     * <p/>
     * jump to the next song:
     * {"id":1,"method":"slim.request","params":["67:1d:9b:bb:a2:85",["button","jump_fwd"]]}
     *
     * @param command
     * @return
     */
    @Override
    public synchronized String[] sendCommand(Command command) {


        String[] retString = null;

        return retString;
    }

    public static void main(String[] args) {

    }
}
