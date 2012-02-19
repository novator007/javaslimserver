/*
 * SlimPlaylist.java
 *
 * Created on October 15, 2007, 10:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

import org.apache.log4j.Logger;
import org.bff.squeezeserver.domain.Folder;
import org.bff.squeezeserver.exception.ResponseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Represents the current playlist of the SqueezeServer
 *
 * @author Bill Findeisen
 */
public class FolderBrowser {

    private SqueezeServer squeezeServer;
    private Database database;
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
    private static final String PARAM_START = Constants.CMD_PARAM_START;
    private static final String PARAM_ITEMS = Constants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = Constants.CMD_PARAM_TAGGED_PARAMS;
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
    private static final String TYPE_TRACK = "track";
    private static final String TYPE_FOLDER = "folder";
    private static final String TYPE_PLAYLIST = "playlist";
    private static final String TYPE_UNKNOWN = "unknown";
    private static final long MAX_SEARCH_RESULTS = Constants.RESULTS_MAX;

    /**
     * Creates a new instance of Playlist
     *
     * @param server the {@link SqueezeServer} for this folder browser
     */
    public FolderBrowser(SqueezeServer server) {
        logger = Logger.getLogger("FolderLogger");
        setSqueezeServer(server);//current playlist
        setDatabase(new Database(getSqueezeServer()));
        prop = getSqueezeServer().getProperties();

        loadProperties();
    }

    private SqueezeServer getSqueezeServer() {
        return squeezeServer;
    }

    private void setSqueezeServer(SqueezeServer squeezeServer) {
        this.squeezeServer = squeezeServer;
    }

    private void loadProperties() {
        SLIM_PROP_FOLDER = prop.getProperty("SS_FOLDER");
    }

    /**
     * @return the database
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * @param database the database to set
     */
    public void setDatabase(Database database) {
        this.database = database;
    }

    public Collection<Folder> getFolders() throws ResponseException {
        logger.trace("Starting GetFolders");
        List<Folder> folders = new ArrayList<Folder>();

        String command = SLIM_PROP_FOLDER.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, Long.toString(MAX_SEARCH_RESULTS));
        command = command.replaceAll(PARAM_TAGS, "");

        logger.debug("GetFolders command: " + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("GetFolders response to command was null.");
            throw new ResponseException("Response was null");
        }


        for (int i = 0; i < response.length; ) {
            logger.debug("GetFolders response: " + response[i]);
            if (response[i].startsWith(PREFIX_ID)) {
                Folder sfo = new Folder();
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

                        if (type.equalsIgnoreCase(Folder.FOLDERTYPE.FOLDER.getDescription())) {
                            sfo.setFolderType(Folder.FOLDERTYPE.FOLDER);
                        } else if (type.equalsIgnoreCase(Folder.FOLDERTYPE.PLAYLIST.getDescription())) {
                            sfo.setFolderType(Folder.FOLDERTYPE.PLAYLIST);
                        } else if (type.equalsIgnoreCase(Folder.FOLDERTYPE.TRACK.getDescription())) {
                            sfo.setFolderType(Folder.FOLDERTYPE.TRACK);
                        } else {
                            sfo.setFolderType(Folder.FOLDERTYPE.UNKNOWN);
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

    public Collection<Folder> getFolderElements(Folder folder) throws ResponseException {
        List<Folder> folders = new ArrayList<Folder>();

        String command = SLIM_PROP_FOLDER.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, Long.toString(MAX_SEARCH_RESULTS));
        command = command.replaceAll(PARAM_TAGS, PREFIX_FOLDER_ID + folder.getId());

        logger.debug("GetFolders command: " + command);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            logger.warn("GetFolders response to command was null.");
            throw new ResponseException("Response was null");
        }

        for (int i = 0; i < response.length; ) {
            logger.debug("GetFolders response: " + response[i]);
            if (response[i].startsWith(PREFIX_ID)) {
                Folder sfo = new Folder();
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

                        if (type.equalsIgnoreCase(Folder.FOLDERTYPE.FOLDER.getDescription())) {
                            sfo.setFolderType(Folder.FOLDERTYPE.FOLDER);
                        } else if (type.equalsIgnoreCase(Folder.FOLDERTYPE.PLAYLIST.getDescription())) {
                            sfo.setFolderType(Folder.FOLDERTYPE.PLAYLIST);
                        } else if (type.equalsIgnoreCase(Folder.FOLDERTYPE.TRACK.getDescription())) {
                            sfo.setFolderType(Folder.FOLDERTYPE.TRACK);
                        } else {
                            sfo.setFolderType(Folder.FOLDERTYPE.UNKNOWN);
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
                return getSqueezeServer().sendCommand(new Command(slimCommand));
            } else {
                return getSqueezeServer().sendCommand(new Command(slimCommand, param));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
