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
public class Mummy extends Monster {

    public Mummy() {
        super(Local.get("mummy"), "/images/monster/Mummy Master 43x51 13 frames.png", 43);
        setCreatureType(super.UNDEAD);   
    }
}
