package org.bff.squeezeserver.domain.app;

import org.bff.squeezeserver.domain.Album;
import org.bff.squeezeserver.domain.Genre;
import org.bff.squeezeserver.domain.XMLPlugin;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a podcast
 *
 * @author Bill Findeisen
 */
class AppPlugin extends XMLPlugin {

    private int count;
    private Collection<AppPluginItem> appItems;
    private boolean error;
    private String errorMessage;
    private String appId;
    private AvailableApp app;

    public AppPlugin(AvailableApp app) {
        this.setApp(app);
        this.appItems = new ArrayList<AppPluginItem>();
    }

    public void addAppItem(AppPluginItem item) {
        getAppItems().add(item);
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
    public Collection<AppPluginItem> getAppItems() {
        return appItems;
    }

    /**
     * @param appItems the radioItems to set
     */
    public void setAppItems(Collection<AppPluginItem> appItems) {
        this.appItems = appItems;
    }

    @Override
    public String getItemId() {
        return getAppId();
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
        throw new UnsupportedOperationException("Not supported yet.");
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public AvailableApp getApp() {
        return app;
    }

    public void setApp(AvailableApp app) {
        this.app = app;
    }
}
