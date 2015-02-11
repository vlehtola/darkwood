/*
 * Monster.java
 *
 * Created on May 4, 2007, 8:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

/**
 *
 * @author Tommi Laukkanen, Teemu Kivim�ki
 */
public class Monster extends Creature {

    private int monsterId;
    private boolean elite;

    /**
     * Create a monster instance.
     * 
     * @param name
     * @param description
     * @param image
     * @param level
     * @param monsterFrameWidth The width of a frame in this monster's PNG image, used to create monster Sprite
     */
    public Monster(String name, String image, int monsterFrameWidth, int level) {
        super(name, image, level, monsterFrameWidth);
        this.money = GameConstants.getMonsterMoney(level);
        this.setLevel(level); // set the stats straight
    }

    public Monster(String name, String image, int monsterFrameWidth) {
        super(name, image, 0, monsterFrameWidth);
    }

    public void setLevel(int level) {
        this.level = level;
        this.money = GameConstants.getMonsterMoney(level);
        updateStats();
        this.health = maxHealth;
        this.mana = maxMana;
    }

    public void updateStats() {
        double msc = GameConstants.monsterStatConst;
        this.strength = (int) msc * level;
        this.dexterity = (int) msc * level;
        this.constitution = (int) msc * level;
        this.willpower = (int) msc * level;
        super.restartBuffs();
        updateMaxHealthAndMana();
    }

    public void updateMaxHealthAndMana() {
        maxHealth = (int) (constitution * 0.7 + (strength + dexterity + willpower) * 0.1) * GameConstants.monsterHPConstant + 1;
        if (elite) {
            maxHealth = (int) maxHealth * GameConstants.monsterELITEHPConstant;
        }
        maxMana = (int) (willpower * 0.9 + constitution * 0.1) * GameConstants.monsterMPConstant + 1;
    }

    public int getId() {
        return monsterId;
    }

    public void setId(int id) {
        monsterId = id;
    }

    public boolean hates(Creature creature) {
        return creature instanceof Player;
    }

    public double getDamage() {
        // The reduced unit for time is 1 round
        return GameConstants.getMonsterDPS(level);
    }

    public void awardKill(Creature c) {
        // monsters dont do anything with exp, do nothing..
    }
    public void combatSpecial(Creature c) {
        // c is the target, if there is a special
    }
/**
 * Toggle mob elite status. Elite mobs have 3x more hp (defined in GameConstants monsterELITEHPConstant)
 * @param e
 */
    public void setElite(boolean e) {
        elite = e;
        updateMaxHealthAndMana();
        this.health = maxHealth;
        this.mana = maxMana;
    }

    public boolean isElite() {
        return elite;
    }
    
}
