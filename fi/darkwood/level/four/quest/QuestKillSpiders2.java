/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.four.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.four.monsters.Spider;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillSpiders2 extends Quest {

    public QuestKillSpiders2() {
        super(Local.get("tier4.questkillspiders2"), 16);

        setCompletedText(Local.get("tier4.completekillspiders2"));
        addKillRequirement(new Spider(), 15);
        setNextQuest(new QuestKillSpiderQueen());
    }

}
