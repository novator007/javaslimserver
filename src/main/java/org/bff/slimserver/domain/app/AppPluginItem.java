/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.domain.app;

import org.bff.slimserver.domain.Album;
import org.bff.slimserver.domain.Genre;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bfindeisen
 */
public class AppPluginItem extends AppPlugin {

    private boolean audio;
    private boolean containsItems;
    private URL imageUrl;
    private String title;
    private String type;
    private Collection<AppPluginItem> appItems;
    private String smallIconUrl;

    private static HashMap<String, ImageIcon> iconMap =
            new HashMap<String, ImageIcon>();

    public AppPluginItem(AvailableApp app) {
        super(app);
        setApp(app);
        this.smallIconUrl = getApp().getSmallIconURL().replace("_25x25_f", "_16x16_f");
        appItems = new ArrayList<AppPluginItem>();
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
     * @return the containsItems
     */
    public boolean isContainsItems() {
        return containsItems;
    }

    /**
     * @param containsItems the containsItems to set
     */
    public void setContainsItems(boolean containsItems) {
        this.containsItems = containsItems;
    }

    /**
     * @return the imageUrl
     */
    @Override
    public URL getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl the imageUrl to set
     */
    @Override
    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Title may sometimes be null.  If so you can use {@link getName} to get
     * a descriptiob of this radio item.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
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
    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the radioItems
     */
    @Override
    public Collection<AppPluginItem> getAppItems() {
        return appItems;
    }

    /**
     * @param radioItems the radioItems to set
     */
    @Override
    public void setAppItems(Collection<AppPluginItem> appItems) {
        this.appItems = appItems;
    }

    @Override
    public String getCommand() {
        return getApp().getCommand();
    }

    @Override
    public void addAppItem(AppPluginItem item) {
        getAppItems().add(item);
    }

    @Override
    public String getItemId() {
        return getAppId();
    }

    @Override
    public String getId() {
        return getItemId();
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

    /**
     * Overrides the equals method.  Returns true if the names are equal.
     *
     * @param object the object to compare with this
     * @return true if the codes are equal, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return (true);
        }

        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }

        AppPluginItem item = (AppPluginItem) object;
        if (this.getAppId().equals(item.getAppId())) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Overrides hashcode method
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) (getName() == null ? getTitle().length() : getName().length());
        hash = 31 * hash + (null == getName() ? getTitle().hashCode() : getName().hashCode());
        return (hash);
    }

    /**
     * Returns a 16x16 icon
     *
     * @return the smallIcon
     */
    @Override
    public ImageIcon getSmallIcon() {
        ImageIcon icon = iconMap.get(getSmallIconUrl());
        if (icon == null) {
            try {
                icon = new ImageIcon(new URL(getSmallIconUrl()));
                iconMap.put(getSmallIconUrl(), icon);
            } catch (MalformedURLException ex) {
                Logger.getLogger(AppPluginItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return icon;
    }

    private String getSmallIconUrl() {
        return this.smallIconUrl;
    }

    /**
     * @param smallIconUrl the smallIconUrl to set
     */
    public void setSmallIconUrl(String smallIconUrl) {
        this.smallIconUrl = smallIconUrl;
    }
}
