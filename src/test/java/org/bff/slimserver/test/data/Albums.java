/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.test.data;

import java.util.ArrayList;
import java.util.List;
import org.bff.slimserver.SlimDatabase;
import org.bff.slimserver.Controller;
import org.bff.slimserver.musicobjects.SlimAlbum;

/**
 *
 * @author bfindeisen
 */
public class Albums {

    private static List<SlimAlbum> testAlbums;

    /**
     * @return the testAlbums
     */
    public static List<SlimAlbum> getTestAlbums() {
        return testAlbums;
    }

    /**
     * @param aTestAlbums the testAlbums to set
     */
    public static void setTestAlbums(List<SlimAlbum> aTestAlbums) {
        testAlbums = aTestAlbums;
    }
}
