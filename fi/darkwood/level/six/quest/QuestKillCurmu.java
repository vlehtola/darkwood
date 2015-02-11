/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.six.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.six.monsters.*;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillCurmu extends Quest {

    public QuestKillCurmu() {
        super(Local.get("tier6.quest6"), 30);

        setCompletedText(Local.get("tier6.quest6.complete"));
        addKillRequirement(new Curmu(), 1);
 
    }
    
}
