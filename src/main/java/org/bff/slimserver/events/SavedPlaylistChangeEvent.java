/*
 * PlaylistChangeEvent.java
 *
 * Created on October 10, 2005, 8:25 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.slimserver.events;

import java.util.EventObject;
import org.bff.slimserver.musicobjects.SlimObject;
import org.bff.slimserver.musicobjects.SlimSavedPlaylist;

/**
 * Represents a change in the status of a Slim Server playlist.
 * @author Bill Findeisen
 */
public class SavedPlaylistChangeEvent extends EventObject {
    private static final long serialVersionUID = 20101010L;
    private int id;
    private transient SlimSavedPlaylist playlist;
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
    public static final int SAVED_PLAYLIST_CHANGED = 15;
    public static final int ALBUM_DELETED = 16;
    public static final int ARTIST_DELETED = 17;

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

    public static final int PLAYLIST_RENAMED = 25;

    public static final int ITEM_MOVED = 26;


    /**
     * 
     * Creates a new instance of PlayListChangeEvent
     * @param source the object on which the Event initially occurred
     * @param id the specific event that occurred
     * @param object the {@@link SlimObject} for the event
     */
    public SavedPlaylistChangeEvent(Object source, int id, SlimSavedPlaylist playlist) {
        super(source);
        this.id = id;
        this.playlist = playlist;

    }

    /**
     * Returns specific id of the event that occurred.  The ids are public static
     * fields in the class.
     * @return the specific id
     */
    public int getId() {
        return (id);
    }

    public SlimSavedPlaylist getNewPlaylist() {
        return playlist;
    }

    public void setNewPlaylist(SlimSavedPlaylist newPlaylist) {
        this.playlist = newPlaylist;
    }
}
