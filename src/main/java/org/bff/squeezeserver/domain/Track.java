package org.bff.squeezeserver.domain;

/**
 * Represents a track
 *
 * @author Bill Findeisen
 */
public class Track extends PlayableItem {
    /**
     * Default constructor
     */
    public Track() {
        super();
    }

    /**
     * Constructor
     *
     * @param id   genre id
     * @param name genre name
     */
    public Track(String id, String name) {
        super(id, name);
    }

}
