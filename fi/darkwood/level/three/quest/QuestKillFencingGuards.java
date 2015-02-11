/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.three.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.three.monsters.FencingGuard;
import fi.mirake.Local;

/**
 *
 * @author Ville
 *
 */
public class QuestKillFencingGuards extends Quest {

    public QuestKillFencingGuards() {
        super(Local.get("tier3.questkillfencingguards"), 12);
              

        setCompletedText(Local.get("tier3.completekillfencingguards"));
        addKillRequirement(new FencingGuard(), 10);
        setNextQuest(new QuestKillHalberdiers());
    }
}
