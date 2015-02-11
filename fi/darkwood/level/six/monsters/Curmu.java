/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.six.monsters;

import fi.darkwood.Creature;
import fi.darkwood.Monster;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;

/**
 * Wraith healbursts itself when taken enough damage. (one time ability)
 * @author Ville
 */
public class Curmu extends Monster {

    private static int healBurst = 0;
    private static boolean burstUsed = false;

    public Curmu() {
        super("Cu'Rmu", "/images/monster/Wraith Master 80x74 10 frames.png", 80);
        setCreatureType(super.UNDEAD);
    }

    public void combatSpecial(Creature target) {
        if (this.health < this.maxHealth / 8 && !burstUsed) {
            healBurst = 10;
            burstUsed = true;
        }
        if (healBurst > 0) {
            this.heal(1000);
            healBurst -= 1;
            this.addAbilityEffect(new AbilityVisualEffect("/images/ability/effects/cleric/heal.png", 43, 9));
            MessageLog.getInstance().addMessage(Local.get("curmuheal"));

        }
    }
}
