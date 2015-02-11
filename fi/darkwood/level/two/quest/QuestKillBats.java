/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.two.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.two.monsters.Bat;
import fi.mirake.Local;

/**
 *
 * @author Ville
 *
 * The queen warms up to the adventurer as the quests get completed.
 */
public class QuestKillBats extends Quest {

    public QuestKillBats() {
        super(Local.get("tier2.quest1"), 8);

        setCompletedText(Local.get("tier2.quest1.complete"));
        addKillRequirement(new Bat(), 5);
        setNextQuest(new QuestKillZombies());
    }
}
