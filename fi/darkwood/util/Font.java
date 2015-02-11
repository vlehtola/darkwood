package fi.darkwood.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.lcdui.Image;

public final class Font {

    private String fontUrl;
    private Image fontImage;
    private boolean hasMixedCase;
    private byte[] characterWidths;
    private short[] xPositions;
    private String characterMap;
    private int fontHeight;
    private int spaceIndex;
    private static Hashtable fontsUrls = new Hashtable();

    /**
     * Creates a new bitmap font.
     *
     * @param fontUrl the url of the *.bmf file containing the font-specification.
     */
    private Font(String fontUrl) {
        super();
        this.fontUrl = fontUrl;

        System.out.println("Creating bitmap font " + fontUrl);
    }

    /**
     * Creates a viewer object for the given string.
     *
     * @param input the input which should be shown.
     * @return a viewer object which shows the font in a performant manner
     */
    public FontCanvas getViewer(String input) {
        if (this.fontImage == null) {
            // try to load the *.bmf file:
            InputStream in = null;
            try {
                in = getClass().getResourceAsStream(this.fontUrl);
                if (in == null) {
                    return null;
                }
                DataInputStream dataIn = new DataInputStream(in);
                this.hasMixedCase = dataIn.readBoolean();
                String map = dataIn.readUTF();
                this.characterMap = map;
                this.spaceIndex = map.indexOf(' ');
                int length = map.length();
                this.characterWidths = new byte[length];
                this.xPositions = new short[length];
                short xPos = 0;
                for (int i = 0; i < length; i++) {
                    byte width = dataIn.readByte();
                    this.characterWidths[i] = width;
                    this.xPositions[i] = xPos;
                    xPos += width;
                }
                this.fontImage = Image.createImage(in);

                this.fontHeight = this.fontImage.getHeight();
                this.fontUrl = null;
            } catch (IOException e) {

                System.out.println("Unable to load bitmap-font [" + this.fontUrl + "]" + e);
                return null;

            } finally {
                try {
                    in.close();
                } catch (IOException e) {

                    System.out.println("Unable to close bitmap-font stream" + e);
                }

            }
        }
        //int imageWidth = this.fontImage.getWidth();
        // get the x/y-position and width for each character:
        if (!this.hasMixedCase) {
            input = input.toLowerCase();
        }
        int length = input.length();
        //short[] yPositions = new short[ length ];
        int[] indeces = new int[length];
        for (int i = length - 1; i >= 0; i--) {
            char inputCharacter = input.charAt(i);
            if (inputCharacter == '\n') {
                indeces[i] = FontCanvas.ABSOLUTE_LINE_BREAK;
            } else {
                indeces[i] = this.characterMap.indexOf(inputCharacter);
            }
        }
        return new FontCanvas(this.fontImage, indeces, this.xPositions, this.characterWidths, this.fontHeight, this.spaceIndex, 1);
    }

    /**
     * Removes the instance of the specified font from the internal cache.
     *
     * @param url the url of the font
     */
    public static void removeInstance(String url) {
        fontsUrls.remove(url);
    }


    /**
     * Gets the instance of the specified font.
     *
     * @param url the url of the font
     * @return the corresponding bitmap font.
     */
    public static Font getInstance(String url) {
        Font font = (Font) fontsUrls.get(url);
        if (font == null) {
            font = new Font(url);
            fontsUrls.put(url, font);
        }
        return font;
    }

}
