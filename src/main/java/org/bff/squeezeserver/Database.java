package org.bff.squeezeserver;

import org.apache.log4j.Logger;
import org.bff.squeezeserver.domain.*;
import org.bff.squeezeserver.exception.DatabaseException;
import org.bff.squeezeserver.exception.SqueezeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Representation of a database with a {@link SqueezeServer} back end. Use this
 * class for all searching and querying.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class Database {

    private SqueezeServer squeezeServer;
    private Properties prop;
    private static String PROP_SONG_COUNT;
    private static String PROP_ALBUM_COUNT;
    private static String PROP_ARTIST_COUNT;
    private static String PROP_GENRE_COUNT;
    private static String PROP_SEARCH;
    private static String PROP_SONG_INFO;
    public static String PROP_RESCAN;
    private static String PROP_RESCAN_QUERY;
    private static String PROP_WIPECACHE;
    private static String PROP_GENRES;
    private static String PROP_ARTISTS;
    private static String PROP_ALBUMS;
    private static String PROP_YEARS;
    private static String PROP_TITLE;
    private static String PROP_RESCAN_PROGRESS;
    private static final String SEARCH_TAG = Constants.SEARCH_TAG;
    private static final String SEARCH_TAG_ALL = Constants.SEARCH_TAG_ALL;
    private static final String SEARCH_TAG_TRACK = Constants.SEARCH_TAG_TRACK;
    private static final String SEARCH_TAG_URL = Constants.SEARCH_TAG_URL;
    private static final String SEARCH_TAG_ARTIST = Constants.SEARCH_TAG_ARTIST;
    private static final String SEARCH_TAG_YEAR = Constants.SEARCH_TAG_YEAR;
    private static final String SEARCH_TAG_ALBUM = Constants.SEARCH_TAG_ALBUM;
    private static final String SEARCH_TAG_GENRE = Constants.SEARCH_TAG_GENRE;
    private static final String SORT_TAG = Constants.SORT_TAG;
    private static final String SORT_TAG_NEW_MUSIC = Constants.SORT_TAG_NEW_MUSIC;
    /**
     * types of return tags. Get these from Song
     */
    private static final String TAG_RETURN = Constants.RESPONSE_TAGS;
    private static final String PREFIX_ID = Constants.RESPONSE_PREFIX_ID;
    private static final String PREFIX_ARTIST_ID = Constants.RESPONSE_PREFIX_ARTIST_ID;
    private static final String PREFIX_ALBUM_ID = Constants.RESPONSE_PREFIX_ALBUM_ID;
    private static final String PREFIX_GENRE_ID = Constants.RESPONSE_PREFIX_GENRE_ID;
    private static final String PREFIX_TRACK_ID = Constants.RESPONSE_PREFIX_TRACK_ID;
    private static final String PREFIX_CONTRIBUTOR_ID = Constants.RESPONSE_PREFIX_CONTRIBUTOR_ID;
    private static final String[] PREFIX_ID_LIST = new String[]{
            PREFIX_ID,
            PREFIX_ARTIST_ID,
            PREFIX_ALBUM_ID,
            PREFIX_GENRE_ID,
            PREFIX_TRACK_ID,
            PREFIX_CONTRIBUTOR_ID};
    private static final String RESULTS_START = Integer.toString(Constants.RESULTS_START);
    private static final String PREFIX_COUNT = Constants.RESPONSE_PREFIX_COUNT;
    private static final String PREFIX_TOTAL_TIME = Constants.RESPONSE_PREFIX_TOTAL_TIME;
    private static final String PREFIX_FULL_NAME = Constants.RESPONSE_PREFIX_FULL_NAME;
    private static final String PREFIX_YEAR = Constants.RESPONSE_PREFIX_YEAR;
    private static final String PREFIX_STEPS = Constants.RESPONSE_PREFIX_STEPS;
    private static final String PREFIX_INFO = Constants.RESPONSE_PREFIX_INFO;
    private static final String PARAM_START = Constants.CMD_PARAM_START;
    private static final String PARAM_ITEMS = Constants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = Constants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PARAM_RESULTS_MAX = Integer.toString(Constants.RESULTS_MAX);

    private Logger logger = Constants.LOGGER_DATABASE;

    /**
     * Creates a new instance of Database
     *
     * @param squeezeServer the {@link SqueezeServer} for this database
     */
    public Database(SqueezeServer squeezeServer) {
        this.squeezeServer = squeezeServer;
        this.prop = SqueezeServer.getSlimProperties();
        loadProperties();
    }

    /**
     * Returns the total number of songs
     *
     * @return the number of songs
     * @throws org.bff.squeezeserver.exception.SqueezeException
     *          if there is a problem getting the count
     */
    public int getSongCount() throws SqueezeException {
        return (Integer.parseInt(sendCommand(PROP_SONG_COUNT)[0]));
    }

    /**
     * Returns the total number of albums
     *
     * @return the number of albums
     * @throws org.bff.squeezeserver.exception.SqueezeException
     *          if there is a problem getting the count
     */
    public int getAlbumCount() throws SqueezeException {
        return (Integer.parseInt(sendCommand(PROP_ALBUM_COUNT)[0]));
    }

    /**
     * Returns the total number of artists
     *
     * @return the number of artists
     * @throws org.bff.squeezeserver.exception.SqueezeException
     *          if there is a problem getting the count
     */
    public int getArtistCount() throws SqueezeException {
        return (Integer.parseInt(sendCommand(PROP_ARTIST_COUNT)[0]));
    }

    /**
     * Returns the total number of genres
     *
     * @return the number of genres
     * @throws org.bff.squeezeserver.exception.SqueezeException
     *          if there is a problem getting the count
     */
    public int getGenreCount() throws SqueezeException {
        return (Integer.parseInt(sendCommand(PROP_GENRE_COUNT)[0]));
    }

    /**
     * Searches albums, artists, and titles for the search criteria.
     *
     * @param criteria
     * @return a collection of {@link org.bff.squeezeserver.domain.Artist}, {@link org.bff.squeezeserver.domain.Album}, and
     *         {@link org.bff.squeezeserver.domain.Song}
     * @throws org.bff.squeezeserver.exception.DatabaseException
     *          if there is a database problem
     */
    public SearchResult searchAll(String criteria) throws DatabaseException {
        String[] response = search(criteria);

        SearchResult ssr = new SearchResult();
        ssr.setArtists(parseArtists(response));
        ssr.setContributors(parseContributors(response));
        ssr.setAlbums(parseAlbums(response));
        ssr.setTracks(parseTracks(response));
        ssr.setGenres(parseGenres(response));
        List<Song> songs = new ArrayList<Song>();

        for (Track track : ssr.getTracks()) {
            Song song = lookupSong(track.getId());
            if (song != null) {
                songs.add(song);
            }
        }
        ssr.setSongs(songs);

        return ssr;
    }

    private Collection<Artist> parseArtists(String[] response) {
        List<Artist> retList = new ArrayList<Artist>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID) || response[i].startsWith(PREFIX_ARTIST_ID)) {
                ++counter;
                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;

                String[] array = new String[tagCount];

                int j = 0;
                do {
                    array[j] = response[i++];
                    ++j;
                } while (i < response.length && !isPrefixId(response[i]));

                Artist artist = new Artist();
                convertResponseToItem(artist, array);
                retList.add(artist);
            } else {
                ++i;
            }
        }

        return retList;
    }

    private Collection<Album> parseAlbums(String[] response) {
        List<Album> retList = new ArrayList<Album>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID) || response[i].startsWith(PREFIX_ALBUM_ID)) {
                ++counter;
                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;

                String[] array = new String[tagCount];

                int j = 0;
                do {
                    array[j] = response[i++];
                    ++j;
                } while (i < response.length && !isPrefixId(response[i]));

                Album album = new Album();
                convertResponseToItem(album, array);
                retList.add(album);
            } else {
                ++i;
            }
        }

        return retList;
    }

    private Collection<Track> parseTracks(String[] response) {
        List<Track> retList = new ArrayList<Track>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID) || response[i].startsWith(PREFIX_TRACK_ID)) {
                ++counter;

                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;

                String[] songArray = new String[tagCount];

                int j = 0;
                do {
                    songArray[j] = response[i++];
                    ++j;
                } while (i < response.length && !isPrefixId(response[i]));

                Track slimTrack = new Track();
                convertResponseToItem(slimTrack, songArray);
                retList.add(slimTrack);
            } else {
                ++i;
            }
        }

        return retList;
    }

    private Collection<Song> parseSongs(String[] response) {
        List<Song> retList = new ArrayList<Song>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID)) {
                ++counter;

                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;

                String[] songArray = new String[tagCount];

                int j = 0;
                do {
                    songArray[j] = response[i++];
                    ++j;
                } while (i < response.length && !isPrefixId(response[i]));

                Song slimSong = new Song();
                convertResponseToItem(slimSong, songArray);
                retList.add(slimSong);
            } else {
                ++i;
            }
        }

        return retList;
    }

    private Collection<Contributor> parseContributors(String[] response) {
        List<Contributor> retList = new ArrayList<Contributor>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID) || response[i].startsWith(PREFIX_CONTRIBUTOR_ID)) {
                ++counter;
                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;

                String[] array = new String[tagCount];

                int j = 0;
                do {
                    array[j] = response[i++];
                    ++j;
                } while (i < response.length && !isPrefixId(response[i]));

                Contributor cont = new Contributor();
                convertResponseToItem(cont, array);
                retList.add(cont);
            } else {
                ++i;
            }
        }

        return retList;
    }

    private Collection<Genre> parseGenres(String[] response) {
        List<Genre> retList = new ArrayList<Genre>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID) || response[i].startsWith(PREFIX_GENRE_ID)) {
                ++counter;
                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;

                String[] array = new String[tagCount];

                int j = 0;
                do {
                    array[j] = response[i++];
                    ++j;
                } while (i < response.length && !isPrefixId(response[i]));

                Genre genre = new Genre();
                convertResponseToItem(genre, array);
                retList.add(genre);
            } else {
                ++i;
            }
        }

        return retList;
    }

    private Collection<String> parseYears(String[] response) {
        List<String> retList = new ArrayList<String>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_YEAR)) {
                ++counter;
                retList.add(response[i++].replaceAll(PREFIX_YEAR, ""));
            } else {
                ++i;
            }
        }

        return retList;
    }

    /**
     * Returns a <CODE>Collection</CODE> of <CODE>Artist</CODE>s for an any
     * artist containing the parameter artist.
     *
     * @param criteria the search criteria
     * @return a <CODE>Collection</CODE> of {@link org.bff.squeezeserver.domain.Song}s
     * @throws org.bff.squeezeserver.exception.DatabaseException
     *          if there is a database problem
     */
    public Collection<Artist> searchArtists(String criteria) throws DatabaseException {
        String command = PROP_ARTISTS.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG + criteria);

        return parseArtists(sendCommand(command));
    }

    /**
     * Returns a <CODE>Collection</CODE> of <CODE>Song</CODE>s for an any
     * album containing the parameter album.
     *
     * @param criteria the album to match
     * @return a <CODE>Collection</CODE> of {@link org.bff.squeezeserver.domain.Song}s
     * @throws org.bff.squeezeserver.exception.DatabaseException
     *          if there is a database problem
     */
    public Collection<Album> searchAlbums(String criteria) throws DatabaseException {
        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG + encode(criteria) + " " + TAG_RETURN
                + Album.RETURN_TAGS);

        return parseAlbums(sendCommand(command));
    }

    /**
     * Returns a <CODE>Collection</CODE> of <CODE>Song</CODE>s for an any
     * title containing the parameter title.
     *
     * @param criteria the search criteria
     * @return a <CODE>Collection</CODE> of {@link org.bff.squeezeserver.domain.Song}s
     * @throws org.bff.squeezeserver.exception.DatabaseException
     *          if there is a database problem
     */
    public Collection<Song> searchTitles(String criteria) throws DatabaseException {
        String command = PROP_TITLE.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG + encode(criteria) + " " + TAG_RETURN
                + PlayableTags.RETURN_TAGS);

        return parseSongs(sendCommand(command));
    }

    private String[] search(String searchCriteria) {
        String command = PROP_SEARCH.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_ALL + encode(searchCriteria));

        return sendCommand(command);
    }

    /**
     * Returns a <CODE>Collection</CODE> of {@link org.bff.squeezeserver.domain.Album}s for an artist
     *
     * @param artist
     * @return a <CODE>Collection</CODE> of {@link org.bff.squeezeserver.domain.Album}s
     * @throws org.bff.squeezeserver.exception.DatabaseException
     *          if there is a database problem
     */
    public Collection<Album> listAlbumsForArtist(Artist artist) throws DatabaseException {
        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_ARTIST + artist.getId() + " " + TAG_RETURN
                + Album.RETURN_TAGS);

        return parseAlbums(sendCommand(command));
    }

    public Collection<Album> listAlbumsForYear(String year) throws DatabaseException {

        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_YEAR + year + " " + TAG_RETURN + Album.RETURN_TAGS);

        return parseAlbums(sendCommand(command));
    }

    public Collection<Album> listAlbumsForGenre(Genre genre) throws DatabaseException {

        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_GENRE + genre.getId() + " " + TAG_RETURN
                + Album.RETURN_TAGS);

        return parseAlbums(sendCommand(command));

    }

    /**
     * Lists the albums for the passed criteria. Pass null to match all.
     *
     * @param artist the {@link org.bff.squeezeserver.domain.Artist} to match
     * @param genre  the {@link org.bff.squeezeserver.domain.Genre} to match
     * @param year   the year to match
     * @return a <CODE>Collection</CODE> of {@link org.bff.squeezeserver.domain.Album}s
     * @throws org.bff.squeezeserver.exception.DatabaseException
     *          if there is a database problem
     */
    public Collection<Album> listAlbums(Artist artist, Genre genre, String year)
            throws DatabaseException {

        if (artist == null && genre == null && year == null) {
            return null;
        }

        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);

        String searchCriteria = "";
        if (artist != null) {
            searchCriteria += SEARCH_TAG_ARTIST + artist.getId();
        }

        if (genre != null) {
            if (!searchCriteria.equals("")) {
                searchCriteria += " ";
            }

            searchCriteria += SEARCH_TAG_GENRE + genre.getId();
        }

        if (year != null) {
            if (!searchCriteria.equals("")) {
                searchCriteria += " ";
            }

            searchCriteria += SEARCH_TAG_YEAR + year;
        }

        command = command.replaceAll(PARAM_TAGS, searchCriteria + " " + TAG_RETURN + Album.RETURN_TAGS);

        return parseAlbums(sendCommand(command));
    }

    /**
     * Returns a {@code Collection} of {@link org.bff.squeezeserver.domain.Artist}s for a given
     * {@link org.bff.squeezeserver.domain.Genre}
     *
     * @param genre the {@link org.bff.squeezeserver.domain.Genre} to match
     * @return a {@code Collection} of {@link org.bff.squeezeserver.domain.Artist}s
     * @throws org.bff.squeezeserver.exception.DatabaseException
     *          if there is a database problem
     */
    public Collection<Artist> listArtistsForGenre(Genre genre) throws DatabaseException {
        String command = PROP_ARTISTS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_GENRE + genre.getId());

        return parseArtists(sendCommand(command));
    }

    /**
     * Returns a {@link org.bff.squeezeserver.domain.Artist} for a {@link org.bff.squeezeserver.domain.Album}
     *
     * @param album the {@link org.bff.squeezeserver.domain.Album} to lookup
     * @return the {@link org.bff.squeezeserver.domain.Artist} for the album
     * @throws org.bff.squeezeserver.exception.DatabaseException
     *          if there is a database problem
     */
    public Collection<Artist> getArtistsForAlbum(Album album) throws DatabaseException {
        String command = PROP_ARTISTS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_ALBUM + album.getId());

        return parseArtists(sendCommand(command));
    }

    /**
     * Returns a {@code Collection} of {@link org.bff.squeezeserver.domain.Song}s for a given
     * {@link org.bff.squeezeserver.domain.Artist}
     *
     * @param artist the {@link org.bff.squeezeserver.domain.Artist} to look up
     * @return a {@code Collection} of {@link org.bff.squeezeserver.domain.Song}s
     * @throws org.bff.squeezeserver.exception.DatabaseException
     *
     */
    public Collection<Song> listSongsForArtist(Artist artist) throws DatabaseException {
        List<Song> retList = new ArrayList<Song>();

        String command = PROP_TITLE.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_ARTIST + artist.getId() + " " + TAG_RETURN
                + PlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        int count = Integer.parseInt(response[response.length - 1].split(PREFIX_COUNT)[1]);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID)) {
                ++counter;
                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;

                String[] songArray = new String[tagCount];

                int j = 0;
                do {
                    songArray[j] = response[i++];
                    ++j;
                } while (!response[i].startsWith(PREFIX_ID) && i < response.length - 1);

                Song slimSong = new Song();
                convertResponseToItem(slimSong, songArray);
                retList.add(slimSong);
            } else {
                ++i;
            }
        }

        return retList;
    }

    public Collection<Song> listSongsForYear(String year) throws DatabaseException {
        List<Song> retList = new ArrayList<Song>();

        String command = PROP_TITLE.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_YEAR + year + " " + TAG_RETURN
                + PlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        int count = Integer.parseInt(response[response.length - 1].split(PREFIX_COUNT)[1]);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID)) {
                ++counter;
                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;

                String[] songArray = new String[tagCount];

                int j = 0;
                do {
                    songArray[j] = response[i++];
                    ++j;
                } while (!response[i].startsWith(PREFIX_ID) && i < response.length - 1);

                Song slimSong = new Song();
                convertResponseToItem(slimSong, songArray);
                retList.add(slimSong);
            } else {
                ++i;
            }
        }

        return retList;
    }

    public Collection<Song> listSongsForGenre(Genre genre) throws DatabaseException {
        List<Song> retList = new ArrayList<Song>();

        String command = PROP_TITLE.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_GENRE + genre.getId() + " " + TAG_RETURN
                + PlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        int count = Integer.parseInt(response[response.length - 1].split(PREFIX_COUNT)[1]);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID)) {
                ++counter;
                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;

                String[] songArray = new String[tagCount];

                int j = 0;
                do {
                    songArray[j] = response[i++];
                    ++j;
                } while (!response[i].startsWith(PREFIX_ID) && i < response.length - 1);

                Song slimSong = new Song();
                convertResponseToItem(slimSong, songArray);
                retList.add(slimSong);
            } else {
                ++i;
            }
        }

        return retList;
    }

    /**
     * Returns the first {@link org.bff.squeezeserver.domain.Song} for a given {@link org.bff.squeezeserver.domain.Album}
     *
     * @param album the {@link org.bff.squeezeserver.domain.Album}
     * @return the first {@link org.bff.squeezeserver.domain.Song}
     * @deprecated this method has been deprecated since it's only real use was
     *             for album art, which is now a part of the album
     */
    public Song getFirstSongForAlbum(Album album) {
        Song song = null;

        String command = PROP_TITLE.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, "1");
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_ALBUM + album.getId() + " " + TAG_RETURN
                + PlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);
        for (int i = 0; i < response.length - 1; ) {
            if (response[i].startsWith(PREFIX_ID)) {
                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;
                String[] songArray = new String[tagCount];
                for (int j = 0; j < tagCount; j++) {
                    if (i < response.length - 1) {
                        songArray[j] = response[i++];
                    }
                }
                convertResponseToItem(song, songArray);
            } else {
                ++i;
            }
        }
        return song;
    }

    /**
     * Returns a <code>Collection</code> of {@link org.bff.squeezeserver.domain.Song}s for a given
     * {@link org.bff.squeezeserver.domain.Album}
     *
     * @param album the {@link org.bff.squeezeserver.domain.Album}
     * @return a <code>Collection</code> of {@link org.bff.squeezeserver.domain.Song}s
     */
    public Collection<Song> listSongsForAlbum(Album album) throws DatabaseException {
        List<Song> retList = new ArrayList<Song>();

        String command = PROP_TITLE.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_ALBUM + album.getId() + " " + TAG_RETURN
                + PlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        int count = Integer.parseInt(response[response.length - 1].split(PREFIX_COUNT)[1]);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID)) {
                ++counter;
                int tagCount = PlayableTags.RETURN_TAGS.length() + 2;

                String[] songArray = new String[tagCount];

                int j = 0;
                do {
                    songArray[j] = response[i++];
                    ++j;
                } while (!response[i].startsWith(PREFIX_ID) && i < response.length - 1);

                Song slimSong = new Song();
                convertResponseToItem(slimSong, songArray);
                retList.add(slimSong);
            } else {
                ++i;
            }
        }

        return retList;
    }

    /**
     * Returns the {@link org.bff.squeezeserver.domain.Song} for a song id. Returns <code>null</code> if
     * there is not a match.
     *
     * @param songId the song id
     * @return the {@link org.bff.squeezeserver.domain.Song}
     */
    public PlayableItem lookupItem(String songId) {
        String command = PROP_SONG_INFO.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_TRACK + songId + " " + TAG_RETURN
                + PlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            return null;
        } else {
            Song slimSong = new Song();
            convertResponseToItem(slimSong, response);
            slimSong.setId(songId);
            return slimSong;
        }
    }

    /**
     * Returns the {@link org.bff.squeezeserver.domain.Song} for a song id. Returns <code>null</code> if
     * there is not a match.
     *
     * @param songId the song id
     * @return the {@link org.bff.squeezeserver.domain.Song}
     */
    public Song lookupSong(String songId) {
        String command = PROP_SONG_INFO.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_TRACK + songId + " " + TAG_RETURN
                + PlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            return null;
        } else {
            Song slimSong = new Song();
            convertResponseToItem(slimSong, response);
            slimSong.setId(songId);
            return slimSong;
        }
    }

    /**
     * Returns the {@link org.bff.squeezeserver.domain.Song} for a song id. Returns <code>null</code> if
     * there is not a match. This will return null for remote streams.
     *
     * @param url the song url
     * @return the {@link org.bff.squeezeserver.domain.Song}
     */
    public PlayableItem lookupItemByUrl(String url) {

        if (url == null) {
            return null;
        }

        String command = PROP_SONG_INFO.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_URL + url + " " + TAG_RETURN + PlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            return null;
        } else {
            Song slimSong = new Song();
            convertResponseToItem(slimSong, response);
            slimSong.setUrl(url);
            return slimSong;
        }
    }

    /**
     * Returns the progress of the database scan. Progress results are stored in
     * a {@link org.bff.squeezeserver.domain.DatabaseProgress} object.
     *
     * @return a {@link org.bff.squeezeserver.domain.DatabaseProgress} containing scan results
     */
    public DatabaseProgress getScanProgress() {
        String command = PROP_RESCAN_PROGRESS.replaceAll(PARAM_TAGS, "");

        String[] response = sendCommand(command);

        if (response == null) {
            return null;
        } else {
            DatabaseProgress sdp = new DatabaseProgress();

            for (int i = 0; i < response.length; ) {
                if (response[i].startsWith(PREFIX_TOTAL_TIME)) {
                    sdp.setTotalTime(response[i].replace(PREFIX_TOTAL_TIME, ""));
                } else if (response[i].startsWith(PREFIX_STEPS)) {
                    sdp.setSteps(response[i].replace(PREFIX_STEPS, ""));
                } else if (response[i].startsWith(PREFIX_INFO)) {
                    sdp.setInfo(response[i].replace(PREFIX_INFO, ""));
                } else if (response[i].startsWith(PREFIX_FULL_NAME)) {
                    sdp.setFullName(response[i].replace(PREFIX_FULL_NAME, ""));
                } else {
                    String[] split = response[i].split(":");

                    int percentage = 0;
                    try {
                        percentage = Integer.parseInt(split[1]);
                    } catch (Exception e) {
                        logger.warn("Could not parse percentage complete", e);
                    }
                    sdp.addImporter(split[0], percentage);
                }
                ++i;
            }
            return sdp;
        }
    }

    private void loadProperties() {
        PROP_SONG_COUNT = prop.getProperty(Constants.PROP_DB_SONG_COUNT);
        PROP_ALBUM_COUNT = prop.getProperty(Constants.PROP_DB_ALBUM_COUNT);
        PROP_ARTIST_COUNT = prop.getProperty(Constants.PROP_DB_ARTIST_COUNT);
        PROP_GENRE_COUNT = prop.getProperty(Constants.PROP_DB_GENRE_COUNT);
        PROP_SEARCH = prop.getProperty(Constants.PROP_DB_SEARCH);
        PROP_SONG_INFO = prop.getProperty(Constants.PROP_DB_SONG_INFO);
        PROP_RESCAN = prop.getProperty(Constants.PROP_DB_RESCAN);
        PROP_RESCAN_QUERY = prop.getProperty(Constants.PROP_DB_RESCAN_QUERY);
        PROP_WIPECACHE = prop.getProperty(Constants.PROP_DB_WIPECACHE);
        PROP_GENRES = prop.getProperty(Constants.PROP_DB_GENRES);
        PROP_ALBUMS = prop.getProperty(Constants.PROP_DB_ALBUMS);
        PROP_ARTISTS = prop.getProperty(Constants.PROP_DB_ARTISTS);
        PROP_YEARS = prop.getProperty(Constants.PROP_DB_YEARS);
        PROP_TITLE = prop.getProperty(Constants.PROP_DB_TITLE);
        PROP_RESCAN_PROGRESS = prop.getProperty(Constants.PROP_DB_RESCAN_PROGRESS);
    }

    /**
     * Scans music directory for new or changed music
     */
    public void rescan() {
        sendCommand(PROP_RESCAN);
    }

    /**
     * Clears the database and scans the music directory
     */
    public void clearDatabaseAndRescan() {
        sendCommand(PROP_WIPECACHE);
        rescan();

    }

    /**
     * Returns true if the database is rescanning, false if not
     *
     * @return true if rescanning, false otherwise
     */
    public boolean isRescanning() {
        return sendCommand(PROP_RESCAN_QUERY)[0].equals("1");

    }

    /**
     * Returns a {@code Collection} of all {@link org.bff.squeezeserver.domain.Genre}s
     *
     * @return a {@code Collection} of all {@link org.bff.squeezeserver.domain.Genre}s
     */
    public Collection<Genre> getGenres() {

        String command = PROP_GENRES.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, "");

        String[] response = sendCommand(command);

        return parseGenres(response);
    }

    /**
     * Returns a {@code Collection} of all {@link org.bff.squeezeserver.domain.Artist}s
     *
     * @return a {@code Collection} of all {@link org.bff.squeezeserver.domain.Artist}s
     */
    public Collection<Artist> getArtists() {
        String command = PROP_ARTISTS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, "");

        String[] response = sendCommand(command);

        return parseArtists(response);
    }

    /**
     * Returns a {@code Collection} of all {@link org.bff.squeezeserver.domain.Album}s.  This method does not populate
     * the artist field so an additional call is necessary if you need that field later on.
     * <p/>
     * Use {@link Database#getAlbums(boolean) if you with to populate the artist field.
     *
     * @return a {@code Collection} of all {@link org.bff.squeezeserver.domain.Album }s
     */
    public Collection<Album> getAlbums() {
        return getAlbums(false);
    }

    /**
     * Returns a {@code Collection} of all {@link org.bff.squeezeserver.domain.Album}s include the artist name
     * associated with the album.  Passing true slightly affects the performance of the lookup.
     *
     * @param includeArtist
     * @return a {@code Collection} of all {@link org.bff.squeezeserver.domain.Album}s
     */
    public Collection<Album> getAlbums(boolean includeArtist) {
        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, TAG_RETURN +
                (includeArtist ?
                        Album.RETURN_TAGS + Album.RETURN_ARTIST :
                        Album.RETURN_TAGS));

        String[] response = sendCommand(command);

        return parseAlbums(response);
    }

    /**
     * Returns a {@code Collection} of new music {@link org.bff.squeezeserver.domain.Album}s.  This method
     * does not populate the artist field so an additional call is necessary if you
     * need that field later on.
     * <p/>
     * Use {@link Database#getAlbums(boolean) if you with to populate the artist field.
     *
     * @return a {@code Collection} of all {@link org.bff.squeezeserver.domain.Album }s
     */
    public Collection<Album> getNewMusic() {
        return getNewMusic(false);
    }

    /**
     * Returns a {@code Collection} of new music {@link org.bff.squeezeserver.domain.Album}s including
     * the artist name associated with the album.  Passing true slightly affects
     * the performance of the lookup.
     *
     * @param includeArtist
     * @return a {@code Collection} of all {@link org.bff.squeezeserver.domain.Album}s
     */
    public Collection<Album> getNewMusic(boolean includeArtist) {
        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, TAG_RETURN
                + (includeArtist
                ? Album.RETURN_TAGS + Album.RETURN_ARTIST
                : Album.RETURN_TAGS)
                + " " + SORT_TAG + SORT_TAG_NEW_MUSIC);

        String[] response = sendCommand(command);

        return parseAlbums(response);
    }

    /**
     * Returns a {@code Collection} of all years
     *
     * @return a {@code Collection} of all years
     */
    public Collection<String> getYears() {
        String command = PROP_YEARS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, "");

        return parseYears(sendCommand(command));
    }

    private String[] sendCommand(String slimCommand) {
        return sendCommand(slimCommand, null);
    }

    private String[] sendCommand(String slimCommand, String param) {
        try {
            if (param == null) {
                return getSqueezeServer().sendCommand(new Command(slimCommand));
            } else {
                return getSqueezeServer().sendCommand(new Command(slimCommand, param));
            }
        } catch (Exception e) {
            logger.fatal(e);
            return null;
        }

    }

    /**
     * Returns the {@link SqueezeServer} for this database
     *
     * @return the {@link SqueezeServer}
     */
    public SqueezeServer getSqueezeServer() {
        return squeezeServer;
    }

    /**
     * Sets the {@link SqueezeServer} for this database
     *
     * @param squeezeServer the {@link SqueezeServer}
     */
    public void setSqueezeServer(SqueezeServer squeezeServer) {
        this.squeezeServer = squeezeServer;
    }

    protected void convertResponseToItem(PlayableItem item, String[] response) {
        getSqueezeServer().convertResponse(item, response);
    }

    private String encode(String criteria) {
        return Utils.encode(criteria, SqueezeServer.getEncoding());
    }

    private int getCount(String[] response) {
        int count = 0;
        for (int i = response.length - 1; i >= 0; i--) {
            String string = response[i];
            if (string.startsWith(PREFIX_COUNT)) {
                count = Integer.parseInt(string.split(PREFIX_COUNT)[1]);
                break;
            }
        }
        return count;
    }

    private boolean isPrefixId(String string) {
        for (int i = 0; i < PREFIX_ID_LIST.length; i++) {
            String s = PREFIX_ID_LIST[i];
            if (string.startsWith(s)) {
                return true;
            }

        }
        return false;
    }
}
