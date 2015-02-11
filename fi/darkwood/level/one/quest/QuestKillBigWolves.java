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
public class QuestKillBigWolves extends Quest {

    public QuestKillBigWolves() {
        super(Local.get("tier1.quest2"), 2);

        setCompletedText(Local.get("tier1.quest2.complete"));
        addKillRequirement(new Wolf(), 10);
        addKillRequirement(new BigWolf(), 10);

        setNextQuest(new QuestKillWerewolf());

    }
}
