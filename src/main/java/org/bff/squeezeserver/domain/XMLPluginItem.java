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
    private String imageUrl;
    private String title;
    private String type;
    private int count;
    private Collection<XMLPluginItem> xmlItems;
    private boolean error;
    private String errorMessage;
    private SQUEEZE_TYPE slimType;
    private String url;

    public XMLPluginItem() {
        xmlItems = new ArrayList<XMLPluginItem>();
    }

    public XMLPluginItem(String id, String name) {
        super(id, name);
        xmlItems = new ArrayList<XMLPluginItem>();
    }

    /**
     * @return the slimType
     */
    public SQUEEZE_TYPE getSlimType() {
        if (slimType == null) {
            slimType = SQUEEZE_TYPE.OTHER;
        }
        return slimType;
    }

    /**
     * @param slimType the slimType to set
     */
    public void setSlimType(SQUEEZE_TYPE slimType) {
        this.slimType = slimType;
    }

    /**
     * @return the url
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * @return the xmlItems
     */
    public Collection<XMLPluginItem> getXmlItems() {
        return xmlItems;
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
        if (getUrl().startsWith(URL_PREFIX_ARTIST)) {
            setSlimType(SQUEEZE_TYPE.ARTIST);
        } else if (getUrl().startsWith(URL_PREFIX_ALBUM)) {
            setSlimType(SQUEEZE_TYPE.ALBUM);
        } else if (getUrl().startsWith(URL_PREFIX_GENRE)) {
            setSlimType(SQUEEZE_TYPE.GENRE);
        } else if (getUrl().startsWith(URL_PREFIX_YEAR)) {
            setSlimType(SQUEEZE_TYPE.YEAR);
        } else {
            setSlimType(SQUEEZE_TYPE.OTHER);
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

    public void addXMLItem(XMLPluginItem item) {
        getXmlItems().add(item);
    }

    /**
     * @return the imageUrl
     */
    @Override
    public URL getImageUrl() {
        try {
            return new URL(imageUrl);
        } catch (MalformedURLException ex) {
            Logger.getLogger(XMLPluginItem.class.getName()).log(Level.SEVERE, null, ex);
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
