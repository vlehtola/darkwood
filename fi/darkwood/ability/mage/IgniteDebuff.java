/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.mage;

import fi.darkwood.Buff;
import fi.darkwood.Creature;


/*
 * @author Ville Lehtola
 */

public class IgniteDebuff extends Buff {

    public IgniteDebuff(Creature source, Creature target, long expireTime, int abilityLevel) {
        super(source, target, expireTime, abilityLevel);
    }

    public void applyEffect() {
        //Logger.getInstance().debug("Ignite: " + mTarget.strength);
 
       }
    public void expireEffect() {
        //Logger.getInstance().debug("Ignite end: " + mTarget.strength);
        //MessageLog.getInstance().addMessage(mTarget.name + "'s Enrage fades.");
    }

    public void abilityTick() {
        if(mTarget.isAlive()) {
            mTarget.harm(mSource.willpower /5 + abilityLevel*5);
        }
    }
}
