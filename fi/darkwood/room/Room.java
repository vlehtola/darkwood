/*
 * Room.java
 *
 * Created on May 4, 2007, 8:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.room;

import fi.darkwood.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Basic room.
 * Special room types (MapRoom, CityRoom, ExitRoom) extend this class.
 * 
 * 
 * @author Tommi Laukkanen, Teemu
 */
public class Room {

    //Creates a new instance of Room (for maproom)
    // used by cities?
    public Room(Zone zone, String name, String background, int id) {
        this.name = name;
        this.background = background;
        this.zone = zone;
        this.setId(id);
    }

    // Used by AreaGenerator
    public Room(Zone zone, String name, int id, String path) {
        this.setName(name);
        this.setZone(zone);
        this.setId(id);
        this.path = path;
    }
    public Room(Zone zone, String name, int id) {
        this.setName(name);
        this.setZone(zone);
        this.setId(id);
    }
    public Zone zone;
    private int id;
    private String path;
    private String name;
    private String background;
    private Room north;
    private Room south;
    private Room east;
    private Room west;
    private Vector things = new Vector();
    private Vector players = new Vector();
    private Vector creatures = new Vector();
    private int roomX,  roomY;
    private int monsterId = 0;

    public int getNextMonsterId() {
        monsterId++;
        return monsterId;
    }
    public String getPath() {
        return path;
    }

    public int getRoomX() {
        return roomX;
    }

    public void setRoomX(int roomX) {
        this.roomX = roomX;
    }

    public int getRoomY() {
        return roomY;
    }

    public void setRoomY(int roomY) {
        this.roomY = roomY;
    }

    public void attachNorth(Room room) {
        this.setNorth(room);
        room.setSouth(this);
    }

    public void attachEast(Room room) {
        this.setEast(room);
        room.setWest(this);
    }

    public void attachSouth(Room room) {
        this.setSouth(room);
        room.setNorth(this);
    }

    public void attachWest(Room room) {
        this.setWest(room);
        room.setEast(this);
    }

    public void addThing(Thing thing) {
        thing.room = this;
        getThings().addElement(thing);

        if (Creature.class.isInstance(thing)) {
            getCreatures().addElement(thing);
        }

        if (Player.class.isInstance(thing)) {
            getPlayers().addElement(thing);
        }
    }

    public void removeThing(Thing thing) {
        getThings().removeElement(thing);

        if (Creature.class.isInstance(thing)) {
            getCreatures().removeElement(thing);
        }

        if (Player.class.isInstance(thing)) {
            getPlayers().removeElement(thing);
        }

        thing.room = null;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Creature getCreatureById(int id) {
        Enumeration e = getCreatures().elements();
        while (e.hasMoreElements()) {
            Creature creature = (Creature) e.nextElement();
            if (creature.getId() == id) {
                return creature;
            }
        }
        Enumeration e2 = getPlayers().elements();
        System.out.println("Player in room: " + getPlayers().size());
        while (e2.hasMoreElements()) {

            Creature creature = (Creature) e2.nextElement();
            System.out.println("Player id: " + creature.getId() + " looking for: " + id);
            if (creature.getId() == id) {
                return creature;
            }
        }
        return null;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Room getNorth() {
        return north;
    }

    public void setNorth(Room north) {
        this.north = north;
    }

    public Room getSouth() {
        return south;
    }

    public void setSouth(Room south) {
        this.south = south;
    }

    public Room getEast() {
        return east;
    }

    public void setEast(Room east) {
        this.east = east;
    }

    public Room getWest() {
        return west;
    }

    public void setWest(Room west) {
        this.west = west;
    }

    public Vector getThings() {
        return things;
    }

    public void setThings(Vector things) {
        this.things = things;
    }

    public Vector getPlayers() {
        return players;
    }

    public void setPlayers(Vector players) {
        this.players = players;
    }

    public Vector getCreatures() {
        return creatures;
    }

    public void setCreatures(Vector creatures) {
        this.creatures = creatures;
    }
}
