/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.one.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.one.monsters.BigWolf;
import fi.darkwood.level.one.monsters.Wolf;
import fi.mirake.Local;

/**
 *
 * @author Administrator
 */
public class QuestKillWolves extends Quest {

    public QuestKillWolves() {
        super(Local.get("tier1.quest1"), 1);

        setCompletedText(Local.get("tier1.quest1.complete"));
        addKillRequirement(new Wolf(), 7);
        setNextQuest(new QuestKillBigWolves());
    }
    
}
