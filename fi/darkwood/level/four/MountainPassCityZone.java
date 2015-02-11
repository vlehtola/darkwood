/*
 * DarkwoodCityZone.java
 *
 * Created on May 17, 2007, 7:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.level.four;

import fi.darkwood.level.four.quest.QuestKillSpiders;
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
import fi.darkwood.room.CityRoom;
import fi.mirake.Local;

/**
 *
 * @author Teemu
 */
public class MountainPassCityZone extends Zone {

    private RoomFactory roomFactory;

    /** Creates a new instance of DarkwoodCityZone */
    public MountainPassCityZone() {
        super(Local.get("Mountain pass"), "/images/background/tier4/mountain_pass.png");

        firstQuest = new QuestKillSpiders();

        if (Game.player.completedQuests.contains((Quest) new QuestKillBarbarianBoss())) {
            allQuestsDoneMessage = Local.get("tier4.allquestsdone1");
        } else {
            allQuestsDoneMessage = Local.get("tier4.allquestsdone2");
       }
        roomFactory = new RoomFactory();

        CityRoom entr = (CityRoom) roomFactory.constructCityEntrance(this, "center", "/images/background/tier4/mountain_pass.png");
        entr.setSpriteImage("/images/background/tier4/Mountain pass girl 13x16 6 frames.png", 13, 113, 115);
        entr.setSpriteFrameTime(new int[]{10, 2, 10, 2, 10, 2});
        //entr.setEyesSpriteImage("/images/background/tier1_city/eyes_frames.png", 13, 113, 115);
        //entr.setEyesSpriteFrameTime( new int[] { 20, 1, 20, 1, 10, 1} );



        maproom = (MapRoom) roomFactory.constructMapRoomSouth(this, entrance, "Map");

        maproom.setCoordinates(401, 289, "/images/map/icons/mountain pass.png");
//        this.setIcon(icon);


        roomFactory.constructStatRoomNorth(this, entrance, "Trainer", "trainer");
        roomFactory.constructTavernRoomEast(this, entrance, "Tavern", "tavern");

        ShopRoom shop = roomFactory.constructShopRoomWest(this, entrance, "Smithy", "shop");

        shop.setWelcomeText(Local.get("tier4.shopwelcome"));
  

        int type = GameConstants.getArmorTypeforClass(Game.player.characterClass);
        EquipmentFactory eqf = EquipmentFactory.getInstance();
        shop.addItem(eqf.createEquipment(15, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(18, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(19, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_RARE));

        shop.addItem(eqf.createEquipment(16, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(19, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(16, GameConstants.SLOT_LEFT_HAND, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(16, GameConstants.SLOT_HEAD, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(19, GameConstants.SLOT_HEAD, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(new Potion());


    }
    public MapRoom maproom;

    public void resetZone() {

        // clear the zones array
        maproom.zones.removeAllElements();

        // Load the zones for this city

        // Tier5 must first be completed to enter the tier6
        maproom.addZone("/fi/darkwood/level/four/Mine.xml", 430, 245, this.getClass().getName(), "/images/map/icons/dungeon (castle).png");
        if (GameConstants.TESTINGMODE || Game.player.completedQuests.contains((Quest) new QuestKillBarbarianBoss())) {
            maproom.addZone("/fi/darkwood/level/four/RoadToExpeditionCamp.xml", 547, 299, this.getClass().getName(), "/images/map/icons/expedition camp.png");
        }
        maproom.addZone("/fi/darkwood/level/four/SpiderCave.xml", 430, 334, this.getClass().getName(), "/images/map/icons/caves.png");
        maproom.addZone("/fi/darkwood/level/four/PartyMines.xml", 408, 373, this.getClass().getName(), "/images/map/icons/dungeon (castle).png");
        maproom.addZone("/fi/darkwood/level/four/RoadToCapital.xml", 319, 391, this.getClass().getName(), "/images/map/icons/city capital.png");
        maproom.addZone("/fi/darkwood/level/four/RoadToSnowKeep.xml", 306, 261, this.getClass().getName(), "/images/map/icons/snow_keep.png");
    }

    public String getDescription() {
        return Local.get("Mountain pass");
    }
}
