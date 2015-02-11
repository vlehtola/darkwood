/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mirake;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author Teemu
 */
public class UnregisteredInfoCanvas extends GameCanvas {

    /**
     * Init democanvas
     * @param midlet The midlet that needs to be notified when demo is complete
     */

    Image image = null;
    public UnregisteredInfoCanvas() {
        super(true);
        
        setFullScreenMode(true);

        try {
                image = Image.createImage("/images/ui/silver_account_screen.png");
            } catch (Exception e) {
                // causes crash if cannot load images, which is pretty natural..
            }
    }
    boolean running;

    private boolean mTrucking = true;
    
    public void start() {
        run();

    }

    public void run() {
        while (mTrucking) {

            // check if keys have been pressed - exit if anykey has been pressed
            // get regular key states (excluding softkeys, which are handled in method keyPressed() )
            int keyState = getKeyStates();
            // check both regular keys and softekeys
            if (keyState != 0 || softKeyPressed != 0) {
                break;
            }


            repaint();

            try {
                Thread.sleep(20);
            } catch (Exception e) {
            }
        }
        System.out.println("Unregistered screen exit");

    }

    // draw the image to the canvas.
    public void paint(Graphics g) {
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(image, getWidth()/2 - image.getWidth()/2, getHeight()/2 - image.getHeight() / 2, 0);

//        g.setColor(100, 100, 100);
//        g.drawString("Silver account", 10, getHeight()/2, 0);
//        g.drawString("Silver accounts can only level characters until level 10. Upgrade to Gold account at http://darkwood.cc!", 10, getHeight()/2+30, 0);

       
    }
    private int softKeyPressed = 0;

    /* Catch softkey presses */
    public void keyPressed(int keyCode) {
        System.out.println("softkey: " + keyCode);
        softKeyPressed = keyCode;
    }
}
