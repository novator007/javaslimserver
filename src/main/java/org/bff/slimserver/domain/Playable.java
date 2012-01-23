/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.domain;

import java.awt.*;
import java.net.URL;

/**
 * @author Bill
 */
public interface Playable {

    public String getName();

    public int getLength();

    public String getId();

    public String getArtist();

    public Album getAlbum();

    public String getTitle();

    public boolean isRemote();

    /**
     * Returns the {@link Genre} of the song
     *
     * @return the {@link Genre}
     */
    public Genre getGenre();

    /**
     * Sets the {@link Genre} of the song
     *
     * @param genre the {@link Genre} of the song
     */
    public void setGenre(Genre genre);

    /**
     * Returns the comment associated with the song
     *
     * @return the comment
     */
    public String getComment();

    /**
     * Sets the comment associated with this song
     *
     * @param comment the comment
     */
    public void setComment(String comment);

    /**
     * Returns the year of the song
     *
     * @return the year
     */
    public String getYear();

    /**
     * Sets the year of the song
     *
     * @param year the year
     */
    public void setYear(String year);

    /**
     * Returns the bitrate of the song
     *
     * @return the bitrate
     */
    public String getBitrate();

    /**
     * Sets the bitrate of the song
     *
     * @param bitrate the bitrate
     */
    public void setBitrate(String bitrate);

    /**
     * Returns the track number of the song
     *
     * @return the track number
     */
    public int getTrack();

    /**
     * Sets the track number of the song
     *
     * @param track the track number
     */
    public void setTrack(int track);

    public String getUrl();

    public Image getImage();

    public URL getImageUrl();

    public String getType();
}
