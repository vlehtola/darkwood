/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mirake;

import fi.darkwood.DarkwoodCanvas;
import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.GameConstants;
import fi.darkwood.util.Utils;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author Teemu
 */
public class SelectMusicCanvas extends GameCanvas {

    /**
     * Init musiccanvas
     * @param midlet The midlet that needs to be notified when demo is complete
     */
    public SelectMusicCanvas() {
        super(true);

        setFullScreenMode(true);

        try {
            buttonsImg = utils.getImage("/images/ui/frames_buttons.png");
        } catch (Exception e) {
        }


    }
    private int width; // get actual screen width
    private int height; // get actual screen height
    Image tmp2 = Image.createImage(176, 208);
    Graphics grap = tmp2.getGraphics();
    DarkwoodGraphics g = new DarkwoodGraphics(grap, 176, 208, tmp2);
    Image temp;
    private Utils utils = Utils.getInstance();
    Image buttonsImg;
    boolean running;
    private int softKeyPressed = 0;

    public void startCanvas() {
        run();
    }

    public void keyPressed(int keyCode) {
        softKeyPressed = keyCode;
    }

    /* Touch screen handling
     * x and y are the point where the touch was released
     */
    public void pointerReleased(int x, int y) {
        if (landscapeMode) return;

        // handle graphics resizing effect, due to buttons appearing at
        // different x y locations depending on screen size
        int reducedX = x * 176 / width;
        int reducedY = y * 208 / height;

        // SOFTKEY HANDLING
        // left softkey handling (softkey1)
        if (reducedX < 85 && reducedY > 170) {
            softKeyPressed = DarkwoodCanvas.SOFTKEY1_PRESSED;
        }

        // right softkey handling (softkey2)
        if (reducedX > 100 && reducedY > 170) {
            softKeyPressed = DarkwoodCanvas.SOFTKEY2_PRESSED;
        }

        // OTHER CONTROLS HANDLING
        // implement if needed..
    }

    public void run() {
        width = getWidth();
        height = getHeight();

        sizeChanged(width, height);

        // if we start in landscape mode, reverse
        if (width > height) {
            int tempw = width;
            width = height;
            height = tempw;
        }

        while (true) {
            int keyState = getKeyStates();

            if (softKeyPressed == DarkwoodCanvas.SOFTKEY1_PRESSED && !landscapeMode) {
                // music off
                SoundPlayer.enableMusic(false);
                break;
            }
            if (softKeyPressed == DarkwoodCanvas.SOFTKEY2_PRESSED && !landscapeMode) {
                // music on
                SoundPlayer.enableMusic(true);
                // start music
                SoundPlayer.playTitleScreenMusic();
                break;
            }



            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }

            updateScreen(g);
            drawSoftKeyTexts(g);


            temp = resizeImage(tmp2);

            if (landscapeMode) {
                try {
                    temp = Utils.rotateImage(temp, 90);
                } catch (Exception e) {
                    System.out.println("Error rotating in selectmusiccanvas");
                    e.printStackTrace();
                }
            }

            getGraphics().drawImage(temp, 0, 0, 0);

            flushGraphics();
        }

    }
    private Image img;
    // draw the image to the canvas.

    public void updateScreen(DarkwoodGraphics g) {

        
        if (landscapeMode) {
            img = utils.getImage("/images/landscape.png");
            g.drawImage(img, 0, 0, 0);
            return;
        }


        g.setColor(0, 0, 0);
        g.fillRect(0, 0, getWidth(), getHeight());

        int availableWidth = 160;
        int padding = 0;

        int textOrientation = Graphics.HCENTER;

        g.drawText(Local.get("music.enablemusic"), 178 / 2, 208 / 2,
                availableWidth, availableWidth, padding, textOrientation);


        g.drawImage(buttonsImg, 0, 0, 0);
        //drawSoftKeyTexts(g);


    }

    protected void drawSoftKeyTexts(DarkwoodGraphics g) {
        if (landscapeMode) return;

        int availableWidth = 60;
        int padding = 0;

        int textOrientation = Graphics.HCENTER;

        g.drawText(Local.get("buttons.no"), 27, 199,
                availableWidth, availableWidth, padding, textOrientation);

        g.drawText(Local.get("buttons.yes"), 149, 199,
                availableWidth, availableWidth, padding, textOrientation);


    }
    int srcWidth = 176;//imageDisplayedOnScreen.getWidth();
    int srcHeight = 208; //imageDisplayedOnScreen.getHeight();
    Image tmp = null;
    Graphics go = null;
    int ratio = (srcWidth << 16) / 176;
    int pos = ratio / 2;
    Image resizedImage = null;

    private Image resizeImage(Image src) {
//        return src;

        if (GameConstants.SCALE_GRAPHICS_TO_FULLSCREEN == false) {
            return src;
        }

        if (tmp == null) {
            tmp = Image.createImage(width, srcHeight);
        }

        go = tmp.getGraphics();

        ratio = (srcWidth << 16) / width;
        pos = ratio / 2;

        //Horizontal Resize

        for (int x = 0; x < width; x++) {
            go.setClip(x, 0, 1, srcHeight);
            go.drawImage(src, x - (pos >> 16), 0, Graphics.LEFT | Graphics.TOP);
            pos += ratio;
        }

        if (resizedImage == null) {
            resizedImage = Image.createImage(width, height);
        }
        go = resizedImage.getGraphics();
        ratio = (srcHeight << 16) / height;
        pos = ratio / 2;

        //Vertical resize

        for (int y = 0; y < height; y++) {
            go.setClip(0, y, width, 1);
            go.drawImage(tmp, 0, y - (pos >> 16), Graphics.LEFT | Graphics.TOP);
            pos += ratio;
        }

        return resizedImage;

    }//resize image 

    protected void sizeChanged(int w, int h) {


        // dont use sizeChanged function.. some phones have w > h on default..
        // update: 23.1.2011 handle landscape mode afterall
        if (w > h) {
            landscapeMode = true;
        } else {
            landscapeMode = false;
        }
        //super.sizeChanged(w, h);
    }
    private boolean landscapeMode = false;
}
