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
public class Gargoyle extends Monster {

    public Gargoyle() {
        super(Local.get("gargoyle"), "/images/monster/gargoyle.png", 73);
        setCreatureType(super.UNDEAD);   
    }
}
