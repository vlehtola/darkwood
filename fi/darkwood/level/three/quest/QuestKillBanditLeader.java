/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.three.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.three.monsters.BanditLeader;
import fi.mirake.Local;

/**
 *
 * @author Ville
 *
 */
public class QuestKillBanditLeader extends Quest {

    public QuestKillBanditLeader() {
        super(Local.get("tier3.questkillbanditleader"), 11);
        setCompletedText(Local.get("tier3.completekillbanditleader"));
        addKillRequirement(new BanditLeader(), 1);
        setNextQuest(new QuestKillFencingGuards());
    }
}
