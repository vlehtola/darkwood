/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.five.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.five.monsters.BarbarianBoss;
import fi.darkwood.level.five.monsters.Berserker;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillBarbarianBoss extends Quest {

    public QuestKillBarbarianBoss() {
        super(
                Local.get("tier5.quest3"),21);

        setCompletedText(Local.get("tier5.quest3.complete"));
        addKillRequirement(new Berserker(), 10);
        addKillRequirement(new BarbarianBoss(), 1);
        setNextQuest(new QuestKillIceQueen());
    }

}
