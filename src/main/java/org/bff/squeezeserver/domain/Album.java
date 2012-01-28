package org.bff.squeezeserver.domain;

/**
 * Represents an album.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class Album extends PlayableItem {

    private String artist;
    private String year;
    private String artworkTrackId;
    private boolean compilation;

    public static final String RETURN_ALBUM = "l";
    public static final String RETURN_YEAR = "y";
    public static final String RETURN_ARTWORK_ID = "j";
    public static final String RETURN_TITLE = "t";
    public static final String RETURN_DISC = "i";
    public static final String RETURN_DISC_COUNT = "q";
    public static final String RETURN_COMPILATION = "w";
    public static final String RETURN_ARTIST = "a";
    public static final String RETURN_TEXT_KEY = "s";

    /**
     * id                  Album ID. Item delimiter.
     * <p/>
     * l 	  album             Album name, including SqueezeCenter's added "(N of M)" if the server is set to group multi disc albums together. See tag "title" for the unmodified value.
     * y 	  year              Album year. This is determined by SqueezeCenter based on the album tracks.
     * j 	  artwork_track_id 	Identifier of one of the album tracks, used by the server to display the album's artwork.
     * t 	  title             "Raw" album title as found in the album tracks ID3 tags, as opposed to "album". Note that "title" and "album" are identical if the server is set to group discs together.
     * i 	  disc              Disc number of this album. Only if the server is not set to group multi-disc albums together.
     * q 	  disccount         Number of discs for this album. Only if known.
     * w 	  compilation       1 if this album is a compilation.
     * a 	  artist            The album artist (depends on server configuration).
     * s 	  textkey           The album's "textkey" is the first letter of the sorting key.
     */

    /*
     * Can't do artist here because of performance reasons
     */
    public static final String RETURN_TAGS =
            RETURN_ALBUM +
                    RETURN_YEAR +
                    RETURN_ARTWORK_ID +
                    RETURN_COMPILATION;

    /**
     * Default constructor
     */
    public Album() {
        super();
    }

    /**
     * Constructor
     *
     * @param id   album id
     * @param name album name
     */
    public Album(String id, String name) {
        super(id, name);
    }

    /**
     * Constructor
     *
     * @param id     album id
     * @param name   album name
     * @param artist artist name
     */
    public Album(String id, String name, String artist) {
        super(id, name);
        this.artist = artist;
    }

    /**
     * Return the artist of the album
     * Check for null and use the database to get the artist if necessary.
     * There is a performance problem in loading the artist with the album
     * call sometimes.
     *
     * @return the artist
     */
    @Override
    public String getArtist() {
        return artist;
    }

    /**
     * Sets the artist for this album
     *
     * @param artist the artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
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
     * @return the year
     */
    @Override
    public String getYear() {
        return year;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * @param year the year to set
     */
    @Override
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @return the artworkTrackId
     */
    public String getArtworkTrackId() {
        return artworkTrackId;
    }

    /**
     * @param artworkTrackId the artworkTrackId to set
     */
    public void setArtworkTrackId(String artworkTrackId) {
        this.artworkTrackId = artworkTrackId;
    }

    public boolean isCompilation() {
        return compilation;
    }

    public void setCompilation(boolean compilation) {
        this.compilation = compilation;
    }

    @Override
    public int getLength() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAlbum() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public String getUrl() {
        return URL_PREFIX_ALBUM + getName();
    }
}
