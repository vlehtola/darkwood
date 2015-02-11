/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood;

import fi.darkwood.ui.component.MessageLog;

/**
 *
 * @author Ville
 */
public class SoloStopSign extends UsableThing {
    public SoloStopSign() {
        super("sign", "/images/solostop.png");
    }

    public void tick() {
    }    

    public boolean useThing() {
        MessageLog.getInstance().addMessage("You need a party to venture further!");
        return true;
    }

    public String queryUsedText() {
        return "";
    }

    public String queryUnusedText() {
        return "Read";
    }

}
