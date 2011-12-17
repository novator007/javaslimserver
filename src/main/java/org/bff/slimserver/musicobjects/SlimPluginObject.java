/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.slimserver.musicobjects;

/**
 *
 * @author bfindeisen
 */
public abstract class SlimPluginObject extends SlimPlayableItem {
    public abstract String getItemId();
    public abstract String getCommand();
}
