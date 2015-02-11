
/*
 * This class changes the alpha value of an image array.
 * All the pixels in the image contains Alpha, Red, Green, Blue (ARGB) colors
 * where each of the color is a value from 0 to 255.
 * If Alpha is 255 the pixel will be opaque, if its 0 the pixel is transparent.
 * 0xFFFF0000 = 11111111 11111111 00000000 00000000 - this is a red opaque pixel.
 *
 * To get the RGB values from the array we can use the AND '&' operator.
 * (11111101 & 01111110) = 01111100, only the 1:s where & = 1 will get through.
 *
 * to change 11111111 to 11111111 00000000 00000000 00000000 we can use the
 * shift left operator '<<', (00000001 << 7) = 10000000 in dec (1<<7) = 128
 *
 * To change the alpha value we loop through all the pixels in the image.
 *
 * With the blend method its also possible to mask and dontmask specific colors.
*/

package fi.mirake;

/**
 *
 * @author Teemu
 */
public class ImageEffect {
// raw is the image array.
    public static void blend(int[] raw, int alphaValue, int maskColor, int dontmaskColor){
        int len = raw.length;

        // Start loop through all the pixels in the image.
        for(int i=0; i<len; i++){
            int a = 0;
            int color = (raw[i] & 0x00FFFFFF); // get the color of the pixel.
            if(maskColor==color){
                a = 0;
            }else if(dontmaskColor==color){
                a = 255;
            }else if(alphaValue>0){
                a = alphaValue;     // set the alpha value we vant to use 0-255.
            }

            a = (a<<24);    // left shift the alpha value 24 bits.
            // if color = 00000000 11111111 11111111 00000000
            // and alpha= 01111111 00000000 00000000 00000000
            // then c+a = 01111111 11111111 11111111 00000000
            // and the pixel will be blended.
            color += a;
            raw[i] = color;
        }
    }
    public static void blend(int[] raw, int alphaValue){
        blend(raw, alphaValue, 0xFFFFFFFF, 0xFFFFFFFF);
    }
}
