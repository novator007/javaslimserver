package org.bff.slimserver.events;

import org.bff.slimserver.SlimPlayer;

/**
 * Represents a change in the position of a playing song.
 * @author Bill Findeisen
 * @version 1.0
 */
public class SleepChangeEvent extends java.util.EventObject {
    private static final long serialVersionUID = 20101010L;
    private int id;
    private transient SlimPlayer player;
    
    /**
     * sleep was stopped
     */
    public static final int SLEEP_STOPPED = 0;
    /**
     * sleep was started
     */
    public static final int SLEEP_STARTED = 1;

    /**
     * Creates a new instance of TrackPositionEvent.
     * @param source the object on which the Event initially occurred
     * @param newTime the new elapsed time of the song
     */
    public SleepChangeEvent(Object source, SlimPlayer p, int id) {
        super(source);
        setPlayer(p);
        setId(id);
    }

    /**
     * @return the player
     */
    public SlimPlayer getPlayer() {
        return player;
    }

    /**
     * @param player the player to set
     */
    private void setPlayer(SlimPlayer player) {
        this.player = player;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    private void setId(int id) {
        this.id = id;
    }
}
