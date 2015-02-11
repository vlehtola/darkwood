/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ability.warrior;

import fi.darkwood.Creature;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ability.SelfBuff;
import fi.mirake.Local;

/**
 *
 * @author Teemu
 */
public class ShieldWall extends SelfBuff {

    public ShieldWall() {
        super(Local.get("shieldwall"), "/images/ability/icons/shield_wall.png", 1);
        setTargetVisualEffect(new AbilityVisualEffect("/images/ability/effects/shield_wall.png", 47, 5));
    }

    public int getAbilityRank() {
        return 3;
    }

    public void start(Creature self, long expireDate) {
        self.addBuff(new ShieldWallBuff(self, self, expireDate, this.abilityLevel));
    }

    public int getManaCost() {
        return abilityLevel * 10;
    }

    public int getDurationMillis() {
        return abilityLevel * 10000;
    }

    public String getDescription() {
        return Local.get("shieldwall.desc");
    }

    public double getCooldownInRounds() {
        return 5;
    }
}
