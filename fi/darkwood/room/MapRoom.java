/*
 * MapRoom.java
 *
 * Created on 20. toukokuuta 2007, 15:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.room;

import fi.darkwood.*;
import fi.darkwood.util.Utils;
import java.util.Vector;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Teemu Kivim�ki
 *  refactored to use XML format by Ville
 */
public class MapRoom extends Room {

    public Vector zones = new Vector();
    public int coordX = 0;
    public int coordY = 0;
    public Room exit;
    public String mapRoomIcon;

    /** Creates a new instance of MapRoom */
    public MapRoom(Zone zone, String name, int id) {
        super(zone, name, id);
        mapRoomIcon = "";
    }

    public void setCoordinates(int x, int y, String iconFileName) {
        coordX = x;
        coordY = y;
        this.mapRoomIcon = iconFileName;
    }
public String getMapRoomIcon()
{
    if(mapRoomIcon.equals("")) {
        mapRoomIcon = "/images/map/icons/darkwood_hut.png";
    }
    return mapRoomIcon;
}
    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    /**
     * Add a newZone related to this maproom
     *
     * @param className Full classname for the area
     * @param x The x coordinate of the area exit on the world map
     * @param y The y coordinate of the area exit on the world map
     * @param fallbackZone The zone where player returns if fleeing or dead
     * @param iconImage Image filename for map icon of this zone
     */    
    public void addZone(String XMLfile, int x, int y, String fallbackZone, String iconImage) {
        System.out.println("Adding zone -- XMLfile:" + XMLfile + ", fallbackZone: " + fallbackZone + ", x: " + x + " y: " + y);

        // Generate the area from XML file
        Zone newZone = AreaGenerator.getInstance().loadArea(XMLfile);
        
        newZone.setX(x);
        newZone.setY(y);
        newZone.setFallbackZone(fallbackZone);
        newZone.setZoneXmlFilename(XMLfile);

        Image icon = Utils.getInstance().getImage(iconImage);
        newZone.setIcon(icon);

        System.out.println("calling resetzone() from maproom addzone.");
        // reset zone to initialize rooms and monsters
        newZone.resetZone();

        zones.addElement(newZone);
    }

    public void addCityZone(String className, int x, int y, String fallbackZone, String exitZone) {
        System.out.println("Adding zone -- className:" + className + ", fallbackZone: " + fallbackZone + ", exitZone: " + exitZone + " x: " + x + " y: " + y);
        
      
        Class luokka = null;
        try {
            luokka = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return;
        }
        Zone zone = null;
        try {
            zone = (Zone) luokka.newInstance();
            zone.setX(x);
            zone.setY(y);

            
            zone.setFallbackZone(fallbackZone);
            
            
            if (exitZone != null) {
              zone.setExitZone(exitZone);
            }
            
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            return;
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            return;
        }

        
System.out.println("calling resetzone() from addcityzone");
        // reset newZone to initialize rooms and monsters
        zone.resetZone();

        zones.addElement(zone);
    }
    
    public Vector getZones() {
        return zones;
    }
}
