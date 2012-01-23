/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.test.data;

import org.bff.slimserver.domain.Album;

import java.util.List;

/**
 * @author bfindeisen
 */
public class Albums {

    private static List<Album> testAlbums;

    /**
     * @return the testAlbums
     */
    public static List<Album> getTestAlbums() {
        return testAlbums;
    }

    /**
     * @param aTestAlbums the testAlbums to set
     */
    public static void setTestAlbums(List<Album> aTestAlbums) {
        testAlbums = aTestAlbums;
    }
}
