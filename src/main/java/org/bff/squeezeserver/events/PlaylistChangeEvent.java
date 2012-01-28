/*
 * PlaylistChangeEvent.java
 *
 * Created on October 10, 2005, 8:25 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.squeezeserver.events;

import org.bff.squeezeserver.domain.Playable;
import org.bff.squeezeserver.domain.PlaylistItem;

import java.util.Collection;
import java.util.EventObject;
import java.util.List;

/**
 * Represents a change in the status of a Slim Server playlist.
 *
 * @author Bill Findeisen
 */
public class PlaylistChangeEvent extends EventObject {

    private int id;
    private Playable object;
    private Collection<PlaylistItem> newPlaylist;
    private int itemCount;
    private String message;
    /**
     * a item was added
     */
    public static final int ITEM_ADDED = 1;
    /**
     * a item was deleted
     */
    public static final int ITEM_DELETED = 2;
    /**
     * a specific item was selected
     */
    public static final int ITEM_SELECTED = 3;
    /**
     * a playlist was appended to this playlist
     */
    public static final int PLAYLIST_ADDED = 4;
    /**
     * the playlist version was changed
     */
    public static final int PLAYLIST_CHANGED = 5;
    /**
     * the playlist was deleted
     */
    public static final int PLAYLIST_DELETED = 6;
    /**
     * the playlist was loaded
     */
    public static final int PLAYLIST_LOADED = 7;
    /**
     * the playlist was changed
     */
    public static final int PLAYLIST_SAVED = 8;
    /**
     * the playlist was cleared
     */
    public static final int PLAYLIST_CLEARED = 9;
    /**
     * the playlist was cleared
     */
    public static final int PLAYLIST_ENDED = 10;
    /**
     * the playlist was cleared
     */
    public static final int ITEM_CHANGED = 11;
    /**
     * album added to the playlist
     */
    public static final int ALBUM_ADDED = 12;
    /**
     * artist added to the playlist
     */
    public static final int ARTIST_ADDED = 13;
    public static final int PLAYLIST_REPLACED = 14;
    public static final int ALBUM_DELETED = 16;
    public static final int ARTIST_DELETED = 17;
    public static final int REPEAT_PLAYLIST = 18;
    public static final int REPEAT_ITEM = 19;
    public static final int REPEAT_OFF = 20;
    /**
     * genre added to the playlist
     */
    public static final int GENRE_ADDED = 21;
    /**
     * year added to the playlist
     */
    public static final int YEAR_ADDED = 22;
    /**
     * genre added to the playlist
     */
    public static final int GENRE_DELETED = 23;
    /**
     * year added to the playlist
     */
    public static final int YEAR_DELETED = 24;
    /**
     * item was inserted into the playlist
     */
    public static final int ITEM_INSERTED = 25;
    /**
     * item was loaded and playlist cleared
     */
    public static final int ITEM_LOADED = 26;
    /**
     * items were shuffled
     */
    public static final int SHUFFLE_ITEMS = 27;
    /**
     * shuffle turned off
     */
    public static final int SHUFFLE_OFF = 28;
    /**
     * Albums were shuffled
     */
    public static final int SHUFFLE_ALBUMS = 29;

    private Collection<Playable> objects;

    /**
     * Creates a new instance of PlayListChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     * @param object the {@@link SlimObject} for the event
     */
    public PlaylistChangeEvent(Object source, int id, Playable object) {
        this(source, id, object, null, null);
    }

    public PlaylistChangeEvent(Object source, int id, Playable object, String message) {
        this(source, id, object, null, message);
    }

    public PlaylistChangeEvent(Object source, int id, Collection<PlaylistItem> playlist) {
        this(source, id, null, playlist, null);
    }

    /*
    public PlaylistChangeEvent(Object source, int id, Collection<Playable> playlist) {
    this(source, id, null, null, playlist, null);
    }
     */

    /**
     * @param source
     * @param id
     * @param object   the {@@link SlimObject} for the event
     * @param objects
     * @param playlist
     * @param message
     */
    public PlaylistChangeEvent(Object source,
                               int id,
                               Playable object,
                               Collection<PlaylistItem> playlist,
                               String message) {
        super(source);
        this.id = id;
        this.object = object;
        this.newPlaylist = playlist;
        this.message = message;
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

    public Collection<PlaylistItem> getNewPlaylist() {
        return newPlaylist;
    }

    public void setNewPlaylist(List<PlaylistItem> newPlaylist) {
        this.newPlaylist = newPlaylist;
    }

    /**
     * Returns the {@@link SlimObject} that generated the event
     *
     * @return the slimobject
     */
    public Playable getObject() {
        return object;
    }

    /**
     * Sets the {@@link SlimObject} that generated the event
     *
     * @param slimobject the slimobject to set
     */
    public void setObject(Playable slimobject) {
        this.object = slimobject;
    }

    /**
     * @return the objects
     */
    public Collection<Playable> getObjects() {
        return objects;
    }

    /**
     * @param objects the objects to set
     */
    public void setObjects(Collection<Playable> objects) {
        this.objects = objects;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
