/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.five.monsters;

import fi.darkwood.Creature;
import fi.darkwood.Monster;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class BarbarianBoss extends Monster {

    private boolean specialUsed = false;

    public BarbarianBoss() {
        super(Local.get("barbarian boss"), "/images/monster/BarbarianBoss 39x72 7frames.png", 39);
        setCreatureType(Creature.HUMANOID);
    }

    public void combatSpecial(Creature target) {
        if (!specialUsed) {
            this.addAbilityEffect(new AbilityVisualEffect("/images/ability/effects/enrage.png", 44, 11));
            this.specialUsed = true;
        }
    }
}
