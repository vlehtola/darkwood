/*
 * Utils.java
 *
 * Created on 17. toukokuuta 2007, 2:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.util;

import fi.darkwood.GameConstants;
import fi.darkwood.Zone;
import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;


import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;

/**
 * Utils singleton
 *
 * @author Teemu Kivim�ki
 */
public class Utils {

    private static Utils instance = null;
    Hashtable images = new Hashtable();
    Hashtable sprites = new Hashtable();

    protected Utils() {
        // Exists only to defeat instantiation.
    }

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public Image getImage(String imageName) {
        if (images.containsKey(imageName)) {
            return (Image) images.get(imageName);
        } else {
            try {
                Image image = Image.createImage(imageName);

                // dont keep large images in buffer to save memory (backgrounds)
                // Update: large images need to be buffered also, otherwise there will be problems..
//                if (image.getWidth() < 200) {
                images.put(imageName, image);
//                }

                return image;
            } catch (Exception e) {
                try {
                    Image image = Image.createImage("/images/questionmark.png");
                    images.put(imageName, image);
                    return image;
                } catch (Exception ex) {
                    return null;
                }
            }
        }

    }

    public Sprite getSprite(String imageName, int width) {
        if (sprites.containsKey(imageName)) {
            return (Sprite) sprites.get(imageName);
        } else {
            Image img = getImage(imageName);
            if (width == 0) {
                width = img.getWidth();
            }
            Sprite sprite = new Sprite(img, width, img.getHeight());
            sprites.put(imageName, sprite);
            return sprite;
        }

    }
    // RESIZING FUNCTIONALITY
    int srcWidth = 176;//tmp2.getWidth();
    int srcHeight = 208; //tmp2.getHeight();
    //Image tmp = null;
    Graphics go = null;
    int ratio = (srcWidth << 16) / 176;
    int pos = ratio / 2;
    //Image resizedImage = null;

    // iWidth and iHeight refer to the size of src, heigh and width are the properties of the wanted resized image.
    public Image resizeImage(Image src, int width, int height, int iWidth, int iHeight) {
        Image tmp = null;
        Image resizedImage = null;
        if (iWidth == 0) {
            iWidth = this.srcWidth;
        }
        if (iHeight == 0) {
            iHeight = srcHeight;
        }

        if (GameConstants.SCALE_GRAPHICS_TO_FULLSCREEN == false) {
            return src;
        }

        if (tmp == null) {
            tmp = Image.createImage(width, iHeight);
        }

        go = tmp.getGraphics();
        go.setColor(0x123456);
        go.fillRect(0, 0, width, iHeight);

        ratio = (iWidth << 16) / width;
        pos = ratio / 2;

        //Horizontal Resize

        for (int x = 0; x < width; x++) {
            go.setClip(x, 0, 1, iHeight);
            go.drawImage(src, x - (pos >> 16), 0, Graphics.LEFT | Graphics.TOP);
            pos += ratio;
        }

        if (resizedImage == null) {
            resizedImage = Image.createImage(width, height);
        }
        go = resizedImage.getGraphics();
        go.setColor(0x123456);
        go.fillRect(0, 0, width, height);

        ratio = (iHeight << 16) / height;
        pos = ratio / 2;

        //Vertical resize

        for (int y = 0; y < height; y++) {
            go.setClip(0, y, width, 1);
            go.drawImage(tmp, 0, y - (pos >> 16), Graphics.LEFT | Graphics.TOP);
            pos += ratio;
        }

        return resizedImage;

    }//resize image

    static public Zone getZoneForClassName(String zoneClassName) {
        Class luokka = null;
        try {
            luokka = Class.forName(zoneClassName);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        Zone zone = null;
        try {
            zone = (Zone) luokka.newInstance();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            return null;
        }
        return zone;
    }

    public static Image rotateImage(Image image, int angle) throws Exception {
        if (angle == 0) {
            return image;
        } else if (angle != 180 && angle != 90 && angle != 270) {
            throw new Exception("Invalid angle");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        int[] rowData = new int[width];
        int[] rotatedData = new int[width * height];

        int rotatedIndex = 0;

        for (int i = 0; i < height; i++) {
            image.getRGB(rowData, 0, width, 0, i, width, 1);

            for (int j = 0; j < width; j++) {
                rotatedIndex =
                        angle == 90 ? (height - i - 1) + j * height
                        : (angle == 270 ? i + height * (width - j - 1)
                        : width * height - (i * width + j) - 1);

                rotatedData[rotatedIndex] = rowData[j];
            }
        }

        if (angle == 90 || angle == 270) {
            return Image.createRGBImage(rotatedData, height, width, true);
        } else {
            return Image.createRGBImage(rotatedData, width, height, true);
        }
    }

    /**
     * Capitalize the string given as argument.
     * @param inputWord
     * @return
     */
    static public String capitalize(String inputWord) {

        // if argument lenght is only 1, return it as capitalized
        if (inputWord.length() < 2) {
            return inputWord.toUpperCase();
        }

        String firstLetter = inputWord.substring(0, 1);  // Get first letter
        String remainder = inputWord.substring(1);    // Get remainder of word.

        return firstLetter.toUpperCase() + remainder.toLowerCase();

    }
}

