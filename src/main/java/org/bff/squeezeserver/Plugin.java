/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.squeezeserver;

import org.apache.log4j.Logger;
import org.bff.squeezeserver.domain.XMLBrowserAudioDetails;
import org.bff.squeezeserver.domain.XMLPluginItem;
import org.bff.squeezeserver.exception.NetworkException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * @author bfindeisen
 */
public abstract class Plugin {

    private SqueezeServer squeezeServer;
    private Properties prop;

    public abstract String getCommand();

    private static String SS_PROP_XML_ITEMS;
    private static String SS_PROP_XML_COUNT;

    private static Logger logger = Constants.LOGGER_XMLPLUGIN;

    private static final String PARAM_START = Constants.CMD_PARAM_START;
    private static final String PARAM_ITEMS = Constants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = Constants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PARAM_COMMAND = Constants.CMD_PARAM_COMMAND;
    private static final String PARAM_PLAYER = Constants.CMD_PARAM_PLAYER_ID;
    protected static final String PREFIX_ITEM_ID = Constants.RESPONSE_XML_PREFIX_ITEM_ID;
    protected static final String PREFIX_COUNT = Constants.RESPONSE_PREFIX_COUNT;
    protected static final String PREFIX_ENCLOSURE_LENGTH = Constants.RESPONSE_XML_PREFIX_ENCLOSURE_LENGTH;
    protected static final String PREFIX_ENCLOSURE_URL = Constants.RESPONSE_XML_PREFIX_ENCLOSURE_URL;
    protected static final String PREFIX_ENCLOSURE_TYPE = Constants.RESPONSE_XML_PREFIX_ENCLOSURE_TYPE;
    protected static final String PREFIX_PUB_DATE = Constants.RESPONSE_XML_PREFIX_PUB_DATE;
    protected static final String PREFIX_DESCRIPTION = Constants.RESPONSE_XML_PREFIX_DESCRIPTION;
    protected static final String PREFIX_LINK = Constants.RESPONSE_XML_PREFIX_LINK;
    protected static final String PREFIX_EXPLICIT = Constants.RESPONSE_XML_PREFIX_EXPLICIT;
    protected static final String PREFIX_DURATION = Constants.RESPONSE_XML_PREFIX_DURATION;
    protected static final String PREFIX_SUBTITLE = Constants.RESPONSE_XML_PREFIX_SUBTITLE;
    protected static final String PREFIX_SUMMARY = Constants.RESPONSE_XML_PREFIX_SUMMARY;
    protected static final String PREFIX_IMAGE = Constants.RESPONSE_XML_PREFIX_IMAGE;
    protected static final String PREFIX_TITLE = Constants.RESPONSE_XML_PREFIX_TITLE;
    protected static final String PREFIX_NAME = Constants.RESPONSE_XML_PREFIX_NAME;
    protected static final String PREFIX_ID = Constants.RESPONSE_XML_PREFIX_ID;
    protected static final String PREFIX_TYPE = Constants.RESPONSE_XML_PREFIX_TYPE;
    protected static final String PREFIX_IS_AUDIO = Constants.RESPONSE_XML_PREFIX_IS_AUDIO;
    protected static final String PREFIX_HAS_ITEMS = Constants.RESPONSE_XML_PREFIX_HAS_ITEMS;
    protected static final String PREFIX_URL = Constants.RESPONSE_XML_PREFIX_URL;
    protected static final String PREFIX_WANT_URL = Constants.RESPONSE_XML_PREFIX_WANT_URL;
    protected static final String PREFIX_BITRATE = Constants.RESPONSE_XML_PREFIX_BITRATE;
    protected static final String PREFIX_ICON = Constants.RESPONSE_XML_PREFIX_ICON;
    protected static final String PREFIX_VALUE = Constants.RESPONSE_XML_PREFIX_VALUE;
    protected static final String PREFIX_NETWORK_ERROR = Constants.RESPONSE_XML_PREFIX_NETWORK_ERROR;
    private static final String SEARCH_RESULTS_START = Integer.toString(Constants.RESULTS_START);
    private static final String SEARCH_RESULTS_MAX = Integer.toString(Constants.RESULTS_MAX);
    private static final String RESULT_TRUE = Constants.RESULT_TRUE;

    public Plugin(SqueezeServer squeezeServer) {
        this.squeezeServer = squeezeServer;
        this.prop = SqueezeServer.getProperties();
        loadProperties();
    }

    public Collection<XMLPluginItem> getXMLItems(XMLPluginItem item) {
        logger.trace("Starting LoadXMLItem  for " + item.getName());
        List<XMLPluginItem> xmlItems = new ArrayList<XMLPluginItem>();
        String command = SS_PROP_XML_ITEMS.replaceAll(PARAM_START, SEARCH_RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, SEARCH_RESULTS_MAX);
        command = command.replaceAll(PARAM_COMMAND, getCommand());
        command = command.replaceAll(PARAM_TAGS, PREFIX_ITEM_ID + item.getId());

        logger.debug("LoadXMLItem command: " + command);

        String[] response = sendCommand(command);
        try {
            logger.trace("LoadXMLItem checking response");
            checkResponse(response[0]);
            logger.trace("LoadXMLItem done checking response");

            for (int k = 0; k < response.length; ) {
                logger.trace("LoadXMLItem response:" + response[k]);

                if (response[k].startsWith(PREFIX_ID)) {
                    XMLPluginItem xmlItem = new XMLPluginItem();
                    xmlItem.setId(response[k].replace(PREFIX_ID, ""));
                    ++k;
                    while (k < response.length && !response[k].startsWith(PREFIX_ID)) {
                        logger.trace("LoadXMLItem response:" + response[k]);
                        if (response[k].startsWith(PREFIX_NAME)) {
                            xmlItem.setName(response[k].replace(PREFIX_NAME, ""));
                        } else if (response[k].startsWith(PREFIX_IS_AUDIO)) {
                            xmlItem.setAudio(RESULT_TRUE.equalsIgnoreCase(response[k].replace(PREFIX_IS_AUDIO, "")) ? true : false);
                        } else if (response[k].startsWith(PREFIX_HAS_ITEMS)) {
                            xmlItem.setContainsItems(RESULT_TRUE.equalsIgnoreCase(response[k].replace(PREFIX_HAS_ITEMS, "")) ? true : false);
                        } else if (response[k].startsWith(PREFIX_TITLE)) {
                            xmlItem.setTitle(response[k].replace(PREFIX_TITLE, ""));
                        } else if (response[k].startsWith(PREFIX_TYPE)) {
                            xmlItem.setType(response[k].replace(PREFIX_TYPE, ""));
                        }
                        ++k;
                    }
                    xmlItems.add(xmlItem);
                } else {
                    ++k;
                }
            }
        } catch (NetworkException ex) {
            logger.warn("Error loading XML item " + item.getName(), ex);
            XMLPluginItem xmlItem = new XMLPluginItem();
            xmlItem.setId("0");
            xmlItem.setError(true);
            xmlItem.setErrorMessage(ex.getMessage());
            xmlItems.add(xmlItem);
        }

        return xmlItems;
    }

    /**
     * Returns the {@link org.bff.squeezeserver.domain.XMLBrowserAudioDetails} for the given {@link org.bff.squeezeserver.domain.XMLPluginItem}
     *
     * @param item the plugin
     * @return the {@link org.bff.squeezeserver.domain.XMLBrowserAudioDetails}
     * @throws org.bff.squeezeserver.exception.NetworkException
     *          if there are network
     */
    public XMLBrowserAudioDetails getXMLAudioDetails(XMLPluginItem item) throws NetworkException {
        if (item != null) {
            logger.trace("GetXMLAudioDetails for " + item.getId() + " - " + item.getName());
            logger.trace("Starting GetXMLAudioDetails for " + item.getId());
            String command = SS_PROP_XML_ITEMS.replaceAll(PARAM_START, SEARCH_RESULTS_START);
            command = command.replaceAll(PARAM_ITEMS, SEARCH_RESULTS_MAX);
            command = command.replaceAll(PARAM_TAGS, PREFIX_ITEM_ID + item.getId());
            command = command.replaceAll(PARAM_COMMAND, getCommand());

            logger.debug("GetXMLAudioDetails command " + command);

            String[] response = sendCommand(command);

            logger.trace("GetXMLAudioDetails checking response.");
            checkResponse(response[0]);
            logger.trace("GetXMLAudioDetails done checking response.");

            XMLBrowserAudioDetails details = new XMLBrowserAudioDetails(item.getId(), getCommand());

            loadAudioDetails(response, details);

            return details;
        } else {
            logger.trace("GetXMLAudioDetails item is null");
            return null;
        }
    }

    public Collection<XMLPluginItem> getXMLList() throws NetworkException {
        logger.trace("Starting GetXMLList.");

        String command = SS_PROP_XML_ITEMS.replaceAll(PARAM_START, SEARCH_RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, SEARCH_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, PREFIX_WANT_URL + RESULT_TRUE);
        command = command.replaceAll(PARAM_COMMAND, getCommand());

        logger.debug("GetXMLList command: " + command);

        List<XMLPluginItem> xmlItems = new ArrayList<XMLPluginItem>();

        String[] response = sendCommand(command);

        logger.trace("GetXMLList starting checking response.");
        checkResponse(response[0]);
        logger.trace("GetXMLList done checking response.");

        //int k = 0;
        for (int k = 0; k < response.length; ) {
            logger.debug("GetXMLList response: " + response[k]);
            if (response[k].startsWith(PREFIX_ID)) {
                XMLPluginItem xpi = new XMLPluginItem();
                xpi.setId(response[k].replace(PREFIX_ID, ""));
                ++k;
                while (k < response.length && !response[k].startsWith(PREFIX_ID)) {
                    logger.debug("GetXMLList response: " + response[k]);
                    if (response[k].startsWith(PREFIX_NAME)) {
                        xpi.setName(response[k].replace(PREFIX_NAME, ""));
                    } else if (response[k].startsWith(PREFIX_IS_AUDIO)) {
                        xpi.setAudio(RESULT_TRUE.equalsIgnoreCase(response[k].replace(PREFIX_IS_AUDIO, "")) ? true : false);
                    } else if (response[k].startsWith(PREFIX_HAS_ITEMS)) {
                        xpi.setContainsItems(RESULT_TRUE.equalsIgnoreCase(response[k].replace(PREFIX_HAS_ITEMS, "")) ? true : false);
                    } else if (response[k].startsWith(PREFIX_TITLE)) {
                        xpi.setTitle(response[k].replace(PREFIX_TITLE, ""));
                    } else if (response[k].startsWith(PREFIX_TYPE)) {
                        xpi.setType(response[k].replace(PREFIX_TYPE, ""));
                    } else if (response[k].startsWith(PREFIX_URL)) {
                        xpi.setUrl(response[k].replace(PREFIX_URL, ""));
                    }
                    ++k;
                }

                if (xpi.getTitle() == null) {
                    xpi.setTitle(xpi.getName());
                }
                xmlItems.add(xpi);
            } else {
                ++k;
            }
        }
        return xmlItems;
    }

    public void loadAudioDetails(String[] response, XMLBrowserAudioDetails details) {
        for (int i = 0; i < response.length; i++) {
            logger.trace("XMLAudioDetails response " + response[i]);
            if (response[i].startsWith(PREFIX_DESCRIPTION)) {
                details.setDescription(response[i].replace(PREFIX_DESCRIPTION, ""));
            } else if (response[i].startsWith(PREFIX_ID)) {
                details.setId(response[i].substring(response[i].lastIndexOf(".") + 1));
                details.setXmlId(response[i].replace(PREFIX_ID, ""));
            } else if (response[i].startsWith(PREFIX_COUNT)) {
                details.setCount(Integer.parseInt(response[i].replace(PREFIX_COUNT, "")));
            } else if (response[i].startsWith(PREFIX_DURATION)) {
                details.setDuration(response[i].replace(PREFIX_DURATION, ""));
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
                    logger.warn("XMLAudioDetails malformed url for image.", ex);
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
            }
        }
    }

    public int getCount() {
        String command = SS_PROP_XML_COUNT.replaceAll(PARAM_COMMAND, getCommand());
        return (Integer.parseInt(sendCommand(command)[0].replace(PREFIX_COUNT, "")));
    }

    protected void checkResponse(String response) throws NetworkException {
        if (response.startsWith(PREFIX_NETWORK_ERROR)) {
            throw new NetworkException(response.replace(PREFIX_NETWORK_ERROR, ""));
        }
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

    private void loadProperties() {
        SS_PROP_XML_COUNT = prop.getProperty(Constants.PROP_XML_COUNT);
        SS_PROP_XML_ITEMS = prop.getProperty(Constants.PROP_XML_ITEMS);
    }

    /**
     * @return the squeezeServer
     */
    public SqueezeServer getSqueezeServer() {
        return squeezeServer;
    }

    /**
     * @param squeezeServer the squeezeServer to set
     */
    public void setSqueezeServer(SqueezeServer squeezeServer) {
        this.squeezeServer = squeezeServer;
    }

    /**
     * Copies the properties from the fromItem to the toItem
     *
     * @param fromItem the item to copy from
     * @param toItem   the item to copy to
     */
    public void copyItem(XMLPluginItem fromItem, XMLPluginItem toItem) {
        toItem.setId(fromItem.getId());
        toItem.setName(fromItem.getName());
        toItem.setAlbum(fromItem.getAlbum());
        toItem.setBitrate(fromItem.getBitrate());
        toItem.setAudio(fromItem.isAudio());
        toItem.setComment(fromItem.getComment());
        toItem.setContainsItems(fromItem.isContainsItems());
        toItem.setCount(fromItem.getCount());
        toItem.setError(fromItem.isError());
        toItem.setErrorMessage(fromItem.getErrorMessage());
        toItem.setGenre(fromItem.getGenre());
        toItem.setImageUrl(fromItem.getImageUrl());
        toItem.setLength(fromItem.getLength());
        toItem.setRating(fromItem.getRating());
        toItem.setRemote(fromItem.isRemote());
        toItem.setSqueezeType(fromItem.getSqueezeType());
        toItem.setTitle(fromItem.getTitle());
        toItem.setType(fromItem.getType());
        toItem.setUrl(fromItem.getUrl());
        toItem.setYear(fromItem.getYear());
    }

    public void copyAudioDetails(XMLBrowserAudioDetails fromDetails, XMLBrowserAudioDetails toDetails) {
        toDetails.setAlbum(fromDetails.getAlbum());
        toDetails.setLength(fromDetails.getLength());
        toDetails.setName(fromDetails.getName());
        toDetails.setRating(fromDetails.getRating());
        toDetails.setBitrate(fromDetails.getBitrate());
        toDetails.setComment(fromDetails.getComment());
        toDetails.setCount(fromDetails.getCount());
        toDetails.setDescription(fromDetails.getDescription());
        toDetails.setEnclosureLength(fromDetails.getEnclosureLength());
        toDetails.setEnclosureType(fromDetails.getEnclosureType());
        toDetails.setEnclosureUrl(fromDetails.getEnclosureUrl());
        toDetails.setExplicit(fromDetails.getExplicit());
        toDetails.setGenre(fromDetails.getGenre());
        toDetails.setImageUrl(fromDetails.getImageUrl());
        toDetails.setPubDate(fromDetails.getPubDate());
        toDetails.setSubTitle(fromDetails.getSubTitle());
        toDetails.setSummary(fromDetails.getSummary());
        toDetails.setTitle(fromDetails.getTitle());
        toDetails.setDuration(fromDetails.getDuration());
        toDetails.setTrack(fromDetails.getTrack());
        toDetails.setType(fromDetails.getType());
        toDetails.setUrl(fromDetails.getUrl());
        toDetails.setValue(fromDetails.getValue());
        toDetails.setXmlId(fromDetails.getXmlId());
        toDetails.setYear(fromDetails.getYear());
    }
}
