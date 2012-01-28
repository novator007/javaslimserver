/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

import org.bff.squeezeserver.domain.Artist;
import org.bff.squeezeserver.events.FavoriteChangeEvent;
import org.bff.squeezeserver.events.FavoriteChangeListener;
import org.junit.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bfindeisen
 */
public class SlimFavoriteEventListenerTest extends Base {
    private String TEST_ARTIST = "Artist0";

    public SlimFavoriteEventListenerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    boolean success = false;

    @Test
    public void testFavoriteAdded() throws Exception {
        FavoriteChangeListener listener = new FavoriteChangeListener() {

            @Override
            public void favoritesChanged(FavoriteChangeEvent event) {
                switch (event.getId()) {
                    case FavoriteChangeEvent.FAVORITE_ADDED:
                        success = true;
                        break;
                }
            }
        };

        getFavoritePlugin().addFavoriteChangeListener(listener);

        getFavoritePlugin().addFavorite(new ArrayList<Artist>(getDatabase().searchArtists(TEST_ARTIST)).get(0));

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
