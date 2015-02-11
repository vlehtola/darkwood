/*
 * Zone.java
 *
 * Created on May 4, 2007, 9:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood;

import fi.darkwood.room.Room;
import java.util.Vector;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Tommi Laukkanen
 * restructured: Ville
 */
public class Zone {
    
    /** Creates a new instance of Zone */
    // Cities
    public Zone(String name, String background) {
        this.name=name;
        this.background = background;
        rooms = new Vector();
    }
    // Areas
    public Zone(String name, String background, String path) {
        this.name=name;
        this.background = background;
        rooms = new Vector();
        this.path = path;
    }

    public String path;
    public String name;
    public String background;
    private String zoneXmlFilename;
    public Room entrance;    
    public int areaLevel;
    private Image icon;
    public boolean partyOnly;
    
    public String allQuestsDoneMessage = "All quests done.";

//    public Room exit;
//    public Room fallbackRoom;
    
    private String mExitZone;
    private String mFallbackZone;
    
    public Quest firstQuest;
    
    private int x, y;
    
    public Vector rooms;
    
    public String getBackground() {
        return background;
    }
    
    public void resetZone(){
        
    }
    public String getDescription (){
        if(partyOnly) {
            return name + " (groups only)";
        }
        return name + " (level "+(areaLevel)+"-"+(areaLevel+2)+")";
    }

    // Y coordinate is not in use    
    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public String getExitZone() {
        return mExitZone;
    }

    public void setExitZone(String exitZone) {
        this.mExitZone = exitZone;
    }

    
    public String getFallbackZone() {
        return mFallbackZone;
    }

    public void setFallbackZone(String fallbackZone) {
        this.mFallbackZone = fallbackZone;
    }

    public String getZoneXmlFilename() {
        return zoneXmlFilename;
    }

    public void setZoneXmlFilename(String zoneXmlFilename) {
        this.zoneXmlFilename = zoneXmlFilename;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }
    
}
