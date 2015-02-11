/*
 * Party.java
 *
 * Created on 9. hein�kuuta 2007, 0:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood.party;

import fi.darkwood.Ability;
import fi.darkwood.Creature;
import fi.darkwood.Game;
import java.util.Enumeration;
import java.util.Vector;
import fi.darkwood.Player;
import fi.darkwood.ability.AbilityVisualEffect;
import fi.darkwood.network.CharacterSerializer;
import fi.darkwood.room.Room;
import fi.darkwood.ui.component.MessageLog;
import nanoxml.kXMLElement;
//#if JSR82
import javax.bluetooth.ServiceRecord;
//#endif

/**
 *
 * @author Teemu Kivim�ki
 */
public class Party {

    public Party() {
    }
    /**
     * Vector consisting of BluetoothListener objects that represent connections to clients
     */
    public Vector clientlist = new Vector();
    public Vector members = new Vector();
    public Game game;
    private boolean active = false;
    private boolean bluetoothAvailable = true;
    public boolean isLeader = false;

// ** Exlude this part if JSR82 not implemented in this build **
//#if JSR82
    public BluetoothConnector connectionToLeader;

    /**
     * Initialize party
     *
     * @param isLeader
     * @param service
     */
    public void createParty(boolean isLeader, ServiceRecord service) {
        cleanParty();

        if (isActive()) {
            System.out.println("ERROR: Party is already active!!!");
            return;
        }

        if (isLeader) {
            // add a listener to where a member can connect
            clientlist.addElement(new BluetoothListener(this));
            // the connectionToLeader is the first element
            members.addElement(game.player);

            setActive(true); // connectionToLeader's party is always active
        } else {
            // attempt to connect the party connectionToLeader
            connectionToLeader = new BluetoothConnector(this, service);
        }

        this.isLeader = isLeader;
    }



    /**
     * Called when connectionToLeader leaves tavern
     * Close all listeners that are not connected.
     * If connectionToLeader is only members, set party not active.
     */
    public void finalizeParty() {
        if (!isLeader) {
            return;
        }
        Enumeration enumeration = clientlist.elements();
        while (enumeration.hasMoreElements()) {
            BluetoothListener bl = (BluetoothListener) enumeration.nextElement();
            if (!bl.isConnected()) {
                bl.closeListener();
                clientlist.removeElement(bl);
            }
        }
        if (clientlist.isEmpty()) {
            active = false;
            System.out.println("No members in party, setting active=false.");
        }
    }

    public void leaveParty() {

        // if party is not active, just return
        if (isActive() == false) return;

        System.out.println("Leaving party..");
        if (isLeader) {
            sendDisbandParty();

            try {
                // wait for message to be sent
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // close all listeners
            Enumeration clients = clientlist.elements();
            while (clients.hasMoreElements()) {
                BluetoothListener bl = (BluetoothListener) clients.nextElement();
                bl.closeListener();
                clientlist.removeElement(bl);
            }
        } else {
            connectionToLeader.send("Leave:" + game.player.getId());
            try {
                // wait for message to be sent
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connectionToLeader.closeConnector();
        }

        // remove all other members from the current room
        Enumeration memberEnu = members.elements();
        while (memberEnu.hasMoreElements()) {
            Player p = (Player) memberEnu.nextElement();
            if (p != game.player) {
                game.player.room.removeThing(p);
            }
        }

        cleanParty();

        setActive(false);

        MessageLog.getInstance().addMessage("You left the party.");
    }

    public void memberLeftParty(int id, BluetoothListener bl) {
        // find the members connection and disconnect it
        Enumeration memberConns = clientlist.elements();
        while (memberConns.hasMoreElements()) {
            BluetoothListener b = (BluetoothListener) memberConns.nextElement();
            if (b.equals(bl)) {
                b.closeListener();
                clientlist.removeElement(bl);
            }
        }


        // find the member in memberlist and remove him from the party and the current room
        Player p = getMemberById(id);
        if (p == null) {
            System.out.println("Could not find party member with id " + id + " to remove!!");
            return;
        }
        members.removeElement(p);
        // also remove from connectionToLeader's room
        if (Game.player.room.getPlayers().contains(p)) {
            Game.player.room.removeThing(p);
        }
        MessageLog.getInstance().addMessage(p.name + " left the party.");

        if (clientlist.isEmpty()) {
            active = false;
            return;
        }

        // sync again to keep all members up to date
        syncParty();


    }

    public Player getMemberById(int id) {
        Enumeration memberEnu = members.elements();
        while (memberEnu.hasMoreElements()) {
            Player p = (Player) memberEnu.nextElement();
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private void sendDisbandParty() {
        sendToAll("disband");
    }


    /** Send msg to all party members. If this is not the server, do nothing.
     *
     * @param msg
     */
    public void sendToAll(String msg) {
        // only does something if party is active and player is connectionToLeader
        if (isActive() && isLeader) {
            Enumeration e = clientlist.elements();
            while (e.hasMoreElements()) {
                BluetoothListener t = (BluetoothListener) e.nextElement();
                if (t.isConnected()) {
                    t.send(msg);
                }
            }
        }
    }

    public void readCommands() {
        // only does something if party is active and player is connectionToLeader
        if (isActive() && isLeader) {
            Enumeration e = clientlist.elements();
            while (e.hasMoreElements()) {
                BluetoothListener t = (BluetoothListener) e.nextElement();
                if (t.isConnected()) {
                    t.readMessages();
                }
            }
        } else if (isActive() && !isLeader) {
            // readMessages commands sent by server
            connectionToLeader.readMessages();
        }
    }


        public void sendAbilityUseToLeader(int userid, Ability ability) {
        connectionToLeader.send("invoke:" + userid + "/" + ability.getClass().getName() + "," + ability.getLevel());

//        connectionToLeader.send("ability:" + userid + "," + ability.getClass().getName() + "/" + ability.getLevel());

    }

//#else
//# 
//#     // If JSR82 was not available in this build, use empty methods
//#     public void sendToAll(String msg) {
//#         // nothing
//#     }
//# 
//#     public void readCommands() {
//#         // nothing
//#     }
//# 
//#     public void sendAbilityUseToLeader(int userid, Ability ability) {
//#         // nothing
//#     }
//# 
//#     public void leaveParty() {
//#         // nothing
//#     }
//#endif

    public void sendVisualEffect(int id, AbilityVisualEffect effect) {
        sendToAll("effect:" + id + "," + effect.getImageFile() + "," + effect.getOriginalDuration() + "," + effect.frameWidth);

    }

    public void cleanParty() {
        System.out.println("Removing all party members, size before remove:" + members.size());
        members.removeAllElements();

    }

    // Send info of all players in the party to all clients
    public void syncParty() {
        sendToAll("cleanParty");

        Enumeration e2 = members.elements();
        while (e2.hasMoreElements()) {
            Player pl = (Player) e2.nextElement();
            sendToAll(pl.sendInformation());
        }

    }

    public void addPartyMember(String str, int id) {
        System.out.println("Adding party member with id: " + id);
        kXMLElement xml = new kXMLElement();
        xml.parseString(str);
        Player p2 = CharacterSerializer.loadCharacter(xml, id);


        if (id == game.player.getId()) {
            members.addElement(game.player);
        } else {
            members.addElement(p2);
            game.player.room.addThing(p2);
        }


    }

    /**
     * Move party and send move message to all members
     * Used only by connectionToLeader.
     * @param destination
     */
    public void moveParty(Room destination) {
        movePartyLocally(destination);

        // send move command to all clients (assume the clients are in the same zone, or else there will be an error)
        sendToAll("move:" + Game.player.room.getId());
    }

    /**
     * Move party locally to destination room.
     * @param destination
     */
    public void movePartyLocally(Room destination) {
        if (!isActive()) {
            return;
        }
        Enumeration e = members.elements();
        while (e.hasMoreElements()) {
            Creature member = (Creature) e.nextElement();
            member.moveTo(destination.getId(), destination.getZone());
            // Reset cooldowns and regain mana
            member.resetCoolDowns();
            Player pl = (Player) member;
            pl.rest();

        }
    }




    public void fallback() {
        if (isLeader) {
            sendToAll("fallback");
        } else {
            leaveParty();
        }
    }
    // If connectionToLeader moved from maproom to a new area, send infos of this new area to all members
    public void initZoneForClients(String zoneXmlName, String fallbackZone) {
        System.out.println("Sending area init to members: " + zoneXmlName + ", " + fallbackZone);
        sendToAll("initzone:" + zoneXmlName + "," + fallbackZone);

    /*        Enumeration e = clientlist.elements();
    while (e.hasMoreElements()) {
    BluetoothListener bl = (BluetoothListener) e.nextElement();
    // Send a message to init the area
    System.out.println("Sending area info to member: " + bl + " " + zoneXmlName + ", " + fallbackZone);
    bl.send("initzone:" + zoneXmlName + "," + fallbackZone);
    } */
    }

    public void initCityForClients(String zoneClassName) {
        System.out.println("Sending city init to members: " + zoneClassName);
        sendToAll("initcity:" + zoneClassName);
    }

    public void sendAbilityUseToClients(String msg) {
        //sendToAll("invoke:" + userid + "/" + ability.getClass().getName() + "," + ability.getLevel());
        sendToAll("message:" + msg);

    }





    public void setActive(boolean arg) {
        active = arg;
    }

    public boolean isActive() {
        return active;
    }

    public boolean getBluetoothAvailable() {
        return bluetoothAvailable;
    }

    public void setBluetoothAvailable(boolean available) {
        bluetoothAvailable = available;
    }
}

