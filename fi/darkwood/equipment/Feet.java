/*
 * ChestArmour.java
 *
 * Created on May 5, 2007, 12:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood.equipment;

import fi.darkwood.GameConstants;

/**
 *
 * @author Teemu Kivimäki
 */
public class Feet extends Armour {
    
    /** Creates a new instance of ChestArmour */
    public Feet(String name,String description,String image,int level) {
        super(name,description,image,level);
    }
    public Feet(String name,String description,String image,int level, int type, int quality, int stats[]) {
        super(name,description,image,level,type,quality,stats);
    }
}
