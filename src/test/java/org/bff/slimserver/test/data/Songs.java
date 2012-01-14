/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.test.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bff.slimserver.domain.Artist;
import org.bff.slimserver.domain.Song;

/**
 * Song ids must be filled since not reliable
 *
 * @author bill
 */
public class Songs {

    private static HashMap<Artist, Collection<Song>> testSongArtistMap =
            new HashMap<Artist, Collection<Song>>();
    private static List<Song> testSongs;
    private static HashMap<String, Song> testDatabaseSongMap =
            new HashMap<String, Song>();

    /**
     * @return the testSongs
     */
    public static List<Song> getTestSongs() {
        return testSongs;
    }

    /**
     * @param aTestSongs the testSongs to set
     */
    public static void setTestSongs(List<Song> aTestSongs) {
        testSongs = aTestSongs;
    }

    /**
     * @return the testSongArtistMap
     */
    public static HashMap<Artist, Collection<Song>> getTestSongArtistMap() {
        return testSongArtistMap;
    }

    /**
     * @param aTestSongArtistMap the testSongArtistMap to set
     */
    public static void setTestSongArtistMap(HashMap<Artist, Collection<Song>> aTestSongArtistMap) {
        testSongArtistMap = aTestSongArtistMap;
    }

    /**
     * @return the testDatabaseSongMap
     */
    public static HashMap<String, Song> getTestDatabaseSongMap() {
        return testDatabaseSongMap;
    }

    /**
     * @param aTestDatabaseSongMap the testDatabaseSongMap to set
     */
    public static void setTestDatabaseSongMap(HashMap<String, Song> aTestDatabaseSongMap) {
        testDatabaseSongMap = aTestDatabaseSongMap;
    }

}
