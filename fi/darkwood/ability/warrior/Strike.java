/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.warrior;

import fi.darkwood.Creature;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ability.DirectDamageAbility;
import fi.darkwood.rule.MeleeRule;
import fi.mirake.Local;

/**
 *
 * @author Administrator
 */
public class Strike extends DirectDamageAbility {

    public Strike() {
        super(Local.get("strike"), "/images/ability/icons/power_strike.png", 3);
        setTargetVisualEffect(new AbilityVisualEffect("/images/ability/effects/strike.png", 61, 9));

    }
    public int getAbilityRank() {
        return 1;
    }
    public int getManaCost(Creature source, Creature target) {
        return getDamage(source, target);
    }

    public int getDamage(Creature source, Creature target) {
        int damage = (int) MeleeRule.getMeleeDamage(source, true);
        damage += 2*abilityLevel;
        return damage;
    }

    public double getCooldownInRounds() {
        return 2;
    }
}
