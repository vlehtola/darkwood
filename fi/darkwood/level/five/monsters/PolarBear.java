/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.five.monsters;

import fi.darkwood.Creature;
import fi.darkwood.Monster;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class PolarBear extends Monster {
    public PolarBear() {
        super(Local.get("polar bear"), "/images/monster/Polar Bear master 115x55 12 frames.png", 115);
        setCreatureType(Creature.ANIMAL);
    }

}
