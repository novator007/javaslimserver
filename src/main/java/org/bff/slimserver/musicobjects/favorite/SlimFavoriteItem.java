/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.slimserver.musicobjects.favorite;

/**
 *
 * @author bfindeisen
 */
public class SlimFavoriteItem extends SlimFavorite {
    
    public SlimFavoriteItem(String id) {
         super(id);
    }

    public void addFavoriteItem(SlimFavoriteItem spi) {
        super.addXMLItem(spi);
    }
}
