/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.bff.slimserver.domain.Playable;
import org.bff.slimserver.events.FavoriteChangeEvent;
import org.bff.slimserver.events.FavoriteChangeListener;
import org.bff.slimserver.events.PlaylistChangeListener;
import org.bff.slimserver.exception.NetworkException;
import org.bff.slimserver.exception.ResponseException;
import org.bff.slimserver.exception.SqueezeException;
import org.bff.slimserver.domain.favorite.Favorite;
import org.bff.slimserver.domain.favorite.FavoriteAudioDetails;
import org.bff.slimserver.domain.favorite.FavoriteItem;

/**
 * @author bfindeisen
 */
public class FavoritePlugin extends Plugin {

    private SqueezeServer squeezeServer;
    private Properties prop;
    private static String SS_PROP_FAVORITE_ITEMS;
    private static String SS_PROP_FAVORITE_EXISTS;
    private static String SS_PROP_FAVORITE_LEVEL;
    private static String SS_PROP_FAVORITE_DELETE;
    private static String SS_PROP_FAVORITE_RENAME;
    private static String SS_PROP_FAVORITE_ADD;
    private static String SS_PROP_FAVORITE_INSERT;
    private static String SS_PROP_FAVORITE_MOVE;
    private static final String PLUGIN_COMMAND = "favorites";
    private static final String PREFIX_COUNT = "count:";
    private static final String PREFIX_TITLE = "title:";
    private static final String PREFIX_CMD = "cmd:";
    private static final String PREFIX_WEIGHT = "weight:";
    private static final String PREFIX_ICON = "icon:";
    private static final String PREFIX_NAME = "name:";
    private static final String PREFIX_ID = "id:";
    private static final String PREFIX_TYPE = "type:";
    private static final String PREFIX_BITRATE = "bitrate:";
    private static final String PREFIX_VALUE = "value:";
    private static final String PREFIX_URL = "url:";
    private static final String PREFIX_ITEM_ID = "item_id:";
    private static final String PREFIX_WANT_URL = "want_url:";
    private static final String PREFIX_IS_AUDIO = "isaudio:";
    private static final String PREFIX_HAS_ITEMS = "hasitems:";
    private static final String PREFIX_EXISTS = "exists:";
    private static final String PREFIX_IMAGE = "image:";
    private static final String PREFIX_SUB_TEXT = "subtext:";
    private static final String PREFIX_FROM_ID = "from_id:";
    private static final String PREFIX_TO_ID = "to_id:";
    private static final String PREFIX_ENCLOSURE_LENGTH = "enclosure_length:";
    private static final String PREFIX_ENCLOSURE_URL = "enclosure_url:";
    private static final String PREFIX_ENCLOSURE_TYPE = "enclosure_type:";
    private static final String PREFIX_PUB_DATE = "pubdate:";
    private static final String PREFIX_DESCRIPTION = "description:";
    private static final String PREFIX_LINK = "link:";
    private static final String PREFIX_EXPLICIT = "explicit:";
    private static final String PREFIX_DURATION = "duration:";
    private static final String PREFIX_SUBTITLE = "subtitle:";
    private static final String PREFIX_SUMMARY = "summary:";
    private static final String PREFIX_NETWORK_ERROR = "networkerror:";
    private static final String PARAM_COMMAND = Constants.CMD_PARAM_COMMAND;
    private static final String PARAM_MARKER = Constants.CMD_PARAM_MARKER;
    private static final String PARAM_START = Constants.CMD_PARAM_START;
    private static final String PARAM_ITEMS = Constants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = Constants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PARAM_SEARCH = "search:";
    private static final long MAX_SEARCH_RESULTS = Constants.RESULTS_MAX;
    private static final String RESULT_TRUE = Constants.RESULT_TRUE;
    public static List<String> detailPrefixes = new ArrayList<String>();
    public static List<String> audioDetailPrefixes = new ArrayList<String>();
    private final Logger logger;
    private List<FavoriteChangeListener> favoriteListeners;

    /**
     * Creates a new instance of Database
     *
     * @param squeezeServer the {@link SqueezeServer} for this database
     */
    public FavoritePlugin(SqueezeServer squeezeServer) {
        super(squeezeServer);
        logger = Logger.getLogger("FavoritesLogger");
        this.squeezeServer = squeezeServer;
        this.prop = SqueezeServer.getSlimProperties();
        loadProperties();

        favoriteListeners = new ArrayList<FavoriteChangeListener>();
    }

    private void loadProperties() {
        SS_PROP_FAVORITE_ITEMS = prop.getProperty("SS_FAVORITE_ITEMS");
        SS_PROP_FAVORITE_EXISTS = prop.getProperty("SS_FAVORITE_EXISTS");
        SS_PROP_FAVORITE_ADD = prop.getProperty("SS_FAVORITE_ADD");
        SS_PROP_FAVORITE_INSERT = prop.getProperty("SS_FAVORITE_INSERT");
        SS_PROP_FAVORITE_LEVEL = prop.getProperty("SS_FAVORITE_LEVEL");
        SS_PROP_FAVORITE_DELETE = prop.getProperty("SS_FAVORITE_DELETE");
        SS_PROP_FAVORITE_RENAME = prop.getProperty("SS_FAVORITE_RENAME");
        SS_PROP_FAVORITE_MOVE = prop.getProperty("SS_FAVORITE_MOVE");
    }

    /**
     * Adds a {@link PlaylistChangeListener} to this playlist
     *
     * @param listener
     */
    public void addFavoriteChangeListener(FavoriteChangeListener listener) {
        favoriteListeners.add(listener);
    }

    /**
     * Removes a {@link PlaylistChangeListener} to this playlist
     *
     * @param listener
     */
    public void removeFavoriteChangeListener(FavoriteChangeListener listener) {
        favoriteListeners.remove(listener);
    }

    private void fireFavoriteEvent(FavoriteChangeEvent event) {
        for (FavoriteChangeListener listener : favoriteListeners) {
            listener.favoritesChanged(event);
        }
    }

    public boolean isFavorite(String trackIdorURL) throws SqueezeException {
        String command = SS_PROP_FAVORITE_EXISTS.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_MARKER, encode(trackIdorURL));
        logger.debug("IsFavorite command: " + command);

        String[] response = sendCommand(command);

        checkResponse(response[0]);

        for (int i = 0; i < response.length; i++) {
            if (response[i].startsWith(PREFIX_EXISTS)) {
                return RESULT_TRUE.equalsIgnoreCase(response[i].replace(PREFIX_EXISTS, "").trim());
            }
        }

        return false;
    }

    public FavoriteAudioDetails getFavoriteAudioDetails(Favorite favorite) throws SqueezeException {
        String command = SS_PROP_FAVORITE_ITEMS.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, Long.toString(MAX_SEARCH_RESULTS));
        command = command.replaceAll(PARAM_TAGS, PREFIX_ITEM_ID + favorite.getId());

        logger.debug("GetFavoriteAudioDetails command: " + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("GetFavoriteAudioDetails response to command was null.");
            throw new ResponseException("Response was null");
        }

        checkResponse(response[0]);

        FavoriteAudioDetails details = new FavoriteAudioDetails(favorite.getId(), getCommand());

        for (int i = 0; i < response.length; i++) {
            logger.trace("GetFavoriteAudioDetails response:" + response[i]);

            if (response[i].startsWith(PREFIX_NAME)) {
                details.setName(response[i].replace(PREFIX_NAME, ""));
            } else if (response[i].startsWith(PREFIX_URL)) {
                details.setUrl(response[i].replace(PREFIX_URL, ""));
            } else if (response[i].startsWith(PREFIX_BITRATE)) {
                details.setBitrate(response[i].replace(PREFIX_BITRATE, ""));
            } else if (response[i].startsWith(PREFIX_TYPE)) {
                details.setType(response[i].replace(PREFIX_TYPE, ""));
            } else if (response[i].startsWith(PREFIX_VALUE)) {
                details.setValue(response[i].replace(PREFIX_VALUE, ""));
            } else if (response[i].startsWith(PREFIX_ICON)) {
                try {
                    details.setIconUrl(new URL("http://" + getSqueezeServer().getServer() + ":" + getSqueezeServer().getWebPort() + "/" + response[i].replace(PREFIX_ICON, "")));
                    details.setImageUrl(details.getIconUrl());
                } catch (MalformedURLException ex) {
                    details.setIconUrl(null);
                    logger.warn("GetFavoriteAudioDetails malformed url for image.", ex);
                }
                logger.trace("GetFavoriteAudioDetails setting icon: " + details.getImageUrl());
            } else if (response[i].startsWith(PREFIX_DESCRIPTION)) {
                details.setDescription(response[i].replace(PREFIX_DESCRIPTION, ""));
            } else if (response[i].startsWith(PREFIX_COUNT)) {
                details.setCount(Integer.parseInt(response[i].replace(PREFIX_COUNT, "")));
            } else if (response[i].startsWith(PREFIX_DURATION)) {
                details.setTotalDuration(response[i].replace(PREFIX_DURATION, ""));
            } else if (response[i].startsWith(PREFIX_ENCLOSURE_LENGTH)) {
                details.setEnclosureLength(Integer.parseInt(response[i].replace(PREFIX_ENCLOSURE_LENGTH, "")));
            } else if (response[i].startsWith(PREFIX_ENCLOSURE_TYPE)) {
                details.setEnclosureType(response[i].replace(PREFIX_ENCLOSURE_TYPE, ""));
            } else if (response[i].startsWith(PREFIX_ENCLOSURE_URL)) {
                details.setEnclosureUrl(response[i].replace(PREFIX_ENCLOSURE_URL, ""));
            } else if (response[i].startsWith(PREFIX_EXPLICIT)) {
                details.setExplicit(response[i].replace(PREFIX_EXPLICIT, ""));
            } else if (response[i].startsWith(PREFIX_IMAGE)) {
                try {
                    details.setImageUrl(new URL(response[i].replace(PREFIX_IMAGE, "")));
                } catch (MalformedURLException ex) {
                    details.setImageUrl(null);
                    logger.warn("GetFavoriteAudioDetails malformed url for image.", ex);
                }
            } else if (response[i].startsWith(PREFIX_LINK)) {
                details.setLink(response[i].replace(PREFIX_LINK, ""));
            } else if (response[i].startsWith(PREFIX_PUB_DATE)) {
                details.setPubDate(response[i].replace(PREFIX_PUB_DATE, ""));
            } else if (response[i].startsWith(PREFIX_SUBTITLE)) {
                details.setSubTitle(response[i].replace(PREFIX_SUBTITLE, ""));
            } else if (response[i].startsWith(PREFIX_SUMMARY)) {
                details.setSummary(response[i].replace(PREFIX_SUMMARY, ""));
            } else if (response[i].startsWith(PREFIX_TITLE)) {
                details.setTitle(response[i].replace(PREFIX_TITLE, ""));
                details.setName(details.getTitle());
            } else if (response[i].startsWith(PREFIX_IS_AUDIO)) {
                details.setAudio(RESULT_TRUE.equalsIgnoreCase(response[i].replace(PREFIX_IS_AUDIO, "")) ? true : false);
            }
        }

        return details;
    }

    /**
     * Clears all favorites
     */
    public void clearFavorites() throws SqueezeException {
        int count = getCount();
        for (int i = 0; i < count; i++) {
            deleteFavorite("0");
        }

        fireFavoriteEvent(new FavoriteChangeEvent(this, FavoriteChangeEvent.FAVORITES_CLEARED));
    }

    public Collection<FavoriteItem> getFavorites() throws SqueezeException {
        return getFavorites((String) null);
    }

    public Collection<FavoriteItem> getFavorites(Favorite favorite) throws SqueezeException {
        return getFavorites(favorite.getId());
    }

    public void loadFavoriteItem(FavoriteItem item) throws SqueezeException {
        logger.trace("Starting LoadFavoriteItem  for " + item.getName());

        for (FavoriteItem sfi : getFavorites(item.getId())) {
            item.addFavoriteItem(sfi);
        }

    }

    /*
    public int getCount() throws SqueezeException {
        logger.trace("Starting GetFavoritesCount");
        String command = SS_PROP_FAVORITE_ITEMS.replaceAll(PARAM_START, "");
        command = command.replaceAll(PARAM_ITEMS, "");
        command = command.replaceAll(PARAM_TAGS, "");

        logger.debug("GetFavoritesCount command: " + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("GetFavoritesCount response to command was null.");
            throw new ResponseException("Response was null");
        }

        checkResponse(response[0]);

        for (int i = 0; i < response.length; i++) {
            if (response[i].startsWith(PREFIX_COUNT)) {
                return Integer.parseInt(response[i].replace(PREFIX_COUNT, ""));
            }
        }
        return 0;

    }
      */
    private Collection<FavoriteItem> getFavorites(String id) throws SqueezeException {
        logger.trace("Starting GetFavorites");
        List<FavoriteItem> favs = new ArrayList<FavoriteItem>();
        String command = SS_PROP_FAVORITE_ITEMS.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, Long.toString(MAX_SEARCH_RESULTS));
        if (id == null) {
            command = command.replaceAll(PARAM_TAGS, "");
        } else {
            command = command.replaceAll(PARAM_TAGS, PREFIX_ITEM_ID + id);
        }

        command = command.trim() + " " + PREFIX_WANT_URL + "1";

        logger.debug("GetFavorites command: " + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("GetFavorites response to command was null.");
            throw new ResponseException("Response was null");
        }

        checkResponse(response[0]);

//        for (int i = 0; i < response.length; i++) {
//            System.out.println(response[i]);
//        }


        for (int i = 0; i < response.length; i++) {
            logger.debug("GetFavorites response:" + response[i]);

            if (response[i].startsWith(PREFIX_ID)) {
                String str = response[i].replace(PREFIX_ID, "");
                if (id == null) {
                    if (str.contains(".")) {
                        str = str.replace(str.split("\\.")[0] + ".", "");
                    }
                }
                FavoriteItem fav = new FavoriteItem(str);

                logger.trace("GetFavorites setting id: " + fav.getId());
                ++i;
                while (i < response.length && !response[i].startsWith(PREFIX_ID)) {
                    logger.debug("GetFavorites response: " + response[i]);
                    if (response[i].startsWith(PREFIX_NAME)) {
                        fav.setName(response[i].replace(PREFIX_NAME, ""));
                        logger.trace("GetFavorites setting name: " + fav.getName());
                    } else if (response[i].startsWith(PREFIX_IS_AUDIO)) {
                        fav.setAudio(RESULT_TRUE.equalsIgnoreCase(response[i].replace(PREFIX_IS_AUDIO, "")) ? true : false);
                        logger.trace("GetFavorites setting audio: " + fav.isAudio());
                    } else if (response[i].startsWith(PREFIX_HAS_ITEMS)) {
                        fav.setContainsItems(RESULT_TRUE.equalsIgnoreCase(response[i].replace(PREFIX_HAS_ITEMS, "")) ? true : false);
                        logger.trace("GetFavorites setting items: " + fav.isContainsItems());
                    } else if (response[i].startsWith(PREFIX_TITLE)) {
                        fav.setTitle(response[i].replace(PREFIX_TITLE, ""));
                        logger.trace("GetFavorites setting title: " + fav.getTitle());
                    } else if (response[i].startsWith(PREFIX_TYPE)) {
                        fav.setType(response[i].replace(PREFIX_TYPE, ""));
                        logger.trace("GetFavorites setting type: " + fav.getType());
                    } else if (response[i].startsWith(PREFIX_URL)) {
                        fav.setUrl(response[i].replace(PREFIX_URL, ""));
                        logger.trace("GetFavorites setting url:" + fav.getUrl());
                    }
                    ++i;
                }
                --i;

                if (fav.isContainsItems() && !fav.isAudio()) {
                    fav.setSlimType(FavoriteItem.SLIMTYPE.FOLDER);
                }

                //System.out.println("type:" + fav.getType());
                favs.add(fav);
            }
        }
        return favs;
    }

    private String[] sendCommand(String slimCommand) {
        return sendCommand(slimCommand, null);
    }

    private String[] sendCommand(String slimCommand, String param) {
        try {
            if (param == null) {
                return getSqueezeServer().sendCommand(new Command(slimCommand));
            } else {
                return getSqueezeServer().sendCommand(new Command(slimCommand, param));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getCommand() {
        return PLUGIN_COMMAND;
    }

    private void checkResponse(String response) throws NetworkException {
        if (response.startsWith(PREFIX_NETWORK_ERROR)) {
            throw new NetworkException(response.replace(PREFIX_NETWORK_ERROR, ""));
        }
    }

    public boolean addFolder(String folder) throws SqueezeException {
        String command = SS_PROP_FAVORITE_LEVEL;
        command = command.replaceAll(PARAM_TAGS, PREFIX_TITLE + folder);
        logger.debug("AddFolder command: " + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("AddFolder response to command was null.");
            throw new ResponseException("Response was null");
        }

        checkResponse(response[0]);

        for (int i = 0; i < response.length; i++) {
            if (response[i].startsWith(PREFIX_COUNT)) {
                fireFavoriteEvent(new FavoriteChangeEvent(this, FavoriteChangeEvent.FOLDER_ADDED));
                return RESULT_TRUE.equalsIgnoreCase(response[i].replace(PREFIX_COUNT, "").trim());
            }
        }
        return false;
    }

    public void addFavoriteToFolder(Playable item, FavoriteItem folder) throws SqueezeException {
        String command = SS_PROP_FAVORITE_INSERT;
        command = command.replaceAll(PARAM_TAGS,
                "index:" + encode(folder.getId()) + " "
                        + PREFIX_URL + encode(item.getUrl()) + " "
                        + PREFIX_TITLE + encode(item.getTitle()));

        logger.debug("AddFavoriteToFolder command: " + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("AddFavorite response to command was null.");
            throw new ResponseException("Response was null");
        }

        checkResponse(response[0]);

        fireFavoriteEvent(new FavoriteChangeEvent(this, FavoriteChangeEvent.FAVORITE_ADDED, item.getTitle()));
    }

    public void addFavorite(String title, String url) throws SqueezeException {
        String command = SS_PROP_FAVORITE_ADD;
        command = command.replaceAll(PARAM_TAGS,
                PREFIX_URL + encode(url) + " "
                        + PREFIX_TITLE + encode(title));

        logger.debug("AddFavorite command: " + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("AddFavorite response to command was null.");
            throw new ResponseException("Response was null");
        }

        checkResponse(response[0]);

        fireFavoriteEvent(new FavoriteChangeEvent(this, FavoriteChangeEvent.FAVORITE_ADDED, title));
    }

    public void addFavorite(Playable item) throws SqueezeException {
        addFavorite(item.getTitle(), item.getUrl());
    }

    public void deleteFavorite(Favorite favorite) throws SqueezeException {
        deleteFavorite(favorite.getId());
        fireFavoriteEvent(new FavoriteChangeEvent(this, FavoriteChangeEvent.FAVORITE_DELETED));
    }

    private void deleteFavorite(String id) {
        String command = SS_PROP_FAVORITE_DELETE;
        command = command.replaceAll(PARAM_TAGS, PREFIX_ITEM_ID + id);
        logger.debug("DeleteFavorite command: " + command);

        sendCommand(command);
    }

    public void renameFavorite(Favorite favorite, String newName) throws SqueezeException {
        String command = SS_PROP_FAVORITE_RENAME;
        command = command.replaceAll(PARAM_TAGS, PREFIX_ITEM_ID + favorite.getId()
                + " " + PREFIX_TITLE + encode(newName));
        logger.debug("RenameFavorite command: " + command);

        sendCommand(command);

        fireFavoriteEvent(new FavoriteChangeEvent(this, FavoriteChangeEvent.FOLDER_RENAMED));
    }

    public void moveFavorite(Favorite fromFavorite, Favorite toFavorite) throws
            SqueezeException {
        String command = SS_PROP_FAVORITE_MOVE;
        command = command.replaceAll(PARAM_TAGS, PREFIX_FROM_ID + fromFavorite.getId()
                + " " + PREFIX_TO_ID + toFavorite.getId());
        logger.debug("MoveFavorite command: " + command);

        sendCommand(command);

        fireFavoriteEvent(new FavoriteChangeEvent(this, FavoriteChangeEvent.FAVORITE_MOVED));
    }

    private String encode(String criteria) {
        return Utils.encode(criteria, SqueezeServer.getEncoding());
    }
}
