package fi.darkwood.initialEquipment;

import fi.darkwood.equipment.*;
import fi.mirake.Local;

/**
 * Starting weapon for cleric and mage
 * @author Ville Lehtola
 */
public class PlainStaff extends Weapon {

    public PlainStaff() {
        super(Local.get("mage.weapon.name"), Local.get("mage.weapon.name"), "/images/equipment_icons/weapon.png", 2, 1);
        setPaperdollImage("/images/equipment/paperdoll/mage/staff.png");
    }
}
