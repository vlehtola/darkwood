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
public class Halberdier extends Monster {
    public Halberdier() {
         super(Local.get("halberdier"), "/images/monster/halberdier.png", 65);
          setCreatureType(super.HUMANOID); 
    }

}
