/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.domain.app;

import org.bff.slimserver.domain.PlayableItem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author bfindeisen
 */
public class AvailableApp extends PlayableItem {

    private String iconURL;
    private String command;
    private int weight;
    private String type;
    private boolean error;
    private String errorMessage;
    private ImageIcon icon;
    private ImageIcon smallIcon;
    private String searchCriteria;
    private static String XML_BROWSER = "xmlbrowser";
    private static String XML_BROWSER_SEARCH = "xmlbrowser_search";

    /**
     * @return the icon
     */
    public String getIconURL() {
        return iconURL;
    }

    /**
     * @param iconURL the icon to set
     */
    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getSmallIconURL() {
        String replace = getIconURL().substring(getIconURL().lastIndexOf("."));
        return getIconURL().replace(replace, "_25x25_f" + replace);
    }

    public static void main(String[] args) {
        try {
            ImageIcon icon = new ImageIcon(new URL("http://172.30.74.5:9000/plugins/cache/icons/picks.png"));
            JLabel lbl = new JLabel(icon);

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(lbl);
            frame.setVisible(true);

        } catch (MalformedURLException ex) {
            Logger.getLogger(AvailableApp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean isBrowser() {
        return XML_BROWSER.equalsIgnoreCase(getType());
    }

    public boolean isSearch() {
        return XML_BROWSER_SEARCH.equalsIgnoreCase(getType());
    }

    /**
     * Returns the icon for the radio.  There is some performance overhead with
     * this method the first time it is called.
     *
     * @return the {@link ImageIcon} for this radio
     */
    public ImageIcon getIcon() {
        if (icon == null) {

            try {
                icon = new ImageIcon(new URL(getIconURL()));
            } catch (Exception ex) {
                Logger.getLogger(AvailableApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return icon;
    }

    /**
     * Returns the small (25x25) icon for the radio.  There is some performance overhead with
     * this method the first time it is called.
     *
     * @return the {@link ImageIcon} for this radio
     */
    public ImageIcon getSmallIcon() {
        if (smallIcon == null) {
            try {
                smallIcon = new ImageIcon(new URL(getSmallIconURL()));
            } catch (Exception ex) {
                Logger.getLogger(AvailableApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return smallIcon;
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the searchCriteria
     */
    public String getSearchCriteria() {
        return searchCriteria;
    }

    /**
     * @param searchCriteria the searchCriteria to set
     */
    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
