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
import fi.darkwood.Player;
import fi.darkwood.party.PartySearcher;
import fi.darkwood.room.TavernRoom;
import fi.darkwood.ui.component.Common;
import fi.darkwood.ui.component.Expbar;
import fi.darkwood.util.Utils;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.bluetooth.ServiceRecord;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author Teemu Kivim�ki
 */
public class TavernView extends ViewAndControls {

    Utils utils = Utils.getInstance();
    Game game;
    Expbar expbar;
    int choice = 0;
    int state = 0;
    Vector availableParties = new Vector();
    //   private BitMapFont bitMapFont = BitMapFont.getInstance("/fonts/arial10.bmf");;
    //  private BitMapFontViewer messageViewer;
    PartySearcher partySearcher = new PartySearcher();
    private boolean searching = false;
    private boolean noPartiesFound = false;
    // blink to indicate search in progress
    public int searchBlink = 0;
    private static boolean drawWelcome = true;

    public TavernView(Game game, int width, int height) {
        super(width, height);
        this.game = game;
        expbar = Expbar.getInstance();
        leftSoftKeyText = "Exit";
        rightSoftKeyText = "Select";
    }
    String txt;

    public void updateScreen(DarkwoodGraphics g) {

        Image img = utils.getImage(Game.player.room.getBackground());

        // draw background
        g.drawImage(img, 0, 29, 0);

        if (drawWelcome) {
            img = utils.getImage("/images/ui/city_pergament_bg.png");
            g.drawImage(img, 10, 80, 0);
            g.drawText("Welcome to the tavern. Here you can use your phones Bluetooth to team up with your friends. Once you have created a party, " + "your friend can use 'Search' to find your party on his mobile phone. Make sure you have Bluetooth enabled.", 15, 85, 150, 150, 0, 0, DarkwoodGraphics.FONT_ARIAL10_BLACK);
        }
        // draw UI frames
        img = utils.getImage("/images/ui/frames_shop.png");
        g.drawImage(img, 0, 0, 0);

        // draw pergament background
//        img = Utils.getInstance().getImage("/images/ui/shop_pergament_bg.png");
//      g.drawImage(img, 3, 126, 0);


        // draw exp bar

        expbar.drawBar(g, Game.player);

        // draw status        
        Common.drawStatus(g, Game.player);

        state = checkState();

        if (state == 0) {
            drawAvailableParties(g);
        } else if (state == 1) {
            drawCurrentMembers(g);
        } else if (state == 2) {
            g.drawText("Bluetooth not available.", 10, 30);
        }

        super.drawSoftKeyTexts(g);

    }

    private int checkState() {
        // bluetooth not available
        if (!Game.party.getBluetoothAvailable()) {
            return 2;
        }

        if (Game.party.isActive()) {
            return 1;
        } else {
            return 0;
        }
    }

    private void drawAvailableParties(DarkwoodGraphics g) {
        rightSoftKeyText = "Select";


        int y = 30;
        g.drawText(">", 5, y + choice * 10);

        g.drawText("Create new party", 10, y);
        y = y + 10;
        g.drawText("Search", 10, y);

        Enumeration enumeration = availableParties.elements();
        while (enumeration.hasMoreElements()) {
            ServiceRecord remotePartyService = (ServiceRecord) enumeration.nextElement();
            txt = "error reading device";
            try {
                txt = remotePartyService.getHostDevice().getFriendlyName(true);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                txt = "null service or device";
            }
            y = y + 10;
            g.drawText(txt, 10, y);

        }
        if (searching) {
            y = y + 10;
            txt = "Searching devices";
            if (searchBlink > 1) {
                txt = txt + ".";
            }
            if (searchBlink > 4) {
                txt = txt + ".";
            }
            if (searchBlink > 7) {
                txt = txt + ".";
            }
            g.drawText(txt, 10, y);
            searchBlink += 1;
            if (searchBlink > 9) {
                searchBlink = 0;
            }
        } else if (noPartiesFound) {
            y = y + 15;
            txt = "No available parties found.";
            g.drawText(txt, 10, y);
        }
    }

    private void drawCurrentMembers(DarkwoodGraphics g) {
        boolean drawWaitingText = false;
        rightSoftKeyText = "Abandon";

        int y = 30;
        int membercount = 0;
        Enumeration enumeration = Game.party.members.elements();
        while (enumeration.hasMoreElements()) {
            Player p = (Player) enumeration.nextElement();
            txt = p.name;
            if (p.getId() == Game.player.getId()) {
                txt = txt + " (you)";
            }
            y = y + 10;
            g.drawText(txt, 10, y);

            membercount++;
        }

        while (membercount < 2) {
            txt = "- Empty slot -";
            y = y + 10;
            g.drawText(txt, 10, y);
            membercount++;
            drawWaitingText = true;
        }


        y = y + 15;
        if (drawWaitingText) {
            txt = "Waiting for connections..";
            g.drawText(txt, 10, y);
        } else {
            txt = "Party is ready!";
            g.drawText(txt, 10, y);

        }
    }

    public void checkInput(int keyState) {
        if (keyState == GameCanvas.DOWN_PRESSED) {
            // hide welcome text
            drawWelcome = false;

            choice += 1;
            if (choice > 1 + availableParties.size()) {
                choice = 0;
            }

        } else if (keyState == GameCanvas.UP_PRESSED) {
            choice -= 1;
            if (choice < 0) {
                choice = 1 + availableParties.size();
            }

        } else if (keyState == DarkwoodCanvas.SOFTKEY1_PRESSED) {

            TavernRoom room = (TavernRoom) Game.player.room;

            room.removeThing(Game.player);
            room.exit.addThing(Game.player);

            // finalize party
            Game.party.finalizeParty();

            // restore welcome text
            drawWelcome = true;

        } else if (keyState == DarkwoodCanvas.SOFTKEY2_PRESSED) {
            // hide welcome text
            drawWelcome = false;

            if (state == 0) {
                if (choice == 0) {
                    // create a new party
                    Game.party.createParty(true, null);
                    state = 1;
                    availableParties.removeAllElements();
                    choice = 0;
                }
                if (choice == 1) {

                    if (searching) {
                        // already searching, do nothing
                        return;
                    }

                    // search for available parties
                    availableParties.removeAllElements();
                    partySearcher.search(this);

                    searching = true;
                }
                if (choice > 1) {
                    // join chose party
                    Game.party.createParty(false, (ServiceRecord) availableParties.elementAt(choice - 2));
                    state = 1;
                    availableParties.removeAllElements();
                    choice = 0;
                }
            } else if (state == 1) {
                // right softkey leaves party if you are in one
                Game.party.leaveParty();
                state = 0;
                availableParties.removeAllElements();
                choice = 0;
            }
        }

    }

    public void addAvailableParty(ServiceRecord record) {
        availableParties.addElement(record);
    }

    public void notifySearchComplete() {
        searching = false;
        if (availableParties.isEmpty()) {
            noPartiesFound = true;
        } else {
            noPartiesFound = false;
        }
    }

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
