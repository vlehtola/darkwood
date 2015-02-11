/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.mage;

import fi.darkwood.Buff;
import fi.darkwood.Creature;
import fi.darkwood.Logger;


/*
 * @author Ville Lehtola
 */

public class PolymorphDebuff extends Buff {

    public PolymorphDebuff(Creature source, Creature target, long expireTime, int abilityLevel) {
        super(source, target, expireTime, abilityLevel);
    }

    public void applyEffect() {
        Logger.getInstance().debug("Polymorph: " + mTarget.strength);
        long duration = mExpireDate - System.currentTimeMillis();
        mTarget.addAttackCoolDown(duration);
        mTarget.stunRounds = (int) duration;
 
       }
    public void expireEffect() {
        Logger.getInstance().debug("Polymorph end: " + mTarget.strength);
        //MessageLog.getInstance().addMessage(mTarget.name + "'s Enrage fades.");
        mTarget.resetCoolDowns();
    }

    public void abilityTick() {
    }
}
