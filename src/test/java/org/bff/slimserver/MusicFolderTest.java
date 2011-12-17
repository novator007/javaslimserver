/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.musicobjects.SlimFolderObject;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bfindeisen
 */
public class MusicFolderTest extends Base {

    public MusicFolderTest() {
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
    public void testAllFolders() throws SlimException {
        List<SlimFolderObject> folders = new ArrayList<SlimFolderObject>(getFolderBrowser().getFolders());

        for (SlimFolderObject folder : folders) {
            //System.out.println(folder.getId());
            //System.out.println(folder.getName());
            //System.out.println(folder.getFileName());
            //System.out.println(folder.getObjectType().getDescription());
        }
    }

// TODO add test methods here.
// The methods must be annotated with annotation @Test. For example:
//
// @Test
// public void hello() {}
}
