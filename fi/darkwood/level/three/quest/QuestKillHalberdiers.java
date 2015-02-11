/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.three.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.three.monsters.Halberdier;
import fi.mirake.Local;

/**
 *
 * @author Ville
 *
 */
public class QuestKillHalberdiers extends Quest {

    public QuestKillHalberdiers() {
        super(Local.get("tier3.questkillhalberdiers"), 13);
   

        setCompletedText(Local.get("tier3.completekillhalberdiers"));
        addKillRequirement(new Halberdier(), 20);
        setNextQuest(new QuestKillQuartermaster());
    }
}
