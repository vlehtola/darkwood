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
import fi.darkwood.GameConstants;
import fi.darkwood.Monster;
import fi.darkwood.Player;
import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Tommi Laukkanen
 */
public abstract class AreaDamageAbility extends Ability {

    private static Random random = new Random(System.currentTimeMillis());
    private Vector targetList;

    /** Creates a new instance of AttackSkill */
    public AreaDamageAbility(String name, String image, int level) {
        super(name, image, level);
    }

    public abstract int getAbilityRank(); // from 1 to 4

    public boolean effect(Creature invoker) {
        targetList = new Vector();
        Creature target = null;
        Creature source = invoker;
        Enumeration e = Game.player.room.getCreatures().elements();
        while (e.hasMoreElements()) {
            Creature creature = (Creature) e.nextElement();
            if (creature instanceof Monster && creature.isAlive()) {
                targetList.addElement(creature);
            }
        }

        if (!source.isReadyToAct(this.getAbilitySlot())) {
            return false;
        }

        if (targetList.isEmpty()) {
            System.out.println("No target for ability!");
            return false;
        }

        if (source.mana < getManaCost(source, target)) {

            // only print to messagelog if invoker is this player (not a party member)
            if (invoker.getId() == Game.player.getId()) {
                MessageLog.getInstance().addMessage(Local.get("Not enough energy!"));
            }
            return false;
        }

        source.addAbilityCooldown(getCooldownInRounds(), getAbilitySlot());

        // Disabled this ++Ville
        //Display.getDisplay(Game.GameMidlet).vibrate(150);

        float coef = 1;

        // if the caster is a player, set casting animation
        if (source instanceof Player) {
            Player s = (Player) source;
            s.setAbilityInvokeAnimationFrameSequence();

            coef = (float) GameConstants.classAbilityCoef(s.characterClass);
        }

        source.reduceMana(getManaCost(source, target));
        Enumeration ee = targetList.elements();
        float damage;
        while (ee.hasMoreElements()) {
            target = (Creature) ee.nextElement();
            damage = (coef * getDamage(source, target));
            if (damage < 0) {
                // Absorbed
                damage = 0;
            }
            target.harm(damage);
        }

        // Use the first mob for the visuals
        target = (Creature) targetList.firstElement();

        target.addAbilityEffect(getTargetVisualEffect());
        Game.party.sendVisualEffect(target.getId(), getTargetVisualEffect());


        if (getSelfVisualEffect() != null) {
            source.addAbilityEffect(getSelfVisualEffect());
            Game.party.sendVisualEffect(source.getId(), getSelfVisualEffect());

        }

        return true;

    }

    public String getDescription() {
        Creature self = (Creature) Game.player;
        return Local.get("areadamage.inflict1")+" "+ getDamage(self, self) +" "+Local.get("areadamage.inflict2")+" "+getManaCost(self, self) + " "+ Local.get("areadamage.inflict3");
    }

    public abstract int getDamage(Creature source, Creature target);

    public abstract int getManaCost(Creature source, Creature target);
}
