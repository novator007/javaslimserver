package org.bff.squeezeserver.events;

/**
 * Represents a change in the status of a SqueezeServer music player.
 *
 * @author Bill Findeisen
 */
public class FavoriteChangeEvent extends java.util.EventObject {
    private int id;
    private String title;
    public static final int FOLDER_ADDED = 0;
    public static final int FOLDER_DELETED = 1;
    public static final int FOLDER_MOVED = 2;
    public static final int FOLDER_RENAMED = 3;
    public static final int FAVORITE_MOVED = 4;
    public static final int FAVORITE_ADDED = 5;
    public static final int FAVORITE_DELETED = 6;
    public static final int FAVORITE_ADDED_REMOTELY = 7;
    public static final int FAVORITE_REMOVED_REMOTELY = 8;
    public static final int FAVORITES_CLEARED = 9;

    /**
     * Creates a new instance of PlayerChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     */
    public FavoriteChangeEvent(Object source, int id) {
        this(source, id, null);
    }

    /**
     * Creates a new instance of PlayerChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     * @param msg    an optional message
     */
    public FavoriteChangeEvent(Object source, int id, String title) {
        super(source);
        this.id = id;
        this.title = title;
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
