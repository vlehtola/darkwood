/*
 * Thing.java
 *
 * Created on May 4, 2007, 8:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.darkwood.room.Room;
import fi.darkwood.util.Utils;
import java.util.Random;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Teemu Kivim�ki
 */
public abstract class Thing {

    private static final Random random = new Random(System.currentTimeMillis());

    /** Creates a new instance of Thing */
    public Thing(String name, String image) {
        this.name = name;
        this.image = image;
        sprite = Utils.getInstance().getSprite(image, 0);
    }

    public Thing(String name, String image, int frameWidth) {
        this.name = name;
        this.image = image;

        Image imageBin = Utils.getInstance().getImage(image);

        sprite = new Sprite(imageBin, frameWidth, imageBin.getHeight());

        // if more than one frame, random from which frame to start the animation
        int rnd = 0;
        if (sprite.getFrameSequenceLength() > 1) {
            rnd = random.nextInt(sprite.getFrameSequenceLength() - 1);
        }
        sprite.setFrame(rnd);

        this.frameWidth = frameWidth;
    }
    public String name;
    public String image;
    public Room room;
    protected Sprite sprite;
    protected int frameWidth;

    public Sprite getSprite() {
        return sprite;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public abstract void tick();
}
