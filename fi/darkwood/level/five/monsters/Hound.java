/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.five.monsters;
import fi.darkwood.Monster;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */

public class Hound extends Monster {
    /** Creates a new instance of Wolf */
    public Hound() {
        super(Local.get("hound"), "/images/monster/wolf/big_wolf.png", 60);
        setCreatureType(super.ANIMAL);
    }

}
