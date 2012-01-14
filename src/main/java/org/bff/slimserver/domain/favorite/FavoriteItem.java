/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.slimserver.domain.favorite;

/**
 * @author bfindeisen
 */
public class FavoriteItem extends Favorite {

    public FavoriteItem(String id) {
        this(id, "");
    }

    public FavoriteItem(String id, String name) {
        super(id, name);
    }

    public void addFavoriteItem(FavoriteItem spi) {
        super.addXMLItem(spi);
    }
}
