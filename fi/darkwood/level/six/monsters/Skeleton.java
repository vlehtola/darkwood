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
public class Skeleton extends Monster {

    public Skeleton() {
        super(Local.get("skeleton"), "/images/monster/skeleton.png", 41);
        setCreatureType(super.UNDEAD);   
    }
}
