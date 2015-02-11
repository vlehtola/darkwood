package fi.darkwood.util;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class FontCanvas {

    public final static byte ABSOLUTE_LINE_BREAK = -2;
    public final static byte ARTIFICAL_LINE_BREAK = -3;
    private final Image image;
    private final short[] xPositions;
    private final byte[] usedCharactersWidths;
    private final int fontHeight;
    private int verticalPadding;
    private final int spaceIndex;
    private int height;
    private int width;
    private int numberOfLines;
    private short[] lineWidths;
    private final int[] indeces;
    private final byte[] actualCharacterWidths;
    private int orientation;

    /**
     * Views a specific input string with a specific bitmap font.
     *
     * @param image the basic font-image
     * @param indeces array of the x-positions of the to-be-displayed characters
     * @param xPositions array of the x-positions of the to-be-displayed characters
     * @param characterWidths array of the widths of the to-be-displayed characters
     * @param fontHeight the height of the font
     * @param spaceIndex the index of the space character
     * @param verticalPadding the padding between two lines
     *
     */
    public FontCanvas(Image image, int[] indeces, short[] xPositions, byte[] characterWidths, int fontHeight, int spaceIndex, int verticalPadding) {
        this.image = image;
        this.indeces = indeces;
        this.actualCharacterWidths = characterWidths;
        this.spaceIndex = spaceIndex;
        this.lineWidths = new short[20];
        this.verticalPadding = verticalPadding;
        this.xPositions = new short[indeces.length];
        this.usedCharactersWidths = new byte[indeces.length];
        short currentLineWidth = 0;
        short maxLineWidth = 0;
        short linesIndex = 0;
        for (int i = 0; i < indeces.length; i++) {
            int index = indeces[i];
            //System.out.println("character [" + i + "] is index " + index );
            if (index == -1) {
                // ignore, this is a character which is not available in the character-map
            } else if (index == ABSOLUTE_LINE_BREAK) {
                // this is a line-break
                if (currentLineWidth > maxLineWidth) {
                    maxLineWidth = currentLineWidth;
                }
                this.lineWidths[linesIndex] = currentLineWidth;
                currentLineWidth = 0;
                // mark the character as absolute line-break:
                this.usedCharactersWidths[i] = ABSOLUTE_LINE_BREAK;
                linesIndex++;
                //now check not to exceed the lineWidths array limit //use a grow factor of 10
                if (linesIndex >= this.lineWidths.length) {
                    this.lineWidths = increaseShortArraySize(this.lineWidths, (byte) 10);
                }
            } else {
                // this is a normal character
                this.xPositions[i] = xPositions[index];
                byte characterWidth = characterWidths[index];
                this.usedCharactersWidths[i] = characterWidth;
                currentLineWidth += characterWidth;
            }
        }
        // store the width of the last-line:
        this.lineWidths[linesIndex] = currentLineWidth;
        if (currentLineWidth > maxLineWidth) {
            maxLineWidth = currentLineWidth;
        }
        this.numberOfLines = linesIndex + 1;
        this.height = this.numberOfLines * (fontHeight + verticalPadding) - verticalPadding;
        this.width = maxLineWidth;
        this.fontHeight = fontHeight;
    }

    /**
     * Paints this viewer on the screen.
     *
     * @param x the x-position for the text.
     *          When the orientation is LEFT, x defines the left-border;
     *          when the orientation is RIGHT, x defines the rigth border;
     *          when the orientation is HCENTER, x defines the middle between left and right border.
     * @param y the top y-position of the first line.
     * @param g the graphics object
     */
    public void paint(int x, int y, Graphics g) {
        int clipX = g.getClipX();
        int clipY = g.getClipY();
        int clipWidth = g.getClipWidth();
        int clipHeight = g.getClipHeight();
        int clipXEnd = clipX + clipWidth;
        int startX = x;
        boolean isLayoutRight = (this.orientation == Graphics.RIGHT);
        boolean isLayoutCenter = (this.orientation == Graphics.HCENTER);
        if (isLayoutCenter) {
            x = startX - (this.lineWidths[ 0] / 2);
        } else if (isLayoutRight) {
            x = startX - this.lineWidths[ 0];
        }
        int lineIndex = 0;
        for (int i = 0; i < this.xPositions.length; i++) {
            byte characterWidth = this.usedCharactersWidths[i];
            //System.out.println("character-width[" + i + "] = " + characterWidth + " xPos=" + this.xPositions[i]);
            if (characterWidth == 0) {
                continue;
            } else if (characterWidth < 0) {
                // this is a line-break:
                lineIndex++;
                if (isLayoutCenter) {
                    x = startX - (this.lineWidths[lineIndex] / 2);
                } else if (isLayoutRight) {
                    x = startX - this.lineWidths[lineIndex];
                } else {
                    x = startX;
                }
                y += this.fontHeight + this.verticalPadding;
                continue;
            } else if (x >= clipXEnd) {
                // this character is outside of the clipping area:
                continue;
            }
            g.clipRect(x, y, characterWidth, this.fontHeight);
            int imageX = x - this.xPositions[i];
            //System.out.println( "clipping " + x + ", " + y + ", " + characterWidth + ", " + this.fontHeight + ")   imageStartX=" +( x - this.xPositions[i]) + " imageX=" + imageX);
            g.drawImage(this.image, imageX, y, Graphics.TOP | Graphics.LEFT);
            x += characterWidth;
            // reset clip:
            g.setClip(clipX, clipY, clipWidth, clipHeight);
        }

    }

    /**
     * Retrieves the width needed for this viewer.
     *
     * @return the width in pixels
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Retrieves the height needed for this viewer.
     *
     * @return the height in pixels
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Layouts this text-viewer.
     *
     * @param firstLineWidth the available width for the first line
     * @param lineWidth the available width for the following lines
     * @param paddingVertical the space between lines
     * @param orientationSetting the orientation of this viewer, either Grapics.LEFT, Graphics.RIGHT or Graphics.HCENTER
     */
    public void layout(int firstLineWidth, int lineWidth, int paddingVertical, int orientationSetting) {
        this.orientation = orientationSetting;
        this.verticalPadding = paddingVertical;
        // remove all existing artificial line-breaks first.
        int lineIndex = 0;
        short currentLineWidth = 0;
        short maxLineWidth = 0;
        int lastSpaceIndex = -1;
        int lastSpaceWidth = 0;
        int lineStartIndex = 0;
        for (int i = 0; i < this.usedCharactersWidths.length; i++) {
            byte characterWidth = this.usedCharactersWidths[i];
            if (characterWidth == ARTIFICAL_LINE_BREAK) {
                // restore original character width:
                characterWidth = this.actualCharacterWidths[this.indeces[i]];
                this.usedCharactersWidths[i] = characterWidth;
            } else if (characterWidth == ABSOLUTE_LINE_BREAK) {
                // this is an absolute linebreak (\n)
                this.lineWidths[lineIndex] = currentLineWidth;
                if (currentLineWidth > maxLineWidth) {
                    maxLineWidth = currentLineWidth;
                }
                lineStartIndex = (i + 1);
                lineIndex++;
                //now check not to exceed the lineWidths array limit //use a grow factor of 10
                if (lineIndex >= this.lineWidths.length) {
                    this.lineWidths = increaseShortArraySize(this.lineWidths, (byte) 10);
                }
                currentLineWidth = 0;
                continue;
            }

            // this is a normal character
            int index = this.indeces[i];
            if (index == this.spaceIndex) {
                lastSpaceIndex = i;
                lastSpaceWidth = currentLineWidth;
            }
            currentLineWidth += characterWidth;
            if (currentLineWidth > firstLineWidth) {
                // we need to include an artificial line-break:
                if (lastSpaceIndex > lineStartIndex) {
                    this.usedCharactersWidths[lastSpaceIndex] = ARTIFICAL_LINE_BREAK;
                    lineStartIndex = lastSpaceIndex + 1;
                    this.lineWidths[lineIndex] = (short) lastSpaceWidth;
                    if (lastSpaceWidth > maxLineWidth) {
                        maxLineWidth = (short) lastSpaceWidth;
                    }
                    currentLineWidth -= (lastSpaceWidth + this.actualCharacterWidths[this.spaceIndex]);
                    lineIndex++;
                    //now check not to exceed the lineWidths array limit //use a grow factor of 10
                    if (lineIndex >= this.lineWidths.length) {
                        this.lineWidths = increaseShortArraySize(this.lineWidths, (byte) 10);
                    }
                    // } else {
                    // System.out.println("Unable to break line without any prior space");
                }
            }
            firstLineWidth = lineWidth;
        }
        // store the width of the last-line:
        this.lineWidths[lineIndex] = currentLineWidth;
        if (currentLineWidth > maxLineWidth) {
            maxLineWidth = currentLineWidth;
        }
        this.numberOfLines = lineIndex + 1;
        this.width = maxLineWidth;
        this.height = this.numberOfLines * (this.fontHeight + paddingVertical) - paddingVertical;
    }

    /**
     * Returns the height of used font.
     *
     * @return the height of the bitmap-font in pixels.
     */
    public int getFontHeight() {
        return this.fontHeight;
    }

    /**
     * Retrieves the number of lines which are used to display the embedded text.
     *
     * @return the number of lines.
     */
    public int getNumberOfLines() {
        return this.numberOfLines;
    }

    /**
     * Increases the size of o short array with a given grow factor. It creates a new array with the size
     * src.length + growFactor and copies the elements of src array into the new array.
     *
     * @param src the array that needs a larger size.
     * @param growFactor the grow factor.
     * @return the increased array.
     */
    private short[] increaseShortArraySize(short src[], byte growFactor) {
        short dest[] = new short[src.length + growFactor];
        System.arraycopy(src, 0, dest, 0, src.length);
        return dest;
    }

    /**
     *	Returns the used character widths. This is usefull to have information about the actual
     *	position of each character. You can modify the array because only a copy is returned.
     *
     * @return a copy of the internal byte array representing the widths of the included characters
     */
    public byte[] getUsedCharactersWidths() {
        byte ret[] = new byte[this.usedCharactersWidths.length];
        System.arraycopy(this.usedCharactersWidths, 0, ret, 0, ret.length);
        return ret;
    }
}
