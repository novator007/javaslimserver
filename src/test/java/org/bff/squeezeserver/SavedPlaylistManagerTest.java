/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

import org.bff.squeezeserver.domain.SavedPlaylist;
import org.bff.squeezeserver.exception.DatabaseException;
import org.bff.squeezeserver.exception.SqueezeException;
import org.junit.*;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;

/**
 * @author bfindeisen
 */
@Category(IntegrationTest.class)
public class SavedPlaylistManagerTest extends BaseTest {

    public SavedPlaylistManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws SqueezeException {
        deleteSavedPlaylists();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSavedPlaylistRename() throws SqueezeException {
        SavedPlaylist pl = getSavedPlaylistManager().createEmptyPlaylist("Temp");
        getSavedPlaylistManager().renamePlaylist(pl, "Temp1");

        SavedPlaylist ssp = new ArrayList<SavedPlaylist>(getSavedPlaylistManager().getPlaylists()).get(0);

        Assert.assertEquals(ssp.getName(), "Temp1");

    }

    @Test(expected = DatabaseException.class)
    public void testSavedPlaylistRenameDup() throws SqueezeException {
        SavedPlaylist pl = getSavedPlaylistManager().createEmptyPlaylist("Temp");
        getSavedPlaylistManager().renamePlaylist(pl, "Temp");
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    private static void deleteSavedPlaylists() throws SqueezeException {
        for (SavedPlaylist pl : getSavedPlaylistManager().getPlaylists()) {
            getSavedPlaylistManager().deleteSavedPlaylist(pl);
        }
    }

}
