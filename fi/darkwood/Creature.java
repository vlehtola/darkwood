/*
 * Creature.java
 *
 * Created on May 4, 2007, 11:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.room.CityRoom;
import fi.darkwood.room.Room;
import fi.darkwood.room.ShopRoom;
import fi.darkwood.room.StatRoom;
import fi.darkwood.ui.component.CombatText;
import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

/**
 * Basic class that represents all living things; monster and players
 *
 * @author Tommi Laukkanen
 */
public abstract class Creature extends Thing {

    /** Creates a new instance of Creature */
    public Creature(String name, String image, int level, int frameWidth) {
        super(name, image, frameWidth);

        this.level = level;
        this.random = new Random(System.currentTimeMillis());
        this.fractionalHP = 0;

    }
    public final static int[] CREATURE_TYPE_FLAGS = {1, 2, 4, 8, 16};
    public final static int ANIMAL = 0, UNDEAD = 1, HUMANOID = 2, DEMON = 3, CONSTRUCT = 4;
    public int creatureType = 0;
    public Random random;
    public int level;
    public int health;
    public int maxHealth;
    public boolean dead;
    public int mana;
    public int maxMana;
    public int strength;
    public int dexterity;
    public int constitution;
    public int willpower;
    public int stunRounds;
    public int[] abilityCooldowns = new int[4];
    public double attackCoolDownRounds;
    private int buffTickCounter = 0;
    public int framesLeftToResetAnimation = 0;
    public int frameOffset = 0;
    public int experience;
    public int money;
    public int armorClass;
    public Vector textEvents = new Vector(); // texts scrolling upwards from this creature (damage etc.)
    public Vector graphicsEffects = new Vector(); // ability effects etc. affecting this creature
    public Vector buffs = new Vector(); // buffs, debuffs, dots etc. affecting this creature
    private float fractionalHP = 0;

    public void setCreatureType(int type) {
        creatureType = creatureType | Creature.CREATURE_TYPE_FLAGS[type];
    }

    public boolean isType(int type) {
        if ((creatureType & CREATURE_TYPE_FLAGS[type]) != 0) {
            return true;
        }
        return false;
    }

    public double getPersonalAttackCooldown() {
        return GameConstants.ticksPerRound - dexterity * GameConstants.attackDexHaste;
    }
    public boolean isAlive() {
        return !dead;
    }

    public boolean isStunned() {
        return stunRounds > 0;
    }

    public int getAbilityCooldown(int index) {
        return abilityCooldowns[index];
    }

    /**
     * Check if this creature is ready to use ability (no cooldown on ability, not stunned)
     * @param abilitySlot
     * @return
     */
    public boolean isReadyToAct(int abilitySlot) {
        return abilityCooldowns[abilitySlot] <= 0 && stunRounds <= 0 && isAlive();
    }

    public boolean isReadyToAttack() {
        return attackCoolDownRounds <= 0 && stunRounds <= 0 && isAlive();
    }

    int regenDelay = GameConstants.HEAL_TICK_DELAY;
    public void regen() {
        regenDelay -= 1;
        if (regenDelay > 0) { return; }
        else { regenDelay = GameConstants.HEAL_TICK_DELAY; }

        if (health < maxHealth) {
            // how many heal ticks are required to get to max health from 0 is defined in GameConstants.HEAL_TICKS_TO_HEAL_TO_MAX
            health = health + maxHealth / GameConstants.HEAL_TICKS_TO_HEAL_HEALTH_TO_MAX;
            if (health > maxHealth) {
                health = maxHealth;
            }
        }
        if (mana < maxMana) {
            // how many heal ticks are required to get to max mana from 0 is defined in GameConstants.HEAL_TICKS_TO_HEAL_TO_MAX
            mana = mana + maxMana / GameConstants.HEAL_TICKS_TO_HEAL_MANA_TO_MAX;
            if (mana > maxMana) {
                mana = maxMana;
            }
        }

        // revive in city
        if (dead && health > 0) {
            dead = false;
        }
    }

    public void addHealthChange(String msg) {
        textEvents.addElement(new CombatText(this, msg, System.currentTimeMillis(), DarkwoodGraphics.FONT_ARIAL10));
    }

    public void addAbilityEffect(AbilityVisualEffect effect) {
        effect.reset(); // reset ability effect object before adding it to creature
        graphicsEffects.addElement(effect);
    }

    public void reduceMana(int amount) {
        mana -= amount;
        Game.party.sendToAll("reducemana:" + this.getId() + "/" + amount);
    }
    
    public void harm(int amount) {
        harm(amount, false);
    }

    /**
     * Harm this creature
     * @param amount amount of damage incoming
     * @param isEffectDamage Damage is coming from ability effect, so it will be drawn in yellow instead of white
     */
    public void harm(int amount, boolean isEffectDamage) {

        health -= amount;

        if (health < 0) {
            this.bury();
        }

//        addHealthChange(amount + "");

        if (!isEffectDamage) {
            // add a white damage text if from normal hit
            textEvents.addElement(new CombatText(this, amount + "", System.currentTimeMillis(), DarkwoodGraphics.FONT_ARIAL10));
        } else {
            // add a yellow damage text if from ability effect
            textEvents.addElement(new CombatText(this, amount + "", System.currentTimeMillis(), DarkwoodGraphics.FONT_ARIAL10_YELLOW));
        }
        



        Game.party.sendToAll("damage:" + this.getId() + "/" + amount);

        //textEvents.addElement(new UIElement(this, amount+"",System.currentTimeMillis())) ;
    }

    /** Call this harm() if you want to do armor reduction properly.
     ** Keeps track of the one fractional HP. Returns the actual damage done.
     ** --Ville
     **/
    public int harm(float amount) {
        int value = (int) amount;
        fractionalHP += amount - (float) value;
        if (fractionalHP > 1) {
            value += 1;
            fractionalHP -= 1;
        }
        this.harm(value);
        return value;
    }

    public void heal(int amount) {
        if (this.dead && this instanceof Player == false) {
            // monsters cant be revived..
            return;
        }

        health += amount;

        if (health > maxHealth) {
            health = maxHealth;
        }
        textEvents.addElement(new CombatText(this, Local.get("combat.healed") + " " + amount + "!", System.currentTimeMillis(), DarkwoodGraphics.FONT_ARIAL10_GREEN));

        Game.party.sendToAll("heal:" + this.getId() + "/" + amount);


        if (this.dead && this instanceof Player && health > 0) {
            dead = false;
            textEvents.addElement(new CombatText(this, Local.get("combat.revived") + "", System.currentTimeMillis(), DarkwoodGraphics.FONT_ARIAL10_GREEN));
        }
    }

    public void stun(int rounds) {
        stunRounds += rounds;
    }

    public void addMoney(int amount) {
        money += amount;
//        textEvents.addElement(new CombatText(this, "gold " + amount, System.currentTimeMillis(), 0xFFD700));
    }

    /**
     * Signals this creature that it has died.
     * Awards exp and money to hostiles in the same room.
     * 
     */
    public void bury() {
        int hostileCount = 0;
        Enumeration e = this.room.getCreatures().elements();
        while (e.hasMoreElements()) {
            Creature creature = (Creature) e.nextElement();
            if (creature != this && isHostile(creature)) {

                creature.awardKill(this);
                hostileCount++;
            }
        }

        dead = true;  // I am dead! oh noes.
        //  this.room.removeThing(this);
    }

    public void moveTo(int roomId, Zone zone) {
        Room currentRoom = room;
        currentRoom.removeThing(this);
        Room moveToRoom = (Room) zone.rooms.elementAt(roomId);
        moveToRoom.addThing(this);


        // only print encounter message for the first player, return for party member auto moving
        if (this != Game.player) {
            return;
        }

        // print encounter message to messagelog
        MessageLog messageLog = MessageLog.getInstance();
        Enumeration e = this.room.getCreatures().elements();
        while (e.hasMoreElements()) {
            Creature creature = (Creature) e.nextElement();
            if (creature != this && isHostile(creature) && creature.isAlive()) {
                String message = Local.get("combat.encounter") + " " + creature.name + " [" + creature.level + "]";
                if (creature instanceof Monster && ((Monster) creature).isElite()) {
                    message = message + " ( " + Local.get("combat.encounter.elite") + ")";
                }
                messageLog.addMessage(message);
                System.out.println(creature.name + " id: " + creature.getId());
            }
        }

    }

    public boolean isHostileInRoom() {
        Enumeration e = this.room.getCreatures().elements();
        while (e.hasMoreElements()) {
            Creature creature = (Creature) e.nextElement();
            if (creature != this && isHostile(creature) && creature.isAlive()) {
                return true;
            }
        }
        return false;
    }

    /*    public void addActionCoolDown(int coolDown) {
    abilityCooldowns += coolDown;
    Game.party.sendToAll("actioncooldown:" + this.getId() + "/" + coolDown);
    } */
    public void addAbilityCooldown(double coolDownInRounds, int slot) {
        int coolDown = (int) (coolDownInRounds*GameConstants.ticksPerRound);
        abilityCooldowns[slot] += coolDown;
        Game.party.sendToAll("actioncooldown:" + this.getId() + "/" + (int) coolDownInRounds + "," + slot);

    }

    public void addAttackCoolDown(double coolDown) {
        attackCoolDownRounds += coolDown;
    }

    public void setFramesLeftToResetAnimation(int framesLeft) {
        framesLeftToResetAnimation = framesLeft;
    }

    /**
     * Frame offset is needed for paper doll eq drawing.
     * @param offset
     */
    public void setFrameOffset(int offset) {
        frameOffset = offset;
    }

    public int getDefense() {
        return 0;
    }

    public double getDamage() {
        // damage is calculated for players and monsters separately
        return 0;
    }

    public int getExperienceWorth() {
        int xp = GameConstants.monsterEXPConstant * level;
        // Awards more xp from elite mobs
        if(this instanceof Monster) {
            Monster mob = (Monster) this;
            if(mob.isElite())
                xp *= GameConstants.eliteEXPmultiplier;
        }
        return xp;
    }

    public int getExpRequiredForNextLevel() {
        int exp = GameConstants.levelCost(level);

        // if class is Mage, all levels cost 3 times more exp (tier 2 class)
        if (Game.player.characterClass == GameConstants.CLASS_MAGE) {
            exp = exp * 3;
        }
        return exp;
    }

    public boolean isHostile(Creature creature) {
        return this.hates(creature) || creature.hates(this);
    }

    public abstract int getId();

    public abstract boolean hates(Creature creature);

    public void resetCoolDowns() {
        stunRounds = 0;
        int[] zeroCooldowns = {0, 0, 0, 0};
        abilityCooldowns = zeroCooldowns;
        attackCoolDownRounds = 0;
    }

    public void tick() {
        // tick buffs every second
        if (buffTickCounter > 14) {
            checkBuffExpiration();
            buffTickCounter = 0;
        } else {
            buffTickCounter++;
        }

        if (this.room instanceof CityRoom ||
                this.room instanceof StatRoom ||
                this.room instanceof ShopRoom) {
            this.regen();
        }

        // only perform the rest if creature is alive
        if (!this.isAlive()) {
            return;
        }

        if (attackCoolDownRounds > 0) {
            attackCoolDownRounds--;
        }

        if (stunRounds > 0) {
            stunRounds--;
        }

        reduceCooldowns();

       
        // no need for this since clients tick cooldowns themselves
    /*    if (Game.player.equals(this) && Game.party.isLeader) {
        Game.party.sendToAll("reducecooldowns");
        } */

    }


    int i = 0;
    public void reduceCooldowns() {

        for (i = 0; i < 4; i++) {
            if (abilityCooldowns[i] > 0) {
                abilityCooldowns[i]--;
            }
        }
    }

    public void restartBuffs() {
        buffEnumeration = buffs.elements();
        while (buffEnumeration.hasMoreElements()) {
            buff = (Buff) buffEnumeration.nextElement();
            buff.applyEffect();
        }
    }

    public void addBuff(Buff buff) {
        buffs.addElement(buff);
    }
    // temp variables for checkBuffExpiration()
    private Enumeration buffEnumeration;
    private Buff buff;

    /**
     * Check if any buffs are expiring. Called each tick.
     */
    public void checkBuffExpiration() {
        buffEnumeration = buffs.elements();
        while (buffEnumeration.hasMoreElements()) {
            buff = (Buff) buffEnumeration.nextElement();
            if (buff.checkExpire()) {
                buffs.removeElement(buff);
            }
        }
    }

    public abstract void awardKill(Creature creature);
}
