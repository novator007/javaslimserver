package org.bff.slimserver.domain;

/**
 * Represents a genre
 *
 * @author Bill Findeisen
 */
public class Genre extends PlayableItem {

    /**
     * Default constructor
     */
    public Genre() {
        super();
    }

    /**
     * Constructor
     *
     * @param id   genre id
     * @param name genre name
     */
    public Genre(String id, String name) {
        super(id, name);
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
        return getName();
    }

    @Override
    public Genre getGenre() {
        return this;
    }

    @Override
    public void setGenre(Genre genre) {

    }


    @Override
    public String getUrl() {
        return URL_PREFIX_GENRE + getName();
    }
}
