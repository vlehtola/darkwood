/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.level.three;

import fi.darkwood.EquipmentFactory;
import fi.darkwood.Game;
import fi.darkwood.GameConstants;
import fi.darkwood.Potion;
import fi.darkwood.Zone;
import fi.darkwood.level.three.quest.QuestKillBandits;
import fi.darkwood.room.CityRoom;
import fi.darkwood.room.MapRoom;
import fi.darkwood.room.Room;
import fi.darkwood.room.RoomFactory;
import fi.darkwood.room.ShopRoom;
import fi.mirake.Local;

/**
 *
 * @author Ville
 */
public class VillageZone extends Zone {

    RoomFactory roomFactory;

    /**
     * Creates a new instance 
     */
    public VillageZone() {
        super(Local.get("Ravenstone village"), "/images/background/ravenstone/background.png");

        roomFactory = new RoomFactory();

        System.out.println("VillageZone instanced: " + this.background);

        firstQuest = new QuestKillBandits();
    //    firstQuest.setReward(new ShiningPlateMail());

        allQuestsDoneMessage = "tier3.questsDone";

        CityRoom entr = (CityRoom) roomFactory.constructCityEntrance(this, "center", "/images/background/tier3_city/background.png");

        // set animation to the background
        entr.setEyesSpriteImage("/images/background/tier3_city/eyes_frames.png", 9, 102, 111);
        entr.setEyesSpriteFrameTime(new int[]{20, 1, 20, 20});

        entr.cityDesc = Local.get("tier3.enter");

        Room maproom = roomFactory.constructMapRoomSouth(this, entrance, "maproom");

        crossroads = (MapRoom) maproom;

        crossroads.setCoordinates(174, 345, "/images/map/icons/village.png");
        //crossroads.addCityZone("fi.darkwood.level.one.DarkwoodCityZone", 433, 440, this.getClass().getName(), "fi.darkwood.level.one.DarkwoodCityZone");
 

        roomFactory.constructStatRoomNorth(this, entrance, "Trainer", "trainer");
        roomFactory.constructTavernRoomEast(this, entrance, "Tavern", "tavern");

        ShopRoom shop = roomFactory.constructShopRoomWest(this, entrance, "Smithy", "shop");
        shop.setWelcomeText("tier3.shopWelcome");
        int type = GameConstants.getArmorTypeforClass(Game.player.characterClass);
        EquipmentFactory eqf = EquipmentFactory.getInstance();
        shop.addItem(eqf.createEquipment(10, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(14, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(11, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(14, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(15, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_RARE));

        shop.addItem(eqf.createEquipment(11, GameConstants.SLOT_LEFT_HAND, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(14, GameConstants.SLOT_LEFT_HAND, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(10, GameConstants.SLOT_HEAD, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(13, GameConstants.SLOT_HEAD, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(new Potion());
    }
    public MapRoom crossroads;

    public void resetZone() {
        // clear the zones array
        crossroads.zones.removeAllElements();

        // add zones
        crossroads.addZone("/fi/darkwood/level/three/Woods.xml", 148, 361, this.getClass().getName(), "/images/map/icons/expedition camp.png");
        crossroads.addZone("/fi/darkwood/level/two/RoadToVillageBack.xml", 319, 391, this.getClass().getName(), "/images/map/icons/city capital.png");

        crossroads.addZone("/fi/darkwood/level/three/RoadToKeep.xml", 175, 297, this.getClass().getName(), "/images/map/icons/castle above village.png");
    }

    public String getDescription() {
        return Local.get("Ravenstone village");
    }
}
