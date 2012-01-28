/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

import org.bff.squeezeserver.domain.*;
import org.bff.squeezeserver.exception.DatabaseException;
import org.bff.squeezeserver.exception.SqueezeException;
import org.bff.squeezeserver.test.data.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bfindeisen
 */
public class DatabaseTest extends Base {

    private static List<Artist> artistList;
    private static List<Album> albumList;
    private static List<Genre> genreList;
    private static List<PlayableItem> songList;
    private static final String SEARCH_CRITERIA_ALL = "a";
    private static final String SEARCH_CRITERIA_ARTIST = "Art";
    private static final String SEARCH_CRITERIA_ALBUM = "Alb";
    private static final String SEARCH_CRITERIA_TITLE = "Title";

    public DatabaseTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws SqueezeException {

        if (getArtistList() == null) {
            setArtistList(new ArrayList<Artist>(getDatabase().getArtists()));
        }

        if (getAlbumList() == null) {
            setAlbumList(new ArrayList<Album>(getDatabase().getAlbums()));
        }

        if (getGenreList() == null) {
            setGenreList(new ArrayList<Genre>(getDatabase().getGenres()));
        }

        if (getSongList() == null) {
            setSongList(new ArrayList<PlayableItem>(Controller.getInstance().getDatabaseSongs()));
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testArtistCount() throws SqueezeException {
        Assert.assertEquals(Artists.getTestArtists().size(), getDatabase().getArtistCount());
    }

    @Test
    public void testAlbumCount() throws SqueezeException {
        Assert.assertEquals(Albums.getTestAlbums().size(), getDatabase().getAlbumCount());
    }

    @Test
    public void testSongCount() throws SqueezeException {
        Assert.assertEquals(Songs.getTestSongs().size(), getDatabase().getSongCount());
    }

    @Test
    public void testGenreCount() throws SqueezeException {
        Assert.assertEquals(Genres.getTestGenres().size(), getDatabase().getGenreCount());
    }

    @Test
    public void testSongs() throws Exception {

        for (PlayableItem song : getSongList()) {
            boolean exists = false;
            for (Song s : Songs.getTestSongs()) {
                if (song.getUrl().equals(s.getUrl())) {
                    exists = true;
                    compareItems(s, song);
                    break;
                }
            }

            if (!exists) {
                Assert.fail("Song does not exist in list.");
            }
        }
    }

    @Test
    public void testAlbumList() {

        for (Album a : Albums.getTestAlbums()) {
            boolean exists = false;
            String albumName = a.getName();
            for (Album album : getAlbumList()) {
                if (album.getName().equals(albumName)) {
                    exists = true;
                    compareItems(a, album);
                    break;
                }
            }

            if (!exists && Controller.getInstance().getDiscAlbumMap().get(a.getName()) != null) {
                //maybe multi disc
                int disc = Controller.getInstance().getDiscAlbumMap().get(a.getName());
                for (Album album : getAlbumList()) {

                    if ((a.getName() + " (Disc " + disc + ")").equals(album.getName())) {
                        exists = true;
                        compareItems(a, album);
                        break;
                    }
                }
            }

            if (!exists) {
                Assert.fail("Album " + a + " does not exist in list.");
            }
        }
    }

    @Test
    public void testGetYears() {
        List<String> resultYears = new ArrayList<String>(getDatabase().getYears());

        List<String> foundYears = new ArrayList<String>(Years.getTestYears());

        Assert.assertEquals(resultYears.size(), foundYears.size());

        for (String year : resultYears) {
            boolean exists = false;

            for (String y : foundYears) {
                if (year.equals(y)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Assert.fail("Year does not exist in list.");
            }
        }

    }

    @Test
    public void testSearchAll() throws DatabaseException {
        SearchResult results = getDatabase().searchAll(SEARCH_CRITERIA_ALL);

        List<Album> resultsAlbums = new ArrayList<Album>(results.getAlbums());
        List<Artist> resultsArtists = new ArrayList<Artist>(results.getArtists());
        List<Song> resultsSongs = new ArrayList<Song>(results.getSongs());
        List<Contributor> resultsContributors = new ArrayList<Contributor>(results.getContributors());
        List<Genre> resultsGenres = new ArrayList<Genre>(results.getGenres());

        List<Song> foundSongs = new ArrayList<Song>();
        for (Song song : Songs.getTestSongs()) {
            if (song.getName().toUpperCase().contains(SEARCH_CRITERIA_ALL.toUpperCase())) {
                foundSongs.add(song);
            }
        }

        List<Artist> foundArtists = new ArrayList<Artist>();
        for (Artist artist : Artists.getTestArtists()) {
            if (artist.getName().toUpperCase().contains(SEARCH_CRITERIA_ALL.toUpperCase())) {
                foundArtists.add(artist);
            }
        }

        List<Genre> foundGenres = new ArrayList<Genre>();
        for (Genre genre : Genres.getTestGenres()) {
            if (genre.getName().toUpperCase().contains(SEARCH_CRITERIA_ALL.toUpperCase())) {
                foundGenres.add(genre);
            }
        }

        List<Album> foundAlbums = new ArrayList<Album>();
        for (Album album : Albums.getTestAlbums()) {
            if (album.getName().toUpperCase().contains(SEARCH_CRITERIA_ALL.toUpperCase())) {
                foundAlbums.add(album);
            }
        }

        Assert.assertEquals(foundArtists.size(), resultsArtists.size() + resultsContributors.size());
        Assert.assertEquals(foundAlbums.size(), resultsAlbums.size());
        Assert.assertEquals(foundSongs.size(), resultsSongs.size());
        Assert.assertEquals(foundGenres.size(), resultsGenres.size());

        for (Song s : foundSongs) {

            boolean exists = false;
            for (Song song : resultsSongs) {
                if (song.getName().equals(s.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Assert.fail("Song does not exist in search list.");
            }
        }

        for (Artist a : foundArtists) {

            boolean exists = false;
            for (Artist artist : resultsArtists) {
                if (a.getName().equals(artist.getName())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                for (Contributor artist : resultsContributors) {
                    if (a.getName().equals(artist.getName())) {
                        exists = true;
                        break;
                    }
                }
            }
            if (!exists) {
                Assert.fail("Artist does not exist in search list.");
            }
        }

        for (Genre g : foundGenres) {

            boolean exists = false;
            for (Genre genre : resultsGenres) {
                if (g.getName().equals(genre.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Assert.fail("Genre does not exist in search list.");
            }
        }

        for (Album a : foundAlbums) {

            boolean exists = false;
            for (Album album : resultsAlbums) {
                if (a.getName().equals(album.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Assert.fail("Album does not exist in search list.");
            }
        }

    }

    @Test
    public void testSearchArtists() throws DatabaseException {

        List<Artist> resultsArtists = new ArrayList<Artist>(getDatabase().searchArtists(SEARCH_CRITERIA_ARTIST));

        if (resultsArtists.size() < 1) {
            Assert.fail("No artists found from search.");
        }

        List<Artist> foundArtists = new ArrayList<Artist>();
        for (Artist artist : Artists.getTestArtists()) {
            if (artist.getName().contains(SEARCH_CRITERIA_ARTIST)) {
                foundArtists.add(artist);
            }
        }

        //The various artist seems to return from SS even though none are various
        Assert.assertEquals(foundArtists.size(), resultsArtists.size() - 1);

        for (Artist a : foundArtists) {

            boolean exists = false;
            for (Artist artist : resultsArtists) {
                if (a.getName().equals(artist.getName())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                Assert.fail("Artist does not exist in search list.");
            }
        }
    }

    @Test
    public void testSearchAlbums() throws DatabaseException {

        List<Album> resultsAlbums = new ArrayList<Album>(getDatabase().searchAlbums(SEARCH_CRITERIA_ALBUM));

        if (resultsAlbums.size() < 1) {
            Assert.fail("No albums found from search.");
        }

        List<Album> foundAlbums = new ArrayList<Album>();

        for (Album album : Albums.getTestAlbums()) {
            if (album.getName().toUpperCase().contains(SEARCH_CRITERIA_ALBUM.toUpperCase())) {
                foundAlbums.add(album);
            }
        }

        Assert.assertEquals(foundAlbums.size(), resultsAlbums.size());

        for (Album a : foundAlbums) {

            boolean exists = false;
            for (Album album : resultsAlbums) {
                if (a.getName().equals(album.getName())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                Assert.fail("Album does not exist in search list.");
            }
        }
    }

    @Test
    public void testSearchTitles() throws DatabaseException {

        List<Song> resultsSongs = new ArrayList<Song>(getDatabase().searchTitles(SEARCH_CRITERIA_TITLE));

        if (resultsSongs.size() < 1) {
            Assert.fail("No songs found from search.");
        }

        List<Song> foundSongs = new ArrayList<Song>();

        for (Song song : Songs.getTestSongs()) {
            if (song.getName().toUpperCase().contains(SEARCH_CRITERIA_TITLE.toUpperCase())) {
                foundSongs.add(song);
            }
        }

        Assert.assertEquals(foundSongs.size(), resultsSongs.size());

        for (Song s : foundSongs) {

            boolean exists = false;
            for (Song song : resultsSongs) {
                if (s.getName().equals(song.getName())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                Assert.fail("Song does not exist in search list.");
            }
        }
    }

    @Test
    public void testListArtists() {
        List<Artist> testArtists = new ArrayList<Artist>(Artists.getTestArtists());

        for (Artist a : testArtists) {
            boolean exists = false;

            for (Artist artist : getArtistList()) {
                if (artist.getName().equals(a.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Assert.fail("Artist does not exist in list.");
            }
        }
    }

    @Test
    public void testListGenres() {
        List<Genre> testGenres = Genres.getTestGenres();

        for (Genre g : testGenres) {

            boolean exists = false;
            for (Genre genre : getGenreList()) {
                if (genre.getName().equals(g.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Assert.fail("Genre does not exist in list.");
            }
        }
    }

    @Test
    public void testListAlbumsForArtist() throws DatabaseException {

        for (Artist a : Artists.getTestArtists()) {
            List<Album> resultsAlbums = new ArrayList<Album>(getDatabase().listAlbumsForArtist(a));

            if (resultsAlbums.size() < 1) {
                Assert.fail("No albums found for artist.");
            }

            for (Album alb : resultsAlbums) {
                boolean exists = false;
                for (Album album : Artists.TEST_ARTIST_ALBUM_MAP.get(a)) {
                    if (alb.getName().equals(album.getName())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    Assert.fail("Album does not exist in artist album list.");
                }
            }
        }
    }

    @Test
    public void testListAlbumsForYear() throws DatabaseException {

        for (String y : Years.getTestYears()) {
            List<Album> resultsAlbums = new ArrayList<Album>(getDatabase().listAlbumsForYear(y));

            if (resultsAlbums.size() < 1) {
                Assert.fail("No albums found for year.");
            }

            for (Album alb : resultsAlbums) {
                boolean exists = false;
                for (Album album : Years.YEAR_ALBUM_MAP.get(y)) {
                    if (alb.getName().equals(album.getName())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    Assert.fail("Album does not exist in year list.");
                }
            }
        }
    }

    @Test
    public void testListAlbums() throws DatabaseException {

        for (Artist a : Artists.getTestArtists()) {
            for (Genre g : Genres.getTestGenres()) {
                for (String y : Years.getTestYears()) {
                    List<Album> resultsAlbums = new ArrayList<Album>(getDatabase().listAlbums(a, g, y));

                    List<Album> foundAlbumsArtist = new ArrayList<Album>(Artists.TEST_ARTIST_ALBUM_MAP.get(a));
                    List<Album> foundAlbumsGenre = new ArrayList<Album>(Genres.GENRE_ALBUM_MAP.get(g));
                    List<Album> foundAlbumsYear = new ArrayList<Album>(Years.YEAR_ALBUM_MAP.get(y));

                    List<Album> foundAlbums = new ArrayList<Album>();
                    for (Album albumArtist : foundAlbumsArtist) {
                        for (Album albumGenre : foundAlbumsGenre) {
                            if (albumArtist.equals(albumGenre)) {
                                for (Album albumYear : foundAlbumsYear) {
                                    if (albumYear.equals(albumGenre)) {
                                        foundAlbums.add(albumGenre);
                                    }
                                }

                            }
                        }
                    }

                    for (Album alb : resultsAlbums) {
                        boolean exists = false;
                        for (Album album : foundAlbums) {
                            if (alb.getName().equals(album.getName())) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            Assert.fail("Album " + alb.getName() + " does not exist in found album list.");
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testListArtistsForGenre() throws DatabaseException {
        for (Genre g : Genres.getTestGenres()) {
            List<Artist> resultsArtists = new ArrayList<Artist>(getDatabase().listArtistsForGenre(g));

            List<Artist> foundArtists = new ArrayList<Artist>();

            List<Album> foundAlbumsGenre = new ArrayList<Album>(Genres.GENRE_ALBUM_MAP.get(g));
            for (Album album : foundAlbumsGenre) {
                for (Artist artist : Artists.getTestArtists()) {
                    for (Album artistAlbum : Artists.TEST_ARTIST_ALBUM_MAP.get(artist)) {
                        if (artistAlbum.getName().equals(album.getName())) {
                            foundArtists.add(artist);
                        }
                    }
                }
            }

            for (Artist art : resultsArtists) {
                boolean exists = false;
                for (Artist artist : foundArtists) {
                    if (art.getName().equals(artist.getName())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    Assert.fail("Artist " + art.getName() + " does not exist in found album list.");
                }
            }
        }
    }

    @Test
    public void testGetArtistForAlbum() throws DatabaseException {
        for (Album album : Albums.getTestAlbums()) {
            List<Artist> resultsArtists = new ArrayList<Artist>(getDatabase().getArtistsForAlbum(album));

            List<Artist> foundArtists = new ArrayList<Artist>();
            for (Artist artist : Artists.getTestArtists()) {
                for (Album artistAlbum : Artists.TEST_ARTIST_ALBUM_MAP.get(artist)) {
                    if (artistAlbum.getName().equals(album.getName())) {
                        foundArtists.add(artist);
                    }
                }
            }

            for (Artist art : resultsArtists) {
                boolean exists = false;
                for (Artist artist : foundArtists) {
                    if (art.getName().equals(artist.getName())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    Assert.fail("Artist " + art.getName() + " does not exist in found album list.");
                }
            }
        }
    }

    @Test
    public void testListSongsForArtist() throws DatabaseException {
        for (Artist artist : Artists.getTestArtists()) {
            List<Song> resultsArtists = new ArrayList<Song>(getDatabase().listSongsForArtist(artist));

            for (Song song : resultsArtists) {
                boolean exists = false;
                for (Song s : Songs.getTestSongArtistMap().get(artist)) {
                    if (s.getName().equals(song.getName())) {
                        exists = true;
                        compareItems(song, s);
                        break;
                    }
                }

                if (!exists) {
                    Assert.fail("Song " + song.getName() + " for artist " + artist.getName()
                            + " does not exist in found song list.");
                }
            }
        }
    }

    @Test
    public void testListSongsForYear() throws DatabaseException {
        for (String year : Years.getTestYears()) {
            List<Song> resultsYears = new ArrayList<Song>(getDatabase().listSongsForYear(year));
            List<Song> foundSongs = new ArrayList<Song>();

            for (Song s : Songs.getTestSongs()) {
                if (s.getYear().equals(year)) {
                    foundSongs.add(s);
                }
            }

            Assert.assertEquals(resultsYears.size(), foundSongs.size());

            for (Song song : resultsYears) {
                boolean exists = false;
                for (Song s : foundSongs) {
                    if (s.getName().equals(song.getName())) {
                        exists = true;
                        compareItems(song, s);
                        break;
                    }
                }

                if (!exists) {
                    Assert.fail("Song " + song.getName() + " for year " + year + " does not exist in found song list.");
                }
            }
        }
    }

    @Test
    public void testListSongsForGenre() throws DatabaseException {
        for (Genre genre : Genres.getTestGenres()) {
            List<Song> resultsGenres = new ArrayList<Song>(getDatabase().listSongsForGenre(genre));
            List<Song> foundSongs = new ArrayList<Song>();

            for (Song s : Songs.getTestSongs()) {
                if (s.getGenre().equals(genre)) {
                    foundSongs.add(s);
                }
            }

            Assert.assertEquals(resultsGenres.size(), foundSongs.size());

            for (Song song : resultsGenres) {
                boolean exists = false;
                for (Song s : foundSongs) {
                    if (s.getName().equals(song.getName())) {
                        exists = true;
                        compareItems(song, s);
                        break;
                    }
                }

                if (!exists) {
                    Assert.fail("Song " + song.getName() + " for genre " + genre.getName()
                            + " does not exist in found song list.");
                }
            }

        }
    }

    @Test
    public void testListSongsForAlbum() throws DatabaseException {
        for (Album album : Albums.getTestAlbums()) {
            List<Song> resultsGenres = new ArrayList<Song>(getDatabase().listSongsForAlbum(album));
            List<Song> foundSongs = new ArrayList<Song>();

            for (Song s : Songs.getTestSongs()) {
                if (s.getAlbum().equals(album)) {
                    foundSongs.add(s);
                }
            }

            Assert.assertEquals(resultsGenres.size(), foundSongs.size());

            for (Song song : resultsGenres) {
                boolean exists = false;
                for (Song s : foundSongs) {
                    if (s.getName().equals(song.getName())) {
                        exists = true;
                        compareItems(song, s);
                        break;
                    }
                }

                if (!exists) {
                    Assert.fail("Song " + song.getName() + " for album " + album.getName()
                            + " does not exist in found song list.");
                }
            }
        }
    }

    @Test
    public void testNewMusic() {
        //for now just make sure not empty
        for (Album album : getDatabase().getNewMusic()) {
            //System.out.println(album.getName());
        }
        Assert.assertFalse(getDatabase().getNewMusic().isEmpty());
    }

    @Test
    public void testNewMusicWithArtist() {
        //for now just make sure not empty
        Assert.assertFalse(getDatabase().getNewMusic(true).isEmpty());
    }

    @Test(timeout = 60000)
    public void testDatabaseProgress() {
        getDatabase().rescan();

        while (!getDatabase().isRescanning()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (getDatabase().isRescanning()) {
            DatabaseProgress progress = getDatabase().getScanProgress();

            List<DatabaseProgress.Importer> importers = progress.getImporters();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (DatabaseProgress.Importer importer : importers) {
//                System.out.println(progress.getFullName());
//                System.out.println("\t" + importer.getImporter() + " \t " + importer.getPercentage());
            }
        }
    }

    private void compareItems(Album album1, Album album2) {
        Assert.assertEquals(album1.getId(), album2.getId());
        Assert.assertEquals(album1.getName(), album2.getName());
        Assert.assertEquals(album1.isCompilation(), album2.isCompilation());
    }

    private void compareItems(Song item1, PlayableItem item2) {
        Assert.assertEquals(item1.getId(), item2.getId());
        Assert.assertEquals(item1.getName(), item2.getName());
        Assert.assertEquals(item1.getUrl(), item2.getUrl());
        Assert.assertEquals(item1.getAlbum(), item2.getAlbum());
        Assert.assertEquals(item1.getGenre(), item2.getGenre());
        Assert.assertEquals(item1.getGenre().getName(), item2.getGenre().getName());
        Assert.assertEquals(item1.getYear(), item2.getYear());
        Assert.assertEquals(item1.getTrack(), item2.getTrack());
        Assert.assertEquals(item1.getImageUrl(), item2.getImageUrl());
    }

    /**
     * @return the artistList
     */
    public static List<Artist> getArtistList() {
        return artistList;
    }

    /**
     * @param artists the artistList to set
     */
    public static void setArtistList(List<Artist> artists) {
        artistList = artists;
    }

    /**
     * @return the album list
     */
    public static List<Album> getAlbumList() {
        return albumList;
    }

    /**
     * @param albums the album list to set
     */
    public static void setAlbumList(List<Album> albums) {
        albumList = albums;
    }

    /**
     * @return the genreList
     */
    public static List<Genre> getGenreList() {
        return genreList;
    }

    /**
     * @param genres the genreList to set
     */
    public static void setGenreList(List<Genre> genres) {
        genreList = genres;
    }

    /**
     * @return the songList
     */
    public static List<PlayableItem> getSongList() {
        return songList;
    }

    /**
     * @param songs the songList to set
     */
    public static void setSongList(List<PlayableItem> songs) {
        songList = songs;
    }
}