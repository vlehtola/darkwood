

package fi.darkwood.initialEquipment;

import fi.darkwood.equipment.*;
import fi.mirake.Local;

/**
 * Starting weapon for cleric and mage
 * @author Ville Lehtola
 */
public class WoodenClub extends Weapon {

    public WoodenClub() {
        super(Local.get("cleric.weapon.name"),Local.get("cleric.weapon.name"),"/images/equipment_icons/weapon.png", 2, 1);
        setPaperdollImage("/images/equipment/paperdoll/cleric/club.png");
    }

}
