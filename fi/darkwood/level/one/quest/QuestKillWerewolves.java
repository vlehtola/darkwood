/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.one.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.one.monsters.Werewolf;
import fi.mirake.Local;

/**
 *
 * @author Administrator
 */
public class QuestKillWerewolves extends Quest {

    public QuestKillWerewolves() {
        super(Local.get("tier1.quest4"), 3);

        setCompletedText(Local.get("tier1.quest4.complete"));
        addKillRequirement(new Werewolf(), 5);
        setNextQuest(new QuestKillWitch());
    }
    
}
