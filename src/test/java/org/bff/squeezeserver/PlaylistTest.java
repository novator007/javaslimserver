/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

import org.bff.squeezeserver.exception.SqueezeException;
import org.junit.*;

/**
 * @author bfindeisen
 */
public class PlaylistTest extends Base {

    public PlaylistTest() {
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

    @Test
    public void testGetCurrentItem() throws SqueezeException {
        getPlaylist().clear();

        Assert.assertTrue(getPlaylist().getCurrentItem() == null);
    }
}
