/*
 * SlimServer.java
 *
 * Created on October 15, 2007, 10:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.bff.slimserver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bff.slimserver.musicobjects.SlimGenre;
import org.bff.slimserver.musicobjects.SlimArtist;
import org.bff.slimserver.musicobjects.SlimAlbum;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;

import org.bff.slimserver.comm.SlimCommunicator;
import org.bff.slimserver.comm.SlimCommunicatorCLI;
import org.bff.slimserver.exception.SlimConnectionException;
import org.bff.slimserver.musicobjects.SlimContributor;
import org.bff.slimserver.musicobjects.SlimPlayableItem;
import org.bff.slimserver.musicobjects.SlimPlayableTags;
import org.bff.slimserver.musicobjects.SlimTrack;

/**
 * Represents the main communication object for all slimserver activities.
 *
 * @author bfindeisen
 * @version 1.0
 */
public class SlimServer {

    private InetAddress serverAddress;
    private String server;
    private static Properties prop;
    private int serverPort;
    private int webPort;
    private static String encoding;

    /**
     * Commands
     */
    private static String CMD_STATUS;
    private static String CMD_EXIT;
    private static String CMD_PREF_QUERY;
    private static String CMD_PREF;
    private static String CMD_CAN;
    private static String CMD_PODCAST_TEST;
    private static String CMD_SYNC_GROUPS;
    private static String CMD_PLAYER_COUNT;
    private static String CMD_PLAYER_QUERY;
    /**
     * Command to listen on a slim connection
     */
    public static String CMD_LISTEN;
    public static String CMD_SUBSCRIBE;
    /**
     * Server Preferences
     */
    private static String PREF_GROUP_DISCS;
    private static String PREF_MEDIA_DIRS;
    private static String PREF_PLAYLIST_DIR;
    /**
     * Server Defaults
     */
    private static final int DEFAULT_CLI_PORT = SlimConstants.DEFAULT_CLI_PORT;
    protected static final int DEFAULT_WEB_PORT = SlimConstants.DEFAULT_WEB_PORT;
    /**
     * Results defaults
     */
    private static final String RESULTS_START = Integer.toString(SlimConstants.RESULTS_START);
    private static final String RESULTS_MAX = Integer.toString(SlimConstants.RESULTS_MAX);
    private static final String RESULT_TRUE = SlimConstants.RESULT_TRUE;
    /**
     * Result Prefixes
     */
    private static final String PREFIX_SYNC_MEMBERS = SlimConstants.RESPONSE_PREFIX_SYNC_MEMBERS;
    private static final String PREFIX_SYNC_DELIM = SlimConstants.RESPONSE_PREFIX_SYNC_DELIM;
    /**
     * Command parameters
     */
    private static final String PARAM_START = SlimConstants.CMD_PARAM_START;
    private static final String PARAM_ITEMS_RESPONSE = SlimConstants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = SlimConstants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PARAM_REQUEST = SlimConstants.CMD_PARAM_REQUEST_TERMS;
    private static final String PARAM_PLAYER_ID = SlimConstants.CMD_PARAM_PLAYER_ID;
    private static final String PARAM_MARKER = SlimConstants.CMD_PARAM_MARKER;
    private static final String PARAM_PREF_NAME = SlimConstants.CMD_PARAM_PREF_NAME;
    /**
     * Grouping responses
     */
    private static final String RESULT_GROUP_SINGLE = SlimConstants.RESULT_GROUP_SINGLE;
    private static final String RESULT_GROUP_MULTIPLE = SlimConstants.RESULT_GROUP_MULTIPLE;

    private SlimCommunicator slimCommunicator;

    static {
        loadProperties();
    }

    /**
     * Creates a new instance of SlimServer
     */
    public SlimServer(String server) throws SlimConnectionException {
        this(server, DEFAULT_CLI_PORT, DEFAULT_WEB_PORT);
    }

    public SlimServer(String server, int webPort) throws SlimConnectionException {
        this(server, DEFAULT_CLI_PORT, webPort, null, null);
    }

    public SlimServer(String server, int cliPort, int webPort) throws SlimConnectionException {
        this(server, cliPort, webPort, null, null);
    }

    public SlimServer(String server, String user, String password) throws SlimConnectionException {
        this(server, DEFAULT_CLI_PORT, user, password);
    }

    public SlimServer(String server, int cliPort, String user, String password) throws SlimConnectionException {
        this(server, cliPort, DEFAULT_WEB_PORT, user, password);
    }

    public SlimServer(String server,
                      int cliPort,
                      int webPort,
                      String user,
                      String password) throws SlimConnectionException {
        try {
            this.serverAddress = InetAddress.getByName(server);
            this.serverPort = cliPort;
            this.webPort = webPort;
            this.server = server;
            this.slimCommunicator = new SlimCommunicatorCLI(server, cliPort, user, password);
        } catch (Exception e) {
            throw new SlimConnectionException(e);
        }
    }

    private static void loadProperties() {
        prop = new Properties();

        InputStream is = SlimServer.class.getResourceAsStream(SlimConstants.PROP_FILE);

        try {
            prop.load(is);
            CMD_STATUS = prop.getProperty(SlimConstants.PROP_CMD_STATUS);
            CMD_EXIT = prop.getProperty(SlimConstants.PROP_CMD_EXIT);
            CMD_PREF_QUERY = prop.getProperty(SlimConstants.PROP_CMD_PREF_QUERY);
            CMD_PREF = prop.getProperty(SlimConstants.PROP_CMD_PREF);
            CMD_CAN = prop.getProperty(SlimConstants.PROP_CMD_CAN);
            CMD_PLAYER_COUNT = prop.getProperty(SlimConstants.PROP_CMD_PLAYER_COUNT);
            CMD_PLAYER_QUERY = prop.getProperty(SlimConstants.PROP_CMD_PLAYER_QUERY);
            PREF_GROUP_DISCS = prop.getProperty(SlimConstants.PROP_PREF_GROUP_DISCS);
            PREF_MEDIA_DIRS = prop.getProperty(SlimConstants.PROP_PREF_MEDIA_DIRS);
            PREF_PLAYLIST_DIR = prop.getProperty(SlimConstants.PROP_PREF_PLAYLIST_DIR);
            CMD_PODCAST_TEST = prop.getProperty(SlimConstants.PROP_CMD_PODCAST_TEST);
            CMD_LISTEN = prop.getProperty(SlimConstants.PROP_CMD_LISTEN);
            CMD_SUBSCRIBE = prop.getProperty(SlimConstants.PROP_CMD_SUBSCRIBE);
            CMD_SYNC_GROUPS = prop.getProperty(SlimConstants.PROP_CMD_SYNC_GROUPS);
            encoding = prop.getProperty(SlimConstants.PROP_SERVER_ENCODING, SlimConstants.DEFAULT_ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(SlimServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public SlimPlaylist getSlimPlaylist(SlimPlayer player) {
        return new SlimPlaylist(player);
    }

    public void getStatus() {
        String cmd = CMD_STATUS;
        cmd = cmd.replaceFirst(PARAM_START, RESULTS_START);
        cmd = cmd.replaceFirst(PARAM_ITEMS_RESPONSE, RESULTS_MAX);
        cmd = cmd.replaceFirst(PARAM_TAGS, "");

        try {
            String[] response = sendCommand(new SlimCommand(cmd));
            for (int i = 0; i < response.length; i++) {
                System.out.println(response[i]);
            }

        } catch (SlimConnectionException ex) {
            Logger.getLogger(SlimServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the raw disc grouping as returned by SlimServer.  Check out the
     * convenience method #{@link isDiscGroupedAsSingle}.
     *
     * @return 1 if discs are grouped as a singe disc, 0 if multiple
     */
    public String getDiscGrouping() throws SlimConnectionException {
        String cmd = CMD_PREF_QUERY;
        cmd = cmd.replaceFirst(PARAM_PREF_NAME, PREF_GROUP_DISCS);

        String retString = RESULT_GROUP_MULTIPLE;

        String[] response = sendCommand(new SlimCommand(cmd));
        retString = response[0];

        return retString;
    }

    /**
     * Sets whether to group multiple disc sets.  Pass true to group
     * together, false to list albums them separately.
     *
     * @param grouping true to group, false otherwise
     */
    public void setDiscGrouping(boolean grouping) throws SlimConnectionException {
        String cmd = CMD_PREF;
        cmd = cmd.replaceFirst(PARAM_PREF_NAME, PREF_GROUP_DISCS);
        cmd = cmd.replaceFirst(PARAM_MARKER, grouping ? RESULT_GROUP_SINGLE : RESULT_GROUP_MULTIPLE);

        sendCommand(new SlimCommand(cmd));
    }

    /**
     * Returns the media directories for this SlimServer
     *
     * @return a collection of the media directories
     */
    public Collection<String> getMediaDirectories() throws SlimConnectionException {
        String cmd = CMD_PREF_QUERY;
        cmd = cmd.replaceFirst(PARAM_PREF_NAME, PREF_MEDIA_DIRS);

        List<String> mediaDirs = new ArrayList<String>();
        String[] response = sendCommand(new SlimCommand(cmd));
        if (response.length > 0) {
            mediaDirs.addAll(Arrays.asList(response[0].split(",")));
        }

        return mediaDirs;
    }

    /**
     * Returns the playlist directory for this SlimServer
     *
     * @return the playlist directory
     */
    public String getPlaylistDirectory() throws SlimConnectionException {
        String cmd = CMD_PREF_QUERY;
        cmd = cmd.replaceFirst(PARAM_PREF_NAME, PREF_PLAYLIST_DIR);

        String retString = "";

        String[] response = sendCommand(new SlimCommand(cmd));
        retString = response[0];

        return retString;
    }

    /**
     * Returns true if multi discs are grouped as 1 disc.
     *
     * @return true is multi discs are grouped as 1 disc
     */
    public boolean isDiscsGroupedAsSingle() throws SlimConnectionException {
        return RESULT_GROUP_SINGLE.equalsIgnoreCase(getDiscGrouping());
    }

    public Collection<SlimPlayer> getSlimPlayers() {
        List<SlimPlayer> playerList = new ArrayList<SlimPlayer>();
        try {
            int playerCount = Integer.parseInt(sendCommand(
                    new SlimCommand(CMD_PLAYER_COUNT))[0]);
            for (int i = 0; i < playerCount; i++) {
                playerList.add(getPlayer(i));
            }

        } catch (SlimConnectionException ex) {
            ex.printStackTrace();
        }

        return (playerList);
    }

    /**
     * Returns the {@link SlimPlayer} for the requested id.  Returns
     * {@code null} is there is no player matching the id.
     *
     * @param id
     * @return the matching {@SlimPlayer}
     */
    public SlimPlayer getSlimPlayer(String id) {
        try {
            int playerCount = Integer.parseInt(sendCommand(
                    new SlimCommand(CMD_PLAYER_COUNT))[0]);
            for (int i = 0; i < playerCount; i++) {
                SlimPlayer pl = getPlayer(i);
                if (pl.getId().equalsIgnoreCase(id)) {
                    return pl;
                }
            }
        } catch (SlimConnectionException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void close() throws SlimConnectionException {
        sendCommand(CMD_EXIT);
    }

    public synchronized boolean isConnected() {
        return getSlimCommunicator().isConnected();
    }

    /**
     * Returns the player sync groups.  The first item is
     * the player id and the second is the player name.
     *
     * @return
     * @throws SlimConnectionException
     */
    public Collection<SlimPlayer> getPlayerSyncGroups() throws SlimConnectionException {
        String command = CMD_SYNC_GROUPS;
        List<SlimPlayer> names = new ArrayList<SlimPlayer>();

        String[] response = sendCommand(command);

        if (response != null && response.length > 0) {
            String[] playerIds = null;
            for (int i = 0; i < response.length; i++) {
                if (response[i].startsWith(PREFIX_SYNC_MEMBERS)) {
                    String s = response[i].replaceFirst(PREFIX_SYNC_MEMBERS, "");
                    playerIds = s.split(PREFIX_SYNC_DELIM);
                }
            }

            for (int i = 0; i < playerIds.length; i++) {
                String strId = playerIds[i];
                names.add(new SlimPlayer(strId, this));
            }
        }

        return names;
    }

    protected SlimPlayableItem convertResponse(String[] response) {
        return convertResponse(null, response);
    }

    /**
     * Send an array containing the response and get back a SlimPlayableItem
     *
     * @param item     the item to fill, passing null will result in a new {@link SlimPlayableItem}
     *                 being created and returned;
     * @param response the {@link SlimServer} response
     * @return the {@link SlimPlayableItem} for the response
     */
    protected SlimPlayableItem convertResponse(SlimPlayableItem item, String[] response) {
        if (item == null) {
            item = new SlimPlayableItem();
        }

        SlimAlbum album = null;
        if (item instanceof SlimAlbum) {
            album = (SlimAlbum) item;
        } else {
            album = new SlimAlbum();
        }

        SlimArtist artist = null;
        if (item instanceof SlimArtist) {
            artist = (SlimArtist) item;
        } else {
            artist = new SlimArtist();
        }

        //@TODO Implement contributor and track

        SlimGenre genre = null;
        if (item instanceof SlimGenre) {
            genre = (SlimGenre) item;
        } else {
            genre = new SlimGenre();
        }

        for (String s : response) {

            if (s == null) {
                continue;
            }

            if (SlimPlayableTags.ID_MAP.get(s.split(":")[0]) == null) {
                continue;
            }

            String param = s.replace(SlimPlayableTags.ID_MAP.get(s.split(":")[0]).getPrefix() + ":", "");

            switch (SlimPlayableTags.ID_MAP.get(s.split(":")[0])) {
                case ID:
                    item.setId(param);
                    break;
                case ALBUM_ID:
                    album.setId(param);
                    break;
                case ALBUM:
                    album.setName(param);
                    break;
                case ARTIST_ID:
                    artist.setId(param);
                    break;
                case ARTIST:
                    artist.setName(param);
                    break;
                case CONTRIBUTOR_ID:
                    item.setId(param);
                    break;
                case CONTRIBUTOR:
                    item.setName(param);
                    break;
                case TRACK_ID:
                    item.setId(param);
                    break;
                case TRACK:
                    item.setTrack(Integer.parseInt(param));
                    break;
                case TITLE:
                    item.setName(param);
                    break;
                case BITRATE:
                    item.setBitrate(param);
                    break;
                case GENRE_ID:
                    genre.setId(param);
                    break;
                case GENRE:
                    genre.setName(param);
                    break;
                case COMMENT:
                    item.setComment(param);
                    break;
                case DURATION:
                    item.setLength(Integer.parseInt(param.split("\\.")[0]));
                    break;
                case YEAR:
                    item.setYear(param);
                    break;
                case URL:
                    item.setUrl(param);
                    break;
                case TYPE:
                    item.setType(param);
                    break;
                case REMOTE:
                    item.setRemote(param.equalsIgnoreCase("1") ? true : false);
                    break;
                case RATING:
                    item.setUrl(param);
                    break;
                case ARTWORK_URL:
                    try {
                        item.setImageUrl(new URL(param));
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SlimServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case ARTWORK_TRACK_ID:
                    album.setArtworkTrackId(param);
                    break;
                case COMPILATION:
                    album.setCompilation(param.equalsIgnoreCase("1") ? true : false);
                    break;
                default:
                    break;
            }
        }

        if (item.getId() == null) {
            throw new IllegalArgumentException("Item id must not be null");
        }

        if (album.getId() != null) {
            album.setArtist(artist.getName());
            item.setAlbum(album);
        }

        if (!(item instanceof SlimGenre) && genre.getId() != null) {
            item.setGenre(genre);
        }

        if (item.getImageUrl() == null) {
            try {
                String url = SlimConstants.IMAGE_URL;
                url = url.replaceFirst(SlimConstants.IMAGE_SERVER, getServer());
                url = url.replaceFirst(SlimConstants.IMAGE_PORT, Integer.toString(getWebPort()));
                if (item instanceof SlimAlbum && album.getArtworkTrackId() != null) {
                    url = url.replaceFirst(SlimConstants.IMAGE_ID, album.getArtworkTrackId());
                } else {
                    url = url.replaceFirst(SlimConstants.IMAGE_ID, item.getId());
                }
                item.setImageUrl(new URL(url));
            } catch (MalformedURLException ex) {
                Logger.getLogger(SlimServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return item;
    }

    /**
     * Returns the properties file containing the command list for the SlimServer.
     *
     * @return the command list properties file
     */
    static Properties getSlimProperties() {
        return (prop);
    }

    /**
     * Returns trus if podcasts are currently supported by this SlimServer
     *
     * @return true if podcasts are supported, false if not
     * @throws SlimConnectionException
     */
    public boolean isPodcastsSupported() throws SlimConnectionException {
        String command = CMD_CAN;
        command = command.replaceAll(PARAM_REQUEST, CMD_PODCAST_TEST);

        String[] response = sendCommand(command);

        return RESULT_TRUE.equalsIgnoreCase(response[0]);
    }

    /**
     * @param index
     * @return
     */
    private SlimPlayer getPlayer(int index) {
        SlimPlayer slimPlayer = null;

        String command = CMD_PLAYER_QUERY;
        command = command.replaceAll(PARAM_PLAYER_ID, Integer.toString(index));
        try {
            String id = sendCommand(
                    new SlimCommand(command))[0].replaceFirst(Integer.toString(index) + " ", "");

            slimPlayer = new SlimPlayer(id, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (slimPlayer);
    }

    public String getVersion() {
        return getSlimCommunicator().getVersion();
    }

    /**
     * @return the slimCommunicator
     */
    public SlimCommunicator getSlimCommunicator() {
        return slimCommunicator;
    }

    public String[] sendCommand(SlimCommand slimCommand) throws SlimConnectionException {
        return getSlimCommunicator().sendCommand(slimCommand);
    }

    public String[] sendCommand(String command) throws SlimConnectionException {
        return getSlimCommunicator().sendCommand(command);
    }

    public void sendCommand(String command, String param) throws SlimConnectionException {
        getSlimCommunicator().sendCommand(command, param);
    }

    /**
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * @return the webPort
     */
    public int getWebPort() {
        return webPort;
    }

    /**
     * @return the encoding
     */
    public static String getEncoding() {
        return encoding;
    }

    /**
     * @return the serverAddress
     */
    public InetAddress getServerAddress() {
        return serverAddress;
    }

    /**
     * @return the serverPort
     */
    public int getServerPort() {
        return serverPort;
    }
}
