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
import fi.darkwood.Monster;
import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;
import java.util.Enumeration;

/**
 *
 * @author Teemu Kivim�ki
 */
public abstract class Debuff extends Ability {

    /** Creates a new instance of Buff */
    public Debuff(String name, String image, int level) {
        super(name, image, level);
    }

    public boolean effect(Creature invoker) {
        if (!invoker.isReadyToAct(this.getAbilitySlot())) {

            return false;
        }

        Creature target = null;
        Creature source = invoker;
        Enumeration e = Game.player.room.getCreatures().elements();
        while (e.hasMoreElements()) {
            Creature creature = (Creature) e.nextElement();
            if (creature instanceof Monster && creature.isAlive()) {
                target = creature;
                break;
            }
        }

        if (target == null) {
            System.out.println("No target for debuff, returning");
            return false;
        }

        if (source.mana < getManaCost()) {

            // only print to messagelog if invoker is this player (not a party member)
            if (invoker.getId() == Game.player.getId()) {
                MessageLog.getInstance().addMessage(Local.get("Not enough energy!"));
            }
            return false;
        }

        source.reduceMana(getManaCost());
        Game.player.addAbilityCooldown(getCooldownInRounds(), this.getAbilitySlot());

        target.addAbilityEffect(getTargetVisualEffect());
        Game.party.sendVisualEffect(target.getId(), getTargetVisualEffect());


        start(source, target, System.currentTimeMillis() + getDurationMillis(), abilityLevel);
        return true;
    }

    public abstract void start(Creature source, Creature target, long expireDate, int abilityLevel);

    public String getDescription() {
        return "Mana cost: " + getManaCost();
    }

    public abstract int getManaCost();

    public abstract int getDurationMillis();
}
