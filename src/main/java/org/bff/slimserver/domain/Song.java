package org.bff.slimserver.domain;

/**
 * Represents a song in a SqueezeCenter
 *
 * @author Bill Findeisen
 */
public class Song extends PlaylistItem {

    /**
     * Default constructor
     */
    public Song() {
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
