/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.musicobjects;

import java.awt.Image;
import java.net.URL;

/**
 *
 * @author Bill
 */
public class SlimFolder extends SlimPlayableItem {

    public enum TYPE {

        TRACK,
        FOLDER,
        PLAYLIST,
        UNKNOWN
    };

    @Override
    public int getLength() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getArtist() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SlimAlbum getAlbum() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public boolean isRemote() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SlimGenre getGenre() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setGenre(SlimGenre genre) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getComment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setComment(String comment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getYear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setYear(String year) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getBitrate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBitrate(String bitrate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getTrack() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTrack(int track) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getUrl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Image getImage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URL getImageUrl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
