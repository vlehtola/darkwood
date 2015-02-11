/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.six.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.six.monsters.*;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillSkeletons extends Quest {

    public QuestKillSkeletons() {
        super(Local.get("tier6.quest1"), 25);

        setCompletedText(Local.get("tier6.quest1.complete"));
        addKillRequirement(new Skeleton(), 10);
        addKillRequirement(new SkeletonWarrior(), 10);       
        setNextQuest(new QuestKillGG());
    }
    
}
