package org.bff.slimserver.musicobjects;

/**
 * Represents a contributor
 * 
 * @author Bill Findeisen
 */
public class SlimContributor extends SlimPlayableItem {

    /**
     * Default constructor
     */
    public SlimContributor() {
        super();
    }

    /**
     * Constructor
     * 
     * @param id contributor id
     * @param name contributor name
     */
    public SlimContributor(String id, String name) {
        super(id, name);
    }
}
