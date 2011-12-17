/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.junit.*;

/**
 * @author bfindeisen
 */
public class SlimServerTest {

    private static SlimServer slimServer;

    public SlimServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        setSlimServer(Controller.getInstance().getSlimServer());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testVersion() {
        Assert.assertEquals(Controller.getVersion(), getSlimServer().getVersion());
    }

    @Test
    public void testEncoding() {
        Assert.assertEquals(SlimServer.getEncoding(), SlimConstants.DEFAULT_ENCODING);
    }

    /**
     * @return the slimServer
     */
    public static SlimServer getSlimServer() {
        return slimServer;
    }

    /**
     * @param aSlimServer the slimServer to set
     */
    public static void setSlimServer(SlimServer aSlimServer) {
        slimServer = aSlimServer;
    }
}
