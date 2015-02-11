/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.ui.forms;

import fi.darkwood.DarkwoodMidlet;
import fi.darkwood.Game;
import fi.darkwood.ui.view.TitleScreenView;
import fi.darkwood.util.LocalDatabase;
import fi.darkwood.util.StringUtils;
import fi.darkwood.util.Utils;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author Teemu
 */
public class TermsOfUseForm extends Form implements CommandListener {

    private Command exitCommand = new Command("Cancel", Command.EXIT, 1);
    private Command okCommand = new Command("Accept", Command.OK, 2);

    Form accountForm;
    Display display;
    DarkwoodMidlet midlet;

    private StringItem str = new StringItem("Accept the Terms of Service",
"Darkwood is a mobile game where players compete against each other in online top lists. Cheating, forging save game data or modifying the client to give an advantage to the player is strictly forbidden. The game's operators reserve the right to exclude any player who violates these rules for a limited period of time or for good.\n\n" +
"Charges apply for data transferred during game play. When you save the game, on average 5 kbytes of data are transmitted (saving is done either manually in cities or automatically when you exit the game). Additional charges only apply if you decide to subscribe to the game's premium services.\n\n" +
"The use of Darkwood is at your own risk. The game's operators rule out any claim for damages caused by the use of this game unless said damages were caused by intentional or gross negligence on their part. Also, loss of data and playing time will under no circumstances be compensated. The game's operators reserve the right to develop the game further.\n\n");
           

    public TermsOfUseForm(Form f, Display d, DarkwoodMidlet dm) {
        super("Darkwood");
        accountForm = f;
        display = d;
        midlet = dm;

        append(str);
        addCommand(exitCommand);
        addCommand(okCommand);

        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {

        if (c == exitCommand) {
            // exit application
            midlet.notifyDestroyed();
        } else if (c == okCommand) {
            display.setCurrent(accountForm);
            // mark that the TOS are accepted for this phone (so they are not shown again next time)
            LocalDatabase.setTermsOfServiceAccepted();
        }
    }

}
