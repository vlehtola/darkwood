/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.mage;

import fi.darkwood.Creature;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ability.Debuff;
import fi.mirake.Local;

/**
 *
 * @author Ville Lehtola
 */
public class Ignite extends Debuff {

    public Ignite() {
        super(Local.get("Ignite"), "/images/ability/icons/mage/ignite.png", 1);
        AbilityVisualEffect av = new AbilityVisualEffect("/images/ability/effects/mage/Master Ignite 26x85 10 frames.png", 26, 10);
        setTargetVisualEffect(av);
        av.matchImageBottom = true;
    }

    public int getAbilityRank() {
        return 2;
    }
    public int getManaCost() {
        return abilityLevel * 5;
    }

    public void start(Creature self, Creature target, long expireDate, int abilityLevel) {
        target.addBuff(new IgniteDebuff(self, target, expireDate, abilityLevel));
    }
     public int getDurationMillis() {
        return abilityLevel * 1000;
    }

    public double getCooldownInRounds() {
        return 3;
    }
}
