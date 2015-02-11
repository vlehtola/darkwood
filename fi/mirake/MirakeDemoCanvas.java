/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mirake;

import fi.darkwood.util.Utils;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author Teemu
 */
public class MirakeDemoCanvas extends GameCanvas {

    /**
     * Init democanvas
     * @param midlet The midlet that needs to be notified when demo is complete
     */
    public MirakeDemoCanvas() {
        super(true);

        setFullScreenMode(true);

        // Load images. Use mGraphics to draw on the backbuffer.
        mBufferImage = Image.createImage(getWidth(), getHeight());
        mGraphics = mBufferImage.getGraphics();
        try {
            mJavaImage = Image.createImage("/images/mirake_logo.png");
            mPresentsImage = Image.createImage("/images/presents_text.png");
//            mBgImage = Image.createImage("/bg.png");
        } catch (Exception e) {
        }

        // Allocate mamory for the image array.
        // Use the getRGB method to get all ARGB values from the image to the rawInt array.
        rawInt = new int[mJavaImage.getWidth() * mJavaImage.getHeight()];

        mJavaImage.getRGB(rawInt, 0, mJavaImage.getWidth(), 0, 0, mJavaImage.getWidth(), mJavaImage.getHeight());

        rawInt2 = new int[mPresentsImage.getWidth() * mPresentsImage.getHeight()];

        mPresentsImage.getRGB(rawInt2, 0, mPresentsImage.getWidth(), 0, 0, mPresentsImage.getWidth(), mPresentsImage.getHeight());

    }
    // Image tmp2 = Image.createImage(176, 208);
    // Graphics g = tmp2.getGraphics();
    Image logo;
    Image logo2;
    boolean running;
    private int width = getWidth(); // get actual screen width
    private int height = getHeight(); // get actual screen height

    public void startDemo() {
        run();
        //  Thread t = new Thread(this);
        //   t.start();


    }
    private Image mJavaImage;
    private Image mPresentsImage;
    //   private Image mBgImage;
    private Image mBufferImage;
    private Graphics mGraphics;
    private boolean mTrucking = true;
    private boolean mUpdate = true;
    private int mAlpha = 0;
    private int mValue = 5;
    private int mShow = 0;
    private int state = 0;
    private int[] rawInt; // this is the array who will hold the image ARGB values.
    // to do the blending we will change the Alpha value.
    private int[] rawInt2; // this is the array who will hold the image ARGB values for image 2.

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


        while (mTrucking) {

            // check if keys have been pressed - exit if anykey has been pressed
            // get regular key states (excluding softkeys, which are handled in method keyPressed() )
            int keyState = getKeyStates();
            // check both regular keys and softekeys
            if (keyState != 0 || softKeyPressed != 0) {
                break;
            }


            if (mUpdate) {
                // image fading into view
                if (state == 0 || state == 3) {
                    // change the alpha value each loop. 255=opaque, 0=transparent.
                    mAlpha += mValue;
                    if (mAlpha >= 255) {
                        state += 1;
                    }
                }

                // just waiting (showing image)
                if (state == 1 || state == 4) {
                    // we are now opaque, change phase to just showing
                    mShow += 1;
                    if (mShow >= 75) { // 20ms * 100  = 1 secs
                        //NOTE: have to take into account that phone takes time to render.. so in phone it was showing like 3x the time as on pc

                        state += 1;
                        mShow = 0;
                    }
                    //                    mValue = mValue * -1;
                }

                // image fading out
                if (state == 2 || state == 5) {
                    mAlpha -= mValue;
                    if (mAlpha <= 0) {
                        state += 1;
                    }
                }

                if (state == 6) {

                    try {
                        // show black screen for for 1000 ms before proceeding
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // nothing
                    }
                    break;
                }
                //else if (mAlpha <= 0) {
                //  mValue = mValue * -1;
                // }

                // Use the blend method in ImageEffect the change the Alpha value.
                // create a new Image from the new rawInt array.

                Image fadingImage = null;
                if (state == 0 || state == 1 || state == 2) {
                    ImageEffect.blend(rawInt, mAlpha);
                    fadingImage = Image.createRGBImage(rawInt, mJavaImage.getWidth(), mJavaImage.getHeight(), true);
                }

                // handle the "presents" text
                if (state == 3 || state == 4 || state == 5) {
                    ImageEffect.blend(rawInt2, mAlpha);
                    fadingImage = Image.createRGBImage(rawInt2, mPresentsImage.getWidth(), mPresentsImage.getHeight(), true);
                }

                mGraphics.setColor(0x000000);
                mGraphics.fillRect(0, 0, width, height);
                // mGraphics.drawImage(mBgImage, 0, 0, 0);
                //mGraphics.drawImage(fadingImage, 0, 10, 0);





                mGraphics.drawImage(fadingImage, width / 2 - fadingImage.getWidth() / 2, height / 2 - fadingImage.getHeight() / 2, 0);
                System.gc();

                mUpdate = false;
            }
            repaint();

            try {
                Thread.sleep(20);
            } catch (Exception e) {
            }
        }
        // System.out.println("Demoloop end");

    }

    // draw the image to the canvas.
    public void paint(Graphics g) {


        g.drawImage(mBufferImage, 0, 0, 0);
        mUpdate = true;
    }
    private int softKeyPressed = 0;

    /* Catch softkey presses */
    public void keyPressed(int keyCode) {

        softKeyPressed = keyCode;
    }

    // exit if user touches screen
    public void pointerReleased(int x, int y) {
        softKeyPressed = 1;
    }

    protected void sizeChanged(int w, int h) {


        // dont use sizeChanged function.. some phones have w > h on default..
        // update: 23.1.2011 handle landscape mode afterall
        if (w > h) {

            if (landscapeMode == false) {
                try {
                    mBufferImage = Utils.rotateImage(mBufferImage, 270);
                } catch (Exception e) {
                    System.out.println("Error rotating in mirakedemocanvas");
                    e.printStackTrace();
                }
            }
            landscapeMode = true;
        } else {

            if (landscapeMode == true) {
                try {
                    mBufferImage = Utils.rotateImage(mBufferImage, 90);
                } catch (Exception e) {
                    System.out.println("Error rotating in mirakedemocanvas");
                    e.printStackTrace();
                }
            }
            landscapeMode = false;


        }
        width = w;
        height = h;
        //super.sizeChanged(w, h);
    }
    private boolean landscapeMode = false;
}
