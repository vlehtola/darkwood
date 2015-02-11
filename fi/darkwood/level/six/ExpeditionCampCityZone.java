/*
 * DarkwoodCityZone.java
 *
 * Created on May 17, 2007, 7:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.level.six;

import fi.darkwood.level.six.quest.*;
import fi.darkwood.EquipmentFactory;
import fi.darkwood.Game;
import fi.darkwood.GameConstants;
import fi.darkwood.Potion;
import fi.darkwood.Quest;
import fi.darkwood.room.MapRoom;
import fi.darkwood.room.RoomFactory;
import fi.darkwood.room.ShopRoom;
import fi.darkwood.Zone;
import fi.darkwood.room.CityRoom;
import fi.mirake.Local;

/**
 *
 * @author Teemu
 * Zones+quests: Ville
 */
public class ExpeditionCampCityZone extends Zone {

    private RoomFactory roomFactory;

    /** Creates a new instance of DarkwoodCityZone */
    public ExpeditionCampCityZone() {
        super("Expedition Camp", "/images/background/tier6/expedition_camp.png");

        firstQuest = new QuestKillSkeletons();

        allQuestsDoneMessage = Local.get("tier6.completed");
        roomFactory = new RoomFactory();

        CityRoom entr = (CityRoom) roomFactory.constructCityEntrance(this, "center", "/images/background/tier6/expedition_camp.png");
        //72,26
        entr.setEyesSpriteImage("/images/background/tier6/death camp a2  104x80.png", 104, 72, 54);
        entr.setEyesSpriteFrameTime(new int[]{3, 3, 3, 3});

//        entr.setSpriteImage("/images/background/tier6/death camp a1 34x26.png", 34, 29, 70);
        //      entr.setSpriteFrameTime(new int[]{1, 1, 2, 1});

        maproom = (MapRoom) roomFactory.constructMapRoomSouth(this, entrance, "Map");

        maproom.setCoordinates(547, 299, "/images/map/icons/expedition camp.png");
//        this.setIcon(icon);

        roomFactory.constructStatRoomNorth(this, entrance, "Trainer", "trainer");
        roomFactory.constructTavernRoomEast(this, entrance, "Tavern", "tavern");

        ShopRoom shop = roomFactory.constructShopRoomWest(this, entrance, "Smithy", "shop");

        //  shop.setWelcomeText("Welcome to the expedition camp smithy. Here you may " + "buy new equipment and replenish potions.");
        shop.setWelcomeText(Local.get("tier6.shop.welcome"));

        int type = GameConstants.getArmorTypeforClass(Game.player.characterClass);
        EquipmentFactory eqf = EquipmentFactory.getInstance();
        shop.addItem(eqf.createEquipment(25, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(26, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(28, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_RARE));

        shop.addItem(eqf.createEquipment(25, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(27, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(27, GameConstants.SLOT_LEFT_HAND, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(25, GameConstants.SLOT_HEAD, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(28, GameConstants.SLOT_HEAD, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(new Potion());


    }
    public MapRoom maproom;

    public void resetZone() {

        // clear the zones array
        maproom.zones.removeAllElements();


        // Load the zones for this city

        // partycrypta oikeeseen paikkaan?
        maproom.addZone("/fi/darkwood/level/six/Desert.xml", 632, 287, this.getClass().getName(), "/images/map/icons/Crypt (catacombs).png");
        maproom.addZone("/fi/darkwood/level/six/PartyCrypt.xml", 611, 434, this.getClass().getName(), "/images/map/icons/Crypt (catacombs).png");
        Quest reqQuest = (Quest) new QuestKillGG();
        if (GameConstants.TESTINGMODE || Game.player.completedQuests.contains(reqQuest)) {
            maproom.addZone("/fi/darkwood/level/six/WarlordDesert.xml", 556, 349, this.getClass().getName(), "/images/map/icons/undead_army.png");
        }
        maproom.addZone("/fi/darkwood/level/six/RoadToMountainPass.xml", 401, 289, this.getClass().getName(), "/images/map/icons/mountain pass.png");

    }

    public String getDescription() {
        return "Expedition camp";
    }
}
