/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.six.monsters;

import fi.darkwood.Monster;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class StoneGolem extends Monster {

    public StoneGolem() {
        super(Local.get("stone golem"), "/images/monster/Stone Golem master 25x88 20 frames.png", 25);
        setCreatureType(super.CONSTRUCT);
    }
}
