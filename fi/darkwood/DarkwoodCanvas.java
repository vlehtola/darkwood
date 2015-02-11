/*
 * DarkWoodCanvas.java
 *
 * Created on 4. toukokuuta 2007, 18:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.darkwood.network.HttpClient;
import fi.darkwood.room.CityRoom;
import fi.darkwood.room.MapRoom;
import fi.darkwood.room.ShopRoom;
import fi.darkwood.room.StatRoom;
import fi.darkwood.room.TavernRoom;
import fi.darkwood.ui.view.TitleScreenView;
import fi.darkwood.ui.view.CombatView;
import fi.darkwood.ui.view.MovementView;
import fi.darkwood.ui.view.CityView;
import fi.darkwood.ui.view.ConsoleView;
import fi.darkwood.ui.view.LandscapeView;
import fi.darkwood.ui.view.MapView;
import fi.darkwood.ui.view.PauseView;
import fi.darkwood.ui.view.ShopView;
import fi.darkwood.ui.view.StatRoomView;
import fi.darkwood.ui.view.ViewAndControls;
import fi.darkwood.util.Utils;
import fi.mirake.SoundPlayer;
import java.util.Random;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;

/**
 * @author Exko
 */
public class DarkwoodCanvas extends GameCanvas implements Runnable {

    public static final int SOFTKEY1_PRESSED = -6;
    public static final int SOFTKEY2_PRESSED = -7;

    /** Creates a new instance of DarkWoodCanvas */
    public DarkwoodCanvas() {
        super(true);
    }
    private int softKeyPressed = 0;
    private Logger logger = Logger.getInstance();
    private Utils utils = Utils.getInstance();
    private Game game;
    private int width; // game design: 176 px;
    private int height; // game design: 208 px;
    private ViewAndControls activeView;
    private MapView mapView;
    private CombatView combatView;
    private MovementView movementView;
    private CityView cityView;
    private StatRoomView statRoomView;
    private TitleScreenView titleScreenView;
    private ShopView shopView;
    private ConsoleView consoleView;
    private LandscapeView landscapeView;
    private PauseView pauseView;


    private boolean blockingKeyPresses;
    private boolean run = true;
    // if JSR82 is not available in this build, dont include tavernview
//#if JSR82
    private fi.darkwood.ui.view.TavernView tavernView;
//#endif

    public void startGame(MIDlet midlet) {
        createGame(midlet);
        Thread runner = new Thread(this);
        runner.start();
    }

    /**
     * This method called when player quits game by pressing the (usually) red phone button
     */
    public void quitGame() {
        // set run to false to terminate main game loop
        run = false;

        // if not in test mode try to save character to server before exiting
        if (!GameConstants.TESTINGMODE) {
            // save char if the player is ingame (not in menu)
            if (game.player != null) {
                HttpClient.saveCharacter(game.player);
            }
        }

        // close bluetooth connections
        if (Game.party != null) {
            Game.party.leaveParty();
        }
    }

    private void createGame(MIDlet midlet) {
        game = new Game(midlet, this);

        // get phone screen resolution
        width = getWidth();
        height = getHeight();

        sizeChanged(width, height); // initial size

        // if we start in landscape mode, reverse
        if (width > height) {
            int tempw = width;
            width = height;
            height = tempw;
        }


        combatView = new CombatView(game, width, height);
        movementView = new MovementView(game, width, height);
        cityView = new CityView(game, width, height);
        titleScreenView = new TitleScreenView(game, width, height);
        mapView = new MapView(game, width, height);
        shopView = new ShopView(game, width, height);
        statRoomView = new StatRoomView(game, width, height);
        consoleView = new ConsoleView(game, width, height);
        landscapeView = new LandscapeView(game, width, height);
        pauseView = new PauseView(game,width, height);
        
        // if JSR82 is not available in this build, dont include tavernview
//#if JSR82
        tavernView = new fi.darkwood.ui.view.TavernView(game, width, height);
//#endif

        if (GameConstants.TESTINGMODE) {
            Player p = Game.createNewCharacter(Game.CLASS_CLERIC, "Testikeke");
            p.addMoney(100);
            p.level = 25;
            p.updateStats();
            p.health = p.maxHealth;
            p.mana = p.maxMana;
            // assign random global character id (normally get this from server) - needed for party to function (needs to be more than 1000)
            Random random = new Random(System.currentTimeMillis());
            int charId = random.nextInt();
            // add to test id so that it does not conflict with monster id's in a room
            if (charId < 1000 && charId > 0) {
                charId += 1000;
            }
            p.setGlobalCharacterId(charId);

            game.gameBegin(p);
        }
    }
    Image imageDisplayedOnScreen = Image.createImage(176, 208);
    Image temp;
    
    Graphics grap = imageDisplayedOnScreen.getGraphics();

    public void run() {
        long milliseconds;
        int sleepTime;


        DarkwoodGraphics g = new DarkwoodGraphics(grap, 176, 208, imageDisplayedOnScreen);
        //DarkwoodGraphics g = new DarkwoodGraphics(getGraphics(), 176, 208);

        try {
            while (run) { // infinite loop


                // pause if canvas is currently not shown (not shown if player is "tabbed out", or incoming call etc)
                if (!isShown()) {
                    // sleep in pause state..
                    Game.setPaused(true);
/*                    try {
                        Thread.currentThread().sleep(100);
                    } catch (Exception e) {
                        logger.debug(e.getMessage());
                    }
                    // resume loop from start (while)
                    continue; */
                }



                milliseconds = System.currentTimeMillis();

                activeView = determineActiveView();

                checkInput(); // call the active view and check player input and act on it
                Game.party.readCommands(); // read messages from server / clients

                if (!Game.getPaused()) {
                    tick(); // game events, if not paused
                }
                
                // determine active view again, because it has maybe changed because of player input
                activeView = determineActiveView();

                updateScreen(g); // update graphics on screen

                temp = utils.resizeImage(imageDisplayedOnScreen, width, height, 0, 0);

             
             
                if (landscapeMode) {
                    temp = Utils.rotateImage(temp, 90);
                }
                getGraphics().drawImage(temp, 0, 0, 0);
                

                flushGraphics();
                // 10 fps - sleep 100 ms minus the time needed to run, each cycle of the loop
                milliseconds = System.currentTimeMillis() - milliseconds;
                sleepTime = 100 - (int) milliseconds;

                logFps(sleepTime); // log fps to game console

                // min sleeptime 5 ms, give other threads some time too
                if (sleepTime < 5) {
                    sleepTime = 5;
                }
                try {
                    // sleep if loop time was less than 100 ms (to control game speed)
                    Thread.currentThread().sleep(sleepTime);
                } catch (Exception e) {
                    logger.debug("Error in main sleep");
                    logger.debug(e.getMessage());
                }
            }

        } catch (Exception e) {
            // print stack trace for all errors occuring
            e.printStackTrace();
            Logger.getInstance().logToFile(e.getMessage());

        }
    }
    int fpsLoggingCounter = 0;

    /**
     * Write lag related logging in game console
     */
    private void logFps(int sleepTime) {
        // if not in debug mode, dont write anything
        if (!GameConstants.LOG_SLEEPTIME) {
            return;        // log sleeptime (frames per second)
        }
        fpsLoggingCounter = fpsLoggingCounter + 1;
        if (fpsLoggingCounter == 20) {
            logger.debug("sleeptime: " + sleepTime);
            fpsLoggingCounter = 0;
        }
    }

    /**
     * Possibility to resolve game events for fewer frames, to reduce lag
     */
    private void tick() {
        Game.tick();

    }

    /*
     * When user presses softkey, this method is automatically called.
     * Softkey presses are handled here. Other keys are handled in checkInput() below
     */
    public void keyPressed(int keyCode) {
        logger.debug("Pressed: " + keyCode + " KeyName: " + getKeyName(keyCode));
        softKeyPressed = keyCode;
    }

    // variables for pointerReleased
    private int reducedX = -1;
    private int reducedY = -1;

    // variables for pointerPressed
    private int reducedX_pressed = -1;
    private int reducedY_pressed = -1;

    /*
     * This method automatically called when user points at touch screen
     */

    public void pointerReleased(int x, int y) {
        // handle graphics resizing effect, due to buttons appearing at
        // different x y locations depending on screen size
        reducedX = x * 176 / width;
        reducedY = y * 208 / height;

        // SOFTKEY HANDLING
        // left softkey handling (softkey1)
        if (reducedX < 50 && reducedY > 180) {
            softKeyPressed = DarkwoodCanvas.SOFTKEY1_PRESSED;

            // reset the variables so no other effect happens
            reducedX = -1;
            reducedY = -1;
            return;
        }

        // right softkey handling (softkey2)
        if (reducedX > 125 && reducedY > 180) {
            softKeyPressed = DarkwoodCanvas.SOFTKEY2_PRESSED;

            // reset the variables so no other effect happens
            reducedX = -1;
            reducedY = -1;

            return;

        }

        // handle landscape mode
        int tempY;
        if (landscapeMode) {
            tempY = reducedY;
            reducedY = reducedX;
            reducedX = tempY;
        }
//        activeView.pointerReleasedEvent(reducedX, reducedY); MOVED TO CHECKINPUT UNDER GAMELOOP BECAUSE OF THREAD ISSUES
    }

    public void pointerPressed(int x, int y) {
        reducedX_pressed = x * 176 / width;
        reducedY_pressed = y * 208 / height;

        // dont do anything if touch on the softkeys
        if ((reducedX_pressed < 50 && reducedY_pressed > 180) || (reducedX_pressed > 125 && reducedY_pressed > 180)) {
            return;
        }
       

        // handle landscape mode
        int tempY;
        if (landscapeMode) {
            tempY = reducedY_pressed;
            reducedY_pressed = reducedX_pressed;
            reducedX_pressed = tempY;
        }
        activeView.pointerPressedEvent(reducedX_pressed, reducedY_pressed);
    }

    private void checkInput() {
        // others than softkeys are handled by getKeyStates()
        // softkeys are handled above in keyPressed() and put to softKeyPressed variable
        int keyState = getKeyStates();
        if (keyState == 0 && softKeyPressed != 0) {
            keyState = softKeyPressed;
            softKeyPressed = 0;
        }

        // HANDLE TOUCH SCREEN PRESS (which was registered at pointerReleased above)
        if (reducedX > -1 && reducedY > -1) {

            // call the active view, which then converts the location to key presses as needed
            activeView.pointerReleasedEvent(reducedX, reducedY);

            // reset touch variables
            reducedX = -1;
            reducedY = -1;
        }

        // if keypresses are not blocked (blocked until previous keys are)
        if (!blockingKeyPresses) {
            activeView.checkInput(keyState);
            
        }
        // block keys until keys released
        if (keyState != 0) {
            blockingKeyPresses = true;
        }
        if (keyState == 0) {
            blockingKeyPresses = false;
        }
    }

    private void updateScreen(DarkwoodGraphics g) {

        // fill the whole mobile phone screen with black

        //Image tmp = Image.createImage(176, 208);

        //DarkwoodGraphics g = new DarkwoodGraphics(tmp.getGraphics(), width, height);

        g.getGraphics().setColor(0x000000);
        g.getGraphics().fillRect(0, 0, width, height);

        activeView.updateScreen(g);

        g.fillBorders();
        //flushGraphics();
    }

    ViewAndControls determineActiveView() {
        if (landscapeMode) {
            return landscapeView;
        }

        if (Game.getPaused()) {
            return pauseView;
        }

        if (Game.player == null) {
            return titleScreenView;
        } else if (Game.showConsole == true) {
            return consoleView;
        } else if (Game.player.room instanceof MapRoom) {
            return mapView;
        } else if (Game.player.room instanceof ShopRoom) {
            return shopView;
        } else if (Game.player.room instanceof CityRoom) {
            return cityView;
        } else if (Game.player.room instanceof StatRoom) {
            return statRoomView;
//#if JSR82
            // only if bluetooth package (JSR82) is present
        } else if (Game.player.room instanceof TavernRoom) {
            return tavernView;
//#endif
        } else if (Game.player.isHostileInRoom() || game.player.textEvents.size() > 0) {
            return combatView;
        } else {
            combatView.fingerOnPad = false; // in case the view is switched while touchpad is pressed
            return movementView;
        }
    }

    // stop music when game goes to background
    protected void hideNotify() {
        SoundPlayer.stopMusic();
    }

    // resume music when game comes back
    protected void showNotify() {
        SoundPlayer.resumeMusic();
    }

    protected void sizeChanged(int w, int h) {


        // dont use sizeChanged function.. some phones have w > h on default..
        // update: 23.1.2011 handle landscape mode afterall
        if (w > h) {
            landscapeMode = true;
        } else {
            landscapeMode = false;
        }
        //super.sizeChanged(w, h);
    }
    private boolean landscapeMode = false;


    private Player pausePlayer;

    /**
     * Method used to return to main menu from city
     */
    public void returnToMainMenu() {
        // save current player into temp variable
        pausePlayer = Game.player;

        // this will cause title screen view to be displayed
        Game.player = null;
        titleScreenView.selectedItem = 0;
        titleScreenView.state = 0;

// no need to be able to pause game in main menu
        Game.pauseable = false;
    }


}
