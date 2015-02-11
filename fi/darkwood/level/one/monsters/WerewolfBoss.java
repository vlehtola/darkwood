/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.one.monsters;

import fi.darkwood.Creature;
import fi.darkwood.Monster;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class WerewolfBoss extends Monster {

    private boolean specialUsed = false;

    /** Creates a new instance of WerewolfBoss */
    public WerewolfBoss() {
        super(Local.get("darkwolf"), "/images/monster/werewolf/werewolf_dark.png", 44);
        setCreatureType(super.UNDEAD);
        setCreatureType(super.HUMANOID);
    }

    public void combatSpecial(Creature target) {
        if (!specialUsed) {
            this.addAbilityEffect(new AbilityVisualEffect("/images/ability/effects/enrage.png", 44, 11));
            this.specialUsed = true;
        }
    }
}
