package org.bff.slimserver.musicobjects.radio;

import java.awt.Image;
import java.net.URL;
import org.bff.slimserver.musicobjects.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a podcast
 * 
 * @author Bill Findeisen
 */
class SlimRadio extends RadioObject {

    private int count;
    private Collection<SlimRadioItem> radioItems;
    private boolean error;
    private String errorMessage;

    public SlimRadio(SlimAvailableRadio radio) {
        super(radio);
        radioItems = new ArrayList<SlimRadioItem>();
    }

    public void addRadioItem(SlimRadioItem item) {
        getRadioItems().add(item);
    }
    

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the radioItems
     */
    public Collection<SlimRadioItem> getRadioItems() {
        return radioItems;
    }

    /**
     * @param radioItems the radioItems to set
     */
    public void setRadioItems(Collection<SlimRadioItem> radioItems) {
        this.radioItems = radioItems;
    }

    @Override
    public String getItemId() {
        return getRadioId();
    }

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
        throw new UnsupportedOperationException("Not supported yet.");
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
    public String getType() {
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

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getCommand() {
        return getRadio().getCommand();
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
