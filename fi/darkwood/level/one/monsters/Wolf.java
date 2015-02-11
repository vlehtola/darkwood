/*
 * Skeleton.java
 *
 * Created on December 28, 2007, 6:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood.level.one.monsters;

import fi.darkwood.Creature;
import fi.darkwood.Monster;
import fi.mirake.Local;

/**
 *
 * @author Teemu Kivim�ki
 */
public class Wolf extends Monster {
    /** Creates a new instance of Wolf */
    public Wolf() {
        super(Local.get("wolf"),"/images/monster/wolf/wolf.png", 52);
        setCreatureType(Creature.ANIMAL);            
    }
    
}
