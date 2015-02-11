/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.warrior;

import fi.darkwood.Creature;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ability.SelfBuff;
import fi.darkwood.util.StringUtils;
import fi.mirake.Local;

/**
 *
 * @author Teemu
 */
public class Enrage extends SelfBuff {

    public Enrage() {
        super(Local.get("enrage"), "/images/ability/icons/enrage.png", 1);
        setTargetVisualEffect(new AbilityVisualEffect("/images/ability/effects/enrage.png", 44, 11));
    }

    public int getAbilityRank() {
        return 2;
    }

    public void start(Creature self, long expireDate) {
        self.addBuff(new EnrageBuff(self, self, expireDate, this.abilityLevel));
    }

    public int getManaCost() {
        return abilityLevel * 10;
    }

    public int getDurationMillis() {
        return abilityLevel * 10000;
    }

    public String getDescription() {

        String msg = Local.get("enrage.desc");

        // replace $1 in the localization string with target name
        msg = StringUtils.replace(msg, "$1", "" + getDurationMillis() / 1000);

        return msg;
    }

    public double getCooldownInRounds() {
        return 3;
    }
}
