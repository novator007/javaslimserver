package org.bff.slimserver.events;

/**
 * The listener interface for receiving connection change events. The class 
 * that is interested in processing a connection event implements this interface, 
 * and the object created with that class is registered with a component 
 * using the component's <CODE>addConnectionChangeListener</CODE> method. When the 
 * connection event occurs, that object's <CODE>connectionChangeEventReceived</CODE> 
 * method is invoked.
 * @author Bill Findeisen
 * @version 1.0
 */
public interface DatabaseScanListener {
    /**
     * Invoked when a connection change event occurs.
     * @param event the event received
     */
    public void databaseScanEventReceived(DatabaseScanEvent event);
}
