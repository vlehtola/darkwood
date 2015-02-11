/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.six.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.six.monsters.Skeleton;

/**
 *
 * @author Ville
 */
public class QuestWelcome extends Quest {

    public QuestWelcome() {
        super(
                "Greetings, brave hero! The Queen has left you a message; it seems that " +
                "an ancient king has awakened in the undead citadel located in the desert.",24);

        setCompletedText("You are most welcome. We need every pair of hands in fighting the enemy.");

        setNextQuest(new QuestKillSkeletons());
    }

}
