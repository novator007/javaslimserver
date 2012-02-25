/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver.domain.podcast;

import org.bff.squeezeserver.domain.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bfindeisen
 */
public class PodcastAudioDetails extends XMLBrowserAudioDetails {

    public PodcastAudioDetails(String id, String command) {
        super(id, command);
    }
}
