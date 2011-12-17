/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.exception.SlimNetworkException;
import org.bff.slimserver.exception.SlimResponseException;
import org.bff.slimserver.musicobjects.app.SlimAvailableApp;
import org.bff.slimserver.musicobjects.app.SlimAppAudioDetails;
import org.bff.slimserver.musicobjects.app.SlimAppItem;

/**
 *
 * @author bfindeisen
 */
public class SlimAppPlugin implements SlimPlugin {

    private SlimServer slimServer;
    private Properties prop;
    private static String SS_PROP_APP_QUERY;
    private static String SS_PROP_APP_ITEMS;
    private static final String PLUGIN_COMMAND = SlimConstants.CMD_APP;
    private static final String PREFIX_COUNT = SlimConstants.RESPONSE_XML_PREFIX_COUNT;
    private static final String PREFIX_TITLE = SlimConstants.RESPONSE_XML_PREFIX_TITLE;
    private static final String PREFIX_CMD = SlimConstants.RESPONSE_XML_PREFIX_CMD;
    private static final String PREFIX_WEIGHT = SlimConstants.RESPONSE_XML_PREFIX_WEIGHT;
    private static final String PREFIX_ICON = SlimConstants.RESPONSE_XML_PREFIX_ICON;
    private static final String PREFIX_NAME = SlimConstants.RESPONSE_XML_PREFIX_NAME;
    private static final String PREFIX_ID = SlimConstants.RESPONSE_XML_PREFIX_ID;
    private static final String PREFIX_TYPE = SlimConstants.RESPONSE_XML_PREFIX_TYPE;
    private static final String PREFIX_BITRATE = SlimConstants.RESPONSE_XML_PREFIX_BITRATE;
    private static final String PREFIX_VALUE = SlimConstants.RESPONSE_XML_PREFIX_VALUE;
    private static final String PREFIX_URL = SlimConstants.RESPONSE_XML_PREFIX_URL;
    private static final String PREFIX_ITEM_ID = SlimConstants.RESPONSE_XML_PREFIX_ITEM_ID;
    private static final String PREFIX_IS_AUDIO = SlimConstants.RESPONSE_XML_PREFIX_IS_AUDIO;
    private static final String PREFIX_HAS_ITEMS = SlimConstants.RESPONSE_XML_PREFIX_HAS_ITEMS;
    private static final String PREFIX_IMAGE = SlimConstants.RESPONSE_XML_PREFIX_IMAGE;
    private static final String PREFIX_SUB_TEXT = SlimConstants.RESPONSE_XML_PREFIX_SUBTEXT;
    private static final String PARAM_PLAYER = SlimConstants.CMD_PARAM_PLAYER_ID;
    private static final String PREFIX_NETWORK_ERROR = SlimConstants.RESPONSE_XML_PREFIX_NETWORK_ERROR;
    private static final String PARAM_COMMAND = SlimConstants.CMD_PARAM_COMMAND;
    private static final String PARAM_START = SlimConstants.CMD_PARAM_START;
    private static final String PARAM_ITEMS = SlimConstants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = SlimConstants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PARAM_SEARCH = SlimConstants.SEARCH_TAG;
    private static final long MAX_SEARCH_RESULTS = SlimConstants.RESULTS_MAX;
    public static List<String> detailPrefixes = new ArrayList<String>();
    public static List<String> audioDetailPrefixes = new ArrayList<String>();
    private final Logger logger;

    /**
     * Creates a new instance of SlimDatabase
     * @param slimServer the {@link SlimServer} for this database
     */
    public SlimAppPlugin(SlimServer slimServer) {
        logger = SlimConstants.LOGGER_APP;
        this.slimServer = slimServer;
        this.prop = SlimServer.getSlimProperties();
        loadProperties();
    }

    private void loadProperties() {
        SS_PROP_APP_QUERY = prop.getProperty(SlimConstants.PROP_APP_QUERY);
        SS_PROP_APP_ITEMS = prop.getProperty(SlimConstants.PROP_APP_ITEMS);
    }

   public Collection<SlimAvailableApp> getAvailableApps() throws SlimNetworkException, SlimResponseException {
        logger.trace("Starting getAvailableApps");
        List<SlimAvailableApp> apps = new ArrayList<SlimAvailableApp>();
        String command = SS_PROP_APP_QUERY.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, Long.toString(MAX_SEARCH_RESULTS));
        command = command.replaceAll(PARAM_TAGS, "");

        logger.debug("GetAvailableApps command: " + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("GetAvailableApps response to command was null.");
            throw new SlimResponseException("Response was null");
        }

        checkResponse(response[0]);

        for (int i = 0; i < response.length;) {
            logger.debug("GetAvailableApps response:" + response[i]);
            //System.out.println(response[i]);
            /*icon:plugins/cache/icons/picks.png
            cmd:picks
            weight:10
            name:Staff Picks
            type:xmlbrowser
             */
            if (response[i].startsWith(PREFIX_ICON) ||
                    response[i].startsWith(PREFIX_CMD) ||
                    response[i].startsWith(PREFIX_WEIGHT) ||
                    response[i].startsWith(PREFIX_NAME) ||
                    response[i].startsWith(PREFIX_TYPE)) {
                SlimAvailableApp app = new SlimAvailableApp();
                logger.trace("GetAvailableApps creating app.");
                int c = 0;
                while (c < 5) {
                    if (response[i].startsWith(PREFIX_ICON)) {
                        app.setIconURL("http://" +
                                getSlimServer().getServer() +
                                ":" +
                                getSlimServer().getWebPort() + "/" +
                                response[i].replace(PREFIX_ICON, ""));
                        logger.trace("GetAvailableApps setting icon: " + app.getIconURL());
                    } else if (response[i].startsWith(PREFIX_CMD)) {
                        app.setCommand(response[i].replace(PREFIX_CMD, ""));
                        logger.trace("GetAvailableApps setting command: " + app.getCommand());
                    } else if (response[i].startsWith(PREFIX_WEIGHT)) {
                        app.setWeight(Integer.parseInt(response[i].replace(PREFIX_WEIGHT, "")));
                        logger.trace("GetAvailableApps setting weight: " + app.getWeight());
                    } else if (response[i].startsWith(PREFIX_NAME)) {
                        app.setName(response[i].replace(PREFIX_NAME, ""));
                        logger.trace("GetAvailableApps setting name: " + app.getName());
                    } else if (response[i].startsWith(PREFIX_TYPE)) {
                        app.setType(response[i].replace(PREFIX_TYPE, ""));
                        logger.trace("GetAvailableApps setting type: " + app.getType());
                    }
                    logger.debug("GetAvailableApps response:" + response[i]);
                    ++i;
                    ++c;
                }
                logger.trace("GetAvailableApps adding app.");
                apps.add(app);
            } else {
                ++i;
            }
        }

        return apps;
    }

    public void loadApp(SlimAppItem appItem,
            String command,
            SlimPlayer player) throws SlimException {
        loadApp(appItem, command, player, null);
    }

    public void loadApp(SlimAppItem appItem,
            SlimPlayer player) throws SlimException {
        loadApp(appItem, appItem.getCommand(), player, null);
    }

    /**
     * Pass null into search criteria for non xmlbrowser_search types.
     * @param appCommand
     * @param player
     * @param searchCriteria
     * @return
     */
    private void loadApp(SlimAppItem appItem,
            String appCommand,
            SlimPlayer player,
            String searchCriteria) throws SlimException {

        String command = SS_PROP_APP_ITEMS.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_PLAYER, player.getId());
        command = command.replaceAll(PARAM_COMMAND, appCommand);
        command = command.replaceAll(PARAM_ITEMS, Long.toString(MAX_SEARCH_RESULTS));

        if (searchCriteria == null) {
            command = command.replaceAll(PARAM_TAGS, PREFIX_ITEM_ID +
                    (appItem.getAppId() == null ? "" : appItem.getAppId()));
        } else {
            command = command.replaceAll(PARAM_TAGS, PARAM_SEARCH + searchCriteria);
        }


        logger.debug("GetApp command:" + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("GetApp response to command was null.");
            throw new SlimResponseException("Response was null");
        }
        checkResponse(response[0]);

        logger.trace("GetApp creating SlimAppitem for " + appItem.getApp().getName() +
                " with id of " + appItem.getApp().getId());

        logger.trace("GetApp looking for title and count.");
        for (int i = 0; i < response.length; i++) {
            logger.debug("GetApp response: " + response[i]);
            if (response[i].startsWith(PREFIX_TITLE)) {
                logger.trace("GetApp found title: " + response[i]);
                appItem.setTitle(response[i].replace(PREFIX_TITLE, ""));
                if (appItem.getName() == null) {
                    appItem.setName(response[i].replace(PREFIX_TITLE, ""));
                } else {
                    logger.trace("GetApp already had name so not setting title.");
                }
            } else if (response[i].startsWith(PREFIX_COUNT)) {
                logger.trace("GetApp found count: " + response[i]);
                appItem.setCount(Integer.parseInt(response[i].replace(PREFIX_COUNT, "")));
            }
        }

        logger.trace("GetApp looking for details.");
        for (int j = 0; j < response.length; j++) {
            logger.debug("GetApp response: " + response[j]);
            if (response[j].startsWith(PREFIX_ID)) {
                logger.trace("GetApp creating SlimAppItem for " + appItem.getApp().getName() + " with id of " + appItem.getApp().getId());

                SlimAppItem spi = new SlimAppItem(appItem.getApp());
                spi.setAppId(response[j].replace(PREFIX_ID, ""));
                logger.trace("GetApp setting id: " + spi.getAppId());
                ++j;
                logger.debug("GetApp response: " + response[j]);
                while (j < response.length && !response[j].startsWith(PREFIX_ID)) {
                    logger.debug("GetApp response: " + response[j]);
                    /*
                    id:0.0
                    name:Popular Now
                    isaudio:0
                    hasitems:1
                     */
                    if (response[j].startsWith(PREFIX_NAME)) {
                        spi.setName(response[j].replace(PREFIX_NAME, ""));
                        logger.trace("GetApp setting name: " + spi.getName());
                    } else if (response[j].startsWith(PREFIX_IS_AUDIO)) {
                        spi.setAudio("1".equalsIgnoreCase(response[j].replace(PREFIX_IS_AUDIO, "")) ? true : false);
                        logger.trace("GetApp setting audio: " + spi.isAudio());
                    } else if (response[j].startsWith(PREFIX_HAS_ITEMS)) {
                        spi.setContainsItems("1".equalsIgnoreCase(response[j].replace(PREFIX_HAS_ITEMS, "")) ? true : false);
                        logger.trace("GetApp setting items: " + spi.isContainsItems());
                    } else if (response[j].startsWith(PREFIX_TITLE)) {
                        spi.setTitle(response[j].replace(PREFIX_TITLE, ""));
                        logger.trace("GetApp setting title: " + spi.getTitle());
                    } else if (response[j].startsWith(PREFIX_TYPE)) {
                        spi.setType(response[j].replace(PREFIX_TYPE, ""));
                        logger.trace("GetApp setting type: " + spi.getType());
                    }
                    ++j;
                }
                --j;
                appItem.addAppItem(spi);
            }
        }
    }

    public SlimAppItem loadApp(SlimAvailableApp availableApp, SlimPlayer player) throws SlimException {
        logger.trace("Starting getApp for " + availableApp.getName() +
                " with id of " + availableApp.getId());

        SlimAppItem spi = new SlimAppItem(availableApp);
        if (availableApp.isSearch()) {
            if (availableApp.getSearchCriteria() == null || "".equalsIgnoreCase(availableApp.getSearchCriteria())) {
                throw new SlimResponseException("Must include search criteria.");
            }
            loadApp(spi, availableApp.getCommand(), player, availableApp.getSearchCriteria());
        } else {
            loadApp(spi, availableApp.getCommand(), player);
        }

        return spi;
    }

    public SlimAppAudioDetails getAudioDetails(SlimAppItem item, SlimPlayer player) throws SlimNetworkException {
        String command = SS_PROP_APP_ITEMS.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_PLAYER, player.getId());
        command = command.replaceAll(PARAM_COMMAND, item.getCommand());
        command = command.replaceAll(PARAM_ITEMS, Long.toString(MAX_SEARCH_RESULTS));
        command = command.replaceAll(PARAM_TAGS, PREFIX_ITEM_ID + item.getAppId());

        String[] response = sendCommand(command);

        checkResponse(response[0]);

        SlimAppAudioDetails srad = new SlimAppAudioDetails(item.getApp(), item.getAppId());

        /*
        Prefix:item_id
        Prefix:count
        Prefix:id
        Prefix:name
        Prefix:isaudio
        Prefix:bitrate
        Prefix:value
        Prefix:url
        Prefix:type
         */

        for (int i = 0; i < response.length; i++) {
            logger.trace("GetAudioDetails response:" + response[i]);

            if (response[i].startsWith(PREFIX_NAME)) {
                srad.setName(response[i].replace(PREFIX_NAME, ""));
            } else if (response[i].startsWith(PREFIX_IS_AUDIO)) {
                srad.setAudio("1".equalsIgnoreCase(response[i].replace(PREFIX_IS_AUDIO, "")) ? true : false);
            } else if (response[i].startsWith(PREFIX_COUNT)) {
                srad.setCount(Integer.parseInt(response[i].replace(PREFIX_COUNT, "")));
            } else if (response[i].startsWith(PREFIX_BITRATE)) {
                srad.setBitrate(response[i].replace(PREFIX_BITRATE, ""));
            } else if (response[i].startsWith(PREFIX_VALUE)) {
                srad.setValue(response[i].replace(PREFIX_VALUE, ""));
            } else if (response[i].startsWith(PREFIX_URL)) {
                srad.setUrl(response[i].replace(PREFIX_URL, ""));
            } else if (response[i].startsWith(PREFIX_TYPE)) {
                srad.setType(response[i].replace(PREFIX_TYPE, ""));
            } else if (response[i].startsWith(PREFIX_IMAGE)) {
                srad.setImageUrl(response[i].replace(PREFIX_IMAGE, ""));
            } else if (response[i].startsWith(PREFIX_SUB_TEXT)) {
                srad.setSubText(response[i].replace(PREFIX_SUB_TEXT, ""));
            }

        }
        return srad;
    }

    public int getAppCount() throws SlimNetworkException {
        String command = SS_PROP_APP_QUERY.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, "2");
        command = command.replaceAll(PARAM_TAGS, "");

        String[] response = sendCommand(command);

        checkResponse(response[0]);

        for (int i = 0; i < response.length; i++) {
            if (response[i].startsWith(PREFIX_COUNT)) {
                return Integer.parseInt(response[i].replace(PREFIX_COUNT, ""));
            }
        }

        return 0;
    }

    private void checkResponse(String response) throws SlimNetworkException {
        if (response.startsWith(PREFIX_NETWORK_ERROR)) {
            throw new SlimNetworkException(response.replace(PREFIX_NETWORK_ERROR, ""));
        }
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
            logger.fatal(e);
            return null;
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

    @Override
    public String getCommand() {
        return PLUGIN_COMMAND;
    }
}
