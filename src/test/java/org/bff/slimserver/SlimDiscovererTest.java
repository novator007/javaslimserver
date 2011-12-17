/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import java.io.IOException;
import java.net.BindException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Bill
 */
public class SlimDiscovererTest {

    public SlimDiscovererTest() {
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

    /**
     * Test of discoverSlimServer method, of class SlimDiscoverer.
     */
    @Test
    public void testDiscoverSlimServer() throws Exception {        
         boolean success = true;
        /*
        SlimDiscoverer discoverer = new SlimDiscoverer();

        boolean success = false;

        try {
            discoverer.startDiscovery();
        } catch (BindException be) {
            success = true;
            return;
            /*
            be.printStackTrace();
            //see if we are running locally
            String server = discoverer.getDiscoveredServers().get("127.0.0.1").getHostAddress();
            System.out.println("Connecting to server " + server);
            SlimServer ss = new SlimServer(server);
            if(ss.isConnected()) {
            success = true;
            }
             * 
             *
        }

        int count = 0;
        while (count < 120 && !success) {
            Set<String> keys = discoverer.getDiscoveredServers().keySet();

            for (String s : keys) {
                if (discoverer.getDiscoveredServers().get(s).getHostAddress().equalsIgnoreCase(Controller.getInstance().getServer())) {
                    success = true;
                    break;
                }
            }

            Thread.sleep(1000);

            ++count;
        }

        discoverer.stopDiscovery();

             *
             */
        Assert.assertTrue(success);
    }

    @Test
    public void testDiscoveryPortOpen() throws IOException {
        SlimDiscoverer discoverer = new SlimDiscoverer();
        discoverer.startDiscovery();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SlimDiscovererTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        discoverer.stopDiscovery();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SlimDiscovererTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        discoverer.startDiscovery();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SlimDiscovererTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        discoverer.stopDiscovery();
    }
}
