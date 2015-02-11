/*
 * Weapon.java
 *
 * Created on May 5, 2007, 12:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood.equipment;

import fi.darkwood.*;
import java.util.Random;

/**
 *
 * @author Tommi Laukkanen, Teemu
 */
public class Weapon extends Equipment {
    
    // x and y coordinate for drawing armor paperdoll image on the warrior animation (index = frame)
    public final static int[] ANIMATION_X_WARRIOR = { 0, 1, 2, 3, 4, 4, 3, 1, 0, -1, 0, 0, -1, 0, -1, 0};
    public final static int[] ANIMATION_Y_WARRIOR = { 0, 0, 0, 0, 0, 2, 3, 2, 1, 0, 0, 0, -3, -7, -3, 0};
 
    
    public final static int[] ANIMATION_X_CLERIC = { 0, 1, -1, -1, -1, -1, -1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0};
    public final static int[] ANIMATION_Y_CLERIC = { 0, 0, -1, -3, -4, -4, -3, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public final static int[] ANIMATION_X_MAGE = { 0, -1, -2, -3, -3, -3, -2, -2, -1, 0, 0, -1, -6, -2, -2, -6};
    public final static int[] ANIMATION_Y_MAGE = { 0, -1, -2, -1, 0, 1, 1, 1, 0, 0, 0, 1, -11, -17, -16, -10};
    

    
    /** Creates a new instance of Weapon */
    // Following constructor is legacy, and should be made obsolete ++Ville
    // It is Still used for initial weapons
    public Weapon(String name,String description,String image,int level,int variance) {
        super(name,description,image,level);
        this.minDamage=level - variance;
        this.maxDamage=level + variance;
        this.randomSeed = variance;
    }

   // Following constructor is used by EquipmentFactory, and should be used elsewhere too. ++Ville
     public Weapon(String name,String description,String image,int level, int type, int quality, int stats[], int randomSeed) {
        super(name,description,image,level,type,quality,stats);
        // Weapon average damage is calculated here
        int avgDmg = GameConstants.weaponDamageCoef * (level + quality*3);
        /*
        Random rnd = new Random();
        int vari = rnd.nextInt(avgDmg);
         */
        int vari = avgDmg * randomSeed / 100;
        this.minDamage=avgDmg - vari;
        this.maxDamage=avgDmg + vari;
        this.randomSeed = randomSeed;
    }
    
    public int minDamage;
    public int maxDamage;
    
}
