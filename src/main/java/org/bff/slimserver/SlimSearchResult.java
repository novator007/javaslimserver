package org.bff.slimserver;

import org.bff.slimserver.musicobjects.SlimTrack;
import org.bff.slimserver.musicobjects.SlimSong;
import org.bff.slimserver.musicobjects.SlimContributor;
import org.bff.slimserver.musicobjects.SlimArtist;
import org.bff.slimserver.musicobjects.SlimAlbum;
import java.util.Collection;
import org.bff.slimserver.musicobjects.SlimGenre;

/**
 * SlimSearchResults is returned from a search.  It contains collections of
 * music objects that match the search criteria.
 *
 * @author bfindeisen
 */
public class SlimSearchResult {

    private Collection<SlimArtist> artists;
    private Collection<SlimAlbum> albums;
    private Collection<SlimSong> items;
    private Collection<SlimContributor> contributors;
    private Collection<SlimTrack> tracks;
    private Collection<SlimGenre> genres;

    /**
     * A {@link Collection} of {@link SlimArtist}s that matched.
     * @return the collection of artists
     */
    public Collection<SlimArtist> getArtists() {
        return artists;
    }

    /**
     * Sets the {@link Collection} of &{link SlimArtist}s
     * @param artists the artists that matched
     */
    public void setArtists(Collection<SlimArtist> artists) {
        this.artists = artists;
    }

    /**
     * A {@link Collection} of {@link SlimAlbum}s that matched.
     * @return the collection of albums
     */
    public Collection<SlimAlbum> getAlbums() {
        return albums;
    }

    /**
     * Sets the {@link Collection} of &{link SlimAlbum}s
     * @param albums the albums that matched
     */
    public void setAlbums(Collection<SlimAlbum> albums) {
        this.albums = albums;
    }

    /**
     * A {@link Collection} of {@link SlimSong}s that matched.
     * @return the collection of songs
     */
    public Collection<SlimSong> getSongs() {
        return items;
    }

    /**
     * Sets the {@link Collection} of &{link SlimSong}s
     * @param songs the songs that matched
     */
    public void setSongs(Collection<SlimSong> songs) {
        this.items = songs;
    }

    /**
     * A {@link Collection} of {@link SlimContributor}s that matched.
     * @return the collection of contributors
     */
    public Collection<SlimContributor> getContributors() {
        return contributors;
    }

    /**
     * Sets the {@link Collection} of &{link SlimContributor}s
     * @param contributors the contributors that matched
     */
    public void setContributors(Collection<SlimContributor> contributors) {
        this.contributors = contributors;
    }

    /**
     * A {@link Collection} of {@link SlimTrack}s that matched.
     * @return the collection of tracks
     */
    public Collection<SlimTrack> getTracks() {
        return tracks;
    }

    /**
     * Sets the {@link Collection} of &{link SlimTrack}s
     * @param tracks the tracks that matched
     */
    public void setTracks(Collection<SlimTrack> tracks) {
        this.tracks = tracks;
    }

    /**
     * @return the genres
     */
    public Collection<SlimGenre> getGenres() {
        return genres;
    }

    /**
     * @param genres the genres to set
     */
    public void setGenres(Collection<SlimGenre> genres) {
        this.genres = genres;
    }
}
