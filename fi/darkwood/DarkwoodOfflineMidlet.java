/*
 * DarkWoodMidlet.java
 *
 * Created on 4. toukokuuta 2007, 18:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.mirake.Local;
import fi.mirake.MirakeDemoCanvas;
import fi.mirake.SelectMusicCanvas;
import fi.mirake.SoundPlayer;
import javax.microedition.midlet.*;

/**
 *
 * @author  Teemu Kivimäki
 * @version
 */
import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Display;

// TODO: NOKIA SPECIFIC, MAYBE USE PREPROCESSOR
//import com.nokia.mid.ui.DeviceControl;
/**
 * This is the MIDlet class for darkwood game.
 * First load properties from config files, like server addresses.
 * Then show login form to get account name and password. When login ok,
 * move to darkwood game canvas and startGame game.
 *
 */
public class DarkwoodOfflineMidlet extends MIDlet {

    /** Creates a new instance of DarkWoodMidlet */
    private DarkwoodCanvas dwc;

    public DarkwoodOfflineMidlet() {

        dwc = new DarkwoodCanvas();
        dwc.setFullScreenMode(true);



    }

    protected void startApp() throws MIDletStateChangeException {
        try {

            if (Game.darkwoodCanvas != null) {
                System.out.println("StartApp called after pause");
                return; // game is already running, this call was from resume after pause
            }

            String locale = this.getAppProperty("darkwood_locale");

            if (locale == null || "".equals(locale)) { locale = "en"; }
            Local.initialize(locale);

            Display display = Display.getDisplay(this);

            // ask if player wants sounds or not
            SelectMusicCanvas music = new SelectMusicCanvas();
            display.setCurrent(music);
            music.startCanvas();

            MirakeDemoCanvas demo = new MirakeDemoCanvas();
            display.setCurrent(demo);

            demo.startDemo();


            dwc.startGame(this);

            // set darkwood canvas as the current display
            display.setCurrent(dwc);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void destroyApp(boolean b) throws MIDletStateChangeException {
        System.out.println("destroyApp");
        dwc.quitGame();
    }

    protected void pauseApp() {
        Game.setPaused(true);
    }
}
