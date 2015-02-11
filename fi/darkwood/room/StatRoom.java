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

/**
 * When is this type of room, StatRoomUI is displayed
 * Shows players stats; can raise them if have stat points available
 *
 *
 * @author Teemu Kivimäki
 */
public class StatRoom extends Room {

    public Room exit;

    /** Creates a new instance of StatRoom */
    public StatRoom(Zone zone,String name, String background, int id) {
        super(zone, name, background, id);
    }
   
}
