/*
 * Ability.java
 *
 * Created on May 9, 2007, 5:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;

/**
 *
 * @author Tommi Laukkanen, Teemu Kivim�ki
 */
public abstract class Ability {
    
    /** Creates a new instance of Ability */
    public Ability(String name, String iconImage, int level) {
        this.name = name;
        this.image = iconImage;
        this.pressedImage = image.substring(0, image.length() - 4) + "_pressed.png";
        this.bigImage = image.substring(0, image.length() - 4) + "_big.png";
        this.pressedBigImage = image.substring(0, image.length() - 4) + "_pressed_big.png";
        this.abilityLevel = level;
    }

    public String name;
    public String desc;
    protected String image;
    protected String pressedImage;
    protected String bigImage;
    protected String pressedBigImage;
    protected int abilityLevel;
    private AbilityVisualEffect targetVisualEffect = null;
    private AbilityVisualEffect selfVisualEffect = null;

// defines the ability slot in the player object (0-3)
    private int abilitySlot;

    public String getImage(boolean pressed, boolean big) {
        if(big && pressed)
            return this.pressedBigImage;
        else if(big)
            return this.bigImage;
        else if(pressed)
            return this.pressedImage;
        else
            return this.image;
    }
    public void invoke( Creature invoker) {        
        if (!Game.party.isActive() || (Game.party.isActive() && Game.party.isLeader)) {
            // party is not active or player is party leader
            if (effect(invoker)) {
                String msg = invoker.name + " "+Local.get("ability.invokes")+" " + name + "!";
                MessageLog.getInstance().addMessage(msg);
                Game.party.sendAbilityUseToClients(msg);
            }

        } 
        else {
            // party active, player is member
            Game.party.sendAbilityUseToLeader(Game.player.getId(), this);
       //     game.party.connectionToLeader.send("invoke:" + game.player.getId() + "/"+ this.getClass().getName() + "," + this.abilityLevel + ",");
        }
    }

    /**
     * Returns true if ability used succesfully, otherwise false (failed due to not enough energy or such)
     * @param game
     * @param invoker The Creature invoking this ability
     * @return Ability use result. True successful, false unsuccesful. Can fail for example due to no target, not enough mana, cooldown.
     */
    public abstract boolean effect( Creature invoker);

    public abstract String getDescription();

    public abstract int getAbilityRank(); // from 1 to 4

    public int getLevel() {
        return abilityLevel;
    }

    public void setLevel(int level) {
        this.abilityLevel = level;
    }

    public AbilityVisualEffect getSelfVisualEffect() {
        return selfVisualEffect;
    }

    public void setSelfVisualEffect(AbilityVisualEffect selfVisualEffect) {
        this.selfVisualEffect = selfVisualEffect;
    }

    public void setTargetVisualEffect(AbilityVisualEffect effect) {
        targetVisualEffect = effect;
    }

    public AbilityVisualEffect getTargetVisualEffect() {
        return targetVisualEffect;
    }

    /**
     * @return the abilitySlot
     */
    public int getAbilitySlot() {
        return abilitySlot;
    }

    /**
     * @param abilitySlot the abilitySlot to set
     */
    public void setAbilitySlot(int abilitySlot) {
        this.abilitySlot = abilitySlot;
    }

    public abstract double getCooldownInRounds();

}
