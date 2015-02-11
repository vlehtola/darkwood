/*
 * View.java
 *
 * Created on 20. toukokuuta 2007, 16:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ui.view;

import fi.darkwood.DarkwoodCanvas;
import fi.darkwood.DarkwoodGraphics;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Teemu Kivim�ki
 */
public abstract class ViewAndControls {

    protected String leftSoftKeyText = "";
    protected String rightSoftKeyText = "";
    protected int xOffset = 0;
    protected int yOffset = 0;
    protected final int GAME_WIDTH = 176;
    protected final int GAME_HEIGHT = 208;

    // actual phone screen resolution
    private int width;
    private int height;
    
    
    protected ViewAndControls(int width, int height) {
        xOffset = (width - 176) / 2;
        yOffset = (height - 208) / 2;    

        this.width = width;
        this.height = height;
    }
    
    protected void drawSoftKeyTexts(DarkwoodGraphics g) {
        int availableWidth = 60;
        int padding = 0;

        int textOrientation = Graphics.HCENTER;

        g.drawText(leftSoftKeyText, 27, 199,
            availableWidth, availableWidth, padding, textOrientation);
//        messageViewer = this.bitMapFont.getViewer(leftSoftKeyText);
//        messageViewer.layout(availableWidth, availableWidth, padding, textOrientation);
//        messageViewer.paint(0, 195, g.getGraphics());

//        textOrientation = Graphics.RIGHT;

        g.drawText(rightSoftKeyText, 149, 199,
            availableWidth, availableWidth, padding, textOrientation);

//        messageViewer = this.bitMapFont.getViewer(rightSoftKeyText);
//        messageViewer.layout(availableWidth, availableWidth, padding, textOrientation);
//        messageViewer.paint(126, 195, g.getGraphics());

    }
/*
    protected void drawImage(Graphics g, Image img, int x, int y, int anchor) {
        g.drawImage(img, x + xOffset, y + yOffset, anchor);
    }
*/
    public abstract void updateScreen(DarkwoodGraphics g);

    public abstract void checkInput(int keyState);

    /* Touch screen handling
     * x and y are the point where the touch was released
     */
    public abstract void pointerReleasedEvent(int x, int y);
    public abstract void pointerPressedEvent(int x, int y);
}
