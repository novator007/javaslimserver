/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.domain;

import org.bff.slimserver.domain.radio.AvailableRadio;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bfindeisen
 */
public class XMLBrowserAudioDetails extends XMLPlugin {

    private int count;
    private boolean audio;
    private String bitrate;
    private String value;
    private String url;
    private String type;
    private String itemId;
    private String subText;
    private URL imageUrl;
    private String title;
    private String command;
    private int enclosureLength;
    private String enclosureUrl;
    private String enclosureType;
    private String pubDate;
    private String description;
    private String link;
    private String explicit;
    private String totalDuration;
    private String subTitle;
    private String summary;
    private URL iconUrl;
    private boolean remote;
    private Image image;
    private ImageIcon smallIcon;
    private String xmlId;

    public XMLBrowserAudioDetails(String id, String command) {
        setItemId(id);
        setCommand(command);
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
        return -1;
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
    public String getTitle() {
        return title == null ? getName() : title;
    }

    @Override
    public boolean isRemote() {
        return this.remote;
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
    public int getTrack() {
        return -1;
    }

    @Override
    public void setTrack(int track) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URL getImageUrl() {
        return this.imageUrl;
    }

    @Override
    public Image getImage() {

        if (image == null && getImageUrl() != null) {
            try {
                image = ImageIO.read(getImageUrl());
            } catch (IOException ex) {
                Logger.getLogger(PlayableItem.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return this.image;
    }

    /**
     * Returns the small (25x25) icon for the radio.  There is some performance overhead with
     * this method the first time it is called.
     *
     * @return the {@link ImageIcon} for this radio
     */
    public ImageIcon getSmallIcon() {
        if (smallIcon == null) {
            try {
                smallIcon = new ImageIcon(new URL(getSmallIconURL()));
            } catch (Exception ex) {
                Logger.getLogger(AvailableRadio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return smallIcon;
    }

    /**
     * @param imageURL the imageURL to set
     */
    public void setImageUrl(URL imageURL) {
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

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @return the enclosureLength
     */
    public int getEnclosureLength() {
        return enclosureLength;
    }

    /**
     * @param enclosureLength the enclosureLength to set
     */
    public void setEnclosureLength(int enclosureLength) {
        this.enclosureLength = enclosureLength;
    }

    /**
     * @return the enclosureUrl
     */
    public String getEnclosureUrl() {
        return enclosureUrl;
    }

    /**
     * @param enclosureUrl the enclosureUrl to set
     */
    public void setEnclosureUrl(String enclosureUrl) {
        this.enclosureUrl = enclosureUrl;
    }

    /**
     * @return the enclosureType
     */
    public String getEnclosureType() {
        return enclosureType;
    }

    /**
     * @param enclosureType the enclosureType to set
     */
    public void setEnclosureType(String enclosureType) {
        this.enclosureType = enclosureType;
    }

    /**
     * @return the pubDate
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * @param pubDate the pubDate to set
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the explicit
     */
    public String getExplicit() {
        return explicit;
    }

    /**
     * @param explicit the explicit to set
     */
    public void setExplicit(String explicit) {
        this.explicit = explicit;
    }

    /**
     * @return the totalDuration
     */
    public String getTotalDuration() {
        return totalDuration;
    }

    /**
     * @param totalDuration the totalDuration to set
     */
    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    /**
     * @return the subTitle
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * @param subTitle the subTitle to set
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the iconUrl
     */
    public URL getIconUrl() {
        return iconUrl;
    }

    /**
     * @param iconUrl the iconUrl to set
     */
    public void setIconUrl(URL iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * @param remote the remote to set
     */
    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public String getSmallIconURL() {
        String replace = getIconUrl().toString().substring(getIconUrl().toString().lastIndexOf("."));
        return getIconUrl().toString().replace(replace, "_25x25_f" + replace);
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }
}
