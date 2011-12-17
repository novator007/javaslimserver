package org.bff.slimserver.musicobjects;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.bff.slimserver.musicobjects.radio.SlimAvailableRadio;

/**
 * Represents a item in a SqueezeCenter
 * 
 * @author Bill Findeisen
 */
public class SlimPlayableItem extends SlimObject implements SlimPlayableObject {

    private SlimAlbum album;
    private SlimGenre genre;
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

    /**
     * Default constructor
     */
    public SlimPlayableItem() {
    }

    public SlimPlayableItem(String id) {
        super(id);
    }

    public SlimPlayableItem(String id, String name) {
        super(id, name);
    }

    /**
     * Returns the {@link SlimGenre} of the item
     * @return the {@link SlimGenre}
     */
    @Override
    public SlimGenre getGenre() {
        return genre;
    }

    /**
     * Sets the {@link SlimGenre} of the item
     * @param genre the {@link SlimGenre} of the item
     */
    @Override
    public void setGenre(SlimGenre genre) {
        this.genre = genre;
    }

    /**
     * Returns the comment associated with the item
     * @return the comment
     */
    @Override
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment associated with this item
     * @param comment the comment
     */
    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the year of the item
     * @return the year
     */
    @Override
    public String getYear() {
        return year;
    }

    /**
     * Sets the year of the item
     * @param year the year
     */
    @Override
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Returns the bitrate of the item
     * @return the bitrate
     */
    @Override
    public String getBitrate() {
        return bitrate;
    }

    /**
     * Sets the bitrate of the item
     * @param bitrate the bitrate
     */
    @Override
    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    /**
     * Returns the track number of the item
     * @return the track number
     */
    @Override
    public int getTrack() {
        return track;
    }

    /**
     * Sets the track number of the item
     * @param track the track number
     */
    @Override
    public void setTrack(int track) {
        this.track = track;
    }

    /**
     * Returns the length of the item in seconds
     * @return the length of the item
     */
    @Override
    public int getLength() {
        return length;
    }

    /**
     * Sets the length of the item
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
     * Returns the {@link SlimArtist} of the item
     * @return the {@link SlimArtist}
     */
    @Override
    public String getArtist() {
        return getAlbum() != null ? getAlbum().getArtist() : "";
    }

    /**
     * Returns the {@link SlimAlbum} of the item
     * @return the {@link SlimAlbum}
     */
    @Override
    public SlimAlbum getAlbum() {
        return album;
    }

    /**
     * Sets the {@link SlimAlbum} of the item
     * @param album the {@link SlimAlbum} of the item
     */
    public void setAlbum(SlimAlbum album) {
        this.album = album;
    }

    /**
     * Sets the {@link SlimAlbum} of the item
     * @param id album is
     * @param name album name
     */
    public void setAlbum(String id, String name) {
        this.album = new SlimAlbum(id, name);
    }

    /**
     * Returns the file url of the item
     * @return the url
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * Sets the file url of the item
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the url link to the cover art for this item's album
     * <b>This on is very buggy</b>
     * @param dimension the desired dimension
     * @return the image url

    public String getImageUrl(int dimension) {
    String dim = Integer.toString(dimension);

    return getImageUrl().replaceAll(".jpg", "_" + dim + "x" + dim + "_o");
    }
     */
    /**
     * Returns the url link to the cover art for this item's album
     * @return the image url
     */
    @Override
    public URL getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the url link to the cover art for this item's album
     * @param imageUrl the image url
     */
    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public Image getImage() {

        if (image == null && getImageUrl() != null) {
            try {
                return ImageIO.read(getImageUrl());
            } catch (IOException ex) {
                Logger.getLogger(SlimPlayableItem.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return null;
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
                Logger.getLogger(SlimAvailableRadio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return smallIcon;
    }

    public String getSmallIconURL() {
        String replace = getImageUrl().toString().substring(getImageUrl().toString().lastIndexOf("."));
        return getImageUrl().toString().replace(replace, "_25x25_f" + replace);
    }

    /**
     * Same as name.
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
        
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Returns the item's rating, -1 if not known
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the item rating
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
}
