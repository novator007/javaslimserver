/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.squeezeserver.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bfindeisen
 */
public class PodcastItem extends Podcast {
    private boolean audio;
    private boolean containsItems;
    private String imageUrl;
    private String title;
    private String type;

    public PodcastItem(String id, String name) {
        super(id, name);
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
        try {
            return new URL(imageUrl);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PodcastItem.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * @param imageUrl the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
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

    @Override
    public String toString() {
        return getName() == null ? getTitle() : getName();
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

        PodcastItem item = (PodcastItem) object;
        if (this.getId().equals(item.getId())) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Overrides hashCode method
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
}
