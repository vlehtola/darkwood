/*
 * CombatView.java
 *
 * Created on 17. toukokuuta 2007, 2:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ui.view;

import fi.darkwood.ui.view.ViewAndControls;
import fi.darkwood.util.Font;
import fi.darkwood.util.FontCanvas;
import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.Game;
import fi.darkwood.Logger;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author Teemu Kivim�ki
 */
public class ConsoleView extends ViewAndControls {

    Game game;
    private Font bitMapFont = Font.getInstance("/fonts/arial10.bmf");
    private FontCanvas messageViewer = this.bitMapFont.getViewer("Hello World!");

    public ConsoleView(Game game, int width, int height) {
        super(width, height);
        this.game = game;
    }

    public void updateScreen(DarkwoodGraphics g) {

        g.setColor(0xFFFF00);
        int ycoord = 0;
        String log = "";
        for (int i = 0; i < 10 && i < Logger.getInstance().messages.size(); i++) {
            log = log + "\n" + (String) (Logger.getInstance().messages.elementAt(i));

        }
        g.drawText(log, 0, ycoord, 176, 176, 0, 0);

    }

    public void checkInput(int keyState) {
        if (keyState == GameCanvas.GAME_A_PRESSED) {
            game.showConsole = false;
        }
    }

    public void pointerReleasedEvent(int x, int y) {
        // implement
    }
     public void pointerPressedEvent(int x, int y) {

    }
}
