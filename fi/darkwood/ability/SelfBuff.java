/*
 * AttackSkill.java
 *
 * Created on May 9, 2007, 5:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ability;

import fi.darkwood.Ability;
import fi.darkwood.Creature;
import fi.darkwood.Game;
import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;

/**
 *
 * @author Teemu Kivim�ki
 */
public abstract class SelfBuff extends Ability {

    /** Creates a new instance of Buff */
    public SelfBuff(String name, String image, int level) {
        super(name, image, level);
    }

    public boolean effect( Creature invoker) {
        if (!invoker.isReadyToAct(this.getAbilitySlot())) {
            return false;
        }

        Creature self = invoker;

        if (self.mana < getManaCost()) {
            

            // only print to messagelog if invoker is this player (not a party member)
            if (invoker.getId() == Game.player.getId()) {
                MessageLog.getInstance().addMessage(Local.get("Not enough energy!"));
            }
            return false;
        }

        Game.player.addAbilityCooldown(getCooldownInRounds(), this.getAbilitySlot());

        self.mana -= getManaCost();
        self.addAbilityEffect(getTargetVisualEffect());

        Game.party.sendVisualEffect(self.getId(), getTargetVisualEffect());

        start(self, System.currentTimeMillis() + getDurationMillis());
        return true;
    }

    public abstract void start(Creature self, long expireDate);

    public abstract String getDescription();

    public abstract int getManaCost();

    public abstract int getDurationMillis();
}
