/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.musicobjects;

import org.bff.slimserver.*;
import java.util.Collection;

/**
 *
 * @author bfindeisen
 */
public class SlimSavedPlaylist extends SlimObject {

    private SlimServer slimServer;
    
    private Collection<SlimPlaylistItem> objects;

    private String url;

    /** Creates a new instance of SlimPlaylist */
    public SlimSavedPlaylist(SlimServer server) {
        setSlimServer(server);
    }

    public Collection<SlimPlaylistItem> getItems() {
        return objects;
    }

    public void setItems(Collection<SlimPlaylistItem> objects) {
        this.objects = objects;
    }
    
    public SlimServer getSlimServer() {
        return slimServer;
    }

    public void setSlimServer(SlimServer slimServer) {
        this.slimServer = slimServer;
    }

    /**
     * Returns the path to the playlist
     * @return the path
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the path to the playlist
     * @param path the path to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
