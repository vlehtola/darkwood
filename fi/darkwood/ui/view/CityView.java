/*
 * CombatView.java
 *
 * Created on 17. toukokuuta 2007, 2:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ui.view;

import fi.darkwood.DarkwoodCanvas;
import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.Game;
import fi.darkwood.Quest;
import fi.darkwood.room.Room;
import fi.darkwood.network.HttpClient;
import fi.darkwood.room.CityRoom;
import fi.darkwood.ui.component.Common;
import fi.darkwood.ui.component.Expbar;
import fi.darkwood.util.Utils;
import fi.mirake.Local;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Teemu Kivim�ki
 */
public class CityView extends ViewAndControls {

    private Utils utils = Utils.getInstance();
    private Game game;
    private Expbar expbar;
    private int state = 2;
    private int blinkCounter = 0;

    public CityView(Game game, int width, int height) {
        super(width, height);
        this.game = game;
        expbar = Expbar.getInstance();
        leftSoftKeyText = Local.get("buttons.menu");
        rightSoftKeyText = Local.get("buttons.talk");
    }
    private Sprite sprite;
    private CityRoom room;
    private Image img;
    private int frameCounter = 1;
    private int frameCounter2 = 1;
    private int frameCounter3 = 1;

    public void updateScreen(DarkwoodGraphics g) {

        room = (CityRoom) game.player.room;

        img = utils.getImage(room.getBackground());
        // draw background
        g.drawImage(img, 0, 29, 0);

        // draw background animation
        sprite = room.getSprite();
        if (sprite != null) {
            g.setRefPixelPosition(sprite, room.spriteX, room.spriteY);
            sprite.paint(g.getGraphics());

            // determine if need to change to next frame
            frameCounter -= 1;
            if (frameCounter < 1) {
                sprite.nextFrame();
                frameCounter = room.getSpriteFrameTime(sprite.getFrame());
            }
        }

        // draw background animation for eyes
        sprite = room.getEyesSprite();
        if (sprite != null) {
            g.setRefPixelPosition(sprite, room.eyesSpriteX, room.eyesSpriteY);
            sprite.paint(g.getGraphics());

            // determine if need to change to next frame
            frameCounter2 -= 1;
            if (frameCounter2 < 1) {
                sprite.nextFrame();
                frameCounter2 = room.getEyesSpriteFrameTime(sprite.getFrame());
            }
        }

        sprite = room.getSndSprite();
        if (sprite != null) {
            g.setRefPixelPosition(sprite, room.sndSpriteX, room.sndSpriteY);
            sprite.paint(g.getGraphics());

            // determine if need to change to next frame
            frameCounter3 -= 1;
            if (frameCounter3 < 1) {
                sprite.nextFrame();
                frameCounter3 = room.getSndSpriteFrameTime(sprite.getFrame());
            }
        }


        // draw UI frames
        img = utils.getImage("/images/ui/frames_shop.png");
        g.drawImage(img, 0, 0, 0);

        // draw exp bar
        expbar.drawBar(g, game.player);

        // draw player status (hp, level etc in upper frame)
        Common.drawStatus(g, game.player);


// NEW QUEST STATE
        if (state == 0) {

            if (game.player.currentQuest != null
                    && game.player.currentQuest.equals(game.player.room.zone.firstQuest.getNextIncompleteQuest(game.player))) {
                state = 1;
            } else {
                img = utils.getImage("/images/ui/city_pergament_bg.png");
                g.drawImage(img, 10, 30, 0);

                Quest quest = game.player.room.zone.firstQuest.getNextIncompleteQuest(game.player);
                String str = Game.player.room.zone.allQuestsDoneMessage;
                if (quest != null) {
                    str = Local.get("city.quest.new") + "\n\n" + quest.getQuestText() + "\n\n";
                    str += quest.printKillRequirements();
                }
                //            CityRoom room = (CityRoom) game.player.room;
                g.drawText(str, 15, 33, 152, 152, 0, 0, DarkwoodGraphics.FONT_ARIAL10_BLACK);
            }
        }


// SHOW QUEST STATE
        if (state == 1) {

            img = utils.getImage("/images/ui/city_pergament_bg.png");
            g.drawImage(img, 10, 30, 0);

            String str = "";

            Quest quest = game.player.currentQuest;

            if (quest != null) {
                if (!quest.checkQuestDone()) {
                    str = Local.get("city.quest.current") + "\n\n" + quest.getQuestText() + "\n\n";
                    str += quest.printKillRequirements();
                } else {
                    str = Local.get("city.quest.completed") + "\n\n" + quest.getCompletedText() + "\n\n";
                    str += Local.get("city.quest.reward") + ":\n";
                    str += quest.getExpReward() + " " + Local.get("city.quest.reward.gold") + "\n";
                    str += quest.getMoneyReward() + " " + Local.get("city.quest.reward.gold") + "\n";
                }
            }
            //            CityRoom room = (CityRoom) game.player.room;
            g.drawText(str, 15, 33, 152, 152, 0, 0, DarkwoodGraphics.FONT_ARIAL10_BLACK);
        }
// SHOW BG AND EXITS STATE        
        if (state == 2) {

            if (blinkCounter < 16) {

                // blinking icons
                img = utils.getImage("/images/ui/icons/trainer.png");
                g.drawImage(img, (GAME_WIDTH / 2 - 20), 29, 0);

                img = utils.getImage("/images/ui/icons/smithy.png");
                g.drawImage(img, 0, 64, 0);

                // check if tavern exists (doesnt if no bluetooth support..)
                if (room.getEast() != null) {
                    img = utils.getImage("/images/ui/icons/tavern.png");
                    g.drawImage(img, GAME_WIDTH - 36, 64, 0);
                }

                img = utils.getImage("/images/ui/icons/map.png");
                g.drawImage(img, GAME_WIDTH / 2 - 30, 129, 0);

            }
            blinkCounter += 1;
            if (blinkCounter == 20) {
                blinkCounter = 0;
            }

            /*
            g.setColor(0xFFFFFF);
            if (game.player.room.getNorth() != null) {
            g.drawText("/\\", GAME_WIDTH / 2, 40);
            g.drawText(game.player.room.getNorth().getName(), (GAME_WIDTH / 2 - 5), 51, DarkwoodGraphics.FONT_ANTIQUA10_WHITE_BOLD);
            }
            if (game.player.room.getWest() != null) {
            g.drawText("< " + game.player.room.getWest().getName(), 0, 64);
            }
            if (game.player.room.getEast() != null) {
            g.drawText(game.player.room.getEast().getName() + " >", GAME_WIDTH - 40, 64, DarkwoodGraphics.FONT_ANTIQUA10_WHITE_BOLD);
            }
            if (game.player.room.getSouth() != null) {
            g.drawText("\\/", GAME_WIDTH / 2, GAME_HEIGHT - 65);
            g.drawText(game.player.room.getSouth().getName(), GAME_WIDTH / 2 - 5, GAME_HEIGHT - 75);
            } */


        }
        super.drawSoftKeyTexts(g);
    }

    public void move(Room directionRoom) {

        // reset softkey text when player leaves
        leftSoftKeyText = Local.get("buttons.menu");
        state = 2;

        if (directionRoom == null) {
            return;
        }

        Game.player.moveTo(directionRoom.getId(), directionRoom.getZone());

        /* old..

        // Is the player leading a party?

        if (game.party.isActive() && game.party.isLeader) {
        game.party.moveParty(directionRoom);
        } // Or adventuring alone?
        else if (!game.party.isActive()) {
        game.player.moveTo(directionRoom.getId(), directionRoom.getZone());
        } // The player is in a party and is not a leader
        else {
        System.out.println("You are not the leader.");
        }
         */
    }

    public void checkInput(int keyState) {
        if (state == 0) {
            // NEW QUEST STATE

            if (keyState == DarkwoodCanvas.SOFTKEY2_PRESSED) {
                // accept quest
                if (game.player.room.zone.firstQuest.getNextIncompleteQuest(game.player) != null) {
                    game.player.currentQuest = game.player.room.zone.firstQuest.getNextIncompleteQuest(game.player);
                }
                state = 2;
                leftSoftKeyText = Local.get("buttons.menu");
                rightSoftKeyText = Local.get("buttons.talk");

            } else if (keyState == DarkwoodCanvas.SOFTKEY1_PRESSED) {
                // decline quest
                state = 2;
                leftSoftKeyText = Local.get("buttons.menu");
                rightSoftKeyText = Local.get("buttons.talk");
            }
        } else if (state == 1) {
            // SHOW QUEST STATE

            if (keyState == DarkwoodCanvas.SOFTKEY2_PRESSED) {
                if (game.player.currentQuest != null
                        && game.player.currentQuest.checkQuestDone()) {
                    game.player.currentQuest.completeQuest(game.player);
                }
                state = 2;
                leftSoftKeyText = Local.get("buttons.menu");
                rightSoftKeyText = Local.get("buttons.talk");

                
            }

        } else if (state == 2) {
            // SHOW BG AND EXITS STATE 

            if ((keyState == GameCanvas.DOWN_PRESSED)) {
                move(game.player.room.getSouth());
                // Load the areas from cityrooms ++Ville
                // Used only to go from city to maproom.
                System.out.println("calling resetzone() from cityview down pressed.");

                Game.player.room.zone.resetZone();
            } else if ((keyState == GameCanvas.UP_PRESSED)) {
                move(game.player.room.getNorth());
            } else if ((keyState == GameCanvas.LEFT_PRESSED)) {
                move(game.player.room.getWest());
            } else if ((keyState == GameCanvas.RIGHT_PRESSED)) {
                move(game.player.room.getEast());
            } else if ((keyState == GameCanvas.GAME_A_PRESSED)) {
            } else if ((keyState == GameCanvas.GAME_B_PRESSED)) {
            } else if ((keyState == GameCanvas.GAME_C_PRESSED)) {
            } else if ((keyState == DarkwoodCanvas.SOFTKEY1_PRESSED)) {
                boolean success = HttpClient.saveCharacter(game.player);
                /*                if (success) {
                leftSoftKeyText = "Saved";
                } else {
                leftSoftKeyText = "Error";
                } */

                // goto menu
                Game.darkwoodCanvas.returnToMainMenu();

            } else if ((keyState == DarkwoodCanvas.SOFTKEY2_PRESSED)) {
                if (Game.player.currentQuest != null
                        && game.player.currentQuest.equals(
                        game.player.room.zone.firstQuest.getNextIncompleteQuest(game.player))) {
                    state = 1;
                    leftSoftKeyText = "";
                    if (game.player.currentQuest.checkQuestDone()) {
                        rightSoftKeyText = Local.get("buttons.complete");
                    } else {
                        rightSoftKeyText = Local.get("buttons.ok");
                    }

                } else {
                    // return if there is no first quest in city to prevent null pointer
                    if (game.player.room.zone.firstQuest == null) {
                        return;
                    }
                    state = 0;

                    if (game.player.room.zone.firstQuest.getNextIncompleteQuest(game.player) != null) {
                        rightSoftKeyText = Local.get("buttons.accept");
                        leftSoftKeyText = Local.get("buttons.skip");
                    } else {
                        rightSoftKeyText = Local.get("buttons.ok");
                        leftSoftKeyText = "";
                    }
                }
            }

        }
        // System.out.println("checkInput() end");
    }

    // handle touch phone controls
    public void pointerReleasedEvent(int x, int y) {
        if (x < 50 && y > 60 && y < 130) {
            checkInput(GameCanvas.LEFT_PRESSED);
        }
        if (x > 125 && y > 60 && y < 130) {
            checkInput(GameCanvas.RIGHT_PRESSED);
        }
        if (x > 50 && x < 125 && y > 20 && y < 95) {
            checkInput(GameCanvas.UP_PRESSED);
        }
        if (x > 50 && x < 125 && y > 115 && y < 175) {
            checkInput(GameCanvas.DOWN_PRESSED);
        }

    }

    public void pointerPressedEvent(int x, int y) {
    }
}
