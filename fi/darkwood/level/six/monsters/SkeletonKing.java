/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.six.monsters;

import fi.darkwood.Creature;
import fi.darkwood.Monster;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.room.Room;
import fi.darkwood.ui.component.MessageLog;
import fi.darkwood.util.StringUtils;
import fi.mirake.Local;
import java.util.Enumeration;

/**
 *
 * @author Ville
 */
public class SkeletonKing extends Monster {

    public SkeletonKing() {
        super(Local.get("Warlord"), "/images/monster/Skeleton chief 58x94 8 frames.png", 58);
        setCreatureType(super.UNDEAD);
    }

    public void combatSpecial(Creature target) {
        if (random.nextInt(100) < 30) {
            // Revives the other monster, if it has died
            Room room = this.room;
            Enumeration e = room.getCreatures().elements();
            while (e.hasMoreElements()) {
                Creature cre = (Creature) e.nextElement();
                if (cre instanceof Monster && !cre.equals(this)) {
                    // Found the other monster
                    if (cre.dead) {
                        cre.dead = false;
                        cre.health = cre.maxHealth;
                        cre.addAbilityEffect(new AbilityVisualEffect("/images/ability/effects/cleric/heal.png", 43, 9));



                        String msg = Local.get("warlordrevive");

                        // replace $1 in the localization string with target name
                        msg = StringUtils.replace(msg, "$1", cre.name);

                        MessageLog.getInstance().addMessage(msg);


                    }
                }
            }
        }
    }
}
