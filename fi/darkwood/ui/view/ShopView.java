/*
 * ShopView.java
 *
 * Created on 23. joulukuuta 2007, 20:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ui.view;

import fi.darkwood.util.Font;
import fi.darkwood.Equipment;
import fi.darkwood.Game;
import fi.darkwood.room.Room;
import fi.darkwood.room.ShopRoom;
import fi.darkwood.ui.component.Expbar;
import fi.darkwood.util.Utils;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import fi.darkwood.DarkwoodCanvas;
import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.GameConstants;
import fi.darkwood.Item;
import fi.darkwood.Potion;
import fi.darkwood.ui.component.Common;
import fi.mirake.Local;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Exko
 */
public class ShopView extends ViewAndControls {

    Utils utils = Utils.getInstance();
    Game game;
    Expbar expbar;
    Vector vendorInvendory;
    Sprite sprite;
    boolean showWelcomeText = true;
    /*int[] slotX = {23, 53, 26, 2, 26, 16};
    int[] slotY = {36, 67, 75, 98, 114, 147};*/
    int[] slotX = {3, 3, 3, 3};
    int[] slotY = {33, 48, 62, 77, 93, 108};
    // itemX =93
    int[] itemY = {35, 60, 85};
    // this number marks the selected item number worn by the player
    int slotIndex = 0;
    // this number marks the selected item in the shop inventory
    int shopInventoryIndex = -1;
    // focus is 0 if player items are selected and 1 if shop inventory items are selected
    int focus = 0;
    int yCoord = 88;
    int xCoord = 24;
    boolean onRight = false;

    public ShopView(Game game, int width, int height) {
        super(width, height);

        this.game = game;
        expbar = Expbar.getInstance();
        leftSoftKeyText = Local.get("buttons.exit");
        rightSoftKeyText = Local.get("buttons.buy");
    }
    static int slotFocusWidth = 15;
    static int slotFocusHeight = 15;

    public void updateScreen(DarkwoodGraphics g) {

        // draw background
        Image img = utils.getImage(game.player.room.getBackground());
        g.drawImage(img, 0, 29, 0);


        // draw empty slots
        for (int i = 0; i < slotX.length; i++) {
            g.setColor(125, 125, 125);
            g.fillRect(slotX[i], slotY[i], 15, 15);
            g.setColor(175, 175, 175);
            g.drawRect(slotX[i], slotY[i], 15, 15);
        }


        // draw pergament background
        img = Utils.getInstance().getImage("/images/ui/shop_pergament_bg.png");
        g.drawImage(img, 3, 97, 0);


        // draw items in the slots, return the currently selected item
        Item equ = drawSlots(g);

        // draw item description or welcome text
        if (showWelcomeText) {
            drawWelcomeText(g);
        } else if (equ != null && focus == 0) {
            drawItemInfo(g, equ);
        }

        // draw vendor items
        ShopRoom sr = (ShopRoom) game.player.room;
        vendorInvendory = sr.getInventory();

        int invendoryY = 32;
        int imgIndex = 0;
        int imgHeight = 15;
        int imgWidth = 15;


        for (int i = shopInventoryIndex; i < vendorInvendory.size(); i++) {
            //113 80 60      
            if (i >= 0) {
                equ = (Item) vendorInvendory.elementAt(i);
                Image itemImg = utils.getImage(equ.image);
                g.setColor(125, 125, 125);
                g.fillRect(inventoryX, invendoryY, itemImg.getWidth(), itemImg.getHeight());
                g.setColor(175, 175, 175);
                g.drawRect(inventoryX, invendoryY, itemImg.getWidth(), itemImg.getHeight());


                Font font = DarkwoodGraphics.FONT_ARIAL10;
                if (equ.quality == GameConstants.QUALITY_UNCOMMON) {
                    font = DarkwoodGraphics.FONT_ARIAL10_GREEN;
                }
                if (equ.quality == GameConstants.QUALITY_RARE) {
                    font = DarkwoodGraphics.FONT_ARIAL10_LIGHTBLUE;
                }
                Font levelFont = DarkwoodGraphics.FONT_ARIAL10;
                if (equ.getLevel() - GameConstants.EQUIPMENT_LEVEL_REQUIREMENT_GAP > Game.player.level) {
                    levelFont = DarkwoodGraphics.FONT_ARIAL10_RED;
                }

                g.drawText(equ.name, inventoryX + 2 + itemImg.getWidth(), invendoryY - 1, font);
                g.drawText(equ.value + " " + Local.get("shop.gold.abbreviation") + " ", inventoryX + 2 + itemImg.getWidth(), invendoryY + 8);
                g.drawText(equ.getLevel() + " " + Local.get("shop.level.abbreviation"), inventoryX + 42 + itemImg.getWidth(), invendoryY + 8, levelFont);

                g.drawImage(itemImg, inventoryX, invendoryY, 0);

                if (imgIndex == 1 && focus == 1) {
                    // Here we draw the detailed info about the active item to the bottom of the screen

                    // pergament background
//                    img = Utils.getInstance().getImage("/images/ui/shop_pergament_bg.png");
//                    g.drawImage(img, 3, 106, 0);

                    availableWidth = 170;
                    drawItemInfo(g, equ);
                }

            }
            invendoryY += 23;
            imgIndex++;

            if (invendoryY > 85) {
                break;
            }
        }

        //draw slot index
        g.setColor(250, 250, 50);
        if (focus == 0) { // player items are selected..
            if (slotIndex >= 0) {
                // draw rectangle around the selected slot
                g.drawRect(slotX[slotIndex], slotY[slotIndex], slotFocusWidth, slotFocusHeight);
            }
            // draw the focus indicator (rectangle around all player items)
            g.drawRect(0, 30, 60, 65);
        } else if (focus == 1) { // shop items are selected..
            if (vendorInvendory.size() > 0) {
                g.drawRect(inventoryX - 1, 55, imgWidth, imgHeight);
            }
            g.drawRect(inventoryX - 3, 30, 105, 65);
            g.setColor(250, 0, 0);
            if (slotIndex >= 0) {
                g.drawRect(slotX[slotIndex], slotY[slotIndex], slotFocusWidth, slotFocusHeight);
            }
        }

        // draw UI frames
        img = utils.getImage("/images/ui/frames_shop.png");
        g.drawImage(img, 0, 0, 0);

        // draw the player
        Common.drawPlayer(game.player, g, xCoord, yCoord);

        // draw the chest paper doll on the player
        Common.drawEquipmentPaperDoll(game.player, game.player.getSprite().getFrame(), g, xCoord, yCoord, utils, slotIndex);

        game.player.getSprite().nextFrame();

        // draw exp bar
        expbar.drawBar(g, game.player);

        Common.drawStatus(g, game.player);

        Common.drawStatsbar(g);

        super.drawSoftKeyTexts(g);

    }
    int inventoryX = 70;
    int availableWidth = 165;
    int padding = 0;
    int textOrientation = Graphics.LEFT;

    private void drawItemInfo(DarkwoodGraphics g, Item item) {

        // draw item name, in the color corresponding to the item rarity        
        Font font = DarkwoodGraphics.FONT_ARIAL10_BLACK;
        if (item.quality == GameConstants.QUALITY_UNCOMMON) {
            font = DarkwoodGraphics.FONT_ARIAL10_DARKGREEN;
        }
        if (item.quality == GameConstants.QUALITY_RARE) {
            font = DarkwoodGraphics.FONT_ARIAL10_LIGHTBLUE;
        }
        g.drawText(item.description, 9, 102, font);
        g.drawText(Local.get("character.level") + ":" + item.getLevel() + "", 100, 102, DarkwoodGraphics.FONT_ARIAL10_BLACK);

        if (focus == 0) {
            // sell value is cost / 3
            g.drawText(Local.get("shop.value") + ":" + item.getSellValue() + "", 100, 112, DarkwoodGraphics.FONT_ARIAL10_BLACK);
        } else if (focus == 1) {
            g.drawText(Local.get("shop.cost") + ":" + item.value + "", 100, 112, DarkwoodGraphics.FONT_ARIAL10_BLACK);
        }

        // draw stats and dmg/def
        g.drawText(item.getItemInfo(), 9, 112, DarkwoodGraphics.FONT_ARIAL10_BLACK);

    }

    private void drawWelcomeText(DarkwoodGraphics g) {
        String text = ((ShopRoom) game.player.room).getWelcomeText();

        g.drawText(text, 9, 102,
                availableWidth, availableWidth, 0, Graphics.LEFT, DarkwoodGraphics.FONT_ARIAL10_BLACK);

    }

    /**
     * Draw player equipment slots to the shop UI, return the currently selected item
     * @param g
     * @return currently selected item
     */
    private Equipment drawSlots(DarkwoodGraphics g) {
        Equipment equ = null;

        for (int i = 0; i < 4; i++) {
            if (game.player.equipmentSlots[i] != null) {
                Image slotImg = utils.getImage(game.player.equipmentSlots[i].image);
                g.setColor(125, 125, 125);
                g.fillRect(slotX[i], slotY[i], slotImg.getWidth(), slotImg.getHeight());
                g.setColor(175, 175, 175);
                g.drawRect(slotX[i], slotY[i], slotImg.getWidth(), slotImg.getHeight());
                g.drawImage(slotImg, slotX[i], slotY[i], 0);
                if (slotIndex == i) {
                    equ = game.player.equipmentSlots[i];
                }
            }

        }

        return equ;
    }

    public void move(Room directionRoom) {

        // reset welcome text (to be shown again when player returns)
        showWelcomeText = true;

        // reset selections
        focus = 0;
        slotIndex = -1;
        shopInventoryIndex = 0;

        if (directionRoom == null) {
            return;
        }

        Game.player.moveTo(directionRoom.getId(), directionRoom.getZone());

    }

    public void checkInput(int keyState) {

        // when player pressed any key, no longer show welcome text
        if (keyState != 0 && showWelcomeText) {
            showWelcomeText = false;
        }

        if (keyState == DarkwoodCanvas.DOWN_PRESSED) {
            if (focus == 0) {
                slotIndex++;
                if (slotIndex >= slotX.length) {
                    slotIndex = 0;
                }
                if (game.player.equipmentSlots[slotIndex] != null) {
                    rightSoftKeyText = Local.get("buttons.sell");
                } else {
                    rightSoftKeyText = "";
                }
            } else {
                // here check if the list has reached the bottom (list starts at -1, hence size-2)
                if (vendorInvendory.size() - 2 > shopInventoryIndex) {
                    shopInventoryIndex++;
                }

                // move player slot indicator to point the slot of the item selected, if selected item is not a wearable equipment, point to nothing
                if (vendorInvendory.elementAt(shopInventoryIndex + 1) instanceof Equipment) {
                    slotIndex = ((Equipment) vendorInvendory.elementAt(shopInventoryIndex + 1)).getSlot();
                } else {
                    slotIndex = -1;
                }

            }


        } else if ((keyState == DarkwoodCanvas.UP_PRESSED)) {
            if (focus == 0) {
                slotIndex--;
                if (slotIndex < 0) {
                    slotIndex = slotX.length - 1;
                }
                if (game.player.equipmentSlots[slotIndex] != null) {
                    rightSoftKeyText = Local.get("buttons.sell");
                } else {
                    rightSoftKeyText = "";
                }

            } else {
                if (shopInventoryIndex > -1) {
                    shopInventoryIndex--;
                }

                // move player slot indicator to point the slot of the item selected, if selected item is not a wearable equipment, point to nothing
                if (vendorInvendory.elementAt(shopInventoryIndex + 1) instanceof Equipment) {
                    slotIndex = ((Equipment) vendorInvendory.elementAt(shopInventoryIndex + 1)).getSlot();
                } else {
                    slotIndex = -1;
                }
            }


        } else if ((keyState == DarkwoodCanvas.RIGHT_PRESSED)) {
            focus = 1;
            if (vendorInvendory.elementAt(shopInventoryIndex + 1) instanceof Equipment) {
                slotIndex = ((Equipment) vendorInvendory.elementAt(shopInventoryIndex + 1)).getSlot();
            } else {
                slotIndex = -1;
            }
            rightSoftKeyText = Local.get("buttons.buy");

        } else if ((keyState == DarkwoodCanvas.LEFT_PRESSED)) {
            focus = 0;

            if (slotIndex < 0) {
                slotIndex = 0;
            }
            if (slotIndex >= GameConstants.NUMBER_OF_SLOTS) {
                slotIndex = GameConstants.NUMBER_OF_SLOTS - 1;
            }

            if (game.player.equipmentSlots[slotIndex] != null) {
                rightSoftKeyText = Local.get("buttons.sell");
            } else {
                rightSoftKeyText = "";
            }

        } else if ((keyState == DarkwoodCanvas.GAME_A_PRESSED)) {
        } else if ((keyState == DarkwoodCanvas.SOFTKEY1_PRESSED)) {
            if (game.player.room.getEast() != null) {
                move(game.player.room.getEast());
            } else if (game.player.room.getWest() != null) {
                move(game.player.room.getWest());
            } else if (game.player.room.getSouth() != null) {
                move(game.player.room.getSouth());
            } else if (game.player.room.getNorth() != null) {
                move(game.player.room.getNorth());
            }
        } else if ((keyState == DarkwoodCanvas.GAME_C_PRESSED)) {
        } else if ((keyState == DarkwoodCanvas.SOFTKEY2_PRESSED)) {
            if (focus == 1) {

                Item itemBeingBought = (Item) vendorInvendory.elementAt(shopInventoryIndex + 1);

                if (Game.player.money < itemBeingBought.value) {
                    // if player hasnt got enough money, do nothing
                    return;
                }
                if (itemBeingBought.getLevel() - GameConstants.EQUIPMENT_LEVEL_REQUIREMENT_GAP > Game.player.level) {
                    // level requirement is not satisfied
                    return;
                }

                if (itemBeingBought instanceof Potion) {
                    // item is a potion, add potions on player
                    Game.player.healingPotions += 1;
                    // substract money from player
                    Game.player.money -= itemBeingBought.value;

                } else if (itemBeingBought instanceof Equipment) {
                    // item is a piece of equipment, equip it on player of slot is free

                    Equipment tmpeq = (Equipment) vendorInvendory.elementAt(shopInventoryIndex + 1);


                    // cannot buy unless slot is empty
                    if (Game.player.equipmentSlots[tmpeq.getSlot()] == null) {

                        // equip the item
                        Game.player.equip(tmpeq);

                        // move selected index to the previous item
                        if (shopInventoryIndex > -1) {
                            shopInventoryIndex--;
                        }

                        // substract money from player
                        Game.player.money -= itemBeingBought.value;

                    }


                }

            } else {
                if (game.player.equipmentSlots[slotIndex] != null) {
                    // sell value is cost / 3;
                    game.player.money += game.player.equipmentSlots[slotIndex].getSellValue();
                    game.player.unequip(game.player.equipmentSlots[slotIndex]);
                }
            }

        }
    }

    // Keep it simple in case the user is clumsy with the touchpad ++Ville
    public void pointerReleasedEvent(int x, int y) {
        if (onRight) {
            if (x < 70) {
                checkInput(GameCanvas.LEFT_PRESSED);
                onRight = false;
            } else {
                if (y < 60) {
                    checkInput(GameCanvas.UP_PRESSED);
                } else if (y > 60 && y < 170) {
                    checkInput(GameCanvas.DOWN_PRESSED);
                }
            }
        } else { // on left
            if (x > 70) {
                checkInput(GameCanvas.RIGHT_PRESSED);
                onRight = true;
            } else {
                if (y < 60) {
                    checkInput(GameCanvas.UP_PRESSED);
                } else if (y > 60 && y < 170) {
                    checkInput(GameCanvas.DOWN_PRESSED);
                }
            }

        }
    }
     public void pointerPressedEvent(int x, int y) {

    }
}
