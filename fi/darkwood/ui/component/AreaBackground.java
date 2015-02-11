/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.ui.component;

import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.Player;
import fi.darkwood.room.ExitRoom;
import fi.darkwood.util.Utils;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Ville
 */
public class AreaBackground {
    Image img;
    private final static AreaBackground INSTANCE = new AreaBackground();
    public static AreaBackground getInstance() {
        return INSTANCE;
    }

    private AreaBackground() {

    }

    // determine what portion of the background image should we draw
    // for each room, advance the background image for a number of pixels
    public void drawBackground(DarkwoodGraphics g, Player player) {
        int background_x, width;
        int screenWidth = 176;
        
        Utils utils = Utils.getInstance();
        img = utils.getImage(player.room.zone.getBackground());
        width = img.getWidth();
        int nrooms = player.room.zone.rooms.size();
        // Show the left side of the picture in the first room, and the right side in the last
        // 'rooms' contains an extra room: the exitroom, if the area leads to a city
        if(player.room.zone.rooms.elementAt(nrooms-1) instanceof ExitRoom) {
            nrooms -= 2;
        } else {
            nrooms -= 1;
        }
        if(nrooms == 0) nrooms = 1; // ++Ville 190909
        background_x = (width - screenWidth) * player.room.getId() / nrooms;
        //System.out.println("ROOMS: "+player.room.zone.rooms.size()+" "+player.room.zone.rooms);

        g.drawRegion(img, background_x, 0, screenWidth, img.getHeight(), 0, 0, 29, 0);

    }
}
