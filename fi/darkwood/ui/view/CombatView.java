/*
 * CombatView.java
 *
 * Created on 17. toukokuuta 2007, 2:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ui.view;

import fi.darkwood.ui.component.CombatText;
import fi.darkwood.Creature;
import fi.darkwood.DarkwoodCanvas;
import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.Game;
import fi.darkwood.GameConstants;
import fi.darkwood.Logger;
import fi.darkwood.Monster;
import fi.darkwood.Player;
import fi.darkwood.Zone;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.room.Room;
import fi.darkwood.ui.component.AreaBackground;
import fi.darkwood.ui.component.Expbar;
import fi.darkwood.ui.component.MessageLog;
import fi.darkwood.ui.component.Common;
import fi.darkwood.util.Utils;
import fi.mirake.Local;
import fi.mirake.SoundPlayer;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Teemu Kivim�ki
 */
public class CombatView extends ViewAndControls {

    Utils utils = Utils.getInstance();
    Vector uielements;
    Game game;
    int currentBarLength;
    Expbar expbar;
    MessageLog messageLog;
    int background_x = 0;
    Monster monster;
    Image img;
    Enumeration e;
    Enumeration e2;
    Enumeration e3;
    Creature t;
    Player player;
    Sprite sprite;
    int abilityIconsPressed;
    AreaBackground bg;

    public CombatView(Game game, int width, int height) {
        super(width, height);
        this.game = game;
        uielements = new Vector();
        expbar = Expbar.getInstance();
        messageLog = MessageLog.getInstance();
        bg = AreaBackground.getInstance();
        leftSoftKeyText = Local.get("buttons.flee");
        rightSoftKeyText = Local.get("buttons.potion");
    }
    private static int monsterCount;
    private static int playersDrawn;    // distance between monsters (pixels)
    private static int MONSTER_DISTANCE = 22;

    public void updateScreen(DarkwoodGraphics g) {


        // draw background
        bg.drawBackground(g, game.player);

        // draw UI frames
        img = utils.getImage("/images/ui/frames.png");
        g.drawImage(img, 0, 0, 0);

        expbar.drawBar(g, Game.player);

        messageLog.drawMessageLog(g);


        // draw player status (hp, level etc in upper frame, abilities and cooldowns in lower)
        Common.drawStatus(g, Game.player);
        Common.drawAbilityIcons(g, Game.player, utils, abilityIconsPressed, false);

        e = Game.player.room.getCreatures().elements();

        monsterCount = 0;
        playersDrawn = 0;

        while (e.hasMoreElements()) {
            t = (Creature) e.nextElement();
            img = utils.getImage(t.image);
            if (t instanceof Player) {
                player = (Player) t;

                int yCoord = 160;

                int numberOfPlayers = 1;
                if (Game.party.isActive()) {
                    numberOfPlayers = Game.party.members.size();
                }

                int xCoord = (numberOfPlayers - playersDrawn - 1) * 25;

                //int xCoord = playersDrawn * 25;

                //g.drawImage(paperdollImg, xCoord, yCoord, Graphics.LEFT | Graphics.TOP);

                // draw player animation
                Common.drawPlayer(player, g, xCoord, yCoord);


                // draw the chest paper doll on the player
                Common.drawEquipmentPaperDoll(player, player.getSprite().getFrame(), g, xCoord, yCoord, utils);


                player.getSprite().nextFrame();
                /*
                
                if (!t.equals(game.player)) {
                g.setColor(0xFFFFFF);
                g.drawString(t.name, xCoord + 2, yCoord + 35, 0);
                }
                
                // DRAW HEALTH & ENERGY
                if (game.player.health > 10) {
                g.setColor(0x00FF00);
                } else {
                g.setColor(0xFF0000);
                }
                g.fillRect(0, yCoord + 2, (int) ((double) game.player.health / (double) game.player.maxHealth * 50), 2); //hela
                
                g.setColor(0x6060FF);
                g.fillRect(0, yCoord + 4, (int) ((double) game.player.mana / (double) game.player.maxMana * 50), 2); // Mana
                 */


                g.setColor(0x0000FF);
                // draw name if this is a party member
                /* Disabled, dont draw party member names in combat..
                if (t != game.player) {
                g.drawString(t.name, xCoord + 2, yCoord - 55, 0);
                } */

                // DRAW HEALTH & ENERGY
                if (game.player.health > 10) {
                    g.setColor(0x00FF00);
                } else {
                    g.setColor(0xFF0000);
                }

                g.fillRect(xCoord + 2, yCoord + 2, player.health * 23 / player.maxHealth, 2); //hela

                //      g.drawString(player.health + "", xCoord + 2, yCoord - 30, 0);

                g.setColor(0x6060FF);
                g.fillRect(xCoord + 2, yCoord + 4, player.mana * 23 / player.maxMana, 2); // Mana


                //    System.out.println("id:" + player.getId() + " hp:" + player.health + "/"+player.maxHealth + "=" + player.health * 24 / player.maxHealth);
                //     System.out.println("id:" + player.getId() + " sp:" + player.mana + "/"+player.maxMana + "=" + player.mana * 24 / player.maxMana);

                printTextEvents(g, t.textEvents, xCoord + player.getSprite().getWidth() / 2, yCoord - 71);
                drawEffects(g, t.graphicsEffects, xCoord, yCoord, player.getSprite());



                playersDrawn++;
            }

            if (t instanceof Monster) {

                monster = (Monster) t;

                int monsterXLocation = GAME_WIDTH / 2 + 10 + monsterCount * MONSTER_DISTANCE;
                int healthBarWidth = 19;

                int imageWidth = monster.getFrameWidth();

                int yCoord = 160;

                if (monster.isAlive()) {

                    //g.drawImage(paperdollImg, xCoord, yCoord, Graphics.LEFT | Graphics.TOP);
                    sprite = monster.getSprite();
                    g.defineReferencePixel(sprite, 0, img.getHeight());
                    g.setRefPixelPosition(sprite, monsterXLocation, yCoord);
                    sprite.paint(g.getGraphics());
                    sprite.nextFrame();

                    //g.drawImage(paperdollImg, monsterXLocation, yCoord, Graphics.BOTTOM | Graphics.LEFT);
                    g.setColor(0x00FF00);
                    g.fillRect(monsterXLocation + imageWidth / 2 - healthBarWidth / 2, yCoord + 2, (int) ((double) monster.health / (double) monster.maxHealth * healthBarWidth) + 1, 2);
                } else {
                    img = utils.getImage("/images/grave/grave1.png");
                    g.drawImage(img, monsterXLocation + imageWidth / 2 - img.getWidth() / 2, yCoord, Graphics.BOTTOM | Graphics.LEFT);
                }

                printTextEvents(g, t.textEvents, monsterXLocation + imageWidth / 2 - 10, yCoord - monster.getSprite().getHeight() - 20);
                // Match the middle point of images: monster and effect
                drawEffects(g, t.graphicsEffects, monsterXLocation, yCoord, monster.getSprite());


                monsterCount++;



            }

        //}
        }
        super.drawSoftKeyTexts(g);
        if (game.player.isAlive() == false) {
            // player is still uncon
            //System.out.println("Player died.");
            //   g.setColor(0xFF0000);
            g.drawText(Local.get("combat.unconsious"), 30, 84, DarkwoodGraphics.FONT_ARIAL10_RED);

            return;

        }
        // Draw skill use icons on the big screen ++Ville
        if ( fingerOnPad) {
            Common.drawAbilityIcons(g, player, utils, abilityIconsPressed, true);
            //img = utils.getImage("/images/ability/icons/power_strike.png");
            //g.drawImage(img, 90, 30, 0);
        }

    }    // variables for drawEffects()
    Image effectsImg;

    /**
     * Draw ability effects
     *
     * @param g
     * @param effects
     * @param x
     * @param y
     */
    private void drawEffects(DarkwoodGraphics g, Vector effects, int x, int y, Sprite creatureSprite) {
        e2 = effects.elements();

        while (e2.hasMoreElements()) {

            AbilityVisualEffect ef = (AbilityVisualEffect) e2.nextElement();
            int sy = ef.sprite.getHeight() / 2;
            int sx = ef.sprite.getWidth() / 2;
            if (ef.matchImageBottom) {
                // If the bottom of the image of the visual effect is matched on
                // the bottom of the creature image
                sy = ef.sprite.getHeight();
            }
            // System.out.println("sy coord "+ sy);
            ef.sprite.defineReferencePixel(sx, sy);


            // some abilities need to be matched to the center of the creature, others at the bottom center
            x = x + creatureSprite.getWidth() / 2;
            if (ef.matchImageBottom == false) {
                y = y - creatureSprite.getHeight() / 2;
            }

            g.setRefPixelPosition(ef.sprite, x, y);

            ef.sprite.paint(g.getGraphics());

            ef.sprite.nextFrame();

            if (ef.checkDuration()) {
                effects.removeElement(ef);
            }
        }
    }

    /**
     * Draw the texts rising from characters.
     * 
     * @param g
     * @param textEvents
     * @param x
     * @param y
     */
    private void printTextEvents(DarkwoodGraphics g, Vector textEvents, int x, int y) {
        e3 = textEvents.elements();
        long now = System.currentTimeMillis();
        long timex, old_timex = 0, y_coord;

        while (e3.hasMoreElements()) {
            CombatText uielement = (CombatText) e3.nextElement();
            timex = uielement.getEventTime() + 700; // duration of the event

            if ((timex - old_timex) < 600) {
                timex = old_timex + 600;
                uielement.delayText(timex - 700);
            }
            old_timex = timex;

            //g.setColor(uielement.getColor());


            y_coord = y - (int) ((now - timex) / 50);

            g.drawText(uielement.getText(), x, (int) y_coord, uielement.getFont()); // Change this to use premade fonts ++Ville
        }
        // Loop elements again to remove expired ones
        e3 = textEvents.elements();
        while (e3.hasMoreElements()) {
            CombatText uielement = (CombatText) e3.nextElement();
            timex = uielement.getEventTime() + 700; // duration of the event

            if (now > timex) {
                textEvents.removeElement(uielement);
            }
        }

    }

    public void checkInput(int keyState) {
        abilityIconsPressed = 0;

        if ((keyState == GameCanvas.DOWN_PRESSED)) {
            game.player.invokeActiveAbility(2);
            abilityIconsPressed = abilityIconsPressed | Common.ACTIVE_ABILITY_FLAGS[2];
        } else if ((keyState == GameCanvas.UP_PRESSED)) {
            game.player.invokeActiveAbility(0);
            abilityIconsPressed = abilityIconsPressed | Common.ACTIVE_ABILITY_FLAGS[0];
        } else if ((keyState == GameCanvas.LEFT_PRESSED)) {
            game.player.invokeActiveAbility(1);
            abilityIconsPressed = abilityIconsPressed | Common.ACTIVE_ABILITY_FLAGS[1];
        } else if ((keyState == GameCanvas.RIGHT_PRESSED)) {
            game.player.invokeActiveAbility(3);
            abilityIconsPressed = abilityIconsPressed | Common.ACTIVE_ABILITY_FLAGS[3];
        } else if ((keyState == GameCanvas.GAME_A_PRESSED)) {
        } else if ((keyState == GameCanvas.GAME_B_PRESSED)) {
        } else if ((keyState == GameCanvas.GAME_C_PRESSED)) {
            // In testing mode
            if (GameConstants.TESTINGMODE) {
                game.player.strength += 500;
            }
        } else if ((keyState == DarkwoodCanvas.SOFTKEY2_PRESSED)) {
            if (game.player.healingPotions > 0 && Game.player.isAlive()) {
                game.player.healingPotions--;
                game.player.heal(20);
            }
        } else if ((keyState == DarkwoodCanvas.SOFTKEY1_PRESSED)) {
            Logger.getInstance().debug("Run away!");

            // Flee the combat

            // if player was uncon, add penalty exp buffer
            if (!game.player.isAlive()) {
                game.player.resetDeathPenalty();
            }


            Zone fallbackZone = Utils.getZoneForClassName(Game.player.room.getZone().getFallbackZone());
            Room fallbackRoom = (Room) fallbackZone.rooms.elementAt(0);
            if (Game.party.isActive()) {
                Game.party.movePartyLocally(fallbackRoom);
                Game.party.fallback();

                // start city music
                SoundPlayer.playCityMusic();
            } else {
                Game.player.moveTo(0, fallbackZone);

                // start city music
                SoundPlayer.playCityMusic();
            }

//            Zone fallbackZone = Utils.getZoneForClassName(game.player.room.getZone().getFallbackZone());
//            Room fallbackRoom = (Room) fallbackZone.rooms.elementAt(0);
//            game.player.room.removeThing(game.player);
//            fallbackRoom.addThing(game.player);

        }
    }
   
    public boolean fingerOnPad = false;

    public void pointerReleasedEvent(int x, int y) {
        System.out.println("combatview - pointerReleasedEvent");
        //65,74
        fingerOnPad = false;
        // touch pad buttons at the middle of the screen
        if (x > 65 && x < 114 && y <= 74) {
            checkInput(GameCanvas.UP_PRESSED);
        } else if (x > 65 && x < 114 && y >= 118) {
            checkInput(GameCanvas.DOWN_PRESSED);
        } else if ( x < 89 && y > 74 && y < 118) {
            checkInput(GameCanvas.LEFT_PRESSED);
        } else if (x > 89 && y > 74 && y < 118) {
            checkInput(GameCanvas.RIGHT_PRESSED);
        }
        // touch pad buttons as the bottom of the screen
        if (x > 70 && x < 104 && y < 174 && y > 100) {
            checkInput(GameCanvas.UP_PRESSED);
        } else if (x > 60 && x < 115 && y > 190) {
            checkInput(GameCanvas.DOWN_PRESSED);
        } else if (x > 60 && x < 78 && y > 174 && y < 190) {
            checkInput(GameCanvas.LEFT_PRESSED);
        } else if (x > 95 && x < 115 && y > 174 && y < 190) {
            checkInput(GameCanvas.RIGHT_PRESSED);
        }

    }

    public void pointerPressedEvent(int x, int y) {
        System.out.println("combatview - pointerPressedEvent");
        fingerOnPad = true;
    }
}
