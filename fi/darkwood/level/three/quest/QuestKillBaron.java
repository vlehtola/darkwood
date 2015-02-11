/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.three.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.three.monsters.Baron;
import fi.mirake.Local;

/**
 *
 * @author Ville
 *
 */
public class QuestKillBaron extends Quest {

    public QuestKillBaron() {
        super(Local.get("tier3.questkillbaron"), 15);
              
        setCompletedText(Local.get("tier3.completekillbaron"));
        addKillRequirement(new Baron(), 1);
       //setNextQuest(new QuestKillBaron());
    }
}
