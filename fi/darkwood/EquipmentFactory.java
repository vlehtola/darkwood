/*
 * EquipmentFactory.java
 *
 * Created on July 17, 2008, 5:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.darkwood.equipment.ChestArmour;
import fi.darkwood.equipment.Helmet;
import fi.darkwood.equipment.Shield;
import fi.darkwood.equipment.Weapon;
import fi.mirake.Local;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Random;
import nanoxml.kXMLElement;

/**
 *
 * @author Ville Lehtola
 */
public class EquipmentFactory {

    Random rand = new Random();

    /** Creates a new instance of EquipmentFactory */
    protected EquipmentFactory() {
    }
    private static EquipmentFactory instance = null;

    public static EquipmentFactory getInstance() {
        if (instance == null) {
            instance = new EquipmentFactory();
        }
        return instance;
    }

    /**
     * Create random equipment for a known level
     * @param level item level
     * @param quality item quality (GameConstants.QUALITY_COMMON etc)
     * @return created equipment
     */
    public Equipment createEquipment(int level, int quality) {
        int slot, type;
        //slot = rand.nextInt(GameConstants.NUMBER_OF_SLOTS);
        slot = rand.nextInt(4);

//type = rand.nextInt(GameConstants.NUMBER_OF_TYPES);

        type = GameConstants.getArmorTypeforClass(Game.player.characterClass);
        System.out.println("Creating random eq: slot " + slot + ", type " + type + ", level "+ level + ".");
        return createEquipment(level, slot, type, quality);
    }

    /**
     * Create random equipment for known level, slot and type
     * @param level
     * @param slot
     * @param type
     * @param quality
     * @return
     */
    public Equipment createEquipment(int level, int slot, int type, int quality) {
        String name, desc, image, paperdoll;

        int stats[] = GameConstants.assignStats(level, quality);

        image = "/images/equipment_icons/";
        paperdoll = "/images/equipment/paperdoll/";
        String str[] = getImages(slot, level, type);
        image = image + str[0];
        paperdoll = paperdoll + str[1];
        name = str[2];
        desc = name;

        int randomSeed = ( rand.nextInt(100) + rand.nextInt(100) + rand.nextInt(100)+3 ) / 3; // avg 3d100
      //  System.out.println("EF: " + name);
        return createEquipment(level, slot, type, quality, stats, image, paperdoll, name, desc, randomSeed);
    }

    //Create a specific equipment with no random
    public Equipment createEquipment(int level, int slot, int type, int quality, int[] stats, String image, String paperdoll, String name, String desc, int randomSeed) {
        Equipment eq = null;
        switch (slot) {
            case GameConstants.SLOT_HEAD:
                eq = (Equipment) new Helmet(name, desc, image, level, type, quality, stats);
                break;
            case GameConstants.SLOT_CHEST:
                eq = (Equipment) new ChestArmour(name, desc, image, level, type, quality, stats);
                break;
            case GameConstants.SLOT_RIGHT_HAND:
                eq = (Equipment) new Weapon(name, desc, image, level, type, quality, stats, randomSeed);
                break;
            case GameConstants.SLOT_LEFT_HAND:
                eq = (Equipment) new Shield(name, desc, image, level, type, quality, stats);
                break;
        /*    case GameConstants.SLOT_FEET:
                eq = (Equipment) new Feet(name, desc, image, level, type, quality, stats);
                break;
            case GameConstants.SLOT_LEGS:
                eq = (Equipment) new Legs(name, desc, image, level, type, quality, stats);
                break; */
            default:

        }
        eq.setPaperdollImage(paperdoll);
        Logger.getInstance().debug("Cloned " + image + " " + paperdoll + " SLOT: " + slot);
        return eq;
    }

    private String[] getImages(int slot, int level, int type) {
        String st[] = new String[3]; // image, paperdoll, name
        String resourceName = "/fi/darkwood/equipment/EquipmentNames.xml";
        StringBuffer b = new StringBuffer();
        try {
            InputStream is = this.getClass().getResourceAsStream(resourceName);
            int ch;
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            //System.out.print((char) ch);
            }
        } catch (java.io.IOException e) {
            System.out.println("Unable to load area " + resourceName);
            return null;
        }
        kXMLElement exml = new kXMLElement();
        String str = b.toString();

        exml.parseString(str);
        Enumeration enumeration = exml.getChildren().elements();
        while (enumeration.hasMoreElements()) {
            kXMLElement itemType = (kXMLElement) enumeration.nextElement();
            String armorType = itemType.getProperty("type");
            // Start matching the required properties: type, slot
            if (type == Integer.parseInt(armorType)) {
                Enumeration e = itemType.getChildren().elements();
                while (e.hasMoreElements()) {
                    kXMLElement itemSlot = (kXMLElement) e.nextElement();
                    String armorSlot = itemSlot.getProperty("slot");
                    if (slot == Integer.parseInt(armorSlot)) {
                        // Match the level to tier
                        Enumeration eTier = itemSlot.getChildren().elements();
                        while (eTier.hasMoreElements()) {
                            kXMLElement itemTier = (kXMLElement) eTier.nextElement();
                            String armorTier = itemTier.getProperty("tier");
                            int tier = Integer.parseInt(armorTier);
//                            System.out.println("Tier:" + tier + " / " + level /10);
                            if( GameConstants.getItemTier(level) == tier) {
                                //Found match
                                kXMLElement item = (kXMLElement) itemTier.getChildren().lastElement();
                                st[0] = item.getProperty("icon");
                                st[1] = item.getProperty("doll");
                                st[2] = Local.get(item.getProperty("name"));
                            }
                        }
                    }

                }
            }
        }
        return st;
    }
}

