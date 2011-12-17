/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.test.data;

import java.util.Collection;
import java.util.HashMap;

import org.bff.slimserver.musicobjects.SlimAlbum;
import org.bff.slimserver.musicobjects.SlimArtist;

/**
 *
 * @author bfindeisen
 */
public class Artists {

    public static final HashMap<SlimArtist, Collection<SlimAlbum>> TEST_ARTIST_ALBUM_MAP =
            new HashMap<SlimArtist, Collection<SlimAlbum>>();
    
    private static Collection<SlimArtist> testArtists;

    /**
     * @return the testArtists
     */
    public static Collection<SlimArtist> getTestArtists() {
        return testArtists;
    }

    /**
     * @param aTestArtists the testArtists to set
     */
    public static void setTestArtists(Collection<SlimArtist> aTestArtists) {
        testArtists = aTestArtists;
    }
}
