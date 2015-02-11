
package fi.darkwood.initialEquipment;

import fi.darkwood.equipment.Weapon;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class WarriorWeapon extends Weapon{
    
       
    public WarriorWeapon() {
        super(Local.get("warrior.weapon.name"),Local.get("warrior.weapon.name"),"/images/equipment_icons/weapon.png", 2, 1);
        setPaperdollImage("/images/equipment/paperdoll/warrior/short-sword.png");
    }
    
}
