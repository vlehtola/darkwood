/*
 * Game.java
 *
 * Created on May 4, 2007, 10:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.darkwood.level.five.SnowkeepCityZone;
import fi.darkwood.level.four.MountainPassCityZone;
import fi.darkwood.level.one.DarkwoodCityZone;
import fi.darkwood.level.six.ExpeditionCampCityZone;

import fi.darkwood.level.three.VillageZone;
import fi.darkwood.level.two.BorderKingdomCapital;
import fi.darkwood.party.Party;
import fi.darkwood.rule.MeleeRule;
import fi.darkwood.ui.component.Expbar;
import fi.darkwood.util.Utils;
import fi.mirake.SoundPlayer;
import java.util.Enumeration;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author Teemu Kivimäki
 */
public class Game extends GameConstants {

    public Game(MIDlet midlet, DarkwoodCanvas dwc) {
        gameMidlet = midlet;
        darkwoodCanvas = dwc;

    }
    static public Player player = null;
    static public Party party = new Party();
    static public boolean isGameOver = false;
    static public boolean showConsole = false;
    /**
     * Defines if the account used is registered or not
     */
    static public boolean registered = true;
    static public MIDlet gameMidlet;
    static public DarkwoodCanvas darkwoodCanvas;
    static public int characterSaveSlot = 0; // the local database slot which the character is saved to (0 - 4)
    static private boolean paused = false;
    static public boolean pauseable = false;

    public static void gameBegin(Player p) {
        Zone zone = null;
        // stop menu music
        SoundPlayer.stopMusic();

        player = p;

        // prepare the exp bar so it doesnt start at 0 length
        Expbar expbar = Expbar.getInstance();
        expbar.prepare(player.experience, player.getExpRequiredForNextLevel());

        if (GameConstants.TESTINGMODE) {
            // for zone testing..
            ExpeditionCampCityZone expeditionCampCityZone = new ExpeditionCampCityZone();
            MountainPassCityZone mountainPassCityZone = new MountainPassCityZone();
            VillageZone villageZone = new VillageZone();
            SnowkeepCityZone snowkeep = new SnowkeepCityZone();
            BorderKingdomCapital bkd = new BorderKingdomCapital();
            Zone testZone = bkd; //villageZone; //expeditionCampCityZone;// mountainPassCityZone;
            testZone.entrance.addThing(player);
            return;
        }
        // move player to city, where he was before the save
        System.out.println("STARTING FROM ZONE: " + player.startZone);
        if (!player.startZone.equals("")) {
            System.out.println("PLAYER STARTZONE: " + player.startZone);
            zone = Utils.getZoneForClassName(player.startZone);
        }
        if (zone == null) {
            // if it fails, move to darkwood
            DarkwoodCityZone darkwoodCityZone = new DarkwoodCityZone();
            zone = darkwoodCityZone;
        }
        zone.entrance.addThing(player);

        // start city music
        SoundPlayer.playCityMusic();

        // game is running, and can be paused
        pauseable = true;
    }

    /**
     * Create a new character
     * @param charClass character class
     * @param name character name
     * @return
     */
    public static Player createNewCharacter(int charClass, String name) {
        String animationFramesFile = getAnimationFramesFile(charClass);
        int animationFrameWidth = getAnimationFrameWidth(charClass);

//        String name = (String) Properties.getApplicationProperties().get("Character-Name");

        Player p = new Player(name, "", animationFramesFile, 1, animationFrameWidth, charClass);
        p.initNewCharacter();

        return p;
    }

    static public String getAnimationFramesFile(int characterClass) {
        if (characterClass == Game.CLASS_WARRIOR) {
            return "/images/characters/warrior/warrior.png";
        } else if (characterClass == Game.CLASS_CLERIC) {
            return "/images/characters/cleric/cleric.png";
        } else if (characterClass == Game.CLASS_MAGE) {
            return "/images/characters/mage/mage.png";
        }
        return null;
    }

    static public int getAnimationFrameWidth(int characterClass) {
        if (characterClass == Game.CLASS_WARRIOR) {
            return 44;
        } else if (characterClass == Game.CLASS_CLERIC) {
            return 38;
        } else if (characterClass == Game.CLASS_MAGE) {
            return 29;
        }
        return -1;
    }

    public static void gameOver() {
//        Network.sendGameOverNotification(player.name,player.type,player.level,player.experience,player.money,player.headSlot.type,player.chestSlot.type,player.leftHandSlot.type,player.rightHandSlot.type);
        player.dead = true;
    }
    static Enumeration tickEnum;
    static Enumeration tickEnum2;
    static Creature t;
    static Creature creature;

    public static void tick() {

        if (player != null) {
            tickEnum = player.room.getCreatures().elements();

            while (tickEnum.hasMoreElements()) {
                t = (Creature) tickEnum.nextElement();

                t.tick();

                // does not attack if has attacked this round (attackCoolDown)
                if (t.isReadyToAttack()) {

                    if (!party.isActive() || party.isLeader) {
                        tickEnum2 = player.room.getCreatures().elements();

                        while (tickEnum2.hasMoreElements()) {
                            creature = (Creature) tickEnum2.nextElement();
                            if (t != creature && t.isHostile(creature) && creature.isAlive()) {
                                int damage = MeleeRule.evaluate(t, creature);
                                // -1 == missed, 0 == absorbed, > 0 is damage
                                //      party.sendToAll("damage:" + creature.getId() + "/" + damage); // damage is sent in harm
                                break;
                            }
                        }
                    }

                }
            }

        }

    }

    public static void setPaused(boolean value) {
        if (pauseable == true) {
            paused = value;
        }
    }

    public static boolean getPaused() {
        return paused;
    }
}
