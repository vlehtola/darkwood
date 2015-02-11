/*
 * Properties.java
 *
 * Created on May 18, 2007, 8:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.util;

import fi.darkwood.DarkwoodMidlet;
import java.util.Hashtable;

/**
 *
 * @author Tommi Laukkanen
 */
public class Properties {

    private static Hashtable applicationProperties = new Hashtable();

    public static Hashtable getApplicationProperties() {
        return applicationProperties;
    }
}
