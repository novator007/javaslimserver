package org.bff.slimserver.musicobjects;

/**
 * Represents a genre
 * @author Bill Findeisen
 */
public class SlimGenre extends SlimPlayableItem {

    /**
     * Default constructor
     */
    public SlimGenre() {
        super();
    }

    /**
     * Constructor
     * @param id genre id
     * @param name genre name
     */
    public SlimGenre(String id, String name) {
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
    public SlimAlbum getAlbum() {
        return null;
    }

    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public SlimGenre getGenre() {
        return this;
    }

    @Override
    public void setGenre(SlimGenre genre) {
        
    }


    @Override
    public String getUrl() {
        return URL_PREFIX_GENRE + getName();
    }
}
