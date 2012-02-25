/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver.domain;

import java.net.URL;

/**
 * @author bfindeisen
 */
public class XMLBrowserAudioDetails extends XMLPlugin {
    private int count;
    private boolean audio;
    private String value;
    private String itemId;
    private String subText;
    private String title;
    private String command;
    private int enclosureLength;
    private String enclosureUrl;
    private String enclosureType;
    private String pubDate;
    private String description;
    private String link;
    private String explicit;
    private String duration;
    private String subTitle;
    private String summary;
    private URL iconUrl;
    private boolean remote;
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
     * @return the duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(String duration) {
        this.duration = duration;
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
