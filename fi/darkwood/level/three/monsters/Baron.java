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
public class Baron extends Monster {
    public Baron() {
         super(Local.get("baron"), "/images/monster/baron.png", 51);
         setCreatureType(super.HUMANOID);   
    }

}
