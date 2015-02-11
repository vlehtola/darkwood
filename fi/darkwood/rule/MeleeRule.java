/*
 * MeleeRule.java
 *
 * Created on May 5, 2007, 8:26 AM
 *
 *
 *
 */
package fi.darkwood.rule;

import fi.darkwood.Creature;
import fi.darkwood.GameConstants;
import fi.darkwood.Humanoid;
import fi.darkwood.Monster;
import fi.darkwood.Player;
import fi.darkwood.equipment.Weapon;
import fi.mirake.Local;
import java.util.Random;

/**
 *
 * @author Tommi Laukkanen
 *
 * Edited by Ville Lehtola 19.05.2007
 */
public class MeleeRule {

    private static Random random = new Random(System.currentTimeMillis());

    public static int evaluate(Creature source, Creature target) {

        source.addAttackCoolDown(source.getPersonalAttackCooldown());
        int returnValue = 0;
        int diceValue = random.nextInt(100);
        // 90% chance to hit on equal level. Only level affects this.
        int hitRange = 90 + (source.level - target.level) * 5;
        if (diceValue < hitRange) {
            // Hit
            double damage = getMeleeDamage(source, true);
            // Here we switch to doubles to make the armor reduction right
            double armorAbsorbChance = ((double) target.getDefense()) / GameConstants.armorClassCoef;

            // if source is of higher level, then armor is lowered.
            // higher impact from level difference can be implemented
            //int levelDiff = 0; // (source.level - target.level) * GameConstants.armorClassConstant;
            armorAbsorbChance /= (source.level + GameConstants.armorClassConstant);
            diceValue = random.nextInt(100);
            if (diceValue < armorAbsorbChance) {
                damage = 0;
                target.addHealthChange(Local.get("combat.deflected"));
                returnValue = -2;
            } else if (diceValue < GameConstants.baseCritChance) {
                // Critical hit!!
                damage *= 2;
                target.addHealthChange(Local.get("combat.crit"));
            }
            System.out.println("Meleedmg: " + damage + " (aaC: " + armorAbsorbChance + ")");

            if (returnValue != -2) {
                returnValue = target.harm((float) damage);
            }

        } else {
            // Miss
            target.addHealthChange(Local.get("combat.missed"));
            returnValue = -1;
        }
        
        // Monsters migt have special abilities
        if(source instanceof Monster) {
            Monster mon = (Monster) source;
            mon.combatSpecial(target);
        }
        return returnValue;
    }

    /**
     * 
     * @param Creature source
     * @param useRandom, 1: get a realization from the uniform distribution 0: use average
     * @return
     */
    public static double getMeleeDamage(Creature source, boolean useRandom) {

        double weaponDamage = source.level;
        // Weapon damage should correspond to wielder's level
        if (source instanceof Humanoid) { // monsters are not instanceof Humanoids. they might have Creature.Humanoid flag
            Weapon weapon = (Weapon) ((Humanoid) source).equipmentSlots[GameConstants.SLOT_RIGHT_HAND];
            if (weapon != null) {
                //Logger.getInstance().debug("DMG: "+weapon.maxDamage+" - "+weapon.minDamage);
                if (useRandom) {
                    weaponDamage = weapon.minDamage + random.nextInt(weapon.maxDamage - weapon.minDamage + 1);
                } else {
                    weaponDamage = (weapon.minDamage + weapon.maxDamage) / 2.0;
                }
            } else {
                // If a player does not have a weapon
                weaponDamage = 1;
            }
        }

        // Strength increases base damage; 30% from str, 70% from weapon
        double damage = getStrengthDamageModifier(source) + weaponDamage;       

        /*
        if (source instanceof Monster) {
        damage *= GameConstants.monsterDPSConstant;
        } else {
        damage *= GameConstants.playerDPSConstant;
        }
         */
        return damage;
    }
    public static double getStrengthDamageModifier(Creature source) {
        double a = GameConstants.statDPSpercent;
        // statDPSpercent is used only here, and not for weapon damage. this leads to the following formula
        double strmod = source.strength * a / (1-a);
         if(source instanceof Player) {
            Player pl = (Player) source;
            strmod *= GameConstants.classMeleeCoef(pl.characterClass);
        } else if(source instanceof Monster) {
            strmod *= 0.3; // monsters grow up slower than players
        }
        
        return strmod;
    }
}


