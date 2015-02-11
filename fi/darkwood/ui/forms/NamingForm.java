/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ui.forms;

import fi.darkwood.Game;
import fi.darkwood.ui.view.TitleScreenView;
import fi.darkwood.util.StringUtils;
import fi.darkwood.util.Utils;
import fi.mirake.Local;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author Teemu
 */
public class NamingForm extends Form implements CommandListener {

    private Command exitCommand = new Command(Local.get("buttons.cancel"), Command.EXIT, 1);
    private Command okCommand = new Command(Local.get("buttons.ok"), Command.OK, 2);
    private TextField characterName = new TextField(Local.get("naming.give.name"), "", 10, TextField.INITIAL_CAPS_SENTENCE |
            TextField.NON_PREDICTIVE);
    TitleScreenView view;

//#if DARKWOOD_OFFLINE
//#     private StringItem str = new StringItem("", Local.get("naming.help.text"));
//#else
    private StringItem str = new StringItem("", "Give a name to your character. The name should be of medieval or fantasy style, for example " +
            "'Aranoth' or 'Galadrien'. Characters with names containing profanity will be renamed or deleted.");
//#endif
    private StringItem invalidName = new StringItem("", Local.get("naming.validation"));

    public NamingForm(TitleScreenView v, Display display) {
        super("Darkwood");
        view = v;

        Image img = Utils.getInstance().getImage("/images/charnaming.png");

        ImageItem imgItem = new ImageItem("",img,ImageItem.LAYOUT_CENTER,"");

        append(imgItem);

        append(characterName);
        append(str);
        addCommand(exitCommand);
        addCommand(okCommand);

        setCommandListener(this);
        
        display.setCurrentItem(characterName);

    }

    public void commandAction(Command c, Displayable d) {

        if (c == exitCommand) {
            view.state = 1;
            Display.getDisplay(Game.gameMidlet).setCurrent(Game.darkwoodCanvas);

        } else if (c == okCommand) {
            boolean formValid = validateForm();
            if (formValid) {
                view.state = 2;
                view.setCharacterName(Utils.capitalize(characterName.getString()));
//                Game.setPaused(false); // game is paused because of not showing dw canvas.. need to unpause
                Display.getDisplay(Game.gameMidlet).setCurrent(Game.darkwoodCanvas);
            } else {
                insert(2, invalidName);
            }
        }
    }

    private boolean validateForm() {
        String name = characterName.getString();
        if (name == null) {
            return false;
        }
        
        if (name.length() < 3) {
            return false;
        }

        if (name.length() > 10) {
            return false;
        }


        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        if (StringUtils.indexOfAnyBut(name, validChars.toCharArray()) != -1) {
            return false;
        }

        return true;
    }

 /*   public String capitalize(String inputWord) {
        
        // if argument lenght is only 1, return it as capitalized
        if (inputWord.length() < 2) { return inputWord.toUpperCase(); }

        String firstLetter = inputWord.substring(0,1);  // Get first letter
        String remainder   = inputWord.substring(1);    // Get remainder of word.
        return firstLetter.toUpperCase() + remainder.toLowerCase();

    } */

}
