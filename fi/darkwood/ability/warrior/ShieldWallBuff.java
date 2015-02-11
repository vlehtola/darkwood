/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.warrior;

import fi.darkwood.Buff;
import fi.darkwood.Creature;
import fi.darkwood.Humanoid;

/**
 *
 * @author Ville
 */
public class ShieldWallBuff extends Buff {

    public ShieldWallBuff(Creature source, Creature target, long expireTime, int abilityLevel) {
        super(source, target, expireTime);
        this.abilityLevel = abilityLevel;
    }

    public void applyEffect() {
        Humanoid buffed = (Humanoid) mTarget;

        System.out.println("Buffed 2");
        if(buffed == null) {
            System.out.println("Buffed 2");
        }

        buffed.defence += this.abilityLevel * 5 + buffed.dexterity;
    }

    public void expireEffect() {
        Humanoid buffed = (Humanoid) mTarget;
        buffed.updateDefense();
    }

    public void abilityTick() {
    }
}
