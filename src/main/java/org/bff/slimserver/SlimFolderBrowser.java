/*
 * SlimPlaylist.java
 *
 * Created on October 15, 2007, 10:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.bff.slimserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.bff.slimserver.exception.SlimNetworkException;
import org.bff.slimserver.exception.SlimResponseException;
import org.bff.slimserver.musicobjects.SlimFolderObject;

/**
 * Represents the current playlist of the SlimServer
 * @author Bill Findeisen
 */
public class SlimFolderBrowser {

    private SlimServer slimServer;
    private SlimDatabase database;
    private Properties prop;
    private static String SLIM_PROP_FOLDER;
    private final Logger logger;
    /**
     * requesting tags
     */
    private static final String TAG_RETURN = "tags:";
    /**
     * command parameters
     */
    private static final String PARAM_START = SlimConstants.CMD_PARAM_START;
    private static final String PARAM_ITEMS = SlimConstants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = SlimConstants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PREFIX_CMD = "cmd:";
    private static final String PREFIX_CMD_ADD = "add";
    private static final String PREFIX_CMD_LOAD = "load";
    private static final String PREFIX_CMD_INSERT = "insert";
    private static final String PREFIX_CMD_DELETE = "delete";
    private static final String PREFIX_ID = "id:";
    private static final String PREFIX_FOLDER_ID = "folder_id:";
    private static final String PREFIX_FILE_NAME = "filename:";
    private static final String PREFIX_IS_AUDIO = "audio:";
    private static final String PREFIX_TITLE = "title:";
    private static final String PREFIX_TYPE = "type:";
    //"track", "folder", "playlist", or "unknown"
    private static final String TYPE_TRACK = "track";
    private static final String TYPE_FOLDER = "folder";
    private static final String TYPE_PLAYLIST = "playlist";
    private static final String TYPE_UNKNOWN = "unknown";
    private static final long MAX_SEARCH_RESULTS = SlimConstants.RESULTS_MAX;

    /** Creates a new instance of SlimPlaylist
     * @param player the {@link SlimPlayer} for this playlist
     */
    public SlimFolderBrowser(SlimServer server) {
        logger = Logger.getLogger("FolderLogger");
        setSlimServer(server);//current playlist
        setDatabase(new SlimDatabase(getSlimServer()));
        prop = getSlimServer().getSlimProperties();

        loadProperties();
    }

    private SlimServer getSlimServer() {
        return slimServer;
    }

    private void setSlimServer(SlimServer slimServer) {
        this.slimServer = slimServer;
    }

    private void loadProperties() {
        SLIM_PROP_FOLDER = prop.getProperty("SS_FOLDER");
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

    public Collection<SlimFolderObject> getFolders() throws SlimResponseException {
        logger.trace("Starting GetFolders");
        List<SlimFolderObject> folders = new ArrayList<SlimFolderObject>();

        String command = SLIM_PROP_FOLDER.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, Long.toString(MAX_SEARCH_RESULTS));
        command = command.replaceAll(PARAM_TAGS, "");

        logger.debug("GetFolders command: " + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("GetFolders response to command was null.");
            throw new SlimResponseException("Response was null");
        }


        for (int i = 0; i < response.length;) {
            logger.debug("GetFolders response: " + response[i]);
            if (response[i].startsWith(PREFIX_ID)) {
                SlimFolderObject sfo = new SlimFolderObject();
                sfo.setId(response[i].replace(PREFIX_ID, ""));

                ++i;
                while (i < response.length && !response[i].startsWith(PREFIX_ID)) {
                    logger.debug("GetFolders response: " + response[i]);

                    if (response[i].startsWith(PREFIX_FILE_NAME)) {
                        sfo.setFileName(response[i].replace(PREFIX_FILE_NAME, ""));
                    } else if (response[i].startsWith(PREFIX_TITLE)) {
                        sfo.setName(response[i].replace(PREFIX_TITLE, ""));
                    } else if (response[i].startsWith(PREFIX_TYPE)) {
                        String type = response[i].replace(PREFIX_TYPE, "");

                        if (type.equalsIgnoreCase(SlimFolderObject.OBJECTTYPE.FOLDER.getDescription())) {
                            sfo.setObjectType(SlimFolderObject.OBJECTTYPE.FOLDER);
                        } else if (type.equalsIgnoreCase(SlimFolderObject.OBJECTTYPE.PLAYLIST.getDescription())) {
                            sfo.setObjectType(SlimFolderObject.OBJECTTYPE.PLAYLIST);
                        } else if (type.equalsIgnoreCase(SlimFolderObject.OBJECTTYPE.TRACK.getDescription())) {
                            sfo.setObjectType(SlimFolderObject.OBJECTTYPE.TRACK);
                        } else {
                            sfo.setObjectType(SlimFolderObject.OBJECTTYPE.UNKNOWN);
                        }
                    }
                    ++i;
                }
                folders.add(sfo);
            } else {
                ++i;
            }
        }
        return folders;
    }

    public Collection<SlimFolderObject> getFolderElements(SlimFolderObject folder) throws SlimResponseException {
        List<SlimFolderObject> folders = new ArrayList<SlimFolderObject>();

        String command = SLIM_PROP_FOLDER.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, Long.toString(MAX_SEARCH_RESULTS));
        command = command.replaceAll(PARAM_TAGS, PREFIX_FOLDER_ID + folder.getId());

        logger.debug("GetFolders command: " + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("GetFolders response to command was null.");
            throw new SlimResponseException("Response was null");
        }

        for (int i = 0; i < response.length;) {
            logger.debug("GetFolders response: " + response[i]);
            if (response[i].startsWith(PREFIX_ID)) {
                SlimFolderObject sfo = new SlimFolderObject();
                sfo.setId(response[i].replace(PREFIX_ID, ""));

                ++i;
                while (i < response.length && !response[i].startsWith(PREFIX_ID)) {
                    logger.debug("GetFolders response: " + response[i]);

                    if (response[i].startsWith(PREFIX_FILE_NAME)) {
                        sfo.setFileName(response[i].replace(PREFIX_FILE_NAME, ""));
                    } else if (response[i].startsWith(PREFIX_TITLE)) {
                        sfo.setName(response[i].replace(PREFIX_TITLE, ""));
                    } else if (response[i].startsWith(PREFIX_TYPE)) {
                        String type = response[i].replace(PREFIX_TYPE, "");

                        if (type.equalsIgnoreCase(SlimFolderObject.OBJECTTYPE.FOLDER.getDescription())) {
                            sfo.setObjectType(SlimFolderObject.OBJECTTYPE.FOLDER);
                        } else if (type.equalsIgnoreCase(SlimFolderObject.OBJECTTYPE.PLAYLIST.getDescription())) {
                            sfo.setObjectType(SlimFolderObject.OBJECTTYPE.PLAYLIST);
                        } else if (type.equalsIgnoreCase(SlimFolderObject.OBJECTTYPE.TRACK.getDescription())) {
                            sfo.setObjectType(SlimFolderObject.OBJECTTYPE.TRACK);
                        } else {
                            sfo.setObjectType(SlimFolderObject.OBJECTTYPE.UNKNOWN);
                        }
                    }
                    ++i;
                }
                folders.add(sfo);
            } else {
                ++i;
            }
        }

        return folders;

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
}
