/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.two.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.two.monsters.Skeleton;
import fi.darkwood.level.two.monsters.Zombie;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillZombies extends Quest {

    public QuestKillZombies() {
        super(Local.get("tier2.quest4"), 6);

        setCompletedText(Local.get("tier2.quest4.complete"));
        addKillRequirement(new Zombie(), 10);
        addKillRequirement(new Skeleton(), 10);

        setNextQuest(new QuestKillSkeletonWarrior());

    }
}
