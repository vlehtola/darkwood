/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.four.monsters;
import fi.darkwood.Monster;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class BigBat extends Monster {

    public BigBat() {
        super(Local.get("big bat"), "/images/monster/bat.png", 37);
        setCreatureType(super.ANIMAL);

    }
}
