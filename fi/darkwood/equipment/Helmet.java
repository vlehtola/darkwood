/*
 * Helmet.java
 *
 * Created on May 5, 2007, 12:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.equipment;

import fi.darkwood.GameConstants;

/**
 *
 * @author Tommi Laukkanen, Teemu
 */
public class Helmet extends Armour {
    
    // x and y coordinate for drawing armor paperdoll image on the warrior animation (index = frame)
   
    public final static int[] ANIMATION_X_WARRIOR = { 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, -1, 0, -1, 0 };
    public final static int[] ANIMATION_Y_WARRIOR = { 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 };

    public final static int[] ANIMATION_X_CLERIC = {0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0};
    public final static int[] ANIMATION_Y_CLERIC = {0, 0, -1, -1, -2, -2, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    
    public final static int[] ANIMATION_X_MAGE = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public final static int[] ANIMATION_Y_MAGE = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    
    /** Creates a new instance of Helmet */
    public Helmet(String name, String description, String image, int level) {
        super(name, description, image, level);
        
    }
   
     public Helmet(String name,String description,String image,int level, int type, int quality, int stats[]) {
        super(name,description,image,level,type, quality, stats);
    }
}
