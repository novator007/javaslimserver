/*
 * SlimPlaylist.java
 *
 * Created on October 15, 2007, 10:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.events.PlaylistChangeEvent;
import org.bff.slimserver.events.PlaylistChangeListener;
import org.bff.slimserver.exception.SlimConnectionException;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.musicobjects.*;

import java.util.*;

/**
 * Represents the current playlist of the SlimServer
 *
 * @author Bill Findeisen
 */
public class SlimPlaylist {

    private SlimServer slimServer;
    private SlimDatabase database;
    private SlimPlayer player;
    private Properties prop;
    private static String SLIM_PROP_PLAYER_PLAYLIST_ADD;
    private static String SLIM_PROP_PLAYER_PLAYLIST_INSERT;
    private static String SLIM_PROP_PLAYER_PLAYLIST;
    private static String SLIM_PROP_PLAYER_PLAYLIST_REPLACE;
    public static String SLIM_PROP_PLAYER_PLAYLIST_SAVE;
    private static String SLIM_PROP_PLAYER_PLAYLIST_CLEAR;
    private static String SLIM_PROP_PLAYER_PLAYLIST_PLAY_NOW;
    private static String SLIM_PROP_PLAYER_PLAYLIST_CURRENT_ITEM;
    public static String SLIM_PROP_PLAYER_PLAYLIST_REPEAT;
    private static String SLIM_PROP_PLAYER_PLAYLIST_REPEAT_QUERY;
    public static String SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE;
    private static String SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE_QUERY;
    private static String SLIM_PROP_PLAYER_PLAYLIST_MOVE;
    private static String SLIM_PROP_PLAYER_PLAYLIST_COUNT;
    private static String SLIM_PROP_PLAYER_PLAYLIST_CURRENT_PATH;
    private static String SLIM_PROP_PLAYER_PLAYLIST_CURRENT_ARTIST;
    private static String SLIM_PROP_PLAYER_PLAYLIST_CURRENT_ALBUM;
    private static String SLIM_PROP_PLAYER_PLAYLIST_CURRENT_GENRE;
    private static String SLIM_PROP_PLAYER_PLAYLIST_CURRENT_TITLE;
    private static String SLIM_PROP_PLAYER_PLAYLIST_CURRENT_DURATION;
    private static String SLIM_PROP_PLAYER_PLAYLIST_CURRENT_REMOTE;
    private static String SLIM_PROP_PLAYER_PLAYLIST_CURRENT_REMOTE_TITLE;
    private static String SLIM_PROP_PLAYER_PLAYLIST_DELETE;
    private static String SLIM_PROP_PLAYLIST_CONTROL;
    private static String SLIM_PROP_PLAYER_PLAYLIST_RADIO_LOAD;
    private static String SLIM_PROP_PLAYER_PLAYLIST_RADIO_ADD;
    private static String SLIM_PROP_PLAYER_PLAYLIST_RADIO_INSERT;
    private static String SLIM_PROP_PLAYER_PLAYLIST_RADIO_PLAY;
    private static final String PARAM_PLAYER_ID = SlimConstants.CMD_PARAM_PLAYER_ID;
    /**
     * requesting tags
     */
    private static final String TAG_RETURN = "tags:";
    /**
     * command parameters
     */
    private static final String PARAM_START = SlimConstants.CMD_PARAM_START;
    private static final String PARAM_ITEMS = SlimConstants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = SlimConstants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PARAM_COMMAND = SlimConstants.CMD_PARAM_COMMAND;
    private static final String PARAM_RESULTS_MAX = Integer.toString(SlimConstants.RESULTS_MAX);
    private static final String PREFIX_PLAYLIST_INDEX = "playlist index:";
    private static final String PREFIX_PLAYLIST_COUNT = "playlist_tracks:";
    private static final String PREFIX_PLAYLIST_PATH = "__playlists/";
    private static final String PREFIX_CMD = "cmd:";
    private static final String PREFIX_CMD_ADD = "add";
    private static final String PREFIX_CMD_LOAD = "load";
    private static final String PREFIX_CMD_INSERT = "insert";
    private static final String PREFIX_CMD_DELETE = "delete";
    private static final String PREFIX_ARTIST_ID = "artist_id:";
    private static final String PREFIX_ALBUM_ID = "album_id:";
    private static final String PREFIX_GENRE_ID = "genre_id:";
    private static final String PREFIX_TRACK_ID = "track_id:";
    private static final String PREFIX_PLAYLIST_ID = "playlist_id:";
    private static final String PREFIX_FOLDER_ID = "folder_id:";
    private static final String PREFIX_YEAR_ID = "year_id:";
    private static final String PREFIX_ITEM_ID = "item_id:";
    private static final String PREFIX_PLAYLIST_TIMESTAMP = "playlist_timestamp:";
    private List<PlaylistChangeListener> playlistListeners;
    private boolean updating;
    private HashMap<String, SlimPlaylistItem> playlistMap;

    /**
     * Creates a new instance of SlimPlaylist
     *
     * @param player the {@link SlimPlayer} for this playlist
     */
    public SlimPlaylist(SlimPlayer player) {
        setPlayer(player);
        setSlimServer(player.getSlimServer());//current playlist
        setDatabase(new SlimDatabase(getSlimServer()));
        prop = getSlimServer().getSlimProperties();

        playlistMap = new HashMap<String, SlimPlaylistItem>();

        loadProperties();

        playlistListeners = new ArrayList<PlaylistChangeListener>();
    }

    /**
     * Adds a {@link PlaylistChangeListener} to this playlist
     *
     * @param listener
     */
    public void addPlaylistChangeListener(PlaylistChangeListener listener) {
        playlistListeners.add(listener);
    }

    /**
     * Removes a {@link PlaylistChangeListener} to this playlist
     *
     * @param listener
     */
    public void removePlaylistChangeListener(PlaylistChangeListener listener) {
        playlistListeners.remove(listener);
    }

    /**
     * Returns the {@link SlimPlayer} for this playlist
     *
     * @return the player for this playlist
     */
    public SlimPlayer getPlayer() {
        return player;
    }

    /**
     * Sets the {@link SlimPlayer} for this playlist
     *
     * @param player
     */
    public void setPlayer(SlimPlayer player) {
        this.player = player;
    }

    private SlimServer getSlimServer() {
        return slimServer;
    }

    private void setSlimServer(SlimServer slimServer) {
        this.slimServer = slimServer;
    }

    /**
     * Adds a {@code Collection} of {@link SlimPlayableObject}s to the end of the playlist.
     * Use this method when you only want one event fired for the entire
     * collection of objects.
     *
     * @param objects the {@code Collection} of {@link SlimPlayableObject}s to add
     * @throws org.bff.slimserver.exception.SlimException
     *
     */
    public void addItems(Collection<SlimPlayableObject> objects) throws SlimException {
        List<SlimPlaylistItem> playableObjects = new ArrayList<SlimPlaylistItem>();

        for (SlimPlayableObject o : objects) {
            playableObjects.add(new SlimPlaylistItem(o));
            controlPlaylistAdd(PREFIX_TRACK_ID + o.getId());
        }

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_ADDED, playableObjects));
    }

    /**
     * Adds a object to the end of the playlist.
     *
     * @param object the {@link SlimPlayableObject} to add
     * @throws org.bff.slimserver.exception.SlimException
     *
     */
    public void addItem(SlimPlayableObject object) throws SlimException {
        controlPlaylistAdd(PREFIX_TRACK_ID + object.getId());
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_ADDED, object));
    }

    public void addFolderItems(Collection<SlimFolderObject> objects) throws SlimException {
        for (SlimFolderObject o : objects) {
            addFolderItem(o);
        }
    }

    public void addFolderItem(SlimFolderObject object) throws SlimException {
        if (object.getObjectType().equals(SlimFolderObject.OBJECTTYPE.TRACK)) {
            controlPlaylistAdd(PREFIX_TRACK_ID + object.getId());
        } else if (object.getObjectType().equals(SlimFolderObject.OBJECTTYPE.PLAYLIST)) {
            controlPlaylistAdd(PREFIX_PLAYLIST_ID + object.getId());
        } else {
            controlPlaylistAdd(PREFIX_FOLDER_ID + object.getId());
        }
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_ADDED, object));
    }

    public void addPluginItem(SlimPluginObject object) throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_RADIO_ADD;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_COMMAND, object.getCommand());
        cmd = cmd.replaceFirst("<item>", PREFIX_ITEM_ID + encode(object.getItemId()));

        getSlimServer().sendCommand(new SlimCommand(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_ADDED, object));
    }

    public void addPluginItems(Collection<SlimPluginObject> objects) throws SlimException {

        List<SlimPlaylistItem> playableObjects = new ArrayList<SlimPlaylistItem>();

        for (SlimPluginObject o : objects) {
            String cmd = SLIM_PROP_PLAYER_PLAYLIST_RADIO_ADD;
            cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
            cmd = cmd.replaceFirst(PARAM_COMMAND, o.getCommand());
            cmd = cmd.replaceFirst("<item>", PREFIX_ITEM_ID + encode(o.getItemId()));

            getSlimServer().sendCommand(new SlimCommand(cmd));

            playableObjects.add(new SlimPlaylistItem(o));
        }

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_ADDED, playableObjects));
    }

    /**
     * Clears the playlist and plays the item.
     *
     * @param plugin
     * @param object
     * @throws SlimException
     */
    public void loadPluginItem(SlimPluginObject object) throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_RADIO_LOAD;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_COMMAND, object.getCommand());
        cmd = cmd.replaceFirst("<item>", PREFIX_ITEM_ID + encode(object.getItemId()));

        getSlimServer().sendCommand(new SlimCommand(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_LOADED, object));
    }

    public void insertPluginItem(SlimPluginObject object) throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_RADIO_INSERT;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_COMMAND, object.getCommand());
        cmd = cmd.replaceFirst("<item>", PREFIX_ITEM_ID + encode(object.getItemId()));

        getSlimServer().sendCommand(new SlimCommand(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_INSERTED, object));
    }

    public void playPluginItem(SlimPluginObject object) throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_RADIO_PLAY;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_COMMAND, object.getCommand());
        cmd = cmd.replaceFirst("<item>", PREFIX_ITEM_ID + encode(object.getItemId()));

        getSlimServer().sendCommand(new SlimCommand(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_LOADED, object));
    }

    /**
     * Clears playlist and plays object
     *
     * @param object
     * @throws org.bff.slimserver.exception.SlimException
     *
     */
    public void playItemClearPlaylist(SlimPlayableObject object) throws SlimException {
        controlPlaylistLoad(PREFIX_TRACK_ID + object.getId());

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.PLAYLIST_CLEARED, object));
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_LOADED, object));
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_CHANGED, object));
    }

    /**
     * Inserts the {@link Collection} of {@link SlimPlayableObject}s after the currently
     * playlist object
     *
     * @param object
     * @throws org.bff.slimserver.exception.SlimException
     *
     */
    public void insertItems(Collection<SlimPlayableObject> objects) throws SlimException {
        List<SlimPlaylistItem> playableObjects = new ArrayList<SlimPlaylistItem>();

        for (SlimPlayableObject o : objects) {
            playableObjects.add(new SlimPlaylistItem(o));
            controlPlaylistInsert(PREFIX_TRACK_ID + o.getId());
        }

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_INSERTED, playableObjects));
    }

    /**
     * Inserts the {@link SlimPlayableObject} after the currently playling object
     *
     * @param object
     * @throws org.bff.slimserver.exception.SlimException
     *
     */
    public void insertItem(SlimPlayableObject object) throws SlimException {
        controlPlaylistInsert(PREFIX_TRACK_ID + object.getId());
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_INSERTED, object));
    }

    public void insertFolderItems(Collection<SlimFolderObject> objects) throws SlimException {

        for (SlimFolderObject o : objects) {
            insertFolderItem(o);
        }
    }

    public void loadFolderItems(Collection<SlimFolderObject> objects) throws SlimException {
        for (SlimFolderObject o : objects) {
            loadFolderItem(o);
        }
    }

    public void loadFolderItem(SlimFolderObject object) throws SlimException {
        if (object.getObjectType().equals(SlimFolderObject.OBJECTTYPE.TRACK)) {
            controlPlaylistLoad(PREFIX_TRACK_ID + object.getId());
        } else if (object.getObjectType().equals(SlimFolderObject.OBJECTTYPE.PLAYLIST)) {
            controlPlaylistLoad(PREFIX_PLAYLIST_ID + object.getId());
        } else {
            controlPlaylistLoad(PREFIX_FOLDER_ID + object.getId());
        }
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_LOADED, object));
    }

    /**
     * Inserts the {@link SlimPlayableObject} after the currently playling object
     *
     * @param object
     * @throws org.bff.slimserver.exception.SlimException
     *
     */
    public void insertFolderItem(SlimFolderObject object) throws SlimException {
        if (object.getObjectType().equals(SlimFolderObject.OBJECTTYPE.TRACK)) {
            controlPlaylistInsert(PREFIX_TRACK_ID + object.getId());
        } else if (object.getObjectType().equals(SlimFolderObject.OBJECTTYPE.PLAYLIST)) {
            controlPlaylistInsert(PREFIX_PLAYLIST_ID + object.getId());
        } else {
            controlPlaylistInsert(PREFIX_FOLDER_ID + object.getId());
        }
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_INSERTED, object));
    }

    public void removeItem(SlimPlaylistItem object) throws SlimException {
        //modified this for remote items
        deleteByPlaylistIndex(object);
        //controlPlaylistDelete(PREFIX_TRACK_ID + object.getPlaylistId());
        playlistMap.remove(object.getId());
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_DELETED, object.getPlayableObject()));
    }

    public void replacePlaylist(SlimSavedPlaylist playlist) throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_REPLACE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<playlist>", encode(playlist.getUrl()));

        getSlimServer().sendCommand(new SlimCommand(cmd));

        playlistMap.clear();

        firePlaylistEvent(new PlaylistChangeEvent(this,
                PlaylistChangeEvent.PLAYLIST_REPLACED,
                new ArrayList<SlimPlaylistItem>(playlist.getItems())));
    }

    public void addPlaylist(SlimSavedPlaylist playlist) throws SlimException {
        setUpdating(true);
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_ADD;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<item>", encode(playlist.getUrl()));

        getSlimServer().sendCommand(new SlimCommand(cmd));
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this,
                PlaylistChangeEvent.PLAYLIST_ADDED,
                new ArrayList<SlimPlaylistItem>(playlist.getItems())));
    }

    public void insertPlaylist(SlimSavedPlaylist playlist) throws SlimException {
        setUpdating(true);
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_INSERT;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<item>", encode(playlist.getUrl()));

        getSlimServer().sendCommand(new SlimCommand(cmd));
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this,
                PlaylistChangeEvent.PLAYLIST_ADDED,
                new ArrayList<SlimPlaylistItem>(playlist.getItems())));
    }

    public void savePlaylist(String playlistName) throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_SAVE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<filename>", encode(playlistName));

        getSlimServer().sendCommand(new SlimCommand(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.PLAYLIST_SAVED, (SlimPlayableObject) null));
    }

    public void addAlbums(Collection<SlimAlbum> albums) throws SlimException {
        for (SlimAlbum album : albums) {
            addAlbum(album);
        }
    }

    public void loadAlbums(Collection<SlimAlbum> albums) throws SlimException {
        clear();
        addAlbums(albums);
    }

    /**
     * Adds the album to the playlist
     *
     * @param album album to add
     */
    public void addAlbum(SlimAlbum album) throws SlimException {
        setUpdating(true);
        controlPlaylistAdd(PREFIX_ALBUM_ID + album.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ALBUM_ADDED, album));
    }

    /**
     * Replaces the current playlist with the album
     *
     * @param album to load
     */
    public void loadAlbum(SlimAlbum album) throws SlimException {
        setUpdating(true);
        controlPlaylistLoad(PREFIX_ALBUM_ID + album.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ALBUM_ADDED, album));
    }

    /**
     * Plays the album next.
     *
     * @param album to play next
     */
    public void insertAlbum(SlimAlbum album) throws SlimException {
        setUpdating(true);
        controlPlaylistInsert(PREFIX_ALBUM_ID + album.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ALBUM_ADDED, album));
    }

    /**
     * Removes the album to the playlist
     *
     * @param album album to remove
     * @throws SlimException
     */
    public void removeAlbum(SlimAlbum album) throws SlimException {
        setUpdating(true);
        controlPlaylistDelete(PREFIX_ALBUM_ID + album.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ALBUM_DELETED, album));
    }

    /**
     * Removes the album to the playlist
     *
     * @param artist artist to remove
     * @throws SlimException
     */
    public void removeArtists(SlimAlbum album) throws SlimException {
        setUpdating(true);
        List<SlimArtist> artists = new ArrayList<SlimArtist>(getDatabase().getArtistsForAlbum(album));
        for (SlimArtist artist : artists) {
            controlPlaylistDelete(PREFIX_ARTIST_ID + artist.getId());
        }
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ARTIST_DELETED, artists.get(0)));
    }

    /**
     * Removes the album to the playlist
     *
     * @param artist artist to remove
     * @throws SlimException
     */
    public void removeArtist(SlimArtist artist) throws SlimException {
        setUpdating(true);
        controlPlaylistDelete(PREFIX_ARTIST_ID + artist.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ARTIST_DELETED, artist));
    }

    /**
     * Plays the item in the playlist and sets as the current item.
     * @param item
     * @throws org.bff.slimserver.exception.SlimException
     */
    /**
     * Adds the album to the playlist
     *
     * @param artist artist to add
     */
    public void addArtist(SlimArtist artist) throws SlimException {
        setUpdating(true);
        controlPlaylistAdd(PREFIX_ARTIST_ID + artist.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ARTIST_ADDED, artist));
    }

    public void addArtist(SlimAlbum album) throws SlimException {
        setUpdating(true);
        List<SlimArtist> artists = new ArrayList<SlimArtist>(getDatabase().getArtistsForAlbum(album));
        for (SlimArtist artist : artists) {
            controlPlaylistAdd(PREFIX_ARTIST_ID + artist.getId());
        }
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ARTIST_ADDED, artists.get(0)));
    }

    /**
     * Replaces the current playlist with the album
     *
     * @param artist artist to load
     */
    public void loadArtist(SlimArtist artist) throws SlimException {
        setUpdating(true);
        controlPlaylistLoad(PREFIX_ARTIST_ID + artist.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ARTIST_ADDED, artist));
    }

    /**
     * Plays the album next.
     *
     * @param artist artist to insert
     */
    public void insertArtist(SlimArtist artist) throws SlimException {
        setUpdating(true);
        controlPlaylistInsert(PREFIX_ARTIST_ID + artist.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ARTIST_ADDED, artist));
    }

    public void addYear(String year) throws SlimException {
        setUpdating(true);
        controlPlaylistAdd(PREFIX_YEAR_ID + year);
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.YEAR_ADDED, null, year));
    }

    public void loadYear(String year) throws SlimException {
        setUpdating(true);
        controlPlaylistLoad(PREFIX_YEAR_ID + year);
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.YEAR_ADDED, null, year));
    }

    public void insertYear(String year) throws SlimException {
        setUpdating(true);
        controlPlaylistInsert(PREFIX_YEAR_ID + year);
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.YEAR_ADDED, null, year));
    }

    public void deleteYear(String year) throws SlimException {
        setUpdating(true);
        controlPlaylistDelete(PREFIX_YEAR_ID + year);
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.YEAR_DELETED, null, year));
    }

    public void addGenre(SlimGenre genre) throws SlimException {
        setUpdating(true);
        controlPlaylistAdd(PREFIX_GENRE_ID + genre.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.GENRE_ADDED, genre));
    }

    public void loadGenre(SlimGenre genre) throws SlimException {
        setUpdating(true);
        controlPlaylistLoad(PREFIX_GENRE_ID + genre.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.GENRE_ADDED, genre));
    }

    public void insertGenre(SlimGenre genre) throws SlimException {
        setUpdating(true);
        controlPlaylistInsert(PREFIX_GENRE_ID + genre.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.GENRE_ADDED, genre));
    }

    public void deleteGenre(SlimGenre genre) throws SlimException {
        setUpdating(true);
        controlPlaylistDelete(PREFIX_GENRE_ID + genre.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.GENRE_DELETED, genre));
    }

    /**
     * Plays the item in the playlist and sets as the current item.
     *
     * @param item item to play
     * @throws org.bff.slimserver.exception.SlimException
     *
     */
    public void playItem(SlimPlaylistItem object) throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_PLAY_NOW;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<itemindex>", Integer.toString(object.getPlaylistIndex()));

        getSlimServer().sendCommand(new SlimCommand(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_CHANGED, object.getPlayableObject()));
    }

    /**
     * Plays the item in the playlist and sets as the current item.
     *
     * @param playlistIndex item to play
     * @throws org.bff.slimserver.exception.SlimException
     *
     */
    public void playItem(int playlistIndex) throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_PLAY_NOW;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<itemindex>", Integer.toString(playlistIndex));

        getSlimServer().sendCommand(new SlimCommand(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_CHANGED, (SlimPlayableObject) null));
    }

    public int getCurrentIndex() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_ITEM;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        return Integer.parseInt(response[0]);
    }

    public SlimPlaylistItem getRemoteItem(String index) throws SlimException {
        SlimPlaylistItem item = playlistMap.get(index);

        if (item == null) {
            getItems();
        }

        return playlistMap.get(index);
    }

    public String getCurrentImageURL() {
        return "http://" + getSlimServer().getServer() + ":" + getSlimServer().getServerPort()
                + "/music/current/cover.jpg?player=" + getPlayer().getId();
    }

    /**
     * Returns the current {@link SlimPlaylistItem} of the currently playing
     * item.  This method will return null for remote streams and empty playlists.  You may want to
     * check the {@link #isCurrentRemote()} or the {@link #getItemCount()} method before calling.
     *
     * @return
     * @throws SlimException
     */
    public SlimPlaylistItem getCurrentItem() throws SlimException {
        SlimPlaylistItem item = null;

        if (!isCurrentRemote()) {
            SlimPlayableItem s = getDatabase().lookupItemByUrl(getCurrentItemPath());

            if (s != null) {
                item = new SlimPlaylistItem(s);
                item.setPlaylistIndex(getCurrentIndex());
            }
        } else {
            item = getCurrentRemoteItem();
        }
        return item;
    }

    public SlimPlaylistItem getCurrentRemoteItem() throws SlimConnectionException {
        SlimPlaylistItem retItem = null;

        String command = SLIM_PROP_PLAYER_PLAYLIST.replaceAll(PARAM_START, "-");
        command = command.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        command = command.replaceAll(PARAM_ITEMS, "1");
        command = command.replaceAll(PARAM_TAGS, TAG_RETURN + SlimPlayableTags.RETURN_TAGS);

        String[] response = getSlimServer().sendCommand(command);

        for (int i = 0; i < response.length - 1; ) {
            if (response[i].startsWith(PREFIX_PLAYLIST_INDEX)) {
                int playlistIndex = Integer.parseInt(response[i].replace(PREFIX_PLAYLIST_INDEX, ""));

                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;
                String[] itemArray = new String[tagCount];
                int j = 0;
                while (j < tagCount) {
                    itemArray[j++] = response[i++];
                    if (response[i].startsWith(PREFIX_PLAYLIST_INDEX) || i > response.length - 2) {
                        itemArray[j] = response[i];
                        break;
                    }
                }
                SlimPlayableItem item = getSlimServer().convertResponse(itemArray);
                retItem = new SlimPlaylistItem(item);
                retItem.setPlaylistIndex(playlistIndex);

            } else {
                ++i;
            }
        }

        return retItem;
    }

    public String getCurrentItemPath() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_PATH;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        if (response == null || response[0] == null) {
            return null;
        }

        return response[0].trim();
    }

    public void repeatPlaylist() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_REPEAT;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSlimServer().sendCommand(new SlimCommand(cmd, "2"));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.REPEAT_PLAYLIST, (SlimPlayableObject) null));
    }

    public void repeatItem() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_REPEAT;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSlimServer().sendCommand(new SlimCommand(cmd, "1"));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.REPEAT_ITEM, (SlimPlayableObject) null));
    }

    public void repeatOff() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_REPEAT;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSlimServer().sendCommand(new SlimCommand(cmd, "0"));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.REPEAT_OFF, (SlimPlayableObject) null));
    }

    public boolean isRepeatingItem() throws SlimException {
        return getRepeatingStatus() == 1;
    }

    public boolean isRepeatingPlaylist() throws SlimException {
        return getRepeatingStatus() == 2;
    }

    public boolean isRepeatingOff() throws SlimConnectionException {
        return getRepeatingStatus() == 0;
    }

    public int getRepeatingStatus() throws SlimConnectionException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_REPEAT_QUERY;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        return Integer.parseInt(response[0]);
    }

    public void shuffleItems() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSlimServer().sendCommand(new SlimCommand(cmd, "1"));
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.PLAYLIST_CHANGED, (SlimPlayableObject) null));
    }

    public void shuffleAlbums() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSlimServer().sendCommand(new SlimCommand(cmd, "2"));
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.PLAYLIST_CHANGED, (SlimPlayableObject) null));
    }

    public void shuffleOff() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSlimServer().sendCommand(new SlimCommand(cmd, "0"));
    }

    public boolean isShuffleItem() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE_QUERY;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        return Integer.parseInt(response[0]) == 1;
    }

    public boolean isShuffleAlbum() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE_QUERY;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        return Integer.parseInt(response[0]) == 2;
    }

    public void clear() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CLEAR;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSlimServer().sendCommand(new SlimCommand(cmd));
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.PLAYLIST_CLEARED, (SlimPlayableObject) null));
    }

    /**
     * Returns playlist items with just playlist and item indexes populated.
     *
     * @return the collection of item ids
     */
    public Collection<SlimPlaylistItem> getItemIds() throws SlimException {
        List<SlimPlaylistItem> retList = new ArrayList<SlimPlaylistItem>();
        String command = SLIM_PROP_PLAYER_PLAYLIST.replaceAll(PARAM_START, "0");
        command = command.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, TAG_RETURN + "");

        String[] response = getSlimServer().sendCommand(command);
        for (int i = 0; i < response.length - 1; ) {
            if (response[i].startsWith(PREFIX_PLAYLIST_INDEX)) {
                int playlistIndex = Integer.parseInt(response[i].replace(PREFIX_PLAYLIST_INDEX, ""));

                int tagCount = 2;
                String[] items = new String[tagCount];
                for (int j = 0; j < tagCount; j++) {
                    items[j] = response[i++];
                }

                SlimPlayableItem item = getSlimServer().convertResponse(items);
                SlimPlaylistItem playlistItem = new SlimPlaylistItem(item);
                playlistItem.setPlaylistIndex(playlistIndex);
                retList.add(playlistItem);
            } else {
                ++i;
            }
        }

        return retList;
    }

    public String getCurrentPath() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_PATH;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        return response[0];
    }

    public String getCurrentGenre() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_GENRE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        return "0".equalsIgnoreCase(response[0]) ? null : response[0];
    }

    public String getCurrentArtist() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_ARTIST;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        return "0".equalsIgnoreCase(response[0]) ? null : response[0];
    }

    public String getCurrentAlbum() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_ALBUM;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        return "0".equalsIgnoreCase(response[0]) ? null : response[0];
    }

    public String getCurrentTitle() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_TITLE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        return "0".equalsIgnoreCase(response[0]) ? null : response[0];
    }

    public double getCurrentDuration() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_DURATION;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        return Double.parseDouble("0".equalsIgnoreCase(response[0]) ? "0" : response[0]);
    }

    public boolean isCurrentRemote() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_REMOTE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        if (response != null) {

            try {
                return Integer.parseInt(response[0]) == 1 ? true : false;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    public String getCurrentRemoteTitle() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_REMOTE_TITLE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        return "0".equalsIgnoreCase(response[0]) ? null : response[0];
    }

    public int getItemCount() throws SlimException {

        String cmd = SLIM_PROP_PLAYER_PLAYLIST_COUNT;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSlimServer().sendCommand(new SlimCommand(cmd));

        return Integer.parseInt(response[0]);
    }

    public String getPlaylistTimeStamp() throws SlimConnectionException {
        String retString = null;
        String command = SLIM_PROP_PLAYER_PLAYLIST.replaceAll(PARAM_START, "0");
        command = command.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        command = command.replaceAll(PARAM_ITEMS, "0");
        command = command.replaceAll(PARAM_TAGS, "");

        String[] response = getSlimServer().sendCommand(command);

        for (int i = 0; i < response.length - 1; ) {
            if (response[i].startsWith(PREFIX_PLAYLIST_TIMESTAMP)) {
                retString = response[i].replace(PREFIX_PLAYLIST_TIMESTAMP, "");
                break;
            } else {
                ++i;
            }
        }

        return retString;
    }

    public Collection<SlimPlaylistItem> getItems() throws SlimException {
        List<SlimPlaylistItem> retList = new ArrayList<SlimPlaylistItem>();

        playlistMap.clear();

        String command = SLIM_PROP_PLAYER_PLAYLIST.replaceAll(PARAM_START, "0");
        command = command.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, TAG_RETURN + SlimPlayableTags.RETURN_TAGS);

        String[] response = getSlimServer().sendCommand(command);

        for (int i = 0; i < response.length - 1; ) {
            if (response[i].startsWith(PREFIX_PLAYLIST_INDEX)) {
                int playlistIndex = Integer.parseInt(response[i].replace(PREFIX_PLAYLIST_INDEX, ""));

                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;
                String[] itemArray = new String[tagCount];
                int j = 0;
                while (j < tagCount) {
                    itemArray[j++] = response[i++];
                    if (response[i].startsWith(PREFIX_PLAYLIST_INDEX) || i > response.length - 2) {
                        //back up the indexes one item
                        --j;
                        --i;
                        itemArray[j] = response[i];
                        break;
                    }
                }
                SlimPlayableItem item = getSlimServer().convertResponse(itemArray);
                SlimPlaylistItem playlistItem = new SlimPlaylistItem(item);
                playlistItem.setPlaylistIndex(playlistIndex);
                retList.add(playlistItem);
                playlistMap.put(item.getId(), playlistItem);
            } else {
                ++i;
            }
        }

        return retList;
    }

    public void playPrevious() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_PLAY_NOW;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<itemindex>", "-1");

        getSlimServer().sendCommand(new SlimCommand(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_CHANGED, (SlimPlayableObject) null));
    }

    public void playNext() throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_PLAY_NOW;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<itemindex>", "+1");

        getSlimServer().sendCommand(new SlimCommand(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_CHANGED, (SlimPlayableObject) null));
    }

    public synchronized void swap(SlimPlaylistItem item1, SlimPlaylistItem item2) throws SlimException {
        int index1 = item1.getPlaylistIndex();
        int index2 = item2.getPlaylistIndex();

        if (index1 == index2) {
            return;
        }

        if (index1 < index2) {
            move(index1, index2);
            move(index2 - 1, index1);
        } else {
            move(index1, index2);
            move(index2 + 1, index1);
        }

        item2.setPlaylistIndex(index1);
        item1.setPlaylistIndex(index2);
    }

    /**
     * Moves the {@link SlimPlaylistObject} to the desired index.
     *
     * @param item    the item to move
     * @param toIndex where to move the item
     * @throws org.bff.slimserver.exception.SlimException
     *          if there is a problem
     *          moving the item
     */
    public void move(SlimPlaylistItem object, int toIndex) throws SlimException {
        move(object.getPlaylistIndex(), toIndex);
    }

    public void move(int fromIndex, int toIndex) throws SlimException {
        if (fromIndex == toIndex) {
            return;
        }

        String command = SLIM_PROP_PLAYER_PLAYLIST_MOVE;
        command = command.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        command = command.replaceFirst("<fromindex>", Integer.toString(fromIndex));
        command = command.replaceFirst("<toindex>", Integer.toString(toIndex));

        getSlimServer().sendCommand(new SlimCommand(command));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.PLAYLIST_CHANGED, (SlimPlayableObject) null));
    }

    private void controlPlaylistAdd(String command) throws SlimException {
        String cmd = SLIM_PROP_PLAYLIST_CONTROL;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_TAGS, PREFIX_CMD + PREFIX_CMD_ADD + " " + command);

        getSlimServer().sendCommand(new SlimCommand(cmd));
    }

    private void controlPlaylistInsert(String command) throws SlimException {
        String cmd = SLIM_PROP_PLAYLIST_CONTROL;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_TAGS, PREFIX_CMD + PREFIX_CMD_INSERT + " " + command);

        getSlimServer().sendCommand(new SlimCommand(cmd));
    }

    private void controlPlaylistLoad(String command) throws SlimException {
        String cmd = SLIM_PROP_PLAYLIST_CONTROL;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_TAGS, PREFIX_CMD + PREFIX_CMD_LOAD + " " + command);

        getSlimServer().sendCommand(new SlimCommand(cmd));
    }

    private void controlPlaylistDelete(String command) throws SlimException {
        String cmd = SLIM_PROP_PLAYLIST_CONTROL;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_TAGS, PREFIX_CMD + PREFIX_CMD_DELETE + " " + command.replace("-", ""));

        getSlimServer().sendCommand(new SlimCommand(cmd));
    }

    private void deleteByPlaylistIndex(SlimPlaylistItem object) throws SlimException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_DELETE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<itemindex>", Integer.toString(object.getPlaylistIndex()));

        getSlimServer().sendCommand(new SlimCommand(cmd));
    }

    private void loadProperties() {
        SLIM_PROP_PLAYER_PLAYLIST = prop.getProperty("SS_PLAYLIST_STATUS");
        SLIM_PROP_PLAYER_PLAYLIST_REPLACE = prop.getProperty("SS_PLAYLIST_REPLACE");
        SLIM_PROP_PLAYER_PLAYLIST_SAVE = prop.getProperty("SS_PLAYLIST_SAVE");
        SLIM_PROP_PLAYER_PLAYLIST_CLEAR = prop.getProperty("SS_PLAYLIST_CLEAR");
        SLIM_PROP_PLAYER_PLAYLIST_CURRENT_ITEM = prop.getProperty("SS_PLAYLIST_CURRENT_ITEM");
        SLIM_PROP_PLAYER_PLAYLIST_PLAY_NOW = prop.getProperty("SS_PLAYLIST_PLAY_NOW");
        SLIM_PROP_PLAYER_PLAYLIST_REPEAT = prop.getProperty("SS_PLAYLIST_REPEAT");
        SLIM_PROP_PLAYER_PLAYLIST_REPEAT_QUERY = prop.getProperty("SS_PLAYLIST_REPEAT_QUERY");
        SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE = prop.getProperty("SS_PLAYLIST_SHUFFLE");
        SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE_QUERY = prop.getProperty("SS_PLAYLIST_SHUFFLE_QUERY");
        SLIM_PROP_PLAYER_PLAYLIST_MOVE = prop.getProperty("SS_PLAYLIST_MOVE");
        SLIM_PROP_PLAYER_PLAYLIST_COUNT = prop.getProperty("SS_PLAYLIST_TRACKS_COUNT");
        SLIM_PROP_PLAYER_PLAYLIST_CURRENT_PATH = prop.getProperty("SS_PLAYLIST_CURRENT_PATH");
        SLIM_PROP_PLAYER_PLAYLIST_CURRENT_GENRE = prop.getProperty("SS_PLAYLIST_CURRENT_GENRE");
        SLIM_PROP_PLAYER_PLAYLIST_CURRENT_ARTIST = prop.getProperty("SS_PLAYLIST_CURRENT_ARTIST");
        SLIM_PROP_PLAYER_PLAYLIST_CURRENT_ALBUM = prop.getProperty("SS_PLAYLIST_CURRENT_ALBUM");
        SLIM_PROP_PLAYER_PLAYLIST_CURRENT_TITLE = prop.getProperty("SS_PLAYLIST_CURRENT_TITLE");
        SLIM_PROP_PLAYER_PLAYLIST_CURRENT_DURATION = prop.getProperty("SS_PLAYLIST_CURRENT_DURATION");
        SLIM_PROP_PLAYER_PLAYLIST_CURRENT_REMOTE = prop.getProperty("SS_PLAYLIST_CURRENT_REMOTE");
        SLIM_PROP_PLAYER_PLAYLIST_CURRENT_REMOTE_TITLE = prop.getProperty("SS_PLAYLIST_CURRENT_REMOTE_TITLE");
        SLIM_PROP_PLAYER_PLAYLIST_DELETE = prop.getProperty("SS_PLAYLIST_REMOVE_ITEM");
        SLIM_PROP_PLAYLIST_CONTROL = prop.getProperty("SS_PLAYLIST_CONTROL");
        SLIM_PROP_PLAYER_PLAYLIST_ADD = prop.getProperty("SS_PLAYLIST_ADD_PLAYLIST");
        SLIM_PROP_PLAYER_PLAYLIST_INSERT = prop.getProperty("SS_PLAYLIST_INSERT_PLAYLIST");
        SLIM_PROP_PLAYER_PLAYLIST_RADIO_LOAD = prop.getProperty("SS_PLAYLIST_RADIO_LOAD");
        SLIM_PROP_PLAYER_PLAYLIST_RADIO_ADD = prop.getProperty("SS_PLAYLIST_RADIO_ADD");
        SLIM_PROP_PLAYER_PLAYLIST_RADIO_INSERT = prop.getProperty("SS_PLAYLIST_RADIO_INSERT");
        SLIM_PROP_PLAYER_PLAYLIST_RADIO_PLAY = prop.getProperty("SS_PLAYLIST_RADIO_PLAY");
    }

    private void firePlaylistEvent(PlaylistChangeEvent event) {
        for (PlaylistChangeListener listener : playlistListeners) {
            listener.playlistChanged(event);
        }
    }

    /**
     * @return the database
     */
    public SlimDatabase getDatabase() {
        return database;
    }

    /**
     * @param database the database to set
     */
    public void setDatabase(SlimDatabase database) {
        this.database = database;
    }

    /**
     * Returns true for long running operations such as removing artists or
     * albums
     *
     * @return the updating
     */
    public boolean isUpdating() {
        return updating;
    }

    /**
     * @param updating the updating to set
     */
    private void setUpdating(boolean updating) {
        this.updating = updating;
    }

    private String encode(String criteria) {
        return Utils.encode(criteria, getSlimServer().getEncoding());
    }
}
