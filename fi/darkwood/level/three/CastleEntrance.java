/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.three;

import fi.darkwood.Game;
import fi.darkwood.Quest;
import fi.darkwood.Zone;
import fi.darkwood.level.three.quest.QuestKillQuartermaster;
import fi.darkwood.room.MapRoom;
import fi.darkwood.room.RoomFactory;

/**
 * This zone consists of a maproom, which leads forwards. no fallbacking here.
 * @author Ville
 */
public class CastleEntrance extends Zone {

    RoomFactory roomFactory;

    /**
     * Creates a new instance 
     */
    public CastleEntrance() {
        super("Castle entrance", "/images/background/darkwood_town/background.png");
        roomFactory = new RoomFactory();

        System.out.println("Castle entrance instanced: " + this.background);

        roomFactory.constructMapRoomEntrance(this, "Castle entrance");

        crossroads = (MapRoom) entrance;

        crossroads.setCoordinates(175, 297, "/images/map/icons/castle above village.png");
        //crossroads.addCityZone("fi.darkwood.level.three.VillageZone", 174, 345, "fi.darkwood.level.three.VillageZone", "fi.darkwood.level.three.VillageZone");

        // This might seem like a hack, but it disables soft1 nullpointer in mapview.
        crossroads.exit = crossroads;

        // add zones and load them
        resetZone();
    }
    public MapRoom crossroads;

    public void resetZone() {
        crossroads.addZone("/fi/darkwood/level/three/CastleBarracks.xml", 177, 250, "fi.darkwood.level.three.VillageZone", "/images/map/icons/castle_barracks.png");

        // Load the zones for this city
        if (Game.player.completedQuests.contains((Quest) new QuestKillQuartermaster())) {
            crossroads.addZone("/fi/darkwood/level/three/CastleKeep.xml", 125, 250, "fi.darkwood.level.three.VillageZone", "/images/map/icons/castle_baron.png");
        }
        crossroads.addZone("/fi/darkwood/level/three/PartyCastleDungeon.xml", 101, 296, "fi.darkwood.level.three.VillageZone", "/images/map/icons/dungeon (castle).png");

    }

    public String getDescription() {
        return "Castle entrance";
    }
}
