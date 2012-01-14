/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.slimserver.comm;

import org.bff.slimserver.Command;
import org.bff.slimserver.exception.ConnectionException;

/**
 * @author bfindeisen
 */
public interface Communicator {

    public String[] sendCommand(String slimCommand) throws ConnectionException;

    public String[] sendCommand(String slimCommand, String param) throws ConnectionException;

    public String[] sendCommand(Command command) throws ConnectionException;

    public boolean isConnected();

    public String getVersion();

    public String getEncoding();

}
