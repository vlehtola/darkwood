/*
 * CharCreationView.java
 *
 * Created on June 28, 2007, 9:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ui.view;

import fi.darkwood.ui.forms.NamingForm;
import fi.darkwood.DarkwoodCanvas;
import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.Game;
import fi.darkwood.GameConstants;
import fi.darkwood.Player;
import fi.darkwood.network.CharacterSerializer;
import fi.darkwood.network.HttpClient;
import fi.darkwood.ui.component.Common;
import fi.darkwood.util.Properties;
import fi.darkwood.util.Utils;
import fi.mirake.Local;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import nanoxml.kXMLParseException;

/**
 *
 * @author Ville Lehtola
 */
public class TitleScreenView extends ViewAndControls {

    private final static Utils utils = Utils.getInstance();
    public int selectedItem = 0;
    public int state = -1;
    Game game;
    Vector characterMenuItems = new Vector(5);
    Vector mainMenuItems = new Vector(4);
    private final static Image titleimg = utils.getImage("/images/startup/background.png");
    public Player classDisplayed = Game.createNewCharacter(selectedItem, "Unnamed");
    private Form namingForm;
//    static private TextField characterName = new TextField("Character Name", "", 20, TextField.ANY);
    String characterName = "Unnamed";
    private Sprite selector;
    int count = 0;

    public TitleScreenView(Game game, int width, int height) {
        super(width, height);
        this.game = game;
        loadCharactersToMenu();
        rightSoftKeyText = Local.get("buttons.ok");
        leftSoftKeyText = "";

// init the selector animation for menu
        Image selectorImg = Utils.getInstance().getImage("/images/ui/selector.png");
        selector = new Sprite(selectorImg, 17, selectorImg.getHeight());
        int[] sequence = {0, 0, 1, 1, 2, 2, 3, 3};
        selector.setFrameSequence(sequence);


        mainMenuItems.insertElementAt(Local.get("mainmenu.play"), 0);
        mainMenuItems.insertElementAt(Local.get("mainmenu.help"), 1);
        mainMenuItems.insertElementAt(Local.get("mainmenu.about"), 2);
        mainMenuItems.insertElementAt(Local.get("mainmenu.exit"), 3);
//#if DARKWOOD_DEMOVERSION
//#         mainMenuItems.insertElementAt(Local.get("mainmenu.getfull"), 4);
//# 
//#endif


    }

    public void updateScreen(DarkwoodGraphics g) {
        String str = "";
        //Image img = null;


// press any key
        if (state == -1) {
            // title screen
            int availableWidth = 200;
            int padding = 0;

            int textOrientation = Graphics.HCENTER;

            g.drawImage(titleimg, 0, 0, 0);
            g.setColor(0xFFFFFF);
            if (count < 8) {
                g.drawText("- " + Local.get("mainmenu.pressanykey") + " -", GAME_WIDTH / 2, GAME_HEIGHT * 1 / 3 + 15,
                        availableWidth, availableWidth, padding, textOrientation, DarkwoodGraphics.FONT_TIMES12_BROWN);
            }
            g.drawText(Local.get("mainmenu.copyright"), GAME_WIDTH / 2, GAME_HEIGHT * 2 / 3 + 25,
                    availableWidth, availableWidth, padding, textOrientation);
            g.drawText(Local.get("mainmenu.allrights"), GAME_WIDTH / 2, GAME_HEIGHT * 2 / 3 + 35,
                    availableWidth, availableWidth, padding, textOrientation);

            count++;
            if (count == 15) {
                count = 0;
            }
        }



// main menu
        if (state == 0) {
            leftSoftKeyText = "";
            rightSoftKeyText = Local.get("buttons.ok");
            // title screen
            g.drawImage(titleimg, 0, 0, 0);

            Enumeration e = mainMenuItems.elements();
            int y = 35;
            int i = 0;
            while (e.hasMoreElements()) {
                String itemStr = "";
                //            String itemStr2 = "";
                Object next = e.nextElement();
                if (next instanceof String) {
                    // this is menu item for creating new character
                    itemStr = (String) next;
                    //                  itemStr2 = "Create new character";

                }

                // draw selector
                if (i == selectedItem) {
                    selector.setRefPixelPosition(30, y);
                    selector.paint(g.getGraphics());
                    selector.nextFrame();
                    //                    g.drawText(">", 15, y, DarkwoodGraphics.FONT_TIMES20_BROWN);
                }

                g.drawText(itemStr, 50, y, DarkwoodGraphics.FONT_TIMES20_BROWN);
//                g.drawText(itemStr2, 34, y + 16, DarkwoodGraphics.FONT_TIMES12_BROWN);

                // move cursor down
                y += 30;
                i += 1;
            }
        }

        if (state == 1) {
            leftSoftKeyText = Local.get("buttons.back");
            rightSoftKeyText = Local.get("buttons.ok");
            // title screen
            g.drawImage(titleimg, 0, 0, 0);

            Enumeration e = characterMenuItems.elements();
            int y = 35;
            int i = 0;
            while (e.hasMoreElements()) {
                String itemStr = "";
                String itemStr2 = "";
                Object next = e.nextElement();
                if (next instanceof String) {
                    // this is menu item for creating new character
                    itemStr = (String) next;
                    itemStr2 = Local.get("titlescreen.newchar");

                } else if (next instanceof Player) {
                    Player p = (Player) next;
                    itemStr = p.name;
                    itemStr2 = Local.get("character.level") + " " + p.level + " " + p.playerClassString();
                }

                // draw selector
                if (i == selectedItem) {
                    selector.setRefPixelPosition(30, y);
                    selector.paint(g.getGraphics());
                    selector.nextFrame();
                    //                    g.drawText(">", 15, y, DarkwoodGraphics.FONT_TIMES20_BROWN);
                }

                g.drawText(itemStr, 50, y, DarkwoodGraphics.FONT_TIMES20_BROWN);
                g.drawText(itemStr2, 50, y + 16, DarkwoodGraphics.FONT_TIMES12_BROWN);

                // move cursor down
                y += 30;
                i += 1;
            }
        }

        if (state == 2) {
            leftSoftKeyText = "";
            rightSoftKeyText = Local.get("buttons.ok");

            // Character class selection screen

            Image bgimg = utils.getImage("/images/ui/city_pergament_bg.png");
            // draw background
            g.drawImage(bgimg, 10, 30, 0);

            bgimg = utils.getImage("/images/ui/big_arrow_left.png");

            g.drawImage(bgimg, 29, 77, 0);

            bgimg = utils.getImage("/images/ui/big_arrow_right.png");

            g.drawImage(bgimg, 176 - 29 - bgimg.getWidth(), 77, 0);

            if (selectedItem == GameConstants.CLASS_WARRIOR) {
                str = Local.get("class.warrior.desc") + "\n(" + Local.get("class.available") + ")";
                //img = utils.getImage("/images/startup/warrior100.jpg");
            } else if (selectedItem == GameConstants.CLASS_MAGE) {
                //img = utils.getImage("/images/startup/mage100.jpg");
                str = Local.get("class.mage.desc") + "\n";

                if (checkMageUnlock()) {
                    str += "(" + Local.get("class.available") + ")";
                } else {
                    str += "(" + Local.get("class.locked") + ")";
                }

            } else if (selectedItem == GameConstants.CLASS_CLERIC) {
                //img = utils.getImage("/images/startup/mage100.jpg");
                str = Local.get("class.cleric.desc") + "\n(" + Local.get("class.available") + ")";
//                str = "Cleric - Defensive spellcaster\n(available)";

            }


            // Display the class paperdoll with initial equipments ++Ville
            int xCoord = 70, yCoord = 115;
            Common.drawPlayer(classDisplayed, g, xCoord, yCoord);

            // draw the chest paper doll on the player
            Common.drawEquipmentPaperDoll(classDisplayed, classDisplayed.getSprite().getFrame(), g, xCoord, yCoord, utils);


            classDisplayed.getSprite().nextFrame();

            // char creation class choose

//            String name = (String) Properties.getApplicationProperties().get("Character-Name");

            //g.drawImage(img, 176 / 2 - img.getWidth() / 2, 208 / 2 - img.getHeight() / 2, 0);

            g.drawText(Local.get("character.name") + ": " + characterName, 176 / 2 - 70, 5, DarkwoodGraphics.FONT_ANTIQUA10_WHITE_BOLD);
            g.drawText(Local.get("character.choose.class"), 176 / 2 - 70, 15, DarkwoodGraphics.FONT_ANTIQUA10_WHITE_BOLD);
            g.drawText(str, 176 / 2 - 70, 155, DarkwoodGraphics.FONT_ANTIQUA10_WHITE_BOLD); // Draws the class name string

        }
// tutorial text
        if (state == 3) {
            leftSoftKeyText = "";
            rightSoftKeyText = Local.get("buttons.begin");
            Image img = utils.getImage("/images/ui/city_pergament_bg.png");
            g.drawImage(img, 10, 30, 0);
            //   String welcomeText = "Hailing from the wild lands in south, you have entered a small village surrounded by a deep forest. The village is a part of Border Kingdoms, ruled by a beautiful queen. Fame and fortune awaits! Adventurer, welcome to Darkwood!\n\nHint: Use ARROW keys to navigate.";
            String welcomeText = Local.get("character.start.text");
            welcomeText += "\n\n" + Local.get("character.start.text.hint");
            g.drawText(welcomeText, 15, 33, 152, 152, 0, 0, DarkwoodGraphics.FONT_ARIAL10_BLACK);
        }


        if (state == 4) {
            leftSoftKeyText = Local.get("buttons.back");
            rightSoftKeyText = "";
            Image img = utils.getImage("/images/ui/city_pergament_bg.png");
            g.drawImage(img, 10, 30, 0);
            //String welcomeText = "DARKWOOD TIPS\n\n" + "Use direction keys and softkeys to play the game.\n\n" + "Remember to talk to the characters in towns. " + "If you at first can't complete a combat area, try buying better equipment and leveling up.\n\n";
            String welcomeText = Local.get("mainmenu.help.text");
            welcomeText = "\n\n" + Local.get("mainmenu.help.text2") + "\n\n" + Local.get("mainmenu.help.text3");
            g.drawText(welcomeText, 15, 33, 152, 152, 0, 0, DarkwoodGraphics.FONT_ARIAL10_BLACK);
        }

        if (state == 5) {
            leftSoftKeyText = Local.get("buttons.back");
            rightSoftKeyText = "";
            Image img = utils.getImage("/images/ui/city_pergament_bg.png");
            g.drawImage(img, 10, 30, 0);
//            String welcomeText = "Darkwood\n\nCopyright 2010 by Mirake Ltd. A game developed and published by Mirake Ltd. "
//                    + "All rights reserved.\nwww.mirake.com";
            String version = Game.gameMidlet.getAppProperty("MIDlet-Version");
//#if DARKWOOD_DEMOVERSION
//#             version = version + " DEMO";
//# 
//#endif

            String welcomeText = "DARKWOOD\n(c) 2010 Mirake\n" + Local.get("mainmenu.about.version") + ": " + version + "\n\n" + "CREDITS\n" + "Lead programmer: Teemu Kivimaki\n" + "Lead designer: Ville Lehtola\n" + "Programmer: Esko Vaarasmaki\n" + "Artist: Ognjen Popovic";

            g.drawText(welcomeText, 15, 34, 152, 152, 0, 0, DarkwoodGraphics.FONT_ARIAL10_BLACK);
        }

        Image img = utils.getImage("/images/ui/frames_buttons.png");
        g.drawImage(img, 0, 0, 0);
        drawSoftKeyTexts(g);

    }

    public void checkInput(int keyState) {
        if (state == -1) {
            if (keyState != 0) {
                // user pressed any key, move to menu
                state = 0;
                // load characters to menu
                loadCharactersToMenu();
                return;
            }
        }

        if (state == 0) {
            if (keyState == GameCanvas.DOWN_PRESSED) {
                selectedItem++;
                if (selectedItem > mainMenuItems.size() - 1) {
                    selectedItem = 0;
                }
            } else if (keyState == GameCanvas.UP_PRESSED) {
                selectedItem--;
                if (selectedItem < 0) {
                    selectedItem = mainMenuItems.size() - 1;
                }
            } else if (keyState == DarkwoodCanvas.SOFTKEY2_PRESSED || keyState == GameCanvas.FIRE_PRESSED) {


                if (selectedItem == 0) {
                    loadCharactersToMenu();

                    // change state to character class selection
                    state = 1;
                    selectedItem = 0; // reset selection marker
                }
                if (selectedItem == 1) {
                    // change state to character class selection
                    state = 4;
                }
                if (selectedItem == 2) {
                    // change state to About
                    state = 5;
                }
                if (selectedItem == 3) {

                    Game.darkwoodCanvas.quitGame();
                    Game.gameMidlet.notifyDestroyed();

                }

                if (selectedItem == 4) {
                    try {
                        String url = "http://store.ovi.com/content/60412";
                        try {
                            url = Game.gameMidlet.getAppProperty("full_game_url");
                        } catch (NullPointerException ex) {
                            // if property doesnt exist, just use default above..
                        }
                        System.out.println("Opening browser to: " + url);
                        Game.gameMidlet.platformRequest(url);
                    } catch (ConnectionNotFoundException e) {
                        // full version url is not correct... just ignore
                        System.out.println("Error: Get full game version browser opening failed..");
                    }

                }

            }

            return;

        }
        // state 1 is character select
        if (state == 1) {
            if (keyState == GameCanvas.DOWN_PRESSED) {
                selectedItem++;
                if (selectedItem > characterMenuItems.size() - 1) {
                    selectedItem = 0;
                }
            } else if (keyState == GameCanvas.UP_PRESSED) {
                selectedItem--;
                if (selectedItem < 0) {
                    selectedItem = characterMenuItems.size() - 1;
                }
            } else if (keyState == DarkwoodCanvas.SOFTKEY2_PRESSED || keyState == GameCanvas.FIRE_PRESSED) {

                if (characterMenuItems.elementAt(selectedItem) instanceof Player) {
                    // player selected an existing character
                    Player player = (Player) characterMenuItems.elementAt(selectedItem);

                    // set the slot to which to save this character (same as the load slot)
                    Game.characterSaveSlot = selectedItem;

                    Game.gameBegin(player);
                } else {
                    // player selected to create new character

                    // set the character save slot to be used
                    Game.characterSaveSlot = selectedItem;

                    // change state to character class selection
                    state = 2;
                    selectedItem = 0; // reset selection marker

                    // switch to naming form
                    namingForm = new NamingForm(this, Display.getDisplay(Game.gameMidlet));
                    Display.getDisplay(Game.gameMidlet).setCurrent(namingForm);


                }
            } else if (keyState == DarkwoodCanvas.SOFTKEY1_PRESSED) {
                selectedItem = 0;
                state = 0;
            }
            return;

        }
        // state 2 is character class selection mode
        if (state == 2) {
            // Creating a new character
            int numberOfClasses = GameConstants.NUMBER_OF_CLASSES;
            if ((keyState == GameCanvas.UP_PRESSED) | (keyState == GameCanvas.RIGHT_PRESSED)) {
                selectedItem++;
                if (selectedItem > numberOfClasses - 1) {
                    selectedItem = 0;
                }
                classDisplayed = Game.createNewCharacter(selectedItem, characterName);
            } else if ((keyState == GameCanvas.DOWN_PRESSED) | (keyState == GameCanvas.LEFT_PRESSED)) {
                selectedItem--;
                if (selectedItem < 0) {
                    selectedItem = numberOfClasses - 1;
                }
                classDisplayed = Game.createNewCharacter(selectedItem, characterName);
            } else if ((keyState == DarkwoodCanvas.SOFTKEY2_PRESSED || keyState == GameCanvas.FIRE_PRESSED) && (selectedItem >= 0)) {

                // if mage class selected, we must check if it's unlocked or not
                if (selectedItem == GameConstants.CLASS_MAGE) {
                    if (checkMageUnlock()) { // check if mage class is unlocked
                        // goto tutorial text mode
                        state = 3;

                    } else {
                        // do nothing
                    }
                } else { // for other classes
                    // goto tutorial text mode
                    state = 3;
                }
            }
            return;
        }
        // state 3 is tutorial text mode
        if ((state == 3) && (keyState == DarkwoodCanvas.SOFTKEY2_PRESSED || keyState == GameCanvas.FIRE_PRESSED)) {
            int charClass = selectedItem;

            Player player = Game.createNewCharacter(charClass, characterName);
            Game.gameBegin(player);
            HttpClient.saveCharacter(player); // This (on success) sets the new character global id
            System.out.println("CHARACTER CREATION ID: " + player.getId());
        }

        if ((state == 4) && (keyState == DarkwoodCanvas.SOFTKEY1_PRESSED || keyState == GameCanvas.FIRE_PRESSED)) {
            state = 0;
        }
        if ((state == 5) && (keyState == DarkwoodCanvas.SOFTKEY1_PRESSED || keyState == GameCanvas.FIRE_PRESSED)) {
            state = 0;
        }
    }

    public void pointerReleased(int x, int y) {
    }

    private void loadCharactersToMenu() {

        //menuItems.addElement("create new character");

//#if DARKWOOD_OFFLINE
//#         characterMenuItems = CharacterSerializer.loadCharactersToMenuOffline();
//#else
        String xmlString = (String) Properties.getApplicationProperties().get("characters-xml");
        //LocalDatabase.loadCharacterXML();

        try {
            if (xmlString != null && xmlString.length() > 0) {
                characterMenuItems = CharacterSerializer.loadCharactersToMenu(xmlString);
            }
        } catch (kXMLParseException xmlException) {
            throw xmlException;
        }

        // maximum of 5 characters - if account has 5 chars, then dont add option to create new
        if (characterMenuItems.size() <= 4) {
            characterMenuItems.insertElementAt("Create", 0);
        }
//#endif




    }

    public void setCharacterName(String name) {
        characterName = name;


    }

    /**
     * Check if mage class is available for selection for new char.
     * Requires atleast one level 30 char on account.
     * @return true if unlocked
     */
    private boolean checkMageUnlock() {

        // get all chars on account (that have been loaded to start menu)
        Enumeration e = characterMenuItems.elements();

        // loop through the chars


        while (e.hasMoreElements()) {
            Object next = e.nextElement();



            if (next instanceof Player) {
                Player p = (Player) next;


                if (p.level >= 30) {
                    // if a character is level 30 or more, mage class is unlocked
                    return true;


                }
            }
        }
        // none of the chars were level 30, return false
        return false;

    }

    // handle touch phone controls
    // Keep it simple in case the user is clumsy with the touchpad ++Ville
    public void pointerReleasedEvent(int x, int y) {
        // press any key state
        if (state == -1) {
            state = 0;
            return;
        }

        // main menu state
        if (state == 0) {
            if (y > 20 && y < 55) {
                selectedItem = 0;
            }
            if (y > 55 && y < 85) {
                selectedItem = 1;
            }
            if (y > 85 && y < 115) {
                selectedItem = 2;
            }
            if (y > 115 && y < 150) {
                selectedItem = 3;
            }

//#if DARKWOOD_DEMOVERSION
//#             if (y > 150 && x > 50 && x < 125) {
//#                 selectedItem = 4;
//#             }
//#endif


            return;
        }


        // character slot selection state
        if (state == 1) {
            if (y > 20 && y < 55) {
                selectedItem = 0;
            }
            if (y > 55 && y < 85) {
                selectedItem = 1;
            }
            if (y > 85 && y < 115) {
                selectedItem = 2;
            }
            if (y > 115 && y < 150) {
                selectedItem = 3;
            }
            if (y > 150 && x > 50 && x < 125) {
                selectedItem = 4;
            }
            return;
        }

        // char class selection state
        if (state == 2) {
            if (x > 115 && x < 163 && y > 60 && y < 110) {
                // right
                checkInput(GameCanvas.RIGHT_PRESSED);

            }
            if (x > 20 && x < 65 && y > 60 && y < 110) {
                // left
                checkInput(GameCanvas.LEFT_PRESSED);

            }
            return;

        }
        /*
        if (y < 95) {
        checkInput(GameCanvas.UP_PRESSED);
        }
        if (y > 95 && y < 170) {
        checkInput(GameCanvas.DOWN_PRESSED);
        }
         */
    }

    public void pointerPressedEvent(int x, int y) {
    }
}
