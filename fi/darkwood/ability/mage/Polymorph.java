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
public class Polymorph extends Debuff {

    public Polymorph() {
        super(Local.get("Polymorph"), "/images/ability/icons/mage/polymorph.png", 1);
        AbilityVisualEffect av = new AbilityVisualEffect("/images/ability/effects/mage/Polymorph to sheep master 50x46 5frames.png", 50, 5);
        av.matchImageBottom = true;
        setTargetVisualEffect(av);
        
    }
    public int getAbilityRank() {
        return 3;
    }
    public int getManaCost() {
        return abilityLevel * 5;
    }

    public void start(Creature self, Creature target, long expireDate, int abilityLevel) {
        // Target is the first monster, this is to be changed?
        target.addBuff(new PolymorphDebuff(self, target, expireDate, abilityLevel));
    }
     public int getDurationMillis() {
        return abilityLevel * 10000;
    }

    public double getCooldownInRounds() {
       return 5;
    }
}
