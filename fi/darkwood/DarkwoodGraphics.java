/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.darkwood.util.Font;
import fi.darkwood.util.FontCanvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 * Wrapper class for javax.microedition.lcdui.Graphics
 * 
 * Adds fuctionality to Graphics.drawImage, Graphics.drawImageArea etc
 * 
 * @author Teemu Kivim�ki
 */
public class DarkwoodGraphics {

    private Graphics g;
    int xOffset;
    int yOffset;
    int screenHeight;
    int screenWidth;
    
    public final static Font FONT_ARIAL10 = Font.getInstance("/fonts/arial10_white_aa_shadows.bmf");
    public final static Font FONT_ARIAL10_BLACK = Font.getInstance("/fonts/arial10_black.bmf");
    public final static Font FONT_ARIAL10_GREEN = Font.getInstance("/fonts/arial10_green_aa_shadows.bmf");
    public final static Font FONT_ARIAL10_DARKGREEN = Font.getInstance("/fonts/arial10_darkgreen_aa.bmf");
    public final static Font FONT_ARIAL10_LIGHTBLUE = Font.getInstance("/fonts/arial10_lightblue_aa_shadows.bmf");
    public final static Font FONT_ARIAL10_RED = Font.getInstance("/fonts/arial10_red_aa.bmf");
    public final static Font FONT_ARIAL10_YELLOW = Font.getInstance("/fonts/arial10_yellow_aa_shadows.bmf");
    public final static Font FONT_ARIAL8 = Font.getInstance("/fonts/arial8.bmf");
    public final static Font FONT_TIMES8 = Font.getInstance("/fonts/times8.bmf");
    public final static Font FONT_ANTIQUA10_WHITE_BOLD = Font.getInstance("/fonts/antiqua10_bold_aa_shadows.bmf");
    public final static Font FONT_SMALL8_WHITE = Font.getInstance("/fonts/small8_white.bmf");
    public final static Font FONT_SMALL8_BLUE = Font.getInstance("/fonts/small8_blue.bmf");
    public final static Font FONT_TIMES20_BROWN = Font.getInstance("/fonts/times20_brown_aa_shadows.bmf");
    public final static Font FONT_TIMES12_BROWN = Font.getInstance("/fonts/times12_brown_aa_shadows.bmf");
    protected static FontCanvas messageViewer;
    public Image imageDisplayedOnScreen;
    
    /**
     * Constructor for DarkwoodGraphics
     * Give parameter g as graphics and parameters width and height for
     * offsetting screen (for example to draw the game at the center of mobile
     * phones screen).
     * 
     * @param g
     * @param width
     * @param height
     */
    public DarkwoodGraphics(Graphics graphics, int width, int height, Image imageDisplayedOnScreen) {
        g = graphics;
        xOffset = (width - 176) / 2;
        yOffset = (height - 208) / 2;
        screenHeight = height;
        screenWidth = width;
        this.imageDisplayedOnScreen = imageDisplayedOnScreen;
    }

    /**
     * Draw text with default font (arial10) in position x, y
     * @param text
     * @param x
     * @param y
     */
    public void drawText(String text, int x, int y) {
            drawText(text, x, y, 176, 176, 0, 0);
    }
    
/**
 * Draw text in x,y using font given as parameter
 * @param text
 * @param x
 * @param y
 * @param font Fonts are found in DarkwoodGraphics as static variables
 */
    public void drawText(String text, int x, int y, Font font) {
            drawText(text, x, y, 176, 176, 0, 0, font);
    }

    public void drawText(String text, int x, int y, int availableWidthFirstLine,
            int availableWidth, int padding, int textOrientation) {
            drawText(text, x, y, availableWidthFirstLine, availableWidth, padding, textOrientation, FONT_ARIAL10);
    }

    /**
     * Draw text
     * 
     * @param text Text to be drawn
     * @param x x coordinate
     * @param y y coordinate
     * @param availableWidthFirstLine Width for the first line
     * @param availableWidth Width for lines (other than the first)
     * @param padding Padding inside borders
     * @param textOrientation Text orientation
     * @param font Fonts are found in DarkwoodGraphics as static variables
     */
    public void drawText(String text, int x, int y, int availableWidthFirstLine,
            int availableWidth, int padding, int textOrientation, Font font) {
        messageViewer = font.getViewer(text);
        
        messageViewer.layout(availableWidthFirstLine, availableWidth, padding, textOrientation);
        messageViewer.paint(x + xOffset, y + yOffset, g);

    }

    public void drawImage(Image img, int x, int y, int anchor) {
        g.drawImage(img, x + xOffset, y + yOffset, anchor);
    }

    public void drawRegion(Image src,
            int x_src,
            int y_src,
            int width,
            int height,
            int transform,
            int x_dest,
            int y_dest,
            int anchor) {
        g.drawRegion(src, x_src, y_src, width, height, transform,
                x_dest + xOffset, y_dest + yOffset, anchor);
    }

    public void drawString(String str,
            int x,
            int y,
            int anchor) {
        g.drawString(str, x + xOffset, y + yOffset, anchor);
    }

    public void fillRect(int x,
            int y,
            int width,
            int height) {
        g.fillRect(x + xOffset, y + yOffset, width, height);
    }

    public void drawRect(int x,
            int y,
            int width,
            int height) {
        g.drawRect(x + xOffset, y + yOffset, width, height);
    }

    public void drawLine(int x1,
            int y1,
            int x2,
            int y2) {
        g.drawLine(x1 + xOffset, y1 + yOffset, x2 + xOffset, y2 + yOffset);
    }

    public void setColor(int RGB) {
        g.setColor(RGB);
    }

    public void setColor(int red,
            int green,
            int blue) {
        g.setColor(red, green, blue);
    }

    public Graphics getGraphics() {
        return g;
    }

    public void setRefPixelPosition(Sprite s, int x, int y) {
        s.setRefPixelPosition(x + xOffset, y + yOffset);
    }
    
    public void defineReferencePixel(Sprite s, int x, int y) {
        s.defineReferencePixel(x, y);
    }

    /**
     * Draw four black rectangles on the borders of the screen, so any extra
     * screen in addition to the 176x208 game screen is filled with black
     * (sometimes things are drawn partly outside the game area)
     */
    public void fillBorders() {
        g.setColor(0x000000);
        //g.fillRect(0, 0, screenWidth, screenHeight);

        g.fillRect(0, 0, xOffset, screenHeight);
        g.fillRect(screenWidth - xOffset, 0, xOffset, screenHeight);
        g.fillRect(0, 0, screenWidth, yOffset);
        g.fillRect(0, screenHeight - yOffset, screenWidth, yOffset);
    
    }
    
}
