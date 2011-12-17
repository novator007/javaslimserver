package org.bff.slimserver.monitor;

import java.util.ArrayList;
import java.util.List;
import org.bff.slimserver.SlimServer;
import org.bff.slimserver.events.ConnectionChangeEvent;
import org.bff.slimserver.events.ConnectionChangeListener;
import org.bff.slimserver.events.TrackPositionChangeEvent;
import org.bff.slimserver.events.TrackPositionChangeListener;

/**
 * SlimEventMonitor is the abstract base class for all event monitors.
 * 
 * @author Bill
 * @version 1.0
 */
public abstract class SlimEventMonitor {
    private SlimServer slimServer;
    
    private boolean connectedState;
    
    private List<TrackPositionChangeListener> trackListeners 
            = new ArrayList<TrackPositionChangeListener>();
    
    private List<ConnectionChangeListener> connectionListeners 
            = new ArrayList<ConnectionChangeListener>();
    
    /**
     * Default no argument constructor
     */
    public SlimEventMonitor() {
        setConnectedState(true);
    }
    
    /**
     * Constructor for this abstract base class.
     * @param server a connection to a SlimServer
     */
    public SlimEventMonitor(SlimServer server) {
        this.slimServer = server;
    }
    
    /**
     * Adds a {@link TrackPositionChangeListener} to this object to receive 
     * {@link TrackPositionChangeEvent}s.
     * @param tpcl the TrackPositionChangeListener to add
     */
    public synchronized void addTrackPositionChangeListener(TrackPositionChangeListener tpcl) {
        trackListeners.add(tpcl);
    }
    
    /**
     * Removes a {@link TrackPositionChangeListener} from this object.
     * @param tpcl the TrackPositionChangeListener to remove
     */
    public synchronized void removeTrackPositionChangeListener(TrackPositionChangeListener tpcl) {
        trackListeners.remove(tpcl);
    }
    
    /**
     * Sends the appropriate {@link TrackPositionChangeEvent} to all registered
     * {@link TrackPositionChangeListener}s.
     * @param newTime the new elapsed time
     */
    protected synchronized void fireTrackPositionChangeEvent(long newTime) {
        TrackPositionChangeEvent tpce = new TrackPositionChangeEvent(this, newTime);
        
        for(TrackPositionChangeListener tpcl : trackListeners) {
            tpcl.trackPositionChanged(tpce);
        }
    }

    /**
     * Adds a {@link ConnectionChangeListener} to this object to receive 
     * {@link ConnectionChangeEvent}s.
     * @param ccl the ConnectionChangeListerner to add
     */
    public synchronized void addConnectionChangeListener(ConnectionChangeListener ccl) {
        connectionListeners.add(ccl);
    }
    
    /**
     * Removes a {@link ConnectionChangeListener} from this object.
     * @param ccl the ConnectionChangeListener to remove
     */
    public synchronized void removeConnectionChangeListener(ConnectionChangeListener ccl) {
        connectionListeners.remove(ccl);
    }
    
    /**
     * Sends the appropriate {@link ConnectionChangeEvent} to all registered
     * {@link ConnectionChangeListener}s.
     * @param isConnected the connection status
     */
    protected synchronized void fireConnectionChangeEvent(boolean isConnected) {
        ConnectionChangeEvent cce = new ConnectionChangeEvent(this, isConnected);
        
        for(ConnectionChangeListener ccl : connectionListeners) {
            ccl.connectionChangeEventReceived(cce);
        }
    }
    
    /**
     * Checks the connection status of the SlimServer.  Fires a {@link ConnectionChangeEvent}
     * if the connection status changes.
     */
    protected final void checkConnection() {
        boolean conn = getSlimServer().isConnected();
        if(isConnectedState()!=conn) {
            setConnectedState(conn);
            fireConnectionChangeEvent(conn);
        }
    }
    
    /**
     * Returns the {@link SlimServer} for this monitor
     * @return the {@link SlimServer}
     */
    protected SlimServer getSlimServer() {
        return slimServer;
    }

    /**
     * Sets the {@link SlimServer} for this monitor
     * @param slimServer the {@link SlimServer}
     */
    protected void setSlimServer(SlimServer slimServer) {
        this.slimServer = slimServer;
    }

    /**
     * @return the connectedState
     */
    protected boolean isConnectedState() {
        return connectedState;
    }

    /**
     * @param connectedState the connectedState to set
     */
    protected void setConnectedState(boolean connectedState) {
        this.connectedState = connectedState;
    }
    
}
