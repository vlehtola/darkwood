/*
 * Shield.java
 *
 * Created on May 5, 2007, 12:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood.equipment;

/**
 *
 * @author Tommi Laukkanen
 */
public class Shield extends Armour {
    
    // x and y coordinate for drawing armor paperdoll image on the warrior animation (index = frame)
    public final static int[] ANIMATION_X_WARRIOR = { 0, 1, 2, 1, 2, 3, 3, 1, 0, -1, -1, 0, 8, 13, 8, 0};
    public final static int[] ANIMATION_Y_WARRIOR = { 0, 0, 0, -1, -2, -1, 0, 1, 1, 1, 0, 0, -1, -5, -1, 0};
    
    
    public final static int[] ANIMATION_X_CLERIC = { 0, 2, 3, 3, 2, 2, 3, 3, 2, 1, 0, 0, 1, 3, 12, 11, 3, 0};
    public final static int[] ANIMATION_Y_CLERIC = { 0, 0, -2, -3, -4, -4, -3, -2, 0, 0, 0, 0, -6, -8, -11, -11, -8, 0};
    
    public final static int[] ANIMATION_X_MAGE = {0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 1, 2, 3, 2, 1};
    public final static int[] ANIMATION_Y_MAGE = {0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 0, 5, 5, 2, 1, 0};

    
    /** Creates a new instance of Shield */
    public Shield(String name,String description,String image,int level) {
        super(name,description,image,level);
    }

    public Shield(String name,String description,String image,int level, int type, int quality, int stats[]) {
        super(name,description,image,level,type,quality,stats);
    }
    
}
