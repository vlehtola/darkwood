/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.two.quest;

import fi.darkwood.EquipmentFactory;
import fi.darkwood.Quest;
import fi.mirake.Local;

/**
 *  The final quest. The final reward. The End.
 *
 * @author Ville
 */
public class QuestTheEnd extends Quest {
   public QuestTheEnd() {
        super(Local.get("tier2.quest5"), 30);

        setCompletedText(Local.get("tier2.quest5.complete"));
        //this.setReward(EquipmentFactory.getInstance().createEquipment(32, 2));
    }

}
