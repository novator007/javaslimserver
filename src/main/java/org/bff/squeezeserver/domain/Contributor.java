package org.bff.squeezeserver.domain;

/**
 * Represents a contributor
 *
 * @author Bill Findeisen
 */
public class Contributor extends PlayableItem {

    /**
     * Default constructor
     */
    public Contributor() {
        super();
    }

    /**
     * Constructor
     *
     * @param id   contributor id
     * @param name contributor name
     */
    public Contributor(String id, String name) {
        super(id, name);
    }
}
