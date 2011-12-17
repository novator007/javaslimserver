/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.exception.SlimNetworkException;
import org.bff.slimserver.exception.SlimResponseException;
import org.bff.slimserver.musicobjects.app.SlimAppItem;
import org.bff.slimserver.musicobjects.app.SlimAvailableApp;
import org.junit.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bfindeisen
 */
public class AppTest {

    private SlimAppPlugin appPlugin;
    private SlimServer slimServer;

    public AppTest() throws SlimException, IOException {
        setSlimServer(Controller.getInstance().getSlimServer());
        appPlugin = new SlimAppPlugin(getSlimServer());
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
    public void testAllRadios() throws SlimNetworkException, MalformedURLException, IOException, SlimResponseException {
        List<SlimAvailableApp> radios = new ArrayList<SlimAvailableApp>(getAppPlugin().getAvailableApps());

        for (SlimAvailableApp radio : radios) {
//            System.out.println(radio.getId());
//            System.out.println(radio.getName());
//            System.out.println(radio.getIconURL());
//            System.out.println(radio.getSmallIconURL());
//            System.out.println(radio.getType());
//            System.out.println(radio.getWeight());
        }
    }

    private void listRadios(SlimAppItem item) throws SlimException {
        getAppPlugin().loadApp(item, item.getCommand(),
                new ArrayList<SlimPlayer>(getSlimServer().getSlimPlayers()).get(0));
        for (SlimAppItem plugin : item.getAppItems()) {
            System.out.println("\tcommand: " + plugin.getCommand());
            System.out.println("\tradio id:" + plugin.getAppId());
            System.out.println("\ttitle: " + plugin.getTitle());
            System.out.println("\tname: " + plugin.getName());
            System.out.println("\ttype: " + plugin.getType());
            System.out.println("\tcontains items: " + plugin.isContainsItems());
            System.out.println("\taudio: " + plugin.isAudio());


            if (plugin.isAudio()) {
                getAppPlugin().getAudioDetails(plugin, new ArrayList<SlimPlayer>(getSlimServer().getSlimPlayers()).get(0));
            }
            if (plugin.isContainsItems()) {
                listRadios(plugin);
            }
        }
    }

    /*
    @Test
    public void testRadioDetails() throws SlimNetworkException, MalformedURLException, IOException, SlimResponseException, SlimException {
        List<SlimAvailableApp> radios = new ArrayList<SlimAvailableApp>(getAppPlugin().getAvailableApps());

        for (SlimAvailableApp radio : radios) {
            System.out.println(radio.getId());
            System.out.println(radio.getName());
            System.out.println(radio.getIconURL());
            System.out.println(radio.getSmallIconURL());
            System.out.println(radio.getType());
            System.out.println(radio.getWeight());


            if (radio.isBrowser()) {
                SlimAppItem item = getAppPlugin().loadApp(radio,
                        new ArrayList<SlimPlayer>(getSlimServer().getSlimPlayers()).get(0));


                listRadios(item);
            }

        }
    }

    /**
     * @return the slimServer
     */
    public SlimServer getSlimServer() {
        return slimServer;
    }

    /**
     * @param slimServer the slimServer to set
     */
    public void setSlimServer(SlimServer slimServer) {
        this.slimServer = slimServer;
    }

    /**
     * @return the radioPlugin
     */
    public SlimAppPlugin getAppPlugin() {
        return appPlugin;
    }

    /**
     * @param radioPlugin the radioPlugin to set
     */
    public void setRadioPlugin(SlimAppPlugin appPlugin) {
        this.appPlugin = appPlugin;
    }
// TODO add test methods here.
// The methods must be annotated with annotation @Test. For example:
//
// @Test
// public void hello() {}
}
