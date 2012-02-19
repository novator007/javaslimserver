/*
 * SqueezeServer.java
 *
 * Created on October 15, 2007, 10:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

import org.bff.squeezeserver.comm.Communicator;
import org.bff.squeezeserver.comm.CommunicatorCLI;
import org.bff.squeezeserver.domain.*;
import org.bff.squeezeserver.exception.ConnectionException;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the main communication object for all squeezeserver activities.
 *
 * @author bfindeisen
 * @version 1.0
 */
public class SqueezeServer {

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
    private static String CMD_VERSION;
    /**
     * Command to listen on a squeeze connection
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
    private static final int DEFAULT_CLI_PORT = Constants.DEFAULT_CLI_PORT;
    protected static final int DEFAULT_WEB_PORT = Constants.DEFAULT_WEB_PORT;
    /**
     * Results defaults
     */
    private static final String RESULTS_START = Integer.toString(Constants.RESULTS_START);
    private static final String RESULTS_MAX = Integer.toString(Constants.RESULTS_MAX);
    private static final String RESULT_TRUE = Constants.RESULT_TRUE;
    /**
     * Result Prefixes
     */
    private static final String PREFIX_SYNC_MEMBERS = Constants.RESPONSE_PREFIX_SYNC_MEMBERS;
    private static final String PREFIX_SYNC_DELIM = Constants.RESPONSE_PREFIX_SYNC_DELIM;
    /**
     * Command parameters
     */
    private static final String PARAM_START = Constants.CMD_PARAM_START;
    private static final String PARAM_ITEMS_RESPONSE = Constants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = Constants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PARAM_REQUEST = Constants.CMD_PARAM_REQUEST_TERMS;
    private static final String PARAM_PLAYER_ID = Constants.CMD_PARAM_PLAYER_ID;
    private static final String PARAM_MARKER = Constants.CMD_PARAM_MARKER;
    private static final String PARAM_PREF_NAME = Constants.CMD_PARAM_PREF_NAME;
    /**
     * Grouping responses
     */
    private static final String RESULT_GROUP_SINGLE = Constants.RESULT_GROUP_SINGLE;
    private static final String RESULT_GROUP_MULTIPLE = Constants.RESULT_GROUP_MULTIPLE;

    private Communicator communicator;
    private String version;

    static {
        loadProperties();
    }

    protected SqueezeServer() {

    }

    /**
     * Creates a new instance of SqueezeServer
     */
    public SqueezeServer(String server) throws ConnectionException {
        this(server, DEFAULT_CLI_PORT, DEFAULT_WEB_PORT);
    }

    public SqueezeServer(String server, int webPort) throws ConnectionException {
        this(server, DEFAULT_CLI_PORT, webPort, null, null);
    }

    public SqueezeServer(String server, int cliPort, int webPort) throws ConnectionException {
        this(server, cliPort, webPort, null, null);
    }

    public SqueezeServer(String server, String user, String password) throws ConnectionException {
        this(server, DEFAULT_CLI_PORT, user, password);
    }

    public SqueezeServer(String server, int cliPort, String user, String password) throws ConnectionException {
        this(server, cliPort, DEFAULT_WEB_PORT, user, password);
    }

    public SqueezeServer(String server,
                         int cliPort,
                         int webPort,
                         String user,
                         String password) throws ConnectionException {
        try {
            this.serverAddress = InetAddress.getByName(server);
            this.serverPort = cliPort;
            this.webPort = webPort;
            this.server = server;
            this.communicator = new CommunicatorCLI(server, cliPort, user, password);

        } catch (Exception e) {
            throw new ConnectionException(e);
        }
    }

    private static void loadProperties() {
        prop = new Properties();

        InputStream is = SqueezeServer.class.getResourceAsStream(Constants.PROP_FILE);

        try {
            prop.load(is);
            CMD_STATUS = prop.getProperty(Constants.PROP_CMD_STATUS);
            CMD_EXIT = prop.getProperty(Constants.PROP_CMD_EXIT);
            CMD_PREF_QUERY = prop.getProperty(Constants.PROP_CMD_PREF_QUERY);
            CMD_PREF = prop.getProperty(Constants.PROP_CMD_PREF);
            CMD_CAN = prop.getProperty(Constants.PROP_CMD_CAN);
            CMD_PLAYER_COUNT = prop.getProperty(Constants.PROP_CMD_PLAYER_COUNT);
            CMD_PLAYER_QUERY = prop.getProperty(Constants.PROP_CMD_PLAYER_QUERY);
            PREF_GROUP_DISCS = prop.getProperty(Constants.PROP_PREF_GROUP_DISCS);
            PREF_MEDIA_DIRS = prop.getProperty(Constants.PROP_PREF_MEDIA_DIRS);
            PREF_PLAYLIST_DIR = prop.getProperty(Constants.PROP_PREF_PLAYLIST_DIR);
            CMD_PODCAST_TEST = prop.getProperty(Constants.PROP_CMD_PODCAST_TEST);
            CMD_LISTEN = prop.getProperty(Constants.PROP_CMD_LISTEN);
            CMD_SUBSCRIBE = prop.getProperty(Constants.PROP_CMD_SUBSCRIBE);
            CMD_SYNC_GROUPS = prop.getProperty(Constants.PROP_CMD_SYNC_GROUPS);
            CMD_VERSION = prop.getProperty(Constants.PROP_CMD_VERSION);
            encoding = prop.getProperty(Constants.PROP_SERVER_ENCODING, Constants.DEFAULT_ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(SqueezeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Playlist getPlaylist(Player player) {
        return new Playlist(player);
    }

    public void getStatus() {
        String cmd = CMD_STATUS;
        cmd = cmd.replaceFirst(PARAM_START, RESULTS_START);
        cmd = cmd.replaceFirst(PARAM_ITEMS_RESPONSE, RESULTS_MAX);
        cmd = cmd.replaceFirst(PARAM_TAGS, "");

        try {
            String[] response = sendCommand(new Command(cmd));
            for (int i = 0; i < response.length; i++) {
                System.out.println(response[i]);
            }

        } catch (ConnectionException ex) {
            Logger.getLogger(SqueezeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the raw disc grouping as returned by SqueezeServer.  Check out the
     * convenience method {@link #isDiscsGroupedAsSingle}.
     *
     * @return 1 if discs are grouped as a singe disc, 0 if multiple
     */
    public String getDiscGrouping() throws ConnectionException {
        String cmd = CMD_PREF_QUERY;
        cmd = cmd.replaceFirst(PARAM_PREF_NAME, PREF_GROUP_DISCS);

        String retString = RESULT_GROUP_MULTIPLE;

        String[] response = sendCommand(new Command(cmd));
        retString = response[0];

        return retString;
    }

    /**
     * Sets whether to group multiple disc sets.  Pass true to group
     * together, false to list albums them separately.
     *
     * @param grouping true to group, false otherwise
     */
    public void setDiscGrouping(boolean grouping) throws ConnectionException {
        String cmd = CMD_PREF;
        cmd = cmd.replaceFirst(PARAM_PREF_NAME, PREF_GROUP_DISCS);
        cmd = cmd.replaceFirst(PARAM_MARKER, grouping ? RESULT_GROUP_SINGLE : RESULT_GROUP_MULTIPLE);

        sendCommand(new Command(cmd));
    }

    /**
     * Returns the media directories for this SqueezeServer
     *
     * @return a collection of the media directories
     */
    public Collection<String> getMediaDirectories() throws ConnectionException {
        String cmd = CMD_PREF_QUERY;
        cmd = cmd.replaceFirst(PARAM_PREF_NAME, PREF_MEDIA_DIRS);

        List<String> mediaDirs = new ArrayList<String>();
        String[] response = sendCommand(new Command(cmd));
        if (response.length > 0) {
            mediaDirs.addAll(Arrays.asList(response[0].split(",")));
        }

        return mediaDirs;
    }

    /**
     * Returns the playlist directory for this SqueezeServer
     *
     * @return the playlist directory
     */
    public String getPlaylistDirectory() throws ConnectionException {
        String cmd = CMD_PREF_QUERY;
        cmd = cmd.replaceFirst(PARAM_PREF_NAME, PREF_PLAYLIST_DIR);

        String retString = "";

        String[] response = sendCommand(new Command(cmd));
        retString = response[0];

        return retString;
    }

    /**
     * Returns true if multi discs are grouped as 1 disc.
     *
     * @return true is multi discs are grouped as 1 disc
     */
    public boolean isDiscsGroupedAsSingle() throws ConnectionException {
        return RESULT_GROUP_SINGLE.equalsIgnoreCase(getDiscGrouping());
    }

    public Collection<Player> getAllPlayers() {
        List<Player> playerList = new ArrayList<Player>();
        try {
            int playerCount = Integer.parseInt(sendCommand(
                    new Command(CMD_PLAYER_COUNT))[0]);
            for (int i = 0; i < playerCount; i++) {
                playerList.add(getPlayer(i));
            }

        } catch (ConnectionException ex) {
            ex.printStackTrace();
        }

        return (playerList);
    }

    /**
     * Returns the {@link Player} for the requested id.  Returns
     * {@code null} is there is no player matching the id.
     *
     * @param id
     * @return the matching {@Player}
     */
    public Player getPlayer(String id) {
        try {
            int playerCount = Integer.parseInt(sendCommand(
                    new Command(CMD_PLAYER_COUNT))[0]);
            for (int i = 0; i < playerCount; i++) {
                Player pl = getPlayer(i);
                if (pl.getId().equalsIgnoreCase(id)) {
                    return pl;
                }
            }
        } catch (ConnectionException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void close() throws ConnectionException {
        sendCommand(CMD_EXIT);
    }

    public synchronized boolean isConnected() {
        return getCommunicator().isConnected();
    }

    /**
     * Returns the player sync groups.  The first item is
     * the player id and the second is the player name.
     *
     * @return
     * @throws org.bff.squeezeserver.exception.ConnectionException
     *
     */
    public Collection<Player> getPlayerSyncGroups() throws ConnectionException {
        String command = CMD_SYNC_GROUPS;
        List<Player> names = new ArrayList<Player>();

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
                names.add(new Player(strId, this));
            }
        }

        return names;
    }

    protected PlayableItem convertResponse(String[] response) {
        return convertResponse(null, response);
    }

    /**
     * Send an array containing the response and get back a PlayableItem
     *
     * @param item     the item to fill, passing null will result in a new {@link org.bff.squeezeserver.domain.PlayableItem}
     *                 being created and returned;
     * @param response the {@link SqueezeServer} response
     * @return the {@link org.bff.squeezeserver.domain.PlayableItem} for the response
     */
    protected PlayableItem convertResponse(PlayableItem item, String[] response) {
        if (item == null) {
            item = new PlayableItem();
        }

        Album album = null;
        if (item instanceof Album) {
            album = (Album) item;
        } else {
            album = new Album();
        }

        Artist artist = null;
        if (item instanceof Artist) {
            artist = (Artist) item;
        } else {
            artist = new Artist();
        }

        //@TODO Implement contributor and track

        Genre genre = null;
        if (item instanceof Genre) {
            genre = (Genre) item;
        } else {
            genre = new Genre();
        }

        for (String s : response) {

            if (s == null) {
                continue;
            }

            if (PlayableTags.ID_MAP.get(s.split(":")[0]) == null) {
                continue;
            }

            String param = s.replace(PlayableTags.ID_MAP.get(s.split(":")[0]).getPrefix() + ":", "");

            switch (PlayableTags.ID_MAP.get(s.split(":")[0])) {
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
                        Logger.getLogger(SqueezeServer.class.getName()).log(Level.SEVERE, null, ex);
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

        if (!(item instanceof Genre) && genre.getId() != null) {
            item.setGenre(genre);
        }

        if (item.getImageUrl() == null) {
            try {
                String url = Constants.IMAGE_URL;
                url = url.replaceFirst(Constants.IMAGE_SERVER, getServer() == null ? "" : getServer());
                url = url.replaceFirst(Constants.IMAGE_PORT, Integer.toString(getWebPort()));
                if (item instanceof Album && album.getArtworkTrackId() != null) {
                    url = url.replaceFirst(Constants.IMAGE_ID, album.getArtworkTrackId());
                } else {
                    url = url.replaceFirst(Constants.IMAGE_ID, item.getId());
                }
                item.setImageUrl(new URL(url));
            } catch (MalformedURLException ex) {
                Logger.getLogger(SqueezeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return item;
    }

    /**
     * Returns the properties file containing the command list for the SqueezeServer.
     *
     * @return the command list properties file
     */
    static Properties getProperties() {
        return (prop);
    }

    /**
     * Returns trus if podcasts are currently supported by this SqueezeServer
     *
     * @return true if podcasts are supported, false if not
     * @throws org.bff.squeezeserver.exception.ConnectionException
     *
     */
    public boolean isPodcastsSupported() throws ConnectionException {
        String command = CMD_CAN;
        command = command.replaceAll(PARAM_REQUEST, CMD_PODCAST_TEST);

        String[] response = sendCommand(command);

        return RESULT_TRUE.equalsIgnoreCase(response[0]);
    }

    /**
     * @param index
     * @return
     */
    private Player getPlayer(int index) {
        Player player = null;

        String command = CMD_PLAYER_QUERY;
        command = command.replaceAll(PARAM_PLAYER_ID, Integer.toString(index));
        try {
            String id = sendCommand(
                    new Command(command))[0].replaceFirst(Integer.toString(index) + " ", "");

            player = new Player(id, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (player);
    }

    public String getVersion() throws ConnectionException {
        return sendCommand(new Command(CMD_VERSION))[0];
    }

    /**
     * @return the communicator
     */
    public Communicator getCommunicator() {
        return communicator;
    }

    public String[] sendCommand(Command command) throws ConnectionException {
        return getCommunicator().sendCommand(command);
    }

    public String[] sendCommand(String command) throws ConnectionException {
        return getCommunicator().sendCommand(command);
    }

    public void sendCommand(String command, String param) throws ConnectionException {
        getCommunicator().sendCommand(command, param);
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

    public void setVersion(String version) {
        this.version = version;
    }
}
