/*
 * MessageLog.java
 *
 */

package fi.darkwood.ui.component;

import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.util.Utils;
import javax.microedition.lcdui.Graphics;

/**
 * MessageLog singleton used by combat and movement UI views.
 * Diplays messages to player, such as encounters and ability invokes.
 * 
 * @author Teemu Kivimäki
 */
public class MessageLog {
    
    private final static int LOG_MAX_ROWS = 4;
    
    private final static MessageLog INSTANCE = new MessageLog();
    Utils utils = Utils.getInstance();
    
    private String[] messages = new String[LOG_MAX_ROWS];
    
    /** Creates a new instance of MesageLog */
    private MessageLog() {
    }

    public static MessageLog getInstance() {
        return INSTANCE;
    }
    
    public void addMessage(String str) {
        
        // move all the previous messages down one slot
        for (int i = messages.length - 1; i > 0; i--) {
            messages[i] = messages[i-1];
        }

        messages[0] = str;
    }
    
    private final static int x = 93;
    private final static int y = 30;
    
    public void drawMessageLog(DarkwoodGraphics g) {
        StringBuffer strbuffer = new StringBuffer();
        
        for (int i = messages.length-1; i >= 0; i--) {
            if (messages[i] != null) {
                strbuffer.append(messages[i] + "\n");
            }
        }
        g.drawText(strbuffer.toString(), x, y, 150, 150, 0, Graphics.HCENTER);

    }
    
}
