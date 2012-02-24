/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

import org.apache.log4j.Logger;
import org.bff.squeezeserver.domain.XMLPluginItem;
import org.bff.squeezeserver.domain.favorite.Favorite;
import org.bff.squeezeserver.domain.podcast.Podcast;
import org.bff.squeezeserver.domain.podcast.PodcastAudioDetails;
import org.bff.squeezeserver.domain.XMLBrowserAudioDetails;
import org.bff.squeezeserver.exception.NetworkException;
import org.bff.squeezeserver.exception.ResponseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author bfindeisen
 */
public class Podcaster extends Plugin {

    private static final String PLUGIN_COMMAND = Constants.CMD_PODCAST;
    private static Logger logger = Constants.LOGGER_PODCAST;

    /**
     * Creates a new instance of Database
     *
     * @param squeezeServer the {@link SqueezeServer} for this database
     */
    public Podcaster(SqueezeServer squeezeServer) {
        super(squeezeServer);
    }

    public Collection<Podcast> getPodcasts() throws NetworkException {
        List<Podcast> podcasts = new ArrayList<Podcast>();
        for (XMLPluginItem xmlItem : getXMLList()) {
            Podcast podcast = new Podcast(xmlItem.getId(), xmlItem.getName());
            podcast.setSqueezeType(XMLPluginItem.SQUEEZE_TYPE.PODCAST);
            super.copyItem(xmlItem, podcast);
            podcasts.add(podcast);
        }
        return podcasts;
    }

    public void loadPodcast(Podcast podcast) {

    }

    @Override
    public String getCommand() {
        return PLUGIN_COMMAND;
    }

    public PodcastAudioDetails getPodcastAudioDetails(Podcast podcast) throws NetworkException {
        XMLBrowserAudioDetails xmlAudioDetails = super.getXMLAudioDetails(podcast);
        PodcastAudioDetails pad = new PodcastAudioDetails(xmlAudioDetails.getId(), PLUGIN_COMMAND);
        super.copyAudioDetails(xmlAudioDetails, pad);
        return pad;
    }
}
