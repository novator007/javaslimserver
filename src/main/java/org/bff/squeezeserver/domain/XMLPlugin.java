/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.squeezeserver.domain;

/**
 * @author bfindeisen
 */
public abstract class XMLPlugin extends PlayableItem {
    public abstract String getItemId();

    public abstract String getCommand();
}
