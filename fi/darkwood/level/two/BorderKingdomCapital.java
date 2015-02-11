/*
 * HammersburgZone.java
 *
 * Created on May 17, 2007, 7:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.level.two;

import fi.darkwood.EquipmentFactory;
import fi.darkwood.Game;
import fi.darkwood.GameConstants;
import fi.darkwood.Potion;
import fi.darkwood.Quest;
import fi.darkwood.room.Room;
import fi.darkwood.room.ShopRoom;
import fi.darkwood.room.MapRoom;
import fi.darkwood.room.RoomFactory;
import fi.darkwood.Zone;
import fi.darkwood.level.six.quest.QuestKillCurmu;
import fi.darkwood.level.three.quest.QuestKillBaron;
import fi.darkwood.level.two.quest.QuestKillBats;
import fi.darkwood.level.two.quest.QuestKillLich;
import fi.darkwood.room.CityRoom;
import fi.mirake.Local;

/**
 *
 * @author Teemu Kivim�ki
 * redone by Ville
 */
public class BorderKingdomCapital extends Zone {

    RoomFactory roomFactory;

    /**
     * Creates a new instance of HammersburgZone
     */
    public BorderKingdomCapital() {
        super(Local.get("Border Kingdoms Capital"), "/images/background/darkwood_town/background.png");

        firstQuest = new QuestKillBats();
   //     firstQuest.setReward(new ShiningPlateMail());

        if (Game.player.completedQuests.contains((Quest) new QuestKillBaron())) {
            if (Game.player.completedQuests.contains((Quest) new QuestKillCurmu())) {
                // The End
                allQuestsDoneMessage = Local.get("allQuestsDoneMessage1");
            } else {
                // direct the player to the plaguelands
                allQuestsDoneMessage =  Local.get("allQuestsDoneMessage2");
            }
        } else {
            allQuestsDoneMessage =  Local.get("allQuestsDoneMessage3");

        }
        roomFactory = new RoomFactory();

        System.out.println("Capital instanced: " + this.background);


        CityRoom entr = (CityRoom) roomFactory.constructCityEntrance(this, "center", "/images/background/tier2_city/background_day.png");

        // if player has killed baron, but has not yet killed Cu'rmu
        if (Game.player.completedQuests.contains((Quest) new QuestKillBaron()) &&
                !Game.player.completedQuests.contains((Quest) new QuestKillCurmu())) {
            entr.setBackground("/images/background/tier2_city/background_day_empty.png");
        } else if (Game.player.completedQuests.contains((Quest) new QuestKillLich()) && // player has killed lich and curmu
                Game.player.completedQuests.contains((Quest) new QuestKillCurmu())) {

            entr.setEyesSpriteImage("/images/background/tier2_city/eyes_frames.png", 16, 49, 101);
            entr.setEyesSpriteFrameTime(new int[]{20, 1, 20, 20});
        }

        // player has not killed lich or baron - night bg
        if (!Game.player.completedQuests.contains((Quest) new QuestKillBaron()) &&
                !Game.player.completedQuests.contains((Quest) new QuestKillLich())) {
            entr.setSpriteImage("/images/background/tier2_city/background_night.png", 176, 0, 29);
            entr.setSpriteFrameTime(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        }

        entr.cityDesc = Local.get("Welcome to Border Kingdoms Capital");

        Room maproom = roomFactory.constructMapRoomSouth(this, entrance, "maproom");

        crossroads = (MapRoom) maproom;

        crossroads.setCoordinates(319, 391, "/images/map/icons/city capital.png");

        roomFactory.constructStatRoomNorth(this, entrance, "Trainer", "trainer");
        roomFactory.constructTavernRoomEast(this, entrance, "Tavern", "tavern");

        ShopRoom shop = roomFactory.constructShopRoomWest(this, entrance, "Smithy", "shop");
        shop.setWelcomeText(Local.get("capital.shop.welcome"));

        int type = GameConstants.getArmorTypeforClass(Game.player.characterClass);
        EquipmentFactory eqf = EquipmentFactory.getInstance();
        shop.addItem(eqf.createEquipment(6, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(9, GameConstants.SLOT_CHEST, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(6, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(10, GameConstants.SLOT_RIGHT_HAND, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(6, GameConstants.SLOT_LEFT_HAND, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(9, GameConstants.SLOT_LEFT_HAND, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(eqf.createEquipment(5, GameConstants.SLOT_HEAD, type, GameConstants.QUALITY_COMMON));
        shop.addItem(eqf.createEquipment(6, GameConstants.SLOT_HEAD, type, GameConstants.QUALITY_UNCOMMON));
        shop.addItem(eqf.createEquipment(10, GameConstants.SLOT_HEAD, type, GameConstants.QUALITY_UNCOMMON));

        shop.addItem(new Potion());

    }
    public MapRoom crossroads;
    private boolean zonesLoaded = false;

    public void resetZone() {
        // clear the zones array
        crossroads.zones.removeAllElements();
        crossroads.addZone("/fi/darkwood/level/two/Sewers.xml", 302, 401, this.getClass().getName(), "/images/map/icons/sewers.png");
        // Tier3 baron needs to be killed first ++Ville
        if (Game.player.completedQuests.contains((Quest) new QuestKillBaron())) {
            crossroads.addZone("/fi/darkwood/level/two/RoadToMountains.xml", 401, 289, this.getClass().getName(), "/images/map/icons/mountain pass.png");
        }
        // Lich of this tier needs to be killed first
        if (Game.player.completedQuests.contains((Quest) new QuestKillLich())) {
            crossroads.addZone("/fi/darkwood/level/two/RoadToVillage.xml", 174, 345, this.getClass().getName(), "/images/map/icons/village.png");
        }
        crossroads.addZone("/fi/darkwood/level/one/ForestRoadBack.xml", 243, 504, this.getClass().getName(), "/images/map/icons/darkwood_hut.png");
        crossroads.addZone("/fi/darkwood/level/two/Catacombs.xml", 354, 397, this.getClass().getName(), "/images/map/icons/dungeon (castle).png");
        crossroads.addZone("/fi/darkwood/level/two/PartyCrypt.xml", 312, 370, this.getClass().getName(), "/images/map/icons/dungeon (castle).png");
    }

    public String getDescription() {
        return Local.get("Border Kingdoms Capital");
    }
}
