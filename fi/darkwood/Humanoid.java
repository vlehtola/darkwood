/*
 * Humanoid.java
 *
 * Created on May 5, 2007, 12:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.darkwood.equipment.ChestArmour;
import fi.darkwood.equipment.Helmet;
import fi.darkwood.equipment.Shield;
import fi.darkwood.equipment.Weapon;

/**
 *
 * @author Tommi Laukkanen
 */
public abstract class Humanoid extends Creature {
        
    public int defence=0;
    public int[] itemStats = new int[4];
    /** Creates a new instance of Humanoid */
    public Humanoid(String name, String description, String image, int level, int frameWidth) {
        super(name, image, level, frameWidth);
    }

    /**
     * Equipment worn by this humanoid. Each index represents a slot. Indexes are found in GameConstants variables.
     */
    public Equipment[] equipmentSlots = new Equipment[GameConstants.NUMBER_OF_SLOTS];
    
    public int getDefense() {
        return this.defence;
    }
    
    public void updateDefense() {
        int defense = 0;       
        for (int i=0; i < equipmentSlots.length; i++) {
            if(equipmentSlots[i] != null) {
                defense += equipmentSlots[i].getArmorClass();
            }
        }        
        this.defence = defense;
    }
    
    public void updateStats() {       
        // substract 1 from current level so that level 1 doesnt add stats..
        int lvl = this.level - 1;
        
        int i,j;
        for(i=0; i < this.itemStats.length; i++)
            this.itemStats[i] =0;
        for(i=0; i < equipmentSlots.length; i++) {
            if(equipmentSlots[i] != null) {
                for(j=0; j < GameConstants.NUMBER_OF_STATS; j++) {
                    this.itemStats[j] += equipmentSlots[i].getStats()[j];
                }
            }
        }        
        // Calculate all stats from scratch        
        this.strength = this.itemStats[GameConstants.STAT_STR] + lvl;        
        this.dexterity = this.itemStats[GameConstants.STAT_DEX] + lvl;
        this.constitution = this.itemStats[GameConstants.STAT_CON] + lvl;
        this.willpower = this.itemStats[GameConstants.STAT_WP] + lvl;
        
        // Add to these the player selected points
    }
    
    public double getDamage() {
        if (equipmentSlots[GameConstants.SLOT_RIGHT_HAND] != null) {
            Weapon weapon = (Weapon) equipmentSlots[GameConstants.SLOT_RIGHT_HAND];
            int damageRange = weapon.maxDamage - weapon.minDamage;
            int damage = random.nextInt(damageRange) + weapon.minDamage;
            return super.getDamage() + damage;
        } else {
            return super.getDamage();
        }
    }
    
    /**
     * Equip an item given as parameter. Return null if slot is alreayd taken,
     * otherwise return the old eq
     * @param equipment new equipment to be equiped
     * @return null if slot not free, otherwise old eq
     */
    public Equipment equip(Equipment equipment) {
        boolean success=false;        
        int slot;
        slot = equipment.getSlot();
        Equipment oldEquipment = equipmentSlots[slot];
        
        if (equipment instanceof Helmet) {
            equipmentSlots[slot] = (Helmet) equipment;
            success = true;
        }
        
        if (equipment instanceof ChestArmour) {
            equipmentSlots[slot] = (ChestArmour) equipment;
            success = true;
        }
        
        if (equipment instanceof Weapon) {
            equipmentSlots[slot] = (Weapon) equipment;
            success = true;
        }
        
        if (equipment instanceof Shield) {
            equipmentSlots[slot] = (Shield) equipment;
            success = true;
        }
       /* 
        if (equipment instanceof Legs) {
            equipmentSlots[slot] = (Legs) equipment;
            success = true;
        }
        
        if (equipment instanceof Feet) {
            equipmentSlots[slot] = (Feet) equipment;
            success = true;
        } */
        if(success) {
            this.updateDefense();
            this.updateStats();
            return oldEquipment;            
        }
        
        return null;
    }
    
    public Equipment unequip(Equipment equipment) {
        for (int i=0; i < equipmentSlots.length; i++) {
            if (equipment == equipmentSlots[i]) {
                equipmentSlots[i] = null;
                return equipment;
            }
        }
        this.updateDefense();
        this.updateStats();
        return null;
    }
}
