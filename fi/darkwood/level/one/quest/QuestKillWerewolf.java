/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.one.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.one.monsters.Werewolf;
import fi.darkwood.level.one.monsters.Wolf;
import fi.mirake.Local;

/**
 *
 * @author Administrator
 */
public class QuestKillWerewolf extends Quest {

    public QuestKillWerewolf() {
        super(Local.get("tier1.quest3"), 3);

        setCompletedText(Local.get("tier1.quest3.complete"));
        addKillRequirement(new Werewolf(), 1);
        setNextQuest(new QuestKillWerewolves());
    }
    
}
