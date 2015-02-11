/*
 * Skeleton.java
 *
 * Created on December 28, 2007, 6:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.level.one.monsters;

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
 * @author Ville Lehtola
 */
public class Witch extends Monster {

    public Witch() {
        super(Local.get("witch"), "/images/monster/witch.png", 38);
        setCreatureType(super.HUMANOID);
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
                    cre.harm(this.level * 3);
                    cre.addAbilityEffect(new AbilityVisualEffect("/images/ability/effects/cleric/holy_bolt_target.png", 51, 6));
                    this.addAbilityEffect(new AbilityVisualEffect("/images/ability/effects/cleric/holy_bolt_caster.png", 44, 5));

                    MessageLog.getInstance().addMessage(Local.get("witch,msg")+" " + cre.name + "!");

                }
            }
        }
    }
}
