/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.exception.SlimConnectionException;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.musicobjects.*;
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
    private static final String PROP_SERVER_PATH = "path.server.mp3";
    public static final String EXTENSION = ".mp3";
    public static final String FILE_PROPS = System.getProperty("user.dir") + "/src/test/java/TestProperties.properties";
    private static final String URL_PREFIX = "file://";
    private static final String PROP_VERSION = "slim.version";
    private String path;
    private String server;
    private int webPort;
    private int cliPort;
    private String serverPath;
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
    private SlimServer slimServer;
    private SlimDatabase slimDatabase;
    private List<SlimArtist> databaseArtists;
    private List<SlimPlayableItem> databaseSongs;
    private HashMap<SlimArtist, Collection<SlimSong>> artistSongMap = new HashMap<SlimArtist, Collection<SlimSong>>();
    private HashMap<String, Collection<SlimAlbum>> albumYearMap = new HashMap<String, Collection<SlimAlbum>>();
    private HashMap<SlimGenre, Collection<SlimAlbum>> albumGenreMap = new HashMap<SlimGenre, Collection<SlimAlbum>>();
    private HashMap<SlimGenre, Collection<SlimArtist>> artistGenreMap = new HashMap<SlimGenre, Collection<SlimArtist>>();
    private HashMap<SlimArtist, Collection<SlimAlbum>> artistAlbumMap;
    private HashMap<String, Integer> discAlbumMap = new HashMap<String, Integer>();
    private Collection<SlimArtist> artists;
    private Collection<SlimAlbum> albums;
    private Collection<SlimSong> songs;
    private Collection<SlimGenre> genres;
    private Collection<String> years;
    private SlimPlayer player;
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
            setServerPath(props.getProperty(PROP_SERVER_PATH));
            version = props.getProperty(PROP_VERSION);
        } finally {
            in.close();
        }
    }

    public void printArtist(SlimArtist artist) {
        List<SlimSong> songs = new ArrayList<SlimSong>(getArtistSongMap().get(artist));
        System.out.println(artist);
        for (SlimSong s : songs) {
            System.out.println("\t" + s.getId() + s.getTitle());
        }
    }

    public Collection<SlimSong> getSongsForArtist(SlimArtist artist) {
        Iterator it = getArtistSongMap().keySet().iterator();
        while (it.hasNext()) {
            SlimArtist key = (SlimArtist) it.next();

            if (key.getName().equals(artist.getName())) {
                return getArtistSongMap().get(key);
            }
        }

        return null;
    }

    private Controller() throws SlimConnectionException, IOException, SlimException {
        loadProperties();

        setSlimServer(new SlimServer(getServer(), getCliPort(), getWebPort()));
        setSlimDatabase(new SlimDatabase(getSlimServer()));
        setGroupedByDisc(getSlimDatabase().getSlimServer().isDiscsGroupedAsSingle());
    }

    public static Controller getInstance() {
        if (instance == null) {
            try {
                instance = new Controller();
            } catch (SlimConnectionException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SlimException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return instance;
    }

    private void loadSongs(File f) throws SlimException {

        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++) {

            File file = files[i];

            if (file.isDirectory()) {
                loadSongs(file);
            } else {
                if (file.getName().endsWith(EXTENSION)) {
                    String[] s = file.getName().replace(EXTENSION, "").split("-");
                    SlimSong song = new SlimSong();
                    song.setUrl(URL_PREFIX + getServerPath() + (f.isDirectory() ? f.getName() : "") + "/" + file.getName());
                    SlimAlbum album = new SlimAlbum();
                    SlimArtist artist = new SlimArtist();
                    SlimGenre genre = new SlimGenre();
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

                    for (SlimAlbum a : albums) {
                        if (a.getName().equals(album.getName())) {
                            album = a;
                        }
                    }

                    song.setAlbum(album);
                    song.setYear(year);

                    for (SlimGenre g : genres) {
                        if (g.getName().equals(genre.getName())) {
                            genre = g;
                        }
                    }
                    song.setGenre(genre);

                    fillSongId(song);

                    songs.add(song);

                    fillArtistId(artist);

                    if (Songs.getTestSongArtistMap().get(artist) == null) {
                        Songs.getTestSongArtistMap().put(artist, new ArrayList<SlimSong>());
                    }

                    Songs.getTestSongArtistMap().get(artist).add(song);

                    boolean found = false;
                    for (SlimArtist a : artists) {
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
                    for (SlimAlbum a : albums) {
                        if (a.getName().equals(album.getName())) {
                            found = true;
                        }
                    }

                    if (!found) {
                        albums.add(album);
                    }

                    if (Artists.TEST_ARTIST_ALBUM_MAP.get(artist) == null) {
                        Artists.TEST_ARTIST_ALBUM_MAP.put(artist, new ArrayList<SlimAlbum>());
                    }

                    if (!Artists.TEST_ARTIST_ALBUM_MAP.get(artist).contains(album)) {
                        Artists.TEST_ARTIST_ALBUM_MAP.get(artist).add(album);
                    }

                    if (Songs.getTestSongArtistMap().get(artist) == null) {
                        Songs.getTestSongArtistMap().put(artist, new ArrayList<SlimSong>());
                    }

                    if (!Songs.getTestSongArtistMap().get(artist).contains(song)) {
                        Songs.getTestSongArtistMap().get(artist).add(song);
                    }

                    found = false;
                    for (SlimGenre g : genres) {
                        if (g.getName().equals(genre.getName())) {
                            found = true;
                        }
                    }
                    if (!found) {
                        genres.add(genre);
                    }

                    if (Genres.GENRE_ALBUM_MAP.get(genre) == null) {
                        Genres.GENRE_ALBUM_MAP.put(genre, new ArrayList<SlimAlbum>());
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
                        Years.YEAR_ALBUM_MAP.put(year, new ArrayList<SlimAlbum>());
                    }

                    if (!Years.YEAR_ALBUM_MAP.get(year).contains(album)) {
                        Years.YEAR_ALBUM_MAP.get(year).add(album);
                    }
                }
            }
        }
    }

    public void loadSongs() throws SlimException {
        if (!songsLoaded) {
            System.out.println(Calendar.getInstance().getTime() + "Loading");
            songs = new ArrayList<SlimSong>();
            artists = new ArrayList<SlimArtist>();
//            artists.add()
            albums = new ArrayList<SlimAlbum>();
            genres = new ArrayList<SlimGenre>();
            years = new ArrayList<String>();

            artistAlbumMap = new HashMap<SlimArtist, Collection<SlimAlbum>>();

            loadSongs(new File(getPath()));

            Songs.setTestSongs(new ArrayList<SlimSong>(getSongs()));
            Albums.setTestAlbums(new ArrayList<SlimAlbum>(getAlbums()));
            Artists.setTestArtists(new ArrayList<SlimArtist>(getArtists()));
            Genres.setTestGenres(new ArrayList<SlimGenre>(getGenres()));
            Years.setTestYears(new ArrayList<String>(getYears()));
            setDatabaseSongs(new ArrayList<SlimPlayableItem>(Songs.getTestDatabaseSongMap().values()));
            fillGenreIds();
            songsLoaded = true;
        }
    }

    /**
     * @return the slimServer
     */
    public SlimServer getSlimServer() {
        return slimServer;
    }

    /**
     * @param slimServer the slimServer to set
     */
    private void setSlimServer(SlimServer slimServer) {
        this.slimServer = slimServer;
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
    public HashMap<SlimArtist, Collection<SlimSong>> getArtistSongMap() {
        return artistSongMap;
    }

    /**
     * @param artistSongMap the artistSongMap to set
     */
    public void setArtistSongMap(HashMap<SlimArtist, Collection<SlimSong>> artistSongMap) {
        this.artistSongMap = artistSongMap;
    }

    /**
     * @return the albumYearMap
     */
    public HashMap<String, Collection<SlimAlbum>> getAlbumYearMap() {
        return albumYearMap;
    }

    /**
     * @param albumYearMap the albumYearMap to set
     */
    public void setAlbumYearMap(HashMap<String, Collection<SlimAlbum>> albumYearMap) {
        this.albumYearMap = albumYearMap;
    }

    /**
     * @return the albumGenreMap
     */
    public HashMap<SlimGenre, Collection<SlimAlbum>> getAlbumGenreMap() {
        return albumGenreMap;
    }

    /**
     * @param albumGenreMap the albumGenreMap to set
     */
    public void setAlbumGenreMap(HashMap<SlimGenre, Collection<SlimAlbum>> albumGenreMap) {
        this.albumGenreMap = albumGenreMap;
    }

    /**
     * @return the artistGenreMap
     */
    public HashMap<SlimGenre, Collection<SlimArtist>> getArtistGenreMap() {
        return artistGenreMap;
    }

    /**
     * @param artistGenreMap the artistGenreMap to set
     */
    public void setArtistGenreMap(HashMap<SlimGenre, Collection<SlimArtist>> artistGenreMap) {
        this.artistGenreMap = artistGenreMap;
    }

    /**
     * @return the artistAlbumMap
     */
    public HashMap<SlimArtist, Collection<SlimAlbum>> getArtistAlbumMap() {
        return artistAlbumMap;
    }

    /**
     * @param albumArtistMap the artistAlbumMap to set
     */
    public void setArtistAlbumMap(HashMap<SlimArtist, Collection<SlimAlbum>> albumArtistMap) {
        this.artistAlbumMap = albumArtistMap;
    }

    /**
     * @return the artists
     */
    public Collection<SlimArtist> getArtists() {
        return artists;
    }

    /**
     * @param artists the artists to set
     */
    public void setArtists(Collection<SlimArtist> artists) {
        this.artists = artists;
    }

    /**
     * @return the albums
     */
    public Collection<SlimAlbum> getAlbums() {
        return albums;
    }

    /**
     * @param albums the albums to set
     */
    public void setAlbums(Collection<SlimAlbum> albums) {
        this.albums = albums;
    }

    /**
     * @return the songs
     */
    public Collection<SlimSong> getSongs() {
        return songs;
    }

    /**
     * @param songs the songs to set
     */
    public void setSongs(Collection<SlimSong> songs) {
        this.songs = songs;
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
    public String getServerPath() {
        return serverPath;
    }

    /**
     * @param serverPath the serverPath to set
     */
    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
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

    public void fillArtistId(SlimArtist artist) throws SlimException {
        if (databaseArtists == null) {
            databaseArtists = new ArrayList<SlimArtist>(getSlimDatabase().getArtists());
        }

        for (SlimArtist a : databaseArtists) {
            if (artist.getName().equals(a.getName())) {
                artist.setId(a.getId());
                break;
            }
        }

        if (artist.getId() == null) {
            Assert.fail("Could not find test artist " + artist.getName() + " in database.");
        }
    }

    public void fillSongId(SlimSong song) {
        SlimPlayableItem s = getSlimDatabase().lookupItemByUrl(song.getUrl());
        //songs.add(s);
        //this cast may need to be removed someday
        try {
            Songs.getTestDatabaseSongMap().put(s.getUrl(), (SlimSong) s);
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

    List<SlimAlbum> databaseAlbums;
    private boolean groupedByDisc;

    public void fillAlbum(SlimAlbum album) throws SlimException {
        if (databaseAlbums == null) {
            databaseAlbums = new ArrayList<SlimAlbum>(getSlimDatabase().getAlbums());
        }

        if (!isGroupedByDisc()) {
            if (getDiscAlbumMap().get(album.getName()) != null) {
                int num = getDiscAlbumMap().get(album.getName());
                album.setName(album.getName() + " (Disc " + num + ")");
            }
        }

        for (SlimAlbum a : databaseAlbums) {
            if (album.getName().equals(a.getName())) {
                album.setId(a.getId());
                break;
            }
        }

        //maybe multi disc
        if (album.getId() == null
                && getDiscAlbumMap().get(album.getName()) != null) {
            for (SlimAlbum a : databaseAlbums) {
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
        List<SlimGenre> databaseGenres = new ArrayList<SlimGenre>(getSlimDatabase().getGenres());
        for (SlimGenre genre : Genres.getTestGenres()) {
            for (SlimGenre g : databaseGenres) {
                if (genre.getName().equals(g.getName())) {
                    genre.setId(g.getId());
                    break;
                }
            }
        }
    }

    /**
     * @return the slimDatabase
     */
    public SlimDatabase getSlimDatabase() {
        return slimDatabase;
    }

    /**
     * @param slimDatabase the slimDatabase to set
     */
    private void setSlimDatabase(SlimDatabase slimDatabase) {
        this.slimDatabase = slimDatabase;
    }

    /**
     * @return the databaseSongs
     */
    public List<SlimPlayableItem> getDatabaseSongs() {
        return databaseSongs;
    }

    /**
     * @param databaseSongs the databaseSongs to set
     */
    public void setDatabaseSongs(List<SlimPlayableItem> databaseSongs) {
        this.databaseSongs = databaseSongs;
    }

    public SlimPlayer getFirstPlayer() {
        if (player == null) {
            player = new ArrayList<SlimPlayer>(getSlimServer().getSlimPlayers()).get(0);
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
}
