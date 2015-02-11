/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.cleric;

import fi.darkwood.Buff;
import fi.darkwood.Creature;
import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;


/**
 * @author Teemu
 */

public class HealingAuraBuff extends Buff {

    public HealingAuraBuff(Creature source, Creature target, long expireTime) {
        super(source, target, expireTime);
    }

    public void applyEffect() {
       
    }
 
    public void expireEffect() {
       
        MessageLog.getInstance().addMessage(mTarget.name + Local.get("Healing Aura fades."));
    }

    public void abilityTick() {
        mTarget.heal(10);
        
    }
}
