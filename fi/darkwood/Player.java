/*
 * Player.java
 *
 * Created on May 4, 2007, 8:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.darkwood.ability.AbilityTables;
import fi.darkwood.ability.cleric.Heal;
import fi.darkwood.ability.cleric.HealingAura;
import fi.darkwood.ability.cleric.HolyBolt;
import fi.darkwood.ability.cleric.Weakness;
import fi.darkwood.ability.mage.*;
import fi.darkwood.ui.component.CombatText;
import fi.darkwood.ability.warrior.Enrage;
import fi.darkwood.ability.warrior.MightyBlow;
import fi.darkwood.ability.warrior.ShieldWall;
import fi.darkwood.ability.warrior.Strike;
import fi.darkwood.initialEquipment.ClericChest;
import fi.darkwood.initialEquipment.MageChest;
import fi.darkwood.initialEquipment.WarriorChest;
import fi.darkwood.initialEquipment.WarriorWeapon;
import fi.darkwood.initialEquipment.WoodenClub;
import fi.darkwood.initialEquipment.PlainStaff;
import fi.darkwood.network.CharacterSerializer;
import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;
import java.util.Date;
import java.util.Vector;
import javax.microedition.lcdui.Display;

/**
 *
 * @author Tommi Laukkanen
 * @author Teemu Kivim�ki
 */
public class Player extends Humanoid {

    public int characterClass; // players character class
    private int globalCharacterId; // This id is for saving character at the server (unique amongs all characters in the game) Also used in party.
    public int totalExperience; // total experience the player has earned during the game (only used for statistics and rankings)
    public Ability[] activeAbilities = new Ability[4];
    public int healingPotions = 0;
    public int trainedStats[] = new int[GameConstants.NUMBER_OF_STATS];
    public Vector completedQuests = new Vector();
    public Quest currentQuest = null;
    public String dateWhenGainedLevel[] = new String[GameConstants.maxLevel + 2]; // [0] is charcreationdate
    private int deathPenaltyExpBuffer = 0;
    public String startZone = "";

    /** Creates a new instance of Player */
    public Player(String name, String description, String image, int level, int frameWidth, int charClass) {
        super(name, description, image, level, frameWidth);
        characterClass = charClass;

        for (int i = 0; i < GameConstants.NUMBER_OF_STATS; i++) {
            trainedStats[i] = 0;
        }
        setIdleAnimationFrameSequence();
    }

    /**
     * Restores mana when moving using movementView.
     */
    public void rest() {

        // return if mana is already at max
        if (mana >= maxMana) {
            return;
        }
        int regenAmount = (int) (maxMana * GameConstants.playerRestConst);

        if (mana + regenAmount > maxMana) {
            regenAmount = maxMana - mana;
        }

        mana += regenAmount;

        MessageLog.getInstance().addMessage("You regain " + regenAmount + " energy!");
    }

    /* Character sprites have both "idle" and "use ability" frames in them.
     * This function sets the correct frames to be played in the sprite to
     * display the idle animation.
     * This method is called when character ability animation is over
     * and we want to return to normal "idle" animation.
     */
    public void setIdleAnimationFrameSequence() {
        // default frame sequence, used by warrior
        int[] seq = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        // if class is cleric, use a different sequence
        if (this.characterClass == GameConstants.CLASS_CLERIC) {
            seq = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        }

        if (this.characterClass == GameConstants.CLASS_MAGE) {
            seq = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        }

        this.getSprite().setFrameSequence(seq);
        setFrameOffset(0);

    }

    public void setAbilityInvokeAnimationFrameSequence() {
        // default frame sequence, used by warrior
        int[] seq = {11, 12, 13, 14, 15};
        setFramesLeftToResetAnimation(6);
        setFrameOffset(11);

        // if class is cleric, use a different sequence
        if (this.characterClass == GameConstants.CLASS_CLERIC) {
            seq = new int[]{11, 12, 13, 14, 15, 16};
            setFramesLeftToResetAnimation(7);
            setFrameOffset(11);
        }
        if (this.characterClass == GameConstants.CLASS_MAGE) {
            seq = new int[]{10, 11, 12, 13, 14, 15};
            setFramesLeftToResetAnimation(7);
            setFrameOffset(10);
        }

        this.getSprite().setFrameSequence(seq);
    }

    /*
     *40 pts. distributed. lvl 30: 40+30*6=220 (55*4) + kamat
    
    str	dex	con	wp
    fighter: 	12	9	11	8
    Mage:		8	9	11	12
    Cleric:		10	10	10	10
     *
     */
    public void initNewCharacter() {
        this.strength = 0;
        this.dexterity = 0;
        this.constitution = 0;
        this.willpower = 0;
        addClassStats();

        updateMaxHealthAndMana();

        updateAbilities();
        health = maxHealth;
        mana = maxMana;
        healingPotions = 2;

        // Add initial equipment for a new character ++Ville
        addClassInitialEquipment();

        Date now = new Date();
        dateWhenGainedLevel[0] = Long.toString(now.getTime()); // Creation date

    }

    private void addClassInitialEquipment() {
        if (characterClass == GameConstants.CLASS_WARRIOR) {
            // WarriorChest, WarriorWeapon
            WarriorWeapon weapon = new WarriorWeapon();
            this.equip((Equipment) weapon);
            WarriorChest armor = new WarriorChest();
            this.equip((Equipment) armor);
        }
        if (characterClass == GameConstants.CLASS_CLERIC) {
            // ClericChest, WoodenClub
            WoodenClub weapon = new WoodenClub();
            this.equip((Equipment) weapon);
            ClericChest armor = new ClericChest();
            this.equip((Equipment) armor);
        }
        if (characterClass == GameConstants.CLASS_MAGE) {
            // MageChest, WoodenStaff
            PlainStaff weapon = new PlainStaff();
            this.equip((Equipment) weapon);
            MageChest armor = new MageChest();
            this.equip((Equipment) armor);
        }
    }

    private void addClassStats() {
        if (characterClass == GameConstants.CLASS_WARRIOR) {
            this.strength += 12;
            this.dexterity += 9;
            this.constitution += 11;
            this.willpower += 8;


        } else if (characterClass == GameConstants.CLASS_CLERIC) {
            this.strength += 10;
            this.dexterity += 10;
            this.constitution += 10;
            this.willpower += 10;


        } else if (characterClass == GameConstants.CLASS_MAGE) {
            this.strength += 8;
            this.dexterity += 9;
            this.constitution += 11;
            this.willpower += 12;
        }
    }

    public void updateAbilities() {
        int lvl = this.level - 1;
        this.activeAbilities[0] = null;
        this.activeAbilities[1] = null;
        this.activeAbilities[2] = null;
        this.activeAbilities[3] = null;

        boolean t = GameConstants.TESTINGMODE;

        if (characterClass == GameConstants.CLASS_WARRIOR) {
            if (t | AbilityTables.ABILITY_LEVELS[lvl][0] > 0) {
                this.activeAbilities[0] = new Strike();
            }
            if (t | AbilityTables.ABILITY_LEVELS[lvl][1] > 0) {
                this.activeAbilities[1] = new Enrage();
            }
            if (t | AbilityTables.ABILITY_LEVELS[lvl][2] > 0) {
                this.activeAbilities[2] = new MightyBlow();
            }
            if (t | AbilityTables.ABILITY_LEVELS[lvl][3] > 0) {
                this.activeAbilities[3] = new ShieldWall();
            }

        } else if (characterClass == GameConstants.CLASS_CLERIC) {
            if (AbilityTables.ABILITY_LEVELS[lvl][0] > 0) {
                this.activeAbilities[0] = new Heal();
            }
            if (AbilityTables.ABILITY_LEVELS[lvl][1] > 0) {
                this.activeAbilities[1] = new HolyBolt();
            }
            if (AbilityTables.ABILITY_LEVELS[lvl][2] > 0) {
                this.activeAbilities[2] = new Weakness();
            }
            if (AbilityTables.ABILITY_LEVELS[lvl][3] > 0) {
                this.activeAbilities[3] = new HealingAura();
            }

        } else if (characterClass == GameConstants.CLASS_MAGE) {            // add mage abilities here
            if (AbilityTables.ABILITY_LEVELS[lvl][0] > 0) {
                this.activeAbilities[0] = new FireBall();
            }
            if (AbilityTables.ABILITY_LEVELS[lvl][1] > 0) {
                this.activeAbilities[1] = new Ignite();
            }
            if (AbilityTables.ABILITY_LEVELS[lvl][2] > 0) {
                this.activeAbilities[2] = new Polymorph();
            }
            if (AbilityTables.ABILITY_LEVELS[lvl][3] > 0) {
                this.activeAbilities[3] = new MeteorStrike();
            }
        }

        // set ability levels and slots
        for (int i = 0; i < 4; i++) {

            if (activeAbilities[i] != null) {
                activeAbilities[i].setLevel(AbilityTables.ABILITY_LEVELS[lvl][i]);
                activeAbilities[i].setAbilitySlot(i);
            }
        }
    }

    public void awardKill(Creature target) {
        int amount = target.getExperienceWorth();
//        textEvents.addElement(new CombatText(this, "exp " + amount, System.currentTimeMillis(), 0x0000FF));
        textEvents.addElement(new CombatText(this, Local.get("combat.exp") + " " + amount, System.currentTimeMillis(), DarkwoodGraphics.FONT_ARIAL10_LIGHTBLUE));

        // return if not current player, dont actually award exp to party members
        if (this != Game.player) {
            return;
        }

        awardExp(amount);

        // check and award quest kills
        if (this.currentQuest != null) {
            this.currentQuest.awardKillRequirement(target);
        }
    }

    public void awardExp(int amount) {

        // no more exp after level 10 if not paid account or after max level
        if (checkExpCap()) {
            return;
        }

        // if the player has a penalty buffer, reduce it first
        if (deathPenaltyExpBuffer > 0) {

            // if death penalty left is less than the exp awarded, reduce amount of exp awarded and set penalty to zero
            if (deathPenaltyExpBuffer < amount) {
                amount = amount - deathPenaltyExpBuffer;
                deathPenaltyExpBuffer = 0;

            } else { // if death penalty is more than exp awarded, award 0 exp, but reduce penalty
                deathPenaltyExpBuffer = deathPenaltyExpBuffer - amount;
                amount = 0;

            }

        }

        experience += amount;
        totalExperience += amount;

        if (experience > getExpRequiredForNextLevel()) {
            experience = experience - getExpRequiredForNextLevel();
            advanceLevel();
        }
        Logger.getInstance().debug("Exp:" + amount + " / " + getExpRequiredForNextLevel());

    }

    private void advanceLevel() {
        // create the text event
        // textEvents.addElement(new CombatText(this, "LEVEL UP", System.currentTimeMillis(), 0xFF8C00));
        textEvents.addElement(new CombatText(this, "LEVEL UP", System.currentTimeMillis(), DarkwoodGraphics.FONT_ARIAL10_LIGHTBLUE));


        level++;
        MessageLog.getInstance().addMessage("You gain a level!");
        Display.getDisplay(Game.gameMidlet).vibrate(500);

        updateStats();
        updateAbilities();

        if (AbilityTables.checkAbilityChange(level) != -1) {
            Ability a = activeAbilities[AbilityTables.checkAbilityChange(level)];
            MessageLog.getInstance().addMessage("New ability: " + a.name + " [" + a.getLevel() + "]");
        }

        checkExpCap();
        dateWhenGainedLevel[level] = Long.toString((new Date()).getTime());
    }

    /**
     * Check if exp is capped, for example due to max level, or trial account.
     * Also set current exp to 0 if level is capped.
     * @return returns true if exp is capped and should not be awarded
     */
    private boolean checkExpCap() {

        // no level cap in offline version
//#ifndef DARKWOOD_OFFLINE
        // no more exp after level 10 if not paid account
        if (level >= 10 && Game.registered == false) {
            experience = 0;
            return true;
        }
//#endif

//#ifdef DARKWOOD_DEMOVERSION
//#         // no more exp after level 3 if not paid account
//#         if (level >= 3) {
//#             experience = 0;
//#             return true;
//#         }
//#endif


        // no more exp at lvl 30
        if (level >= 30) {
            experience = 0;
            return true;
        }

        // exp is not capped, returning false for normal awarding
        return false;
    }

    public void updateStats() {

        // set stats so that each level grants one stat, then add stats from equipment
        super.updateStats();

        // add base stats coming from player character class
        addClassStats();

        // add stats that the player has trained at the trainer
        this.strength += trainedStats[GameConstants.STAT_STR];
        this.dexterity += trainedStats[GameConstants.STAT_DEX];
        this.constitution += trainedStats[GameConstants.STAT_CON];
        this.willpower += trainedStats[GameConstants.STAT_WP];

        // reset max hp and max mana
        updateMaxHealthAndMana();

        // restart buff effects, which can affect stats
        super.restartBuffs();

    }

    public void updateMaxHealthAndMana() {

        maxHealth = (constitution * 7 + (strength + dexterity + willpower)) * GameConstants.playerHPConstant;

//        maxMana = (int) (willpower * 0.9 + constitution * 0.1) * GameConstants.playerMPConstant;   // imo ei connia manaan, muuten tankin ei tarvi ikin� ottaa wil
        maxMana = willpower * GameConstants.playerMPConstant;

    }

    public String sendInformation() {
        return "Player:" + this.getId() + "," + CharacterSerializer.createXmlString(this);
    }

    public boolean hates(Creature creature) {
        return creature instanceof Monster;
    }

    public String playerClassString() {
        String strClass = "undefined";
        switch (characterClass) {
            case GameConstants.CLASS_WARRIOR:
                strClass = Local.get("class.warrior");
                break;
            case GameConstants.CLASS_MAGE:
                strClass = Local.get("class.mage");
                break;
            case GameConstants.CLASS_CLERIC:
                strClass = Local.get("class.cleric");
                break;
        }
        return strClass;
    }

    /* Calculates and returns to number of free stat points.
     * Uses value of existing stats and compares them to current lvl.
     */
    public int getAvailableStatPoints() {
        int statValue = 0;
        for (int i = 0; i < GameConstants.NUMBER_OF_STATS; i++) {
            statValue += this.trainedStats[i];
            // initial stats + lvl ups. Note: no stat raise allowed at the first lvl
        }
        int referenceValue = (level - 1) * 6; // + 10; //+10 for testing
        return referenceValue - statValue;
    }

    /** This id is for saving character at the server (unique amongs all characters in the game)
     */
    public int getId() {
        return globalCharacterId;
    }

    /* This id is for saving character at the server (unique amongs all characters in the game)
     */
    public void setGlobalCharacterId(int globalCharacterId) {
        this.globalCharacterId = globalCharacterId;
    }

    public void invokeActiveAbility(int i) {
        if (activeAbilities[i] != null) {
            /*   if (game.party.isActive()) {
            if (game.party.isLeader) {
            activeAbilities[i].invoke(game, game.player);
            game.party.sendAbilityUseToClients(game.player.getId(), activeAbilities[i]);
            } else {
            game.party.sendAbilityUseToLeader(game.player.getId(), activeAbilities[i]);
            }
            
            } else { */
            activeAbilities[i].invoke(Game.player);
            // }
        }
    }

    // Reset death penalty exp buffer (called when player retreats from combat when uncon).
    // Value is 10% of level cost.
    public void resetDeathPenalty() {
        deathPenaltyExpBuffer = getExpRequiredForNextLevel() / 10;
    }

    public void setDeathPenaltyExpBuffer(int value) {
        deathPenaltyExpBuffer = value;
    }

    public int getDeathPenaltyExpBuffer() {
        return deathPenaltyExpBuffer;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        if (this.globalCharacterId != other.globalCharacterId) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.globalCharacterId;
        return hash;
    }
}
