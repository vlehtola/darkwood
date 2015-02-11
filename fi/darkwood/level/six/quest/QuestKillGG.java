/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.six.quest;

import fi.darkwood.Quest;
import fi.darkwood.level.six.monsters.*;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillGG extends Quest {

    public QuestKillGG() {
        super(Local.get("tier6.quest2"),26);

        setCompletedText(Local.get("tier6.quest2.complete"));
        addKillRequirement(new Gargoyle(), 15);
        addKillRequirement(new StoneGolem(), 15);
        setNextQuest(new QuestKillWarlord());
    }
    
}
