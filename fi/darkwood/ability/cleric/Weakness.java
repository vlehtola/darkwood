/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.cleric;

import fi.darkwood.Creature;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ability.Debuff;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class Weakness extends Debuff {

    public Weakness() {
        super(Local.get("Weakness"), "/images/ability/icons/cleric/weakness.png", 1);
        setTargetVisualEffect(new AbilityVisualEffect("/images/ability/effects/cleric/weakness.png", 61, 7));
    }
    public int getAbilityRank() {
        return 3;
    }
    public void start(Creature source, Creature target, long expireDate, int abilityLevel) {
        target.addBuff(new WeaknessDebuff(source, target, expireDate, abilityLevel));
    }

    public int getManaCost() {
        return abilityLevel * 10;
    }

    public int getDurationMillis() {
        return abilityLevel * 10000;
    }

    public double getCooldownInRounds() {
        return 5;
    }
    
}
