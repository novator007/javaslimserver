/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.domain.SavedPlaylist;
import org.bff.slimserver.exception.DatabaseException;
import org.bff.slimserver.exception.SqueezeException;
import org.junit.*;

import java.util.ArrayList;

/**
 * @author bfindeisen
 */
public class SavedPlaylistManagerTest extends Base {

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
