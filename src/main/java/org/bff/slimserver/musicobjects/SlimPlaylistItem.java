package org.bff.slimserver.musicobjects;

/**
 * Represents an item in a playlist
 * 
 * @author Bill Findeisen
 */
public class SlimPlaylistItem extends SlimPlayableItem {

    private int playlistIndex;
    private int duration;

    /**
     * Default constructor
     */
    public SlimPlaylistItem() {
    }

    /**
     * Constructor taking a {@link SlimPlayableObject} to fill details.  Remember to
     * set the playlist index.
     * 
     * @param object {@link SlimPlayableObject} in the playlist
     */
    public SlimPlaylistItem(SlimPlayableObject object) {
        setAlbum(object.getAlbum());
        setBitrate(object.getBitrate());
        setComment(object.getComment());
        setGenre(object.getGenre());
        setId(object.getId());
        setLength(object.getLength());
        setName(object.getName());
        setTrack(object.getTrack());
        setUrl(object.getUrl());
        setYear(object.getYear());
        setImageUrl(object.getImageUrl());
        setRemote(object.isRemote());
        setType(object.getType());
    }

    /**
     * Returns the playlist position
     * @return the playlist index
     */
    public int getPlaylistIndex() {
        return playlistIndex;
    }

    /**
     * Sets the playlist position
     * @param playlistIndex the playlist index
     */
    public void setPlaylistIndex(int playlistIndex) {
        this.playlistIndex = playlistIndex;
    }

    @Override
    public String toString() {
        return getPlaylistIndex() + " " + super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return (true);
        }

        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }

        SlimPlaylistItem item = (SlimPlaylistItem) object;

        if (this.getPlaylistIndex() == item.getPlaylistIndex()
                && this.getId().equalsIgnoreCase(item.getId())) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Overrides hashCode method
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + getName().length();
        hash = 31 * hash + (null == getName() ? 0 : getName().hashCode());
        return (hash);
    }

    /**
     * Duration in seconds if currently playing.
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration
     * @param duration the duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public SlimPlayableObject getPlayableObject() {
        return this;
    }
}
