/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mirake;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;

/** Background music player
 *
 * @author Teemu
 */
public class SoundPlayer {

    // use full package name to avoid confusion with darkwood Player class
    static javax.microedition.media.Player player;
    static boolean musicEnabled;

    static public void enableMusic(boolean enabled) {
        musicEnabled = enabled;
    }

    static public void playTitleScreenMusic() {
        playMusic("/music/a_warriors_resolve7.mp3");
    }

    static public void playCityMusic() {
        playMusic("/music/feudal.mp3");
    }

    static public void playZoneMusic() {
        playMusic("/music/clash_of_titans.mp3");
    }

    static private void playMusic(String file) {

        // if music is not enabled, just return
        if (musicEnabled == false) return;

        // stop playing current music
        stopMusic();

        // create new player for the new song
        try {

            // open the mp3 file to play
            InputStream is = SoundPlayer.class.getResourceAsStream(file);
            player = Manager.createPlayer(is, "audio/mpeg");

            player.setLoopCount(-1);
            player.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (MediaException me) {
            System.out.println("Could not init sound player for mpeg: " + file);
        }

    }

    static public void resumeMusic() {
        if(player == null)
            return;
        try {
            player.start();
        } catch (MediaException me) {
            me.printStackTrace();
        }

    }

    static public void stopMusic() {
        if(player == null)
            return;
        try {
            player.stop();
        } catch (MediaException me) {
            me.printStackTrace();
        }

    }

}
