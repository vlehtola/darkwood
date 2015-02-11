/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.six.monsters;

import fi.darkwood.Creature;
import fi.darkwood.Monster;
import fi.darkwood.Player;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.room.Room;
import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;
import java.util.Enumeration;

/**
 * Abomination is a boss, who adds a 5 round cooldown to each player ability at the start of combat.
 *
 * @author Ville
 */
public class Abomination extends Monster {

    private boolean weaknessCast = false;

    public Abomination() {
        super(Local.get("abomination"), "/images/monster/Abomination master 71 x 87 10 frames.png", 71);
        setCreatureType(super.UNDEAD);

    }

    public void combatSpecial(Creature c) {
        int i;
        // Casts weakness on player
        if (!weaknessCast) {
            weaknessCast = true;
            Room room = this.room;
            Enumeration e = room.getCreatures().elements();
            MessageLog.getInstance().addMessage(Local.get("abominationweakness"));
            while (e.hasMoreElements()) {
                Creature cre = (Creature) e.nextElement();
                if (cre instanceof Player) {
                    // Found a player, put 5 round cooldowns on his abilities.
                    for(i=0;i < 4;i++)
                        cre.addAbilityCooldown(5, i);
                    cre.addAbilityEffect(new AbilityVisualEffect("/images/ability/effects/cleric/weakness.png", 61, 7));
                }
            }
        }
    }
}
