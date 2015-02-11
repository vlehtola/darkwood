/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.three.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.three.monsters.Quartermaster;
import fi.mirake.Local;

/**
 *
 * @author Ville
 *
 */
public class QuestKillQuartermaster extends Quest {

    public QuestKillQuartermaster() {
        super(Local.get("tier3.questkillquartermaster"), 14);
  
        setCompletedText(Local.get("tier3.questcompletekillquartermaster"));
        addKillRequirement(new Quartermaster(), 1);
        setNextQuest(new QuestKillBaron());
    }
}
