/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.six;

import fi.darkwood.*;
import fi.darkwood.util.Utils;
import fi.mirake.Local;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Ville
 */
public class CapturedQueen extends UsableThing {

    public CapturedQueen() {
        super("captured Queen", "/images/Captured Queen 70x99 23 frames.png", 70);

    }

    public void tick() {
    }

    public boolean useThing() {
        if (used) {
            return used;
        }
        this.used = true;
        // Free the queen
        // change the sprite
        Image imageBin = Utils.getInstance().getImage("/images/Captured Queen freed 70x99 9frames.png");
        this.sprite = new Sprite(imageBin, 70, imageBin.getHeight());
        sprite.setFrame(0);
        this.frameWidth = 70;
        // Complete a quest, chatbox etc.
        Quest quest = Game.player.currentQuest;
        if(quest != null)
            quest.awardRequirement(Local.get("tier6.quest5.objective"));
        return used;
    }

    public String queryUsedText() {
        return "";
    }

    public String queryUnusedText() {
        return Local.get("tier6.queen.action");
    }
}
