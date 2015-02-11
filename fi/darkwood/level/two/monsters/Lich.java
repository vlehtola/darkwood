/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.two.monsters;

import fi.darkwood.Creature;
import fi.darkwood.Monster;
import fi.darkwood.Player;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.room.Room;
import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;
import java.util.Enumeration;

/**
 *
 * @author Ville
 */
public class Lich extends Monster {

    public Lich() {
        super(Local.get("Lich"), "/images/monster/lich.png", 61);
        setCreatureType(super.UNDEAD);
    }

    public void combatSpecial(Creature target) {
        if (random.nextInt(100) < 20) {
            // Cast fireball at a player
            Room room = this.room;
            Enumeration e = room.getCreatures().elements();
            while (e.hasMoreElements()) {
                Creature cre = (Creature) e.nextElement();
                if (cre instanceof Player && !cre.dead) {
                    // Found a player
                    cre.harm(this.level * 5);
                    cre.addAbilityEffect(new AbilityVisualEffect("/images/ability/effects/mage/Fireball master 67x62 9frames.png", 67, 9));
                    MessageLog.getInstance().addMessage(Local.get("Lich.attack") +" " + cre.name + "!");

                }
            }
        }
    }
}
