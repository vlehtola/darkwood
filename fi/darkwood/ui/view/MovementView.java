/*
 * CombatView.java
 *
 * Created on 17. toukokuuta 2007, 2:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ui.view;

import fi.darkwood.util.Font;
import fi.darkwood.Chest;
import fi.darkwood.DarkwoodCanvas;
import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.Equipment;
import fi.darkwood.Game;
import fi.darkwood.GameConstants;
import fi.darkwood.UsableThing;
import fi.darkwood.Logger;
import fi.darkwood.Monster;
import fi.darkwood.Player;
import fi.darkwood.room.Room;
import fi.darkwood.Thing;
import fi.darkwood.Zone;
import fi.darkwood.ui.component.AreaBackground;
import fi.darkwood.ui.component.Expbar;
import fi.darkwood.ui.component.Common;
import fi.darkwood.ui.component.MessageLog;
import fi.darkwood.util.StringUtils;
import fi.darkwood.util.Utils;
import fi.mirake.Local;
import fi.mirake.SoundPlayer;
import java.util.Enumeration;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Teemu Kivim�ki
 */
public class MovementView extends ViewAndControls {

    MessageLog messageLog = MessageLog.getInstance();
    Utils utils = Utils.getInstance();
    Game game;
    Expbar expbar;
    int showMessageTimeLeft = 0;
    int playerAnimationFrame = 0;
    int background_x = 0;
    Player player;
    Monster monster;
    Sprite sprite;
    Image img;
    AreaBackground bg;
    private boolean lootMode = false;     // variables for chest looting
    private Chest lootChest = null;     // variables for chest looting

    public MovementView(Game game, int width, int height) {
        super(width, height);
        this.game = game;
        expbar = Expbar.getInstance();
        bg = AreaBackground.getInstance();
        // leftSoftKeyText = "Leave";
        // rightSoftKeyText = "";
    }

    public void updateScreen(DarkwoodGraphics g) {

        leftSoftKeyText = Local.get("buttons.leave");
        rightSoftKeyText = "";

        img = utils.getImage(game.player.room.zone.getBackground());

        // draw background
        bg.drawBackground(g, game.player);

        // draw UI frames
        img = utils.getImage("/images/ui/frames_shop.png");
        g.drawImage(img, 0, 0, 0);

        // draw exp bar
        expbar.drawBar(g, game.player);

        Common.drawStatus(g, game.player);

        messageLog.drawMessageLog(g);

        Enumeration e = game.player.room.getThings().elements();

        int monsterCount = 0;
        int monsterDistance = 22;
        int playersDrawn = 0;

        while (e.hasMoreElements()) {
            Thing t = (Thing) e.nextElement();
            img = utils.getImage(t.image);

            if (t instanceof Chest) {
                Chest ch = (Chest) t;
                g.drawImage(img, 150, 162, Graphics.BOTTOM | Graphics.LEFT);
                if (ch.isOpen() || lootMode) {
                    // If the chest is already open and empty OR we are now looting it
                    rightSoftKeyText = "";
                } else {
                    // Opening the chest is available
                    rightSoftKeyText = Local.get("buttons.open");
                }
            }
            if (t instanceof UsableThing) {
                UsableThing hs = (UsableThing) t;
                sprite = t.getSprite();
                if (sprite == null) {
                    g.drawImage(img, 100, 162, Graphics.BOTTOM | Graphics.LEFT);
                } else {
                    sprite = t.getSprite();
                    g.defineReferencePixel(sprite, 0, sprite.getHeight());
                    g.setRefPixelPosition(sprite, 100, 162);
                    sprite.paint(g.getGraphics());
                    sprite.nextFrame();
                }
                if (hs.isUsed()) {
                    rightSoftKeyText = hs.queryUsedText();
                } else {
                    rightSoftKeyText = hs.queryUnusedText();
                }
            }
            if (t instanceof Player) {

                player = (Player) t;

                int yCoord = 160;

                int numberOfPlayers = 1;
                if (Game.party.isActive()) {
                    numberOfPlayers = Game.party.members.size();
                }

                int xCoord = (numberOfPlayers - playersDrawn - 1) * 25;

                Common.drawPlayer(player, g, xCoord, yCoord);

                // draw the chest paper doll on the player
                Common.drawEquipmentPaperDoll(player, player.getSprite().getFrame(), g, xCoord, yCoord, utils);


                player.getSprite().nextFrame();
                //                g.drawImage(img, xCoord, yCoord, 0);

                // increment playersDrawn so next player looped will be drawn behind the first
                playersDrawn++;

                g.setColor(0x0000FF);
                // draw name if this is a party member
                if (t != game.player) {
//                    g.drawString(t.name, xCoord + 2, yCoord - 55, 0);
                    g.drawText(t.name, xCoord + 2, yCoord - 65, DarkwoodGraphics.FONT_ARIAL10_LIGHTBLUE);
                }

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


            }


            if (t instanceof Monster) {

                // in movement view, monsters are always dead (only draw graves)

                monster = (Monster) t;
                int monsterXLocation = GAME_WIDTH / 2 + 10 + monsterCount * monsterDistance;
                int healthBarWidth = 20;

                int imageWidth = monster.getFrameWidth();
                if (imageWidth == 0) {
                    imageWidth = img.getWidth();
                }

                int yCoord = 160;

                img = utils.getImage("/images/grave/grave1.png");

                g.drawImage(img, monsterXLocation + imageWidth / 2 - img.getWidth() / 2, yCoord, Graphics.BOTTOM | Graphics.LEFT);

                monsterCount++;



            }


        }

        g.setColor(0xFFFFFF);

        if (game.player.room.getNorth() != null) {
            g.drawString("/\\", GAME_WIDTH / 2, 40, 0);
            g.drawString(game.player.room.getNorth().getPath(), GAME_WIDTH / 2 - 5, 51, 0);
        }
        if (game.player.room.getSouth() != null) {
            g.drawString("\\/", GAME_WIDTH / 2, GAME_HEIGHT - 65, 0);
            g.drawString(game.player.room.getSouth().getPath(), GAME_WIDTH / 2 - 5, GAME_HEIGHT - 75, 0);
        }

        if (Game.player.room.getEast() != null) {
            // Draw the forward arrow and the path name eg. "Forest"
            g.drawText(Game.player.room.getPath() + "", GAME_WIDTH / 2 - 10, GAME_HEIGHT - 40, DarkwoodGraphics.FONT_ANTIQUA10_WHITE_BOLD);
            img = utils.getImage("/images/ui/simple_arrow.png");
            g.drawImage(img, GAME_WIDTH / 2 - 5, GAME_HEIGHT - 20, 0);
        }

        /*        if (showMessageTimeLeft == 0 && gotEquipment != null) {
        showMessageTimeLeft = 60;
        
        }
        if (showMessageTimeLeft > 0) {
        g.setColor(0xFFFFFF);
        g.drawImage(utils.getImage(gotEquipment.image), 60, 100, 0);
        g.drawString(gotEquipment.name + " GET ", 80, 100, 0);
        showMessageTimeLeft = showMessageTimeLeft - 1;
        if (showMessageTimeLeft == 0) {
        gotEquipment = null;
        }
        } */

        // If we are looting a chest that contains an item, player needs
        // to choose if he wants to take item or gold
        if (lootMode) {

            img = utils.getImage("/images/ui/city_pergament_bg.png");
            g.drawImage(img, 10, 30, 0);
            g.drawText(Local.get("movement.choose.reward"), 15, 35, DarkwoodGraphics.FONT_ARIAL10_BLACK);

            g.drawText(lootChest.getMoney() + " " + Local.get("city.quest.reward.gold"), 15, 45);

            g.drawText(Local.get("movement.choose.or"), 20, 55, DarkwoodGraphics.FONT_ARIAL10_BLACK);

            // DRAW ITEM AVAILABLE FOR LOOTING
            Equipment lootItem = lootChest.getEquipment();
            img = utils.getImage(lootItem.image);
            g.drawImage(img, 15, 65, 0);


            // draw item name, in the color corresponding to the item rarity        
            Font font = DarkwoodGraphics.FONT_ARIAL10_BLACK;
            if (lootItem.quality == GameConstants.QUALITY_UNCOMMON) {
                font = DarkwoodGraphics.FONT_ARIAL10_DARKGREEN;
            }
            if (lootItem.quality == GameConstants.QUALITY_RARE) {
                font = DarkwoodGraphics.FONT_ARIAL10_LIGHTBLUE;
            }
            // cost and level
            g.drawText(lootItem.description + "", 35, 67, font);

            // stats
            g.drawText(Local.get("character.level") + ": " + lootItem.getLevel() + " " + lootItem.getItemInfo(), 15, 80, DarkwoodGraphics.FONT_ARIAL10_BLACK);

            // Level requirement text
            if (lootItem.getLevel() - GameConstants.EQUIPMENT_LEVEL_REQUIREMENT_GAP > Game.player.level) {
                g.drawText("(" + Local.get("movement.choose.insufficent") + ")", 38, 55, DarkwoodGraphics.FONT_ARIAL10_RED);
            }

            // DRAW CURRENT EQUIPMENT (to be replaced by new eq)

            int y = 95;

            Equipment currentEq = Game.player.equipmentSlots[lootItem.getSlot()];
            if (currentEq != null) {
                g.drawText(Local.get("movement.choose.replaced"), 15, y);

                img = utils.getImage(currentEq.image);
                g.drawImage(img, 15, y + 10, 0);

                // draw item name, in the color corresponding to the item rarity        
                font = DarkwoodGraphics.FONT_ARIAL10_BLACK;
                if (currentEq.quality == GameConstants.QUALITY_UNCOMMON) {
                    font = DarkwoodGraphics.FONT_ARIAL10_DARKGREEN;
                }
                if (currentEq.quality == GameConstants.QUALITY_RARE) {
                    font = DarkwoodGraphics.FONT_ARIAL10_LIGHTBLUE;
                }
                g.drawText(currentEq.description + "", 35, y + 12, font);

                // draw stats
                g.drawText(Local.get("character.level") + ": " + currentEq.getLevel() + " " + currentEq.getItemInfo(), 15, y + 25, DarkwoodGraphics.FONT_ARIAL10_BLACK);


            }

            leftSoftKeyText = Local.get("buttons.gold");
            rightSoftKeyText = Local.get("buttons.item");


        }


        // finally draw the softkey texts
        super.drawSoftKeyTexts(g);

    }

    public void move(Room destinationRoom) {
        if (destinationRoom == null) {
            return;
        }
        // Is the player leading a party?
        if (game.party.isActive() && game.party.isLeader) {
            game.party.moveParty(destinationRoom);
            // todo: party cooldowns
        } // Or adventuring alone?
        else if (!game.party.isActive()) {
            game.player.moveTo(destinationRoom.getId(), destinationRoom.getZone());
            game.player.rest();
            game.player.resetCoolDowns();
        } // The player is in a party and is not a connectionToLeader
        else {
            MessageLog.getInstance().addMessage(Local.get("movement.notleader"));
        }

    }

    public void checkInput(int keyState) {
        if (lootMode) {
            checkLootModeInput(keyState);
        } else {

            if ((keyState == GameCanvas.DOWN_PRESSED)) {
                move(game.player.room.getSouth());
            } else if ((keyState == GameCanvas.UP_PRESSED)) {
                move(game.player.room.getNorth());
            } else if ((keyState == GameCanvas.RIGHT_PRESSED)) {
                move(game.player.room.getEast());
            } else if ((keyState == GameCanvas.FIRE)) {
                move(game.player.room.getEast());
            } else if ((keyState == GameCanvas.LEFT_PRESSED)) {
                if (GameConstants.TESTINGMODE) {
                    Game.player.harm(Game.player.health - 1);
                }
                /*
                Zone fallbackZone = Utils.getZoneForClassName(game.player.room.getZone().getFallbackZone());
                Room fallbackRoom = (Room) fallbackZone.rooms.elementAt(0);
                game.player.room.removeThing(game.player);
                fallbackRoom.addThing(game.player);
                //     game.player.lastMovementDirection = Direction.Exit;
                 */

            } else if ((keyState == GameCanvas.GAME_A_PRESSED)) {
                Game.showConsole = true;


            } else if ((keyState == DarkwoodCanvas.SOFTKEY1_PRESSED)) {
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


                /*            Enumeration e = Game.party.members.elements();
                while (e.hasMoreElements()) {
                Creature member = (Creature) e.nextElement();
                member.moveTo(destination.getId(), destination.getZone());
                } */
                //            Game.player.room.removeThing(Game.player);
//            fallbackRoom.addThing(Game.player);

            } else if ((keyState == DarkwoodCanvas.SOFTKEY2_PRESSED)) {
                boolean useSuccess = false;
                Enumeration e = Game.player.room.getThings().elements();
                Logger.getInstance().debug("Trying to open a chest.");
                while (e.hasMoreElements()) {
                    Thing t = (Thing) e.nextElement();
                    //               Image img = utils.getImage(t.image);
                    if (t instanceof Chest) {
                        Chest c = (Chest) t;
                        if (!c.isOpen()) {
                            // if the chest was closed, we can loot it

                            if (c.getEquipment() != null) {
                                // if there was an item, goto loot mode (player must choose gold or item)
                                lootMode = true;
                                lootChest = c;
                            } else {
                                c.setOpen(true);
                                Game.player.addMoney(c.getMoney());

                                String msg = Local.get("movement.get.gold");
                                // replace $1 in the localization string with target name
                                msg = StringUtils.replace(msg, "$1", "" + c.getMoney());

                                MessageLog.getInstance().addMessage(msg);
                            }


                        }
                        useSuccess = true;
                    }
                    if (t instanceof UsableThing) {
                        UsableThing c = (UsableThing) t;
                        if (!c.isUsed()) {
                            c.useThing();
                            //MessageLog.getInstance().addMessage("You are healed!");
                        }
                        useSuccess = true;
                    }
                }
                if (useSuccess == false) {
                    System.out.println("No chest here.");
                }
            } else if ((keyState == GameCanvas.GAME_B_PRESSED)) {
                //       game.party.createParty(true);
                System.out.println("Starting party..");
                Logger.getInstance().debug("Starting party..");
            } else if ((keyState == GameCanvas.GAME_C_PRESSED)) {
                Logger.getInstance().debug("Connecting to party leader..");
                System.out.println("Connecting to party leader..");
                //       game.party.createParty(false);
            } else if ((keyState == GameCanvas.GAME_D_PRESSED)) {
                /*       String xmlString = CharacterSerializer.createXmlString(game.player,
                "sonique", "jees", game.player.room.getZone().getClass().getName());
                
                int newId = 0;
                try {
                // save the character and read assigned id from the response
                newId = HttpClient.saveCharacterXml(xmlString, player.getId());
                } catch (IOException e) {
                e.printStackTrace();
                }
                
                // if player doesnt have an id, set the id returned in save
                if (player.getId() < 1) {
                player.setGlobalCharacterId(newId);
                
                } */            //LocalDatabase.saveCharacterXML(xmlString);
            }
        }
    }

    private void checkLootModeInput(int keyState) {
        if ((keyState == DarkwoodCanvas.SOFTKEY1_PRESSED)) {
            // player pressed Gold

            Game.player.addMoney(lootChest.getMoney());

            String msg = Local.get("movement.get.gold");
            // replace $1 in the localization string with target name
            msg = StringUtils.replace(msg, "$1", "" + lootChest.getMoney());
            MessageLog.getInstance().addMessage(msg);

            lootMode = false;
            lootChest.setOpen(true);

        } else if ((keyState == DarkwoodCanvas.SOFTKEY2_PRESSED)) {
            // player pressed Item
            Equipment eq = lootChest.getEquipment();
            if (eq.getLevel() - GameConstants.EQUIPMENT_LEVEL_REQUIREMENT_GAP > Game.player.level) {
                MessageLog.getInstance().addMessage(Local.get("movement.choose.cannot.wear") + " " + lootChest.getEquipment().name + "!");
                return;
            }
            Game.player.equip(eq);
            MessageLog.getInstance().addMessage(Local.get("movement.choose.use") + " " + lootChest.getEquipment().name + "!");
            lootMode = false;
            lootChest.setOpen(true);
        }
    }

    public void pointerReleasedEvent(int x, int y) {

        if (x > 60 && x < 115 && y > 110) {
            checkInput(GameCanvas.RIGHT_PRESSED);
        }

    }

    public void pointerPressedEvent(int x, int y) {
    }
}
