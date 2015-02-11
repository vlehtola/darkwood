/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.two.monsters;

import fi.darkwood.Monster;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class Bat extends Monster {

    public Bat() {
        super(Local.get("bat"), "/images/monster/bat.png", 37);
        setCreatureType(super.ANIMAL);    

    }
}
