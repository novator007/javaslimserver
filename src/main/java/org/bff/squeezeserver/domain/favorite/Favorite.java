/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver.domain.favorite;

import org.bff.squeezeserver.domain.XMLPluginItem;

/**
 * @author Bill
 */
public class Favorite extends XMLPluginItem {
    public Favorite(String id) {
        this(id, "");
    }

    public Favorite(String id, String name) {
        super(id, name);
    }
}
