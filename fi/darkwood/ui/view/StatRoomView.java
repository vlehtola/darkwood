/*
 * CombatView.java
 *
 * Created on 17. toukokuuta 2007, 2:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ui.view;

import fi.darkwood.Creature;
import fi.darkwood.DarkwoodCanvas;
import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.Game;
import fi.darkwood.GameConstants;
import fi.darkwood.room.StatRoom;
import fi.darkwood.rule.MeleeRule;
import fi.darkwood.ui.component.Common;
import fi.darkwood.ui.component.Expbar;
import fi.darkwood.util.StringUtils;
import fi.darkwood.util.Utils;
import fi.mirake.Local;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author Teemu Kivim�ki
 */
public class StatRoomView extends ViewAndControls {

    Utils utils = Utils.getInstance();
    Game game;
    Expbar expbar;
    int choice;
    // state 0: stat training view, state 1: ability training view
    int state = 0;
    private static final int CHOICE_STR = GameConstants.STAT_STR;
    private static final int CHOICE_DEX = GameConstants.STAT_DEX;
    private static final int CHOICE_CON = GameConstants.STAT_CON;
    private static final int CHOICE_INT = GameConstants.STAT_WP;

    //   private BitMapFont bitMapFont = BitMapFont.getInstance("/fonts/arial10.bmf");;
    //  private BitMapFontViewer messageViewer;
    public StatRoomView(Game game, int width, int height) {
        super(width, height);
        this.game = game;
        expbar = Expbar.getInstance();
        leftSoftKeyText = Local.get("buttons.exit");
        rightSoftKeyText = Local.get("buttons.select");

    }
    private final static int[] ABILITY_X = {6, 6, 86, 86};
    private final static int[] ABILITY_Y = {37, 62, 37, 62};

    public void updateScreen(DarkwoodGraphics g) {

        Image img = utils.getImage(Game.player.room.getBackground());

        // draw background
        g.drawImage(img, 0, 29, 0);

        // draw UI frames
        img = utils.getImage("/images/ui/frames_shop.png");
        g.drawImage(img, 0, 0, 0);

        // draw exp bar
        expbar.drawBar(g, game.player);

        // draw status        
        Common.drawStatus(g, game.player);



        if (state == 0) {

            int yLoc = 21;

//        g.drawText("                Total  (Trained)", 10, yLoc + 10);

            g.drawText(Local.get("trainer.train.str") + ": " + game.player.trainedStats[GameConstants.STAT_STR] + " (" + Local.get("trainer.trained") + ")", 10, yLoc + 10);
            g.drawText(Local.get("trainer.train.dex") + ": " + game.player.trainedStats[GameConstants.STAT_DEX] + " (" + Local.get("trainer.trained") + ")", 10, yLoc + 20);
            g.drawText(Local.get("trainer.train.con") + ": " + game.player.trainedStats[GameConstants.STAT_CON] + " (" + Local.get("trainer.trained") + ")", 10, yLoc + 30);
            g.drawText(Local.get("trainer.train.wil") + ": " + game.player.trainedStats[GameConstants.STAT_WP] + " (" + Local.get("trainer.trained") + ")", 10, yLoc + 40);
            g.drawText(Local.get("trainer.show.abilities"), 10, yLoc + 50);
            g.drawText(">", 0, choice * 10 + 10 + yLoc);

            g.drawText(Local.get("trainer.available.points") + ": " + game.player.getAvailableStatPoints(), 10, yLoc + 65);

            String description = "";
            switch (choice) {
                case CHOICE_STR:
                    int strmod = (int) (MeleeRule.getStrengthDamageModifier((Creature) Game.player) * 10);

                    description = Local.get("trainer.str.desc");
                    description = StringUtils.replace(description, "$1", ""+(double) strmod / 10.0);

                    break;
                case CHOICE_CON:
                    description = Local.get("trainer.con.desc");
                    break;
                case CHOICE_DEX:
                    description = Local.get("trainer.dex.desc");
                    description = StringUtils.replace(description, "$1", ""+Game.player.getPersonalAttackCooldown());

                    break;
                case CHOICE_INT:
                    description = Local.get("trainer.wil.desc");
                    break;
                case 4:
                    description = "";
                    break;
            }


// initialize messageViewer and draw stat description in it
            int availableWidth = 158;
            int padding = 0;
            int textOrientation = Graphics.LEFT;

            int cost = getCost();

            description = description + " " + Local.get("trainer.cost") + ": " + cost + " " + Local.get("city.quest.reward.gold");
            if (choice != 4) {
                g.drawText(description, 10, 95, availableWidth, availableWidth, padding, textOrientation, DarkwoodGraphics.FONT_ARIAL10);
            }
        }

        if (state == 1) {
            int yLoc = 22;


            for (int i = 0; i < 4; i++) {
                if (Game.player.activeAbilities[i] != null) {
                    img = utils.getImage(Game.player.activeAbilities[i].getImage(false, false));
                    g.drawImage(img, ABILITY_X[i], ABILITY_Y[i], 0);
                    g.drawText(Game.player.activeAbilities[i].name, ABILITY_X[i] + 24, ABILITY_Y[i]);
                    g.drawText(Local.get("character.level") + " " + Game.player.activeAbilities[i].getLevel(), ABILITY_X[i] + 24, ABILITY_Y[i] + 10);

                } else {
                    img = utils.getImage("/images/ability/icons/empty_slot.png");
                    g.drawImage(img, ABILITY_X[i], ABILITY_Y[i], 0);
                }
                if (choice == i) {
                    g.setColor(0x00FBFF38);
                    g.drawRect(ABILITY_X[i] - 1, ABILITY_Y[i] - 1, img.getWidth() + 2, img.getHeight() + 2);
                }

            }

            String description = Local.get("trainer.ability.locked");
            if (Game.player.activeAbilities[choice] != null) {
                // Long description of the ability
                description = Game.player.activeAbilities[choice].name + " - " + Local.get("character.level") + " " + Game.player.activeAbilities[choice].getLevel();
                description += "\n" + Game.player.activeAbilities[choice].getDescription();
            }
            g.drawText(description, 10, 95, 160, 160, 0, 0);



// initialize messageViewer and draw stat description in it
            int availableWidth = 158;
            int padding = 0;
            int textOrientation = Graphics.LEFT;

            int cost = getCost();

//            description = description + " Training cost: " + cost + " gold";

        //          g.drawText(description, 10, 90, availableWidth, availableWidth, padding, textOrientation, DarkwoodGraphics.FONT_ARIAL10);

        }

        Common.drawStatsbar(g);
        super.drawSoftKeyTexts(g);

    }

    public void checkInput(int keyState) {
        int cost;
        if (state == 0) {
            if (keyState == GameCanvas.DOWN_PRESSED) {
                choice += 1;
                if (choice > 4) {
                    choice = 0;
                }

            } else if (keyState == GameCanvas.UP_PRESSED) {
                choice -= 1;
                if (choice < 0) {
                    choice = 4;
                }

            }
        } else if (state == 1) {
            if (keyState == GameCanvas.DOWN_PRESSED) {
                choice += 1;
                if (choice > 3) {
                    choice = 0;
                }

            } else if (keyState == GameCanvas.UP_PRESSED) {
                choice -= 1;
                if (choice < 0) {
                    choice = 3;
                }

            }
        }

        if (keyState == DarkwoodCanvas.SOFTKEY1_PRESSED) {
            if (state == 1) {
                state = 0;
                leftSoftKeyText = Local.get("buttons.exit");
                rightSoftKeyText = Local.get("buttons.select");
            } else {
                StatRoom room = (StatRoom) Game.player.room;

                room.removeThing(Game.player);
                room.exit.addThing(Game.player);
            }
        } else if (keyState == DarkwoodCanvas.SOFTKEY2_PRESSED) {
            if (choice == 4) {
                choice = 0;
                state = 1;
                leftSoftKeyText = Local.get("buttons.back");
                rightSoftKeyText = "";
            } else {
                if (game.player.getAvailableStatPoints() > 0) {
                    // Set some good cost here
                    cost = getCost();
                    if (game.player.money >= cost) {
                        game.player.money -= cost;
                        game.player.trainedStats[choice] += 1;
                        game.player.updateStats();
                    }

                }
            }
        }
    }

    private int getCost() {
        if (choice == 4) {
            return 0;
        }
        int cost = game.player.trainedStats[choice] / 4 + 1;
        return cost;
    }
     // Keep it simple in case the user is clumsy with the touchpad ++Ville
    public void pointerReleasedEvent(int x, int y) {
        if (y < 95) {
            checkInput(GameCanvas.UP_PRESSED);
        }
        if (y > 95 && y < 170) {
            checkInput(GameCanvas.DOWN_PRESSED);
        }

    }
     public void pointerPressedEvent(int x, int y) {

    }
}


