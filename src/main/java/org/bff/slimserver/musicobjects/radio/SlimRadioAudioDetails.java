/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.musicobjects.radio;

import java.awt.Image;
import org.bff.slimserver.musicobjects.SlimAlbum;
import org.bff.slimserver.musicobjects.SlimGenre;
import org.bff.slimserver.musicobjects.SlimXMLBrowserAudioDetails;

/**
 *
 * @author bfindeisen
 */
public class SlimRadioAudioDetails extends SlimXMLBrowserAudioDetails {

    private SlimAvailableRadio radio;

    public SlimRadioAudioDetails(SlimAvailableRadio radio, String radioId) {
        super(radioId, radio.getCommand());
        setRadio(radio);
        setItemId(radioId);
    }

    @Override
    public int getLength() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getArtist() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SlimAlbum getAlbum() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRemote() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SlimGenre getGenre() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setGenre(SlimGenre genre) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getComment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setComment(String comment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getYear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setYear(String year) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getTrack() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTrack(int track) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the radio
     */
    public SlimAvailableRadio getRadio() {
        return radio;
    }

    /**
     * @param radio the radio to set
     */
    public void setRadio(SlimAvailableRadio radio) {
        this.radio = radio;
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
