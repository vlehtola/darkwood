/*
 * DarkwoodCityZone.java
 *
 * Created on May 17, 2007, 7:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.level.one;

import fi.darkwood.EquipmentFactory;
import fi.darkwood.Game;
import fi.darkwood.GameConstants;
import fi.darkwood.Potion;
import fi.darkwood.Quest;
import fi.darkwood.room.Room;
import fi.darkwood.room.MapRoom;
import fi.darkwood.room.RoomFactory;
import fi.darkwood.room.ShopRoom;
import fi.darkwood.Zone;
import fi.darkwood.level.one.quest.QuestKillWolves;
import fi.darkwood.room.CityRoom;
import fi.mirake.Local;

/**
 *
 * @author Ville, Teemu
 */
public class DarkwoodCityZone extends Zone {

    private RoomFactory roomFactory;

    /** Creates a new instance of DarkwoodCityZone */
    public DarkwoodCityZone() {
        super("Darkwood", "/images/background/darkwood_town/background.png");

        firstQuest = new QuestKillWolves();
  //      firstQuest.setReward(new ShiningPlateMail());

        allQuestsDoneMessage = Local.get("tier1.completed");
        roomFactory = new RoomFactory();

        CityRoom entr = (CityRoom) roomFactory.constructCityEntrance(this, "center", "/images/background/tier1_city/background.png");
        entr.setSpriteImage("/images/background/tier1_city/lantern_frames.png", 6, 63, 88);
        entr.setSpriteFrameTime(new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10});
        entr.setEyesSpriteImage("/images/background/tier1_city/eyes_frames.png", 13, 113, 115);
        entr.setEyesSpriteFrameTime(new int[]{20, 1, 20, 1, 10, 1});


        //        crossroads=roomFactory.ConstructRoomSouth(this,entrance,"crossroads","forest");
        entr.cityDesc = Local.get("Welcome to town of Darkwood");


        Room maproom = roomFactory.constructMapRoomSouth(this, entrance, "Map");

        crossroads = (MapRoom) maproom;
        crossroads.setCoordinates(243, 504, "/images/map/icons/darkwood_hut.png");


        roomFactory.constructStatRoomNorth(this, entrance, "Trainer", "trainer");

//#ifndef DARKWOOD_DEMOVERSION
        roomFactory.constructTavernRoomEast(this, entrance, "Tavern", "tavern");
//#endif

        ShopRoom shop = roomFactory.constructShopRoomWest(this, entrance, "Smithy", "shop");

        shop.setWelcomeText(Local.get("tier1.shop.welcome"));

        int type = GameConstants.getArmorTypeforClass(Game.player.characterClass);
        EquipmentFactory eqf = EquipmentFactory.getInstance();
        shop.addItem(eqf.createEquipment(2, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_COMMON));
        shop.addItem(eqf.createEquipment(2, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(4, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(3, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_COMMON));
        shop.addItem(eqf.createEquipment(4, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(3, GameConstants.SLOT_LEFT_HAND, type, GameConstants.QUALITY_COMMON));

        shop.addItem(new Potion());


    }
    public MapRoom crossroads;

    /** Reset the city (add and reload all the zones that belong to this tier)
     * This is called by cityView everytime the player moves here.
     *
     */
    public void resetZone() {
        System.out.println("In darkwoodcityzone resetZone()");

        System.out.println("Before removeAllElements, vector size: " + crossroads.zones.size());

        // clear the zones array
        crossroads.zones.removeAllElements();
        System.out.println("After clearing vector, size: " + crossroads.zones.size());


        // Load the zones for this city
        crossroads.addZone("/fi/darkwood/level/one/VillageFields.xml", 238, 462, this.getClass().getName(), "/images/map/icons/fields.png");
        crossroads.addZone("/fi/darkwood/level/one/DarkForest.xml", 187, 492, this.getClass().getName(), "/images/map/icons/wolves.png");
        crossroads.addZone("/fi/darkwood/level/one/OmniousForest.xml", 231, 557, this.getClass().getName(), "/images/map/icons/witch.png");
        crossroads.addZone("/fi/darkwood/level/one/PartyForest.xml", 297, 519, this.getClass().getName(), "/images/map/icons/wolves.png");
        // QuestKillWitch() as final requirement. ++Ville
        Quest reqQuest = (Quest) new QuestKillWolves();
        if (Game.player.completedQuests.contains(reqQuest)) {
            //||(Game.player.currentQuest != null && Game.player.currentQuest.equals(reqQuest) && Game.player.currentQuest.isQuestDone())) {
            crossroads.addZone("/fi/darkwood/level/one/ForestRoad.xml", 319, 391, this.getClass().getName(), "/images/map/icons/city capital.png");
            System.out.println("ADDED QUESTZONE2!");
        }
        //    }
    }

    public String getDescription() {
        return Local.get("Town of Darkwood");
    }
}
