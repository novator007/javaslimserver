package org.bff.squeezeserver.events;

/**
 * The listener interface for receiving track position change events. The class
 * that is interested in processing a position event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addTrackPositionChangeListener</CODE> method.
 * When the position event occurs, that object's <CODE>trackPositionChanged</CODE>
 * method is invoked.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public interface SleepChangeListener {
    /**
     * Invoked when a track position change occurs.
     *
     * @param event the event fired
     */
    void sleepTimeChanged(SleepChangeEvent event);
}
