/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.four.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.four.monsters.SpiderSmall;
import fi.mirake.Local;

/**
 *
 * @author Administrator
 */
public class QuestKillSpiders extends Quest {

    public QuestKillSpiders() {
        super(Local.get("tier4.questkillspiders"), 15);

        setCompletedText(Local.get("tier4.completekillspiders"));
        addKillRequirement(new SpiderSmall(), 15);
        setNextQuest(new QuestKillSpiders2());
    }
    
}
