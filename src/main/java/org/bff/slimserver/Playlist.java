/*
 * SlimPlaylist.java
 *
 * Created on October 15, 2007, 10:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.domain.*;
import org.bff.slimserver.events.PlaylistChangeEvent;
import org.bff.slimserver.events.PlaylistChangeListener;
import org.bff.slimserver.exception.ConnectionException;
import org.bff.slimserver.exception.SqueezeException;

import java.util.*;

/**
 * Represents the current playlist of the SqueezeServer
 *
 * @author Bill Findeisen
 */
public class Playlist {

    private SqueezeServer squeezeServer;
    private Database database;
    private Player player;
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
    private static final String PARAM_PLAYER_ID = Constants.CMD_PARAM_PLAYER_ID;
    /**
     * requesting tags
     */
    private static final String TAG_RETURN = "tags:";
    /**
     * command parameters
     */
    private static final String PARAM_START = Constants.CMD_PARAM_START;
    private static final String PARAM_ITEMS = Constants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = Constants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PARAM_COMMAND = Constants.CMD_PARAM_COMMAND;
    private static final String PARAM_RESULTS_MAX = Integer.toString(Constants.RESULTS_MAX);
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
    private HashMap<String, PlaylistItem> playlistMap;

    /**
     * Creates a new instance of Playlist
     *
     * @param player the {@link Player} for this playlist
     */
    public Playlist(Player player) {
        setPlayer(player);
        setSqueezeServer(player.getSqueezeServer());//current playlist
        setDatabase(new Database(getSqueezeServer()));
        prop = getSqueezeServer().getSlimProperties();

        playlistMap = new HashMap<String, PlaylistItem>();

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
     * Returns the {@link Player} for this playlist
     *
     * @return the player for this playlist
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the {@link Player} for this playlist
     *
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    private SqueezeServer getSqueezeServer() {
        return squeezeServer;
    }

    private void setSqueezeServer(SqueezeServer squeezeServer) {
        this.squeezeServer = squeezeServer;
    }

    /**
     * Adds a {@code Collection} of {@link org.bff.slimserver.domain.Playable}s to the end of the playlist.
     * Use this method when you only want one event fired for the entire
     * collection of playables.
     *
     * @param playables the {@code Collection} of {@link org.bff.slimserver.domain.Playable}s to add
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public void addItems(Collection<Playable> playables) throws SqueezeException {
        List<PlaylistItem> playlistItems = new ArrayList<PlaylistItem>();

        for (Playable o : playables) {
            playlistItems.add(new PlaylistItem(o));
            controlPlaylistAdd(PREFIX_TRACK_ID + o.getId());
        }

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_ADDED, playlistItems));
    }

    /**
     * Adds a playable to the end of the playlist.
     *
     * @param playable the {@link org.bff.slimserver.domain.Playable} to add
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public void addItem(Playable playable) throws SqueezeException {
        controlPlaylistAdd(PREFIX_TRACK_ID + playable.getId());
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_ADDED, playable));
    }

    public void addFolderItems(Collection<Folder> folders) throws SqueezeException {
        for (Folder o : folders) {
            addFolderItem(o);
        }
    }

    public void addFolderItem(Folder folder) throws SqueezeException {
        if (folder.getFolderType().equals(Folder.FOLDERTYPE.TRACK)) {
            controlPlaylistAdd(PREFIX_TRACK_ID + folder.getId());
        } else if (folder.getFolderType().equals(Folder.FOLDERTYPE.PLAYLIST)) {
            controlPlaylistAdd(PREFIX_PLAYLIST_ID + folder.getId());
        } else {
            controlPlaylistAdd(PREFIX_FOLDER_ID + folder.getId());
        }
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_ADDED, folder));
    }

    public void addPluginItem(XMLPlugin plugin) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_RADIO_ADD;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_COMMAND, plugin.getCommand());
        cmd = cmd.replaceFirst("<item>", PREFIX_ITEM_ID + encode(plugin.getItemId()));

        getSqueezeServer().sendCommand(new Command(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_ADDED, plugin));
    }

    public void addPluginItems(Collection<XMLPlugin> plugins) throws SqueezeException {

        List<PlaylistItem> playlistItems = new ArrayList<PlaylistItem>();

        for (XMLPlugin o : plugins) {
            String cmd = SLIM_PROP_PLAYER_PLAYLIST_RADIO_ADD;
            cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
            cmd = cmd.replaceFirst(PARAM_COMMAND, o.getCommand());
            cmd = cmd.replaceFirst("<item>", PREFIX_ITEM_ID + encode(o.getItemId()));

            getSqueezeServer().sendCommand(new Command(cmd));

            playlistItems.add(new PlaylistItem(o));
        }

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_ADDED, playlistItems));
    }

    /**
     * Clears the playlist and plays the item.
     *
     * @param plugin
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public void loadPluginItem(XMLPlugin plugin) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_RADIO_LOAD;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_COMMAND, plugin.getCommand());
        cmd = cmd.replaceFirst("<item>", PREFIX_ITEM_ID + encode(plugin.getItemId()));

        getSqueezeServer().sendCommand(new Command(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_LOADED, plugin));
    }

    public void insertPluginItem(XMLPlugin plugin) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_RADIO_INSERT;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_COMMAND, plugin.getCommand());
        cmd = cmd.replaceFirst("<item>", PREFIX_ITEM_ID + encode(plugin.getItemId()));

        getSqueezeServer().sendCommand(new Command(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_INSERTED, plugin));
    }

    public void playPluginItem(XMLPlugin plugin) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_RADIO_PLAY;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_COMMAND, plugin.getCommand());
        cmd = cmd.replaceFirst("<item>", PREFIX_ITEM_ID + encode(plugin.getItemId()));

        getSqueezeServer().sendCommand(new Command(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_LOADED, plugin));
    }

    /**
     * Clears playlist and plays playable
     *
     * @param playable
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public void playItemClearPlaylist(Playable playable) throws SqueezeException {
        controlPlaylistLoad(PREFIX_TRACK_ID + playable.getId());

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.PLAYLIST_CLEARED, playable));
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_LOADED, playable));
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_CHANGED, playable));
    }

    /**
     * Inserts the {@link Collection} of {@link org.bff.slimserver.domain.Playable}s after the currently
     * playlist item
     *
     * @param playableItems the items to insert
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public void insertItems(Collection<Playable> playableItems) throws SqueezeException {
        List<PlaylistItem> playlistItems = new ArrayList<PlaylistItem>();

        for (Playable o : playableItems) {
            playlistItems.add(new PlaylistItem(o));
            controlPlaylistInsert(PREFIX_TRACK_ID + o.getId());
        }

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_INSERTED, playlistItems));
    }

    /**
     * Inserts the {@link org.bff.slimserver.domain.Playable} after the currently playling item
     *
     * @param playable
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public void insertItem(Playable playable) throws SqueezeException {
        controlPlaylistInsert(PREFIX_TRACK_ID + playable.getId());
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_INSERTED, playable));
    }

    public void insertFolderItems(Collection<Folder> folders) throws SqueezeException {

        for (Folder o : folders) {
            insertFolderItem(o);
        }
    }

    public void loadFolderItems(Collection<Folder> folders) throws SqueezeException {
        for (Folder o : folders) {
            loadFolderItem(o);
        }
    }

    public void loadFolderItem(Folder folder) throws SqueezeException {
        if (folder.getFolderType().equals(Folder.FOLDERTYPE.TRACK)) {
            controlPlaylistLoad(PREFIX_TRACK_ID + folder.getId());
        } else if (folder.getFolderType().equals(Folder.FOLDERTYPE.PLAYLIST)) {
            controlPlaylistLoad(PREFIX_PLAYLIST_ID + folder.getId());
        } else {
            controlPlaylistLoad(PREFIX_FOLDER_ID + folder.getId());
        }
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_LOADED, folder));
    }

    /**
     * Inserts the {@link org.bff.slimserver.domain.Playable} after the currently playling folder
     *
     * @param folder
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public void insertFolderItem(Folder folder) throws SqueezeException {
        if (folder.getFolderType().equals(Folder.FOLDERTYPE.TRACK)) {
            controlPlaylistInsert(PREFIX_TRACK_ID + folder.getId());
        } else if (folder.getFolderType().equals(Folder.FOLDERTYPE.PLAYLIST)) {
            controlPlaylistInsert(PREFIX_PLAYLIST_ID + folder.getId());
        } else {
            controlPlaylistInsert(PREFIX_FOLDER_ID + folder.getId());
        }
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_INSERTED, folder));
    }

    public void removeItem(PlaylistItem playlistItem) throws SqueezeException {
        //modified this for remote items
        deleteByPlaylistIndex(playlistItem);
        playlistMap.remove(playlistItem.getId());
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_DELETED, playlistItem.getPlayableItem()));
    }

    public void replacePlaylist(SavedPlaylist playlist) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_REPLACE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<playlist>", encode(playlist.getUrl()));

        getSqueezeServer().sendCommand(new Command(cmd));

        playlistMap.clear();

        firePlaylistEvent(new PlaylistChangeEvent(this,
                PlaylistChangeEvent.PLAYLIST_REPLACED,
                new ArrayList<PlaylistItem>(playlist.getItems())));
    }

    public void addPlaylist(SavedPlaylist playlist) throws SqueezeException {
        setUpdating(true);
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_ADD;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<item>", encode(playlist.getUrl()));

        getSqueezeServer().sendCommand(new Command(cmd));
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this,
                PlaylistChangeEvent.PLAYLIST_ADDED,
                new ArrayList<PlaylistItem>(playlist.getItems())));
    }

    public void insertPlaylist(SavedPlaylist playlist) throws SqueezeException {
        setUpdating(true);
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_INSERT;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<item>", encode(playlist.getUrl()));

        getSqueezeServer().sendCommand(new Command(cmd));
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this,
                PlaylistChangeEvent.PLAYLIST_ADDED,
                new ArrayList<PlaylistItem>(playlist.getItems())));
    }

    public void savePlaylist(String playlistName) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_SAVE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<filename>", encode(playlistName));

        getSqueezeServer().sendCommand(new Command(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.PLAYLIST_SAVED, (Playable) null));
    }

    public void addAlbums(Collection<Album> albums) throws SqueezeException {
        for (Album album : albums) {
            addAlbum(album);
        }
    }

    public void loadAlbums(Collection<Album> albums) throws SqueezeException {
        clear();
        addAlbums(albums);
    }

    /**
     * Adds the album to the playlist
     *
     * @param album album to add
     */
    public void addAlbum(Album album) throws SqueezeException {
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
    public void loadAlbum(Album album) throws SqueezeException {
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
    public void insertAlbum(Album album) throws SqueezeException {
        setUpdating(true);
        controlPlaylistInsert(PREFIX_ALBUM_ID + album.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ALBUM_ADDED, album));
    }

    /**
     * Removes the album to the playlist
     *
     * @param album album to remove
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public void removeAlbum(Album album) throws SqueezeException {
        setUpdating(true);
        controlPlaylistDelete(PREFIX_ALBUM_ID + album.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ALBUM_DELETED, album));
    }

    /**
     * Removes the artist from the passed album from the playlist
     *
     * @param album the album's artist to remove
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public void removeArtists(Album album) throws SqueezeException {
        setUpdating(true);
        List<Artist> artists = new ArrayList<Artist>(getDatabase().getArtistsForAlbum(album));
        for (Artist artist : artists) {
            controlPlaylistDelete(PREFIX_ARTIST_ID + artist.getId());
        }
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ARTIST_DELETED, artists.get(0)));
    }

    /**
     * Removes the artist from the playlist
     *
     * @param artist artist to remove
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public void removeArtist(Artist artist) throws SqueezeException {
        setUpdating(true);
        controlPlaylistDelete(PREFIX_ARTIST_ID + artist.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ARTIST_DELETED, artist));
    }

    /**
     * Plays the item in the playlist and sets as the current item.
     * @param item
     * @throws org.bff.slimserver.exception.SqueezeException
     */
    /**
     * Adds the album to the playlist
     *
     * @param artist artist to add
     */
    public void addArtist(Artist artist) throws SqueezeException {
        setUpdating(true);
        controlPlaylistAdd(PREFIX_ARTIST_ID + artist.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ARTIST_ADDED, artist));
    }

    public void addArtist(Album album) throws SqueezeException {
        setUpdating(true);
        List<Artist> artists = new ArrayList<Artist>(getDatabase().getArtistsForAlbum(album));
        for (Artist artist : artists) {
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
    public void loadArtist(Artist artist) throws SqueezeException {
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
    public void insertArtist(Artist artist) throws SqueezeException {
        setUpdating(true);
        controlPlaylistInsert(PREFIX_ARTIST_ID + artist.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ARTIST_ADDED, artist));
    }

    public void addYear(String year) throws SqueezeException {
        setUpdating(true);
        controlPlaylistAdd(PREFIX_YEAR_ID + year);
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.YEAR_ADDED, null, year));
    }

    public void loadYear(String year) throws SqueezeException {
        setUpdating(true);
        controlPlaylistLoad(PREFIX_YEAR_ID + year);
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.YEAR_ADDED, null, year));
    }

    public void insertYear(String year) throws SqueezeException {
        setUpdating(true);
        controlPlaylistInsert(PREFIX_YEAR_ID + year);
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.YEAR_ADDED, null, year));
    }

    public void deleteYear(String year) throws SqueezeException {
        setUpdating(true);
        controlPlaylistDelete(PREFIX_YEAR_ID + year);
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.YEAR_DELETED, null, year));
    }

    public void addGenre(Genre genre) throws SqueezeException {
        setUpdating(true);
        controlPlaylistAdd(PREFIX_GENRE_ID + genre.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.GENRE_ADDED, genre));
    }

    public void loadGenre(Genre genre) throws SqueezeException {
        setUpdating(true);
        controlPlaylistLoad(PREFIX_GENRE_ID + genre.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.GENRE_ADDED, genre));
    }

    public void insertGenre(Genre genre) throws SqueezeException {
        setUpdating(true);
        controlPlaylistInsert(PREFIX_GENRE_ID + genre.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.GENRE_ADDED, genre));
    }

    public void deleteGenre(Genre genre) throws SqueezeException {
        setUpdating(true);
        controlPlaylistDelete(PREFIX_GENRE_ID + genre.getId());
        setUpdating(false);
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.GENRE_DELETED, genre));
    }

    /**
     * Plays the item in the playlist and sets as the current item.
     *
     * @param item item to play
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public void playItem(PlaylistItem item) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_PLAY_NOW;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<itemindex>", Integer.toString(item.getPlaylistIndex()));

        getSqueezeServer().sendCommand(new Command(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_CHANGED, item.getPlayableItem()));
    }

    /**
     * Plays the item in the playlist and sets as the current item.
     *
     * @param playlistIndex item to play
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public void playItem(int playlistIndex) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_PLAY_NOW;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<itemindex>", Integer.toString(playlistIndex));

        getSqueezeServer().sendCommand(new Command(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_CHANGED, (Playable) null));
    }

    public int getCurrentIndex() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_ITEM;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        return Integer.parseInt(response[0]);
    }

    public PlaylistItem getRemoteItem(String index) throws SqueezeException {
        PlaylistItem item = playlistMap.get(index);

        if (item == null) {
            getItems();
        }

        return playlistMap.get(index);
    }

    public String getCurrentImageURL() {
        return "http://" + getSqueezeServer().getServer() + ":" + getSqueezeServer().getServerPort()
                + "/music/current/cover.jpg?player=" + getPlayer().getId();
    }

    /**
     * Returns the current {@link org.bff.slimserver.domain.PlaylistItem} of the currently playing
     * item.  This method will return null for remote streams and empty playlists.  You may want to
     * check the {@link #isCurrentRemote()} or the {@link #getItemCount()} method before calling.
     *
     * @return
     * @throws org.bff.slimserver.exception.SqueezeException
     *
     */
    public PlaylistItem getCurrentItem() throws SqueezeException {
        PlaylistItem item = null;

        if (!isCurrentRemote()) {
            PlayableItem s = getDatabase().lookupItemByUrl(getCurrentItemPath());

            if (s != null) {
                item = new PlaylistItem(s);
                item.setPlaylistIndex(getCurrentIndex());
            }
        } else {
            item = getCurrentRemoteItem();
        }
        return item;
    }

    public PlaylistItem getCurrentRemoteItem() throws ConnectionException {
        PlaylistItem retItem = null;

        String command = SLIM_PROP_PLAYER_PLAYLIST.replaceAll(PARAM_START, "-");
        command = command.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        command = command.replaceAll(PARAM_ITEMS, "1");
        command = command.replaceAll(PARAM_TAGS, TAG_RETURN + PlayableTags.RETURN_TAGS);

        String[] response = getSqueezeServer().sendCommand(command);

        for (int i = 0; i < response.length - 1; ) {
            if (response[i].startsWith(PREFIX_PLAYLIST_INDEX)) {
                int playlistIndex = Integer.parseInt(response[i].replace(PREFIX_PLAYLIST_INDEX, ""));

                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;
                String[] itemArray = new String[tagCount];
                int j = 0;
                while (j < tagCount) {
                    itemArray[j++] = response[i++];
                    if (response[i].startsWith(PREFIX_PLAYLIST_INDEX) || i > response.length - 2) {
                        itemArray[j] = response[i];
                        break;
                    }
                }
                PlayableItem item = getSqueezeServer().convertResponse(itemArray);
                retItem = new PlaylistItem(item);
                retItem.setPlaylistIndex(playlistIndex);

            } else {
                ++i;
            }
        }

        return retItem;
    }

    public String getCurrentItemPath() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_PATH;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        if (response == null || response[0] == null) {
            return null;
        }

        return response[0].trim();
    }

    public void repeatPlaylist() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_REPEAT;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSqueezeServer().sendCommand(new Command(cmd, "2"));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.REPEAT_PLAYLIST, (Playable) null));
    }

    public void repeatItem() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_REPEAT;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSqueezeServer().sendCommand(new Command(cmd, "1"));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.REPEAT_ITEM, (Playable) null));
    }

    public void repeatOff() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_REPEAT;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSqueezeServer().sendCommand(new Command(cmd, "0"));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.REPEAT_OFF, (Playable) null));
    }

    public boolean isRepeatingItem() throws SqueezeException {
        return getRepeatingStatus() == 1;
    }

    public boolean isRepeatingPlaylist() throws SqueezeException {
        return getRepeatingStatus() == 2;
    }

    public boolean isRepeatingOff() throws ConnectionException {
        return getRepeatingStatus() == 0;
    }

    public int getRepeatingStatus() throws ConnectionException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_REPEAT_QUERY;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        return Integer.parseInt(response[0]);
    }

    public void shuffleItems() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSqueezeServer().sendCommand(new Command(cmd, "1"));
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.PLAYLIST_CHANGED, (Playable) null));
    }

    public void shuffleAlbums() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSqueezeServer().sendCommand(new Command(cmd, "2"));
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.PLAYLIST_CHANGED, (Playable) null));
    }

    public void shuffleOff() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSqueezeServer().sendCommand(new Command(cmd, "0"));
    }

    public boolean isShuffleItem() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE_QUERY;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        return Integer.parseInt(response[0]) == 1;
    }

    public boolean isShuffleAlbum() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE_QUERY;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        return Integer.parseInt(response[0]) == 2;
    }

    public void clear() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CLEAR;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        getSqueezeServer().sendCommand(new Command(cmd));
        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.PLAYLIST_CLEARED, (Playable) null));
    }

    /**
     * Returns playlist items with just playlist and item indexes populated.
     *
     * @return the collection of item ids
     */
    public Collection<PlaylistItem> getItemIds() throws SqueezeException {
        List<PlaylistItem> retList = new ArrayList<PlaylistItem>();
        String command = SLIM_PROP_PLAYER_PLAYLIST.replaceAll(PARAM_START, "0");
        command = command.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, TAG_RETURN + "");

        String[] response = getSqueezeServer().sendCommand(command);
        for (int i = 0; i < response.length - 1; ) {
            if (response[i].startsWith(PREFIX_PLAYLIST_INDEX)) {
                int playlistIndex = Integer.parseInt(response[i].replace(PREFIX_PLAYLIST_INDEX, ""));

                int tagCount = 2;
                String[] items = new String[tagCount];
                for (int j = 0; j < tagCount; j++) {
                    items[j] = response[i++];
                }

                PlayableItem item = getSqueezeServer().convertResponse(items);
                PlaylistItem playlistItem = new PlaylistItem(item);
                playlistItem.setPlaylistIndex(playlistIndex);
                retList.add(playlistItem);
            } else {
                ++i;
            }
        }

        return retList;
    }

    public String getCurrentPath() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_PATH;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        return response[0];
    }

    public String getCurrentGenre() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_GENRE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        return "0".equalsIgnoreCase(response[0]) ? null : response[0];
    }

    public String getCurrentArtist() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_ARTIST;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        return "0".equalsIgnoreCase(response[0]) ? null : response[0];
    }

    public String getCurrentAlbum() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_ALBUM;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        return "0".equalsIgnoreCase(response[0]) ? null : response[0];
    }

    public String getCurrentTitle() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_TITLE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        return "0".equalsIgnoreCase(response[0]) ? null : response[0];
    }

    public double getCurrentDuration() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_DURATION;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        return Double.parseDouble("0".equalsIgnoreCase(response[0]) ? "0" : response[0]);
    }

    public boolean isCurrentRemote() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_REMOTE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        if (response != null) {

            try {
                return Integer.parseInt(response[0]) == 1 ? true : false;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    public String getCurrentRemoteTitle() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_CURRENT_REMOTE_TITLE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        return "0".equalsIgnoreCase(response[0]) ? null : response[0];
    }

    public int getItemCount() throws SqueezeException {

        String cmd = SLIM_PROP_PLAYER_PLAYLIST_COUNT;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());

        String[] response = getSqueezeServer().sendCommand(new Command(cmd));

        return Integer.parseInt(response[0]);
    }

    public String getPlaylistTimeStamp() throws ConnectionException {
        String retString = null;
        String command = SLIM_PROP_PLAYER_PLAYLIST.replaceAll(PARAM_START, "0");
        command = command.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        command = command.replaceAll(PARAM_ITEMS, "0");
        command = command.replaceAll(PARAM_TAGS, "");

        String[] response = getSqueezeServer().sendCommand(command);

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

    public Collection<PlaylistItem> getItems() throws SqueezeException {
        List<PlaylistItem> retList = new ArrayList<PlaylistItem>();

        playlistMap.clear();

        String command = SLIM_PROP_PLAYER_PLAYLIST.replaceAll(PARAM_START, "0");
        command = command.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, TAG_RETURN + PlayableTags.RETURN_TAGS);

        String[] response = getSqueezeServer().sendCommand(command);

        for (int i = 0; i < response.length - 1; ) {
            if (response[i].startsWith(PREFIX_PLAYLIST_INDEX)) {
                int playlistIndex = Integer.parseInt(response[i].replace(PREFIX_PLAYLIST_INDEX, ""));

                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;
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
                PlayableItem item = getSqueezeServer().convertResponse(itemArray);
                PlaylistItem playlistItem = new PlaylistItem(item);
                playlistItem.setPlaylistIndex(playlistIndex);
                retList.add(playlistItem);
                playlistMap.put(item.getId(), playlistItem);
            } else {
                ++i;
            }
        }

        return retList;
    }

    public void playPrevious() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_PLAY_NOW;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<itemindex>", "-1");

        getSqueezeServer().sendCommand(new Command(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_CHANGED, (Playable) null));
    }

    public void playNext() throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_PLAY_NOW;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<itemindex>", "+1");

        getSqueezeServer().sendCommand(new Command(cmd));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.ITEM_CHANGED, (Playable) null));
    }

    public synchronized void swap(PlaylistItem item1, PlaylistItem item2) throws SqueezeException {
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
     * Moves the {@link org.bff.slimserver.domain.PlaylistItem} to the desired index.
     *
     * @param item    the item to move
     * @param toIndex where to move the item
     * @throws org.bff.slimserver.exception.SqueezeException
     *          if there is a problem
     *          moving the item
     */
    public void move(PlaylistItem item, int toIndex) throws SqueezeException {
        move(item.getPlaylistIndex(), toIndex);
    }

    public void move(int fromIndex, int toIndex) throws SqueezeException {
        if (fromIndex == toIndex) {
            return;
        }

        String command = SLIM_PROP_PLAYER_PLAYLIST_MOVE;
        command = command.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        command = command.replaceFirst("<fromindex>", Integer.toString(fromIndex));
        command = command.replaceFirst("<toindex>", Integer.toString(toIndex));

        getSqueezeServer().sendCommand(new Command(command));

        firePlaylistEvent(new PlaylistChangeEvent(this, PlaylistChangeEvent.PLAYLIST_CHANGED, (Playable) null));
    }

    private void controlPlaylistAdd(String command) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYLIST_CONTROL;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_TAGS, PREFIX_CMD + PREFIX_CMD_ADD + " " + command);

        getSqueezeServer().sendCommand(new Command(cmd));
    }

    private void controlPlaylistInsert(String command) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYLIST_CONTROL;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_TAGS, PREFIX_CMD + PREFIX_CMD_INSERT + " " + command);

        getSqueezeServer().sendCommand(new Command(cmd));
    }

    private void controlPlaylistLoad(String command) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYLIST_CONTROL;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_TAGS, PREFIX_CMD + PREFIX_CMD_LOAD + " " + command);

        getSqueezeServer().sendCommand(new Command(cmd));
    }

    private void controlPlaylistDelete(String command) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYLIST_CONTROL;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst(PARAM_TAGS, PREFIX_CMD + PREFIX_CMD_DELETE + " " + command.replace("-", ""));

        getSqueezeServer().sendCommand(new Command(cmd));
    }

    private void deleteByPlaylistIndex(PlaylistItem playlistItem) throws SqueezeException {
        String cmd = SLIM_PROP_PLAYER_PLAYLIST_DELETE;
        cmd = cmd.replaceFirst(PARAM_PLAYER_ID, getPlayer().getId());
        cmd = cmd.replaceFirst("<itemindex>", Integer.toString(playlistItem.getPlaylistIndex()));

        getSqueezeServer().sendCommand(new Command(cmd));
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
    public Database getDatabase() {
        return database;
    }

    /**
     * @param database the database to set
     */
    public void setDatabase(Database database) {
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
        return Utils.encode(criteria, getSqueezeServer().getEncoding());
    }
}
