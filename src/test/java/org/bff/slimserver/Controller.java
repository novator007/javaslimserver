/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.capture.CaptureSqueezeCenter;
import org.bff.slimserver.domain.*;
import org.bff.slimserver.exception.ConnectionException;
import org.bff.slimserver.exception.SqueezeException;
import org.bff.slimserver.mock.MockEventListener;
import org.bff.slimserver.mock.MockSqueezeServer;
import org.bff.slimserver.monitor.EventListener;
import org.bff.slimserver.test.data.*;
import org.junit.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Music
 */
public class Controller {

    private static final String PROP_SERVER = "server";
    private static final String PROP_PATH = "path.testmp3";
    private static final String PROP_PORT_WEB = "port.web";
    private static final String PROP_PORT_CLI = "port.cli";
    private static final String PROP_MP3_PATH = "path.server.mp3";
    private static final String PROP_MP3_PATH_2 = "path.server.mp3Path2";
    public static final String EXTENSION = ".mp3";
    public static final String FILE_PROPS = System.getProperty("user.dir") + "/src/test/java/TestProperties.properties";
    private static final String URL_PREFIX = "file://";
    private static final String PROP_VERSION = "slim.version";
    private static final String PROP_MOCK = "mock";
    private static final String PROP_CAPTURE = "capture";
    private String path;
    private String server;
    private RadioPlugin radioPlugin;
    private FavoritePlugin favoritePlugin;
    private SavedPlaylistManager savedPlaylistManager;
    private Playlist playlist;
    private EventListener listener;
    private int webPort;
    private int cliPort;
    private String mp3Path;
    private String mp3Path2;
    private boolean mock;
    private boolean capture;
    private static Controller instance;
    private static final int INDEX_ARTIST = 0;
    private static final int INDEX_ALBUM = 1;
    private static final int INDEX_TRACK = 2;
    private static final int INDEX_TITLE = 3;
    private static final int INDEX_YEAR = 4;
    private static final int INDEX_GENRE = 5;
    private static final int INDEX_COMMENT = 6;
    private static final int INDEX_DISC_NUMBER = 7;
    /**
     * Null values
     */
    private static final String NULL_TITLE = "No Title";
    private static final String NULL_ALBUM = "No Album";
    private static final String NULL_ARTIST = "No Artist";
    private static final String NULL_YEAR = "0";
    private static final String NULL_GENRE = "No Genre";
    private static final String NULL_COMMENT = "";
    private static final String NULL_TRACK = "0";
    private static final String NULL_DISC_NUM = "";
    private SqueezeServer squeezeServer;
    private Database database;
    private FolderBrowser folderBrowser;
    private List<Artist> databaseArtists;
    private List<PlayableItem> databaseSongs;
    private HashMap<Artist, Collection<Song>> artistSongMap = new HashMap<Artist, Collection<Song>>();
    private HashMap<String, Collection<Album>> albumYearMap = new HashMap<String, Collection<Album>>();
    private HashMap<Genre, Collection<Album>> albumGenreMap = new HashMap<Genre, Collection<Album>>();
    private HashMap<Genre, Collection<Artist>> artistGenreMap = new HashMap<Genre, Collection<Artist>>();
    private HashMap<Artist, Collection<Album>> artistAlbumMap;
    private HashMap<String, Integer> discAlbumMap = new HashMap<String, Integer>();
    private Collection<Artist> artists;
    private Collection<Album> albums;
    private Collection<Song> songs;
    private Collection<Genre> genres;
    private Collection<String> years;
    private Player player;
    private static String version;
    private boolean songsLoaded;

    private void loadProperties() throws IOException {
        Properties props = new Properties();

        InputStream in = null;
        try {
            in = new FileInputStream(FILE_PROPS);
            props.load(in);

            setServer(props.getProperty(PROP_SERVER));
            setPath(props.getProperty(PROP_PATH));
            setWebPort(Integer.parseInt(props.getProperty(PROP_PORT_WEB)));
            setCliPort(Integer.parseInt(props.getProperty(PROP_PORT_CLI)));
            setMp3Path(props.getProperty(PROP_MP3_PATH));
            setMp3Path2(props.getProperty(PROP_MP3_PATH_2));
            version = props.getProperty(PROP_VERSION);
            setMock(Boolean.parseBoolean(props.getProperty(PROP_MOCK, "true")));
            setCapture(Boolean.parseBoolean(props.getProperty(PROP_CAPTURE, "false")));
        } finally {
            in.close();
        }
    }

    public void printArtist(Artist artist) {
        List<Song> songs = new ArrayList<Song>(getArtistSongMap().get(artist));
        System.out.println(artist);
        for (Song s : songs) {
            System.out.println("\t" + s.getId() + s.getTitle());
        }
    }

    public Collection<Song> getSongsForArtist(Artist artist) {
        Iterator it = getArtistSongMap().keySet().iterator();
        while (it.hasNext()) {
            Artist key = (Artist) it.next();

            if (key.getName().equals(artist.getName())) {
                return getArtistSongMap().get(key);
            }
        }

        return null;
    }

    private Controller() throws ConnectionException, IOException {
        loadProperties();
        if (isMock()) {
            setSqueezeServer(new MockSqueezeServer(getServer(), getCliPort(), getWebPort()));
            setDatabase(new Database(getSqueezeServer()));
            setPlaylist(new Playlist(getPlayer()));
            setListener(new MockEventListener(getPlayer()));
        } else {
            if (isCapture()) {
                setSqueezeServer(new CaptureSqueezeCenter(getServer(), getCliPort(), getWebPort()));
            } else {
                setSqueezeServer(new SqueezeServer(getServer(), getCliPort(), getWebPort()));
            }
            setDatabase(new Database(getSqueezeServer()));
            setPlaylist(new Playlist(getPlayer()));
            setListener(new EventListener(getPlayer()));
        }

        setGroupedByDisc(getDatabase().getSqueezeServer().isDiscsGroupedAsSingle());
        setFolderBrowser(new FolderBrowser(getSqueezeServer()));
        setRadioPlugin(new RadioPlugin(getSqueezeServer()));
        setFavoritePlugin(new FavoritePlugin(getSqueezeServer()));
        setSavedPlaylistManager(new SavedPlaylistManager(getSqueezeServer()));


    }

    public static Controller getInstance() {
        if (instance == null) {
            try {
                instance = new Controller();
            } catch (ConnectionException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SqueezeException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return instance;
    }

    private void loadSongs(File f) throws SqueezeException {

        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++) {

            File file = files[i];

            if (file.isDirectory()) {
                loadSongs(file);
            } else {
                if (file.getName().endsWith(EXTENSION)) {
                    String[] s = file.getName().replace(EXTENSION, "").split("-");
                    Song song = new Song();
                    song.setUrl(URL_PREFIX + getMp3Path() + "/" + (f.isDirectory() ? f.getName() : "") + "/" + file.getName());
                    Album album = new Album();
                    Artist artist = new Artist();
                    Genre genre = new Genre();
                    String year = "";

                    int discNum = 0;
                    for (int j = 0; j < s.length; j++) {
                        switch (j) {
                            case INDEX_TITLE:
                                song.setName("null".equalsIgnoreCase(s[INDEX_TITLE]) ? NULL_TITLE : s[INDEX_TITLE]);
                                break;

                            case INDEX_ALBUM:
                                album.setName("null".equalsIgnoreCase(s[INDEX_ALBUM]) ? NULL_ALBUM : s[INDEX_ALBUM]);
                                break;

                            case INDEX_ARTIST:
                                artist.setName("null".equalsIgnoreCase(s[INDEX_ARTIST]) ? NULL_ARTIST : s[INDEX_ARTIST]);
                                break;

                            case INDEX_COMMENT:
                                song.setComment("null".equalsIgnoreCase(s[INDEX_COMMENT]) ? NULL_COMMENT : s[INDEX_COMMENT]);
                                break;

                            case INDEX_GENRE:
                                genre.setName("null".equalsIgnoreCase(s[INDEX_GENRE]) ? NULL_GENRE : s[INDEX_GENRE]);
                                break;

                            case INDEX_TRACK:
                                song.setTrack(Integer.parseInt("null".equalsIgnoreCase(s[INDEX_TRACK]) ? NULL_TRACK : s[INDEX_TRACK]));
                                break;

                            case INDEX_YEAR:
                                year = "null".equalsIgnoreCase(s[INDEX_YEAR]) ? NULL_YEAR : s[INDEX_YEAR];
                                break;
                            case INDEX_DISC_NUMBER:
                                if (!"null".equals(s[INDEX_DISC_NUMBER])) {
                                    discNum = Integer.parseInt(s[INDEX_DISC_NUMBER]);
                                }
                                break;
                        }
                    }

                    if (discNum > 0) {
                        getDiscAlbumMap().put(album.getName(), discNum);
                    }

                    for (Album a : albums) {
                        if (a.getName().equals(album.getName())) {
                            album = a;
                        }
                    }

                    song.setAlbum(album);
                    song.setYear(year);

                    for (Genre g : genres) {
                        if (g.getName().equals(genre.getName())) {
                            genre = g;
                        }
                    }
                    song.setGenre(genre);

                    fillSongId(song);

                    songs.add(song);

                    fillArtistId(artist);

                    if (Songs.getTestSongArtistMap().get(artist) == null) {
                        Songs.getTestSongArtistMap().put(artist, new ArrayList<Song>());
                    }

                    Songs.getTestSongArtistMap().get(artist).add(song);

                    boolean found = false;
                    for (Artist a : artists) {
                        if (a.getName().equals(artist.getName())) {
                            found = true;
                            break;
                        }
                    }


                    if (!found) {
                        artists.add(artist);
                    }

                    fillAlbum(album);

                    found = false;
                    for (Album a : albums) {
                        if (a.getName().equals(album.getName())) {
                            found = true;
                        }
                    }

                    if (!found) {
                        albums.add(album);
                    }

                    if (Artists.TEST_ARTIST_ALBUM_MAP.get(artist) == null) {
                        Artists.TEST_ARTIST_ALBUM_MAP.put(artist, new ArrayList<Album>());
                    }

                    if (!Artists.TEST_ARTIST_ALBUM_MAP.get(artist).contains(album)) {
                        Artists.TEST_ARTIST_ALBUM_MAP.get(artist).add(album);
                    }

                    if (Songs.getTestSongArtistMap().get(artist) == null) {
                        Songs.getTestSongArtistMap().put(artist, new ArrayList<Song>());
                    }

                    if (!Songs.getTestSongArtistMap().get(artist).contains(song)) {
                        Songs.getTestSongArtistMap().get(artist).add(song);
                    }

                    found = false;
                    for (Genre g : genres) {
                        if (g.getName().equals(genre.getName())) {
                            found = true;
                        }
                    }
                    if (!found) {
                        genres.add(genre);
                    }

                    if (Genres.GENRE_ALBUM_MAP.get(genre) == null) {
                        Genres.GENRE_ALBUM_MAP.put(genre, new ArrayList<Album>());
                    }

                    if (!Genres.GENRE_ALBUM_MAP.get(genre).contains(album)) {
                        Genres.GENRE_ALBUM_MAP.get(genre).add(album);
                    }

                    found = false;
                    for (String y : years) {
                        if (y.equals(year)) {
                            found = true;
                        }
                    }
                    if (!found && year != NULL_YEAR) {
                        years.add(year);
                    }

                    if (Years.YEAR_ALBUM_MAP.get(year) == null) {
                        Years.YEAR_ALBUM_MAP.put(year, new ArrayList<Album>());
                    }

                    if (!Years.YEAR_ALBUM_MAP.get(year).contains(album)) {
                        Years.YEAR_ALBUM_MAP.get(year).add(album);
                    }
                }
            }
        }
    }

    public void loadSongs() throws SqueezeException {
        if (!songsLoaded) {
            songs = new ArrayList<Song>();
            artists = new ArrayList<Artist>();
            albums = new ArrayList<Album>();
            genres = new ArrayList<Genre>();
            years = new ArrayList<String>();

            artistAlbumMap = new HashMap<Artist, Collection<Album>>();

            loadSongs(new File(getPath()));

            Songs.setTestSongs(new ArrayList<Song>(getSongs()));
            Albums.setTestAlbums(new ArrayList<Album>(getAlbums()));
            Artists.setTestArtists(new ArrayList<Artist>(getArtists()));
            Genres.setTestGenres(new ArrayList<Genre>(getGenres()));
            Years.setTestYears(new ArrayList<String>(getYears()));
            setDatabaseSongs(new ArrayList<PlayableItem>(Songs.getTestDatabaseSongMap().values()));
            fillGenreIds();
            songsLoaded = true;
        }
    }

    /**
     * @return the squeezeServer
     */
    public SqueezeServer getSqueezeServer() {
        return squeezeServer;
    }

    /**
     * @param squeezeServer the squeezeServer to set
     */
    private void setSqueezeServer(SqueezeServer squeezeServer) {
        this.squeezeServer = squeezeServer;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return the artistSongMap
     */
    public HashMap<Artist, Collection<Song>> getArtistSongMap() {
        return artistSongMap;
    }

    /**
     * @param artistSongMap the artistSongMap to set
     */
    public void setArtistSongMap(HashMap<Artist, Collection<Song>> artistSongMap) {
        this.artistSongMap = artistSongMap;
    }

    /**
     * @return the albumYearMap
     */
    public HashMap<String, Collection<Album>> getAlbumYearMap() {
        return albumYearMap;
    }

    /**
     * @param albumYearMap the albumYearMap to set
     */
    public void setAlbumYearMap(HashMap<String, Collection<Album>> albumYearMap) {
        this.albumYearMap = albumYearMap;
    }

    /**
     * @return the albumGenreMap
     */
    public HashMap<Genre, Collection<Album>> getAlbumGenreMap() {
        return albumGenreMap;
    }

    /**
     * @param albumGenreMap the albumGenreMap to set
     */
    public void setAlbumGenreMap(HashMap<Genre, Collection<Album>> albumGenreMap) {
        this.albumGenreMap = albumGenreMap;
    }

    /**
     * @return the artistGenreMap
     */
    public HashMap<Genre, Collection<Artist>> getArtistGenreMap() {
        return artistGenreMap;
    }

    /**
     * @param artistGenreMap the artistGenreMap to set
     */
    public void setArtistGenreMap(HashMap<Genre, Collection<Artist>> artistGenreMap) {
        this.artistGenreMap = artistGenreMap;
    }

    /**
     * @return the artistAlbumMap
     */
    public HashMap<Artist, Collection<Album>> getArtistAlbumMap() {
        return artistAlbumMap;
    }

    /**
     * @param albumArtistMap the artistAlbumMap to set
     */
    public void setArtistAlbumMap(HashMap<Artist, Collection<Album>> albumArtistMap) {
        this.artistAlbumMap = albumArtistMap;
    }

    /**
     * @return the artists
     */
    public Collection<Artist> getArtists() {
        return artists;
    }

    /**
     * @param artists the artists to set
     */
    public void setArtists(Collection<Artist> artists) {
        this.artists = artists;
    }

    /**
     * @return the albums
     */
    public Collection<Album> getAlbums() {
        return albums;
    }

    /**
     * @param albums the albums to set
     */
    public void setAlbums(Collection<Album> albums) {
        this.albums = albums;
    }

    /**
     * @return the songs
     */
    public Collection<Song> getSongs() {
        return songs;
    }

    /**
     * @param songs the songs to set
     */
    public void setSongs(Collection<Song> songs) {
        this.songs = songs;
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

    /**
     * @return the years
     */
    public Collection<String> getYears() {
        return years;
    }

    /**
     * @param years the years to set
     */
    public void setYears(Collection<String> years) {
        this.years = years;
    }

    /**
     * @return the webPort
     */
    public int getWebPort() {
        return webPort;
    }

    /**
     * @param webPort the webPort to set
     */
    public void setWebPort(int webPort) {
        this.webPort = webPort;
    }

    /**
     * @return the cliPort
     */
    private int getCliPort() {
        return cliPort;
    }

    /**
     * @param cliPort the cliPort to set
     */
    public void setCliPort(int cliPort) {
        this.cliPort = cliPort;
    }

    /**
     * @return the serverPath
     */
    public String getMp3Path() {
        return mp3Path;
    }

    /**
     * @param mp3Path the serverPath to set
     */
    public void setMp3Path(String mp3Path) {
        this.mp3Path = mp3Path;
    }

    /**
     * @return the discAlbumMap
     */
    public HashMap<String, Integer> getDiscAlbumMap() {
        return discAlbumMap;
    }

    /**
     * @param discAlbumMap the discAlbumMap to set
     */
    public void setDiscAlbumMap(HashMap<String, Integer> discAlbumMap) {
        this.discAlbumMap = discAlbumMap;
    }

    public void fillArtistId(Artist artist) throws SqueezeException {
        if (databaseArtists == null) {
            databaseArtists = new ArrayList<Artist>(getDatabase().getArtists());
        }

        for (Artist a : databaseArtists) {
            if (artist.getName().equals(a.getName())) {
                artist.setId(a.getId());
                break;
            }
        }

        if (artist.getId() == null) {
            Assert.fail("Could not find test artist " + artist.getName() + " in database.");
        }
    }

    public void fillSongId(Song song) {
        PlayableItem s = getDatabase().lookupItemByUrl(song.getUrl());
        //songs.add(s);
        //this cast may need to be removed someday
        try {
            Songs.getTestDatabaseSongMap().put(s.getUrl(), (Song) s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        song.setId(s.getId());
        try {
            song.setImageUrl(new URL("http://"
                    + getServer()
                    + ":"
                    + getWebPort()
                    + "/music/" + s.getId()
                    + "/cover.jpg"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //}
    }

    List<Album> databaseAlbums;
    private boolean groupedByDisc;

    public void fillAlbum(Album album) throws SqueezeException {
        if (databaseAlbums == null) {
            databaseAlbums = new ArrayList<Album>(getDatabase().getAlbums());
        }

        if (!isGroupedByDisc()) {
            if (getDiscAlbumMap().get(album.getName()) != null) {
                int num = getDiscAlbumMap().get(album.getName());
                album.setName(album.getName() + " (Disc " + num + ")");
            }
        }

        for (Album a : databaseAlbums) {
            if (album.getName().equals(a.getName())) {
                album.setId(a.getId());
                break;
            }
        }

        //maybe multi disc
        if (album.getId() == null
                && getDiscAlbumMap().get(album.getName()) != null) {
            for (Album a : databaseAlbums) {
                int disc = getDiscAlbumMap().get(album.getName());
                if ((album.getName() + " (Disc " + disc + ")").equals(a.getName())) {
                    album.setId(a.getId());
                    break;
                }
            }
        }

        if (album.getId() == null) {
            Assert.fail("Could not find test album " + album.getName() + " in database.");
        }
    }

    public void fillGenreIds() {
        List<Genre> databaseGenres = new ArrayList<Genre>(getDatabase().getGenres());
        for (Genre genre : Genres.getTestGenres()) {
            for (Genre g : databaseGenres) {
                if (genre.getName().equals(g.getName())) {
                    genre.setId(g.getId());
                    break;
                }
            }
        }
    }

    /**
     * @return the database
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * @param database the database to set
     */
    private void setDatabase(Database database) {
        this.database = database;
    }

    /**
     * @return the databaseSongs
     */
    public List<PlayableItem> getDatabaseSongs() {
        return databaseSongs;
    }

    /**
     * @param databaseSongs the databaseSongs to set
     */
    public void setDatabaseSongs(List<PlayableItem> databaseSongs) {
        this.databaseSongs = databaseSongs;
    }

    public Player getPlayer() {
        if (player == null) {
            player = new ArrayList<Player>(getSqueezeServer().getSlimPlayers()).get(0);
        }

        return player;
    }

    /**
     * @return the groupedByDisc
     */
    public boolean isGroupedByDisc() {
        return groupedByDisc;
    }

    /**
     * @param groupedByDisc the groupedByDisc to set
     */
    public void setGroupedByDisc(boolean groupedByDisc) {
        this.groupedByDisc = groupedByDisc;
    }

    /**
     * @return the version
     */
    public static String getVersion() {
        return version;
    }

    public String getMp3Path2() {
        return mp3Path2;
    }

    public void setMp3Path2(String mp3Path2) {
        this.mp3Path2 = mp3Path2;
    }

    public boolean isMock() {
        return mock;
    }

    public void setMock(boolean mock) {
        this.mock = mock;
    }

    public FolderBrowser getFolderBrowser() {
        return folderBrowser;
    }

    public void setFolderBrowser(FolderBrowser folderBrowser) {
        this.folderBrowser = folderBrowser;
    }

    public RadioPlugin getRadioPlugin() {
        return radioPlugin;
    }

    public void setRadioPlugin(RadioPlugin radioPlugin) {
        this.radioPlugin = radioPlugin;
    }

    public FavoritePlugin getFavoritePlugin() {
        return favoritePlugin;
    }

    public void setFavoritePlugin(FavoritePlugin favoritePlugin) {
        this.favoritePlugin = favoritePlugin;
    }

    public SavedPlaylistManager getSavedPlaylistManager() {
        return savedPlaylistManager;
    }

    public void setSavedPlaylistManager(SavedPlaylistManager savedPlaylistManager) {
        this.savedPlaylistManager = savedPlaylistManager;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public EventListener getListener() {
        return listener;
    }

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public boolean isCapture() {
        return capture;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }
}
