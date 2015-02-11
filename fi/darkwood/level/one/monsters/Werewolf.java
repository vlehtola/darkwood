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
 * @author Teemu
 */
public class Werewolf extends Monster {
    /** Creates a new instance of Werewolf */
    public Werewolf() {
        super(Local.get("werewolf"),"/images/monster/werewolf/werewolf.png", 44);
        setCreatureType(super.UNDEAD);
        setCreatureType(super.HUMANOID);
    }
    
}
