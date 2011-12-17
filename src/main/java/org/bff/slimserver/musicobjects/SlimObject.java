package org.bff.slimserver.musicobjects;

/**
 * Abstract base class for slim objects
 * 
 * @author Bill Findeisen
 */
public abstract class SlimObject {

    public static final String URL_PREFIX_ALBUM = "db:album.titlesearch=";
    public static final String URL_PREFIX_ARTIST = "db:contributor.namesearch=";
    public static final String URL_PREFIX_GENRE = "db:genre.namesearch=";
    public static final String URL_PREFIX_YEAR = "db:year.id=";
    private String id;
    private String name;

    /**
     * Default constructor
     */
    public SlimObject() {
    }

    public SlimObject(String id) {
        this.id = id;
    }

    /**
     * Constructor
     * @param id id
     * @param name name
     */
    public SlimObject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the name for this object
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for this object
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the id for this object
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id for this object
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
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

        try {
            if (this.getId().equals(((SlimObject) object).getId())) {
                return (true);
            } else {
                return (false);
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Overrides hashCode method
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) getName().length();
        hash = 31 * hash + (null == getName() ? 0 : getName().hashCode());
        return (hash);
    }
}
