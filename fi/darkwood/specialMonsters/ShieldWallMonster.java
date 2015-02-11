/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.specialMonsters;

import fi.darkwood.Creature;
import fi.darkwood.Monster;
import fi.darkwood.ability.AbilityVisualEffect;

/**
 *
 * @author Ville
 */
public class ShieldWallMonster extends Monster {
    private boolean specialUsed = false;

    public ShieldWallMonster(String name, String image, int monsterFrameWidth) {
        super(name, image,monsterFrameWidth);
    }
    public void combatSpecial(Creature target) {
       if(!specialUsed) {
            this.addAbilityEffect(new AbilityVisualEffect("/images/ability/effects/shield_wall_mirror.png", 47, 5));
            this.specialUsed = true;
       }
    }
}
