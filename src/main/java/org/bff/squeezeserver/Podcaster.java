/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver;

import org.bff.squeezeserver.domain.Podcast;
import org.bff.squeezeserver.domain.PodcastAudioDetails;
import org.bff.squeezeserver.domain.XMLBrowserAudioDetails;
import org.bff.squeezeserver.exception.NetworkException;

/**
 * @author bfindeisen
 */
public class Podcaster extends Plugin {

    private static final String PLUGIN_COMMAND = Constants.CMD_PODCAST;

    /**
     * Creates a new instance of Database
     *
     * @param squeezeServer the {@link SqueezeServer} for this database
     */
    public Podcaster(SqueezeServer squeezeServer) {
        super(squeezeServer);
    }

    @Override
    public String getCommand() {
        return PLUGIN_COMMAND;
    }

    public PodcastAudioDetails getPodcastAudioDetails(Podcast podcast) throws NetworkException {
        XMLBrowserAudioDetails xmlAudioDetails = super.getXMLAudioDetails(podcast);
        return convertDetails(xmlAudioDetails);
    }

    private PodcastAudioDetails convertDetails(XMLBrowserAudioDetails xmlAudioDetails) {
        PodcastAudioDetails pad = new PodcastAudioDetails(xmlAudioDetails.getId(), PLUGIN_COMMAND);
        pad.setAlbum(xmlAudioDetails.getAlbum());
        pad.setLength(xmlAudioDetails.getLength());
        pad.setName(xmlAudioDetails.getName());
        pad.setRating(xmlAudioDetails.getRating());
        pad.setBitrate(xmlAudioDetails.getBitrate());
        pad.setComment(xmlAudioDetails.getComment());
        pad.setCount(xmlAudioDetails.getCount());
        pad.setDescription(xmlAudioDetails.getDescription());
        pad.setEnclosureLength(xmlAudioDetails.getEnclosureLength());
        pad.setEnclosureType(xmlAudioDetails.getEnclosureType());
        pad.setEnclosureUrl(xmlAudioDetails.getEnclosureUrl());
        pad.setExplicit(xmlAudioDetails.getExplicit());
        pad.setGenre(xmlAudioDetails.getGenre());
        pad.setImageUrl(xmlAudioDetails.getImageUrl());
        pad.setPubDate(xmlAudioDetails.getPubDate());
        pad.setSubTitle(xmlAudioDetails.getSubTitle());
        pad.setSummary(xmlAudioDetails.getSummary());
        pad.setTitle(xmlAudioDetails.getTitle());
        pad.setTotalDuration(xmlAudioDetails.getTotalDuration());
        pad.setTrack(xmlAudioDetails.getTrack());
        pad.setType(xmlAudioDetails.getType());
        pad.setUrl(xmlAudioDetails.getUrl());
        pad.setValue(xmlAudioDetails.getValue());
        pad.setXmlId(xmlAudioDetails.getXmlId());
        pad.setYear(xmlAudioDetails.getYear());
        return pad;
    }
}
