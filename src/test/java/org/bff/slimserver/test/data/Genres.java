/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.test.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bff.slimserver.domain.Album;
import org.bff.slimserver.domain.Genre;

/**
 * @author bfindeisen
 */
public class Genres {
    private static List<Genre> testGenres;

    public static final HashMap<Genre, Collection<Album>> GENRE_ALBUM_MAP = new HashMap<Genre, Collection<Album>>();

    /**
     * @return the testGenres
     */
    public static List<Genre> getTestGenres() {
        return testGenres;
    }

    /**
     * @param aTestGenres the testGenres to set
     */
    public static void setTestGenres(List<Genre> aTestGenres) {
        testGenres = aTestGenres;
    }
}
