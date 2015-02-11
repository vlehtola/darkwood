/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.ability.warrior;

import fi.darkwood.Buff;
import fi.darkwood.Creature;
import fi.darkwood.Game;

/**
 *
 * @author Ville
 */
class WhirlwindStanceBuff extends Buff {

    int tickTimer;

    public WhirlwindStanceBuff(Creature source, Creature target, long expireDate, int abilityLevel) {
        super(source, target, expireDate);
        this.abilityLevel = abilityLevel;
    }

    public void applyEffect() {        
    }

    public void expireEffect() {
        //mSource.addActionCoolDown(10*15);
    }

    public void abilityTick() {
        // Reset cooldowns for more efficient combat
        mSource.resetCoolDowns();
      //  mSource.addActionCoolDown(15);
    }

}
