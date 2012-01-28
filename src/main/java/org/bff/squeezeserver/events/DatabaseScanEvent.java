package org.bff.squeezeserver.events;

import java.util.EventObject;

/**
 * An event used to identify a database scan started or ended.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class DatabaseScanEvent extends EventObject {
    private int id;

    public static final int SCAN_STARTED = 0;
    public static final int SCAN_ENDED = 1;

    /**
     * Creates a new instance of DatabaseScanEvent
     *
     * @param source      the object on which the Event initially occurred
     * @param isConnected the connection status
     */
    public DatabaseScanEvent(Object source, int id) {
        super(source);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
