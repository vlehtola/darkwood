/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.ability.cleric;

import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ability.HealSpell;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class Heal extends HealSpell {
    public Heal() {
        super(Local.get("Heal"), "/images/ability/icons/cleric/heal.png", 1);
        setTargetVisualEffect(new AbilityVisualEffect("/images/ability/effects/cleric/heal.png", 43, 9));
    }

    public int getManaCost() {
        return abilityLevel * 7;
    }
    
    public int getHealEffect(int willpower) {
        return random.nextInt(willpower/2)+willpower/2+abilityLevel*5;
    }

    public double getCooldownInRounds() {
        return 2;
    }
    
}
