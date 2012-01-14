/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.domain;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * @author bfindeisen
 */
public class PodcastAudioDetails extends XMLBrowserAudioDetails {

    private int count;
    private String title;
    private int enclosureLength;
    private String enclosureUrl;
    private String enclosureType;
    private String pubDate;
    private String description;
    private String link;
    private String explicit;
    private URL imageUrl;
    private String totalDuration;
    private String subTitle;
    private String summary;
    private String podcastId;
    private String type;
    private String command;

    public PodcastAudioDetails(String id, String command) {
        super(id, command);
        this.command = command;
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
     * @return the enclosureLength
     */
    public int getEnclosureLength() {
        return enclosureLength;
    }

    @Override
    public int getLength() {
        return getEnclosureLength();
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
     * @return the pubDat
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * @param pubDat the pubDat to set
     */
    public void setPubDate(String pubDat) {
        this.pubDate = pubDat;
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
     * @return the image
     */
    public Image getImage() {
        try {
            return ImageIO.read(getImageUrl());
        } catch (IOException ex) {
            Logger.getLogger(PlayableItem.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * @return the duration
     */
    public String getDuration() {
        return totalDuration;
    }

    /**
     * @param duration the duration to set
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

    public Playable getPlayableObject() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getArtist() {
        return null;
    }

    public Album getAlbum() {
        return null;
    }

    public Genre getGenre() {
        return null;
    }

    public void setGenre(Genre genre) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getComment() {
        return null;
    }

    public void setComment(String comment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getYear() {
        return null;
    }

    public void setYear(String year) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getBitrate() {
        return null;
    }

    public void setBitrate(String bitrate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getTrack() {
        return -1;
    }

    public void setTrack(int track) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRemote() {
        return true;
    }

    /**
     * @return the podcastId
     */
    public String getPodcastId() {
        return podcastId;
    }

    /**
     * @param podcastId the podcastId to set
     */
    public void setPodcastId(String podcastId) {
        this.podcastId = podcastId;
    }

    @Override
    public String getItemId() {
        return getPodcastId();
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

    @Override
    public String getUrl() {
        return getEnclosureUrl();
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

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
