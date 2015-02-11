/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.network;

import fi.darkwood.Equipment;
import fi.darkwood.EquipmentFactory;
import fi.darkwood.Game;
import fi.darkwood.GameConstants;
import fi.darkwood.Player;
import fi.darkwood.Quest;
import fi.darkwood.util.LocalDatabase;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import nanoxml.kXMLElement;

/**
 * Character serializer class for saving and loading over network
 * 
 * @author Teemu Kivim�ki
 */
public class CharacterSerializer {

    public static String createXmlString(Player player) {
        //      kXMLElement save = new kXMLElement();
        //      save.setTagName("save");
        //      save.addProperty("username", account);
        //      save.addProperty("password", password);

        kXMLElement character = new kXMLElement();
        character.setTagName("character");

        character.addProperty("name", player.name);
        character.addProperty("class", player.characterClass);
        character.addProperty("level", player.level);
        character.addProperty("experience", player.experience);
        character.addProperty("totalExperience", player.totalExperience);
        character.addProperty("money", player.money);
        character.addProperty("hp", player.health);
        character.addProperty("mana", player.mana);
        character.addProperty("str", player.strength);
        character.addProperty("dex", player.dexterity);
        character.addProperty("con", player.constitution);
        character.addProperty("int", player.willpower);
        //character.addProperty("location", player.room.getZone().getClass().getName());

        character.addProperty("healingPotions", player.healingPotions);

        character.addProperty("trainedStr", player.trainedStats[GameConstants.STAT_STR]);
        character.addProperty("trainedDex", player.trainedStats[GameConstants.STAT_DEX]);
        character.addProperty("trainedCon", player.trainedStats[GameConstants.STAT_CON]);
        character.addProperty("trainedWp", player.trainedStats[GameConstants.STAT_WP]);
        character.addProperty("deathPenaltyExpValue", player.getDeathPenaltyExpBuffer());

        String fallbackZone = player.room.zone.getFallbackZone();
        if (fallbackZone == null) {
            System.out.println("SAVER fallbackZONE: NULL!");
            fallbackZone = player.room.zone.getClass().getName();
        }
        character.addProperty("fallbackzone", fallbackZone);
        System.out.println("SAVER fallbackZONE: " + fallbackZone);

        kXMLElement gainedLevel = new kXMLElement();
        gainedLevel.setTagName("dateWhenGainedLevel");
        int i;
        for (i = 0; i <= GameConstants.maxLevel; i++) {
            if (player.dateWhenGainedLevel[i] != null) {
                gainedLevel.addProperty("dateWhenGainedLevel" + (Integer.toString(i)), player.dateWhenGainedLevel[i]);
            }
        }
        character.addChild(gainedLevel);

//        save.addChild(character);

        /*     for (int i = 0; i < player.activeAbilities.length; i++) {
        if (player.activeAbilities[i] != null) {
        kXMLElement ability = new kXMLElement();
        ability.setTagName("ability");
        ability.addProperty("class", player.activeAbilities[i].getClass().getName());
        ability.addProperty("level", player.activeAbilities[i].getLevel());
        character.addChild(ability);
        }
        } */

        for (i = 0; i < player.equipmentSlots.length; i++) {
            saveItem(player.equipmentSlots[i], character);
        }

        for (i = 0; i < player.completedQuests.size(); i++) {
            saveCompletedQuests((Quest) player.completedQuests.elementAt(i), character);
        }

        saveActiveQuest(player.currentQuest, character);

        return character.toString();
    }

    /**
     * Adds the equipment given as parameter to XMLElement character
     * @param eq
     * @param character
     */
    private static void saveItem(Equipment eq, kXMLElement character) {

        /*
         *  From Equipment:
         *
         *   private int armorClass;
        private String paperdollImage = "";
        private int stats[] = new int[4];
        private int type;
         *
         * From Item:
         *
         *    private int level;
        public int value;
        public String description;

        public int quality = GameConstants.QUALITY_COMMON;
         * *
         *
         * From Thing:
         *  public String name;
        public String image;
         */

        if (eq != null) {
            kXMLElement element = new kXMLElement();
            element.setTagName("item");
            element.addProperty("level", eq.getLevel());
            element.addProperty("slot", eq.getSlot());
            element.addProperty("type", eq.getType());
            element.addProperty("quality", eq.quality);
            element.addProperty("imagefile", eq.getPaperdollImage()); // this needed for paperdoll at server
            // following lines added 26.1.2010 ++Ville
            element.addProperty("strength", eq.getStrength());
            element.addProperty("dexterity", eq.getDexterity());
            element.addProperty("constitution", eq.getConstitution());
            element.addProperty("willpower", eq.getWillpower());
            element.addProperty("desc", eq.description);
            element.addProperty("name", eq.name);
            element.addProperty("randomSeed", eq.getRandomSeed()); // used to save the weapon damage variance
            element.addProperty("image", eq.image);
            character.addChild(element);
        }
    }

    private static void saveCompletedQuests(Quest quest, kXMLElement character) {

        kXMLElement element = new kXMLElement();

        element.setTagName("quest");
        element.addProperty("class", quest.getClass().getName());
        element.addProperty("status", "completed");

        character.addChild(element);

    }

    private static void saveActiveQuest(Quest quest, kXMLElement character) {
        if (quest == null) {
            return;
        }

        kXMLElement element = new kXMLElement();

        element.setTagName("quest");
        element.addProperty("class", quest.getClass().getName());
        element.addProperty("status", "current");

        // save the kills the player has already done
        Enumeration enumeration = quest.requirementsDone.keys();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();

            Integer done = (Integer) quest.requirementsDone.get(key);
            int doneInt = done.intValue();

            kXMLElement requirement = new kXMLElement();
            requirement.setTagName("requirement");
            requirement.addProperty("id", key);
            requirement.addProperty("value", doneInt);

            element.addChild(requirement);
        }

        character.addChild(element);

    }

    public static Vector loadCharactersToMenu(String str) {
        kXMLElement xml = new kXMLElement();
        Vector menuItems = new Vector();
        xml.parseString(str);
        Enumeration enumeration = xml.getChildren().elements();
        int id;
        while (enumeration.hasMoreElements()) {
            kXMLElement response = (kXMLElement) enumeration.nextElement();
            id = response.getProperty("id", 0);
            Player player = loadCharacter((kXMLElement) response.getChildren().firstElement(), id);
            menuItems.addElement(player);
        }
        return menuItems;
    }

    public static Vector loadCharactersToMenuOffline() {
        Random random = new Random(System.currentTimeMillis());
        Vector menuItems = new Vector();


        // loop though the character slots
        for (int i = 0; i < 5; i++) {

            String str = LocalDatabase.loadCharacterXML(i);

            if (str == null || "".equals(str) || str.length() < 1) {
                // empty slot
                menuItems.addElement("Slot " + (i + 1));
            } else {
                // character saved in this slot, now load it
                kXMLElement xml = new kXMLElement();
                xml.parseString(str);

                int id = random.nextInt(1000000) + 1000;
                Player player = loadCharacter(xml, id);

                menuItems.addElement(player);
            }
        }

        return menuItems;
    }

    public static Player loadCharacter(kXMLElement character, int id) {

        System.out.println("Loading character id:" + id);

        String name = character.getProperty("name");
        int charClass = character.getProperty("class", -1);
        int strength = character.getProperty("str", -1);
        int intelligence = character.getProperty("int", -1);
        int dexterity = character.getProperty("dex", -1);
        int constitution = character.getProperty("con", -1);
        int level = character.getProperty("level", -1);

        int[] trainedStats = new int[GameConstants.NUMBER_OF_STATS];
        trainedStats[GameConstants.STAT_STR] = character.getProperty("trainedStr", -1);
        trainedStats[GameConstants.STAT_DEX] = character.getProperty("trainedDex", -1);
        trainedStats[GameConstants.STAT_CON] = character.getProperty("trainedCon", -1);
        trainedStats[GameConstants.STAT_WP] = character.getProperty("trainedWp", -1);

        int hp = character.getProperty("hp", 1);
        int mana = character.getProperty("mana", 1);

        int experience = character.getProperty("experience", -1);
        int totalExperience = character.getProperty("totalExperience", -1);

        int money = character.getProperty("money", -1);
        int healingPotions = character.getProperty("healingPotions", -1);


        String animationFramesFile = Game.getAnimationFramesFile(charClass);
        int animationFrameWidth = Game.getAnimationFrameWidth(charClass);

        int deathPenaltyExpValue = character.getProperty("deathPenaltyExpValue", 0);
        String startZone = character.getProperty("fallbackzone", "");
        Player player = new Player(name, "", animationFramesFile, level, animationFrameWidth, charClass);

        player.setGlobalCharacterId(id);

        player.health = hp;
        player.mana = mana;

        player.trainedStats = trainedStats;

        player.experience = experience;
        player.totalExperience = totalExperience;

        player.money = money;
        player.healingPotions = healingPotions;

        player.setDeathPenaltyExpBuffer(deathPenaltyExpValue);
        player.startZone = startZone;

        System.out.println("STARTZONE LOADER: " + player.startZone + " id:" + id);

        Enumeration e = character.getChildren().elements();

        while (e.hasMoreElements()) {
            kXMLElement element = (kXMLElement) e.nextElement();
            if ("item".equals(element.getTagName())) {
                loadItem(element, player);
            } else if ("quest".equals(element.getTagName())) {
                loadQuest(element, player);
            } else if ("dateWhenGainedLevel".equals(element.getTagName())) {
                loadDateWhenGainedLevel(element, player);
            }

        }

        player.updateStats();

        player.updateAbilities();


        return player;

    }

    private static void loadDateWhenGainedLevel(kXMLElement element, Player player) {

        int i;
        for (i = 0; i <= GameConstants.maxLevel; i++) {
            player.dateWhenGainedLevel[i] = element.getProperty("dateWhenGainedLevel" + (Integer.toString(i)));
        }
    }

    private static void loadItem(kXMLElement element, Player player) {
        int level = element.getProperty("level", 0);
        int type = element.getProperty("type", 0);
        int slot = element.getProperty("slot", 0);
        int quality = element.getProperty("quality", 0);
        // following lines added 26.1.2010 ++Ville
        int[] stats = new int[4];
        stats[GameConstants.STAT_STR] = element.getProperty("strength", 0);
        stats[GameConstants.STAT_DEX] = element.getProperty("dexterity", 0);
        stats[GameConstants.STAT_CON] = element.getProperty("constitution", 0);
        stats[GameConstants.STAT_WP] = element.getProperty("willpower", 0);
        String desc = element.getProperty("desc", "loadedweapon");
        String name = element.getProperty("name", "loadedname");
        int randomSeed = element.getProperty("randomSeed", 0);
        String paperdoll = element.getProperty("imagefile", "");
        String image = element.getProperty("image", "");


        try {
            // Equipment eq = EquipmentFactory.getInstance().createEquipment(level, slot, type, quality);
            Equipment eq = EquipmentFactory.getInstance().createEquipment(level, slot, type, quality, stats, image, paperdoll, name, desc, randomSeed);

            player.equip(eq);
        } catch (Exception e) {
            System.out.println("Unable to load item: " + level + ", " + slot + ", " + type + ", " + quality + " ERROR: " + e.getMessage());
        }

        /*
        String className = element.getProperty("class");

        Class luokka = null;
        try {
        luokka = Class.forName(className);
        Equipment eq = null;
        eq = (Equipment) luokka.newInstance();
        player.equip(eq);

        } catch (ClassNotFoundException ex) {
        System.out.println("Class not found loading eq: " + className);
        ex.printStackTrace();
        } catch (InstantiationException ex) {
        ex.printStackTrace();
        } catch (IllegalAccessException ex) {
        System.out.println("Illegal access loading eq: " + className);
        ex.printStackTrace();
        }
         */
    }

    /* NOT IN USE. ABILITIES ARE LOADED AUTOMATICALLY BY CHAR LEVEL
    private static void loadAbility(kXMLElement element, Player player, int abilityCount) {
    String abilityClassName = element.getProperty("class");
    int abilityLevel = element.getProperty("level", 0);

    Class luokka = null;
    try {
    luokka = Class.forName(abilityClassName);
    Ability ability = null;
    ability = (Ability) luokka.newInstance();
    ability.setLevel(abilityLevel);

    player.activeAbilities[abilityCount] = ability;

    } catch (ClassNotFoundException ex) {
    ex.printStackTrace();
    } catch (InstantiationException ex) {
    ex.printStackTrace();
    } catch (IllegalAccessException ex) {
    ex.printStackTrace();
    }

    } */
    private static void loadQuest(kXMLElement element, Player player) {
        String questClassName = element.getProperty("class");
        Class luokka = null;

        try {
            luokka = Class.forName(questClassName);
            Quest q = (Quest) luokka.newInstance();

            String status = element.getProperty("status");
            if (status != null && status.equals("completed")) {
                player.completedQuests.addElement(q);
            } else if (status != null && status.equals("current")) {
                Enumeration e = element.getChildren().elements();
                while (e.hasMoreElements()) {
                    kXMLElement reqElem = (kXMLElement) e.nextElement();
                    //System.out.println((String) reqElem.getProperty("id") + ", " + reqElem.getProperty("value", 0));
                    q.setCompletedKills((String) reqElem.getProperty("id"), reqElem.getProperty("value", 0));

                }
                player.currentQuest = q;
            }

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

    }
}
