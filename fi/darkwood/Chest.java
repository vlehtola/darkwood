/*
 * Chest.java
 *
 * Created on 29. syyskuuta 2007, 17:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import java.util.Random;

/**
 *
 * @author Teemu Kivim�ki
 */
public class Chest extends Thing {

    private int money;
    private Equipment contains;
    private boolean open = false;

    /** Creates a new instance of Chest */
    public Chest() {
        super("chest", "/images/arkku_kiinni.png");
    }

    public void tick() {
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean value) {
        if (value == true) {
            this.image = "/images/arkku.png";
        } else {
            this.image = "/images/arkku_kiinni.png";
        }

        open = value;
    }

    public void addItem(Equipment eq) {
        contains = eq;
    }

    public Equipment getEquipment() {
        return contains;
    }

    /**
     * Generates money and/or equipment to the chest. The piece of equipment has level definedLevel, while
     * the money amount is determined from the total level of monsters killed before the chest.
     *
     * @param definedLevel level of the area plus the level that was set for this chest
     * @param totalMonsterLevels total level of the monsters killed so far
     * @param numberOfRooms number of rooms so far in the area (not used currently)
     * @param itemQuality item quality set for this chest (-1 - 3) -1 means no eq
     *
     */
    public void generateContent(int totalMonsterLevels, int numberOfRooms, int definedLevel, int itemQuality) {

        // money comes directly from the levels of the monsters killed so far
        this.money = (int) (GameConstants.monsterMONEYConstant * totalMonsterLevels);

        System.out.println("Generating chest with level: " + totalMonsterLevels);

        // item quality -1 means no item
        if (itemQuality >= 0) {
            Random random = new Random(System.currentTimeMillis());
            if(itemQuality == 2) {
                // 25% dropchance for rares (blue), if the chest has quality 2
                if(random.nextFloat() < 0.75) {
                    itemQuality = 1;
                }
            }
            if(itemQuality == 1) {
                // 50% dropchance for uncommons (green)
                if(random.nextFloat() < 0.5) {
                    itemQuality = 0;
                }
            }
            addItem(EquipmentFactory.getInstance().createEquipment(definedLevel, itemQuality));
        }

    }

    public int getMoney() {
        return this.money;
    }
}
