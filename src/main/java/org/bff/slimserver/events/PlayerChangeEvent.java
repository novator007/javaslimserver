package org.bff.slimserver.events;

/**
 * Represents a change in the status of a SqueezeServer music player.
 *
 * @author Bill Findeisen
 */
public class PlayerChangeEvent extends java.util.EventObject {

    private int id;
    private String msg;
    /**
     * the player was stopped
     */
    public static final int PLAYER_STOPPED = 0;
    /**
     * the player was started
     */
    public static final int PLAYER_STARTED = 1;
    /**
     * the player was paused
     */
    public static final int PLAYER_PAUSED = 2;
    /**
     * the player turning on
     */
    public static final int PLAYER_ON = 3;
    /**
     * the player turnong off
     */
    public static final int PLAYER_OFF = 4;
    /**
     * the volume was muted
     */
    public static final int PLAYER_MUTED = 5;
    /**
     * the volume was unmuted
     */
    public static final int PLAYER_UNMUTED = 6;
    /**
     * a specific song was requested by the player
     */
    public static final int PLAYER_SONG_SET = 9;
    /**
     * the player was taken off pause
     */
    public static final int PLAYER_UNPAUSED = 10;
    /**
     * the player is seeking within a song
     */
    public static final int PLAYER_SEEKING = 11;
    public static final int PLAYER_BITRATE_CHANGE = 12;
    public static final int PLAYER_ADDED = 13;
    public static final int PLAYER_DELETED = 14;
    public static final int PLAYER_SYNCED = 15;
    public static final int PLAYER_UNSYNCED = 16;


    /**
     * Creates a new instance of PlayerChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     */
    public PlayerChangeEvent(Object source, int id) {
        this(source, id, null);
    }

    /**
     * Creates a new instance of PlayerChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     * @param msg    an optional message
     */
    public PlayerChangeEvent(Object source, int id, String msg) {
        super(source);
        this.id = id;
        this.msg = msg;
    }

    /**
     * Returns specific id of the event that occurred.  The ids are public static
     * fields in the class.
     *
     * @return the specific id
     */
    public int getId() {
        return (id);
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
