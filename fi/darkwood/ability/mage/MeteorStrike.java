/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.mage;

import fi.darkwood.Creature;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ability.AreaDamageAbility;
import fi.mirake.Local;

/**
 *
 * @author Ville Lehtola
 */
public class MeteorStrike extends AreaDamageAbility {

    public MeteorStrike() {
        super(Local.get("Meteor strike"), "/images/ability/icons/mage/meteor_strike.png", 1);
        AbilityVisualEffect av = new AbilityVisualEffect("/images/ability/effects/mage/Meteor master 64x144 9frames.png", 64, 9);
        setTargetVisualEffect(av);
        av.matchImageBottom = true;
    }

    public int getAbilityRank() {
        return 4;
    }
    public int getManaCost(Creature source, Creature target) {
        return getDamage(source, target)*2;
    }

    public int getDamage(Creature source, Creature target) {
        int damage = abilityLevel*30 + source.willpower*2;
        return damage;
    }

    public double getCooldownInRounds() {
       return 5;
    }
}
