/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.exception.SlimException;
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
    public void testGetCurrentItem() throws SlimException {
        getPlaylist().clear();

        Assert.assertTrue(getPlaylist().getCurrentItem() == null);
    }

// TODO add test methods here.
// The methods must be annotated with annotation @Test. For example:
//
// @Test
// public void hello() {}
}
