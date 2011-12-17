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
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.exception.SlimNetworkException;
import org.bff.slimserver.exception.SlimResponseException;
import org.bff.slimserver.musicobjects.radio.SlimAvailableRadio;
import org.bff.slimserver.musicobjects.radio.SlimRadioAudioDetails;
import org.bff.slimserver.musicobjects.radio.SlimRadioItem;

/**
 *
 * @author bfindeisen
 */
public class SlimRadioPlugin implements SlimPlugin {

    private SlimServer slimServer;
    private Properties prop;
    private static String SS_PROP_RADIO_QUERY;
    private static String SS_PROP_RADIO_ITEMS;
    /*
     * icon:plugins/cache/icons/radioproviders.png
    cmd:moreradio
    weight:90
    name:More Radio...
    type:xmlbrowser
     */
    private static final String PLUGIN_COMMAND = "radio";
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
    private static final String PREFIX_IS_AUDIO = "isaudio:";
    private static final String PREFIX_HAS_ITEMS = "hasitems:";
    private static final String PREFIX_IMAGE = "image:";
    private static final String PREFIX_SUB_TEXT = "subtext:";
    private static final String PARAM_PLAYER = "<playerid>";
    private static final String PREFIX_NETWORK_ERROR = "networkerror:";
    private static final String PARAM_COMMAND = SlimConstants.CMD_PARAM_COMMAND;
    private static final String PARAM_START = SlimConstants.CMD_PARAM_START;
    private static final String PARAM_ITEMS = SlimConstants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = SlimConstants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PARAM_SEARCH = "search:";
    private static final long MAX_SEARCH_RESULTS = SlimConstants.RESULTS_MAX;
    public static List<String> detailPrefixes = new ArrayList<String>();
    public static List<String> audioDetailPrefixes = new ArrayList<String>();
    private final Logger logger;

    /**
     * Creates a new instance of SlimDatabase
     * @param slimServer the {@link SlimServer} for this database
     */
    public SlimRadioPlugin(SlimServer slimServer) {
        logger = Logger.getLogger("RadioLogger");
        this.setSlimServer(slimServer);
        this.prop = getSlimServer().getSlimProperties();
        loadProperties();
    }

    private void loadProperties() {
        SS_PROP_RADIO_QUERY = prop.getProperty("SS_RADIO_QUERY");
        SS_PROP_RADIO_ITEMS = prop.getProperty("SS_XMLBROWSER_ITEMS");
    }

   public Collection<SlimAvailableRadio> getAvailableRadios() throws SlimNetworkException, SlimResponseException {
        logger.trace("Starting getAvailableRadios");
        List<SlimAvailableRadio> radios = new ArrayList<SlimAvailableRadio>();
        String command = SS_PROP_RADIO_QUERY.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, Long.toString(MAX_SEARCH_RESULTS));
        command = command.replaceAll(PARAM_TAGS, "");

        logger.debug("GetAvailableRadios command: " + command);

        String[] response = sendCommand(command);

        if (response[0] == null || response.length == 1) {
            logger.warn("GetAvailableRadios response to command was null.");
            throw new SlimResponseException("Response was null");
        }

        checkResponse(response[0]);

        for (int i = 0; i < response.length;) {
            logger.debug("GetAvailableRadios response:" + response[i]);
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
                SlimAvailableRadio radio = new SlimAvailableRadio();
                logger.trace("GetAvailableRadios creating radio.");
                int c = 0;
                while (c < 5) {
                    if (response[i].startsWith(PREFIX_ICON)) {
                        radio.setIconURL("http://" +
                                getSlimServer().getServer() +
                                ":" +
                                getSlimServer().getWebPort() + "/" +
                                response[i].replace(PREFIX_ICON, ""));
                        logger.trace("GetAvailableRadios setting icon: " + radio.getIconURL());
                    } else if (response[i].startsWith(PREFIX_CMD)) {
                        radio.setCommand(response[i].replace(PREFIX_CMD, ""));
                        logger.trace("GetAvailableRadios setting command: " + radio.getCommand());
                    } else if (response[i].startsWith(PREFIX_WEIGHT)) {
                        radio.setWeight(Integer.parseInt(response[i].replace(PREFIX_WEIGHT, "")));
                        logger.trace("GetAvailableRadios setting weight: " + radio.getWeight());
                    } else if (response[i].startsWith(PREFIX_NAME)) {
                        radio.setName(response[i].replace(PREFIX_NAME, ""));
                        logger.trace("GetAvailableRadios setting name: " + radio.getName());
                    } else if (response[i].startsWith(PREFIX_TYPE)) {
                        radio.setType(response[i].replace(PREFIX_TYPE, ""));
                        logger.trace("GetAvailableRadios setting type: " + radio.getType());
                    }
                    logger.debug("GetAvailableRadios response:" + response[i]);
                    ++i;
                    ++c;
                }
                logger.trace("GetAvailableRadios adding radio.");
                radios.add(radio);
            } else {
                ++i;
            }
        }

        return radios;
    }

    public void loadRadio(SlimRadioItem radioItem,
            String command,
            SlimPlayer player) throws SlimException {
        loadRadio(radioItem, command, player, null);
    }

    public void loadRadio(SlimRadioItem radioItem,
            SlimPlayer player) throws SlimException {
        loadRadio(radioItem, radioItem.getCommand(), player, null);
    }

    /**
     * Pass null into search criteria for non xmlbrowser_search types.
     * @param radioCommand
     * @param player
     * @param searchCriteria
     * @return
     */
    private void loadRadio(SlimRadioItem radioItem,
            String radioCommand,
            SlimPlayer player,
            String searchCriteria) throws SlimException {

        String command = SS_PROP_RADIO_ITEMS.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_PLAYER, player.getId());
        command = command.replaceAll(PARAM_COMMAND, radioCommand);
        command = command.replaceAll(PARAM_ITEMS, Long.toString(MAX_SEARCH_RESULTS));

        if (searchCriteria == null) {
            command = command.replaceAll(PARAM_TAGS, PREFIX_ITEM_ID +
                    (radioItem.getRadioId() == null ? "" : radioItem.getRadioId()));
        } else {
            command = command.replaceAll(PARAM_TAGS, PARAM_SEARCH + searchCriteria);
        }


        logger.debug("GetRadio command:" + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("GetRadio response to command was null.");
            throw new SlimResponseException("Response was null");
        }
        checkResponse(response[0]);

        logger.trace("GetRadio creating SlimRadioitem for " + radioItem.getRadio().getName() +
                " with id of " + radioItem.getRadio().getId());

        logger.trace("GetRadio looking for title and count.");
        for (int i = 0; i < response.length; i++) {
            logger.debug("GetRadio response: " + response[i]);
            if (response[i].startsWith(PREFIX_TITLE)) {
                logger.trace("GetRadio found title: " + response[i]);
                radioItem.setTitle(response[i].replace(PREFIX_TITLE, ""));
                if (radioItem.getName() == null) {
                    radioItem.setName(response[i].replace(PREFIX_TITLE, ""));
                } else {
                    logger.trace("GetRadio already had name so not setting title.");
                }
            } else if (response[i].startsWith(PREFIX_COUNT)) {
                logger.trace("GetRadio found count: " + response[i]);
                radioItem.setCount(Integer.parseInt(response[i].replace(PREFIX_COUNT, "")));
            }
        }

        logger.trace("GetRadio looking for details.");
        for (int j = 0; j < response.length; j++) {
            logger.debug("GetRadio response: " + response[j]);
            if (response[j].startsWith(PREFIX_ID)) {
                logger.trace("GetRadio creating SlimRadioItem for " + radioItem.getRadio().getName() + " with id of " + radioItem.getRadio().getId());

                SlimRadioItem spi = new SlimRadioItem(radioItem.getRadio());
                spi.setRadioId(response[j].replace(PREFIX_ID, ""));
                logger.trace("GetRadio setting id: " + spi.getRadioId());
                ++j;
                logger.debug("GetRadio response: " + response[j]);
                while (j < response.length && !response[j].startsWith(PREFIX_ID)) {
                    logger.debug("GetRadio response: " + response[j]);
                    /*
                    id:0.0
                    name:Popular Now
                    isaudio:0
                    hasitems:1
                     */
                    if (response[j].startsWith(PREFIX_NAME)) {
                        spi.setName(response[j].replace(PREFIX_NAME, ""));
                        logger.trace("GetRadio setting name: " + spi.getName());
                    } else if (response[j].startsWith(PREFIX_IS_AUDIO)) {
                        spi.setAudio("1".equalsIgnoreCase(response[j].replace(PREFIX_IS_AUDIO, "")) ? true : false);
                        logger.trace("GetRadio setting audio: " + spi.isAudio());
                    } else if (response[j].startsWith(PREFIX_HAS_ITEMS)) {
                        spi.setContainsItems("1".equalsIgnoreCase(response[j].replace(PREFIX_HAS_ITEMS, "")) ? true : false);
                        logger.trace("GetRadio setting items: " + spi.isContainsItems());
                    } else if (response[j].startsWith(PREFIX_TITLE)) {
                        spi.setTitle(response[j].replace(PREFIX_TITLE, ""));
                        logger.trace("GetRadio setting title: " + spi.getTitle());
                    } else if (response[j].startsWith(PREFIX_TYPE)) {
                        spi.setType(response[j].replace(PREFIX_TYPE, ""));
                        logger.trace("GetRadio setting type: " + spi.getType());
                    }
                    ++j;
                }
                --j;
                radioItem.addRadioItem(spi);
            }
        }
    }

    public SlimRadioItem loadRadio(SlimAvailableRadio availableRadio, SlimPlayer player) throws SlimException {
        logger.trace("Starting getRadio for " + availableRadio.getName() +
                " with id of " + availableRadio.getId());

        SlimRadioItem spi = new SlimRadioItem(availableRadio);
        if (availableRadio.isSearch()) {
            if (availableRadio.getSearchCriteria() == null || "".equalsIgnoreCase(availableRadio.getSearchCriteria())) {
                throw new SlimResponseException("Must include search criteria.");
            }
            loadRadio(spi, availableRadio.getCommand(), player, availableRadio.getSearchCriteria());
        } else {
            loadRadio(spi, availableRadio.getCommand(), player);
        }

        return spi;
    }

    public SlimRadioAudioDetails getAudioDetails(SlimRadioItem item, SlimPlayer player) throws SlimNetworkException {
        String command = SS_PROP_RADIO_ITEMS.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_PLAYER, player.getId());
        command = command.replaceAll(PARAM_COMMAND, item.getCommand());
        command = command.replaceAll(PARAM_ITEMS, Long.toString(MAX_SEARCH_RESULTS));
        command = command.replaceAll(PARAM_TAGS, PREFIX_ITEM_ID + item.getRadioId());

        String[] response = sendCommand(command);

        checkResponse(response[0]);

        SlimRadioAudioDetails srad = new SlimRadioAudioDetails(item.getRadio(), item.getRadioId());

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
                try {
                    srad.setImageUrl(new URL(response[i].replace(PREFIX_IMAGE, "")));
                } catch (MalformedURLException ex) {
                    java.util.logging.Logger.getLogger(SlimRadioPlugin.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (response[i].startsWith(PREFIX_SUB_TEXT)) {
                srad.setSubText(response[i].replace(PREFIX_SUB_TEXT, ""));
            }

        }
        return srad;
    }

    public int getRadioCount() throws SlimNetworkException {
        String command = SS_PROP_RADIO_QUERY.replaceAll(PARAM_START, "0");
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
            e.printStackTrace();
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
