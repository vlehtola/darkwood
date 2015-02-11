/*
 * Armour.java
 *
 * Created on May 5, 2007, 12:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood.equipment;

import fi.darkwood.*;

/**
 *
 * @author Tommi Laukkanen
 */
public class Armour extends Equipment {
    
    /** Creates a new instance of Armour */
    public Armour(String name,String description,String image,int level) {
        super(name,description,image,level);
    }
    /*
    public Armour(String name,String description,String image,int level, int type, int stats[]) {
        super(name,description,image, level, type, 0, stats); // Create a common item
    } */
    public Armour(String name,String description,String image,int level, int type, int category, int stats[]) {
        super(name,description,image, level, type, category, stats);
    }

   
}
