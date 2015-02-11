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
public class SpiderSmall extends Monster {
    public SpiderSmall() {
        super(Local.get("small spider"), "/images/monster/SpiderSmall 54x20 8 frames.png", 54);
        setCreatureType(Creature.ANIMAL); 
    }

}
