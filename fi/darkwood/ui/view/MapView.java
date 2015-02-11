/*
 * CombatView.java
 *
 * Created on 17. toukokuuta 2007, 2:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.ui.view;

import fi.darkwood.DarkwoodCanvas;
import fi.darkwood.DarkwoodGraphics;
import fi.darkwood.Game;
import fi.darkwood.Logger;
import fi.darkwood.room.MapRoom;
import fi.darkwood.Zone;
import fi.darkwood.room.Room;
import fi.darkwood.util.Utils;
import fi.mirake.Local;
import fi.mirake.SoundPlayer;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author Teemu Kivim�ki
 */
public class MapView extends ViewAndControls {

    Utils utils = Utils.getInstance();
    Game game;
    int selection = 0;
    Image img;
    Image mapImg;
    Image mapImg2;
    Image mapImg3;
    Image mapImg4;
    Image buttonsImg;

    public MapView(Game game, int width, int height) {
        super(width, height);
        this.game = game;
        try {
            // Load map in 4 parts. Dont use Utils class since we dont want to cache these images there (they are only needed here)
            mapImg = Image.createImage("/images/map/map_part1.png");
            mapImg2 = Image.createImage("/images/map/map_part2.png");
            mapImg3 = Image.createImage("/images/map/map_part3.png");
            mapImg4 = Image.createImage("/images/map/map_part4.png");
        } catch (Exception e) {
            Logger.getInstance().logToFile("Failed to load map part images. Error: " + e.getMessage());
        }

        buttonsImg = utils.getImage("/images/ui/frames_buttons.png");

        leftSoftKeyText = Local.get("buttons.back");
        rightSoftKeyText = Local.get("buttons.travel");

    }
    private float x = 200;
    private float y = 200;
    private int intX;
    private int intY;

    public void updateScreen(DarkwoodGraphics g) {
        MapRoom maproom = (MapRoom) Game.player.room;
//        g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.SIZE_SMALL, Font.STYLE_ITALIC));

        // first time init
        if (x == 0) {
            x = ((Zone) maproom.zones.elementAt(selection)).getX();
            y = ((Zone) maproom.zones.elementAt(selection)).getY();
        }

        String currentZoneDesc = "";

        if (maproom != null) {
            /*
            int x = width/2 - ((MapRoom)(game.player.room)).coordX;
            int y = height/2 - ((MapRoom)(game.player.room)).coordY;
             */
            int mapRoomX = maproom.coordX;
            int mapRoomY = maproom.coordY;

            Zone selectedZone = (Zone) maproom.zones.elementAt(selection);

            int targetZoneX = selectedZone.getX();
            int targetZoneY = selectedZone.getY();

            int targetX = mapRoomX + (targetZoneX - mapRoomX) / 2 - GAME_WIDTH / 2;
            int targetY = mapRoomY + (targetZoneY - mapRoomY) / 2 - GAME_HEIGHT / 2;

            float kulmakerroin_x = (targetX - x) / 5;
            float kulmakerroin_y = (targetY - y) / 5;

            //System.out.println("Kulmakerroin: " + kulmakerroin_x + ", " + kulmakerroin_y);

            x += kulmakerroin_x;
            y += kulmakerroin_y;


            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }

            intX = (int) x;
            intY = (int) y;

            // Need to replace drawregion with because drawregion is bugged on some phones (ex. nokia 6120) - Teemu 9.5
            // Also needed to cut map to 4 parts because map was too large for some phones (ex. LG LX600). - Teemu 10.5
            //            g.drawRegion(mapImg, intX, intY, GAME_WIDTH, GAME_HEIGHT, Sprite.TRANS_NONE, 0, 0, Graphics.LEFT | Graphics.TOP);

            // draw map (in 4 parts, since some phones cant handle the map as one image (too large)
            g.drawImage(mapImg, -intX, -intY, 0);
            g.drawImage(mapImg2, -intX + 352, -intY, 0);
            g.drawImage(mapImg3, -intX, -intY + 350, 0);
            g.drawImage(mapImg4, -intX + 352, -intY + 350, 0);

            //            g.drawImage(mapImg, -intX, -intY, 0);

            g.setColor(0xFFFFFF);
            //center (replaced by icon)
            // g.drawRect(mapRoomX - intX - 2, mapRoomY - intY - 2, 5, 5);

            // loop zones and draw paths to them
            
            Vector zones = maproom.zones;
            for (int i = 0; i < zones.size(); i++) {
                Zone zone = (Zone) zones.elementAt(i);
                g.setColor(0x8b4513); // color: saddle brown

                //g.drawRect(zone.getX() - intX - 2, zone.getY() - intY - 2, 5, 5);
                if (selection == i) {
                    currentZoneDesc = zone.getDescription();
                    g.setColor(0xFFFFFF);
                }
                g.drawLine(mapRoomX - intX, mapRoomY - intY, zone.getX() - intX, zone.getY() - intY);

                // draw icon for the zone
                g.drawImage(zone.getIcon(), zone.getX() - intX - zone.getIcon().getWidth() / 2, zone.getY() - intY - zone.getIcon().getHeight() / 2, 0);

                //    System.out.println("zonedesc:"+zone.getDescription());
                if (selection == i) {
                    currentZoneDesc = zone.getDescription();
                }

            }

            // Draw the center icon
            img = utils.getImage(maproom.getMapRoomIcon());
            g.drawImage(img, mapRoomX - intX - img.getWidth() / 2, mapRoomY - intY - img.getHeight() / 2 - 1, 0);

            g.setColor(0x000000);
            //    g.fillRect(0, GAME_HEIGHT - 18, GAME_WIDTH, 19);
            g.setColor(0xFFFFFF);
            g.drawText(currentZoneDesc, 10, GAME_HEIGHT - 30);
        }

        g.drawImage(buttonsImg, 0, 0, 0);
        drawSoftKeyTexts(g);
    }

    private void move(Zone zone) {

        Room destination = (Room) zone.rooms.elementAt(0);

        if (Game.party.isActive() && Game.party.isLeader) {
            Game.party.movePartyLocally(destination);
            // send area initialization to party members and move them to the first room of the area
            Game.party.initZoneForClients(zone.getZoneXmlFilename(), zone.getFallbackZone());

        } // Or adventuring alone?
        else if (!Game.party.isActive()) {
            Game.player.moveTo(destination.getId(), zone);
//            game.player.room.removeThing(game.player);
//            zone.entrance.addThing(game.player);
        } else { // The player is in a party and is not a connectionToLeader
            System.out.println("You are not the leader.");
        }

    }

    public void checkInput(int keyState) {
        MapRoom room = (MapRoom) (game.player.room);
        if (keyState == GameCanvas.DOWN_PRESSED) {
            System.out.println("Keystate: " + keyState + " and: " + GameCanvas.DOWN_PRESSED + " combined: " + (keyState & GameCanvas.DOWN_PRESSED));
            int areaCount = room.zones.size();
            selection += 1;
            if (selection > areaCount - 1) {
                selection = 0;
            }
        } else if (keyState == GameCanvas.UP_PRESSED) {
            System.out.println("Keystate2: " + (keyState & GameCanvas.DOWN_PRESSED));
            int areaCount = room.zones.size();
            selection -= 1;
            if (selection < 0) {
                selection = areaCount - 1;
            }
        } else if (keyState == GameCanvas.FIRE_PRESSED || keyState == DarkwoodCanvas.SOFTKEY2_PRESSED) {

            Zone zone = (Zone) room.zones.elementAt(selection);
            move(zone);

            // reset selection
            selection = 0;

            // start zone music
            SoundPlayer.playZoneMusic();


        //     game.player.lastMovementDirection=Direction.Enter;

        } else if (keyState == DarkwoodCanvas.SOFTKEY1_PRESSED) {
            if (room.exit != null) {
                selection = 0;
                room.removeThing(Game.player);
                room.exit.addThing(Game.player);
            }
        }
    }
    public void pointerReleasedEvent(int x, int y) {
        MapRoom room = (MapRoom) (Game.player.room);

        int closestPointIndex = 0;
        double distance = 1000;
        int workX, workY;

        // check which zone icon is closest to the point touched by the user
        for (int i = 0; i < room.zones.size(); i++) {
            workX = ((Zone) room.zones.elementAt(i)).getX() - intX;
            workY = ((Zone) room.zones.elementAt(i)).getY() - intY;
            double workDistance = Math.sqrt((workX - x) * (workX - x) + (workY - y) * (workY - y));

            // check if the current zones distance is the closest
            if (workDistance < distance) {
                closestPointIndex = i;
                distance = workDistance;
            }


        }

        selection = closestPointIndex;

    }

    public void pointerPressedEvent(int x, int y) {
    }
}
