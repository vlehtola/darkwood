/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.five.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.five.monsters.Barbarian;
import fi.darkwood.level.five.monsters.Hound;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillFirstBarbarians extends Quest {

    public QuestKillFirstBarbarians() {
        super(
                Local.get("tier5.quest2"), 19);

        setCompletedText(Local.get("tier5.quest2.complete"));
        addKillRequirement(new Barbarian(), 14);
        addKillRequirement(new Hound(), 8);
        setNextQuest(new QuestKillBarbarians());
    }
}
