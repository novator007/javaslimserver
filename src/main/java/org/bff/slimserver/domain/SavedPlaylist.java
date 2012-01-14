/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.domain;

import org.bff.slimserver.*;

import java.util.Collection;

/**
 * @author bfindeisen
 */
public class SavedPlaylist extends PlayableItem {

    private SqueezeServer squeezeServer;

    private Collection<PlaylistItem> objects;

    private String url;

    /**
     * Creates a new instance of Playlist
     */
    public SavedPlaylist(SqueezeServer server) {
        setSqueezeServer(server);
    }

    public Collection<PlaylistItem> getItems() {
        return objects;
    }

    public void setItems(Collection<PlaylistItem> objects) {
        this.objects = objects;
    }

    public SqueezeServer getSqueezeServer() {
        return squeezeServer;
    }

    public void setSqueezeServer(SqueezeServer squeezeServer) {
        this.squeezeServer = squeezeServer;
    }

    /**
     * Returns the path to the playlist
     *
     * @return the path
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the path to the playlist
     *
     * @param url the path to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
