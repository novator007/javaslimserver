/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.events.FavoriteChangeEvent;
import org.bff.slimserver.events.FavoriteChangeListener;
import org.bff.slimserver.exception.DatabaseException;
import org.bff.slimserver.exception.SqueezeException;
import org.bff.slimserver.domain.Artist;
import org.bff.slimserver.domain.Song;
import org.bff.slimserver.domain.favorite.Favorite;
import org.bff.slimserver.domain.favorite.FavoriteAudioDetails;
import org.bff.slimserver.domain.favorite.FavoriteItem;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author bfindeisen
 */
public class FavoritePluginTest extends Base {

    private static Artist testArtist;
    private static Artist testArtist2;
    private static Song testSong;
    private String FAV_COMMAND = "favorites";

    public FavoritePluginTest() throws DatabaseException {
        if (testArtist == null) {
            testArtist = new ArrayList<Artist>(getDATABASE().getArtists()).get(0);
            testArtist2 = new ArrayList<Artist>(getDATABASE().getArtists()).get(1);
            testSong = new ArrayList<Song>(getDATABASE().listSongsForArtist(testArtist)).get(0);
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
        getFAVORITE_PLUGIN().clearFavorites();
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
        FavoritePlugin instance = getFAVORITE_PLUGIN();
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
        FavoritePlugin instance = getFAVORITE_PLUGIN();
        instance.addFavoriteChangeListener(listener);
        instance.removeFavoriteChangeListener(listener);
    }

    /**
     * Test of isFavorite method, of class FavoritePlugin.
     */
    @Test
    public void testIsFavorite() throws Exception {
        getFAVORITE_PLUGIN().addFavorite(getTestArtist());

        Assert.assertTrue(getFAVORITE_PLUGIN().isFavorite(getTestArtist().getUrl()));
    }

    @Test
    public void testClearFavorites() throws Exception {

        List<Artist> artists = new ArrayList<Artist>(getDATABASE().getArtists());
        for (Artist artist : artists) {
            getFAVORITE_PLUGIN().addFavorite(artist);
        }

        if (getFAVORITE_PLUGIN().getCount() != artists.size()) {
            fail("Problem adding items to favorites");
        }

        getFAVORITE_PLUGIN().clearFavorites();

        Assert.assertTrue(getFAVORITE_PLUGIN().getFavorites().isEmpty());
    }

    /**
     * Test of getFavoriteAudioDetails method, of class FavoritePlugin.
     */
    @Test
    public void testGetFavoriteAudioDetails() throws Exception {
        //System.out.println("getFavoriteAudioDetails");

        getFAVORITE_PLUGIN().addFavorite(getTestSong());
        FavoriteItem favorite = new ArrayList<FavoriteItem>(getFAVORITE_PLUGIN().getFavorites()).get(0);
        FavoriteAudioDetails details = getFAVORITE_PLUGIN().getFavoriteAudioDetails(favorite);

        assertEquals(details.isAudio(), true);
        assertEquals(details.getTitle(), getTestSong().getTitle());
        assertEquals(details.getUrl(), getTestSong().getUrl());
    }

    /**
     * Test of getFavorites method, of class FavoritePlugin.
     */
    @Test
    public void testGetFavorites_0args() throws Exception {
        //System.out.println("getFavorites");
        List<Artist> artists = new ArrayList<Artist>(getDATABASE().getArtists());
        for (Artist artist : artists) {
            getFAVORITE_PLUGIN().addFavorite(artist);
        }

        boolean success = false;
        for (Favorite fav : getFAVORITE_PLUGIN().getFavorites()) {
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
     * Test of getFavorites method, of class FavoritePlugin.

     @Test public void testGetFavorites_SlimFavorite() throws Exception {
     //System.out.println("getFavorites");
     String testFolder = "TestFolder";

     getFAVORITE_PLUGIN().addFolder(testFolder);

     Favorite favorite = null;
     FavoritePlugin instance = null;
     Collection expResult = null;
     Collection result = instance.getFavorites(favorite);
     assertEquals(expResult, result);
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }
     */
    /**
     * Test of getCount method, of class FavoritePlugin.
     */
    @Test
    public void testGetFavoritesCount() throws Exception {
        //System.out.println("getFavoritesCount");

        List<Artist> artists = new ArrayList<Artist>(getDATABASE().getArtists());
        for (Artist artist : artists) {
            getFAVORITE_PLUGIN().addFavorite(artist);
        }

        assertEquals(getFAVORITE_PLUGIN().getCount(), artists.size());
    }

    /**
     * Test of getCommand method, of class FavoritePlugin.
     */
    @Test
    public void testGetCommand() {
        //System.out.println("getCommand");
        assertEquals(getFAVORITE_PLUGIN().getCommand(), FAV_COMMAND);
    }

    /**
     * Test of addFolder method, of class FavoritePlugin.
     */
    @Test
    public void testAddFolder() throws Exception {
        String testFolder = "TestFolder";
        getFAVORITE_PLUGIN().addFolder(testFolder);
        Assert.assertTrue(checkFavExists(testFolder));
    }

    /*
    @Test
    public void testAddFavoriteToFolder() throws Exception {
    String testFolder = "TestFolder";

    getFAVORITE_PLUGIN().addFolder(testFolder);
    FavoriteItem folder = new ArrayList<FavoriteItem>(getFAVORITE_PLUGIN().getFavorites()).get(0);
    getFAVORITE_PLUGIN().addFavorite(getTestArtist2());
    getFAVORITE_PLUGIN().addFavoriteToFolder(getTestArtist(), folder);
    getFAVORITE_PLUGIN().addFavoriteToFolder(getTestArtist2(), folder);

    getFAVORITE_PLUGIN().loadFavoriteItem(folder);

    Assert.assertTrue(folder.getXmlItems().size() == 2);
    }    

    
    @Test
    public void testLoadFavoriteItem() throws Exception {
    String testFolder = "TestFolder";

    getFAVORITE_PLUGIN().addFolder(testFolder);
    FavoriteItem folder = new ArrayList<FavoriteItem>(getFAVORITE_PLUGIN().getFavorites()).get(0);
    getFAVORITE_PLUGIN().addFavoriteToFolder(getTestArtist(), folder);
    getFAVORITE_PLUGIN().addFavoriteToFolder(getTestArtist2(), folder);

    getFAVORITE_PLUGIN().loadFavoriteItem(folder);

    Assert.assertTrue(folder.getXmlItems().size() == 2);
    }
     */

    /**
     * Test of addFavorite method, of class FavoritePlugin.
     */
    @Test
    public void testAddFavorite_Title_Url() throws Exception {
        //System.out.println("addFavorite");
        String title = "TestTitle";
        String url = getTestArtist().getUrl();
        getFAVORITE_PLUGIN().addFavorite(title, url);

        Assert.assertTrue(checkFavExists(title));
    }

    /**
     * Test of addFavorite method, of class FavoritePlugin.
     */
    @Test
    public void testAddFavorite_SlimPlayableObject() throws Exception {
        getFAVORITE_PLUGIN().addFavorite(getTestArtist());

        Assert.assertTrue(checkFavExists(getTestArtist().getName()));
    }

    /**
     * Test of deleteFavorite method, of class FavoritePlugin.
     */
    @Test
    public void testDeleteFavorite() throws Exception {
        //System.out.println("deleteFavorite");
        getFAVORITE_PLUGIN().addFavorite(testArtist);
        assertEquals(1, getFAVORITE_PLUGIN().getCount());
        getFAVORITE_PLUGIN().deleteFavorite(new ArrayList<Favorite>(getFAVORITE_PLUGIN().getFavorites()).get(0));
        assertEquals(0, getFAVORITE_PLUGIN().getCount());
    }

    /**
     * Test of renameFavorite method, of class FavoritePlugin.
     */
    @Test
    public void testRenameFavorite() throws Exception {
        //System.out.println("renameFavorite");
        String newName = "NEW_NAME";
        getFAVORITE_PLUGIN().addFavorite(testArtist);
        getFAVORITE_PLUGIN().renameFavorite(new ArrayList<Favorite>(getFAVORITE_PLUGIN().getFavorites()).get(0), newName);
        assertEquals(new ArrayList<Favorite>(getFAVORITE_PLUGIN().getFavorites()).get(0).getName(), newName);
    }

    /**
     * Test of moveFavorite method, of class FavoritePlugin.
     */
    @Test
    public void testMoveFavorite() throws Exception {
        //System.out.println("moveFavorite");
        List<Artist> artists = new ArrayList<Artist>(getDATABASE().getArtists());
        for (Artist artist : artists) {
            getFAVORITE_PLUGIN().addFavorite(artist);
        }

        List<Favorite> favorites = new ArrayList<Favorite>(getFAVORITE_PLUGIN().getFavorites());
        Favorite fromFavorite = favorites.get(0);
        Favorite toFavorite = favorites.get(1);

        getFAVORITE_PLUGIN().moveFavorite(fromFavorite, toFavorite);

        List<Favorite> favorites2 = new ArrayList<Favorite>(getFAVORITE_PLUGIN().getFavorites());
        assertEquals(toFavorite.getName(), favorites2.get(0).getName());
        assertEquals(fromFavorite.getName(), favorites2.get(1).getName());
    }

    private boolean checkFavExists(String name) throws SqueezeException {
        List<Favorite> favs = new ArrayList<Favorite>(getFAVORITE_PLUGIN().getFavorites());

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
