/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.test.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bff.slimserver.musicobjects.SlimArtist;
import org.bff.slimserver.musicobjects.SlimSong;

/**
 * Song ids must be filled since not reliable
 * 
 * @author bill
 */
public class Songs {

    private static HashMap<SlimArtist, Collection<SlimSong>> testSongArtistMap =
            new HashMap<SlimArtist, Collection<SlimSong>>();
    private static List<SlimSong> testSongs;
    private  static HashMap<String, SlimSong> testDatabaseSongMap =
            new HashMap<String, SlimSong>();

    /**
     * @return the testSongs
     */
    public static List<SlimSong> getTestSongs() {
        return testSongs;
    }

    /**
     * @param aTestSongs the testSongs to set
     */
    public static void setTestSongs(List<SlimSong> aTestSongs) {
        testSongs = aTestSongs;
    }

    /**
     * @return the testSongArtistMap
     */
    public static HashMap<SlimArtist, Collection<SlimSong>> getTestSongArtistMap() {
        return testSongArtistMap;
    }

    /**
     * @param aTestSongArtistMap the testSongArtistMap to set
     */
    public static void setTestSongArtistMap(HashMap<SlimArtist, Collection<SlimSong>> aTestSongArtistMap) {
        testSongArtistMap = aTestSongArtistMap;
    }

    /**
     * @return the testDatabaseSongMap
     */
    public static HashMap<String, SlimSong> getTestDatabaseSongMap() {
        return testDatabaseSongMap;
    }

    /**
     * @param aTestDatabaseSongMap the testDatabaseSongMap to set
     */
    public static void setTestDatabaseSongMap(HashMap<String, SlimSong> aTestDatabaseSongMap) {
        testDatabaseSongMap = aTestDatabaseSongMap;
    }

}
