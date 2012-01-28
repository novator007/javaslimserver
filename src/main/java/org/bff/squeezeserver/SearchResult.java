package org.bff.squeezeserver;

import org.bff.squeezeserver.domain.*;

import java.util.Collection;

/**
 * SlimSearchResults is returned from a search.  It contains collections of
 * music objects that match the search criteria.
 *
 * @author bfindeisen
 */
public class SearchResult {

    private Collection<Artist> artists;
    private Collection<Album> albums;
    private Collection<Song> items;
    private Collection<Contributor> contributors;
    private Collection<Track> tracks;
    private Collection<Genre> genres;

    /**
     * A {@link Collection} of {@link org.bff.squeezeserver.domain.Artist}s that matched.
     *
     * @return the collection of artists
     */
    public Collection<Artist> getArtists() {
        return artists;
    }

    /**
     * Sets the {@link Collection} of &{link Artist}s
     *
     * @param artists the artists that matched
     */
    public void setArtists(Collection<Artist> artists) {
        this.artists = artists;
    }

    /**
     * A {@link Collection} of {@link org.bff.squeezeserver.domain.Album}s that matched.
     *
     * @return the collection of albums
     */
    public Collection<Album> getAlbums() {
        return albums;
    }

    /**
     * Sets the {@link Collection} of &{link Album}s
     *
     * @param albums the albums that matched
     */
    public void setAlbums(Collection<Album> albums) {
        this.albums = albums;
    }

    /**
     * A {@link Collection} of {@link org.bff.squeezeserver.domain.Song}s that matched.
     *
     * @return the collection of songs
     */
    public Collection<Song> getSongs() {
        return items;
    }

    /**
     * Sets the {@link Collection} of &{link Song}s
     *
     * @param songs the songs that matched
     */
    public void setSongs(Collection<Song> songs) {
        this.items = songs;
    }

    /**
     * A {@link Collection} of {@link org.bff.squeezeserver.domain.Contributor}s that matched.
     *
     * @return the collection of contributors
     */
    public Collection<Contributor> getContributors() {
        return contributors;
    }

    /**
     * Sets the {@link Collection} of &{link Contributor}s
     *
     * @param contributors the contributors that matched
     */
    public void setContributors(Collection<Contributor> contributors) {
        this.contributors = contributors;
    }

    /**
     * A {@link Collection} of {@link org.bff.squeezeserver.domain.Track}s that matched.
     *
     * @return the collection of tracks
     */
    public Collection<Track> getTracks() {
        return tracks;
    }

    /**
     * Sets the {@link Collection} of &{link Track}s
     *
     * @param tracks the tracks that matched
     */
    public void setTracks(Collection<Track> tracks) {
        this.tracks = tracks;
    }

    /**
     * @return the genres
     */
    public Collection<Genre> getGenres() {
        return genres;
    }

    /**
     * @param genres the genres to set
     */
    public void setGenres(Collection<Genre> genres) {
        this.genres = genres;
    }
}
