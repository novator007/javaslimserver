package org.bff.slimserver.musicobjects;

/**
 * Represents a track
 * 
 * @author Bill Findeisen
 */
public class SlimTrack extends SlimPlayableItem {
    /**
     * Default constructor
     */
    public SlimTrack() {
        super();
    }

    /**
     * Constructor
     * @param id genre id
     * @param name genre name
     */
    public SlimTrack(String id, String name) {
        super(id, name);
    }

}
