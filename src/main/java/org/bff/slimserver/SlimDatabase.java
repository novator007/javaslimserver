package org.bff.slimserver;

import org.apache.log4j.Logger;
import org.bff.slimserver.exception.SlimDatabaseException;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.musicobjects.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Representation of a database with a {@link SlimServer} back end. Use this
 * class for all searching and querying.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class SlimDatabase {

    private SlimServer slimServer;
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
    private static final String SEARCH_TAG = SlimConstants.SEARCH_TAG;
    private static final String SEARCH_TAG_ALL = SlimConstants.SEARCH_TAG_ALL;
    private static final String SEARCH_TAG_TRACK = SlimConstants.SEARCH_TAG_TRACK;
    private static final String SEARCH_TAG_URL = SlimConstants.SEARCH_TAG_URL;
    private static final String SEARCH_TAG_ARTIST = SlimConstants.SEARCH_TAG_ARTIST;
    private static final String SEARCH_TAG_YEAR = SlimConstants.SEARCH_TAG_YEAR;
    private static final String SEARCH_TAG_ALBUM = SlimConstants.SEARCH_TAG_ALBUM;
    private static final String SEARCH_TAG_GENRE = SlimConstants.SEARCH_TAG_GENRE;
    private static final String SORT_TAG = SlimConstants.SORT_TAG;
    private static final String SORT_TAG_NEW_MUSIC = SlimConstants.SORT_TAG_NEW_MUSIC;
    /**
     * types of return tags. Get these from SlimSong
     */
    private static final String TAG_RETURN = SlimConstants.RESPONSE_TAGS;
    private static final String PREFIX_ID = SlimConstants.RESPONSE_PREFIX_ID;
    private static final String PREFIX_ARTIST_ID = SlimConstants.RESPONSE_PREFIX_ARTIST_ID;
    private static final String PREFIX_ALBUM_ID = SlimConstants.RESPONSE_PREFIX_ALBUM_ID;
    private static final String PREFIX_GENRE_ID = SlimConstants.RESPONSE_PREFIX_GENRE_ID;
    private static final String PREFIX_TRACK_ID = SlimConstants.RESPONSE_PREFIX_TRACK_ID;
    private static final String PREFIX_CONTRIBUTOR_ID = SlimConstants.RESPONSE_PREFIX_CONTRIBUTOR_ID;
    private static final String[] PREFIX_ID_LIST = new String[]{
            PREFIX_ID,
            PREFIX_ARTIST_ID,
            PREFIX_ALBUM_ID,
            PREFIX_GENRE_ID,
            PREFIX_TRACK_ID,
            PREFIX_CONTRIBUTOR_ID};
    private static final String RESULTS_START = Integer.toString(SlimConstants.RESULTS_START);
    private static final String PREFIX_COUNT = SlimConstants.RESPONSE_PREFIX_COUNT;
    private static final String PREFIX_TOTAL_TIME = SlimConstants.RESPONSE_PREFIX_TOTAL_TIME;
    private static final String PREFIX_FULL_NAME = SlimConstants.RESPONSE_PREFIX_FULL_NAME;
    private static final String PREFIX_YEAR = SlimConstants.RESPONSE_PREFIX_YEAR;
    private static final String PREFIX_STEPS = SlimConstants.RESPONSE_PREFIX_STEPS;
    private static final String PREFIX_INFO = SlimConstants.RESPONSE_PREFIX_INFO;
    private static final String PARAM_START = SlimConstants.CMD_PARAM_START;
    private static final String PARAM_ITEMS = SlimConstants.CMD_PARAM_ITEMS_RESPONSE;
    private static final String PARAM_TAGS = SlimConstants.CMD_PARAM_TAGGED_PARAMS;
    private static final String PARAM_RESULTS_MAX = Integer.toString(SlimConstants.RESULTS_MAX);

    private Logger logger = SlimConstants.LOGGER_DATABASE;

    /**
     * Creates a new instance of SlimDatabase
     *
     * @param slimServer the {@link SlimServer} for this database
     */
    public SlimDatabase(SlimServer slimServer) {
        this.slimServer = slimServer;
        this.prop = SlimServer.getSlimProperties();
        loadProperties();
    }

    /**
     * Returns the total number of songs
     *
     * @return the number of songs
     * @throws SlimException if there is a problem getting the count
     */
    public int getSongCount() throws SlimException {
        return (Integer.parseInt(sendCommand(PROP_SONG_COUNT)[0]));
    }

    /**
     * Returns the total number of albums
     *
     * @return the number of albums
     * @throws SlimException if there is a problem getting the count
     */
    public int getAlbumCount() throws SlimException {
        return (Integer.parseInt(sendCommand(PROP_ALBUM_COUNT)[0]));
    }

    /**
     * Returns the total number of artists
     *
     * @return the number of artists
     * @throws SlimException if there is a problem getting the count
     */
    public int getArtistCount() throws SlimException {
        return (Integer.parseInt(sendCommand(PROP_ARTIST_COUNT)[0]));
    }

    /**
     * Returns the total number of genres
     *
     * @return the number of genres
     * @throws SlimException if there is a problem getting the count
     */
    public int getGenreCount() throws SlimException {
        return (Integer.parseInt(sendCommand(PROP_GENRE_COUNT)[0]));
    }

    /**
     * Searches albums, artists, and titles for the search criteria.
     *
     * @param criteria
     * @return a collection of {@link SlimArtist}, {@link SlimAlbum}, and
     *         {@link SlimSong}
     * @throws SlimDatabaseException if there is a database problem
     */
    public SlimSearchResult searchAll(String criteria) throws SlimDatabaseException {
        String[] response = search(criteria);

        SlimSearchResult ssr = new SlimSearchResult();
        ssr.setArtists(parseArtists(response));
        ssr.setContributors(parseContributors(response));
        ssr.setAlbums(parseAlbums(response));
        ssr.setTracks(parseTracks(response));
        ssr.setGenres(parseGenres(response));
        List<SlimSong> songs = new ArrayList<SlimSong>();

        for (SlimTrack track : ssr.getTracks()) {
            SlimSong song = lookupSong(track.getId());
            if (song != null) {
                songs.add(song);
            }
        }
        ssr.setSongs(songs);

        return ssr;
    }

    private Collection<SlimArtist> parseArtists(String[] response) {
        List<SlimArtist> retList = new ArrayList<SlimArtist>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID) || response[i].startsWith(PREFIX_ARTIST_ID)) {
                ++counter;
                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;

                String[] array = new String[tagCount];

                int j = 0;
                do {
                    array[j] = response[i++];
                    ++j;
                } while (i < response.length && !isPrefixId(response[i]));

                SlimArtist slimArtist = new SlimArtist();
                convertResponseToItem(slimArtist, array);
                retList.add(slimArtist);
            } else {
                ++i;
            }
        }

        return retList;
    }

    private Collection<SlimAlbum> parseAlbums(String[] response) {
        List<SlimAlbum> retList = new ArrayList<SlimAlbum>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID) || response[i].startsWith(PREFIX_ALBUM_ID)) {
                ++counter;
                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;

                String[] array = new String[tagCount];

                int j = 0;
                do {
                    array[j] = response[i++];
                    ++j;
                } while (i < response.length && !isPrefixId(response[i]));

                SlimAlbum slimAlbum = new SlimAlbum();
                convertResponseToItem(slimAlbum, array);
                retList.add(slimAlbum);
            } else {
                ++i;
            }
        }

        return retList;
    }

    private Collection<SlimTrack> parseTracks(String[] response) {
        List<SlimTrack> retList = new ArrayList<SlimTrack>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID) || response[i].startsWith(PREFIX_TRACK_ID)) {
                ++counter;

                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;

                String[] songArray = new String[tagCount];

                int j = 0;
                do {
                    songArray[j] = response[i++];
                    ++j;
                } while (i < response.length && !isPrefixId(response[i]));

                SlimTrack slimTrack = new SlimTrack();
                convertResponseToItem(slimTrack, songArray);
                retList.add(slimTrack);
            } else {
                ++i;
            }
        }

        return retList;
    }

    private Collection<SlimSong> parseSongs(String[] response) {
        List<SlimSong> retList = new ArrayList<SlimSong>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID)) {
                ++counter;

                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;

                String[] songArray = new String[tagCount];

                int j = 0;
                do {
                    songArray[j] = response[i++];
                    ++j;
                } while (i < response.length && !isPrefixId(response[i]));

                SlimSong slimSong = new SlimSong();
                convertResponseToItem(slimSong, songArray);
                retList.add(slimSong);
            } else {
                ++i;
            }
        }

        return retList;
    }

    private Collection<SlimContributor> parseContributors(String[] response) {
        List<SlimContributor> retList = new ArrayList<SlimContributor>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID) || response[i].startsWith(PREFIX_CONTRIBUTOR_ID)) {
                ++counter;
                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;

                String[] array = new String[tagCount];

                int j = 0;
                do {
                    array[j] = response[i++];
                    ++j;
                } while (i < response.length && !isPrefixId(response[i]));

                SlimContributor slimCont = new SlimContributor();
                convertResponseToItem(slimCont, array);
                retList.add(slimCont);
            } else {
                ++i;
            }
        }

        return retList;
    }

    private Collection<SlimGenre> parseGenres(String[] response) {
        List<SlimGenre> retList = new ArrayList<SlimGenre>();

        int count = getCount(response);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID) || response[i].startsWith(PREFIX_GENRE_ID)) {
                ++counter;
                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;

                String[] array = new String[tagCount];

                int j = 0;
                do {
                    array[j] = response[i++];
                    ++j;
                } while (i < response.length && !isPrefixId(response[i]));

                SlimGenre slimGenre = new SlimGenre();
                convertResponseToItem(slimGenre, array);
                retList.add(slimGenre);
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
     * Returns a <CODE>Collection</CODE> of <CODE>SlimArtist</CODE>s for an any
     * artist containing the parameter artist.
     *
     * @param criteria the search criteria
     * @return a <CODE>Collection</CODE> of {@link SlimSong}s
     * @throws SlimDatabaseException if there is a database problem
     */
    public Collection<SlimArtist> searchArtists(String criteria) throws SlimDatabaseException {
        String command = PROP_ARTISTS.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG + criteria);

        return parseArtists(sendCommand(command));
    }

    /**
     * Returns a <CODE>Collection</CODE> of <CODE>SlimSong</CODE>s for an any
     * album containing the parameter album.
     *
     * @param criteria the album to match
     * @return a <CODE>Collection</CODE> of {@link SlimSong}s
     * @throws SlimDatabaseException if there is a database problem
     */
    public Collection<SlimAlbum> searchAlbums(String criteria) throws SlimDatabaseException {
        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG + encode(criteria) + " " + TAG_RETURN
                + SlimAlbum.RETURN_TAGS);

        return parseAlbums(sendCommand(command));
    }

    /**
     * Returns a <CODE>Collection</CODE> of <CODE>SlimSong</CODE>s for an any
     * title containing the parameter title.
     *
     * @param criteria the search criteria
     * @return a <CODE>Collection</CODE> of {@link SlimSong}s
     * @throws SlimDatabaseException if there is a database problem
     */
    public Collection<SlimSong> searchTitles(String criteria) throws SlimDatabaseException {
        String command = PROP_TITLE.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG + encode(criteria) + " " + TAG_RETURN
                + SlimPlayableTags.RETURN_TAGS);

        return parseSongs(sendCommand(command));
    }

    private String[] search(String searchCriteria) {
        String command = PROP_SEARCH.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_ALL + encode(searchCriteria));

        return sendCommand(command);
    }

    /**
     * Returns a <CODE>Collection</CODE> of {@link SlimAlbum}s for an artist
     *
     * @param artist
     * @return a <CODE>Collection</CODE> of {@link SlimAlbum}s
     * @throws SlimDatabaseException if there is a database problem
     */
    public Collection<SlimAlbum> listAlbumsForArtist(SlimArtist artist) throws SlimDatabaseException {
        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_ARTIST + artist.getId() + " " + TAG_RETURN
                + SlimAlbum.RETURN_TAGS);

        return parseAlbums(sendCommand(command));
    }

    public Collection<SlimAlbum> listAlbumsForYear(String year) throws SlimDatabaseException {

        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_YEAR + year + " " + TAG_RETURN + SlimAlbum.RETURN_TAGS);

        return parseAlbums(sendCommand(command));
    }

    public Collection<SlimAlbum> listAlbumsForGenre(SlimGenre genre) throws SlimDatabaseException {

        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_GENRE + genre.getId() + " " + TAG_RETURN
                + SlimAlbum.RETURN_TAGS);

        return parseAlbums(sendCommand(command));

    }

    /**
     * Lists the albums for the passed criteria. Pass null to match all.
     *
     * @param artist the {@link SlimArtist} to match
     * @param genre  the {@link SlimGenre} to match
     * @param year   the year to match
     * @return a <CODE>Collection</CODE> of {@link SlimAlbum}s
     * @throws SlimDatabaseException if there is a database problem
     */
    public Collection<SlimAlbum> listAlbums(SlimArtist artist, SlimGenre genre, String year)
            throws SlimDatabaseException {

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

        command = command.replaceAll(PARAM_TAGS, searchCriteria + " " + TAG_RETURN + SlimAlbum.RETURN_TAGS);

        return parseAlbums(sendCommand(command));
    }

    /**
     * Returns a {@code Collection} of {@link SlimArtist}s for a given
     * {@link SlimGenre}
     *
     * @param genre the {@link SlimGenre} to match
     * @return a {@code Collection} of {@link SlimArtist}s
     * @throws SlimDatabaseException if there is a database problem
     */
    public Collection<SlimArtist> listArtistsForGenre(SlimGenre genre) throws SlimDatabaseException {
        String command = PROP_ARTISTS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_GENRE + genre.getId());

        return parseArtists(sendCommand(command));
    }

    /**
     * Returns a {@link SlimArtist} for a {@link SlimAlbum}
     *
     * @param album the {@link SlimAlbum} to lookup
     * @return the {@link SlimArtist} for the album
     * @throws SlimDatabaseException if there is a database problem
     */
    public Collection<SlimArtist> getArtistsForAlbum(SlimAlbum album) throws SlimDatabaseException {
        String command = PROP_ARTISTS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_ALBUM + album.getId());

        return parseArtists(sendCommand(command));
    }

    /**
     * Returns a {@code Collection} of {@link SlimSong}s for a given
     * {@link SlimArtist}
     *
     * @param artist the {@link SlimArtist} to look up
     * @return a {@code Collection} of {@link SlimSong}s
     * @throws SlimDatabaseException
     */
    public Collection<SlimSong> listSongsForArtist(SlimArtist artist) throws SlimDatabaseException {
        List<SlimSong> retList = new ArrayList<SlimSong>();

        String command = PROP_TITLE.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_ARTIST + artist.getId() + " " + TAG_RETURN
                + SlimPlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        int count = Integer.parseInt(response[response.length - 1].split(PREFIX_COUNT)[1]);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID)) {
                ++counter;
                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;

                String[] songArray = new String[tagCount];

                int j = 0;
                do {
                    songArray[j] = response[i++];
                    ++j;
                } while (!response[i].startsWith(PREFIX_ID) && i < response.length - 1);

                SlimSong slimSong = new SlimSong();
                convertResponseToItem(slimSong, songArray);
                retList.add(slimSong);
            } else {
                ++i;
            }
        }

        return retList;
    }

    public Collection<SlimSong> listSongsForYear(String year) throws SlimDatabaseException {
        List<SlimSong> retList = new ArrayList<SlimSong>();

        String command = PROP_TITLE.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_YEAR + year + " " + TAG_RETURN
                + SlimPlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        int count = Integer.parseInt(response[response.length - 1].split(PREFIX_COUNT)[1]);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID)) {
                ++counter;
                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;

                String[] songArray = new String[tagCount];

                int j = 0;
                do {
                    songArray[j] = response[i++];
                    ++j;
                } while (!response[i].startsWith(PREFIX_ID) && i < response.length - 1);

                SlimSong slimSong = new SlimSong();
                convertResponseToItem(slimSong, songArray);
                retList.add(slimSong);
            } else {
                ++i;
            }
        }

        return retList;
    }

    public Collection<SlimSong> listSongsForGenre(SlimGenre genre) throws SlimDatabaseException {
        List<SlimSong> retList = new ArrayList<SlimSong>();

        String command = PROP_TITLE.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_GENRE + genre.getId() + " " + TAG_RETURN
                + SlimPlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        int count = Integer.parseInt(response[response.length - 1].split(PREFIX_COUNT)[1]);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID)) {
                ++counter;
                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;

                String[] songArray = new String[tagCount];

                int j = 0;
                do {
                    songArray[j] = response[i++];
                    ++j;
                } while (!response[i].startsWith(PREFIX_ID) && i < response.length - 1);

                SlimSong slimSong = new SlimSong();
                convertResponseToItem(slimSong, songArray);
                retList.add(slimSong);
            } else {
                ++i;
            }
        }

        return retList;
    }

    /**
     * Returns the first {@link SlimSong} for a given {@link SlimAlbum}
     *
     * @param album the {@link SlimAlbum}
     * @return the first {@link SlimSong}
     * @deprecated this method has been deprecated since it's only real use was
     *             for album art, which is now a part of the album
     */
    public SlimSong getFirstSongForAlbum(SlimAlbum album) {
        SlimSong song = null;

        String command = PROP_TITLE.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, "1");
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_ALBUM + album.getId() + " " + TAG_RETURN
                + SlimPlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);
        for (int i = 0; i < response.length - 1; ) {
            if (response[i].startsWith(PREFIX_ID)) {
                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;
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
     * Returns a <code>Collection</code> of {@link SlimSong}s for a given
     * {@link SlimAlbum}
     *
     * @param album the {@link SlimAlbum}
     * @return a <code>Collection</code> of {@link SlimSong}s
     */
    public Collection<SlimSong> listSongsForAlbum(SlimAlbum album) throws SlimDatabaseException {
        List<SlimSong> retList = new ArrayList<SlimSong>();

        String command = PROP_TITLE.replaceAll(PARAM_START, "0");
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_ALBUM + album.getId() + " " + TAG_RETURN
                + SlimPlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        int count = Integer.parseInt(response[response.length - 1].split(PREFIX_COUNT)[1]);
        int i = 0;
        for (int counter = 0; counter < count && i < response.length; ) {
            if (response[i].startsWith(PREFIX_ID)) {
                ++counter;
                int tagCount = SlimPlayableTags.RETURN_TAGS.length() + 2;

                String[] songArray = new String[tagCount];

                int j = 0;
                do {
                    songArray[j] = response[i++];
                    ++j;
                } while (!response[i].startsWith(PREFIX_ID) && i < response.length - 1);

                SlimSong slimSong = new SlimSong();
                convertResponseToItem(slimSong, songArray);
                retList.add(slimSong);
            } else {
                ++i;
            }
        }

        return retList;
    }

    /**
     * Returns the {@link SlimSong} for a song id. Returns <code>null</code> if
     * there is not a match.
     *
     * @param songId the song id
     * @return the {@link SlimSong}
     */
    public SlimPlayableItem lookupItem(String songId) {
        String command = PROP_SONG_INFO.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_TRACK + songId + " " + TAG_RETURN
                + SlimPlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            return null;
        } else {
            SlimSong slimSong = new SlimSong();
            convertResponseToItem(slimSong, response);
            slimSong.setId(songId);
            return slimSong;
        }
    }

    /**
     * Returns the {@link SlimSong} for a song id. Returns <code>null</code> if
     * there is not a match.
     *
     * @param songId the song id
     * @return the {@link SlimSong}
     */
    public SlimSong lookupSong(String songId) {
        String command = PROP_SONG_INFO.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_TRACK + songId + " " + TAG_RETURN
                + SlimPlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            return null;
        } else {
            SlimSong slimSong = new SlimSong();
            convertResponseToItem(slimSong, response);
            slimSong.setId(songId);
            return slimSong;
        }
    }

    /**
     * Returns the {@link SlimSong} for a song id. Returns <code>null</code> if
     * there is not a match. This will return null for remote streams.
     *
     * @param url the song url
     * @return the {@link SlimSong}
     */
    public SlimPlayableItem lookupItemByUrl(String url) {

        if (url == null) {
            return null;
        }

        String command = PROP_SONG_INFO.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, SEARCH_TAG_URL + url + " " + TAG_RETURN + SlimPlayableTags.RETURN_TAGS);

        String[] response = sendCommand(command);

        if (response[0] == null) {
            return null;
        } else {
            SlimSong slimSong = new SlimSong();
            convertResponseToItem(slimSong, response);
            slimSong.setUrl(url);
            return slimSong;
        }
    }

    /**
     * Returns the progress of the database scan. Progress results are stored in
     * a {@link SlimDatabaseProgress} object.
     *
     * @return a {@link SlimDatabaseProgress} containing scan results
     */
    public SlimDatabaseProgress getScanProgress() {
        String command = PROP_RESCAN_PROGRESS.replaceAll(PARAM_TAGS, "");

        String[] response = sendCommand(command);

        if (response == null) {
            return null;
        } else {
            SlimDatabaseProgress sdp = new SlimDatabaseProgress();

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
        PROP_SONG_COUNT = prop.getProperty(SlimConstants.PROP_DB_SONG_COUNT);
        PROP_ALBUM_COUNT = prop.getProperty(SlimConstants.PROP_DB_ALBUM_COUNT);
        PROP_ARTIST_COUNT = prop.getProperty(SlimConstants.PROP_DB_ARTIST_COUNT);
        PROP_GENRE_COUNT = prop.getProperty(SlimConstants.PROP_DB_GENRE_COUNT);
        PROP_SEARCH = prop.getProperty(SlimConstants.PROP_DB_SEARCH);
        PROP_SONG_INFO = prop.getProperty(SlimConstants.PROP_DB_SONG_INFO);
        PROP_RESCAN = prop.getProperty(SlimConstants.PROP_DB_RESCAN);
        PROP_RESCAN_QUERY = prop.getProperty(SlimConstants.PROP_DB_RESCAN_QUERY);
        PROP_WIPECACHE = prop.getProperty(SlimConstants.PROP_DB_WIPECACHE);
        PROP_GENRES = prop.getProperty(SlimConstants.PROP_DB_GENRES);
        PROP_ALBUMS = prop.getProperty(SlimConstants.PROP_DB_ALBUMS);
        PROP_ARTISTS = prop.getProperty(SlimConstants.PROP_DB_ARTISTS);
        PROP_YEARS = prop.getProperty(SlimConstants.PROP_DB_YEARS);
        PROP_TITLE = prop.getProperty(SlimConstants.PROP_DB_TITLE);
        PROP_RESCAN_PROGRESS = prop.getProperty(SlimConstants.PROP_DB_RESCAN_PROGRESS);
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
     * Returns a {@code Collection} of all {@link SlimGenre}s
     *
     * @return a {@code Collection} of all {@link SlimGenre}s
     */
    public Collection<SlimGenre> getGenres() {

        String command = PROP_GENRES.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, "");

        String[] response = sendCommand(command);

        return parseGenres(response);
    }

    /**
     * Returns a {@code Collection} of all {@link SlimArtist}s
     *
     * @return a {@code Collection} of all {@link SlimArtist}s
     */
    public Collection<SlimArtist> getArtists() {
        String command = PROP_ARTISTS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, "");

        String[] response = sendCommand(command);

        return parseArtists(response);
    }

    /**
     * Returns a {@code Collection} of all {@link SlimAlbum}s.  This method does not populate
     * the artist field so an additional call is necessary if you need that field later on.
     * <p/>
     * Use {@link SlimDatabase#getAlbums(boolean) if you with to populate the artist field.
     *
     * @return a {@code Collection} of all {@link SlimAlbum}s
     */
    public Collection<SlimAlbum> getAlbums() {
        return getAlbums(false);
    }

    /**
     * Returns a {@code Collection} of all {@link SlimAlbum}s include the artist name
     * associated with the album.  Passing true slightly affects the performance of the lookup.
     *
     * @param includeArtist
     * @return a {@code Collection} of all {@link SlimAlbum}s
     */
    public Collection<SlimAlbum> getAlbums(boolean includeArtist) {
        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, TAG_RETURN +
                (includeArtist ?
                        SlimAlbum.RETURN_TAGS + SlimAlbum.RETURN_ARTIST :
                        SlimAlbum.RETURN_TAGS));

        String[] response = sendCommand(command);

        return parseAlbums(response);
    }

    /**
     * Returns a {@code Collection} of new music {@link SlimAlbum}s.  This method
     * does not populate the artist field so an additional call is necessary if you
     * need that field later on.
     * <p/>
     * Use {@link SlimDatabase#getAlbums(boolean) if you with to populate the artist field.
     *
     * @return a {@code Collection} of all {@link SlimAlbum}s
     */
    public Collection<SlimAlbum> getNewMusic() {
        return getNewMusic(false);
    }

    /**
     * Returns a {@code Collection} of new music {@link SlimAlbum}s including
     * the artist name associated with the album.  Passing true slightly affects
     * the performance of the lookup.
     *
     * @param includeArtist
     * @return a {@code Collection} of all {@link SlimAlbum}s
     */
    public Collection<SlimAlbum> getNewMusic(boolean includeArtist) {
        String command = PROP_ALBUMS.replaceAll(PARAM_START, RESULTS_START);
        command = command.replaceAll(PARAM_ITEMS, PARAM_RESULTS_MAX);
        command = command.replaceAll(PARAM_TAGS, TAG_RETURN
                + (includeArtist
                ? SlimAlbum.RETURN_TAGS + SlimAlbum.RETURN_ARTIST
                : SlimAlbum.RETURN_TAGS)
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
                return getSlimServer().sendCommand(new SlimCommand(slimCommand));
            } else {
                return getSlimServer().sendCommand(new SlimCommand(slimCommand, param));
            }
        } catch (Exception e) {
            logger.fatal(e);
            return null;
        }

    }

    /**
     * Returns the {@link SlimServer} for this database
     *
     * @return the {@link SlimServer}
     */
    public SlimServer getSlimServer() {
        return slimServer;
    }

    /**
     * Sets the {@link SlimServer} for this database
     *
     * @param slimServer the {@link SlimServer}
     */
    public void setSlimServer(SlimServer slimServer) {
        this.slimServer = slimServer;
    }

    protected void convertResponseToItem(SlimPlayableItem item, String[] response) {
        getSlimServer().convertResponse(item, response);
    }

    private String encode(String criteria) {
        return Utils.encode(criteria, SlimServer.getEncoding());
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
