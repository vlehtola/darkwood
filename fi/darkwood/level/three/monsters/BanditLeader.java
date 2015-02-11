/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.three.monsters;

import fi.darkwood.Monster;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class BanditLeader extends Monster {
    public BanditLeader() {
        super(Local.get("bandit leader"), "/images/monster/bandit_leader.png", 59);
        setCreatureType(super.HUMANOID);      
    }

}
