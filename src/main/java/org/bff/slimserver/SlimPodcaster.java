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
import org.bff.slimserver.exception.SlimNetworkException;
import org.bff.slimserver.musicobjects.SlimPodcastAudioDetails;
import org.bff.slimserver.musicobjects.SlimPodcastItem;

/**
 *
 * @author bfindeisen
 */
public class SlimPodcaster implements SlimPlugin {

    private SlimServer slimServer;
    private Properties prop;
    private static String SS_PROP_POD_COUNT;
    private static String SS_PROP_POD_ITEMS;

    /*
    icon:plugins/cache/icons/radioproviders.png
    cmd:moreradio
    weight:90
    name:More Radio...
    type:xmlbrowser
     */
    private static final String PARAM_START = SlimConstants.CMD_PARAM_START;
    private static final String PARAM_ITEMS = SlimConstants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = SlimConstants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PLUGIN_COMMAND = SlimConstants.CMD_PODCAST;
    private static final String PREFIX_COUNT = SlimConstants.RESPONSE_PREFIX_COUNT;
    private static final String PREFIX_TITLE = SlimConstants.RESPONSE_XML_PREFIX_TITLE;
    private static final String PREFIX_NAME = SlimConstants.RESPONSE_XML_PREFIX_NAME;
    private static final String PREFIX_ID = SlimConstants.RESPONSE_XML_PREFIX_ID;
    private static final String PREFIX_TYPE = SlimConstants.RESPONSE_XML_PREFIX_TYPE;
    private static final String PREFIX_ITEM_ID = SlimConstants.RESPONSE_XML_PREFIX_ITEM_ID;
    private static final String PREFIX_IS_AUDIO = SlimConstants.RESPONSE_XML_PREFIX_IS_AUDIO;
    private static final String PREFIX_HAS_ITEMS = SlimConstants.RESPONSE_XML_PREFIX_HAS_ITEMS;
    private static final String PREFIX_IMAGE = SlimConstants.RESPONSE_XML_PREFIX_IMAGE;
    private static final String PREFIX_NETWORK_ERROR = SlimConstants.RESPONSE_XML_PREFIX_NETWORK_ERROR;
    private static final String PREFIX_ENCLOSURE_LENGTH = SlimConstants.RESPONSE_XML_PREFIX_ENCLOSURE_LENGTH;
    private static final String PREFIX_ENCLOSURE_URL = SlimConstants.RESPONSE_XML_PREFIX_ENCLOSURE_URL;
    private static final String PREFIX_ENCLOSURE_TYPE = SlimConstants.RESPONSE_XML_PREFIX_ENCLOSURE_TYPE;
    private static final String PREFIX_PUB_DATE = SlimConstants.RESPONSE_XML_PREFIX_PUB_DATE;
    private static final String PREFIX_DESCRIPTION = SlimConstants.RESPONSE_XML_PREFIX_DESCRIPTION;
    private static final String PREFIX_LINK = SlimConstants.RESPONSE_XML_PREFIX_LINK;
    private static final String PREFIX_EXPLICIT = SlimConstants.RESPONSE_XML_PREFIX_EXPLICIT;
    private static final String PREFIX_DURATION = SlimConstants.RESPONSE_XML_PREFIX_DURATION;
    private static final String PREFIX_SUBTITLE = SlimConstants.RESPONSE_XML_PREFIX_SUBTITLE;
    private static final String PREFIX_SUMMARY = SlimConstants.RESPONSE_XML_PREFIX_SUMMARY;
    private static final String PREFIX_URL = SlimConstants.RESPONSE_XML_PREFIX_URL;
    private static final String PREFIX_WANT_URL = SlimConstants.RESPONSE_XML_PREFIX_WANT_URL;
    private static final String SEARCH_RESULTS_START = Integer.toString(SlimConstants.RESULTS_START);
    private static final String SEARCH_RESULTS_MAX = Integer.toString(SlimConstants.RESULTS_MAX);
    private static final String RESULT_TRUE = SlimConstants.RESULT_TRUE;
    private static Logger logger = SlimConstants.LOGGER_PODCAST;

    /**
     * Creates a new instance of SlimDatabase
     * @param slimServer the {@link SlimServer} for this database
     */
    public SlimPodcaster(SlimServer slimServer) {
        this.slimServer = slimServer;
        this.prop = SlimServer.getSlimProperties();
        loadProperties();
    }

    private void loadProperties() {
        SS_PROP_POD_COUNT = prop.getProperty(SlimConstants.PROP_PODCAST_COUNT);
        SS_PROP_POD_ITEMS = prop.getProperty(SlimConstants.PROP_PODCAST_ITEMS);
    }

    @Override
    public String getCommand() {
        return PLUGIN_COMMAND;
    }

    public SlimPodcastAudioDetails getPodcastAudioDetails(SlimPodcastItem item) throws SlimNetworkException {
        if (item != null) {
            logger.trace("GetPodcastAudioDetails for " + item.getId() + " - " + item.getName());
            return getPodcastAudioDetails(item.getId());
        } else {
            logger.trace("GetPodcastAudioDetails item is null");
            return null;
        }
    }

    /**
     * Id is in the form x.x.x.x
     * @param id
     * @return
     * @throws SlimNetworkException
     */
    public SlimPodcastAudioDetails getPodcastAudioDetails(String id) throws SlimNetworkException {
        logger.trace("Starting GetPodcastAudioDetails for " + id);
        String command = SS_PROP_POD_ITEMS.replaceAll(PARAM_START, SEARCH_RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, SEARCH_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, PREFIX_ITEM_ID + id);

        logger.debug("GetPodcastAudioDetails command " + command);

        String[] response = sendCommand(command);

        logger.trace("GetPodcastAudioDetails checking response.");
        checkResponse(response[0]);
        logger.trace("GetPodcastAudioDetails done checking response.");

        SlimPodcastAudioDetails spad = new SlimPodcastAudioDetails(getCommand());
        for (int i = 0; i < response.length; i++) {
            logger.trace("GetPodcastAudioDetails response " + response[i]);
            if (response[i].startsWith(PREFIX_DESCRIPTION)) {
                spad.setDescription(response[i].replace(PREFIX_DESCRIPTION, ""));
            } else if (response[i].startsWith(PREFIX_ID)) {
                spad.setId(response[i].substring(response[i].lastIndexOf(".") + 1));
                spad.setPodcastId(response[i].replace(PREFIX_ID, ""));
            } else if (response[i].startsWith(PREFIX_COUNT)) {
                spad.setCount(Integer.parseInt(response[i].replace(PREFIX_COUNT, "")));
            } else if (response[i].startsWith(PREFIX_DURATION)) {
                spad.setTotalDuration(response[i].replace(PREFIX_DURATION, ""));
            } else if (response[i].startsWith(PREFIX_ENCLOSURE_LENGTH)) {
                spad.setEnclosureLength(Integer.parseInt(response[i].replace(PREFIX_ENCLOSURE_LENGTH, "")));
            } else if (response[i].startsWith(PREFIX_ENCLOSURE_TYPE)) {
                spad.setEnclosureType(response[i].replace(PREFIX_ENCLOSURE_TYPE, ""));
            } else if (response[i].startsWith(PREFIX_ENCLOSURE_URL)) {
                spad.setEnclosureUrl(response[i].replace(PREFIX_ENCLOSURE_URL, ""));
            } else if (response[i].startsWith(PREFIX_EXPLICIT)) {
                spad.setExplicit(response[i].replace(PREFIX_EXPLICIT, ""));
            } else if (response[i].startsWith(PREFIX_IMAGE)) {
                try {
                    spad.setImageUrl(new URL(response[i].replace(PREFIX_IMAGE, "")));
                } catch (MalformedURLException ex) {
                    spad.setImageUrl(null);
                    logger.warn("GetPodcastAudioDetails malformed url for image.", ex);
                }
            } else if (response[i].startsWith(PREFIX_LINK)) {
                spad.setLink(response[i].replace(PREFIX_LINK, ""));
            } else if (response[i].startsWith(PREFIX_PUB_DATE)) {
                spad.setPubDate(response[i].replace(PREFIX_PUB_DATE, ""));
            } else if (response[i].startsWith(PREFIX_SUBTITLE)) {
                spad.setSubTitle(response[i].replace(PREFIX_SUBTITLE, ""));
            } else if (response[i].startsWith(PREFIX_SUMMARY)) {
                spad.setSummary(response[i].replace(PREFIX_SUMMARY, ""));
            } else if (response[i].startsWith(PREFIX_TITLE)) {
                spad.setTitle(response[i].replace(PREFIX_TITLE, ""));
                spad.setName(spad.getTitle());
            }
        }

        if (spad.getImageUrl() == null) {
            spad.setImageUrl(getClass().getResource("/resources/podcaster_icon_large.jpg"));
        }
        return spad;
    }

    public Collection<SlimPodcastItem> getPodcastList() throws SlimNetworkException {
        logger.trace("Starting GetPodcastList.");

        String command = SS_PROP_POD_ITEMS.replaceAll(PARAM_START, SEARCH_RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, SEARCH_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, PREFIX_WANT_URL + RESULT_TRUE);

        logger.debug("GetPodcastList command: " + command);

        List<SlimPodcastItem> podItems = new ArrayList<SlimPodcastItem>();

        String[] response = sendCommand(command);

        logger.trace("GetPodcastList starting checking response.");
        checkResponse(response[0]);
        logger.trace("GetPodcastList done checking response.");

        //int k = 0;
        for (int k = 0; k < response.length;) {
            logger.debug("GetPodcastList response: " + response[k]);
            if (response[k].startsWith(PREFIX_ID)) {
                SlimPodcastItem spi = new SlimPodcastItem();
                spi.setId(response[k].replace(PREFIX_ID, ""));
                ++k;
                while (k < response.length && !response[k].startsWith(PREFIX_ID)) {
                    logger.debug("GetPodcastList response: " + response[k]);

                    /*
                    id:0.0
                    name:Popular Now
                    isaudio:0
                    hasitems:1
                     */
                    if (response[k].startsWith(PREFIX_NAME)) {
                        spi.setName(response[k].replace(PREFIX_NAME, ""));
                    } else if (response[k].startsWith(PREFIX_IS_AUDIO)) {
                        spi.setAudio(RESULT_TRUE.equalsIgnoreCase(response[k].replace(PREFIX_IS_AUDIO, "")) ? true : false);
                    } else if (response[k].startsWith(PREFIX_HAS_ITEMS)) {
                        spi.setContainsItems(RESULT_TRUE.equalsIgnoreCase(response[k].replace(PREFIX_HAS_ITEMS, "")) ? true : false);
                    } else if (response[k].startsWith(PREFIX_TITLE)) {
                        spi.setTitle(response[k].replace(PREFIX_TITLE, ""));
                    } else if (response[k].startsWith(PREFIX_TYPE)) {
                        spi.setType(response[k].replace(PREFIX_TYPE, ""));
                    } else if (response[k].startsWith(PREFIX_URL)) {
                        spi.setUrl(response[k].replace(PREFIX_URL, ""));
                    }
                    ++k;
                }

                if (spi.getTitle() == null) {
                    spi.setTitle(spi.getName());
                }
                podItems.add(spi);
            } else {
                ++k;
            }
        }
        return podItems;
    }

    public void loadPodcastItem(SlimPodcastItem item) {
        logger.trace("Starting LoadPodcastItem  for " + item.getName());

        String command = SS_PROP_POD_ITEMS.replaceAll(PARAM_START, SEARCH_RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, SEARCH_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, PREFIX_ITEM_ID + item.getId());

        logger.debug("LoadPodcastItem command: " + command);

        String[] response = sendCommand(command);
        try {
            logger.trace("LoadPodcastItem checking response");
            checkResponse(response[0]);
            logger.trace("LoadPodcastItem done checking response");

            for (int k = 0; k < response.length;) {
                logger.trace("LoadPodcastItem response:" + response[k]);

                if (response[k].startsWith(PREFIX_ID)) {
                    SlimPodcastItem spi = new SlimPodcastItem();
                    spi.setId(response[k].replace(PREFIX_ID, ""));
                    ++k;
                    while (k < response.length && !response[k].startsWith(PREFIX_ID)) {
                        logger.trace("LoadPodcastItem response:" + response[k]);
                        /*
                        id:0.0
                        name:Popular Now
                        isaudio:0
                        hasitems:1
                         */
                        if (response[k].startsWith(PREFIX_NAME)) {
                            spi.setName(response[k].replace(PREFIX_NAME, ""));
                        } else if (response[k].startsWith(PREFIX_IS_AUDIO)) {
                            spi.setAudio(RESULT_TRUE.equalsIgnoreCase(response[k].replace(PREFIX_IS_AUDIO, "")) ? true : false);
                        } else if (response[k].startsWith(PREFIX_HAS_ITEMS)) {
                            spi.setContainsItems(RESULT_TRUE.equalsIgnoreCase(response[k].replace(PREFIX_HAS_ITEMS, "")) ? true : false);
                        } else if (response[k].startsWith(PREFIX_TITLE)) {
                            spi.setTitle(response[k].replace(PREFIX_TITLE, ""));
                        } else if (response[k].startsWith(PREFIX_TYPE)) {
                            spi.setType(response[k].replace(PREFIX_TYPE, ""));
                        }
                        ++k;
                    }
                    item.addPodcastItem(spi);
                } else {
                    ++k;
                }
            }
        } catch (SlimNetworkException ex) {
            logger.warn("Error loading podcast item " + item.getName(), ex);
            SlimPodcastItem pod = new SlimPodcastItem();
            pod.setId("0");
            pod.setError(true);
            pod.setErrorMessage(ex.getMessage());
            item.addPodcastItem(pod);
        }
    }

    private void checkResponse(String response) throws SlimNetworkException {
        if (response.startsWith(PREFIX_NETWORK_ERROR)) {
            throw new SlimNetworkException(response.replace(PREFIX_NETWORK_ERROR, ""));
        }

    }

    public int getPodcastCount() {
        return (Integer.parseInt(sendCommand(SS_PROP_POD_COUNT)[0].replace(PREFIX_COUNT, "")));
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

    /**
     * @return the logger
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * @param aLogger the logger to set
     */
    public static void setLogger(Logger aLogger) {
        logger = aLogger;
    }
}
