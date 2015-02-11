/*
 * Equipment.java
 *
 * Created on May 5, 2007, 11:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.darkwood.equipment.Armour;
import fi.darkwood.equipment.ChestArmour;
import fi.darkwood.equipment.Helmet;
import fi.darkwood.equipment.Shield;
import fi.darkwood.equipment.Weapon;

/**
 *
 * @author Tommi Laukkanen, Ville
 */
public class Equipment extends Item {

    /** Creates a new instance of Equipment */
    public Equipment(String name, String description, String image, int level) {
        super(name, description, image, 0, level);
        this.type = GameConstants.ARMOR_MEDIUM;
        this.armorClass = GameConstants.itemACValue(type, level, getSlot());
        // By default the value should be zero before the following line for all but manually modified items
        // Now we add the basic money value

        this.quality = GameConstants.QUALITY_COMMON;
        this.value += GameConstants.itemCost(level, quality);
        this.stats = GameConstants.assignStats(level, quality);

    }

    public Equipment(String name, String description, String image, int value, int level, int itemType) {
        super(name, description, image, value, level);
        this.type = itemType;
        this.armorClass = GameConstants.itemACValue(itemType, level, getSlot());
        // By default the value should be zero before the following line for all but manually modified items
        // Now we add the basic value
        this.quality = GameConstants.QUALITY_COMMON;
        this.value += GameConstants.itemCost(level, quality);
        this.stats = GameConstants.assignStats(level, quality);
    }

    // Used by EquipmentFactory
    public Equipment(String name, String description, String image, int level, int itemType, int quality, int stats[]) {
        super(name, description, image, 0, level, quality);
        this.type = itemType;
        this.armorClass = GameConstants.itemACValue(itemType, level, getSlot());
        // By default the value should be zero before the following line for all but manually modified items
        // Now we add the basic value
        this.value += GameConstants.itemCost(level, quality);

        this.stats = stats;
    }
    private int armorClass;
    private String paperdollImage = "";
    private int stats[] = new int[4];
    private int type;
    protected int randomSeed;

    public int getRandomSeed() {
        return randomSeed;
    }

    public void setRandomSeed(int rs) {
        this.randomSeed = rs;
    }

    public int getStrength() {
        return stats[GameConstants.STAT_STR];
    }

    public int getDexterity() {
        return stats[GameConstants.STAT_DEX];
    }

    public int getConstitution() {
        return stats[GameConstants.STAT_CON];
    }

    public int getWillpower() {
        return stats[GameConstants.STAT_WP];
    }

    public int[] getStats() {
        return stats;
    }
    /*
    public String getSerializedStats() {
      return ""+this.getStrength()+","+this.getDexterity()+","+this.getConstitution()+","+this.getWillpower()+"";
    }
     * */
    
    public int getType() {
        return type;
    }

    public int getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    public int getSlot() {
        if (this instanceof Helmet) {
            return GameConstants.SLOT_HEAD;
        } else if (this instanceof ChestArmour) {
            return GameConstants.SLOT_CHEST;
        } else if (this instanceof Weapon) {
            return GameConstants.SLOT_RIGHT_HAND;
        } else if (this instanceof Shield) {
            return GameConstants.SLOT_LEFT_HAND;
        }
        System.out.println("Unknown slot for: " + this.name);
        return -1;
        /* else if (this instanceof Legs) {
            return GameConstants.SLOT_LEGS;
        } else if (this instanceof Feet) {
            return GameConstants.SLOT_FEET;
        } else {
            // unknown slot
            return -1;
        } */
    }

    public String getPaperdollImage() {
        return paperdollImage;
    }

    public void setPaperdollImage(String paperdollImage) {
        this.paperdollImage = paperdollImage;
    }

    public String getItemInfo() {
        String statinfo = ""; String addinfo = "";

        if(this instanceof Weapon ) {
            Weapon wp = (Weapon) this;
            addinfo = "Dmg: "+ wp.minDamage +" - " +wp.maxDamage + " ";
        } else if (this instanceof Armour) {
            addinfo = "Def: " + this.armorClass + " ";
        }
        if (this.getStrength() > 0) {
            statinfo += "+" + this.getStrength() + " Str ";
        }
        if (this.getDexterity() > 0) {
            statinfo += "+" + this.getDexterity() + " Dex ";
        }
        if (this.getConstitution() > 0) {
            statinfo += "+" + this.getConstitution() + " Con ";
        }
        if (this.getWillpower() > 0) {
            statinfo += "+" + this.getWillpower() + " Wpr";
        }

        statinfo = addinfo + "\n" + statinfo;
        return statinfo;
    }

}
