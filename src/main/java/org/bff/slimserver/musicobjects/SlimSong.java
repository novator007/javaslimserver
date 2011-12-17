package org.bff.slimserver.musicobjects;

/**
 * Represents a song in a SqueezeCenter
 * 
 * @author Bill Findeisen
 */
public class SlimSong extends SlimPlaylistItem {

    /**
     * Default constructor
     */
    public SlimSong() {
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return (true);
        }

        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }

        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
