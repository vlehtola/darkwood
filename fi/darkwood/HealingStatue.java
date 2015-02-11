/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood;

/**
 *
 * @author Ville
 */
public class HealingStatue extends UsableThing {

    public HealingStatue() {
        super("statue", "/images/statue small.png");

    }

    public void tick() {
    }

    public boolean useThing() {
        if (used) {
            return used;
        }
        this.used = true;

        // do not call heal() for it displays a upgoing msg and activates the combat view                        
        int healAmount = this.level * 50;
        int toBeHealed = Game.player.maxHealth - Game.player.health;
        if (healAmount > toBeHealed) {
            healAmount = toBeHealed;
        }
        Game.player.health += healAmount;
        Game.party.syncParty();
        return used;
    }

    public String queryUsedText() {
        return "";
    }

    public String queryUnusedText() {
        return "Touch";
    }
}
