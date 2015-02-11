
package fi.darkwood.initialEquipment;

import fi.darkwood.equipment.*;
import fi.mirake.Local;

/**
 * 
 * @author Ville Lehtola
 */
public class MageChest extends ChestArmour {
    
    public MageChest() {
        super(Local.get("mage.chest.name"), Local.get("mage.chest.name"),"/images/equipment_icons/armour.png", 1);
        setPaperdollImage("/images/equipment/paperdoll/mage/common-robe.png");
    }
    
}
