package org.bff.squeezeserver.domain;

/**
 * Represents a root level podcast
 *
 * @author Bill Findeisen
 */
public class Podcast extends XMLPluginItem {

    /**
     * Constructor
     *
     * @param id   podcast id
     * @param name podcast name
     */
    public Podcast(String id, String name) {
        super(id, name);
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
