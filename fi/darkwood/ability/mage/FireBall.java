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
public class FireBall extends AreaDamageAbility {

    public FireBall() {
        super(Local.get("Fire ball"), "/images/ability/icons/mage/fireball.png", 1);
        setTargetVisualEffect(new AbilityVisualEffect("/images/ability/effects/mage/Fireball master 67x62 9frames.png", 67, 9));

    }
    public int getAbilityRank() {
        return 1;
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
