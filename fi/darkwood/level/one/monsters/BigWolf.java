/*
 * Skeleton.java
 *
 * Created on December 28, 2007, 6:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood.level.one.monsters;

import fi.darkwood.Monster;
import fi.mirake.Local;

/**
 *
 * @author Teemu Kivim�ki
 */
public class BigWolf extends Monster {
    /** Creates a new instance of Wolf */    
    public BigWolf() {
        super(Local.get("big wolf"), "/images/monster/wolf/big_wolf.png", 60);
        setCreatureType(super.ANIMAL);      
    }
    
}
