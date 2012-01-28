package org.bff.squeezeserver.events;

import java.util.EventObject;

/**
 * An event used to identify a change in connection status.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class ConnectionChangeEvent extends EventObject {
    private boolean connected;
    private String msg;

    /**
     * Creates a new instance of ConnectionChangeEvent
     *
     * @param source      the object on which the Event initially occurred
     * @param isConnected the connection status
     */
    public ConnectionChangeEvent(Object source, boolean isConnected) {
        super(source);
        this.connected = isConnected;
    }

    /**
     * Creates a new instance of ConnectionChangeEvent
     *
     * @param msg         an optional message
     * @param source      the object on which the Event initially occurred
     * @param isConnected the connection status
     */
    public ConnectionChangeEvent(Object source, boolean isConnected, String msg) {
        super(source);
        this.connected = isConnected;
        this.msg = msg;
    }

    /**
     * Returns true if there is a connection with the Slim Server.  If there is no
     * connection returns false.
     *
     * @return true if connected; false otherwise
     */
    public boolean isConnected() {
        return (connected);
    }

    /**
     * Returns the message attached to this event.  If there is no message null
     * is returned.
     *
     * @return the optional message
     */
    public String getMsg() {
        return msg;
    }
}
