/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver.test.data;

import org.bff.squeezeserver.domain.Album;
import org.bff.squeezeserver.domain.Genre;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
