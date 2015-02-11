/*
 * ShopRoom.java
 *
 * Created on 23. joulukuuta 2007, 21:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.room;

import fi.darkwood.*;
import java.util.Vector;

/**
 *
 * @author Exko
 */
public class ShopRoom extends Room {
    // text displayed when player enters the room.
    String welcomeText = "";

    /** Creates a new instance of ShopRoom */
    public ShopRoom(Zone zone, String name, String background, int id) {
        super(zone, name, background, id);
    }
    public Vector inventory = new Vector();

    public void addItem(Item item) {
        inventory.addElement(item);
    }

    public void removeItem(Item item) {
        inventory.removeElement(item);
    }

    public Vector getInventory() {
        return inventory;
    }

    public String getWelcomeText() {
        return welcomeText;
    }
    public void setWelcomeText(String welcomeText) {
        this.welcomeText = welcomeText;
    }

}
