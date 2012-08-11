/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

import org.bff.squeezeserver.domain.Artist;
import org.bff.squeezeserver.domain.Song;
import org.bff.squeezeserver.domain.favorite.Favorite;
import org.bff.squeezeserver.domain.favorite.FavoriteAudioDetails;
import org.bff.squeezeserver.events.FavoriteChangeEvent;
import org.bff.squeezeserver.events.FavoriteChangeListener;
import org.bff.squeezeserver.exception.DatabaseException;
import org.bff.squeezeserver.exception.SqueezeException;
import org.junit.*;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author bfindeisen
 */
@Category(IntegrationTest.class)
public class FavoritePluginTest extends BaseTest {

    private static Artist testArtist;
    private static Artist testArtist2;
    private static Song testSong;
    private String FAV_COMMAND = "favorites";

    public FavoritePluginTest() throws DatabaseException {
        if (testArtist == null) {
            testArtist = new ArrayList<Artist>(getDatabase().getArtists()).get(0);
            testArtist2 = new ArrayList<Artist>(getDatabase().getArtists()).get(1);
            testSong = new ArrayList<Song>(getDatabase().listSongsForArtist(testArtist)).get(0);
        }
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws SqueezeException {
        getFavoritePlugin().clearFavorites();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addFavoriteChangeListener method, of class FavoritePlugin.
     */
    @Test
    public void testAddFavoriteChangeListener() {
        //System.out.println("addFavoriteChangeListener");
        FavoriteChangeListener listener = new FavoriteChangeListener() {

            @Override
            public void favoritesChanged(FavoriteChangeEvent event) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        FavoritePlugin instance = getFavoritePlugin();
        instance.addFavoriteChangeListener(listener);
    }

    /**
     * Test of removeFavoriteChangeListener method, of class FavoritePlugin.
     */
    @Test
    public void testRemoveFavoriteChangeListener() {
        FavoriteChangeListener listener = new FavoriteChangeListener() {

            @Override
            public void favoritesChanged(FavoriteChangeEvent event) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        FavoritePlugin instance = getFavoritePlugin();
        instance.addFavoriteChangeListener(listener);
        instance.removeFavoriteChangeListener(listener);
    }

    /**
     * Test of isFavorite method, of class FavoritePlugin.
     */
    @Test
    public void testIsFavorite() throws Exception {
        getFavoritePlugin().addFavorite(getTestArtist());

        Assert.assertTrue(getFavoritePlugin().isFavorite(getTestArtist().getUrl()));
    }

    @Test
    public void testClearFavorites() throws Exception {

        List<Artist> artists = new ArrayList<Artist>(getDatabase().getArtists());
        for (Artist artist : artists) {
            getFavoritePlugin().addFavorite(artist);
        }

        if (getFavoritePlugin().getCount() != artists.size()) {
            fail("Problem adding items to favorites");
        }

        getFavoritePlugin().clearFavorites();

        Assert.assertTrue(getFavoritePlugin().getFavorites().isEmpty());
    }

    /**
     * Test of getFavoriteAudioDetails method, of class FavoritePlugin.
     */
    @Test
    public void testGetFavoriteAudioDetails() throws Exception {
        getFavoritePlugin().addFavorite(getTestSong());
        Favorite favorite = new ArrayList<Favorite>(getFavoritePlugin().getFavorites()).get(0);
        FavoriteAudioDetails details = getFavoritePlugin().getFavoriteAudioDetails(favorite);

        assertEquals(details.isAudio(), true);
        assertEquals(details.getTitle(), getTestSong().getTitle());
        assertEquals(details.getUrl(), getTestSong().getUrl());
    }

    /**
     * Test of getFavorites method, of class FavoritePlugin.
     */
    @Test
    public void testGetFavorites_0args() throws Exception {
        List<Artist> artists = new ArrayList<Artist>(getDatabase().getArtists());
        for (Artist artist : artists) {
            getFavoritePlugin().addFavorite(artist);
        }

        boolean success = false;
        for (Favorite fav : getFavoritePlugin().getFavorites()) {
            for (Artist artist : artists) {
                if (fav.getName().equals(artist.getName())) {
                    success = true;
                    break;
                }
            }
            assertTrue(success);
        }
    }

    /**
     * Test of getCount method, of class FavoritePlugin.
     */
    @Test
    public void testGetFavoritesCount() throws Exception {
        List<Artist> artists = new ArrayList<Artist>(getDatabase().getArtists());
        for (Artist artist : artists) {
            getFavoritePlugin().addFavorite(artist);
        }

        assertEquals(getFavoritePlugin().getCount(), artists.size());
    }

    /**
     * Test of getCommand method, of class FavoritePlugin.
     */
    @Test
    public void testGetCommand() {
        assertEquals(getFavoritePlugin().getCommand(), FAV_COMMAND);
    }

    /**
     * Test of addFolder method, of class FavoritePlugin.
     */
    @Test
    public void testAddFolder() throws Exception {
        String testFolder = "TestFolder";
        getFavoritePlugin().addFolder(testFolder);
        Assert.assertTrue(checkFavExists(testFolder));
    }

    /*
    @Test
    public void testAddFavoriteToFolder() throws Exception {
    String testFolder = "TestFolder";

    getFavoritePlugin().addFolder(testFolder);
    Favorite folder = new ArrayList<Favorite>(getFavoritePlugin().getFavorites()).get(0);
    getFavoritePlugin().addFavorite(getTestArtist2());
    getFavoritePlugin().addFavoriteToFolder(getTestArtist(), folder);
    getFavoritePlugin().addFavoriteToFolder(getTestArtist2(), folder);

    getFavoritePlugin().loadFavorite(folder);


    Assert.assertTrue(folder.getXmlItems().size() == 2);
    }    

    
    @Test
    public void testLoadFavorite() throws Exception {
    String testFolder = "TestFolder";

    getFavoritePlugin().addFolder(testFolder);
    Favorite folder = new ArrayList<Favorite>(getFavoritePlugin().getFavorites()).get(0);
    getFavoritePlugin().addFavoriteToFolder(getTestArtist(), folder);
    getFavoritePlugin().addFavoriteToFolder(getTestArtist2(), folder);

    getFavoritePlugin().loadFavorite(folder);

    Assert.assertTrue(folder.getXmlItems().size() == 2);
    }
     */

    /**
     * Test of addFavorite method, of class FavoritePlugin.
     */
    @Test
    public void testAddFavorite_Title_Url() throws Exception {
        String title = "TestTitle";
        String url = getTestArtist().getUrl();
        getFavoritePlugin().addFavorite(title, url);

        Assert.assertTrue(checkFavExists(title));
    }

    /**
     * Test of addFavorite method, of class FavoritePlugin.
     */
    @Test
    public void testAddFavorite_SlimPlayableObject() throws Exception {
        getFavoritePlugin().addFavorite(getTestArtist());

        Assert.assertTrue(checkFavExists(getTestArtist().getName()));
    }

    /**
     * Test of deleteFavorite method, of class FavoritePlugin.
     */
    @Test
    public void testDeleteFavorite() throws Exception {
        //System.out.println("deleteFavorite");
        getFavoritePlugin().addFavorite(testArtist);
        assertEquals(1, getFavoritePlugin().getCount());
        getFavoritePlugin().deleteFavorite(new ArrayList<Favorite>(getFavoritePlugin().getFavorites()).get(0));
        assertEquals(0, getFavoritePlugin().getCount());
    }

    /**
     * Test of renameFavorite method, of class FavoritePlugin.
     */
    @Test
    public void testRenameFavorite() throws Exception {
        //System.out.println("renameFavorite");
        String newName = "NEW_NAME";
        getFavoritePlugin().addFavorite(testArtist);
        getFavoritePlugin().renameFavorite(new ArrayList<Favorite>(getFavoritePlugin().getFavorites()).get(0), newName);
        assertEquals(new ArrayList<Favorite>(getFavoritePlugin().getFavorites()).get(0).getName(), newName);
    }

    /**
     * Test of moveFavorite method, of class FavoritePlugin.
     */
    @Test
    public void testMoveFavorite() throws Exception {
        List<Artist> artists = new ArrayList<Artist>(getDatabase().getArtists());
        for (Artist artist : artists) {
            getFavoritePlugin().addFavorite(artist);
        }

        List<Favorite> favorites = new ArrayList<Favorite>(getFavoritePlugin().getFavorites());
        Favorite fromFavorite = favorites.get(0);
        Favorite toFavorite = favorites.get(1);

        getFavoritePlugin().moveFavorite(fromFavorite, toFavorite);

        List<Favorite> favorites2 = new ArrayList<Favorite>(getFavoritePlugin().getFavorites());
        assertEquals(toFavorite.getName(), favorites2.get(0).getName());
        assertEquals(fromFavorite.getName(), favorites2.get(1).getName());
    }

    private boolean checkFavExists(String name) throws SqueezeException {
        List<Favorite> favs = new ArrayList<Favorite>(getFavoritePlugin().getFavorites());

        for (Favorite fav : favs) {
            if (fav.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return the testArtist
     */
    public Artist getTestArtist() {
        return testArtist;
    }

    /**
     * @param testArtist the testArtist to set
     */
    public void setTestArtist(Artist testArtist) {
        this.testArtist = testArtist;
    }

    /**
     * @return the testArtist2
     */
    public Artist getTestArtist2() {
        return testArtist2;
    }

    /**
     * @return the testSong
     */
    public Song getTestSong() {
        return testSong;
    }

    /**
     * @param testSong the testSong to set
     */
    public void setTestSong(Song testSong) {
        this.testSong = testSong;
    }
}
