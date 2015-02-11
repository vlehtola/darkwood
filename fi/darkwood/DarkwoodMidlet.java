/*
 * DarkWoodMidlet.java
 *
 * Created on 4. toukokuuta 2007, 18:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.darkwood.ui.forms.TermsOfUseForm;
import fi.darkwood.util.LocalDatabase;
import fi.darkwood.util.Properties;
import fi.darkwood.util.Utils;
import fi.mirake.MirakeDemoCanvas;
import fi.mirake.SoundPlayer;
import fi.mirake.UnregisteredInfoCanvas;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.midlet.*;

/**
 *
 * @author  Exko
 * @version
 */
import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

// TODO: NOKIA SPECIFIC, MAYBE USE PREPROCESSOR
//import com.nokia.mid.ui.DeviceControl;
/** 
 * This is the MIDlet class for darkwood game. 
 * First load properties from config files, like server addresses.
 * Then show login form to get account name and password. When login ok,
 * move to darkwood game canvas and startGame game.
 * 
 */
public class DarkwoodMidlet extends MIDlet implements CommandListener {

    /** Creates a new instance of DarkWoodMidlet */
    private DarkwoodCanvas dwc;
    private Form accountForm; // form for account info management
    private Alert alert;
    private Alert silverAccountAlert;
    private TextField accountName = new TextField("Account Name", "", 20, TextField.ANY);
    private TextField password = new TextField("Password", "", 20, TextField.ANY);
    //   static private TextField characterName = new TextField("Character Name", "", 20, TextField.ANY);
    private StringItem validationError = new StringItem("", "All fields are mandatory");
    private ChoiceGroup savePassword = new ChoiceGroup("", Choice.MULTIPLE,
            new String[]{"Save password"}, null);
    private ChoiceGroup enableMusic = new ChoiceGroup("", Choice.MULTIPLE,
            new String[]{"Enable music"}, null);
    final Command sendCommand = new Command("OK", Command.OK, 1);
    final Command exitCommand = new Command("Quit", Command.EXIT, 2);
    public boolean btSupport; // marks if bluetooth support is found

    public DarkwoodMidlet() {

        Properties.getApplicationProperties().put("Account-Name", "");
        Properties.getApplicationProperties().put("Account-Password", "");

        dwc = new DarkwoodCanvas();
        dwc.setFullScreenMode(true);


// This to set backlight on. Problem is that it's Nokia only, need to use preprocessor to sort out phone vendor :< -Teemu
// Decision: not to make a different build for Nokia phones (and each other manufacturer) - 30.12.2009 Teemu
        //DeviceControl.setLights(0,100);


        accountForm = new Form("Darkwood login");


    }

    protected void startApp() throws MIDletStateChangeException {
        try {

            if (Game.darkwoodCanvas != null) {
                System.out.println("StartApp called after pause");
                return; // game is already running, this call was from resume after pause
            }

            if (GameConstants.TESTINGMODE) {
                Properties.getApplicationProperties().put("Account-Name", "test");
                Properties.getApplicationProperties().put("Account-Password", "test");
                //   Properties.getApplicationProperties().put("Character-Name", "Testiaija");

                Display display = Display.getDisplay(this);
                display.setCurrent(dwc);
                dwc.startGame(this);
//                fetchCharacterData();
            } else {

                Display display = Display.getDisplay(this);

                // load darkwood logo
                Image img = Utils.getInstance().getImage("/images/login_logo.png");
                ImageItem imgItem = new ImageItem("", img, ImageItem.LAYOUT_CENTER, "");

                // load mirake logo
                Image img2 = Utils.getInstance().getImage("/images/mirake_logo.png");
                ImageItem imgItem2 = new ImageItem("", img2, ImageItem.LAYOUT_NEWLINE_BEFORE | ImageItem.LAYOUT_CENTER, "");

                // append darkwood logo
                accountForm.append(imgItem);

                accountName.setInitialInputMode("MIDP_LOWERCASE_LATIN"); // set accountName field default input mode to lowercase
                accountForm.append(accountName);

                password.setConstraints(TextField.PASSWORD);
                accountForm.append(password);
                //       accountForm.append(characterName);
                accountForm.append(savePassword);

                boolean[] flags = {true};
                enableMusic.setSelectedFlags(flags);
                accountForm.append(enableMusic);



                // add commands
                accountForm.addCommand(sendCommand);
                accountForm.addCommand(exitCommand);
                accountForm.setCommandListener(this);

                // populate the fields with pre-existing data from phone local datastore
                LocalDatabase.populateLoginFields(accountName, password, savePassword);


                // test bluetooth availability
                StringItem btSupportString;
                btSupport = true; //BluetoothTester.hasBluetoothAPI();
                if (btSupport) {
                    String str = "Available";
                    //#ifndef JSR82
//#                     // if this is "no bluetooth" build, add text
//#                     str += ", you should install Bluetooth enabled version of Darkwood.";
//#endif

                    btSupportString = new StringItem("Bluetooth support:", str);
                } else {
                    btSupportString = new StringItem("Bluetooth support:", "Unavailable, multiplayer features disabled! Please check phone Bluetooth settings.");
                }
                accountForm.append(btSupportString);


                // append mirake logo
                accountForm.append(imgItem2);

                accountForm.append(new StringItem("", "©2010 Copyright Mirake Ltd. All rights reserved."));


                // if terms of service is not accepted on this phone, show form with TOS
                // account form is then displayed after user accepts TOS
                if (LocalDatabase.isTermsOfServiceAccepted() == false) {
                    TermsOfUseForm TOSForm = new TermsOfUseForm(accountForm, display, this);
                    display.setCurrent(TOSForm);
                } else { // else display account form directly
                    display.setCurrent(accountForm);

                    // set focus on the account name field
                    display.setCurrentItem(accountName);
                }
            }
            /*
            dwc.startGame();
            display.setCurrent(dwc); */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commandAction(Command c, Displayable d) {

        if (c == exitCommand) {
            notifyDestroyed();
        } else if (c == sendCommand) {
            boolean formValid = validateForm();
            if (formValid) {
                fetchCharacterData();
            }
        }
    }

    private void fetchCharacterData() {
        // authenticate and request character xml from the server
        new ServerConnectionThread(this);
        showConfirmation("Connecting..", "Connecting to server, please wait..", "Cancel");

    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean b) throws MIDletStateChangeException {
        System.out.println("destroyApp");
        dwc.quitGame();
        /* try {
        Thread.sleep(10000);
        } catch (Exception e) {
        //
        } */
    }

    public void closeAlert() {
        Display.getDisplay(this).setCurrent(accountForm);
    }

    public void showConfirmation(String title, String text, String buttonText) {
//        Image img = Utils.getInstance().getImage("/images/city.png");
        alert = new Alert(title, text, null, AlertType.CONFIRMATION);
        alert.setTimeout(Alert.FOREVER);
        alert.addCommand(new Command(buttonText, Command.OK, 1));
        alert.setCommandListener(new CommandListener() {

            public void commandAction(Command c, Displayable d) {
                if (c.getLabel().equals("Ok")) {
                    System.out.println("Ok clicked");
                    closeAlert();
//                    notifyDestroyed();
                }
                if (c.getLabel().equals("No")) {
                    System.out.println("No clicked");
                    closeAlert();

                }
            }
        });
        Display.getDisplay(this).setCurrent(alert);
    }

    private boolean validateForm() {

        // check that no field is empty
        if (accountName.getString() == null || accountName.getString().length() < 1
                || password.getString() == null || password.getString().length() < 1) {
            //   characterName.getString() == null || characterName.getString().length() < 1) {
            accountForm.append(validationError);

            return false;
        }

        Properties.getApplicationProperties().put("Account-Name", accountName.getString());
        Properties.getApplicationProperties().put("Account-Password", password.getString());
        //   Properties.getApplicationProperties().put("Character-Name", characterName.getString());

        return true;
    }

    public void notifyFetchComplete(String charactersXml) {
        try {

            // save the character info to be used later when actual game loads
            Properties.getApplicationProperties().put("characters-xml", charactersXml);

            LocalDatabase.saveAccountInfo(accountName.getString(), password.getString(), savePassword.isSelected(0));

            // enable or disable music
            SoundPlayer.enableMusic(enableMusic.isSelected(0));

            /*   if (Game.registered == false) {
            showSilverAccountAlert("Silver account", "Silver accounts can only level characters until level 10. Upgrade to Gold account at http://darkwood.cc!");
            } */


            // start (load) the game (starts only after displaying possible silver screen and mirake demo)
            dwc.startGame(this);

            // get the display object to change current
            Display display = Display.getDisplay(this);

            // if silver account, display silver account info screen
            if (Game.registered == false) {
                UnregisteredInfoCanvas unreg = new UnregisteredInfoCanvas();
                display.setCurrent(unreg);
                unreg.start();
            }

            // display mirake demo
            MirakeDemoCanvas demo = new MirakeDemoCanvas();
            display.setCurrent(demo);

            // start music
            SoundPlayer.playTitleScreenMusic();

            demo.startDemo();

            // set darkwood canvas as the current display
            display.setCurrent(dwc);


        } catch (Exception e) {
            // print stack trace for all errors occuring
            e.printStackTrace();
        }
    }

    public void closeSilverAccountAlert() {
        Display.getDisplay(this).setCurrent(dwc);
    }

    public void showSilverAccountAlert(String title, String text) {
        //  Image img = Utils.getInstance().getImage("/images/city.png");
        silverAccountAlert = new Alert(title, text, null, AlertType.CONFIRMATION);
        silverAccountAlert.setTimeout(Alert.FOREVER);
        silverAccountAlert.addCommand(new Command("Ok", Command.OK, 1));
        silverAccountAlert.setCommandListener(new CommandListener() {

            public void commandAction(Command c, Displayable d) {
                if (c.getLabel().equals("Ok")) {
                    System.out.println("Ok clicked");
                    closeSilverAccountAlert();
//                    notifyDestroyed();
                }

            }
        });
        Display.getDisplay(this).setCurrent(silverAccountAlert);
    }
}
