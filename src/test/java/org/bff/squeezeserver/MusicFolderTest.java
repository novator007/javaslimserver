/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

import org.bff.squeezeserver.domain.Folder;
import org.bff.squeezeserver.exception.SqueezeException;
import org.junit.*;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bfindeisen
 */
@Category(IntegrationTest.class)
public class MusicFolderTest extends BaseTest {

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
    public void testAllFolders() throws SqueezeException {
        List<Folder> folders = new ArrayList<Folder>(getFolderBrowser().getFolders());

        for (Folder folder : folders) {
            System.out.println(folder.getId());
            System.out.println("\t" + folder.getFileName());
            System.out.println("\t" + folder.getFolderType().getDescription());
        }
    }

// TODO add test methods here.
// The methods must be annotated with annotation @Test. For example:
//
// @Test
// public void hello() {}
}
