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


/**
 * This room is actually skipped, as players are moved directly to the exitzone
 * @author Teemu Kivim�ki
 */
public class ExitRoom extends Room {
    
    String exitZone;
    
    /** Creates a new instance of ExitRoom */
    // OBSOLETE
    public ExitRoom(Zone zone, String name, String background, String exitZone, int id) {
        super(zone, name, background, id);
        this.exitZone = exitZone;
    }
    
     /** Creates a new instance of ExitRoom */
    public ExitRoom(Zone zone, String name, String exitZone, int id) {
        super(zone, name, id);
        this.exitZone = exitZone;
    }
    /** Overrides room addThing(Thing thing) */
    public void addThing(Thing thing) {
        super.addThing(thing);
        
        if (thing instanceof Player) {
 
            Zone zone = Utils.getZoneForClassName(exitZone);
            if (zone == null) {
                System.out.println("Error! Failed to initilize zone for: " + exitZone);
                // failed at instantiating the new zone
            }
          //  setEast(zone.entrance);
            
            
            // move player directly to next zone
            this.removeThing(thing);
            zone.entrance.addThing(thing);
            Game.party.initCityForClients(exitZone);
        }
    }
}
