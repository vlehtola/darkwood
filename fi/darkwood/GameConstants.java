/*
 * GameConstants.java
 *
 * The purpose of this file is to contain all the constants used to multiply level to get
 * the gaming factors, ex. hp, mp, dps, exp/money gained ...
 * Since the game mechanics are linear, all calculations can be made from these constants.
 * Hence "tuning the system" is simple.
 *
 * Created on December 29, 2007, 4:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import java.util.Random;

/**
 * "Keeps track" of the game balance.
 * 
 * @author Ville Lehtola
 */
public class GameConstants {

    // if this is true, skip start menus and go straight into game with a test char, determined via preprocessor
//#if TESTMODE
//#     public static final boolean TESTINGMODE = true;
//#else
    public static final boolean TESTINGMODE = false;
//#endif

    // if this is true, scale the game display to fit full screen
    public static final boolean SCALE_GRAPHICS_TO_FULLSCREEN = true;

    // log to console the sleeptime that occurs after each frame (basically lag meter)
    public static final boolean LOG_SLEEPTIME = false;
    public static final int maxLevel = 30;
    public static final int ticksPerRound = 15; // how many ticks between normal combat rounds. affects ability cooldowns also
    // The interesting parameters, how long does it take to level or obtain cash to buy items
    public static final double statDPSpercent = 0.30; // Strength increases base damage; 30% from str, 70% from weapon
    // All these values are linear coefficients
    public static final int monsterHPConstant = 10; // this * level = monsterHP
    public static final int monsterELITEHPConstant = 3; // multiply elite monster HP by this factor
    public static final double eliteEXPmultiplier = 1.5; // multiply elite monster XP
    public static final int monsterMPConstant = 10; // monster mana
    public static final double monsterStatConst = 2.5; // stat coef (this) * level = monster stat
    public static final double monsterDPSConstant = 1 + (monsterStatConst - 1) * statDPSpercent;
    public static final int playerHPConstant = 1;
    public static final int playerMPConstant = 10;
    public static final double playerStatConst = 10.0 / 4.0;
    public static final double playerDPSConstant = 1 + (playerStatConst - 1) * statDPSpercent; // stats gained per level per stat
    public static final double playerRestConst = 0.05; // when moving into a new room, regain 5% of energy
    public static final int weaponDamageCoef = 1; // used in weapon.java to calculate avg weapon damage

    public static final int monsterEXPConstant = 10;
    public static final double monsterMONEYConstant = 2.0;
    public static final double itemsPerLevel = 0.8; // how many items of the same level we can buy on levelup on average
    public static final int levelCostConstant = 50;
    public static final double dpsConstant = 1.0;
    public static final double attackDexHaste = 0.02;
    public static final int armorClassConstant = 2; // Affects how drastic the level difference is between the items and the opponent
    public static final double armorClassCoef = 1.0;
    public static final int NUMBER_OF_CLASSES = 3;
    public static final int CLASS_WARRIOR = 0,  CLASS_MAGE = 1,  CLASS_CLERIC = 2;
    public static final int ARMOR_HEAVY = 0,  ARMOR_MEDIUM = 1,  ARMOR_LIGHT = 3,  ARMOR_CLOTH = 2;
    public static final int QUALITY_COMMON = 0,  QUALITY_UNCOMMON = 1,  QUALITY_RARE = 2, QUALITY_EPIC = 3;
    public static final int STAT_STR = 0,  STAT_DEX = 1,  STAT_CON = 2,  STAT_WP = 3;
    public static final int ITEM_STATS_PER_LEVEL = 2;
    public static final int SLOT_HEAD = 0;
    public static final int SLOT_CHEST = 1;
    public static final int SLOT_RIGHT_HAND = 2;
    public static final int SLOT_LEFT_HAND = 3;
/*    public static final int SLOT_LEGS = 4;
    public static final int SLOT_FEET = 5; */
    public static final int NUMBER_OF_SLOTS = 4;
    public static final int NUMBER_OF_TYPES = 3;
    public static final int NUMBER_OF_STATS = 4;

    public static final int baseCritChance = 2;

    public static final int EQUIPMENT_LEVEL_REQUIREMENT_GAP = 3;

    // frames between heal "ticks" in city (10 = 1 sec)
    public static final int HEAL_TICK_DELAY = 10;
    // how many heal tick needed to heal to max health (see above)
    public static final int HEAL_TICKS_TO_HEAL_HEALTH_TO_MAX = 50;
    public static final int HEAL_TICKS_TO_HEAL_MANA_TO_MAX = 25;

    /** Creates a new instance of GameConstants */
    public GameConstants() {
        if (TESTINGMODE) {
            int i;
            String str = "", str2 = "";
            for (i = 1; i < 31; i++) {
                str += timeToLevel(i) + " ";
                str2 += levelCost(i) + " ";
            }
            System.out.println("timeToLevel: " + str);
            System.out.println("levelCost: " + str2);
        }
    }

    // in ticks!! remember that one tick != 1 second, necessarily.
    public static double timeToLevel(int level) {

        int timeToLevelFirst = 60;  // (60 secs = 1 min ;)
        // For time to level last level:
        // pow yields: 14242, Target: 14400 = 4 * 60 * 60;  //in seconds (4 hours)
        // Future considerations: add one "checkpoint" to level 10
        double levelTime = timeToLevelFirst*(0.15*level*level+ 0.35);
         if(level > 20) {
            levelTime += timeToLevelFirst*0.1*(level*level - 400);
         }
        return levelTime;
    }

//    public static double timeToGetItem(int level) {
//        return 1.2*timeToLevel(level);
//    }
//    public static double timeToTrainPerLevel(int level) {
//        return 0.8*timeToLevel(level);
//    }

    /*
     *  Player level and advancement
     */
    // levelcost for level+1
    public static int levelCost(int level) {
        // xp/t = (M.hp / P.dps) (M.xp / M.hp) = (M.L / P.L) (M.XP / P.DPS) ~= M.XP / P.DPS
        // Surprisingly, there is no secondary level dependence.
        double C = monsterEXPConstant / playerDPSConstant;
        return (int) (timeToLevel(level - 1) * C);
    }

    public static int playerStat(int level, int stat, int playerClass) {
        int coef = 0;
        if (stat == STAT_STR && playerClass == CLASS_WARRIOR) {
            coef++;
        }
        if (stat == STAT_WP && playerClass == CLASS_MAGE) {
            coef++;
        }
        if (stat == STAT_CON && playerClass == CLASS_CLERIC) {
            coef++;
        }
        return coef * level;
    }

    public static int getArmorTypeforClass(int playerClass) {
        if (playerClass == CLASS_WARRIOR) {
            return ARMOR_HEAVY;
        } else if (playerClass == CLASS_CLERIC) {
            return ARMOR_MEDIUM;
        } else if (playerClass == CLASS_MAGE) {
            return ARMOR_CLOTH;
        }
        return ARMOR_CLOTH;
    }

    /*
     *  Monsters
     *
     */
    public static int getMonsterMoney(int level) {
        return (int) monsterMONEYConstant * level;
    }

    public static double getMonsterDPS(int level) {
        return monsterDPSConstant * level;
    }
    /*
     *  Items and equipment
     */

    public static int getItemTier(int level) {
        if (level < 9) {
            return 1;
        } else if (level < 20) {
            return 2;
        }
        return 3;
    }

    // totally random stats
    public static int[] assignStats(int level, int quality) {
        float rv[] = new float[4];
        int st[] = new int[4];
        float sum = 0;
        int i, statsum=0;
        if (quality != QUALITY_COMMON) {
            Random rand = new Random();
            int qualityBonus = 4*(quality-1); // two levels higher
            for (i = 0; i < 4; i++) {
                rv[i] = rand.nextFloat();
                sum += rv[i];
            }
            int totalstats = GameConstants.ITEM_STATS_PER_LEVEL * (level + qualityBonus);
            for (i = 0; i < 4; i++) {
                st[i] = (int) ((rv[i] / sum) * totalstats);
                statsum += st[i];
            }
            // check for rounding errors
            if (statsum != totalstats) {
               int chosenStat = rand.nextInt(4);
               st[chosenStat] += totalstats - statsum;
               if(st[chosenStat] < 0) st[chosenStat] = 0;
            }
        } else {
            for (i = 0; i < 4; i++) {
                st[i] = 0;
            }
        }

        return st;
    }

    public static int itemCost(int level, int quality) {
        //return (int) ( timeToGetItem(level) *monsterMONEYConstant ); //for testing
        double val = levelCost(level) / monsterEXPConstant;
        val = val / itemsPerLevel * monsterMONEYConstant;
        if (quality == QUALITY_COMMON) {
            val /= 2;
        }
        return (int) val;
    }

    public static int weaponCost(int level, int quality) {
        return itemCost(level, quality) * 2;
    }

    // in addition to this there is a reduction from dexterity
    // Used to be 5/10/15/30, changed by Ville 2.5.09
    public static int classArmorReduction(int itemClass) {
        switch (itemClass) {
            case ARMOR_HEAVY:
                return 45;
            case ARMOR_CLOTH:
                return 5;
            case ARMOR_MEDIUM:
                return 30;
            case ARMOR_LIGHT:
                return 15;
        }
        return 0;
    }
    // classMeleeCoef() + classAbilityCoef() == 1
    public static double classMeleeCoef(int playerClass) {
        switch(playerClass) {
            case CLASS_WARRIOR: return 0.6;
            case CLASS_MAGE: return 0.3;
            case CLASS_CLERIC: return 0.45;
            default: return 0;
        }
    }
    public static double classAbilityCoef(int playerClass) {
        switch(playerClass) {
            case CLASS_WARRIOR: return 0.4;
            case CLASS_MAGE: return 0.7;
            case CLASS_CLERIC: return 0.55;
            default: return 0;
        }
    }


    // Returns values in range [5, 30] * level / nos
    public static int itemACValue(int itemType, int level, int slot) {
        // Cleric: ac = (2+2) * 15 = 60
        // aCC = 1.0
        // 60 * 40 / 100 = 24
        double ac = ( (double) ( level + armorClassConstant) ) * classArmorReduction(itemType);
        ac *= armorClassCoef;
        int mp; // multiplier
        // emphasize the AC effect of shield and chest armor
        // Here we define the effect of different slots. the numbers add up to 100, which is the norming factor
        switch (slot) {
            case SLOT_LEFT_HAND:
                mp = 40;
                break;
            case SLOT_CHEST:
                mp = 40;
                break;
            case SLOT_HEAD:
                mp = 20;
                break;
            default:
                mp = 0;
        }
        ac = ac * mp / 100;
        return (int) ac;
    }

    public static int itemStatRequirement(int stat, int level, int itemType) {
        if (itemType == ARMOR_HEAVY) {
            switch (stat) {
                case STAT_STR:
                    return level * 3;
                default:
                    return 0;
            }
        }
        if (itemType == ARMOR_MEDIUM) {
            switch (stat) {
                case STAT_STR:
                    return level * 3 / 2;
                case STAT_WP:
                    return level * 3 / 2;
                default:
                    return 0;
            }
        }
        if (itemType == ARMOR_CLOTH) {
            switch (stat) {
                case STAT_WP:
                    return level * 3;
                default:
                    return 0;
            }
        }
        if (itemType == ARMOR_LIGHT) {
            switch (stat) {
                case STAT_DEX:
                    return level * 2;
                default:
                    return 0;
            }
        }
        return 0;
    }

    /**
     * Returns the level recommendation for the player to clear the area
     * 
     * Who wins the fight? m.hp / p.dps =? p.hp / m.dps
     * regen?
     * p.hp > (mobsPerRoom * m.hp * m.dps / p.dps) * number of fights
     * p.l*p.l > (mobsPerRoom * m.HP * m.DPS * m.l * m.l / ( p.DPS * p.HP)) * number of fights
     * 
     * NOTE: effect of skills, potions etc. are not yet taken into account
     */
    public static int calculateAreaLevel(int roomAmount, int monsterLevelSum, int monsterAmount) {
        int level = 0;
        if (monsterAmount == 0) {
            return -1;
        }
        double avgLevel = (double) monsterLevelSum / monsterAmount;
        double mobsPerRoom = (double) monsterAmount / roomAmount;
        double numberOfFights = (double) roomAmount;

        if (mobsPerRoom < 1) {
            // if there is a lot of empty rooms
            numberOfFights *= mobsPerRoom;
        }
        if (mobsPerRoom == 0) {
            mobsPerRoom = 1;
        }
        // Calculate
        // Need to take into account the 10% mana regen per room
        // m.HP / p.DPS < p.HP / m.DPS
        // m.HP * m.DPS < p.HP * p.DPS
        // 1 < p.HP * p.DPS / ( m.HP * m.DPS )
        // 1 < p.hp * p.dps * p.L^2 / ( m.hp * m.dps * m.L^2)
        // m.hp * m.dps / ( p.hp * p.dps ) < p.L^2 / m.L^2
        //
        // Mana regen modifier
        //
        // m.hp * m.dps / (p.hp * p.dps * mrm) < p.L^2 / ( <m.L>^2 * mobsPerRoom )


        // Req. level l^2 \geq m.hp * m.dps / (p.hp * p.dps * mrm) * ( \sum m.L^2 * mobsPerRoom )
        // TODO: take healing statues into account
        double MRM = 1.05; // Mana Regen Modifier
        double abilityMod = 2.0;
        double LHS = monsterHPConstant * monsterDPSConstant / (abilityMod * playerDPSConstant * playerHPConstant * MRM);
        double RHS = avgLevel * avgLevel * mobsPerRoom; // * numberOfFights;
        double l2 = LHS * RHS;

        level = (int) Math.sqrt(l2);
        System.out.println("AREALEVEL: "+roomAmount+" "+monsterLevelSum+" "+monsterAmount+" out "+LHS+" "+RHS+" "+level);

        // Also take into account the initial stats and bonuses
        // 10 on each stat / statIncrPerLvl = 4
        //level -= 4;

        if (level < 2) {
            level = 2;
        }
        return level;
    }
}
