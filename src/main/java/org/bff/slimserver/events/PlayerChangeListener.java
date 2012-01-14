package org.bff.slimserver.events;

/**
 * The listener interface for receiving player events. The class that is
 * interested in processing a player event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addPlayerChangeListener</CODE> method. When the
 * player event occurs, that object's <CODE>playerChanged</CODE> method is
 * invoked.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public interface PlayerChangeListener {
    /**
     * Invoked when a player change event occurs.
     *
     * @param event the event fired
     */
    public void playerChanged(PlayerChangeEvent event);
}
