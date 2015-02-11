/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.six.quest;

import fi.darkwood.Quest;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestFreeQueen extends Quest {

    public QuestFreeQueen() {
        super(Local.get("tier6.quest5"), 25);

        setCompletedText(Local.get("tier6.quest5.complete"));
        this.addRequirement(Local.get("tier6.quest5.objective"), 1);
        setNextQuest(new QuestKillCurmu());
    }

}