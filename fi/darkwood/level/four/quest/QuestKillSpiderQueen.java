/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.four.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.four.monsters.SpiderQueen;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillSpiderQueen extends Quest {

    public QuestKillSpiderQueen() {
        super(Local.get("tier4.questkillspiderqueen"), 17);

        setCompletedText(Local.get("tier4.completekillspiderqueen"));
        addKillRequirement(new SpiderQueen(), 1);
    }

}
