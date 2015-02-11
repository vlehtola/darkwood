/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.four.monsters;

import fi.darkwood.Monster;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class Zombie extends Monster {
    public Zombie() {
        super(Local.get("zombie"), "/images/monster/zombie.png", 34);
        setCreatureType(super.UNDEAD);   
    }

}
