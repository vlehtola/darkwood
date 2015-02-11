
package fi.darkwood.initialEquipment;

import fi.darkwood.equipment.*;
import fi.mirake.Local;

/**
 * 
 * @author Ville Lehtola
 */
public class WarriorChest extends ChestArmour {
    
    public WarriorChest() {
        super(Local.get("warrior.chest.name"),Local.get("warrior.chest.name"),"/images/equipment_icons/armour.png", 1);
        setPaperdollImage("/images/equipment/paperdoll/warrior/cuirass.png");
    }
    
}
