/*
 *  4th ability for fighter. a better strike ++Ville
 * 
 */
package fi.darkwood.ability.warrior;

import fi.darkwood.Creature;
import fi.darkwood.Game;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ability.DirectDamageAbility;
import fi.darkwood.rule.MeleeRule;
import fi.mirake.Local;

/**
 *
 * @author Ville Lehtola
 */
public class MightyBlow extends DirectDamageAbility {

    public MightyBlow() {
        super(Local.get("mightyblow"), "/images/ability/icons/whirlwind_stance.png", 1);
        setTargetVisualEffect(new AbilityVisualEffect("/images/ability/effects/whirlwind_stance.png", 44, 8));

    }
    public int getAbilityRank() {
        return 4;
    }
    public int getManaCost(Creature source, Creature target) {
        return getDamage(source, target);
    }

     public int getDamage(Creature source, Creature target) {
         // Advancing strike helps also this ability
        int damage = (int) MeleeRule.getMeleeDamage(source, true);
        int strikeLevel = Game.player.activeAbilities[0].getLevel();
        damage = damage*2 + 5*abilityLevel + strikeLevel*2;
        return damage;
    }



    public double getCooldownInRounds() {
        return 6;
    }

}
