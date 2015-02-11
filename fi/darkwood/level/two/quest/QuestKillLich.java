/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.two.quest;

import fi.darkwood.Game;
import fi.darkwood.Quest;
import fi.darkwood.level.six.quest.QuestKillCurmu;
import fi.darkwood.level.two.monsters.Lich;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillLich extends Quest {

    public QuestKillLich() {
        super(Local.get("tier2.quest2"), 9);

        setCompletedText(Local.get("tier2.quest2.complete"));
        addKillRequirement(new Lich(), 1);
        
        if (Game.player != null && Game.player.completedQuests.contains((Quest) new QuestKillCurmu())) {
            setNextQuest(new QuestTheEnd()); 
        }
    }
}
