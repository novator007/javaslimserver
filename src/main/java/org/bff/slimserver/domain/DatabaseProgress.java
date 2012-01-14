/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bill
 */
public class DatabaseProgress {

    private String totalTime;
    private String info;
    private String steps;
    private String fullName;
    private List<Importer> importers;

    public DatabaseProgress() {
        importers = new ArrayList<Importer>();
    }

    /**
     * @return the totalTime
     */
    public String getTotalTime() {
        return totalTime;
    }

    /**
     * @param totalTime the totalTime to set
     */
    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * @return the importer
     */
    public List<Importer> getImporters() {
        return importers;
    }

    /**
     * @param name       the name of the importer
     * @param percentage the percentage complete
     */
    public void addImporter(String name, int percentage) {
        Importer imp = new Importer();

        if (name.equalsIgnoreCase("directory")) {
            name = "Directory Scan";
        }

        if (name.equalsIgnoreCase("mergeva")) {
            name = "Various Artists Merging";
        }

        if (name.equalsIgnoreCase("dboptimize")) {
            name = "Database Optimization";
        }

        if (name.startsWith("cleanup")) {
            name = name.replace("cleanup", "Database Cleanup #");
        }

        if (name.equalsIgnoreCase("itunes_playlists")) {
            name = "iTunes Playlists";
        }

        String firstLetter = name.substring(0, 1);  // Get first letter
        String remainder = name.substring(1);    // Get remainder of word.
        name = firstLetter.toUpperCase() + remainder;

        imp.setImporter(name);
        imp.setPercentage(percentage);

        importers.add(imp);
    }

    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * @return the steps
     */
    public String getSteps() {
        return steps;
    }

    /**
     * @param steps the steps to set
     */
    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public static class Importer {

        private String importer;
        private int percentage;

        /**
         * @return the importer
         */
        public String getImporter() {
            return importer;
        }

        /**
         * @param importer the importer to set
         */
        public void setImporter(String importer) {
            this.importer = importer;
        }

        /**
         * @return the percentage
         */
        public int getPercentage() {
            return percentage;
        }

        /**
         * @param percentage the percentage to set
         */
        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }
    }
}
