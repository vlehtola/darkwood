/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.three.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.three.monsters.Bandit;
import fi.mirake.Local;

/**
 *
 * @author Ville
 *
 */
public class QuestKillBandits extends Quest {

    public QuestKillBandits() {
        super(Local.get("tier3.questkillbandits"), 10);
   
        setCompletedText(Local.get("tier3.completekillbandits"));
        addKillRequirement(new Bandit(), 20);
        setNextQuest(new QuestKillBanditLeader());
    }
}
