/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.exception.SlimConnectionException;

/**
 *
 * @author bfindeisen
 */
public class SlimServerJSON extends SlimServer {

    public SlimServerJSON(String server) throws SlimConnectionException {
        this(server, DEFAULT_WEB_PORT);
    }

    public SlimServerJSON(String server, int webPort) throws SlimConnectionException {
        this(server, webPort, null, null);
    }

    public SlimServerJSON(String server, String user, String password) throws SlimConnectionException {
        this(server, DEFAULT_WEB_PORT, user, password);
    }

    public SlimServerJSON(String server, int webPort, String user, String password) throws SlimConnectionException {
        super(server, user, password);
    }

    /**
     * Send JSON requests forming a valid CLI command to /jsonrpc.js:
     * get the server status:
     * {"id":1,"method":"slim.request","params":["",["serverstatus"]]}
     *
     *  get player status with song information:
     * {"id":1,"method":"slim.request","params":["67:1d:9b:bb:a2:85",["status","-",1,"tags:gABbehldiqtyrSuoKLN"]]}
     *
     * jump to the next song:
     * {"id":1,"method":"slim.request","params":["67:1d:9b:bb:a2:85",["button","jump_fwd"]]}
     *
     * @param command
     * @return
     */
    @Override
    public synchronized String[] sendCommand(SlimCommand command) {


        String[] retString = null;

        return retString;
    }

    public static void main(String[] args) {
        
    }
}
