/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.musicobjects.favorite;

import org.bff.slimserver.musicobjects.SlimXMLBrowserAudioDetails;

/**
 *
 * @author bfindeisen
 *
 * count
id
title
isaudio
enclosure_length
enclosure_url
enclosure_type
pubdate
value
description
link
explicit
image
duration
subtitle
summary

 */
public class SlimFavoriteAudioDetails extends SlimXMLBrowserAudioDetails {


    public SlimFavoriteAudioDetails(String id, String command) {
       super(id, command);
    }
}
