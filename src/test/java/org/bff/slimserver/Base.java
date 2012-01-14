/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bff.slimserver.exception.SqueezeException;
import org.bff.slimserver.monitor.EventListener;

/**
 * @author bill
 */
public abstract class Base {

    private static final SqueezeServer SQUEEZE_SERVER = Controller.getInstance().getSqueezeServer();
    private static final Player PLAYER = new ArrayList<Player>(Controller.getInstance().getSqueezeServer().getSlimPlayers()).get(0);
    private static final Playlist PLAYLIST = new Playlist(getPLAYER());
    private static final EventListener LISTENER = new EventListener(PLAYER);
    private static final Database DATABASE = new Database(getSQUEEZE_SERVER());
    private static final SavedPlaylistManager SAVED_PLAYLIST_MANAGER = new SavedPlaylistManager(SQUEEZE_SERVER);
    private static final FavoritePlugin FAVORITE_PLUGIN = new FavoritePlugin(SQUEEZE_SERVER);
    private static final FolderBrowser FOLDER_BROWSER = new FolderBrowser(getSQUEEZE_SERVER());
    private static final RadioPlugin radioPlugin = new RadioPlugin(getSQUEEZE_SERVER());
    ;

    static {
        try {
            Controller.getInstance().loadSongs();
        } catch (SqueezeException ex) {
            Logger.getLogger(Base.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the DATABASE
     */
    public static Database getDATABASE() {
        return DATABASE;
    }

    /**
     * @return the SQUEEZE_SERVER
     */
    public static SqueezeServer getSQUEEZE_SERVER() {
        return SQUEEZE_SERVER;
    }

    /**
     * @return the PLAYLIST
     */
    public static Playlist getPLAYLIST() {
        return PLAYLIST;
    }

    /**
     * @return the LISTENER
     */
    public static EventListener getLISTENER() {
        return LISTENER;
    }

    /**
     * @return the SAVED_PLAYLIST_MANAGER
     */
    public static SavedPlaylistManager getSAVED_PLAYLIST_MANAGER() {
        return SAVED_PLAYLIST_MANAGER;
    }

    /**
     * @return the FAVORITE_PLUGIN
     */
    public static FavoritePlugin getFAVORITE_PLUGIN() {
        return FAVORITE_PLUGIN;
    }

    /**
     * @return the PLAYER
     */
    public static Player getPLAYER() {
        return PLAYER;
    }

    /**
     * @return the FOLDER_BROWSER
     */
    public static FolderBrowser getFOLDER_BROWSER() {
        return FOLDER_BROWSER;
    }

    /**
     * @return the radioPlugin
     */
    public static RadioPlugin getRadioPlugin() {
        return radioPlugin;
    }
}
