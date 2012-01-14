package org.bff.slimserver.domain;

/**
 * Represents an artist.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class Artist extends PlayableItem {

    /**
     * Default constructor
     */
    public Artist() {
        super();
    }

    /**
     * Constructor
     *
     * @param id   the artist id
     * @param name the artist name
     */
    public Artist(String id, String name) {
        super(id, name);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String getArtist() {
        return this.getName();
    }

    @Override
    public Album getAlbum() {
        return null;
    }

    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public String getUrl() {
        return URL_PREFIX_ARTIST + getName();
    }

    public void setArtist() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return getName();
    }
}
