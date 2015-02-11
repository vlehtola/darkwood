/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.cleric;

import fi.darkwood.Creature;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ability.SelfBuff;
import fi.mirake.Local;

/**
 *
 * @author Teemu
 */
public class HealingAura extends SelfBuff {

    public HealingAura() {
        super(Local.get("Healing Aura"), "/images/ability/icons/cleric/healing_aura.png", 1);
        setTargetVisualEffect(new AbilityVisualEffect("/images/ability/effects/cleric/healing_aura.png", 42, 8));
    }
    public int getAbilityRank() {
        return 4;
    }
    public void start(Creature self, long expireDate) {
        self.addBuff(new HealingAuraBuff(self, self, expireDate));
    }

    public int getManaCost() {
        return abilityLevel * 10;
    }

    public int getDurationMillis() {
        return 10000;
    }

    public String getDescription() {
        return Local.get("Heals yourself over time");
    }

    public double getCooldownInRounds() {
        return 3;
    }
}
