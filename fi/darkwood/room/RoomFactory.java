/*
 * RoomFactory.java
 *
 * Created on May 17, 2007, 9:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.room;

import fi.darkwood.*;

/**
 *
 * @author Tommi Laukkanen
 */
public class RoomFactory {

    private int roomCount;

    public Room constructEntrance(Zone zone, String name, String type) {
        roomCount = 0;
        zone.entrance = new Room(zone, name, "/images/" + type + ".png", roomCount);
        zone.rooms.insertElementAt(zone.entrance, roomCount);
        roomCount += 1;

        return zone.entrance;

    }

    /**
     * Generates a new maproom between adjacent xml zones.
     * @param zone
     * @param name
     * @param img
     * @return
     */
    public Room constructMapRoomEntrance(Zone zone, String name) {
        roomCount = 0;
        zone.entrance = new MapRoom(zone, name, roomCount);
        zone.rooms.insertElementAt(zone.entrance, roomCount);
        roomCount += 1;

        return zone.entrance;

    }

    public Room constructCityEntrance(Zone zone, String name, String img) {
        roomCount = 0;
        zone.entrance = new CityRoom(zone, name, img, roomCount);
        zone.rooms.insertElementAt(zone.entrance, roomCount);
        roomCount += 1;

        return zone.entrance;

    }

    public Room constructRoomWest(Zone zone, Room parent, String name, String type) {
        Room room;
        if (parent instanceof CityRoom) {
            room = new CityRoom(zone, name, "/images/" + type + ".png", roomCount);
        } else {
            room = new Room(zone, name, "/images/" + type + ".png", roomCount);
        }
        parent.attachWest(room);
        zone.rooms.insertElementAt(room, roomCount);
        roomCount += 1;
        return room;
    }

    public Room constructRoomEast(Zone zone, Room parent, String name, String type) {
        Room room;
        if (parent instanceof CityRoom) {
            room = new CityRoom(zone, name, "/images/" + type + ".png", roomCount);
        } else {
            room = new Room(zone, name, "/images/" + type + ".png", roomCount);
        }
        parent.attachEast(room);
        zone.rooms.insertElementAt(room, roomCount);
        roomCount += 1;
        return room;
    }

    public Room constructRoomNorth(Zone zone, Room parent, String name, String type) {
        Room room;
        if (parent instanceof CityRoom) {
            room = new CityRoom(zone, name, "/images/" + type + ".png", roomCount);
        } else {
            room = new Room(zone, name, "/images/" + type + ".png", roomCount);
        }
        parent.attachNorth(room);
        zone.rooms.insertElementAt(room, roomCount);
        roomCount += 1;
        return room;
    }
/*
 * South used only for maprooms ++Ville
 * 
    public Room constructRoomSouth(Zone zone, Room parent, String name, String type) {
        Room room;
        if (parent instanceof CityRoom) {
            room = new CityRoom(zone, name, "/images/" + type + ".png", roomCount);
        } else {
            room = new Room(zone, name, "/images/" + type + ".png", roomCount);
        }
        parent.attachSouth(room);
        zone.rooms.insertElementAt(room, roomCount);
        roomCount += 1;
        return room;
    }
*/
    public Room constructExitRoomEast(Zone zone, Room parent, String name, String type, String exitZone) {
        Room room = new ExitRoom(zone, name, "/images/" + type + ".png", exitZone, roomCount);
        parent.attachEast(room);
        zone.rooms.insertElementAt(room, roomCount);
        roomCount += 1;
        return room;
    }

    public Room constructMapRoomSouth(Zone zone, Room parent, String name) {
        MapRoom room = new MapRoom(zone, name, roomCount);
        parent.attachSouth(room);
        zone.rooms.insertElementAt(room, roomCount);
        roomCount += 1;

        room.exit = parent;
        return room;
    }

    public Room constructStatRoomNorth(Zone zone, Room parent, String name, String type) {
        StatRoom room = new StatRoom(zone, name, "/images/background/" + type + ".png", roomCount);
        parent.attachNorth(room);
        zone.rooms.insertElementAt(room, roomCount);
        roomCount += 1;

        room.exit = parent;
        return room;
    }

    public ShopRoom constructShopRoomWest(Zone zone, Room parent, String name, String type) {
        ShopRoom room;
        room = new ShopRoom(zone, name, "/images/" + type + ".png", roomCount);
        parent.attachWest(room);
        zone.rooms.insertElementAt(room, roomCount);
        roomCount += 1;
        return room;
    }

    public void constructTavernRoomEast(Zone zone, Room parent, String name, String type) {
//#if JSR82
        TavernRoom room;
        room = new TavernRoom(zone, name, "/images/background/tavern.png", roomCount);
        parent.attachEast(room);
        zone.rooms.insertElementAt(room, roomCount);
        roomCount += 1;

        room.exit = parent;
        return;
//#else
//#         // nothing
//#endif
    }
}
