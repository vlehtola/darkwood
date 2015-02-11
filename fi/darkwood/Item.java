/*
 * Item.java
 *
 * Created on May 5, 2007, 12:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood;

/**
 *
 * @author Tommi Laukkanen, Teemu Kivim�ki
 */
public abstract class Item extends Thing {
    
    public Item(String name, String description, String image, int value, int level) {
        super(name,image);
        this.value=value;          
        this.description = description;
        this.level = level;
    }

    
    /** Creates a new instance of Equipment */
    public Item(String name, String description, String image, int value, int level, int quality) {
        super(name,image);
        this.value=value;          
        this.description = description;
        this.level = level;
        this.quality = quality;
    }

    private int level;
    public int value;
    public String description;

    public int quality = GameConstants.QUALITY_COMMON;
            
    public void tick()
    {
    
    }

    public int getLevel() {
        return level;
    }
    
    public abstract String getItemInfo();

    /**
     * Get the sell value of this item
     * @return sell value
     */
    public int getSellValue() {
        return value / 3;
    }
    
}
