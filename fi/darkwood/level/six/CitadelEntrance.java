/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.six;

import fi.darkwood.Game;
import fi.darkwood.GameConstants;
import fi.darkwood.Quest;
import fi.darkwood.Zone;
import fi.darkwood.level.six.quest.QuestFreeQueen;
import fi.darkwood.level.six.quest.QuestKillGG;
import fi.darkwood.level.six.quest.QuestKillMummies;
import fi.darkwood.room.MapRoom;
import fi.darkwood.room.RoomFactory;

/**
 * This zone consists of a maproom, which leads forwards. no fallbacking here.
 * @author Ville
 */
public class CitadelEntrance extends Zone {

    RoomFactory roomFactory;

    /**
     * Creates a new instance 
     */
    public CitadelEntrance() {
        super("Citadel entrance", "/images/background/darkwood_town/background.png");
        roomFactory = new RoomFactory();

        System.out.println("Citadel entrance instanced: " + this.background);

        roomFactory.constructMapRoomEntrance(this, "Citadel entrance");

        citadelEntrance = (MapRoom) entrance;

        citadelEntrance.setCoordinates(627, 291, "/images/map/icons/Crypt (catacombs).png");
        //crossroads.addCityZone("fi.darkwood.level.three.VillageZone", 174, 345, "fi.darkwood.level.three.VillageZone", "fi.darkwood.level.three.VillageZone");

        // This might seem like a hack, but it disables soft1 nullpointer in mapview.
        citadelEntrance.exit = citadelEntrance;

        // add zones and load them
        resetZone();
    }
    public MapRoom citadelEntrance;

    public void resetZone() {
        Quest reqQuest = (Quest) new QuestKillGG();
        citadelEntrance.addZone("/fi/darkwood/level/six/AncientTomb.xml", 620, 328, "fi.darkwood.level.six.ExpeditionCampCityZone", "/images/map/icons/skeleton_camp.png");
        if (GameConstants.TESTINGMODE || Game.player.completedQuests.contains(reqQuest)) {
            // Queen rescue optional mission zone
            if (GameConstants.TESTINGMODE || (Game.player.currentQuest != null && Game.player.currentQuest.equals(new QuestFreeQueen()))) {
                citadelEntrance.addZone("/fi/darkwood/level/six/PyramidWithQueen.xml", 634, 250, "fi.darkwood.level.six.ExpeditionCampCityZone", "/images/map/icons/Pyramids ico.png");
            } else {
                citadelEntrance.addZone("/fi/darkwood/level/six/Pyramid.xml", 634, 250, "fi.darkwood.level.six.ExpeditionCampCityZone", "/images/map/icons/Pyramids ico.png");
            }
        }


        reqQuest = (Quest) new QuestFreeQueen();
        if (GameConstants.TESTINGMODE || Game.player.completedQuests.contains(reqQuest)) {

            citadelEntrance.addZone("/fi/darkwood/level/six/InnerChamber.xml", 661, 290, "fi.darkwood.level.six.ExpeditionCampCityZone", "/images/map/icons/necropolis.png");
        }
    }

    public String getDescription() {
        return "Citadel entrance";
    }
}
