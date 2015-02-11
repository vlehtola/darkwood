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
import fi.darkwood.Player;
import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;
import java.util.Enumeration;
import java.util.Random;

/**
 *s
 * @author Tommi Laukkanen
 */
public abstract class DirectDamageAbility extends Ability {

    private static Random random = new Random(System.currentTimeMillis());
    public int test;

    /** Creates a new instance of AttackSkill */
    public DirectDamageAbility(String name, String image, int level) {
        super(name, image, level);
    }

    public abstract int getAbilityRank(); // from 1 to 4

    public boolean effect(Creature invoker) {
        Creature target = null;
        Creature source = invoker;
        Enumeration e = Game.player.room.getCreatures().elements();
        while (e.hasMoreElements()) {
            Creature creature = (Creature) e.nextElement();
//            if (creature != Game.player && Game.player.isHostile(creature) && creature.isAlive()) {
            if (creature instanceof Monster && creature.isAlive()) {
                target = creature;
                break;
            }
        }

        if (!source.isReadyToAct(this.getAbilitySlot())) {

            return false;
        }

        if (target == null) {
            System.out.println("No target for strike!");
            return false;
        }

        if (source.mana < getManaCost(source, target)) {
            //source.textEvents.addElement(new CombatText(source, "Not enough energy!", System.currentTimeMillis(), 0xFFFFFF));

            // only print to messagelog if invoker is this player (not a party member)
            if (invoker.getId() == Game.player.getId()) {
                MessageLog.getInstance().addMessage(Local.get("Not enough energy!"));
            }

            return false;
        }

        source.addAbilityCooldown(getCooldownInRounds(), this.getAbilitySlot());

        // Disabled this ++Ville
        //Display.getDisplay(Game.gameMidlet).vibrate(150);  

        int damage = getDamage(source, target);

        // if the caster is a player, set casting animation
        if (source instanceof Player) {
            Player s = (Player) source;
            s.setAbilityInvokeAnimationFrameSequence();

            // Disabled this, since defined damage and cooldowns are anyway bound to overrule this. ++Ville
            //damage *= GameConstants.classAbilityCoef(s.characterClass);
        }


        source.reduceMana(getManaCost(source, target));
        if (damage < 0) {
            // Absorbed
            damage = 0;
        }
        
        target.harm(damage, true);

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

        //return "Inflicts around "+getDamage(self,self)+" damage to one opponent for "+getManaCost(self,self)+" mana.";
    }

    public abstract int getDamage(Creature source, Creature target);

    public abstract int getManaCost(Creature source, Creature target);

    public abstract double getCooldownInRounds();
}
