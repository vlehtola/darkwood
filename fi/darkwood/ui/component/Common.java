/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ui.component;

import fi.darkwood.Creature;
import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.Game;
import fi.darkwood.GameConstants;
import fi.darkwood.Humanoid;
import fi.darkwood.equipment.ChestArmour;
import fi.darkwood.equipment.Helmet;
import fi.darkwood.equipment.Shield;
import fi.darkwood.equipment.Weapon;
import javax.microedition.lcdui.Image;
import fi.darkwood.Player;
import fi.darkwood.util.Utils;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;
import fi.darkwood.rule.MeleeRule;
import fi.mirake.Local;

/**
 * Contains static methods used by multiple UI views
 * @author Teemu
 */
public class Common {

    /**
     * Used to distinguish abilities by bits
     */
    public final static int[] ACTIVE_ABILITY_FLAGS = {1, 2, 4, 8};
    private static Image img;
    static int skipCounter = 0;    // used to determine flash interval
    static int SKIP = 1;
    static int MAX_SKIP = 7;

    /**
     * Draw player paper doll equipment.
     * 
     * 
     * @param player
     * @param frame
     * @param g
     * @param xCoord
     * @param yCoord
     * @param utils
     */
    public static void drawEquipmentPaperDoll(Player player, int frame, DarkwoodGraphics g, int xCoord, int yCoord, Utils utils) {
        // dont flash any slot
        drawEquipmentPaperDoll(player, frame, g, xCoord, yCoord, utils, -1);

    }

    /**
     * Draw equipment on the player paperdoll. Flash the slot skipSlot
     * (in shop for example to indicate which item is selected)
     * 
     * @param player
     * @param frame
     * @param g
     * @param xCoord
     * @param yCoord
     * @param utils
     * @param skipSlot
     */
    public static void drawEquipmentPaperDoll(Player player, int frame, DarkwoodGraphics g, int xCoord, int yCoord, Utils utils, int flashSlot) {
        frame = frame + player.frameOffset;
        if (player.characterClass == Game.CLASS_WARRIOR) {

            // draw chest armor, unless we are currently flashing it
            if (player.equipmentSlots[GameConstants.SLOT_CHEST] != null && ((flashSlot == GameConstants.SLOT_CHEST && skipCounter > SKIP) || (flashSlot != GameConstants.SLOT_CHEST))) {
                ChestArmour chest = (ChestArmour) player.equipmentSlots[GameConstants.SLOT_CHEST];
                if (!chest.getPaperdollImage().equals("")) {
                    img = utils.getImage(chest.getPaperdollImage());
                    g.drawImage(img, xCoord - 22 + ChestArmour.ANIMATION_X_WARRIOR[frame],
                            yCoord + 7 + ChestArmour.ANIMATION_Y_WARRIOR[frame], Graphics.BOTTOM | Graphics.LEFT);
                }
            }
            if (player.equipmentSlots[GameConstants.SLOT_HEAD] != null && ((flashSlot == GameConstants.SLOT_HEAD && skipCounter > SKIP) || (flashSlot != GameConstants.SLOT_HEAD))) {
                Helmet helmet = (Helmet) player.equipmentSlots[GameConstants.SLOT_HEAD];
                if (!helmet.getPaperdollImage().equals("")) {
                    img = utils.getImage(helmet.getPaperdollImage());
                    g.drawImage(img, xCoord - 22 + Helmet.ANIMATION_X_WARRIOR[frame],
                            yCoord + 7 + Helmet.ANIMATION_Y_WARRIOR[frame], Graphics.BOTTOM | Graphics.LEFT);
                }
            }
            if (player.equipmentSlots[GameConstants.SLOT_RIGHT_HAND] != null && ((flashSlot == GameConstants.SLOT_RIGHT_HAND && skipCounter > SKIP) || (flashSlot != GameConstants.SLOT_RIGHT_HAND))) {
                Weapon weapon = (Weapon) player.equipmentSlots[GameConstants.SLOT_RIGHT_HAND];
                if (!weapon.getPaperdollImage().equals("")) {
                    img = utils.getImage(weapon.getPaperdollImage());
                    g.drawImage(img, xCoord - 22 + Weapon.ANIMATION_X_WARRIOR[frame],
                            yCoord + 7 + Weapon.ANIMATION_Y_WARRIOR[frame], Graphics.BOTTOM | Graphics.LEFT);
                }
            }
            if (player.equipmentSlots[GameConstants.SLOT_LEFT_HAND] != null && ((flashSlot == GameConstants.SLOT_LEFT_HAND && skipCounter > SKIP) || (flashSlot != GameConstants.SLOT_LEFT_HAND))) {
                Shield shield = (Shield) player.equipmentSlots[GameConstants.SLOT_LEFT_HAND];
                if (!shield.getPaperdollImage().equals("")) {
                    img = utils.getImage(shield.getPaperdollImage());
                    g.drawImage(img, xCoord - 22 + Shield.ANIMATION_X_WARRIOR[frame],
                            yCoord + 7 + Shield.ANIMATION_Y_WARRIOR[frame], Graphics.BOTTOM | Graphics.LEFT);
                }
            }



        } else if (player.characterClass == Game.CLASS_CLERIC) {

            if (player.equipmentSlots[GameConstants.SLOT_CHEST] != null && ((flashSlot == GameConstants.SLOT_CHEST && skipCounter > SKIP) || (flashSlot != GameConstants.SLOT_CHEST))) {
                ChestArmour chest = (ChestArmour) player.equipmentSlots[GameConstants.SLOT_CHEST];
                if (!chest.getPaperdollImage().equals("")) {
                    img = utils.getImage(chest.getPaperdollImage());
                    g.drawImage(img, xCoord - 27 + ChestArmour.ANIMATION_X_CLERIC[frame],
                            yCoord + 8 + ChestArmour.ANIMATION_Y_CLERIC[frame], Graphics.BOTTOM | Graphics.LEFT);
                }
            }
            if (player.equipmentSlots[GameConstants.SLOT_HEAD] != null && ((flashSlot == GameConstants.SLOT_HEAD && skipCounter > SKIP) || (flashSlot != GameConstants.SLOT_HEAD))) {
                Helmet helmet = (Helmet) player.equipmentSlots[GameConstants.SLOT_HEAD];
                if (!helmet.getPaperdollImage().equals("")) {
                    img = utils.getImage(helmet.getPaperdollImage());
                    g.drawImage(img, xCoord - 27 + Helmet.ANIMATION_X_CLERIC[frame],
                            yCoord + 8 + Helmet.ANIMATION_Y_CLERIC[frame], Graphics.BOTTOM | Graphics.LEFT);
                }
            }
            if (player.equipmentSlots[GameConstants.SLOT_RIGHT_HAND] != null && ((flashSlot == GameConstants.SLOT_RIGHT_HAND && skipCounter > SKIP) || (flashSlot != GameConstants.SLOT_RIGHT_HAND))) {
                Weapon weapon = (Weapon) player.equipmentSlots[GameConstants.SLOT_RIGHT_HAND];
                if (!weapon.getPaperdollImage().equals("")) {
                    img = utils.getImage(weapon.getPaperdollImage());
                    g.drawImage(img, xCoord - 27 + Weapon.ANIMATION_X_CLERIC[frame],
                            yCoord + 8 + Weapon.ANIMATION_Y_CLERIC[frame], Graphics.BOTTOM | Graphics.LEFT);
                }
            }
            if (player.equipmentSlots[GameConstants.SLOT_LEFT_HAND] != null && ((flashSlot == GameConstants.SLOT_LEFT_HAND && skipCounter > SKIP) || (flashSlot != GameConstants.SLOT_LEFT_HAND))) {
                Shield shield = (Shield) player.equipmentSlots[GameConstants.SLOT_LEFT_HAND];
                if (!shield.getPaperdollImage().equals("")) {
                    img = utils.getImage(shield.getPaperdollImage());
                    g.drawImage(img, xCoord - 27 + Shield.ANIMATION_X_CLERIC[frame],
                            yCoord + 11 + Shield.ANIMATION_Y_CLERIC[frame], Graphics.BOTTOM | Graphics.LEFT);
                }
            }




        } else if (player.characterClass == Game.CLASS_MAGE) {

            if (player.equipmentSlots[GameConstants.SLOT_CHEST] != null && ((flashSlot == GameConstants.SLOT_CHEST && skipCounter > SKIP) || (flashSlot != GameConstants.SLOT_CHEST))) {
                ChestArmour chest = (ChestArmour) player.equipmentSlots[GameConstants.SLOT_CHEST];
                if (!chest.getPaperdollImage().equals("")) {
                    img = utils.getImage(chest.getPaperdollImage());
                    g.drawImage(img, xCoord - 24 + ChestArmour.ANIMATION_X_MAGE[frame],
                            yCoord + 6 + ChestArmour.ANIMATION_Y_MAGE[frame], Graphics.BOTTOM | Graphics.LEFT);
                }
            }
            if (player.equipmentSlots[GameConstants.SLOT_HEAD] != null && ((flashSlot == GameConstants.SLOT_HEAD && skipCounter > SKIP) || (flashSlot != GameConstants.SLOT_HEAD))) {
                Helmet helmet = (Helmet) player.equipmentSlots[GameConstants.SLOT_HEAD];
                if (!helmet.getPaperdollImage().equals("")) {
                    img = utils.getImage(helmet.getPaperdollImage());
                    g.drawImage(img, xCoord - 24 + Helmet.ANIMATION_X_MAGE[frame],
                            yCoord + 6 + Helmet.ANIMATION_Y_MAGE[frame], Graphics.BOTTOM | Graphics.LEFT);
                }
            }
            if (player.equipmentSlots[GameConstants.SLOT_RIGHT_HAND] != null && ((flashSlot == GameConstants.SLOT_RIGHT_HAND && skipCounter > SKIP) || (flashSlot != GameConstants.SLOT_RIGHT_HAND))) {
                Weapon weapon = (Weapon) player.equipmentSlots[GameConstants.SLOT_RIGHT_HAND];
                if (!weapon.getPaperdollImage().equals("")) {
                    img = utils.getImage(weapon.getPaperdollImage());
                    g.drawImage(img, xCoord - 24 + Weapon.ANIMATION_X_MAGE[frame],
                            yCoord + 6 + Weapon.ANIMATION_Y_MAGE[frame], Graphics.BOTTOM | Graphics.LEFT);
                }
            }
            if (player.equipmentSlots[GameConstants.SLOT_LEFT_HAND] != null && ((flashSlot == GameConstants.SLOT_LEFT_HAND && skipCounter > SKIP) || (flashSlot != GameConstants.SLOT_LEFT_HAND))) {
                Shield shield = (Shield) player.equipmentSlots[GameConstants.SLOT_LEFT_HAND];
                if (!shield.getPaperdollImage().equals("")) {
                    img = utils.getImage(shield.getPaperdollImage());
                    g.drawImage(img, xCoord - 24 + Shield.ANIMATION_X_MAGE[frame],
                            yCoord + 6 + Shield.ANIMATION_Y_MAGE[frame], Graphics.BOTTOM | Graphics.LEFT);
                }
            }



        }

        skipCounter++;

        if (skipCounter > MAX_SKIP) {
            skipCounter = 0;

        }

    }

    /**
     * draw player status in the game frames
     * hp, abilityLevel etc in upper frame, abilities and cooldowns in lower
     * 
     * @param g
     */
    public static void drawStatus(DarkwoodGraphics g, Player player) {

        g.drawText(Game.player.name + "", 1, 3, 120, 120, 0, Graphics.LEFT, DarkwoodGraphics.FONT_ANTIQUA10_WHITE_BOLD);
        //        g.drawText("s:" + player.strength + " d:" + player.dexterity + " c:" + player.constitution + " w:" + player.willpower, 1, 3, 120, 120, 0, Graphics.LEFT, DarkwoodGraphics.FONT_ANTIQUA10_WHITE_BOLD);
        g.drawText(player.playerClassString(), 144, 1, 76, 76, 0, Graphics.HCENTER, DarkwoodGraphics.FONT_ANTIQUA10_WHITE_BOLD);
        g.drawText(Local.get("character.level") + ":" + player.level, 2, 18, 76, 76, 0, 0, DarkwoodGraphics.FONT_ANTIQUA10_WHITE_BOLD);

        img = Utils.getInstance().getImage("/images/ui/heart_icon.png");
        g.drawImage(img, 0, 167, 0);
        g.drawText(player.health + "/" + player.maxHealth, 13, 167, 100, 100, 0, 0, DarkwoodGraphics.FONT_ARIAL10_GREEN);
        //g.drawText("HP:" + player.health + "/" + player.maxHealth, 0, 165, 76, 76, 0, Graphics.LEFT);

        img = Utils.getInstance().getImage("/images/ui/mana_icon.png");
        g.drawImage(img, 0, 179, 0);
        g.drawText(player.mana + "/" + player.maxMana, 13, 180, 100, 100, 0, 0, DarkwoodGraphics.FONT_ARIAL10_LIGHTBLUE);
        //g.drawText("MP:" + player.mana + "/" + player.maxMana, 0, 177, 76, 76, 0, Graphics.LEFT);

        // pullo
        img = Utils.getInstance().getImage("/images/ui/potion.png");
        g.drawImage(img, 166, 168, Graphics.TOP | Graphics.LEFT);
        g.drawText(player.healingPotions + " x ", 164, 170, 60, 60, 0, Graphics.RIGHT);

        //raha
        img = Utils.getInstance().getImage("/images/ui/goldcoin.png");
        g.drawImage(img, 165, 182, Graphics.TOP | Graphics.LEFT);
        g.drawText(player.money + " x ", 164, 182, 60, 60, 0, Graphics.RIGHT);


    }

    /**
     * Draw stats bar, used in shop and trainer screens
     * @param g
     */
    public static void drawStatsbar(DarkwoodGraphics g) {
        int yLoc = 136;

        Image img = Utils.getInstance().getImage("/images/ui/stats_bar_mod.png");
        g.drawImage(img, 1, yLoc, 0);

        g.drawText(Game.player.strength + "", 41, yLoc + 2, 12, 12, 0, Graphics.RIGHT, g.FONT_ARIAL10);
        g.drawText(Game.player.dexterity + "", 85, yLoc + 2, 12, 12, 0, Graphics.RIGHT, g.FONT_ARIAL10);
        g.drawText(Game.player.constitution + "", 129, yLoc + 2, 12, 12, 0, Graphics.RIGHT, g.FONT_ARIAL10);
        g.drawText(Game.player.willpower + "", 168, yLoc + 2, 12, 12, 0, Graphics.RIGHT, g.FONT_ARIAL10);
        /*
        g.drawText(Game.player.defence + "", 65, yLoc + 16, 12, 12, 0, Graphics.RIGHT, g.FONT_ARIAL10);

        g.drawText(Game.player.defence + "", 139, yLoc + 16, 12, 12, 0, Graphics.RIGHT, g.FONT_ARIAL10);
        g.drawText(Game.player.defence + "", 160, yLoc + 16, 12, 12, 0, Graphics.RIGHT, g.FONT_ARIAL10);
         * */
        g.drawText(Game.player.defence + "", 56, yLoc + 16, 12, 12, 0, Graphics.RIGHT, g.FONT_ARIAL10);


        // Weapon damaget: (xx - yy) + strenamodikka ++Ville
        int minDmg = 1, maxDmg = 1;
        int strmod = (int) (MeleeRule.getStrengthDamageModifier((Creature) Game.player) * 10);
        Weapon weapon = (Weapon) ((Humanoid) Game.player).equipmentSlots[GameConstants.SLOT_RIGHT_HAND];
        if (weapon != null) {
            minDmg = weapon.minDamage;
            maxDmg = weapon.maxDamage;
        }
        g.drawText(minDmg + "", 122, yLoc + 16, 12, 12, 0, Graphics.RIGHT, g.FONT_ARIAL10);
        g.drawText(maxDmg + "", 143, yLoc + 16, 12, 12, 0, Graphics.RIGHT, g.FONT_ARIAL10);

        g.drawText("+" + (double) strmod / 10.0 + "", 167, yLoc + 16, 12, 12, 0, Graphics.RIGHT, g.FONT_ARIAL10);

    }
    private final static int[] ABILITY_X = {74, 59, 74, 93};
    private final static int[] ABILITY_Y = {158, 174, 192, 174};
    private final static int[] ABILITY_X_SCREEN = {67, 17, 67, 115};
    private final static int[] ABILITY_Y_SCREEN = {38, 70, 106, 70};

    public static void drawAbilityIcons(DarkwoodGraphics g, Player player, Utils utils, int abilityIconsPressed, boolean big) {
        for (int i = 0; i < 4; i++) {
            int x, y;
            if (big) {
                x = ABILITY_X_SCREEN[i];
                y = ABILITY_Y_SCREEN[i];
            } else {
                x = ABILITY_X[i];
                y = ABILITY_Y[i];
            }
            if (player.activeAbilities[i] != null) {

                if ((abilityIconsPressed & ACTIVE_ABILITY_FLAGS[i]) != 0) {
                    img = utils.getImage(player.activeAbilities[i].getImage(true, big));
                } else {
                    img = utils.getImage(player.activeAbilities[i].getImage(false, big));
                }
                g.drawImage(img, x, y, 0);

                if (player.abilityCooldowns[i] > 0 && !big) {
                    g.drawText("" + (player.abilityCooldowns[i] / 15 + 1), ABILITY_X[i] + 9, ABILITY_Y[i] + 4, 20, 20, 0, 0);
                }
            } else {
                if (big) {
                    img = utils.getImage("/images/ability/icons/empty_slot_big.png");
                } else {
                    img = utils.getImage("/images/ability/icons/empty_slot.png");
                }
                g.drawImage(img, x, y, 0);
            }
        }
    /*        if (player.activeAbilities[1] != null) {
    img = utils.getImage(player.activeAbilities[1].image);
    g.drawImage(img, 60, 174, 0);
    if (player.actionCoolDownRounds > 0) {
    g.drawString("" + player.actionCoolDownRounds, 68, 180, 0);
    }
    } */
    }
    /*
    public static Image makeTransparent(Image img, int width, int height) {
    int[] byteArray = new int[width * height];
    img.getRGB(byteArray, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
    //replace the color that you had put before for a transparent pixel
    for (int i = 0; i < byteArray.length; i++) {
    if (byteArray[i] == 0x123456) {
    byteArray[i] = 0x00000000;
    }
    }

    //and then create the image with a transparent background
    img = Image.createRGBImage(byteArray, width, height, true);
    return img;
    }

    public static void drawAbilityIconsOnScreen(DarkwoodGraphics g, Player player, Utils utils, int abilityIconsPressed) {
    //Graphics gr = g.getGraphics();
    //gr.setClip(10,20,20,30);
    //Specify the screen area to be copied
    int boxWidth = 114 - 60;
    int boxHeight = 207 - 157;
    Image tmp = Image.createImage(boxWidth, boxHeight);
    Graphics tmpGr = tmp.getGraphics();

    tmpGr.drawRegion(g.imageDisplayedOnScreen, 60, 157, boxWidth, boxHeight, Sprite.TRANS_NONE, 0, 0, Graphics.LEFT | Graphics.TOP);
    tmp = utils.resizeImage(tmp, boxWidth * 2, boxHeight * 2, boxWidth, boxHeight);

    int drawX = (g.imageDisplayedOnScreen.getWidth() - boxWidth * 2) / 2;
    int drawY = (g.imageDisplayedOnScreen.getHeight() - boxHeight * 2) / 3;

    g.drawImage(tmp, drawX, drawY, Graphics.LEFT | Graphics.TOP);
     * }
     */
    /*
    for (int i = 0; i < 4; i++) {
    if (player.activeAbilities[i] != null) {
    if ((abilityIconsPressed & ACTIVE_ABILITY_FLAGS[i]) != 0) {
    img = utils.getImage(player.activeAbilities[i].pressedImage);
    } else {
    img = utils.getImage(player.activeAbilities[i].image);
    }
    img = utils.resizeImage(img, 50, 50, 22, 22);
    img = makeTransparent(img, 50, 50);
    g.drawImage(img, ABILITY_X_SCREEN[i], ABILITY_Y_SCREEN[i], 0);

    if (player.abilityCooldowns[i] > 0) {
    g.drawText("" + (player.abilityCooldowns[i] / 15 + 1), ABILITY_X_SCREEN[i] + 9, ABILITY_Y_SCREEN[i] + 4, 20, 20, 0, 0);
    }
    } else {
    img = utils.getImage("/images/ability/icons/empty_slot.png");
    img = utils.resizeImage(img, 50, 50, 22, 22);
    img = makeTransparent(img, 50, 50);
    g.drawImage(img, ABILITY_X_SCREEN[i], ABILITY_Y_SCREEN[i], 0);
    }
    }
     */
    /*        if (player.activeAbilities[1] != null) {
    img = utils.getImage(player.activeAbilities[1].image);
    g.drawImage(img, 60, 174, 0);
    if (player.actionCoolDownRounds > 0) {
    g.drawString("" + player.actionCoolDownRounds, 68, 180, 0);
    }
    } */
    private static Sprite sprite;

    public static void drawPlayer(Player player, DarkwoodGraphics g, int x, int y) {
        sprite = player.getSprite();
        g.defineReferencePixel(sprite, 0, sprite.getHeight());
        g.setRefPixelPosition(sprite, x, y);
        sprite.paint(g.getGraphics());

        // check if ability animation is ending and need to return to "idle" animation sequence
        if (player.framesLeftToResetAnimation > 0) {
            player.framesLeftToResetAnimation -= 1;
            if (player.framesLeftToResetAnimation == 0) {
                player.setIdleAnimationFrameSequence();
            }
        }
    }
}
