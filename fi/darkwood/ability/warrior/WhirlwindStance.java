/*
 * NOT IN USE! ++Ville
 *
 */
package fi.darkwood.ability.warrior;

import fi.darkwood.Creature;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ability.SelfBuff;

/**
 *
 * @author Administrator
 */
public class WhirlwindStance extends SelfBuff {

    public WhirlwindStance() {
        super("Whirlwind stance", "/images/ability/icons/whirlwind_stance.png", 1);
        setTargetVisualEffect(new AbilityVisualEffect("/images/ability/effects/whirlwind_stance.png", 44, 8));

    }
    public int getAbilityRank() {
        return 4;
    }
    public void start(Creature self, long expireDate) {
        self.addBuff(new WhirlwindStanceBuff(self, self, expireDate, this.abilityLevel));
    }

    public int getManaCost() {
        return abilityLevel * 5;
    }
    
    public int getDurationMillis() {
        return 40*1000;
    }

    public String getDescription() {
        return "Whirlwind stance";
    }

    public double getCooldownInRounds() {
        return 10;
    }
}
