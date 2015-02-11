/*
 * CityRoom.java
 *
 * Created on 20. toukokuuta 2007, 15:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood.room;

import fi.darkwood.*;
import fi.darkwood.util.Utils;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Teemu Kivim�ki
 */
public class CityRoom extends Room {
    public String cityDesc;
    
    private Sprite sprite = null;
    private Sprite sndSprite = null;
    private Sprite eyesSprite = null;
    private static final Utils utils = Utils.getInstance();
    public int spriteX;
    public int spriteY;
    private int[] spriteFrameTime;
    public int eyesSpriteX;
    public int eyesSpriteY;
    private int[] eyesSpriteFrameTime;
    public int sndSpriteX;
    public int sndSpriteY;
    private int[] sndSpriteFrameTime;
    
    /** Creates a new instance of CityRoom */
    public CityRoom(Zone zone,String name, String background, int id) {
        super(zone, name, background, id);
    }

    public void setSpriteImage(String image, int width, int x, int y) {
        Image img = utils.getImage(image);
        sprite = new Sprite(img, width, img.getHeight());
        spriteX = x;
        spriteY = y;
    }    

        
    public void setSpriteFrameTime(int[] time) {
        spriteFrameTime = time;
    }

    public int getSpriteFrameTime(int frame) {
        return spriteFrameTime[frame];
    }
    
    public Sprite getSprite() {
        return sprite;
    }
    public void setSndSpriteImage(String image, int width, int x, int y) {
        Image img = utils.getImage(image);
        sndSprite = new Sprite(img, width, img.getHeight());
        sndSpriteX = x;
        sndSpriteY = y;
    }


    public void setSndSpriteFrameTime(int[] time) {
        sndSpriteFrameTime = time;
    }

    public int getSndSpriteFrameTime(int frame) {
        return sndSpriteFrameTime[frame];
    }

    public Sprite getSndSprite() {
        return sndSprite;
    }

    
    public void setEyesSpriteImage(String image, int width, int x, int y) {
        Image img = utils.getImage(image);
        eyesSprite = new Sprite(img, width, img.getHeight());
        eyesSpriteX = x;
        eyesSpriteY = y;
    }
    
    /**
     * Give an array for frame times in animations (if frames need to be displayed for a different time)
     * This is because otherwise some animations images would have same frame for 20 times or such.
     * Give an int value for each frame. Default frame rate is 10 per second.
     * @param time
     */
    public void setEyesSpriteFrameTime(int[] time) {
        eyesSpriteFrameTime = time;
    }

    public int getEyesSpriteFrameTime(int frame) {
        return eyesSpriteFrameTime[frame];
    }
    
    public Sprite getEyesSprite() {
        return eyesSprite;
    }
}
