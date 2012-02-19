/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.squeezeserver.domain.radio;

import org.bff.squeezeserver.domain.XMLPluginItem;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bfindeisen
 */
public class Radio extends XMLPluginItem {
    private String searchCriteria;

    /**
     * Constructor
     *
     * @param id   podcast id
     * @param name podcast name
     */
    public Radio(String id, String name) {
        super(id, name);
    }

    public Collection<Radio> getRadios() {
        List<Radio> radios = new ArrayList<Radio>();
        return radios;
    }

    public boolean isSearch() {
        return false;
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

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
