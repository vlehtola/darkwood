/*
 * HealSpell.java
 *
 * Created on May 9, 2007, 6:00 PM
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
import java.util.Enumeration;
import java.util.Random;

/**
 *
 * @author Teemu
 */
public abstract class HealSpell extends Ability {

    protected static Random random = new Random(System.currentTimeMillis());

    /** Creates a new instance of HealSpell 
     * 
     * @param name
     * @param image
     * @param abilityLevel
     */
    public HealSpell(String name, String image, int level) {
        super(name, image, level);
    }

    public int getAbilityRank() {
        return 1;
    }

    public boolean effect(Creature invoker) {

        if (!invoker.isReadyToAct(this.getAbilitySlot())) {
            return false;
        }
        if (invoker.mana < getManaCost()) {

            // only print to messagelog if invoker is this player (not a party member)
            if (invoker.getId() == Game.player.getId()) {
                MessageLog.getInstance().addMessage(Local.get("Not enough energy!"));
            }
            return false;
        }

        invoker.reduceMana(getManaCost());

        invoker.addAbilityCooldown(getCooldownInRounds(), this.getAbilitySlot());


        Creature target = null;
        Enumeration e = Game.player.room.getCreatures().elements();
        while (e.hasMoreElements()) {
            Creature creature = (Creature) e.nextElement();
            if (!Game.player.isHostile(creature) && creature.isAlive()) {
                target = creature;
                break;
            }
        }
        /*
        if (!Game.party.isActive() || Game.party.isLeader) {
        GameEffect(invoker, target);
        } */
        GameEffect(invoker, target);

        target.addAbilityEffect(getTargetVisualEffect());
        Game.party.sendVisualEffect(target.getId(), getTargetVisualEffect());

//        Game.party.sendToAll("effect:" + target.getId() + "," + getTargetVisualEffect().getImageFile() + "," + getTargetVisualEffect().getOriginalDuration() + "," + getTargetVisualEffect().frameWidth);
        return true;

    }

    private void GameEffect(Creature source, Creature target) {
        int healAmount = getHealEffect(source.willpower);
        target.heal(healAmount);

    }

    public String getDescription() {
        return "Heals around " + getHealEffect(Game.player.willpower) + " hit points for " + getManaCost() + " mana.";
    }

    public abstract int getHealEffect(int wpr);

    public abstract int getManaCost();
}
