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
import javax.microedition.lcdui.Image;

/**
 *
 * @author Teemu Kivim�ki
 */
public class LandscapeView extends ViewAndControls {

    private Utils utils = Utils.getInstance();

    public LandscapeView(Game game, int width, int height) {
        super(width, height);
   
    }
    private Image img;

    public void updateScreen(DarkwoodGraphics g) {


        img = utils.getImage("/images/landscape.png");
        // draw background
        g.drawImage(img, 0, 0, 0);

 
      
    }

   
    public void checkInput(int keyState) {
        
    }


    // handle touch phone controls
    public void pointerReleasedEvent(int x, int y) {
    }

    public void pointerPressedEvent(int x, int y) {

    }

}
