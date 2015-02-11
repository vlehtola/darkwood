/*
 * AbilityVisualEffect.java
 *
 * Created on September 16, 2007, 6:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ability;

import fi.darkwood.util.Utils;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Ville Lehtola
 */
public class AbilityVisualEffect {
    // current duration in frames
    private int duration;
    // duration
    private int originalDuration;
    private Image image;
    private static final Utils utils = Utils.getInstance();
    public Sprite sprite;
    private String imageFile;
    public int frameWidth;

    // some ability visual effects need to be matched to the center of the creature, others at the bottom
    public boolean matchImageBottom = false;
    
    /**
     * Create a new ability visual effect
     * 
     * @param imageFile file name of the sprite
     * @param width width of one frame in the sprite
     * @param duration duration is frames
     */
    public AbilityVisualEffect(String imageFile, int width, int duration) {
        this.imageFile = imageFile;
        image = utils.getImage(imageFile);
        sprite = new Sprite(image, width, image.getHeight());
        frameWidth = width;
        sprite.defineReferencePixel(sprite.getWidth() / 2, sprite.getHeight());

        this.originalDuration = duration;
        this.duration = duration;

    }
    
    // return true if effect ends
    public boolean checkDuration() {
        duration -= 1;
        if (duration <= 0) {
            duration = getOriginalDuration();
            sprite.setFrame(0);
            return true;
        }
        return false;
    }

    public int getOriginalDuration() {
        return originalDuration;
    }

    public void setOriginalDuration(int originalDuration) {
        this.originalDuration = originalDuration;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public void reset() {
        sprite.setFrame(0);
        duration = originalDuration;
    }
}
