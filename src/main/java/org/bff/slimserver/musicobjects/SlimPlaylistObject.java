/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.musicobjects;

/**
 *
 * @author Bill
 */
public interface SlimPlaylistObject extends SlimPlayableObject {

    /**
     * Returns the playlist postition
     * @return the playlist index
     */
    public int getPlaylistIndex();

    /**
     * Sets the playlist position
     * @param playlistIndex the playlist index
     */
    public void setPlaylistIndex(int playlistIndex);

    public SlimPlayableObject getPlayableObject();

    public String getPlaylistId();

}
