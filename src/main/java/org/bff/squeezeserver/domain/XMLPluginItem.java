/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bfindeisen
 */
public class XMLPluginItem extends PlayableItem {

    private boolean audio;
    private boolean containsItems;
    private String title;
    private String type;
    private int count;
    private boolean error;
    private String errorMessage;
    private SQUEEZE_TYPE squeezeType;
    private String url;

    public XMLPluginItem() {
    }

    public XMLPluginItem(String id, String name) {
        super(id, name);
    }

    /**
     * @return the squeezeType
     */
    public SQUEEZE_TYPE getSqueezeType() {
        if (squeezeType == null) {
            squeezeType = SQUEEZE_TYPE.OTHER;
        }
        return squeezeType;
    }

    /**
     * @param squeezeType the squeezeType to set
     */
    public void setSqueezeType(SQUEEZE_TYPE squeezeType) {
        this.squeezeType = squeezeType;
    }

    /**
     * @return the url
     */
    @Override
    public String getUrl() {
        return url;
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

    public enum SQUEEZE_TYPE {

        ARTIST,
        ALBUM,
        PODCAST,
        GENRE,
        YEAR,
        FOLDER,
        OTHER
    }

    ;

    /**
     * @param url the url to set
     */
    @Override
    public void setUrl(String url) {
        this.url = url;
        if (url != null) {
            if (getUrl().startsWith(URL_PREFIX_ARTIST)) {
                setSqueezeType(SQUEEZE_TYPE.ARTIST);
            } else if (getUrl().startsWith(URL_PREFIX_ALBUM)) {
                setSqueezeType(SQUEEZE_TYPE.ALBUM);
            } else if (getUrl().startsWith(URL_PREFIX_GENRE)) {
                setSqueezeType(SQUEEZE_TYPE.GENRE);
            } else if (getUrl().startsWith(URL_PREFIX_YEAR)) {
                setSqueezeType(SQUEEZE_TYPE.YEAR);
            } else {
                setSqueezeType(SQUEEZE_TYPE.OTHER);
            }
        }
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

        XMLPluginItem item = (XMLPluginItem) object;
        if (this.getId().equals(item.getId()) &&
                this.getUrl().equals(item.getUrl())) {
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
