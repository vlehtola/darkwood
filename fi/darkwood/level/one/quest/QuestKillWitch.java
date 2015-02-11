/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.one.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.one.monsters.Witch;
import fi.mirake.Local;

/**
 *
 * @author Administrator
 */
public class QuestKillWitch extends Quest {

    public QuestKillWitch() {
        super(Local.get("tier1.quest5"), 4);

        setCompletedText(Local.get("tier1.quest5.complete"));
        addKillRequirement(new Witch(), 1);
    //    setNextQuest(new QuestKillWitch());
    }
    
}
