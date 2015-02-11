/*
 * CombatView.java
 *
 * Created on 17. toukokuuta 2007, 2:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ui.view;

import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.Game;
import fi.darkwood.util.Utils;
import fi.mirake.Local;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Teemu Kivim�ki
 */
public class PauseView extends ViewAndControls {

    private Utils utils = Utils.getInstance();

    public PauseView(Game game, int width, int height) {
        super(width, height);
        buttonsImg = utils.getImage("/images/ui/frames_buttons.png");
        img = utils.getImage("/images/paused.png");
        rightSoftKeyText = Local.get("buttons.resume");
    }
    private Image img;
    private Image buttonsImg;

    public void updateScreen(DarkwoodGraphics g) {


        // draw background
        g.drawImage(img, 0, 40, 0);

        g.drawImage(buttonsImg, 0, 0, 0);

        drawSoftKeyTexts(g);
      
    }

   
    public void checkInput(int keyState) {
        if (keyState != 0) { Game.setPaused(false); }
    }


    // handle touch phone controls
    public void pointerReleasedEvent(int x, int y) {
    }

    public void pointerPressedEvent(int x, int y) {

    }

}
