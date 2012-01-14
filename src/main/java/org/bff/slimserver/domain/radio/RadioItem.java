/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.domain.radio;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

import org.bff.slimserver.domain.Album;
import org.bff.slimserver.domain.Genre;

/**
 * @author bfindeisen
 */
public class RadioItem extends Radio {

    private boolean audio;
    private boolean containsItems;
    private URL imageUrl;
    private String title;
    private String type;
    private Collection<RadioItem> radioItems;
    private String smallIconUrl;

    private static HashMap<String, ImageIcon> iconMap =
            new HashMap<String, ImageIcon>();

    public RadioItem(AvailableRadio radio) {
        super(radio);
        setRadio(radio);
        setSmallIconUrl(getRadio().getSmallIconURL().replace("_25x25_f", "_16x16_f"));
        radioItems = new ArrayList<RadioItem>();
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
    public URL getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl the imageUrl to set
     */
    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * A description of this radio item.
     *
     * @return the title
     */
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
     * @return the radioItems
     */
    public Collection<RadioItem> getRadioItems() {
        return radioItems;
    }

    /**
     * @param radioItems the radioItems to set
     */
    public void setRadioItems(Collection<RadioItem> radioItems) {
        this.radioItems = radioItems;
    }

    public String getCommand() {
        return getRadio().getCommand();
    }

    public void addRadioItem(RadioItem item) {
        getRadioItems().add(item);
    }

    @Override
    public String getItemId() {
        return getRadioId();
    }

    @Override
    public String getId() {
        return getItemId();
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public String getArtist() {
        return null;
    }

    @Override
    public Album getAlbum() {
        return null;
    }

    @Override
    public boolean isRemote() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Genre getGenre() {
        return null;
    }

    @Override
    public void setGenre(Genre genre) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getComment() {
        return null;
    }

    @Override
    public void setComment(String comment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getYear() {
        return null;
    }

    @Override
    public void setYear(String year) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getBitrate() {
        return null;
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
        return null;
    }

    /**
     * Overrides the equals method.  Returns true if the names are equal.
     *
     * @param object the object to compare with this
     * @return true if the codes are equal, false otherwise
     */
    public boolean equals(Object object) {
        if (this == object) {
            return (true);
        }

        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }

        RadioItem item = (RadioItem) object;
        if (this.getRadioId().equals(item.getRadioId())) {
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
    public ImageIcon getSmallIcon() {
        ImageIcon icon = iconMap.get(getSmallIconUrl());
        if (icon == null) {
            try {
                icon = new ImageIcon(new URL(getSmallIconUrl()));
                iconMap.put(getSmallIconUrl(), icon);
            } catch (MalformedURLException ex) {
                Logger.getLogger(RadioItem.class.getName()).log(Level.SEVERE, null, ex);
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
