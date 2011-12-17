/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.slimserver.comm;

import org.bff.slimserver.SlimCommand;
import org.bff.slimserver.exception.SlimConnectionException;

/**
 *
 * @author bfindeisen
 */
public interface SlimCommunicator {
    
    public String[] sendCommand(String slimCommand) throws SlimConnectionException;

    public String[] sendCommand(String slimCommand, String param) throws SlimConnectionException;

    public String[] sendCommand(SlimCommand command) throws SlimConnectionException;

    public boolean isConnected();

    public String getVersion();

    public String getEncoding();

}
