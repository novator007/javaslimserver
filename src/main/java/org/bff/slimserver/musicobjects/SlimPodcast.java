package org.bff.slimserver.musicobjects;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a root level podcast
 * 
 * @author Bill Findeisen
 */
public class SlimPodcast extends SlimPlayableItem {
    private int count;
    private Collection<SlimPodcastItem> podcastItems;
    private boolean error;
    private String errorMessage;

    //private boolean
    /**
     * Default constructor
     */
    public SlimPodcast() {
        super();
        podcastItems = new ArrayList<SlimPodcastItem>();
    }

    /**
     * Constructor
     * @param id podcast id
     * @param name podcast name
     */
    public SlimPodcast(String id, String name) {
        super(id, name);
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * The number of elements this podcast has
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    public void addPodcastItem(SlimPodcastItem item) {
        getPodcastItems().add(item);
    }

    /**
     * @return the podcastItems
     */
    public Collection<SlimPodcastItem> getPodcastItems() {
        return podcastItems;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isError() {
        return this.error;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getTitle() {
        return getName();
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
