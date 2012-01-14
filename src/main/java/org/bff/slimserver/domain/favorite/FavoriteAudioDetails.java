/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.domain.favorite;

import org.bff.slimserver.domain.XMLBrowserAudioDetails;

/**
 * @author bfindeisen
 *         <p/>
 *         count
 *         id
 *         title
 *         isaudio
 *         enclosure_length
 *         enclosure_url
 *         enclosure_type
 *         pubdate
 *         value
 *         description
 *         link
 *         explicit
 *         image
 *         duration
 *         subtitle
 *         summary
 */
public class FavoriteAudioDetails extends XMLBrowserAudioDetails {


    public FavoriteAudioDetails(String id, String command) {
        super(id, command);
    }
}
