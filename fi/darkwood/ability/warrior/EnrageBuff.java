/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.warrior;

import fi.darkwood.Buff;
import fi.darkwood.Creature;
import fi.darkwood.Logger;
import fi.darkwood.ui.component.MessageLog;
import fi.darkwood.util.StringUtils;
import fi.mirake.Local;


/*
 * @author Teemu
 */
public class EnrageBuff extends Buff {

    public EnrageBuff(Creature source, Creature target, long expireTime, int abilityLevel) {
        super(source, target, expireTime, abilityLevel);
    }

    public void applyEffect() {
        // Here abilitylevel is still not set ++Ville
        mTarget.strength = mTarget.strength + 10 * abilityLevel;
        Logger.getInstance().debug("Enrage: " + mTarget.strength);

    }

    public void expireEffect() {
        mTarget.strength = mTarget.strength - 10 * abilityLevel;
        Logger.getInstance().debug("Enrage end: " + mTarget.strength);

        String msg = Local.get("enrage.fade");

        // replace $1 in the localization string with target name
        msg = StringUtils.replace(msg, "$1", mTarget.name);

        MessageLog.getInstance().addMessage(msg);

    }

    public void abilityTick() {
    }
}
