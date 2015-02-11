/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.level.five.quest;
import fi.darkwood.Quest;
import fi.darkwood.level.five.monsters.IceQueen;
import fi.darkwood.level.five.monsters.PolarBear;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class QuestKillIceQueen extends Quest {
public QuestKillIceQueen() {
        super(
                Local.get("tier5.quest4"),25);

        setCompletedText(Local.get("tier5.quest4.complete"));
        addKillRequirement(new PolarBear(), 3);
        addKillRequirement(new IceQueen(), 1);

    }
}
