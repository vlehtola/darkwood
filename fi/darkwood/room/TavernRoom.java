/*
 * CityRoom.java
 *
 * Created on 20. toukokuuta 2007, 15:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood.room;

import fi.darkwood.*;
import java.util.Vector;

/**
 * When is this type of room, StatRoomUI is displayed
 * Shows players stats; can raise them if have stat points available
 *
 *
 * @author Teemu Kivimäki
 */
public class TavernRoom extends Room {

    public Room exit;

    /** Creates a new instance of StatRoom */
    public TavernRoom(Zone zone,String name, String background, int id) {
        super(zone, name, background, id);
    }
   
}
