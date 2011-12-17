/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.test.data;

import java.util.Collection;
import java.util.HashMap;

import org.bff.slimserver.musicobjects.SlimAlbum;

/**
 * 
 * @author bill
 */
public class Years {

    private static Collection<String> testYears;
    public static final HashMap<String, Collection<SlimAlbum>> YEAR_ALBUM_MAP = new HashMap<String, Collection<SlimAlbum>>();

    /**
     * @return the testYears
     */
    public static Collection<String> getTestYears() {
        return testYears;
    }

    /**
     * @param aTestYears the testYears to set
     */
    public static void setTestYears(Collection<String> aTestYears) {
        testYears = aTestYears;
    }
}
