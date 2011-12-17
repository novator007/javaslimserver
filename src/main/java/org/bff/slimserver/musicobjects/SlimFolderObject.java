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
public class SlimFolderObject extends SlimPlayableItem {

    private String fileName;
    private OBJECTTYPE objectType;

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the objectType
     */
    public OBJECTTYPE getObjectType() {
        return objectType;
    }

    /**
     * @param objectType the objectType to set
     */
    public void setObjectType(OBJECTTYPE objectType) {
        this.objectType = objectType;
    }

    //"track", "folder", "playlist", or "unknown"
    public enum OBJECTTYPE {

        TRACK("track"),
        FOLDER("folder"),
        PLAYLIST("playlist"),
        UNKNOWN("unknown");
        private final String description;

        OBJECTTYPE(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    };

    @Override
    public String getName() {
        return super.getName() == null ? getFileName() : getName();
    }

    @Override
    public int getLength() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getArtist() {
        return "";
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

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return (true);
        }

        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }
        
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
