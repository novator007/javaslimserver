/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bff.slimserver.Controller;
import org.bff.slimserver.SlimDatabase;
import org.bff.slimserver.SlimFavoritePlugin;
import org.bff.slimserver.SlimFolderBrowser;
import org.bff.slimserver.SlimPlayer;
import org.bff.slimserver.SlimPlaylist;
import org.bff.slimserver.SlimRadioPlugin;
import org.bff.slimserver.SlimSavedPlaylistManager;
import org.bff.slimserver.SlimServer;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.monitor.SlimEventListener;

/**
 *
 * @author bill
 */
public abstract class Base {

    private static final SlimServer slimServer = Controller.getInstance().getSlimServer();
    private static final SlimPlayer player = new ArrayList<SlimPlayer>(Controller.getInstance().getSlimServer().getSlimPlayers()).get(0);
    private static final SlimPlaylist playlist = new SlimPlaylist(getPlayer());
    private static final SlimEventListener listener = new SlimEventListener(player);
    private static final SlimDatabase database = new SlimDatabase(getSlimServer());
    private static final SlimSavedPlaylistManager savedPlaylistManager = new SlimSavedPlaylistManager(slimServer);
    private static final SlimFavoritePlugin favoritePlugin = new SlimFavoritePlugin(slimServer);
    private static final SlimFolderBrowser folderBrowser= new SlimFolderBrowser(getSlimServer());
    private static final SlimRadioPlugin radioPlugin = new SlimRadioPlugin(getSlimServer());;

    static {
        try {
            Controller.getInstance().loadSongs();
        } catch (SlimException ex) {
            Logger.getLogger(Base.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the database
     */
    public static SlimDatabase getDatabase() {
        return database;
    }

    /**
     * @return the slimServer
     */
    public static SlimServer getSlimServer() {
        return slimServer;
    }

    /**
     * @return the playlist
     */
    public static SlimPlaylist getPlaylist() {
        return playlist;
    }

    /**
     * @return the listener
     */
    public static SlimEventListener getListener() {
        return listener;
    }

    /**
     * @return the savedPlaylistManager
     */
    public static SlimSavedPlaylistManager getSavedPlaylistManager() {
        return savedPlaylistManager;
    }

    /**
     * @return the favoritePlugin
     */
    public static SlimFavoritePlugin getFavoritePlugin() {
        return favoritePlugin;
    }

    /**
     * @return the player
     */
    public static SlimPlayer getPlayer() {
        return player;
    }

    /**
     * @return the folderBrowser
     */
    public static SlimFolderBrowser getFolderBrowser() {
        return folderBrowser;
    }

    /**
     * @return the radioPlugin
     */
    public static SlimRadioPlugin getRadioPlugin() {
        return radioPlugin;
    }
}
