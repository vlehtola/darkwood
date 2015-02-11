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
public class Necklace extends Armour {
    
    /** Creates a new instance of ChestArmour */
    public Necklace(String name,String description,String image,int level) {
        super(name,description,image,level);
        this.value = GameConstants.itemCost(level, GameConstants.QUALITY_UNCOMMON);;
        this.setArmorClass(0);
    }
    
}
