/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bff.slimserver.SlimCommand;
import org.bff.slimserver.SlimPlayer;
import org.bff.slimserver.SlimConstants;
import org.bff.slimserver.SlimDatabase;
import org.bff.slimserver.SlimFavoritePlugin;
import org.bff.slimserver.SlimPlaylist;
import org.bff.slimserver.SlimSavedPlaylistManager;
import org.bff.slimserver.SlimServer;
import org.bff.slimserver.Utils;
import org.bff.slimserver.events.ConnectionChangeEvent;
import org.bff.slimserver.events.ConnectionChangeListener;
import org.bff.slimserver.events.DatabaseScanEvent;
import org.bff.slimserver.events.DatabaseScanListener;
import org.bff.slimserver.events.FavoriteChangeEvent;
import org.bff.slimserver.events.FavoriteChangeListener;
import org.bff.slimserver.events.PlayerChangeEvent;
import org.bff.slimserver.events.PlayerChangeListener;
import org.bff.slimserver.events.PlaylistChangeEvent;
import org.bff.slimserver.events.PlaylistChangeListener;
import org.bff.slimserver.events.SavedPlaylistChangeEvent;
import org.bff.slimserver.events.SavedPlaylistChangeListener;
import org.bff.slimserver.events.SleepChangeEvent;
import org.bff.slimserver.events.SleepChangeListener;
import org.bff.slimserver.events.VolumeChangeEvent;
import org.bff.slimserver.events.VolumeChangeListener;
import org.bff.slimserver.exception.SlimConnectionException;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.musicobjects.SlimPlayableObject;
import org.bff.slimserver.musicobjects.SlimPlaylistItem;
import org.bff.slimserver.musicobjects.SlimPlaylistObject;
import org.bff.slimserver.musicobjects.SlimSavedPlaylist;

/**
 * The class uses the listen feature of SlimServer to receive events and forward
 * them to attached listeners.  The {@link start} method must be called to start listening.
 * Stop the thread gracefull by calling the {@link stop} method.
 *
 * <p>
 * Events dispatched from this class:
 * <br />
 * {@link PlayerChangeEvent.PLAYER_STOPPED}
 * </p>
 * @author bfindeisen
 */
public class SlimEventListener extends SlimEventMonitor {

    private SlimPlayer player;
    private SlimPlaylist playlist;
    private SlimDatabase database;
    private SlimFavoritePlugin favoritePlugin;
    private SlimSavedPlaylistManager playlistManager;
    private InetAddress serverAddress;
    private int serverPort;
    private Socket socket;
    private boolean running;
    private static final String ON = SlimConstants.RESULT_TRUE;
    private static final String OFF = SlimConstants.RESULT_FALSE;
    private static final String PREFIX_PLAYER_PAUSE =
            SlimPlayer.SLIM_PROP_PLAYER_PAUSE.replaceAll(SlimConstants.CMD_PARAM_PLAYER_ID, "").trim();
    private static final String PREFIX_PLAYER_PLAY =
            SlimPlayer.SLIM_PROP_PLAYER_PLAY.replaceAll(SlimConstants.CMD_PARAM_PLAYER_ID, "").trim();
    private static final String PREFIX_PLAYER_STOP =
            SlimPlayer.SLIM_PROP_PLAYER_STOP.replaceAll(SlimConstants.CMD_PARAM_PLAYER_ID, "").trim();
    private static final String PREFIX_PLAYER_SYNC =
            SlimPlayer.SLIM_PROP_SYNC.replaceAll(SlimConstants.CMD_PARAM_PLAYER_ID, "").trim();
    private static final String PREFIX_PLAYER_POWER =
            SlimPlayer.SLIM_PROP_PLAYER_POWER.replaceAll(SlimConstants.CMD_PARAM_PLAYER_ID, "").replaceAll(SlimConstants.CMD_PARAM_MARKER, "").trim();
    private static final String PREFIX_PLAYER_VOLUME =
            SlimPlayer.SLIM_PROP_PLAYER_VOLUME.replaceAll(SlimConstants.CMD_PARAM_PLAYER_ID, "").replaceAll(SlimConstants.CMD_PARAM_MARKER, "").trim();
    private static final String PREFIX_PLAYER_SLEEP =
            SlimPlayer.SLIM_PROP_PLAYER_SLEEP.replaceAll(SlimConstants.CMD_PARAM_PLAYER_ID, "").replaceAll(SlimConstants.CMD_PARAM_MARKER, "").trim();
    private static final String PREFIX_DATABASE_RESCAN =
            SlimDatabase.PROP_RESCAN.trim();
    private static final String PREFIX_PLAYLIST_REPEAT =
            SlimPlaylist.SLIM_PROP_PLAYER_PLAYLIST_REPEAT.replaceAll(SlimConstants.CMD_PARAM_PLAYER_ID, "").replaceAll(SlimConstants.CMD_PARAM_MARKER, "").trim();
    private static final String PREFIX_PLAYLIST_SAVE =
            SlimPlaylist.SLIM_PROP_PLAYER_PLAYLIST_SAVE.replaceAll(SlimConstants.CMD_PARAM_PLAYER_ID, "").replaceAll(SlimConstants.CMD_PARAM_MARKER, "").replaceAll(SlimConstants.CMD_PARAM_FILENAME, "").trim();
    private static final String PREFIX_SAVED_PLAYLIST_NEW =
            SlimSavedPlaylistManager.SLIM_PROP_PLAYLIST_NEW.replaceAll(SlimConstants.CMD_PARAM_TAGGED_PARAMS, "");
    private static final String PREFIX_SAVED_PLAYLIST_DELETE =
            SlimSavedPlaylistManager.SLIM_PROP_PLAYLIST_DELETE.replaceAll(SlimConstants.CMD_PARAM_TAGGED_PARAMS, "");
    private static final String PREFIX_SAVED_PLAYLIST_RENAME =
            SlimSavedPlaylistManager.SLIM_PROP_PLAYLIST_RENAME.replaceAll(SlimConstants.CMD_PARAM_TAGGED_PARAMS, "");
    private static final String PREFIX_SAVED_PLAYLIST_ITEM_ADDED =
            SlimSavedPlaylistManager.SLIM_PROP_PLAYLIST_EDIT.replaceAll(SlimConstants.CMD_PARAM_TAGGED_PARAMS, "")
            + SlimSavedPlaylistManager.PREFIX_PLAYLIST_CMD_ADD;
    private static final String PREFIX_SAVED_PLAYLIST_ITEM_DELETED =
            SlimSavedPlaylistManager.SLIM_PROP_PLAYLIST_EDIT.replaceAll(SlimConstants.CMD_PARAM_TAGGED_PARAMS, "")
            + SlimSavedPlaylistManager.PREFIX_PLAYLIST_CMD_DELETE;
    private static final String PREFIX_SAVED_PLAYLIST_ITEM_UP =
            SlimSavedPlaylistManager.SLIM_PROP_PLAYLIST_EDIT.replaceAll(SlimConstants.CMD_PARAM_TAGGED_PARAMS, "")
            + SlimSavedPlaylistManager.PREFIX_PLAYLIST_CMD_UP;
    private static final String PREFIX_SAVED_PLAYLIST_ITEM_DOWN =
            SlimSavedPlaylistManager.SLIM_PROP_PLAYLIST_EDIT.replaceAll(SlimConstants.CMD_PARAM_TAGGED_PARAMS, "")
            + SlimSavedPlaylistManager.PREFIX_PLAYLIST_CMD_DOWN;
    private static final String PREFIX_SAVED_PLAYLIST_ITEM_MOVED =
            SlimSavedPlaylistManager.SLIM_PROP_PLAYLIST_EDIT.replaceAll(SlimConstants.CMD_PARAM_TAGGED_PARAMS, "")
            + SlimSavedPlaylistManager.PREFIX_PLAYLIST_CMD_MOVE;
    private static final String PLAYLIST_ADD_TRACKS =
            SlimConstants.SUBSCRIBE_PLAYLIST_RESPONSE_ADD_TRACKS;
    private static final String PLAYLIST_ADD =
            SlimConstants.SUBSCRIBE_PLAYLIST_RESPONSE_ADD;
    private static final String PLAYLIST_DELETE_TRACKS =
            SlimConstants.SUBSCRIBE_PLAYLIST_RESPONSE_DELETE;
    private static final String PLAYLIST_LOAD_TRACKS =
            SlimConstants.SUBSCRIBE_PLAYLIST_RESPONSE_LOAD;
    private static final String PLAYLIST_INSERT_TRACKS =
            SlimConstants.SUBSCRIBE_PLAYLIST_RESPONSE_INSERT;
    private static final String PLAYLIST_SHUFFLE =
            SlimPlaylist.SLIM_PROP_PLAYER_PLAYLIST_SHUFFLE.replaceAll(SlimConstants.CMD_PARAM_PLAYER_ID, "").replaceAll(SlimConstants.CMD_PARAM_MARKER, "").trim();
    private static final String PLAYLIST_CLEARED =
            SlimConstants.SUBSCRIBE_PLAYLIST_RESPONSE_CLEARED;
    private static final String PLAYLIST_NEW_SONG =
            SlimConstants.SUBSCRIBE_PLAYLIST_ITEM_CHANGED;
    private static final String SUFFIX_DATABASE_RESCAN_DONE = SlimConstants.RESPONSE_RESCAN_DONE;
    private static final String PARAM_MARKER = SlimConstants.CMD_PARAM_MARKER;
    private static String CMD_SUBSCRIBE = SlimServer.CMD_SUBSCRIBE;
    private static final String PREFIX_PLAYLIST = SlimConstants.SUBSCRIBE_PLAYLIST;
    private static final String PREFIX_SAVED_PLAYLIST = SlimConstants.SUBSCRIBE_SAVED_PLAYLISTS;
    private static final String PREFIX_PLAYER = SlimConstants.SUBSCRIBE_PLAYER;
    private static final String PLAYER_NEW = SlimConstants.SUBSCRIBE_PLAYER_RESPONSE_NEW;
    private static final String PLAYER_RECONNECT = SlimConstants.SUBSCRIBE_PLAYER_RESPONSE_RECONNECT;
    private static final String PLAYER_DISCONNECT = SlimConstants.SUBSCRIBE_PLAYER_RESPONSE_DISCONNECT;
    private static final String PLAYER_FORGET = SlimConstants.SUBSCRIBE_PLAYER_RESPONSE_FORGET;
    private static final String PREFIX_FAVORITE = SlimConstants.SUBSCRIBE_FAVORITES;
    private static final String FAVORITE_ADDED = SlimConstants.SUBSCRIBE_FAVORITES_RESPONSE_ADD;
    private static final String FAVORITE_DELETED = SlimConstants.SUBSCRIBE_FAVORITES_RESPONSE_DELETE;
    private static final String FAVORITE_MOVED = SlimConstants.SUBSCRIBE_FAVORITES_RESPONSE_MOVE;
    private static final String FAVORITE_ADDED_LEVEL = SlimConstants.SUBSCRIBE_FAVORITES_RESPONSE_ADD_LEVEL;
    private static final String[] SUBSCRIBE_LIST = {
        PREFIX_PLAYER_PAUSE,
        PREFIX_PLAYER_PLAY,
        PREFIX_PLAYER_STOP,
        PREFIX_PLAYER_SYNC,
        PREFIX_PLAYLIST,
        PREFIX_DATABASE_RESCAN,
        PREFIX_SAVED_PLAYLIST,
        PREFIX_PLAYER,
        PREFIX_PLAYER_POWER,
        PREFIX_PLAYER_SLEEP,
        PREFIX_FAVORITE,
        SlimConstants.SUBSCRIBE_PLAYER_MIXER
    };
    private List<PlayerChangeListener> playerListeners =
            new ArrayList<PlayerChangeListener>();
    private List<PlaylistChangeListener> playlistListeners =
            new ArrayList<PlaylistChangeListener>();
    private List<ConnectionChangeListener> connectionListeners =
            new ArrayList<ConnectionChangeListener>();
    private List<VolumeChangeListener> volListeners =
            new ArrayList<VolumeChangeListener>();
    private List<DatabaseScanListener> scanListeners =
            new ArrayList<DatabaseScanListener>();
    private List<SavedPlaylistChangeListener> savedPlaylistListeners =
            new ArrayList<SavedPlaylistChangeListener>();
    private List<SleepChangeListener> sleepListeners =
            new ArrayList<SleepChangeListener>();
    private List<FavoriteChangeListener> favoritesListeners =
            new ArrayList<FavoriteChangeListener>();

    /**
     * Creates a new SlimEventListener instance.  To activate the listener call the {@link  listen()} method.
     * @param player
     * @throws IOException
     */
    public SlimEventListener(SlimPlayer player) {
        StringBuilder commandList = new StringBuilder();
        for (int i = 0; i < SUBSCRIBE_LIST.length; i++) {
            commandList.append(SUBSCRIBE_LIST[i]);
            if (i < SUBSCRIBE_LIST.length - 1) {
                commandList.append(",");
            }
        }
        CMD_SUBSCRIBE = CMD_SUBSCRIBE.replace(PARAM_MARKER, commandList.toString());
        try {
            setPlayer(player);
        } catch (IOException ex) {
            Logger.getLogger(SlimEventListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SlimException ex) {
            Logger.getLogger(SlimEventListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates a connection on a new socket.
     */
    private void initialize() throws IOException, SlimException {
        this.setSocket(new Socket(getServerAddress(), getServerPort()));
        sendCommand(new SlimCommand(CMD_SUBSCRIBE));
    }

    public void stop() {
        setRunning(false);
        try {
            getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(SlimEventListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void sendCommand(SlimCommand command) throws SlimConnectionException {
        byte[] bytesToSend = null;
        //List<String> responseList = new ArrayList<String>();
        OutputStream outStream = null;
        BufferedReader in = null;

        if (!socket.isConnected()) {
            try {
                initialize();
            } catch (Exception e) {
                throw new SlimConnectionException("Connection to server lost: " + e.getMessage());
            }
        }

        try {
            String cmd = convertCommand(command.getCommand(), command.getParams());

            bytesToSend = cmd.getBytes(getPlayer().getSlimServer().getEncoding());
            outStream = getSocket().getOutputStream();
            outStream.write(bytesToSend);
            in = new BufferedReader(new InputStreamReader(getSocket().getInputStream(), getPlayer().getSlimServer().getEncoding()));

            in.readLine();

        } catch (Exception e) {
            e.printStackTrace();

            int count = 0;

            while (count < 2) {
                try {
                    wait(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SlimServer.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    initialize();
                    break;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ++count;
                }
            }
            throw new SlimConnectionException(e.getMessage());
        }
    }

    private String convertCommand(String command, List<String> params) {
        StringBuilder sb = new StringBuilder(command);
        if (params != null && params.size() > 0) {
            for (String param : params) {
                if (param != null) {
                    sb.replace(sb.indexOf(PARAM_MARKER),
                            sb.indexOf(PARAM_MARKER) + PARAM_MARKER.length(),
                            param);
                }
            }
        }
        sb.append("\n");

        return (sb.toString());
    }

    /**
     * @return the player
     */
    protected SlimPlayer getPlayer() {
        return player;
    }

    /**
     * @param player the player to set
     */
    protected void setPlayer(SlimPlayer player) throws IOException, SlimException {
        this.player = player;
        setSlimServer(getPlayer().getSlimServer());
        setServerAddress(getPlayer().getSlimServer().getServerAddress());
        setServerPort(getPlayer().getSlimServer().getServerPort());

        this.setDatabase(new SlimDatabase(player.getSlimServer()));
        this.setPlaylist(new SlimPlaylist(player));
        this.setPlaylistManager(new SlimSavedPlaylistManager(player.getSlimServer()));
        this.setFavoritePlugin(new SlimFavoritePlugin(player.getSlimServer()));

        initialize();
    }

    /**
     * @return the serverAddress
     */
    public InetAddress getServerAddress() {
        return serverAddress;
    }

    /**
     * @param serverAddress the serverAddress to set
     */
    private void setServerAddress(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * @return the serverPort
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * @param serverPort the serverPort to set
     */
    private void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * @return the running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @param running the running to set
     */
    private void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * @return the socket
     */
    private Socket getSocket() {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    private void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * This method <b>MUST</b> be called to start the listener.
     */
    public void start() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    initialize();
                    BufferedReader in = new BufferedReader(new InputStreamReader(getSocket().getInputStream(), getPlayer().getSlimServer().getEncoding()));
                    setRunning(true);
                    while (isRunning()) {
                        try {
                            String inLine = Utils.decode(in.readLine(), SlimServer.getEncoding()).trim();
                            if (inLine.startsWith(PREFIX_SAVED_PLAYLIST)) {
                                processSavedPlaylistEvent(inLine);
                            } else if (inLine.contains(PREFIX_PLAYER_SLEEP)) {
                                processSleepEvent(inLine);
                            } else if (inLine.contains(PREFIX_PLAYER)) {
                                processClientEvent(inLine);
                            } else if (inLine.contains(PREFIX_FAVORITE)) {
                                processFavoriteEvent(inLine);
                            } else if (inLine.startsWith(getPlayer().getId())) {
                                inLine = inLine.replaceFirst(getPlayer().getId(), "").trim();
                                if (inLine.startsWith(PREFIX_PLAYLIST)) {
                                    processPlaylistEvent(inLine);
                                } else {
                                    processPlayerEvent(inLine);
                                }
                            } else if (inLine.equalsIgnoreCase(PREFIX_DATABASE_RESCAN)) {
                                fireDatabaseScanEvent(DatabaseScanEvent.SCAN_STARTED);
                            } else if (inLine.startsWith(PREFIX_DATABASE_RESCAN) && inLine.endsWith(SUFFIX_DATABASE_RESCAN_DONE)) {
                                fireDatabaseScanEvent(DatabaseScanEvent.SCAN_ENDED);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //if (e instanceof SlimConnectionException) {
                            //setConnectedState(false);
                            fireConnectionChangeEvent(false);
                            boolean retry = true;
                            while (retry) {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(SlimStandAloneMonitor.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                try {
                                    initialize();
                                    in = new BufferedReader(new InputStreamReader(getSocket().getInputStream(), getPlayer().getSlimServer().getEncoding()));
                                } catch (Exception ex) {
                                    Logger.getLogger(SlimStandAloneMonitor.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                if (getSlimServer().isConnected()) {
                                    retry = false;
                                    fireConnectionChangeEvent(true);
                                }
                            }
                            //                    } else {
                            //                        //fireSlimErrorEvent(e.getMessage());
                            //                    }
                            //                    } else {
                            //                        //fireSlimErrorEvent(e.getMessage());
                            //                    }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SlimEventListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SlimException ex) {
                    Logger.getLogger(SlimEventListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

    }

    private void processRepeatEvent(int id) {
        switch (id) {
            case 0:
                firePlaylistChangeEvent(PlaylistChangeEvent.REPEAT_OFF, null);
                break;
            case 1:
                firePlaylistChangeEvent(PlaylistChangeEvent.REPEAT_ITEM, null);
                break;
            case 2:
                firePlaylistChangeEvent(PlaylistChangeEvent.REPEAT_PLAYLIST, null);
                break;
        }
    }

    private void processShuffleEvent(int id) {
        switch (id) {
            case 0:
                firePlaylistChangeEvent(PlaylistChangeEvent.SHUFFLE_OFF, null);
                break;
            case 1:
                firePlaylistChangeEvent(PlaylistChangeEvent.SHUFFLE_ITEMS, null);
                break;
            case 2:
                firePlaylistChangeEvent(PlaylistChangeEvent.SHUFFLE_ALBUMS, null);
                break;
        }
    }

    private void processPlaylistEvent(String inLine) throws SlimException {
        if (inLine.startsWith(PREFIX_PLAYLIST_REPEAT)) {
            processRepeatEvent(Integer.parseInt(inLine.replace(PREFIX_PLAYLIST_REPEAT, "").trim()));
        } else if (inLine.contains(PLAYLIST_ADD_TRACKS) || inLine.contains(PLAYLIST_ADD)) {
            firePlaylistChangeEvent(PlaylistChangeEvent.ITEM_ADDED, null, getPlaylist().getItemCount());
        } else if (inLine.contains(PLAYLIST_DELETE_TRACKS)) {
            firePlaylistChangeEvent(PlaylistChangeEvent.ITEM_DELETED, null, getPlaylist().getItemCount());
        } else if (inLine.contains(PLAYLIST_LOAD_TRACKS)) {
            firePlaylistChangeEvent(PlaylistChangeEvent.ITEM_LOADED, null, getPlaylist().getItemCount());
        } else if (inLine.contains(PLAYLIST_INSERT_TRACKS)) {
            firePlaylistChangeEvent(PlaylistChangeEvent.ITEM_INSERTED, null, getPlaylist().getItemCount());
        } else if (inLine.contains(PLAYLIST_CLEARED)) {
            firePlaylistChangeEvent(PlaylistChangeEvent.PLAYLIST_CLEARED, null, 0);
        } else if (inLine.contains(PLAYLIST_NEW_SONG)) {
            firePlaylistChangeEvent(PlaylistChangeEvent.ITEM_CHANGED, null, 0);
        } else if (inLine.contains(PREFIX_PLAYLIST_SAVE)) {
            firePlaylistChangeEvent(PlaylistChangeEvent.PLAYLIST_SAVED, null, 0);
        } else if (inLine.contains(PLAYLIST_SHUFFLE)) {
            processShuffleEvent(Integer.parseInt(inLine.replace(PLAYLIST_SHUFFLE, "").trim()));
        }
    }

    private void processSleepEvent(String inLine) throws SlimException {
        SlimPlayer pl = getSlimServer().getSlimPlayer(inLine.replaceAll(PREFIX_PLAYER_SLEEP + ".*$", "").trim());
        if (inLine.endsWith(PREFIX_PLAYER_SLEEP + " " + OFF)) {
            fireSleepChangeEvent(pl, SleepChangeEvent.SLEEP_STOPPED);
        } else {
            fireSleepChangeEvent(pl, SleepChangeEvent.SLEEP_STARTED);
        }
    }

    private void processClientEvent(String inLine) throws SlimException {
        if (inLine.contains(PLAYER_NEW) || inLine.contains(PLAYER_RECONNECT)) {
            firePlayerChangeEvent(PlayerChangeEvent.PLAYER_ADDED);
        } else if (inLine.contains(PLAYER_DISCONNECT)) {
            firePlayerChangeEvent(PlayerChangeEvent.PLAYER_DELETED);
        }
    }

    private void processFavoriteEvent(String inLine) throws SlimException {
        if (inLine.contains(FAVORITE_ADDED)) {
            fireFavoriteChangeEvent(FavoriteChangeEvent.FAVORITE_ADDED_REMOTELY);
        } else if (inLine.contains(FAVORITE_DELETED)) {
            fireFavoriteChangeEvent(FavoriteChangeEvent.FAVORITE_REMOVED_REMOTELY);
        } else if (inLine.contains(FAVORITE_MOVED)) {
            fireFavoriteChangeEvent(FavoriteChangeEvent.FAVORITE_MOVED);
        } else if (inLine.contains(FAVORITE_ADDED_LEVEL)) {
            fireFavoriteChangeEvent(FavoriteChangeEvent.FOLDER_ADDED);
        }
    }

    private void processSavedPlaylistEvent(String inLine) throws SlimConnectionException {
        if (inLine.startsWith(PREFIX_SAVED_PLAYLIST_NEW)) {
            fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.PLAYLIST_ADDED, null);
        } else if (inLine.startsWith(PREFIX_SAVED_PLAYLIST_DELETE)) {
            fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.PLAYLIST_DELETED, null);
        } else if (inLine.startsWith(PREFIX_SAVED_PLAYLIST_ITEM_ADDED)) {
            fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.ITEM_ADDED, null);
        } else if (inLine.startsWith(PREFIX_SAVED_PLAYLIST_ITEM_DELETED)) {
            fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.ITEM_DELETED, null);
        } else if (inLine.startsWith(PREFIX_SAVED_PLAYLIST_RENAME)) {
            fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.PLAYLIST_RENAMED, null);
        } else if (inLine.startsWith(PREFIX_SAVED_PLAYLIST_ITEM_UP)) {
            fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.ITEM_MOVED, null);
        } else if (inLine.startsWith(PREFIX_SAVED_PLAYLIST_ITEM_DOWN)) {
            fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.ITEM_MOVED, null);
        } else if (inLine.startsWith(PREFIX_SAVED_PLAYLIST_ITEM_MOVED)) {
            fireSavedPlaylistChangeEvent(SavedPlaylistChangeEvent.ITEM_MOVED, null);
        }
    }

    private void processPlayerEvent(String inLine) throws SlimConnectionException {
        if (inLine.startsWith(PREFIX_PLAYER_PAUSE)) {
            switch (getPlayer().getMode()) {
                case STATUS_PAUSED:
                    firePlayerChangeEvent(PlayerChangeEvent.PLAYER_PAUSED);
                    break;
                case STATUS_PLAYING:
                    firePlayerChangeEvent(PlayerChangeEvent.PLAYER_UNPAUSED);
                    break;
                default:
                    firePlayerChangeEvent(PlayerChangeEvent.PLAYER_PAUSED);
                    break;
            }

        } else if (inLine.startsWith(PREFIX_PLAYER_PLAY)) {
            firePlayerChangeEvent(PlayerChangeEvent.PLAYER_STARTED);
        } else if (inLine.startsWith(PREFIX_PLAYER_STOP)) {
            firePlayerChangeEvent(PlayerChangeEvent.PLAYER_STOPPED);
        } else if (inLine.startsWith(PREFIX_PLAYER_SYNC) && !inLine.endsWith("-")) {
            firePlayerChangeEvent(PlayerChangeEvent.PLAYER_SYNCED);
        } else if (inLine.startsWith(PREFIX_PLAYER_SYNC) && inLine.endsWith("-")) {
            firePlayerChangeEvent(PlayerChangeEvent.PLAYER_UNSYNCED);
        } else if (inLine.startsWith(PREFIX_PLAYER_POWER)) {
            if (inLine.endsWith(ON)) {
                firePlayerChangeEvent(PlayerChangeEvent.PLAYER_ON);
            } else if (inLine.endsWith(OFF)) {
                firePlayerChangeEvent(PlayerChangeEvent.PLAYER_OFF);
            }
        } else if (inLine.startsWith(PREFIX_PLAYER_VOLUME)) {
            fireVolumeChangeEvent(getPlayer().getVolume());
        }
    }

    /**
     * Adds a {@link PlaylistChangeListener} to this object to receive
     * {@link PlaylistChangeEvent}s.
     * @param pcl the PlaylistChangeListener to add
     */
    public synchronized void addPlaylistChangeListener(PlaylistChangeListener pcl) {
        playlistListeners.add(pcl);
    }

    /**
     * Removes a {@link PlaylistChangeListener} from this object.
     * @param pcl the PlaylistChangeListener to remove
     */
    public synchronized void removePlaylistChangeListener(PlaylistChangeListener pcl) {
        playlistListeners.remove(pcl);
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     * @param id the event id to send
     */
    protected synchronized void firePlaylistChangeEvent(int id, SlimPlaylistItem object) {
        firePlaylistChangeEvent(id, object, 0);
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     * @param id the event id to send
     */
    protected synchronized void firePlaylistChangeEvent(int id, SlimPlaylistItem object, int itemCount) {
        PlaylistChangeEvent pce = new PlaylistChangeEvent(this, id, object, null, null);

        for (PlaylistChangeListener pcl : playlistListeners) {
            pcl.playlistChanged(pce);
        }
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     * @param id the event id to send
     * @param playlist the list of {@link SlimPlaylistObject}s
     */
    protected synchronized void firePlaylistChangeEvent(int id, SlimPlayableObject object, List<SlimPlaylistItem> playlist) {
        PlaylistChangeEvent pce = new PlaylistChangeEvent(this, id, object, new ArrayList<SlimPlaylistItem>(playlist), null);

        for (PlaylistChangeListener pcl : playlistListeners) {
            pcl.playlistChanged(pce);
        }
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
     * Sends the appropriate {@link ConnectionChangeEvent} to all registered
     * {@link ConnectionChangeListener}s.
     * @param isConnected the connection status
     */
    protected synchronized void fireConnectionChangeEvent(boolean isConnected) {
        ConnectionChangeEvent cce = new ConnectionChangeEvent(this, isConnected);
        for (ConnectionChangeListener ccl : connectionListeners) {
            ccl.connectionChangeEventReceived(cce);
        }
    }

    /**
     * Adds a {@link PlayerChangeListener} to this object to receive
     * {@link PlayerChangeEvent}s.
     * @param pcl the PlayerChangeListener to add
     */
    public synchronized void addPlayerChangeListener(PlayerChangeListener pcl) {
        playerListeners.add(pcl);
    }

    /**
     * Removes a {@link PlayerChangeListener} from this object.
     * @param pcl the PlayerChangeListener to remove
     */
    public synchronized void removePlayerChangeListener(PlayerChangeListener pcl) {
        playerListeners.remove(pcl);
    }

    /**
     * Sends the appropriate {@link PlayerChangeEvent} to all registered
     * {@link PlayerChangeListener}s.
     * @param id the event id to send
     */
    protected synchronized void firePlayerChangeEvent(int id) {
        PlayerChangeEvent pce = new PlayerChangeEvent(this, id);

        for (PlayerChangeListener pcl : playerListeners) {
            pcl.playerChanged(pce);
        }
    }

    /**
     * Adds a {@link VolumeChangeListener} to this object to receive
     * {@link VolumeChangeEvent}s.
     * @param vcl the VolumeChangeListener to add
     */
    public synchronized void addVolumeChangeListener(VolumeChangeListener vcl) {
        volListeners.add(vcl);
    }

    /**
     * Removes a {@link VolumeChangeListener} from this object.
     * @param vcl the VolumeChangeListener to remove
     */
    public synchronized void removeVolumeChangedListener(VolumeChangeListener vcl) {
        volListeners.remove(vcl);
    }

    /**
     * Sends the appropriate {@link VolumeChangeEvent} to all registered
     * {@link VolumeChangeListener}.
     * @param volume the new volume
     */
    protected synchronized void fireVolumeChangeEvent(int volume) {
        VolumeChangeEvent vce = new VolumeChangeEvent(this, volume);

        for (VolumeChangeListener vcl : volListeners) {
            vcl.volumeChanged(vce);
        }
    }

    /**
     * Adds a {@link ConnectionChangeListener} to this object to receive
     * {@link ConnectionChangeEvent}s.
     * @param ccl the ConnectionChangeListerner to add
     */
    public synchronized void addDatabaseScanListener(DatabaseScanListener dsl) {
        scanListeners.add(dsl);
    }

    /**
     * Removes a {@link ConnectionChangeListener} from this object.
     * @param ccl the ConnectionChangeListener to remove
     */
    public synchronized void removeDatabaseScanListener(DatabaseScanListener dsl) {
        scanListeners.remove(dsl);
    }

    /**
     * Sends the appropriate {@link ConnectionChangeEvent} to all registered
     * {@link ConnectionChangeListener}s.
     * @param isConnected the connection status
     */
    protected synchronized void fireDatabaseScanEvent(int id) {
        DatabaseScanEvent dse = new DatabaseScanEvent(this, id);

        for (DatabaseScanListener dsl : scanListeners) {
            dsl.databaseScanEventReceived(dse);
        }
    }

    /**
     * Adds a {@link PlayerChangeListener} to this object to receive
     * {@link PlayerChangeEvent}s.
     * @param pcl the PlayerChangeListener to add
     */
    public synchronized void addSleepChangeListener(SleepChangeListener scl) {
        sleepListeners.add(scl);
    }

    /**
     * Removes a {@link PlayerChangeListener} from this object.
     * @param pcl the PlayerChangeListener to remove
     */
    public synchronized void removeSleepChangeListener(SleepChangeListener scl) {
        sleepListeners.remove(scl);
    }

    protected synchronized void fireSleepChangeEvent(SlimPlayer p, int id) {
        SleepChangeEvent sce = new SleepChangeEvent(this, p, id);

        for (SleepChangeListener scl : sleepListeners) {
            scl.sleepTimeChanged(sce);
        }
    }

    /**
     * Adds a {@link PlayerChangeListener} to this object to receive
     * {@link PlayerChangeEvent}s.
     * @param pcl the PlayerChangeListener to add
     */
    public synchronized void addFavoritesChangeListener(FavoriteChangeListener fcl) {
        favoritesListeners.add(fcl);
    }

    /**
     * Removes a {@link PlayerChangeListener} from this object.
     * @param pcl the PlayerChangeListener to remove
     */
    public synchronized void removeFavoritesChangeListener(FavoriteChangeListener fcl) {
        favoritesListeners.remove(fcl);
    }

    protected synchronized void fireFavoriteChangeEvent(int id) {
        FavoriteChangeEvent fce = new FavoriteChangeEvent(this, id);

        for (FavoriteChangeListener fcl : favoritesListeners) {
            fcl.favoritesChanged(fce);
        }
    }

    /**
     * @return the playlist
     */
    public SlimPlaylist getPlaylist() {
        return playlist;
    }

    /**
     * @param playlist the playlist to set
     */
    public void setPlaylist(SlimPlaylist playlist) {
        this.playlist = playlist;
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
     * @return the favoritePlugin
     */
    public SlimFavoritePlugin getFavoritePlugin() {
        return favoritePlugin;
    }

    /**
     * @param favoritePlugin the favoritePlugin to set
     */
    public void setFavoritePlugin(SlimFavoritePlugin favoritePlugin) {
        this.favoritePlugin = favoritePlugin;
    }

    /**
     * @return the playlistManager
     */
    public SlimSavedPlaylistManager getPlaylistManager() {
        return playlistManager;
    }

    /**
     * @param playlistManager the playlistManager to set
     */
    public void setPlaylistManager(SlimSavedPlaylistManager playlistManager) {
        this.playlistManager = playlistManager;
    }
}
