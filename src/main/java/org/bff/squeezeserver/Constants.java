/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

import org.apache.log4j.Logger;

/**
 * Constants is used to hold SqueezeServer constant values.
 *
 * @author bfindeisen
 */
public class Constants {

    public static final String PROP_FILE = "/org/bff/squeezeserver/slimserver.properties";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final int DEFAULT_CLI_PORT = 9090;
    public static final int DEFAULT_WEB_PORT = 9000;
    /**
     *
     */
    public static final String CMD_PODCAST = "podcast";
    public static final String CMD_APP = "app";
    /**
     * Command params
     */
    public static final String CMD_PARAM_PLAYER_ID = "<playerid>";
    public static final String CMD_PARAM_MARKER = "<param>";
    public static final String CMD_PARAM_SEARCH = "<searchparam>";
    public static final String CMD_PARAM_REQUEST_TERMS = "<request terms>";
    public static final String CMD_PARAM_TAGGED_PARAMS = "<taggedParameters>";
    public static final String CMD_PARAM_ITEMS_RESPONSE = "<itemsPerResponse>";
    public static final String CMD_PARAM_START = "<start>";
    public static final String CMD_PARAM_COMMAND = "<command>";
    public static final String CMD_PARAM_PREF_NAME = "<prefname>";
    public static final String CMD_PARAM_FILENAME = "<filename>";
    /**
     * Response params
     */
    public static final String RESPONSE_PREFIX_RESCAN = "rescan:";
    public static final String RESPONSE_PREFIX_SYNC_MEMBERS = "sync_members:";
    public static final String RESPONSE_PREFIX_SYNC_NAMES = "sync_member_names:";
    public static final String RESPONSE_PREFIX_SYNC_DELIM = ",";
    public static final String RESPONSE_PREFIX_ID = "id:";
    public static final String RESPONSE_PREFIX_ARTIST_ID = "artist_id:";
    public static final String RESPONSE_PREFIX_ALBUM_ID = "album_id:";
    public static final String RESPONSE_PREFIX_GENRE_ID = "genre_id:";
    public static final String RESPONSE_PREFIX_TRACK_ID = "track_id:";
    public static final String RESPONSE_PREFIX_CONTRIBUTOR_ID = "contributor_id:";
    public static final String RESPONSE_PREFIX_COUNT = "count:";
    public static final String RESPONSE_PREFIX_ARTIST_COUNT = "artists_count:";
    public static final String RESPONSE_PREFIX_CONTRIBUTOR_COUNT = "contributors_count:";
    public static final String RESPONSE_PREFIX_YEAR = "year:";
    public static final String RESPONSE_PREFIX_STEPS = "steps:";
    public static final String RESPONSE_PREFIX_INFO = "info:";
    public static final String RESPONSE_PREFIX_TOTAL_TIME = "totaltime:";
    public static final String RESPONSE_PREFIX_FULL_NAME = "fullname:";
    public static final String RESPONSE_TAGS = "tags:";
    public static final String RESPONSE_RESCAN_DONE = "done";
    /**
     * XML browser details
     */
    public static final String RESPONSE_XML_PREFIX_TITLE = "title:";
    public static final String RESPONSE_XML_PREFIX_NAME = "name:";
    public static final String RESPONSE_XML_PREFIX_ID = "id:";
    public static final String RESPONSE_XML_PREFIX_CMD = "cmd:";
    public static final String RESPONSE_XML_PREFIX_COUNT = "count:";
    public static final String RESPONSE_XML_PREFIX_WEIGHT = "weight:";
    public static final String RESPONSE_XML_PREFIX_ICON = "icon:";
    public static final String RESPONSE_XML_PREFIX_BITRATE = "bitrate:";
    public static final String RESPONSE_XML_PREFIX_VALUE = "value:";
    public static final String RESPONSE_XML_PREFIX_SUBTEXT = "subtext:";
    public static final String RESPONSE_XML_PREFIX_TYPE = "type:";
    public static final String RESPONSE_XML_PREFIX_ITEM_ID = "item_id:";
    public static final String RESPONSE_XML_PREFIX_IS_AUDIO = "isaudio:";
    public static final String RESPONSE_XML_PREFIX_HAS_ITEMS = "hasitems:";
    public static final String RESPONSE_XML_PREFIX_IMAGE = "image:";
    public static final String RESPONSE_XML_PREFIX_NETWORK_ERROR = "networkerror:";
    public static final String RESPONSE_XML_PREFIX_ENCLOSURE_LENGTH = "enclosure_length:";
    public static final String RESPONSE_XML_PREFIX_ENCLOSURE_URL = "enclosure_url:";
    public static final String RESPONSE_XML_PREFIX_ENCLOSURE_TYPE = "enclosure_type:";
    public static final String RESPONSE_XML_PREFIX_PUB_DATE = "pubdate:";
    public static final String RESPONSE_XML_PREFIX_DESCRIPTION = "description:";
    public static final String RESPONSE_XML_PREFIX_LINK = "link:";
    public static final String RESPONSE_XML_PREFIX_EXPLICIT = "explicit:";
    public static final String RESPONSE_XML_PREFIX_DURATION = "duration:";
    public static final String RESPONSE_XML_PREFIX_SUBTITLE = "subtitle:";
    public static final String RESPONSE_XML_PREFIX_SUMMARY = "summary:";
    public static final String RESPONSE_XML_PREFIX_URL = "url:";
    public static final String RESPONSE_XML_PREFIX_WANT_URL = "want_url:";

    /**
     * XML Browser plugin commands
     */
    public static final String PROP_XML_ITEMS = "SS_XML_BROWSER_ITEMS";
    public static final String PROP_XML_COUNT = "SS_XML_BROWSER_COUNT";

    /**
     * Searching
     */
    public static final String SEARCH_TAG = "search:";
    public static final String SEARCH_TAG_ALL = "term:";
    public static final String SEARCH_TAG_TRACK = "track_id:";
    public static final String SEARCH_TAG_URL = "url:";
    public static final String SEARCH_TAG_ARTIST = "artist_id:";
    public static final String SEARCH_TAG_YEAR = "year:";
    public static final String SEARCH_TAG_ALBUM = "album_id:";
    public static final String SEARCH_TAG_GENRE = "genre_id:";
    /**
     * Sorting
     */
    public static final String SORT_TAG = "sort:";
    public static final String SORT_TAG_NEW_MUSIC = "new";

    /**
     * Result params
     */
    public static final int RESULTS_START = 0;
    public static final int RESULTS_MAX = 999999;
    /**
     * Results
     */
    public static final String RESULT_TRUE = "1";
    public static final String RESULT_FALSE = "0";
    /**
     * Grouping results
     */
    public static final String RESULT_GROUP_SINGLE = "1";
    public static final String RESULT_GROUP_MULTIPLE = "0";
    /**
     * Image server settings
     */
    public static final String IMAGE_SERVER = "<server>";
    public static final String IMAGE_PORT = "<port>";
    public static final String IMAGE_ID = "<id>";
    public static final String IMAGE_URL = "http://" + IMAGE_SERVER + ":" + IMAGE_PORT + "/music/" + IMAGE_ID + "/cover.jpg";
    /**
     * Properties mapping
     */
    public static final String PROP_SERVER_ENCODING = "SERVER_ENCODING";
    public static final String PROP_CMD_LOGIN = "SS_CMD_LOGIN";
    public static final String PROP_CMD_VERSION = "SS_CMD_VERSION";
    public static final String PROP_CMD_STATUS = "SS_CMD_STATUS";
    public static final String PROP_CMD_EXIT = "SS_CMD_EXIT";
    public static final String PROP_CMD_PREF_QUERY = "SS_CMD_PREF_QUERY";
    public static final String PROP_CMD_PREF = "SS_CMD_PREF";
    public static final String PROP_CMD_CAN = "SS_CMD_CAN";
    public static final String PROP_CMD_PLAYER_COUNT = "SS_PLAYER_COUNT";
    public static final String PROP_CMD_PLAYER_QUERY = "SS_PLAYER_ID";
    public static final String PROP_PREF_GROUP_DISCS = "SS_PREF_GROUP_DISCS";
    public static final String PROP_PREF_MEDIA_DIRS = "SS_PREF_MEDIA_DIRS";
    public static final String PROP_PREF_PLAYLIST_DIR = "SS_PREF_PLAYLIST_DIR";
    public static final String PROP_CMD_PODCAST_TEST = "SS_PODCAST_TEST";
    public static final String PROP_CMD_LISTEN = "SS_CMD_LISTEN";
    public static final String PROP_CMD_SUBSCRIBE = "SS_CMD_SUBSCRIBE";
    public static final String PROP_CMD_SYNC_GROUPS = "SS_PLAYER_SYNC_GROUPS";
    public static final String PROP_DB_SONG_COUNT = "SS_DB_SONG_COUNT";
    public static final String PROP_DB_ALBUM_COUNT = "SS_DB_ALBUM_COUNT";
    public static final String PROP_DB_ARTIST_COUNT = "SS_DB_ARTIST_COUNT";
    public static final String PROP_DB_GENRE_COUNT = "SS_DB_GENRE_COUNT";
    public static final String PROP_DB_SEARCH = "SS_DB_SONG_SEARCH";
    public static final String PROP_DB_SONG_INFO = "SS_DB_SONG_INFO";
    public static final String PROP_DB_RESCAN = "SS_DB_RESCAN";
    public static final String PROP_DB_RESCAN_QUERY = "SS_DB_RESCAN_QUERY";
    public static final String PROP_DB_WIPECACHE = "SS_DB_WIPE_CACHE";
    public static final String PROP_DB_GENRES = "SS_DB_GENRES_QUERY";
    public static final String PROP_DB_ALBUMS = "SS_DB_ALBUMS_QUERY";
    public static final String PROP_DB_ARTISTS = "SS_DB_ARTISTS_QUERY";
    public static final String PROP_DB_YEARS = "SS_DB_YEARS_QUERY";
    public static final String PROP_DB_TITLE = "SS_DB_TITLE_QUERY";
    public static final String PROP_DB_RESCAN_PROGRESS = "SS_DB_RESCAN_PROGRESS";
    public static final String PROP_APP_QUERY = "SS_APP_QUERY";
    public static final String PROP_APP_ITEMS = "SS_XML_BROWSER_ITEMS";
    /*
     * subscribe
     */
    public static final String SUBSCRIBE_PLAYLIST = "playlist";
    public static final String SUBSCRIBE_SAVED_PLAYLISTS = "playlists";
    public static final String SUBSCRIBE_PLAYER_MIXER = "mixer";
    public static final String SUBSCRIBE_PLAYER = "client";
    public static final String SUBSCRIBE_PLAYER_RESPONSE_NEW = "new";
    public static final String SUBSCRIBE_PLAYER_RESPONSE_RECONNECT = "reconnect";
    public static final String SUBSCRIBE_PLAYER_RESPONSE_DISCONNECT = "disconnect";
    public static final String SUBSCRIBE_PLAYER_RESPONSE_FORGET = "forget";
    public static final String SUBSCRIBE_PLAYLIST_RESPONSE_ADD_TRACKS = "addtracks";
    public static final String SUBSCRIBE_PLAYLIST_RESPONSE_ADD = "add ";
    public static final String SUBSCRIBE_PLAYLIST_RESPONSE_DELETE = "delete";
    public static final String SUBSCRIBE_PLAYLIST_RESPONSE_LOAD = "loadtracks";
    public static final String SUBSCRIBE_PLAYLIST_RESPONSE_INSERT = "inserttracks";
    public static final String SUBSCRIBE_PLAYLIST_RESPONSE_CLEARED = "clear";
    public static final String SUBSCRIBE_PLAYLIST_ITEM_CHANGED = "newsong";
    /**
     * Favorite listen
     */
    public static final String SUBSCRIBE_FAVORITES = "favorites";
    public static final String SUBSCRIBE_FAVORITES_RESPONSE_ADD = "add";
    public static final String SUBSCRIBE_FAVORITES_RESPONSE_DELETE = "delete";
    public static final String SUBSCRIBE_FAVORITES_RESPONSE_RENAME = "rename";
    public static final String SUBSCRIBE_FAVORITES_RESPONSE_MOVE = "move";
    public static final String SUBSCRIBE_FAVORITES_RESPONSE_ADD_LEVEL = "move";
    /**
     * Log4j Loggers
     */
    public static final Logger LOGGER_XMLPLUGIN = Logger.getLogger("XMLPluginLogger");
    public static final Logger LOGGER_PODCAST = Logger.getLogger("PodcastLogger");
    public static final Logger LOGGER_DATABASE = Logger.getLogger("DatabaseLogger");
    public static final Logger LOGGER_APP = Logger.getLogger("AppLogger");
}
