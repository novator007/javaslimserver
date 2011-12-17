/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.events.FavoriteChangeEvent;
import org.bff.slimserver.events.FavoriteChangeListener;
import org.bff.slimserver.exception.SlimDatabaseException;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.musicobjects.SlimArtist;
import org.bff.slimserver.musicobjects.SlimSong;
import org.bff.slimserver.musicobjects.favorite.SlimFavorite;
import org.bff.slimserver.musicobjects.favorite.SlimFavoriteAudioDetails;
import org.bff.slimserver.musicobjects.favorite.SlimFavoriteItem;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author bfindeisen
 */
public class SlimFavoritePluginTest extends Base {

    private static SlimArtist testArtist;
    private static SlimArtist testArtist2;
    private static SlimSong testSong;
    private String FAV_COMMAND = "favorites";

    public SlimFavoritePluginTest() throws SlimDatabaseException {
        if (testArtist == null) {
            testArtist = new ArrayList<SlimArtist>(getDatabase().getArtists()).get(0);
            testArtist2 = new ArrayList<SlimArtist>(getDatabase().getArtists()).get(1);
            testSong = new ArrayList<SlimSong>(getDatabase().listSongsForArtist(testArtist)).get(0);
        }
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws SlimException {
        getFavoritePlugin().clearFavorites();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addFavoriteChangeListener method, of class SlimFavoritePlugin.
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
        SlimFavoritePlugin instance = getFavoritePlugin();
        instance.addFavoriteChangeListener(listener);
    }

    /**
     * Test of removeFavoriteChangeListener method, of class SlimFavoritePlugin.
     */
    @Test
    public void testRemoveFavoriteChangeListener() {
        FavoriteChangeListener listener = new FavoriteChangeListener() {

            @Override
            public void favoritesChanged(FavoriteChangeEvent event) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        SlimFavoritePlugin instance = getFavoritePlugin();
        instance.addFavoriteChangeListener(listener);
        instance.removeFavoriteChangeListener(listener);
    }

    /**
     * Test of isFavorite method, of class SlimFavoritePlugin.
     */
    @Test
    public void testIsFavorite() throws Exception {
        getFavoritePlugin().addFavorite(getTestArtist());

        Assert.assertTrue(getFavoritePlugin().isFavorite(getTestArtist().getUrl()));
    }

    @Test
    public void testClearFavorites() throws Exception {

        List<SlimArtist> artists = new ArrayList<SlimArtist>(getDatabase().getArtists());
        for (SlimArtist artist : artists) {
            getFavoritePlugin().addFavorite(artist);
        }

        if (getFavoritePlugin().getCount() != artists.size()) {
            fail("Problem adding items to favorites");
        }

        getFavoritePlugin().clearFavorites();

        Assert.assertTrue(getFavoritePlugin().getFavorites().isEmpty());
    }

    /**
     * Test of getFavoriteAudioDetails method, of class SlimFavoritePlugin.
     */
    @Test
    public void testGetFavoriteAudioDetails() throws Exception {
        //System.out.println("getFavoriteAudioDetails");

        getFavoritePlugin().addFavorite(getTestSong());
        SlimFavoriteItem favorite = new ArrayList<SlimFavoriteItem>(getFavoritePlugin().getFavorites()).get(0);
        SlimFavoriteAudioDetails details = getFavoritePlugin().getFavoriteAudioDetails(favorite);

        assertEquals(details.isAudio(), true);
        assertEquals(details.getTitle(), getTestSong().getTitle());
        assertEquals(details.getUrl(), getTestSong().getUrl());
    }

    /**
     * Test of getFavorites method, of class SlimFavoritePlugin.
     */
    @Test
    public void testGetFavorites_0args() throws Exception {
        //System.out.println("getFavorites");
        List<SlimArtist> artists = new ArrayList<SlimArtist>(getDatabase().getArtists());
        for (SlimArtist artist : artists) {
            getFavoritePlugin().addFavorite(artist);
        }

        boolean success = false;
        for (SlimFavorite fav : getFavoritePlugin().getFavorites()) {
            for (SlimArtist artist : artists) {
                if (fav.getName().equals(artist.getName())) {
                    success = true;
                    break;
                }
            }
            assertTrue(success);
        }
    }

    /**
     * Test of getFavorites method, of class SlimFavoritePlugin.

     @Test public void testGetFavorites_SlimFavorite() throws Exception {
     //System.out.println("getFavorites");
     String testFolder = "TestFolder";

     getFavoritePlugin().addFolder(testFolder);

     SlimFavorite favorite = null;
     SlimFavoritePlugin instance = null;
     Collection expResult = null;
     Collection result = instance.getFavorites(favorite);
     assertEquals(expResult, result);
     // TODO review the generated test code and remove the default call to fail.
     fail("The test case is a prototype.");
     }
     */
    /**
     * Test of getCount method, of class SlimFavoritePlugin.
     */
    @Test
    public void testGetFavoritesCount() throws Exception {
        //System.out.println("getFavoritesCount");

        List<SlimArtist> artists = new ArrayList<SlimArtist>(getDatabase().getArtists());
        for (SlimArtist artist : artists) {
            getFavoritePlugin().addFavorite(artist);
        }

        assertEquals(getFavoritePlugin().getCount(), artists.size());
    }

    /**
     * Test of getCommand method, of class SlimFavoritePlugin.
     */
    @Test
    public void testGetCommand() {
        //System.out.println("getCommand");
        assertEquals(getFavoritePlugin().getCommand(), FAV_COMMAND);
    }

    /**
     * Test of addFolder method, of class SlimFavoritePlugin.
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
    SlimFavoriteItem folder = new ArrayList<SlimFavoriteItem>(getFavoritePlugin().getFavorites()).get(0);
    getFavoritePlugin().addFavorite(getTestArtist2());
    getFavoritePlugin().addFavoriteToFolder(getTestArtist(), folder);
    getFavoritePlugin().addFavoriteToFolder(getTestArtist2(), folder);

    getFavoritePlugin().loadFavoriteItem(folder);

    Assert.assertTrue(folder.getXmlItems().size() == 2);
    }    

    
    @Test
    public void testLoadFavoriteItem() throws Exception {
    String testFolder = "TestFolder";

    getFavoritePlugin().addFolder(testFolder);
    SlimFavoriteItem folder = new ArrayList<SlimFavoriteItem>(getFavoritePlugin().getFavorites()).get(0);
    getFavoritePlugin().addFavoriteToFolder(getTestArtist(), folder);
    getFavoritePlugin().addFavoriteToFolder(getTestArtist2(), folder);

    getFavoritePlugin().loadFavoriteItem(folder);

    Assert.assertTrue(folder.getXmlItems().size() == 2);
    }
     */

    /**
     * Test of addFavorite method, of class SlimFavoritePlugin.
     */
    @Test
    public void testAddFavorite_Title_Url() throws Exception {
        //System.out.println("addFavorite");
        String title = "TestTitle";
        String url = getTestArtist().getUrl();
        getFavoritePlugin().addFavorite(title, url);

        Assert.assertTrue(checkFavExists(title));
    }

    /**
     * Test of addFavorite method, of class SlimFavoritePlugin.
     */
    @Test
    public void testAddFavorite_SlimPlayableObject() throws Exception {
        getFavoritePlugin().addFavorite(getTestArtist());

        Assert.assertTrue(checkFavExists(getTestArtist().getName()));
    }

    /**
     * Test of deleteFavorite method, of class SlimFavoritePlugin.
     */
    @Test
    public void testDeleteFavorite() throws Exception {
        //System.out.println("deleteFavorite");
        getFavoritePlugin().addFavorite(testArtist);
        assertEquals(1, getFavoritePlugin().getCount());
        getFavoritePlugin().deleteFavorite(new ArrayList<SlimFavorite>(getFavoritePlugin().getFavorites()).get(0));
        assertEquals(0, getFavoritePlugin().getCount());
    }

    /**
     * Test of renameFavorite method, of class SlimFavoritePlugin.
     */
    @Test
    public void testRenameFavorite() throws Exception {
        //System.out.println("renameFavorite");
        String newName = "NEW_NAME";
        getFavoritePlugin().addFavorite(testArtist);
        getFavoritePlugin().renameFavorite(new ArrayList<SlimFavorite>(getFavoritePlugin().getFavorites()).get(0), newName);
        assertEquals(new ArrayList<SlimFavorite>(getFavoritePlugin().getFavorites()).get(0).getName(), newName);
    }

    /**
     * Test of moveFavorite method, of class SlimFavoritePlugin.
     */
    @Test
    public void testMoveFavorite() throws Exception {
        //System.out.println("moveFavorite");
        List<SlimArtist> artists = new ArrayList<SlimArtist>(getDatabase().getArtists());
        for (SlimArtist artist : artists) {
            getFavoritePlugin().addFavorite(artist);
        }

        List<SlimFavorite> favorites = new ArrayList<SlimFavorite>(getFavoritePlugin().getFavorites());
        SlimFavorite fromFavorite = favorites.get(0);
        SlimFavorite toFavorite = favorites.get(1);

        getFavoritePlugin().moveFavorite(fromFavorite, toFavorite);

        List<SlimFavorite> favorites2 = new ArrayList<SlimFavorite>(getFavoritePlugin().getFavorites());
        assertEquals(toFavorite.getName(), favorites2.get(0).getName());
        assertEquals(fromFavorite.getName(), favorites2.get(1).getName());
    }

    private boolean checkFavExists(String name) throws SlimException {
        List<SlimFavorite> favs = new ArrayList<SlimFavorite>(getFavoritePlugin().getFavorites());

        for (SlimFavorite fav : favs) {
            if (fav.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return the testArtist
     */
    public SlimArtist getTestArtist() {
        return testArtist;
    }

    /**
     * @param testArtist the testArtist to set
     */
    public void setTestArtist(SlimArtist testArtist) {
        this.testArtist = testArtist;
    }

    /**
     * @return the testArtist2
     */
    public SlimArtist getTestArtist2() {
        return testArtist2;
    }

    /**
     * @return the testSong
     */
    public SlimSong getTestSong() {
        return testSong;
    }

    /**
     * @param testSong the testSong to set
     */
    public void setTestSong(SlimSong testSong) {
        this.testSong = testSong;
    }
}
