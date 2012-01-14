/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.exception.ConnectionException;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bfindeisen
 */
public class SqueezeServerTest {

    private static SqueezeServer squeezeServer;

    public SqueezeServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        setSqueezeServer(Controller.getInstance().getSqueezeServer());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testVersion() {
        Assert.assertEquals(Controller.getVersion(), getSqueezeServer().getVersion());
    }

    @Test
    public void testEncoding() {
        Assert.assertEquals(SqueezeServer.getEncoding(), Constants.DEFAULT_ENCODING);
    }

    @Test
    public void testGetAudioDirectory1() throws ConnectionException {
        List<String> mediaList = new ArrayList<String>(getSqueezeServer().getMediaDirectories());
        Assert.assertTrue(mediaList.contains(Controller.getInstance().getMp3Path()));
    }

    @Test
    public void testGetAudioDirectory2() throws ConnectionException {
        List<String> mediaList = new ArrayList<String>(getSqueezeServer().getMediaDirectories());
        Assert.assertTrue(mediaList.contains(Controller.getInstance().getMp3Path2()));
    }

    /**
     * @return the squeezeServer
     */
    public static SqueezeServer getSqueezeServer() {
        return squeezeServer;
    }

    /**
     * @param aSqueezeServer the squeezeServer to set
     */
    public static void setSqueezeServer(SqueezeServer aSqueezeServer) {
        squeezeServer = aSqueezeServer;
    }
}
