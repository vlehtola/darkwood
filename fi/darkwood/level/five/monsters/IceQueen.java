/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.five.monsters;
import fi.darkwood.Creature;
import fi.darkwood.Monster;

/**
 *
 * @author Ville
 */
public class IceQueen extends Monster {
    public IceQueen() {
         super("Esmeralda", "/images/monster/IceQueen master 77x68 21 frame.png", 77);
        setCreatureType(Creature.HUMANOID);
    }
}

