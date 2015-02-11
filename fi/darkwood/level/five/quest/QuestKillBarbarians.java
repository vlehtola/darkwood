/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.five.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.five.monsters.Barbarian;
import fi.darkwood.level.five.monsters.Berserker;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillBarbarians extends Quest {

    public QuestKillBarbarians() {
        super(
                Local.get("tier5.quest2"),19);

        setCompletedText(Local.get("tier5.quest2.complete"));
        addKillRequirement(new Barbarian(), 17);
        addKillRequirement(new Berserker(), 13);
        setNextQuest(new QuestKillBarbarianBoss());
    }

}
