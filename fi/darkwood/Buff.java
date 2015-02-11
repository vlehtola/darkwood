/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood;

import fi.darkwood.*;


/**
 * Buff is a generic effect affecting creatures in the game.
 * Each creature has a buffs Vector holding all buff currently affecting it
 * Buff expiration is checked by the game every once in a while and when it
 * expires, expireEffect() is called
 * 
 * @author Teemu
 */
public abstract class Buff {

    public long mExpireDate;
    public Creature mSource;
    public Creature mTarget;
    public int abilityLevel;
    
    public Buff(Creature source, Creature target, long expireDate) {
        mSource = source;
        mTarget = target;
        mExpireDate = expireDate;
        applyEffect();
        
    }
    public Buff(Creature source, Creature target, long expireDate, int abilityLevel) {
        mSource = source;
        mTarget = target;
        mExpireDate = expireDate;
        this.abilityLevel = abilityLevel;
        applyEffect();

    }
    public boolean checkExpire() {
        abilityTick();
        if (mExpireDate < System.currentTimeMillis()) {
            expireEffect();
            return true;
        }
        return false;
    }
    
    public abstract void applyEffect();

    public abstract void expireEffect();

    public abstract void abilityTick();
}
