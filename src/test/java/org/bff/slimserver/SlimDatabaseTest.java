/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.exception.SlimDatabaseException;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.musicobjects.*;
import org.bff.slimserver.test.data.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bfindeisen
 */
public class SlimDatabaseTest extends Base {

    private static List<SlimArtist> artistList;
    private static List<SlimAlbum> albumList;
    private static List<SlimGenre> genreList;
    private static List<SlimPlayableItem> songList;
    private static final String SEARCH_CRITERIA_ALL = "a";
    private static final String SEARCH_CRITERIA_ARTIST = "Art";
    private static final String SEARCH_CRITERIA_ALBUM = "Alb";
    private static final String SEARCH_CRITERIA_TITLE = "Title";

    public SlimDatabaseTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws SlimException {

        if (getArtistList() == null) {
            setArtistList(new ArrayList<SlimArtist>(getDatabase().getArtists()));
        }

        if (getAlbumList() == null) {
            setAlbumList(new ArrayList<SlimAlbum>(getDatabase().getAlbums()));
        }

        if (getGenreList() == null) {
            setGenreList(new ArrayList<SlimGenre>(getDatabase().getGenres()));
        }

        if (getSongList() == null) {
            setSongList(new ArrayList<SlimPlayableItem>(Controller.getInstance().getDatabaseSongs()));
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testArtistCount() throws SlimException {
        Assert.assertEquals(Artists.getTestArtists().size(), getDatabase().getArtistCount());
    }

    @Test
    public void testAlbumCount() throws SlimException {
        Assert.assertEquals(Albums.getTestAlbums().size(), getDatabase().getAlbumCount());
    }

    @Test
    public void testSongCount() throws SlimException {
        Assert.assertEquals(Songs.getTestSongs().size(), getDatabase().getSongCount());
    }

    @Test
    public void testGenreCount() throws SlimException {
        Assert.assertEquals(Genres.getTestGenres().size(), getDatabase().getGenreCount());
    }

    @Test
    public void testSongs() throws Exception {

        for (SlimPlayableItem song : getSongList()) {
            boolean exists = false;
            for (SlimSong s : Songs.getTestSongs()) {
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

        for (SlimAlbum a : Albums.getTestAlbums()) {
            boolean exists = false;
            String albumName = a.getName();
            for (SlimAlbum album : getAlbumList()) {
                if (album.getName().equals(albumName)) {
                    exists = true;
                    compareItems(a, album);
                    break;
                }
            }

            if (!exists && Controller.getInstance().getDiscAlbumMap().get(a.getName()) != null) {
                //maybe multi disc
                int disc = Controller.getInstance().getDiscAlbumMap().get(a.getName());
                for (SlimAlbum album : getAlbumList()) {

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
    public void testSearchAll() throws SlimDatabaseException {
        SlimSearchResult results = getDatabase().searchAll(SEARCH_CRITERIA_ALL);

        List<SlimAlbum> resultsAlbums = new ArrayList<SlimAlbum>(results.getAlbums());
        List<SlimArtist> resultsArtists = new ArrayList<SlimArtist>(results.getArtists());
        List<SlimSong> resultsSongs = new ArrayList<SlimSong>(results.getSongs());
        List<SlimContributor> resultsContributors = new ArrayList<SlimContributor>(results.getContributors());
        List<SlimGenre> resultsGenres = new ArrayList<SlimGenre>(results.getGenres());

        List<SlimSong> foundSongs = new ArrayList<SlimSong>();
        for (SlimSong song : Songs.getTestSongs()) {
            if (song.getName().toUpperCase().contains(SEARCH_CRITERIA_ALL.toUpperCase())) {
                foundSongs.add(song);
            }
        }

        List<SlimArtist> foundArtists = new ArrayList<SlimArtist>();
        for (SlimArtist artist : Artists.getTestArtists()) {
            if (artist.getName().toUpperCase().contains(SEARCH_CRITERIA_ALL.toUpperCase())) {
                foundArtists.add(artist);
            }
        }

        List<SlimGenre> foundGenres = new ArrayList<SlimGenre>();
        for (SlimGenre genre : Genres.getTestGenres()) {
            if (genre.getName().toUpperCase().contains(SEARCH_CRITERIA_ALL.toUpperCase())) {
                foundGenres.add(genre);
            }
        }

        List<SlimAlbum> foundAlbums = new ArrayList<SlimAlbum>();
        for (SlimAlbum album : Albums.getTestAlbums()) {
            if (album.getName().toUpperCase().contains(SEARCH_CRITERIA_ALL.toUpperCase())) {
                foundAlbums.add(album);
            }
        }

        Assert.assertEquals(foundArtists.size(), resultsArtists.size() + resultsContributors.size());
        Assert.assertEquals(foundAlbums.size(), resultsAlbums.size());
        Assert.assertEquals(foundSongs.size(), resultsSongs.size());
        Assert.assertEquals(foundGenres.size(), resultsGenres.size());

        for (SlimSong s : foundSongs) {

            boolean exists = false;
            for (SlimSong song : resultsSongs) {
                if (song.getName().equals(s.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Assert.fail("Song does not exist in search list.");
            }
        }

        for (SlimArtist a : foundArtists) {

            boolean exists = false;
            for (SlimArtist artist : resultsArtists) {
                if (a.getName().equals(artist.getName())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                for (SlimContributor artist : resultsContributors) {
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

        for (SlimGenre g : foundGenres) {

            boolean exists = false;
            for (SlimGenre genre : resultsGenres) {
                if (g.getName().equals(genre.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Assert.fail("Genre does not exist in search list.");
            }
        }

        for (SlimAlbum a : foundAlbums) {

            boolean exists = false;
            for (SlimAlbum album : resultsAlbums) {
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
    public void testSearchArtists() throws SlimDatabaseException {

        List<SlimArtist> resultsArtists = new ArrayList<SlimArtist>(getDatabase().searchArtists(SEARCH_CRITERIA_ARTIST));

        if (resultsArtists.size() < 1) {
            Assert.fail("No artists found from search.");
        }

        List<SlimArtist> foundArtists = new ArrayList<SlimArtist>();
        for (SlimArtist artist : Artists.getTestArtists()) {
            if (artist.getName().contains(SEARCH_CRITERIA_ARTIST)) {
                foundArtists.add(artist);
            }
        }

        //The various artist seems to return from SS even though none are various
        Assert.assertEquals(foundArtists.size(), resultsArtists.size() - 1);

        for (SlimArtist a : foundArtists) {

            boolean exists = false;
            for (SlimArtist artist : resultsArtists) {
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
    public void testSearchAlbums() throws SlimDatabaseException {

        List<SlimAlbum> resultsAlbums = new ArrayList<SlimAlbum>(getDatabase().searchAlbums(SEARCH_CRITERIA_ALBUM));

        if (resultsAlbums.size() < 1) {
            Assert.fail("No albums found from search.");
        }

        List<SlimAlbum> foundAlbums = new ArrayList<SlimAlbum>();

        for (SlimAlbum album : Albums.getTestAlbums()) {
            if (album.getName().toUpperCase().contains(SEARCH_CRITERIA_ALBUM.toUpperCase())) {
                foundAlbums.add(album);
            }
        }

        Assert.assertEquals(foundAlbums.size(), resultsAlbums.size());

        for (SlimAlbum a : foundAlbums) {

            boolean exists = false;
            for (SlimAlbum album : resultsAlbums) {
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
    public void testSearchTitles() throws SlimDatabaseException {

        List<SlimSong> resultsSongs = new ArrayList<SlimSong>(getDatabase().searchTitles(SEARCH_CRITERIA_TITLE));

        if (resultsSongs.size() < 1) {
            Assert.fail("No songs found from search.");
        }

        List<SlimSong> foundSongs = new ArrayList<SlimSong>();

        for (SlimSong song : Songs.getTestSongs()) {
            if (song.getName().toUpperCase().contains(SEARCH_CRITERIA_TITLE.toUpperCase())) {
                foundSongs.add(song);
            }
        }

        Assert.assertEquals(foundSongs.size(), resultsSongs.size());

        for (SlimSong s : foundSongs) {

            boolean exists = false;
            for (SlimSong song : resultsSongs) {
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
        List<SlimArtist> testArtists = new ArrayList<SlimArtist>(Artists.getTestArtists());

        for (SlimArtist a : testArtists) {
            boolean exists = false;

            for (SlimArtist artist : getArtistList()) {
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
        List<SlimGenre> testGenres = Genres.getTestGenres();

        for (SlimGenre g : testGenres) {

            boolean exists = false;
            for (SlimGenre genre : getGenreList()) {
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
    public void testListAlbumsForArtist() throws SlimDatabaseException {

        for (SlimArtist a : Artists.getTestArtists()) {
            List<SlimAlbum> resultsAlbums = new ArrayList<SlimAlbum>(getDatabase().listAlbumsForArtist(a));

            if (resultsAlbums.size() < 1) {
                Assert.fail("No albums found for artist.");
            }

            for (SlimAlbum alb : resultsAlbums) {
                boolean exists = false;
                for (SlimAlbum album : Artists.TEST_ARTIST_ALBUM_MAP.get(a)) {
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
    public void testListAlbumsForYear() throws SlimDatabaseException {

        for (String y : Years.getTestYears()) {
            List<SlimAlbum> resultsAlbums = new ArrayList<SlimAlbum>(getDatabase().listAlbumsForYear(y));

            if (resultsAlbums.size() < 1) {
                Assert.fail("No albums found for year.");
            }

            for (SlimAlbum alb : resultsAlbums) {
                boolean exists = false;
                for (SlimAlbum album : Years.YEAR_ALBUM_MAP.get(y)) {
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
    public void testListAlbums() throws SlimDatabaseException {

        for (SlimArtist a : Artists.getTestArtists()) {
            for (SlimGenre g : Genres.getTestGenres()) {
                for (String y : Years.getTestYears()) {
                    List<SlimAlbum> resultsAlbums = new ArrayList<SlimAlbum>(getDatabase().listAlbums(a, g, y));

                    List<SlimAlbum> foundAlbumsArtist = new ArrayList<SlimAlbum>(Artists.TEST_ARTIST_ALBUM_MAP.get(a));
                    List<SlimAlbum> foundAlbumsGenre = new ArrayList<SlimAlbum>(Genres.GENRE_ALBUM_MAP.get(g));
                    List<SlimAlbum> foundAlbumsYear = new ArrayList<SlimAlbum>(Years.YEAR_ALBUM_MAP.get(y));

                    List<SlimAlbum> foundAlbums = new ArrayList<SlimAlbum>();
                    for (SlimAlbum albumArtist : foundAlbumsArtist) {
                        for (SlimAlbum albumGenre : foundAlbumsGenre) {
                            if (albumArtist.equals(albumGenre)) {
                                for (SlimAlbum albumYear : foundAlbumsYear) {
                                    if (albumYear.equals(albumGenre)) {
                                        foundAlbums.add(albumGenre);
                                    }
                                }

                            }
                        }
                    }

                    for (SlimAlbum alb : resultsAlbums) {
                        boolean exists = false;
                        for (SlimAlbum album : foundAlbums) {
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
    public void testListArtistsForGenre() throws SlimDatabaseException {
        for (SlimGenre g : Genres.getTestGenres()) {
            List<SlimArtist> resultsArtists = new ArrayList<SlimArtist>(getDatabase().listArtistsForGenre(g));

            List<SlimArtist> foundArtists = new ArrayList<SlimArtist>();

            List<SlimAlbum> foundAlbumsGenre = new ArrayList<SlimAlbum>(Genres.GENRE_ALBUM_MAP.get(g));
            for (SlimAlbum album : foundAlbumsGenre) {
                for (SlimArtist artist : Artists.getTestArtists()) {
                    for (SlimAlbum artistAlbum : Artists.TEST_ARTIST_ALBUM_MAP.get(artist)) {
                        if (artistAlbum.getName().equals(album.getName())) {
                            foundArtists.add(artist);
                        }
                    }
                }
            }

            for (SlimArtist art : resultsArtists) {
                boolean exists = false;
                for (SlimArtist artist : foundArtists) {
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
    public void testGetArtistForAlbum() throws SlimDatabaseException {
        for (SlimAlbum album : Albums.getTestAlbums()) {
            List<SlimArtist> resultsArtists = new ArrayList<SlimArtist>(getDatabase().getArtistsForAlbum(album));

            List<SlimArtist> foundArtists = new ArrayList<SlimArtist>();
            for (SlimArtist artist : Artists.getTestArtists()) {
                for (SlimAlbum artistAlbum : Artists.TEST_ARTIST_ALBUM_MAP.get(artist)) {
                    if (artistAlbum.getName().equals(album.getName())) {
                        foundArtists.add(artist);
                    }
                }
            }

            for (SlimArtist art : resultsArtists) {
                boolean exists = false;
                for (SlimArtist artist : foundArtists) {
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
    public void testListSongsForArtist() throws SlimDatabaseException {
        for (SlimArtist artist : Artists.getTestArtists()) {
            List<SlimSong> resultsArtists = new ArrayList<SlimSong>(getDatabase().listSongsForArtist(artist));

            for (SlimSong song : resultsArtists) {
                boolean exists = false;
                for (SlimSong s : Songs.getTestSongArtistMap().get(artist)) {
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
    public void testListSongsForYear() throws SlimDatabaseException {
        for (String year : Years.getTestYears()) {
            List<SlimSong> resultsYears = new ArrayList<SlimSong>(getDatabase().listSongsForYear(year));
            List<SlimSong> foundSongs = new ArrayList<SlimSong>();

            for (SlimSong s : Songs.getTestSongs()) {
                if (s.getYear().equals(year)) {
                    foundSongs.add(s);
                }
            }

            Assert.assertEquals(resultsYears.size(), foundSongs.size());

            for (SlimSong song : resultsYears) {
                boolean exists = false;
                for (SlimSong s : foundSongs) {
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
    public void testListSongsForGenre() throws SlimDatabaseException {
        for (SlimGenre genre : Genres.getTestGenres()) {
            List<SlimSong> resultsGenres = new ArrayList<SlimSong>(getDatabase().listSongsForGenre(genre));
            List<SlimSong> foundSongs = new ArrayList<SlimSong>();

            for (SlimSong s : Songs.getTestSongs()) {
                if (s.getGenre().equals(genre)) {
                    foundSongs.add(s);
                }
            }

            Assert.assertEquals(resultsGenres.size(), foundSongs.size());

            for (SlimSong song : resultsGenres) {
                boolean exists = false;
                for (SlimSong s : foundSongs) {
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
    public void testListSongsForAlbum() throws SlimDatabaseException {
        for (SlimAlbum album : Albums.getTestAlbums()) {
            List<SlimSong> resultsGenres = new ArrayList<SlimSong>(getDatabase().listSongsForAlbum(album));
            List<SlimSong> foundSongs = new ArrayList<SlimSong>();

            for (SlimSong s : Songs.getTestSongs()) {
                if (s.getAlbum().equals(album)) {
                    foundSongs.add(s);
                }
            }

            Assert.assertEquals(resultsGenres.size(), foundSongs.size());

            for (SlimSong song : resultsGenres) {
                boolean exists = false;
                for (SlimSong s : foundSongs) {
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
        for (SlimAlbum album : getDatabase().getNewMusic()) {
            System.out.println(album.getName());
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
            SlimDatabaseProgress progress = getDatabase().getScanProgress();

            List<SlimDatabaseProgress.Importer> importers = progress.getImporters();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (SlimDatabaseProgress.Importer importer : importers) {
//                System.out.println(progress.getFullName());
//                System.out.println("\t" + importer.getImporter() + " \t " + importer.getPercentage());
            }
        }
    }

    private void compareItems(SlimAlbum album1, SlimAlbum album2) {
        Assert.assertEquals(album1.getId(), album2.getId());
        Assert.assertEquals(album1.getName(), album2.getName());
        Assert.assertEquals(album1.isCompilation(), album2.isCompilation());
    }

    private void compareItems(SlimSong item1, SlimPlayableItem item2) {
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
    public static List<SlimArtist> getArtistList() {
        return artistList;
    }

    /**
     * @param artists the artistList to set
     */
    public static void setArtistList(List<SlimArtist> artists) {
        artistList = artists;
    }

    /**
     * @return the album list
     */
    public static List<SlimAlbum> getAlbumList() {
        return albumList;
    }

    /**
     * @param albums the album list to set
     */
    public static void setAlbumList(List<SlimAlbum> albums) {
        albumList = albums;
    }

    /**
     * @return the genreList
     */
    public static List<SlimGenre> getGenreList() {
        return genreList;
    }

    /**
     * @param genres the genreList to set
     */
    public static void setGenreList(List<SlimGenre> genres) {
        genreList = genres;
    }

    /**
     * @return the songList
     */
    public static List<SlimPlayableItem> getSongList() {
        return songList;
    }

    /**
     * @param songs the songList to set
     */
    public static void setSongList(List<SlimPlayableItem> songs) {
        songList = songs;
    }
}