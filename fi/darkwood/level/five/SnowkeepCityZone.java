/*
 * DarkwoodCityZone.java
 *
 * Created on May 17, 2007, 7:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.level.five;

import fi.darkwood.EquipmentFactory;
import fi.darkwood.Game;
import fi.darkwood.GameConstants;
import fi.darkwood.Potion;
import fi.darkwood.Quest;
import fi.darkwood.room.MapRoom;
import fi.darkwood.room.RoomFactory;
import fi.darkwood.room.ShopRoom;
import fi.darkwood.Zone;
import fi.darkwood.level.five.quest.QuestKillBarbarianBoss;
import fi.darkwood.level.five.quest.QuestKillBarbarians;
import fi.darkwood.level.five.quest.QuestKillFirstBarbarians;
import fi.darkwood.room.CityRoom;
import fi.mirake.Local;

/**
 *
 * @author Teemu
 *  zones&quests by Ville
 */
public class SnowkeepCityZone extends Zone {

    private RoomFactory roomFactory;

    /** Creates a new instance of DarkwoodCityZone */
    public SnowkeepCityZone() {
        super("Mountain pass", "/images/background/tier4/mountain_pass.png");


        firstQuest = new QuestKillFirstBarbarians();

        allQuestsDoneMessage = Local.get("tier5.completed");


        roomFactory = new RoomFactory();

        CityRoom entr = (CityRoom) roomFactory.constructCityEntrance(this, "center", "/images/background/tier5/city/snowkeep.png");
        entr.setSpriteImage("/images/background/tier5/city/Snow Keep girl animation.png", 69, 1, 95);
        entr.setSpriteFrameTime(new int[]{20, 1, 20, 1, 10, 1, 20, 1});
        if (GameConstants.TESTINGMODE || Game.player.completedQuests.contains((Quest) new QuestKillBarbarians())) {
            // "eyes". an 'if' to check camp destroyed quest. ++Ville
            entr.setEyesSpriteImage("/images/background/tier5/city/tents master 30x18 3 frames.png", 30, 77, 66);
            entr.setEyesSpriteFrameTime(new int[]{1, 1, 1});
        }
        if (GameConstants.TESTINGMODE || Game.player.completedQuests.contains((Quest) new QuestKillBarbarianBoss())) {
            entr.setSndSpriteImage("/images/background/tier5/city/catapults master 15x18 3 frames.png", 15, 59, 39);
            entr.setSndSpriteFrameTime(new int[]{1, 1, 1});
        }

        maproom = (MapRoom) roomFactory.constructMapRoomSouth(this, entrance, "Map");

        maproom.setCoordinates(306, 261, "/images/map/icons/snow_keep.png");
//        this.setIcon(icon);

        /*
        maproom.addZone("/fi/darkwood/level/one/VillageFields.xml", 241, 483, this.getClass().getName(), "/images/map/icons/fields.png");
        maproom.addZone("/fi/darkwood/level/one/DarkForest.xml", 187, 492, this.getClass().getName(), "/images/map/icons/wolves.png");
        maproom.addZone("/fi/darkwood/level/one/OmniousForest.xml", 231, 557, this.getClass().getName(), "/images/map/icons/witch.png");
        maproom.addZone("/fi/darkwood/level/one/PartyForest.xml", 297,519, this.getClass().getName(), "/images/map/icons/wolves.png");
        maproom.addZone("/fi/darkwood/level/one/ForestRoad.xml", 319, 391, this.getClass().getName(), "/images/map/icons/city capital.png");
         */
        roomFactory.constructStatRoomNorth(this, entrance, "Trainer", "trainer");
        roomFactory.constructTavernRoomEast(this, entrance, "Tavern", "tavern");

        ShopRoom shop = roomFactory.constructShopRoomWest(this, entrance, "Smithy", "shop");

//        shop.setWelcomeText("Welcome to the Snow Keep smithy. Here you may " +
        //              "buy new equipment and replenish potions.");
        shop.setWelcomeText(Local.get("tier5.shop.welcome"));

        int type = GameConstants.getArmorTypeforClass(Game.player.characterClass);
        EquipmentFactory eqf = EquipmentFactory.getInstance();
        shop.addItem(eqf.createEquipment(20, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(24, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(20, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(23, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(23, GameConstants.SLOT_LEFT_HAND, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(21, GameConstants.SLOT_HEAD, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(24, GameConstants.SLOT_HEAD, type, GameConstants.QUALITY_UNCOMMON));


        shop.addItem(new Potion());


    }
    public MapRoom maproom;

    public void resetZone() {

        // clear the zones array
        maproom.zones.removeAllElements();


        // Load the zones for this city

        maproom.addZone("/fi/darkwood/level/five/Camp.xml", 239, 231, this.getClass().getName(), "/images/map/icons/expedition camp.png");
        maproom.addZone("/fi/darkwood/level/five/SiegeMachines.xml", 296, 213, this.getClass().getName(), "/images/map/icons/siege_machines.png");
        if (Game.player.completedQuests.contains((Quest) new QuestKillBarbarianBoss())) {
            maproom.addZone("/fi/darkwood/level/five/IceTower.xml", 274, 188, this.getClass().getName(), "/images/map/icons/snow_keep.png");
        }
        maproom.addZone("/fi/darkwood/level/five/RoadToMountainPass.xml", 401, 289, this.getClass().getName(), "/images/map/icons/mountain pass.png");

    }

    public String getDescription() {
        return "Mountain pass";
    }
}
