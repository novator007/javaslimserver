/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.exception.SqueezeException;
import org.bff.slimserver.monitor.EventListener;

/**
 * @author bill
 */
public abstract class Base {

    public Base() {
        try {
            Controller.getInstance().loadSongs();
        } catch (SqueezeException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the DATABASE
     */
    public static Database getDatabase() {
        return Controller.getInstance().getDatabase();
    }

    /**
     * @return the SQUEEZE_SERVER
     */
    public static SqueezeServer getSqueezeServer() {
        return Controller.getInstance().getSqueezeServer();
    }

    /**
     * @return the PLAYLIST
     */
    public static Playlist getPlaylist() {
        return Controller.getInstance().getPlaylist();
    }

    /**
     * @return the LISTENER
     */
    public static EventListener getListener() {
        return Controller.getInstance().getListener();
    }

    /**
     * @return the SAVED_PLAYLIST_MANAGER
     */
    public static SavedPlaylistManager getSavedPlaylistManager() {
        return Controller.getInstance().getSavedPlaylistManager();
    }

    /**
     * @return the FAVORITE_PLUGIN
     */
    public static FavoritePlugin getFavoritePlugin() {
        return Controller.getInstance().getFavoritePlugin();
    }

    /**
     * @return the PLAYER
     */
    public static Player getPlayer() {
        return Controller.getInstance().getPlayer();
    }

    /**
     * @return the FOLDER_BROWSER
     */
    public static FolderBrowser getFolderBrowser() {
        return Controller.getInstance().getFolderBrowser();
    }

    /**
     * @return the radioPlugin
     */
    public static RadioPlugin getRadioPlugin() {
        return Controller.getInstance().getRadioPlugin();
    }
}
