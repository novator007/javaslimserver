/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver.test.integrationdata;

import org.bff.squeezeserver.domain.Album;
import org.bff.squeezeserver.domain.Artist;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author bfindeisen
 */
public class Artists {

    public static final HashMap<Artist, Collection<Album>> TEST_ARTIST_ALBUM_MAP =
            new HashMap<Artist, Collection<Album>>();

    private static Collection<Artist> testArtists;

    /**
     * @return the testArtists
     */
    public static Collection<Artist> getTestArtists() {
        return testArtists;
    }

    /**
     * @param aTestArtists the testArtists to set
     */
    public static void setTestArtists(Collection<Artist> aTestArtists) {
        testArtists = aTestArtists;
    }
}
