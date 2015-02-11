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
public class QuestKillMummies extends Quest {

    public QuestKillMummies() {
        super(Local.get("tier6.quest4"), 27);

        setCompletedText(Local.get("tier6.quest4.complete"));
        addKillRequirement(new Mummy(), 15);
       
        setNextQuest(new QuestFreeQueen());
    }
    
}
