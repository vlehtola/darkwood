/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.cleric;

import fi.darkwood.Buff;
import fi.darkwood.Creature;
import fi.darkwood.Logger;
import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;


/**
 * @author Ville
 */

public class WeaknessDebuff extends Buff {

    public WeaknessDebuff(Creature source, Creature target, long expireTime, int abilityLevel) {
        super(source, target, expireTime, abilityLevel);
    }
    private int originalStr = 0;
    public void applyEffect() {
        originalStr = mTarget.strength;
        mTarget.strength = mTarget.strength*9/10 - 20;
        Logger.getInstance().debug("Weakness: " + mTarget.strength);
    }
 
    public void expireEffect() {
        mTarget.strength = originalStr;
        Logger.getInstance().debug("Weakness end: " + mTarget.strength);
        MessageLog.getInstance().addMessage(mTarget.name + Local.get("Weakness fades."));
    }

    public void abilityTick() {
    }
}
