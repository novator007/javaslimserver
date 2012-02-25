/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver.domain.radio;

import org.bff.squeezeserver.domain.Album;
import org.bff.squeezeserver.domain.Genre;
import org.bff.squeezeserver.domain.XMLBrowserAudioDetails;

/**
 * @author bfindeisen
 */
public class RadioAudioDetails extends XMLBrowserAudioDetails {

    private Radio radio;

    public RadioAudioDetails(Radio radio, String command) {
        super(radio.getId(), command);
        setRadio(radio);
        setItemId(radio.getId());
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
    public Radio getRadio() {
        return radio;
    }

    /**
     * @param radio the radio to set
     */
    public void setRadio(Radio radio) {
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
