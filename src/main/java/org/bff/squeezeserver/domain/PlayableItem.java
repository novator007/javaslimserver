package org.bff.squeezeserver.domain;

import org.bff.squeezeserver.domain.radio.Radio;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a item in a SqueezeCenter
 *
 * @author Bill Findeisen
 */
public class PlayableItem implements Playable {
    private String id;
    private String name;
    private Album album;
    private Genre genre;
    private String comment;
    private String year;
    private String url;
    private String bitrate;
    private URL imageUrl;
    private int rating;
    private boolean remote;
    private int track;
    private int length;
    private String type;
    private Image image;
    private ImageIcon smallIcon;

    public static final String URL_PREFIX_ALBUM = "db:album.titlesearch=";
    public static final String URL_PREFIX_ARTIST = "db:contributor.namesearch=";
    public static final String URL_PREFIX_GENRE = "db:genre.namesearch=";
    public static final String URL_PREFIX_YEAR = "db:year.id=";

    /**
     * Default constructor
     */
    public PlayableItem() {
    }

    public PlayableItem(String id) {
        this.id = id;
    }

    public PlayableItem(String id, String name) {
        this(id);
        this.name = name;
    }

    /**
     * Returns the {@link Genre} of the item
     *
     * @return the {@link Genre}
     */
    @Override
    public Genre getGenre() {
        return genre;
    }

    /**
     * Sets the {@link Genre} of the item
     *
     * @param genre the {@link Genre} of the item
     */
    @Override
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    /**
     * Returns the comment associated with the item
     *
     * @return the comment
     */
    @Override
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment associated with this item
     *
     * @param comment the comment
     */
    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the year of the item
     *
     * @return the year
     */
    @Override
    public String getYear() {
        return year;
    }

    /**
     * Sets the year of the item
     *
     * @param year the year
     */
    @Override
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Returns the bitrate of the item
     *
     * @return the bitrate
     */
    @Override
    public String getBitrate() {
        return bitrate;
    }

    /**
     * Sets the bitrate of the item
     *
     * @param bitrate the bitrate
     */
    @Override
    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    /**
     * Returns the track number of the item
     *
     * @return the track number
     */
    @Override
    public int getTrack() {
        return track;
    }

    /**
     * Sets the track number of the item
     *
     * @param track the track number
     */
    @Override
    public void setTrack(int track) {
        this.track = track;
    }

    /**
     * Returns the length of the item in seconds
     *
     * @return the length of the item
     */
    @Override
    public int getLength() {
        return length;
    }

    /**
     * Sets the length of the item
     *
     * @param length the length
     */
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return (getId() + " - "
                + (getArtist() == null ? "" : getArtist())
                + " - "
                + (getAlbum() == null ? "" : getAlbum().getName())
                + " - "
                + getName());
    }

    /**
     * Returns the {@link Artist} of the item
     *
     * @return the {@link Artist}
     */
    @Override
    public String getArtist() {
        return getAlbum() != null ? getAlbum().getArtist() : "";
    }

    /**
     * Returns the {@link Album} of the item
     *
     * @return the {@link Album}
     */
    @Override
    public Album getAlbum() {
        return album;
    }

    /**
     * Sets the {@link Album} of the item
     *
     * @param album the {@link Album} of the item
     */
    public void setAlbum(Album album) {
        this.album = album;
    }

    /**
     * Sets the {@link Album} of the item
     *
     * @param id   album is
     * @param name album name
     */
    public void setAlbum(String id, String name) {
        this.album = new Album(id, name);
    }

    /**
     * Returns the file url of the item
     *
     * @return the url
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * Sets the file url of the item
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the url link to the cover art for this item's album
     *
     * @return the image url
     */
    @Override
    public URL getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the url link to the cover art for this item's album
     *
     * @param imageUrl the image url
     */
    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the image.  Returns null if the image cannot be generated.
     *
     * @return
     * @throws IOException
     */
    @Override
    public Image getImage() throws IOException {

        if (image == null && getImageUrl() != null) {
            image = ImageIO.read(getImageUrl());
        }
        return image;
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
                Logger.getLogger(Radio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return smallIcon;
    }

    /**
     * Returns the url for a small icon.  Will return null if the image url is null.
     *
     * @return
     */
    public String getSmallIconURL() {
        if (getImageUrl() == null) {
            return null;
        }
        String replace = getImageUrl().toString().substring(getImageUrl().toString().lastIndexOf("."));
        return getImageUrl().toString().replace(replace, "_25x25_f" + replace);
    }

    /**
     * Same as name.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return (true);
        }

        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }

        try {
            if (this.getId().equals(((PlayableItem) object).getId())) {
                return (true);
            } else {
                return (false);
            }
        } catch (Exception e) {
            return false;
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
        hash = 31 * hash + (int) getName().length();
        hash = 31 * hash + (null == getName() ? 0 : getName().hashCode());
        return (hash);
    }

    /**
     * Returns the item's rating, -1 if not known
     *
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the item rating
     *
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPlaylistId() {
        return getId();
    }

    /**
     * @return the remote
     */
    @Override
    public boolean isRemote() {
        return remote;
    }

    /**
     * @param remote the remote to set
     */
    public void setRemote(boolean remote) {
        this.remote = remote;
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
     * Returns the name for this object
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for this object
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the id for this object
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id for this object
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }
}
