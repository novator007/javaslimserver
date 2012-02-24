package org.bff.squeezeserver.domain;

import java.net.MalformedURLException;

/**
 * Represents an item in a playlist
 *
 * @author Bill Findeisen
 */
public class PlaylistItem extends PlayableItem {

    private int playlistIndex;
    private int duration;

    /**
     * Default constructor
     */
    public PlaylistItem() {
    }

    /**
     * Constructor taking a {@link Playable} to fill details.  Remember to
     * set the playlist index.
     *
     * @param playable {@link Playable} in the playlist
     */
    public PlaylistItem(Playable playable) {
        setAlbum(playable.getAlbum());
        setBitrate(playable.getBitrate());
        setComment(playable.getComment());
        setGenre(playable.getGenre());
        setId(playable.getId());
        setLength(playable.getLength());
        setName(playable.getName());
        setTrack(playable.getTrack());
        setUrl(playable.getUrl());
        setYear(playable.getYear());
        try {
            setImageUrl(playable.getImageUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        setRemote(playable.isRemote());
        setType(playable.getType());
    }

    /**
     * Returns the playlist position
     *
     * @return the playlist index
     */
    public int getPlaylistIndex() {
        return playlistIndex;
    }

    /**
     * Sets the playlist position
     *
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

        PlaylistItem item = (PlaylistItem) object;

        if (this.getPlaylistIndex() == item.getPlaylistIndex()
                && this.getId().equalsIgnoreCase(item.getId())) {
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
        hash = 31 * hash + getName().length();
        hash = 31 * hash + (null == getName() ? 0 : getName().hashCode());
        return (hash);
    }

    /**
     * Duration in seconds if currently playing.
     *
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration
     *
     * @param duration the duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Playable getPlayableItem() {
        return this;
    }
}
