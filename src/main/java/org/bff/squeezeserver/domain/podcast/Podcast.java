package org.bff.squeezeserver.domain.podcast;

import org.bff.squeezeserver.domain.XMLPluginItem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public Collection<Podcast> getPodcasts() {
        List<Podcast> Podcasts = new ArrayList<Podcast>();

        return Podcasts;
    }

    @Override
    public String toString() {
        return getName() == null ? getTitle() : getName();
    }

    /**
     * Overrides the equals method.  Returns true if the names are equal.
     *
     * @param object the object to compare with this
     * @return true if the codes are equal, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return (true);
        }

        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }

        Podcast item = (Podcast) object;
        if (this.getId().equals(item.getId())) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Overrides hashCode method
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) (getName() == null ? getTitle().length() : getName().length());
        hash = 31 * hash + (null == getName() ? getTitle().hashCode() : getName().hashCode());
        return (hash);
    }
}
