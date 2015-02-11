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
public class FencingGuard extends Monster {
    public FencingGuard(){
        super(Local.get("fencing guard"), "/images/monster/fencing guard.png", 72);
         setCreatureType(super.HUMANOID); 
    }

}
