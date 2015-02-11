/*
 * UIElement.java
 *
 * Created on 8. hein�kuuta 2007, 23:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood.ui.component;

import fi.darkwood.Creature;
import fi.darkwood.util.Font;

/**
 *
 * @author Teemu Kivim�ki
 */
public class CombatText {
    
    private Creature target;
    private String text;
    private long eventTime;
    private int rgbColor;
    private Font font;
    
    /** Creates a new instance of CombatText | DEPRECATED
     */
    public CombatText(Creature target, String text, long expire, int rgbColor) {
        this.target = target;
        this.text = text;
        this.eventTime = expire;
        this.rgbColor = rgbColor;
    }

    /** Creates a new instance of CombatText for a certain font*/
    public CombatText(Creature target, String text, long expire, Font font) {
        this.target = target;
        this.text = text;
        this.eventTime = expire;
        this.font = font;
    }


    public int getColor() {
        return rgbColor;
    }
    
    public Creature getTarget() {
        return target;
    }

    public String getText() {
        return text;
    }

    public long delayText(long tt) {
        eventTime = tt;
        return tt;
    }
    public long getEventTime() {
        return eventTime;
    }

    public Font getFont() {
        return font;
    }
}
