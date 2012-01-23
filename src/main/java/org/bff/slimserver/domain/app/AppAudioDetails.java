/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.domain.app;

import org.bff.slimserver.domain.Album;
import org.bff.slimserver.domain.Genre;
import org.bff.slimserver.domain.XMLPlugin;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bfindeisen
 */
public class AppAudioDetails extends XMLPlugin {

    private int count;
    private boolean audio;
    private String bitrate;
    private String value;
    private String url;
    private String type;
    private String itemId;
    private AvailableApp app;
    private String imageUrl;
    private String subText;
    private String title;

    public AppAudioDetails(AvailableApp app, String appId) {
        setApp(app);
        setItemId(appId);
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
     * @return the audio
     */
    public boolean isAudio() {
        return audio;
    }

    /**
     * @param audio the audio to set
     */
    public void setAudio(boolean audio) {
        this.audio = audio;
    }

    /**
     * @return the bitrate
     */
    public String getBitrate() {
        return bitrate;
    }

    /**
     * @param bitrate the bitrate to set
     */
    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the type
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
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
    public Album getAlbum() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTitle() {
        return title == null ? getName() : getTitle();
    }

    @Override
    public boolean isRemote() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Genre getGenre() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setGenre(Genre genre) {
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
    public int getTrack() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTrack(int track) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URL getImageUrl() {
        try {
            return new URL(this.imageUrl);
        } catch (MalformedURLException ex) {
            Logger.getLogger(AppAudioDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Image getImage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the radio
     */
    public AvailableApp getApp() {
        return app;
    }

    /**
     * @param radio the radio to set
     */
    public void setApp(AvailableApp app) {
        this.app = app;
    }

    /**
     * @param imageURL the imageURL to set
     */
    public void setImageUrl(String imageURL) {
        this.imageUrl = imageURL;
    }

    /**
     * @return the subText
     */
    public String getSubText() {
        return subText;
    }

    /**
     * @param subText the subText to set
     */
    public void setSubText(String subText) {
        this.subText = subText;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getCommand() {
        return getApp().getCommand();
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
