/*
 * AreaGenerator builds rooms with monsters and chests from .xml formatted area file
 * 
 */
package fi.darkwood;

import fi.darkwood.room.ExitRoom;
import fi.darkwood.room.Room;
import fi.mirake.Local;
import java.io.InputStream;
import java.util.Enumeration;
import nanoxml.kXMLElement;

/**
 *
 * @author Ville Lehtola
 */
public class AreaGenerator {

    private static AreaGenerator instance = null;

    public AreaGenerator() {
    }

    public static AreaGenerator getInstance() {
        if (instance == null) {
            instance = new AreaGenerator();
        }
        return instance;
    }

    // resourceName = fi/darkwood/level/one/DarkForest.xml
    public Zone loadArea(String resourceName) {
        StringBuffer b = new StringBuffer();
        try {
            InputStream is = this.getClass().getResourceAsStream(resourceName);
            int ch;
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            //System.out.print((char) ch);
            }
        } catch (java.io.IOException e) {
            System.out.println("Unable to load area " + resourceName);
            return null;
        }
        kXMLElement exml = new kXMLElement();
        String str = b.toString();

        exml.parseString(str);

        // Only one area per file
        String areaName = exml.getProperty("name");
        String background = exml.getProperty("background");
        String path = exml.getProperty("path");
        areaName = Local.get(areaName);
        path = Local.get(path);
        String partyArea = exml.getProperty("party");
        int baseLevel = exml.getProperty("level", 0); // monster base level
        System.out.println("ZONE: " + areaName + " " + background);
        Zone zone = new Zone(areaName, background); // instantiate the zone
        Enumeration enumeration = exml.getChildren().elements();
        Room roomOb;

        if (partyArea != null && !Game.party.isActive()) {
            // We have entered a party area. The first room has been loaded without any objects in it.
            // If not in a party, we stop loading and send a message.
            roomOb = new Room(zone, areaName, 0, path);
            zone.areaLevel = 0;
            zone.partyOnly = true;
            zone.rooms.addElement(roomOb);
            zone.entrance = roomOb;
            roomOb.addThing(new SoloStopSign());
            System.out.println(zone.name + " is party only.");
            return zone;
        }

        // Loop over the rooms in the area
        int id = 0;
        int monsterLevelSum = 0, monsterAmount = 0, tmp;
        Room oldRoom = null;
        roomOb = null;
        boolean possibleStatueRoom;
        int roomAmount = exml.countChildren();
        int placedStatues = 0;
        while (enumeration.hasMoreElements()) {
            possibleStatueRoom = true;
            kXMLElement room = (kXMLElement) enumeration.nextElement();
            // See if the room has an exit to the following zone
            String exit = room.getProperty("exit");

            if (exit != null) {
                roomOb = (Room) new ExitRoom(zone, areaName, exit, id);
                possibleStatueRoom = false;
            } else {
                roomOb = new Room(zone, areaName, id, path);
            }
            zone.rooms.addElement(roomOb);
            if (id == 0) {
                zone.entrance = roomOb;
                possibleStatueRoom = false;
            } else if (id >= placedStatues+roomAmount -2) {
                // No healing statues after the second last room
                possibleStatueRoom = false;
            }
            id += 1;
            // Link the rooms to each other. Areas are straight paths towards east.
            if (oldRoom != null) {
                oldRoom.setEast(roomOb);
            }
            oldRoom = roomOb;


            // Loop over the monsters and chests etc inside the room
            Enumeration e2 = room.getChildren().elements();
            int monsterId = 0;
            while (e2.hasMoreElements()) {
                kXMLElement ob = (kXMLElement) e2.nextElement();

                // chest level formula: total_monster_levels_so_far / (room_count*2)
                tmp = loadObject(ob, roomOb, monsterId, monsterLevelSum, baseLevel, id + 1);
                if (tmp > 0) {
                    monsterId += 1;
                    monsterLevelSum += tmp;
                    monsterAmount += 1;
                }

            }
            if ((id % 10 == 0) && possibleStatueRoom) {
                // every 10th room comes an extra room containing a healing statue. ++Ville

                HealingStatue statu = new HealingStatue();
                statu.setLevel(baseLevel);

                roomOb = new Room(zone, areaName, id, path);
                roomOb.addThing(statu);
                zone.rooms.addElement(roomOb);
                oldRoom.setEast(roomOb);
                oldRoom = roomOb;
                id++;
                placedStatues++;

            }

        }
        zone.areaLevel = baseLevel; //GameConstants.calculateAreaLevel(id + 1, monsterLevelSum, monsterAmount);
        System.out.println(zone.name + " areaLevel: " + zone.areaLevel + ", " + id + " : " + monsterLevelSum + " : " + monsterAmount);
        return zone;
    }

    // Dynamically load a monster or chest and put it into the room
    // Return monster level
    private int loadObject(kXMLElement element, Room room, int monsterId, int monsterLevelSum, int baseLevel, int numberOfRooms) {
        String className = element.getProperty("class");

        // get item quality: -1=no item, 0=common, 1=uncommon, 2=rare
        int itemQuality = element.getProperty("quality", -1);

        int level = baseLevel + element.getProperty("level", 0);
        int mobLevel = 0;

        Class luokka = null;
        try {
            luokka = Class.forName(className);
            Thing ob = null;
            ob = (Thing) luokka.newInstance();
            if (ob instanceof Monster) {
                Monster mob = (Monster) ob;
                mob.setLevel(level);
                mobLevel = level;
                mob.setId(monsterId);
                if ("true".equals(element.getProperty("elite"))) {
                    mob.setElite(true);
                    System.out.println("Elitemob: " + mob.name + " level " + mob.level);
                }

            }
            if (ob instanceof Chest) {
                Chest ch = (Chest) ob;
                ch.generateContent(monsterLevelSum, numberOfRooms, element.getProperty("level", 0), itemQuality);
            }
            if (ob instanceof UsableThing) {
                UsableThing hs = (UsableThing) ob;
                hs.setLevel(level);
            }
            room.addThing(ob);
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found loading zone: " + className);
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            System.out.println("Illegal access loading zone: " + className);
            ex.printStackTrace();
        }
        return mobLevel;
    }
}

