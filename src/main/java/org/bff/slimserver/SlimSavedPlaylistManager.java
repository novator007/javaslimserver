/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.bff.slimserver.events.PlaylistChangeEvent;
import org.bff.slimserver.events.PlaylistChangeListener;
import org.bff.slimserver.events.SavedPlaylistChangeEvent;
import org.bff.slimserver.events.SavedPlaylistChangeListener;
import org.bff.slimserver.exception.SlimConnectionException;
import org.bff.slimserver.exception.SlimDatabaseException;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.musicobjects.SlimPlayableItem;
import org.bff.slimserver.musicobjects.SlimPlayableTags;
import org.bff.slimserver.musicobjects.SlimPlaylistItem;
import org.bff.slimserver.musicobjects.SlimSavedPlaylist;
import org.bff.slimserver.musicobjects.SlimSong;

/**
 * Class used to add, delete, and manipulate saved playlists.
 * @author bfindeisen
 */
public class SlimSavedPlaylistManager {

    private SlimServer slimServer;
    private static Properties prop = SlimServer.getSlimProperties();
    public static String SLIM_PROP_PLAYLIST_NEW;
    public static String SLIM_PROP_PLAYLIST_RENAME;
    public static String SLIM_PROP_PLAYLIST_EDIT;
    private static String SLIM_PROP_PLAYLISTS;
    private static String SLIM_PROP_PLAYLIST_TRACKS;
    public static String SLIM_PROP_PLAYLIST_DELETE;
    private static final String PREFIX_PLAYLIST_CMD = "cmd:";
    public static final String PREFIX_PLAYLIST_CMD_ADD = PREFIX_PLAYLIST_CMD + "add ";
    public static final String PREFIX_PLAYLIST_CMD_DELETE = PREFIX_PLAYLIST_CMD + "delete ";
    public static final String PREFIX_PLAYLIST_CMD_UP = PREFIX_PLAYLIST_CMD + "up ";
    public static final String PREFIX_PLAYLIST_CMD_DOWN = PREFIX_PLAYLIST_CMD + "down ";
    public static final String PREFIX_PLAYLIST_CMD_MOVE = PREFIX_PLAYLIST_CMD + "move ";
    private static final String PREFIX_ID = "id:";
    private static final String PREFIX_PLAYLIST_NAME = "name:";
    private static final String PREFIX_PLAYLIST_RENAME_DRY_RUN = "dry_run:";
    private static final String PREFIX_PLAYLIST_NEW_NAME = "newname:";
    private static final String PREFIX_PLAYLIST_OVERWRRITEN_ID = "overwritten_playlist_id:";
    private static final String PREFIX_PLAYLIST_EDIT_INDEX = "index:";
    private static final String PREFIX_PLAYLIST_EDIT_TO_INDEX = "toindex:";
    private static final String PREFIX_PLAYLIST = "playlist:";
    private static final String PREFIX_PLAYLIST_ID = "playlist_id:";
    private static final String PREFIX_PLAYLIST_INDEX = "playlist index:";
    private static final String PREFIX_PLAYLIST_URL = "url:";
    private static final String PREFIX_TITLE = "title:";
    private static final String TAG_RETURN = "tags:";
    private static final String TAG_PLAYLIST_URL = "u";
    private static final String PARAM_START = SlimConstants.CMD_PARAM_START;
    private static final String PARAM_ITEMS = SlimConstants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = SlimConstants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PARAM_RESULTS_MAX = Integer.toString(SlimConstants.RESULTS_MAX);
    private List<SavedPlaylistChangeListener> savedPlaylistListeners =
            new ArrayList<SavedPlaylistChangeListener>();

    static {
        loadProperties();
    }

    public SlimSavedPlaylistManager(SlimServer slimServer) {
        this.slimServer = slimServer;
    }

    /**
     * Adds a {@link PlaylistChangeListener} to this object to receive
     * {@link PlaylistChangeEvent}s.
     * @param pcl the PlaylistChangeListener to add
     */
    public synchronized void addSavedPlaylistChangeListener(SavedPlaylistChangeListener pcl) {
        savedPlaylistListeners.add(pcl);
    }

    /**
     * Removes a {@link PlaylistChangeListener} from this object.
     * @param pcl the PlaylistChangeListener to remove
     */
    public synchronized void removeSavedPlaylistChangeListener(SavedPlaylistChangeListener pcl) {
        savedPlaylistListeners.remove(pcl);
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     * @param id the event id to send
     */
    protected synchronized void fireSavedPlaylistChangeEvent(int id, SlimSavedPlaylist playlist) {
        SavedPlaylistChangeEvent pce = new SavedPlaylistChangeEvent(this, id, playlist);

        for (SavedPlaylistChangeListener pcl : savedPlaylistListeners) {
            pcl.playlistChanged(pce);
        }
    }

    /**
     * @return the slimServer
     */
    public SlimServer getSlimServer() {
        return slimServer;
    }

    /**
     * @param slimServer the slimServer to set
     */
    public void setSlimServer(SlimServer slimServer) {
        this.slimServer = slimServer;
    }

    public Collection<SlimPlaylistItem> listSongsForSavedPlaylist(SlimSavedPlaylist playlist) throws SlimDatabaseException, SlimConnectionException {
        List<SlimPlaylistItem> retList = new ArrayList<SlimPlaylistItem>();

        String command = SLIM_PROP_PLAYLIST_TRACKS.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, PREFIX_PLAYLIST_ID + playlist.getId() + " " + TAG_RETURN + SlimPlayableTags.RETURN_TAGS);

        String[] response = getSlimServer().sendCommand(command);

        if (response != null) {
            for (int i = 0; i < response.length - 1;) {
                if (response[i].startsWith(PREFIX_PLAYLIST_INDEX)) {
                    int playlistIndex = Integer.parseInt(response[i].replace(PREFIX_PLAYLIST_INDEX, ""));
                    ++i;

                    int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;
                    String[] songArray = new String[tagCount];
                    int j = 0;
                    while ((!response[i].startsWith(PREFIX_PLAYLIST_INDEX)
                            && i < response.length - 1)
                            && j < tagCount) {
                        songArray[j++] = response[i++];
                    }
                    SlimPlayableItem song = getSlimServer().convertResponse(songArray);
                    SlimPlaylistItem playlistSong = new SlimPlaylistItem(song);
                    playlistSong.setPlaylistIndex(playlistIndex);
                    retList.add(playlistSong);
                } else {
                    ++i;
                }
            }
        }
        
        return retList;
    }

    /**
     * Deletes a {@link SlimSavedPlaylist}
     * @param playlist the {@link SlimSavedPlaylist} to delete
     */
    public void deleteSavedPlaylist(SlimSavedPlaylist playlist) throws SlimConnectionException {
        String command = SLIM_PROP_PLAYLIST_DELETE;
        command = command.replaceAll(PARAM_TAGS, PREFIX_PLAYLIST_ID + playlist.getId());

        getSlimServer().sendCommand(command);

        fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.ITEM_DELETED, playlist);
    }

    /**
     * Returns a {@code Collection} of all {@link SlimSavedPlaylist}s
     * @return a {@code Collection} of all {@link SlimSavedPlaylist}s
     */
    public Collection<SlimSavedPlaylist> getPlaylists() throws SlimConnectionException {
        return getPlaylists(true);
    }

    /**
     * Returns a {@code Collection} of all {@link SlimSavedPlaylist}s
     * @return a {@code Collection} of all {@link SlimSavedPlaylist}s
     */
    public Collection<SlimSavedPlaylist> getPlaylists(boolean fillSongs) throws SlimConnectionException {
        List<SlimSavedPlaylist> retList = new ArrayList<SlimSavedPlaylist>();

        String command = SLIM_PROP_PLAYLISTS.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, TAG_RETURN + TAG_PLAYLIST_URL);

        String[] response = sendCommand(command);

        //the last item is count so ignore the last one
        for (int i = 0; i < response.length - 1; i++) {
            try {
                SlimSavedPlaylist ssp = new SlimSavedPlaylist(getSlimServer());
                ssp.setId(response[i++].split(PREFIX_ID)[1].trim());
                ssp.setName(response[i++].split(PREFIX_PLAYLIST)[1].trim());
                ssp.setUrl(response[i].split(PREFIX_PLAYLIST_URL)[1].trim());

                if (fillSongs) {
                    ssp.setItems(listSongsForSavedPlaylist(ssp));
                }

                retList.add(ssp);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return retList;
    }

    public SlimSavedPlaylist createEmptyPlaylist(String name) throws SlimException {
        String command = SLIM_PROP_PLAYLIST_NEW;
        command = command.replaceFirst(PARAM_TAGS, PREFIX_PLAYLIST_NAME + encode(name));

        String[] response = getSlimServer().sendCommand(command);

        SlimSavedPlaylist savedPlaylist = new SlimSavedPlaylist(getSlimServer());
        savedPlaylist.setName(name);

        for (int i = 0; i < response.length; i++) {
            if (response[i].startsWith(PREFIX_PLAYLIST_OVERWRRITEN_ID)) {
                throw new SlimDatabaseException("Playlist already exists.");
            } else if (response[i].startsWith(PREFIX_PLAYLIST_ID)) {
                savedPlaylist.setId(response[i++].split(PREFIX_PLAYLIST_ID)[1].trim());
                fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.PLAYLIST_ADDED, null);
            }
        }

        return savedPlaylist;
    }

    /**
     *
     * @param playlist
     * @param newName
     * @throws SlimException is the new playlist name already exists
     */
    public void renamePlaylist(SlimSavedPlaylist playlist, String newName) throws SlimException {
        //first check to see if the new playlist name exists
        String command = SLIM_PROP_PLAYLIST_RENAME;
        command = command.replaceFirst(PARAM_TAGS,
                PREFIX_PLAYLIST_ID + playlist.getId() + " "
                + PREFIX_PLAYLIST_RENAME_DRY_RUN + "1 "
                + PREFIX_PLAYLIST_NEW_NAME + encode(newName));

        String[] response = getSlimServer().sendCommand(command);

        if (response != null) {
            for (int i = 0; i < response.length; i++) {
                if (response[i].startsWith(PREFIX_PLAYLIST_OVERWRRITEN_ID)) {
                    throw new SlimDatabaseException("Playlist already exists.");
                }
            }
        }
        //we have tested the new name so go ahead and rename
        command = SLIM_PROP_PLAYLIST_RENAME;
        command = command.replaceFirst(PARAM_TAGS, PREFIX_PLAYLIST_ID + playlist.getId() + " " + PREFIX_PLAYLIST_NEW_NAME + encode(newName));

        getSlimServer().sendCommand(command);

        fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.PLAYLIST_RENAMED, null);
    }

    public void addPlaylistSongs(SlimSavedPlaylist playlist,
            Collection<SlimSong> songs) throws SlimException {

        for (SlimSong song : songs) {
            addPlaylistSong(playlist, song);
        }
    }

    public void addPlaylistSong(SlimSavedPlaylist playlist, SlimSong song) throws SlimException {
        String command = SLIM_PROP_PLAYLIST_EDIT;
        command = command.replaceFirst(PARAM_TAGS,
                PREFIX_PLAYLIST_CMD_ADD
                + PREFIX_PLAYLIST_ID + playlist.getId() + " "
                + PREFIX_TITLE + encode(song.getTitle()) + " "
                + PREFIX_PLAYLIST_URL + song.getUrl());

        getSlimServer().sendCommand(command);

        fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.ITEM_ADDED, playlist);
    }

    public void deletePlaylistSong(SlimSavedPlaylist playlist, SlimPlaylistItem song) throws SlimException {
        String command = SLIM_PROP_PLAYLIST_EDIT;
        command = command.replaceFirst(PARAM_TAGS,
                PREFIX_PLAYLIST_CMD_DELETE
                + PREFIX_PLAYLIST_ID + playlist.getId() + " "
                + PREFIX_PLAYLIST_EDIT_INDEX + song.getPlaylistIndex());
        getSlimServer().sendCommand(command);
        fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.ITEM_DELETED, playlist);
    }

    public void movePlaylistSongUp(SlimSavedPlaylist playlist, SlimPlaylistItem item) throws SlimConnectionException {
        movePlaylistSong(playlist, item, PREFIX_PLAYLIST_CMD_UP);
    }

    public void movePlaylistSongDown(SlimSavedPlaylist playlist, SlimPlaylistItem item) throws SlimConnectionException {
        movePlaylistSong(playlist, item, PREFIX_PLAYLIST_CMD_DOWN);
    }

    public void movePlaylistSong(SlimSavedPlaylist playlist, SlimPlaylistItem item, int toIndex) throws SlimConnectionException {
        String command = SLIM_PROP_PLAYLIST_EDIT;
        command = command.replaceFirst(PARAM_TAGS,
                PREFIX_PLAYLIST_CMD_MOVE
                + PREFIX_PLAYLIST_ID + playlist.getId() + " "
                + PREFIX_PLAYLIST_EDIT_INDEX + item.getPlaylistIndex() + " "
                + PREFIX_PLAYLIST_EDIT_TO_INDEX + toIndex);


        getSlimServer().sendCommand(command);
        fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.ITEM_MOVED, playlist);
    }

    private void movePlaylistSong(SlimSavedPlaylist playlist, SlimPlaylistItem item, String cmd) throws SlimConnectionException {
        String command = SLIM_PROP_PLAYLIST_EDIT;
        command = command.replaceFirst(PARAM_TAGS,
                cmd
                + PREFIX_PLAYLIST_ID + playlist.getId() + " "
                + PREFIX_PLAYLIST_EDIT_INDEX + item.getPlaylistIndex());


        getSlimServer().sendCommand(command);
        fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.ITEM_MOVED, playlist);
    }

    private static void loadProperties() {
        SLIM_PROP_PLAYLIST_NEW = prop.getProperty("SS_PLAYLIST_NEW");
        SLIM_PROP_PLAYLIST_RENAME = prop.getProperty("SS_PLAYLIST_RENAME");
        SLIM_PROP_PLAYLIST_EDIT = prop.getProperty("SS_PLAYLIST_EDIT");
        SLIM_PROP_PLAYLISTS = prop.getProperty("SS_DB_PLAYLISTS_QUERY");
        SLIM_PROP_PLAYLIST_TRACKS = prop.getProperty("SS_DB_PLAYLIST_TRACKS");
        SLIM_PROP_PLAYLIST_DELETE = prop.getProperty("SS_DB_PLAYLIST_DELETE");
    }

    private String[] sendCommand(String slimCommand) {
        return sendCommand(slimCommand, null);
    }

    private String[] sendCommand(String slimCommand, String param) {
        try {
            if (param == null) {
                return getSlimServer().sendCommand(new SlimCommand(slimCommand));
            } else {
                return getSlimServer().sendCommand(new SlimCommand(slimCommand, param));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String encode(String criteria) {
        return Utils.encode(criteria, getSlimServer().getEncoding());
    }
}
