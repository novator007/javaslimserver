/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bfindeisen
 */
public class Utils {

    public static String encode(String string, String encoding) {
        String encodedString = null;
        try {
            encodedString = URLEncoder.encode(string, encoding);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return encodedString.replaceAll("\\+", "%20");
    }

    public static String decode(String string, String encoding) {
        String decodedString = null;
        try {
            decodedString = URLDecoder.decode(string, encoding);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return decodedString;
    }
}
