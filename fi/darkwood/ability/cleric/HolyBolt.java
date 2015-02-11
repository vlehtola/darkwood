/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.cleric;

import fi.darkwood.Creature;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ability.DirectDamageAbility;
import fi.mirake.Local;

/**
 *
 * @author Teemu
 */
public class HolyBolt extends DirectDamageAbility {

    public HolyBolt() {
        super(Local.get("Holy Bolt"), "/images/ability/icons/cleric/holy_bolt.png", 1);
        setTargetVisualEffect(new AbilityVisualEffect("/images/ability/effects/cleric/holy_bolt_target.png", 51, 6));
        setSelfVisualEffect(new AbilityVisualEffect("/images/ability/effects/cleric/holy_bolt_caster.png", 44, 5));

    }
    public int getAbilityRank() {
        return 2;
    }
    public int getManaCost(Creature source, Creature target) {
        return getDamage(source, target);
    }
   public int getDamage(Creature source, Creature target) {
        int damage = abilityLevel*5 + source.willpower;
        return damage;
    }
    public double getCooldownInRounds() {
        return 2;
    }
}
