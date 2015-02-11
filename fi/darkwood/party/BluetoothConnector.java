/*
 * BluetoothConnector.java
 *
 * Created on April 18, 2007, 12:45 AM
 *
 */
package fi.darkwood.party;

import fi.darkwood.AreaGenerator;
import fi.darkwood.Creature;
import fi.darkwood.Game;
import fi.darkwood.Logger;
import fi.darkwood.room.Room;
import fi.darkwood.Zone;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.ui.component.MessageLog;
import fi.darkwood.util.Utils;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;


public class BluetoothConnector implements Runnable {

    private InquiryListener inq_listener;
    private ServiceListener serv_listener;
    private boolean listening = true;
    private Party party;
    private String deviceName;
    // private L2CAPConnection con;
    private StreamConnection con;
    private DataOutputStream dos;
    private DataInputStream dis;
    private ServiceRecord serviceRecord;
    private boolean connected = false;

    /**
     * Creates a new instance of BluetoothConnector
     */
    public BluetoothConnector(Party party, ServiceRecord serviceRecord) {
        this.party = party;
        this.serviceRecord = serviceRecord;

        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        System.out.println("Starting client ? please wait...");
        Logger.getInstance().debug("Starting client...");

        try {
            String url;
            url = serviceRecord.getConnectionURL(0, false);
            deviceName = LocalDevice.getLocalDevice().getFriendlyName();

            //con = (L2CAPConnection) Connector.open( url );
            con = (StreamConnection) Connector.open(url);
            Date time = new Date();
            // Send contact information

            dos = con.openDataOutputStream();
            dis = con.openDataInputStream();

            send(party.game.player.sendInformation());
            //       byte[] b = new byte[1000];

            connected = false;
            while (listening) {
                if (dis.available() > 0 && connected == false) {
                    // go here if this is the first message to be received
                    String s = dis.readUTF();

                    parseCommand(s);

                    Logger.getInstance().debug("Connected! ping " + (new Date().getTime() - (time.getTime())));

                    connected = true;
                    party.setActive(true); // member's party is active

                }
                /*
                if (dis.available() > 0 && connected == true) {
                String s = dis.readUTF();
                parseCommand(s);
                }
                 */
                Thread.sleep(100);
            }
        } catch (SecurityException e) {
            Game.party.setBluetoothAvailable(false);
        } catch (Exception g) {
            g.printStackTrace();
        }
        Logger.getInstance().debug("BC loop exited.");
    }

    public void send(String s) {
        /*        byte[] b = s.getBytes();
        String s2 = new String(b, 0, b.length); */
        System.out.println("connector tx: " + s);

        try {
            dos.writeUTF(s);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessages() {
        try {
            if (dis.available() > 0 && connected == true) {
                String s = dis.readUTF();
                parseCommand(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void parseCommand(String s) {

        Logger.getInstance().debug("Rx:" + s);

        if (s.indexOf("move:") != -1) {
            // move command format is: "move:[roomId]"
            int roomId = -1;
            
            try {
                // filter out the prefix "move:"
                String idString = s.substring(5);

                System.out.println("parsed move id:" + idString);
                // parse the room id
                roomId = Integer.parseInt(idString);

            } catch (NumberFormatException e) {
                System.out.println("Invalid number format at parseCommand move! " + s);
            }
            
            try {
                Zone zone = Game.player.room.getZone();

                zone.resetZone();
                Room moveRoom = (Room) zone.rooms.elementAt(roomId);
                party.moveParty(moveRoom);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (s.indexOf("damage:") != -1) {
            int creatureId, damage;
            try {
                creatureId = Integer.parseInt(s.substring(7, s.indexOf("/")));
                damage = Integer.parseInt(s.substring(s.indexOf("/") + 1));
                Creature creature = party.game.player.room.getCreatureById(creatureId);
                if (creature != null) {
                    if (damage >= 0) {
                        creature.harm(damage);
                    } else {
                        creature.addHealthChange("Missed");
                    }
                } else {
                    System.out.println("Error, creature to damage not found. ID " + creatureId);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid message at parseCommand damage! " + s);
            }
        } else if (s.indexOf("heal:") != -1) {
            int creatureId, heal;
            try {
                creatureId = Integer.parseInt(s.substring(5, s.indexOf("/")));
                heal = Integer.parseInt(s.substring(s.indexOf("/") + 1));
                Creature creature = party.game.player.room.getCreatureById(creatureId);
                if (creature != null) {
                    creature.heal(heal);
                } else {
                    System.out.println("Error, creature to heal not found. ID " + creatureId);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid message at parseCommand heal! " + s);
            }
       } else if (s.indexOf("reducemana:") != -1) {
            int creatureId, mana;
            try {
                creatureId = Integer.parseInt(s.substring(11, s.indexOf("/")));
                mana = Integer.parseInt(s.substring(s.indexOf("/") + 1));
                Creature creature = party.game.player.room.getCreatureById(creatureId);
                if (creature != null) {
                    creature.reduceMana(mana);
                } else {
                    System.out.println("Error, creature to reducemana not found. ID " + creatureId);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid message at parseCommand reducemana! " + s);
            }
        } else if (s.indexOf("actioncooldown:") != -1) {
            int creatureId, cooldown, slot;
            try {
                
                creatureId = Integer.parseInt(s.substring(15, s.indexOf("/")));
                cooldown = Integer.parseInt(s.substring(s.indexOf("/") + 1, s.indexOf(",")));
                slot = Integer.parseInt(s.substring(s.indexOf(",") + 1));
                Creature creature = Game.player.room.getCreatureById(creatureId);
                if (creature != null) {
                    creature.addAbilityCooldown(cooldown, slot);
                } else {
                    System.out.println("Error, creature to accd not found. ID " + creatureId);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid message at parseCommand accd! " + s);
            }
     /*   } else if (s.equals("reducecooldowns")) { // No need for this since clients tick cooldowns themselves
            Game.player.reduceCooldowns(); */
        } else if (s.indexOf("Player:") != -1) {
            String msg;
            msg = s.substring(7);
            String idstr = msg.substring(0, msg.indexOf(","));
            int id = Integer.parseInt(idstr);
            msg = msg.substring(msg.indexOf(",") + 1);
            party.addPartyMember(msg, id);
        } else if (s.indexOf("cleanParty") != -1) {
            party.cleanParty();
        } else if (s.indexOf("initzone:") != -1) {
            // parse zone, fallback zone and exit zone names from the message

            // trim off the "initzone:"-part
            String msg = s.substring(9);

            String zoneXml = msg.substring(0, msg.indexOf(","));
            // trim off the zone class name
            msg = msg.substring(msg.indexOf(",") + 1);
            String fallbackZone = msg;
//            String fallbackZone = msg.substring(0, msg.indexOf(","));

//            String exitZone = msg.substring(msg.indexOf(",") + 1);

            // instantiate the zone

            Zone zone = AreaGenerator.getInstance().loadArea(zoneXml);
//            Zone zone = Utils.getZoneForClassName(zoneClass);
            zone.setFallbackZone(fallbackZone);

            //            zone.setExitZone(exitZone);

            zone.resetZone();
            // move the whole party in this client to the first room of the zone
            party.moveParty((Room) zone.rooms.elementAt(0));
        } else if (s.indexOf("initcity:") != -1) {
            // parse zone, fallback zone and exit zone names from the message

            // trim off the "initzone:"-part
            String zoneClass = s.substring(9);

            // instantiate the zone
            Zone zone = Utils.getZoneForClassName(zoneClass);
            zone.resetZone();

            // move the whole party in this client to the first room of the zone
            party.moveParty((Room) zone.rooms.elementAt(0));
        } else if (s.equals("disband")) {
            party.leaveParty();
        } else if (s.equals("fallback")) {
            Zone fallbackZone = Utils.getZoneForClassName(Game.player.room.getZone().getFallbackZone());
            Room fallbackRoom = (Room) fallbackZone.rooms.elementAt(0);
            Game.party.moveParty(fallbackRoom);
        //     Game.player.room.removeThing(Game.player);
        //    fallbackRoom.addThing(Game.player);


        //    Game.player.room.zone.getFallbackZone();

        } else if (s.indexOf("effect:") != -1) {
            int creatureId;
            try {
                // first drop "effect:" and sort creature id
                s = s.substring(7);
                String idStr = s.substring(0, s.indexOf(","));
                creatureId = Integer.parseInt(idStr);

                // drop the id bit
                s = s.substring(s.indexOf(",") + 1);

                // then, get image file
                String imagefile = s.substring(0, s.indexOf(","));

                // drop imagefile bit
                s = s.substring(s.indexOf(",") + 1);

                // get duration
                String durationStr = s.substring(0, s.indexOf(","));
                int duration = Integer.parseInt(durationStr);

                // drop duration bit
                s = s.substring(s.indexOf(",") + 1);

                // get width (last parameter)
                int width = Integer.parseInt(s);

                //System.out.println("Id: " + creatureId + " Class: " + abilityClass + " / " + abilityLevel);

                AbilityVisualEffect effect = new AbilityVisualEffect(imagefile, width, duration);

                Creature creature = Game.player.room.getCreatureById(creatureId);
                creature.addAbilityEffect(effect);
            // send the effect to all clients (?)

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (s.indexOf("message:") != -1) {
            // Used to display e.g. the skill use messages
            String msg = s.substring(8);
            MessageLog.getInstance().addMessage(msg);
        }
    /*
    else if (s.indexOf("invoke:") != -1) {
    String msg;
    msg = s.substring(7);
    String idstr = msg.substring(0, msg.indexOf(","));
    int id = Integer.parseInt(idstr);
    
    Creature invoker = party.getMemberById(id);
    
    String className = msg.substring(msg.indexOf(",") + 1, msg.indexOf("/"));
    
    Class luokka = null;
    try {
    luokka = Class.forName(className);
    Ability ability = null;
    ability = (Ability) luokka.newInstance();
    
    ability.invoke(party.game, invoker);
    } catch (ClassNotFoundException ex) {
    System.out.println("Class not found loading ability: " + className);
    ex.printStackTrace();
    } catch (InstantiationException ex) {
    ex.printStackTrace();
    } catch (IllegalAccessException ex) {
    System.out.println("Illegal access loading ability: " + className);
    ex.printStackTrace();
    } 
    
    
    }*/
    }

    /**
     * Close this connector. Input and output streams are closed, aswell as the bluetooth connection.
     */
    public void closeConnector() {
        System.out.println("Closing BluetoothConnector.");
        try {
            listening = false;
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (IOException e) {
            Logger.getInstance().debug("IOException while closing BT connector.");
            e.printStackTrace();
        }
    }
}
