/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.one.monsters;

import fi.darkwood.Monster;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class Bandit extends Monster {
    public Bandit() {
        super(Local.get("bandit"), "/images/monster/bandit.png", 51);
        setCreatureType(super.HUMANOID);      
    }

}
