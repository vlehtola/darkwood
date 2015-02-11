/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.six.monsters;

import fi.darkwood.specialMonsters.ShieldWallMonster;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class SkeletonWarrior extends ShieldWallMonster {

    public SkeletonWarrior() {
        super(Local.get("skeleton warrior"), "/images/monster/skeleton_warrior.png", 68);
        setCreatureType(super.UNDEAD);   
    }
    
}
