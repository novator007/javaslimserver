/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.domain.radio;

import org.bff.slimserver.domain.Album;
import org.bff.slimserver.domain.Genre;
import org.bff.slimserver.domain.XMLBrowserAudioDetails;

/**
 * @author bfindeisen
 */
public class RadioAudioDetails extends XMLBrowserAudioDetails {

    private AvailableRadio radio;

    public RadioAudioDetails(AvailableRadio radio, String radioId) {
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
    public Album getAlbum() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRemote() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Genre getGenre() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setGenre(Genre genre) {
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
    public AvailableRadio getRadio() {
        return radio;
    }

    /**
     * @param radio the radio to set
     */
    public void setRadio(AvailableRadio radio) {
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
