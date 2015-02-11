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
public class Quartermaster extends Monster {
    public Quartermaster() {
         super(Local.get("quartermaster"), "/images/monster/quartermaster.png", 47);
          setCreatureType(super.HUMANOID); 
    }

}
