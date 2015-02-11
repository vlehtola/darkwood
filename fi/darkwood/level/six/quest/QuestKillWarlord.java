/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.six.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.six.monsters.SkeletonKing;
import fi.darkwood.level.six.monsters.SkeletonKnight;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillWarlord extends Quest {
    public QuestKillWarlord() {
        super(Local.get("tier6.quest3"), 26);

        setCompletedText(Local.get("tier6.quest3.complete"));
         addKillRequirement(new SkeletonKnight(), 5);
        addKillRequirement(new SkeletonKing(), 1);
        setNextQuest(new QuestKillMummies());
    }

}
