
package fi.darkwood.initialEquipment;

import fi.darkwood.equipment.*;
import fi.mirake.Local;

/**
 * 
 * @author Ville Lehtola
 */
public class ClericChest extends ChestArmour {
    
    public ClericChest() {
        super(Local.get("cleric.chest.name"), Local.get("cleric.chest.name"),"/images/equipment_icons/armour.png", 1);
        setPaperdollImage("/images/equipment/paperdoll/cleric/priest-toga.png");
    }
    
}
