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
public class SpiderQueen extends Monster {
    public SpiderQueen() {
        super(Local.get("spider queen"), "/images/monster/SpiderQueen 165 x 76 7 frames.png", 165);
        setCreatureType(Creature.ANIMAL); 
    }

}
