/*
 * Expbar.java
 *
 * Created on 30. syyskuuta 2007, 18:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ui.component;

import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.Player;
import fi.darkwood.util.Utils;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 * Expbar singleton used by all the UI views
 *
 * @author Teemu Kivim�ki
 */
public class Expbar {
    // the speed of bar size increase (pixels per frame)

    double STEPSIZE = 2.0;
    private final static int BARSTOP = 118;
    private final static int BARSTART = 45;
    private final static Expbar INSTANCE = new Expbar();
    Utils utils = Utils.getInstance();
    private float currentLength = 0;
    private float currentLengthRed = 0;
    private int lastLevel;

    /** Creates a new instance of Expbar */
    private Expbar() {
    }

    public static Expbar getInstance() {
        return INSTANCE;
    }

    public void prepare(int expCurrent, int expToLevel) {
        int targetLength = (int) ((double) expCurrent / (double) expToLevel * BARSTOP);
        currentLength = targetLength;
    }

    /**
     * Draw the exp bar. Also handle animation of the bar (slow increase)
     * 
     * @param g
     * @param expCurrent
     * @param expToLevel
     * @param level
     */
    public void drawBar(DarkwoodGraphics g, Player player) {
        int expCurrent = player.experience;
        int expToLevel = player.getExpRequiredForNextLevel();
        int level = player.level;

        if (lastLevel == 0) {
            lastLevel = level;
        }
        int targetLength = (int) ((double) expCurrent / (double) expToLevel * BARSTOP);

// draw exp bar
        g.setColor(0x0000FF);
        Image img = utils.getImage("/images/ui/exp_bar.png");
        g.drawRegion(img, 0, 0, (int) currentLength, img.getHeight(), Sprite.TRANS_NONE, BARSTART, 19, 0);


        // draw the rightmost end of the bar (3 pixels, or less if we are at beginning of bar)
        int end = 3;
        if (currentLength < 3 && currentLength <= targetLength) {
            end = (int) currentLength;
        }
        g.drawRegion(img, img.getWidth() - end, 0, end, img.getHeight(), Sprite.TRANS_NONE, (int) currentLength + (BARSTART - 3), 19, 0);

// kasvata expabaaria pikkuhiljaa jos on tullu expaa
        if (currentLength != targetLength) {
            currentLength += STEPSIZE;
            if (currentLength > targetLength && lastLevel == level) {
                currentLength = targetLength;
            }

            if (currentLength > BARSTOP) {
                lastLevel = level;
                currentLength = 0;
            }
        }

        drawExpTextOnBar(g, player);

        // if player has death penalty, draw red bar
        if (player.getDeathPenaltyExpBuffer() > 0) {
            drawRedBar(g, player);
        }
    }

    public void drawRedBar(DarkwoodGraphics g, Player player) {
        int expCurrent = player.getDeathPenaltyExpBuffer();
        int expToLevel = player.getExpRequiredForNextLevel();
        int level = player.level;

        if (lastLevel == 0) {
            lastLevel = level;
        }
        int targetLength = (int) ((double) expCurrent / (double) expToLevel * BARSTOP);

// draw exp bar
        g.setColor(0x0000FF);
        Image img = utils.getImage("/images/ui/exp_bar_red.png");
        g.drawRegion(img, 0, 0, (int) currentLengthRed, img.getHeight(), Sprite.TRANS_NONE, BARSTART, 19, 0);


        // draw the rightmost end of the bar (3 pixels, or less if we are at beginning of bar)
        int end = 3;
        if (currentLengthRed < 3 && currentLengthRed <= targetLength) {
            end = (int) currentLengthRed;
        }
        g.drawRegion(img, img.getWidth() - end, 0, end, img.getHeight(), Sprite.TRANS_NONE, (int) currentLengthRed + (BARSTART - 3), 19, 0);

// kasvata expabaaria pikkuhiljaa jos on tullu expaa
        if (currentLengthRed != targetLength) {
            currentLengthRed += STEPSIZE;
            if (currentLengthRed > targetLength && lastLevel == level) {
                currentLengthRed = targetLength;
            }

            if (currentLengthRed > BARSTOP) {
                lastLevel = level;
                currentLengthRed = 0;
            }
        }

        //   drawExpTextOnBar(g, player);
    }

    private void drawExpTextOnBar(DarkwoodGraphics g, Player player) {
        // draw exp amount / exp required
        g.drawText(player.experience + " / " + player.getExpRequiredForNextLevel(), 110, 18, 120, 120, 0, Graphics.HCENTER, DarkwoodGraphics.FONT_SMALL8_BLUE);

    }
}
