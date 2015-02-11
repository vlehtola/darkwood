/*
 * RoomDaemon.java
 *
 * Creates Room objects and adds exits between them from an ASCII format area map.
 * Vertical exits can be inserted using capital letters.
 * Things (mobs etc.) are added after the room cloning by coordinate basis of the area map.
 *
 * Created on December 28, 2007, 12:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood.room;

import fi.darkwood.Monster;
import fi.darkwood.room.Room;
import fi.darkwood.Thing;
import fi.darkwood.Zone;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author Ville Lehtola
 */
public class RoomDaemon {
    private Zone zone;
    private Vector map;
    private int roomCount = 0;
    
    
    /** Creates a new instance of RoomDaemon */
    public RoomDaemon(Zone zone, Vector map) {
        this.zone = zone;
        this.map = map;
    }
    // Clone the rooms first, then add the exits
    public void constructRooms() {
        Room room, preRoom = null, tmpRoom;
        String name;
        String type = "forest";
        int i, roomX=0, roomY=0, yCheck=0;
        Enumeration e = map.elements();
        // Loop through the lines (defined as the y-direction)
        while (e.hasMoreElements()) {
            roomY++;
            roomX = 0;
            yCheck = 0;
            String sh = (String)e.nextElement();
            // Loop through one line (defined as the x-direction)
            for(i=0;i<sh.length();i++) {
                roomX++;
                if(sh.charAt(i) == type.charAt(0) || sh.charAt(i) == Character.toUpperCase(type.charAt(0))) {
                    name = "forest";
                    room = new Room(zone,name,"/images/background/"+type+".png", roomCount);
                    room.setRoomX(roomX);
                    room.setRoomY(roomY);
                    zone.rooms.insertElementAt(room, roomCount); // room created
                    if( (preRoom != null) && yCheck == 1 ) { // yCheck is for not to connect the last room of line j with the first room of line j+1
                        preRoom.attachEast(room);
                    }
                    preRoom = room;
                    roomCount++;
                    // Here we claim that the westmost room is the entrance
                    if(roomX == 1) {
                        System.out.println("Found entrance.");
                        zone.entrance = room;
                    }
                    // Add the y-directional exits
                    if(sh.charAt(i) == Character.toUpperCase(type.charAt(0)) && roomY > 1) {
                        Enumeration en = zone.rooms.elements();
                        while(en.hasMoreElements()) {
                            tmpRoom = (Room) en.nextElement();
                            if( (tmpRoom.getRoomX() == roomX) && (tmpRoom.getRoomY() == roomY -1) ) {
                                room.attachNorth(tmpRoom);
                                System.out.println("Adding Y- exit:"+sh.charAt(i)+" "+Character.toUpperCase(type.charAt(0))+" RoomX: "+roomX+" "+roomY+" "+tmpRoom.getRoomX()+ " "+
                                        tmpRoom.getRoomX());
                            }
                        }
                    }
                    yCheck = 1;
                }
            }
        }
        
        if(zone.entrance == null) {
            System.out.println("NO ENTRANCE!");
        }
    }
    public Room addThing(Thing thing, int roomX, int roomY) {
        Enumeration e = zone.rooms.elements();
        while(e.hasMoreElements()) {
            Room room = (Room) e.nextElement();
            if(room.getRoomX() == roomX && room.getRoomY() == roomY) {
                //System.out.println("Adding monster to RoomX: "+roomX+" "+roomY+" "+room.getRoomX()+ " "+room.getRoomX());
                room.addThing(thing);
                return room;
            }
        }
        System.out.println("ERROR! addThing() Room not found. xy: "+roomX+" "+roomY);
        return null;
    }
    public void addMonster(Monster monster, int roomX, int roomY) {
        Room room = addThing((Thing) monster, roomX, roomY);
        monster.setId(room.getNextMonsterId());
        
    }
}
