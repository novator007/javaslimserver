/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.exception.SlimDatabaseException;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.musicobjects.SlimSavedPlaylist;
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
    public void setUp() throws SlimException {
        deleteSavedPlaylists();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSavedPlaylistRename() throws SlimException {
        SlimSavedPlaylist pl = getSavedPlaylistManager().createEmptyPlaylist("Temp");
        getSavedPlaylistManager().renamePlaylist(pl, "Temp1");

        SlimSavedPlaylist ssp = new ArrayList<SlimSavedPlaylist>(getSavedPlaylistManager().getPlaylists()).get(0);

        Assert.assertEquals(ssp.getName(), "Temp1");

    }

    @Test(expected = SlimDatabaseException.class)
    public void testSavedPlaylistRenameDup() throws SlimException {
        SlimSavedPlaylist pl = getSavedPlaylistManager().createEmptyPlaylist("Temp");
        getSavedPlaylistManager().renamePlaylist(pl, "Temp");
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    private static void deleteSavedPlaylists() throws SlimException {
        for (SlimSavedPlaylist pl : getSavedPlaylistManager().getPlaylists()) {
            getSavedPlaylistManager().deleteSavedPlaylist(pl);
        }
    }

}
