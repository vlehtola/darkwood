/*
 * Copyright Mirake Ltd. All Rights reserved.
 */

package fi.darkwood;

/**
 *
 * @author Administrator
 */
public class Potion extends Item {
    public Potion() {
        super("Health potion", "Health potion", "/images/equipment_icons/potion_icon.png", 10, 1);
    }

    public String getItemInfo() {
        return "A healing potion";
    }

}
