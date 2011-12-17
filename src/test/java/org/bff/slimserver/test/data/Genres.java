/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.test.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bff.slimserver.SlimDatabase;
import org.bff.slimserver.Controller;
import org.bff.slimserver.musicobjects.SlimAlbum;
import org.bff.slimserver.musicobjects.SlimGenre;

/**
 * 
 * @author bfindeisen
 */
public class Genres {
    private static List<SlimGenre> testGenres;

    public static final HashMap<SlimGenre, Collection<SlimAlbum>> GENRE_ALBUM_MAP = new HashMap<SlimGenre, Collection<SlimAlbum>>();

    /**
     * @return the testGenres
     */
    public static List<SlimGenre> getTestGenres() {
        return testGenres;
    }

    /**
     * @param aTestGenres the testGenres to set
     */
    public static void setTestGenres(List<SlimGenre> aTestGenres) {
        testGenres = aTestGenres;
    }
}
