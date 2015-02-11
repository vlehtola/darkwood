/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.four.monsters;

import fi.darkwood.Creature;
import fi.darkwood.Monster;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class Spider extends Monster {
    public Spider() {
        super(Local.get("spider"), "/images/monster/Spider 56x38 5 frames.png", 56);
        setCreatureType(Creature.ANIMAL); 
    }

}
