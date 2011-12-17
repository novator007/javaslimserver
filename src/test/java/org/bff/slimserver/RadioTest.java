/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.exception.SlimNetworkException;
import org.bff.slimserver.exception.SlimResponseException;
import org.bff.slimserver.musicobjects.radio.SlimAvailableRadio;
import org.bff.slimserver.musicobjects.radio.SlimRadioItem;
import org.junit.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bfindeisen
 */
@Ignore
public class RadioTest extends Base {

    public RadioTest() {
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
        List<SlimAvailableRadio> radios = new ArrayList<SlimAvailableRadio>(getRadioPlugin().getAvailableRadios());

        for (SlimAvailableRadio radio : radios) {
            System.out.println(radio.getId());
            System.out.println(radio.getName());
            System.out.println(radio.getIconURL());
            System.out.println(radio.getSmallIconURL());
            System.out.println(radio.getType());
            System.out.println(radio.getWeight());
        }
    }

    private void listRadios(SlimRadioItem item) throws SlimException {
        getRadioPlugin().loadRadio(item, item.getCommand(),
                new ArrayList<SlimPlayer>(getSlimServer().getSlimPlayers()).get(0));
        for (SlimRadioItem plugin : item.getRadioItems()) {
            System.out.println("\tcommand: " + plugin.getCommand());
//            System.out.println("\tradio id:" + plugin.getRadioId());
            System.out.println("\ttitle: " + plugin.getTitle());
//            System.out.println("\tname: " + plugin.getName());
            System.out.println("\ttype: " + plugin.getType());
            System.out.println("\tcontains items: " + plugin.isContainsItems());
            System.out.println("\taudio: " + plugin.isAudio());


            if (plugin.isAudio()) {
                getRadioPlugin().getAudioDetails(plugin, new ArrayList<SlimPlayer>(getSlimServer().getSlimPlayers()).get(0));
            }
            if (plugin.isContainsItems()) {
                listRadios(plugin);
            }
        }
    }

    /*
    @Test
    public void testRadioDetails() throws SlimNetworkException, MalformedURLException, IOException, SlimResponseException, SlimException {
    List<SlimAvailableRadio> radios = new ArrayList<SlimAvailableRadio>(getRadioPlugin().getAvailableRadios());

    for (SlimAvailableRadio radio : radios) {
    System.out.println(radio.getId());
    System.out.println(radio.getName());
    System.out.println(radio.getIconURL());
    System.out.println(radio.getSmallIconURL());
    System.out.println(radio.getType());
    System.out.println(radio.getWeight());


    if (radio.isBrowser()) {
    SlimRadioItem item = getRadioPlugin().loadRadio(radio,
    new ArrayList<SlimPlayer>(getSlimServer().getSlimPlayers()).get(0));


    listRadios(item);
    }

    }
    }
     */
// TODO add test methods here.
// The methods must be annotated with annotation @Test. For example:
//
// @Test
// public void hello() {}
}
