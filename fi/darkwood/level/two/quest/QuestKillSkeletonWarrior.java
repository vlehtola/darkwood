/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.two.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.two.monsters.SkeletonWarrior;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillSkeletonWarrior extends Quest {

    public QuestKillSkeletonWarrior() {
        super(Local.get("tier2.quest3"), 7);

        setCompletedText(Local.get("tier2.quest3.complete"));
        addKillRequirement(new SkeletonWarrior(), 6);

        setNextQuest(new QuestKillLich());
    }
}
