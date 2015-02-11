/*
 * BluetoothListener.java
 *
 * Created on April 18, 2007, 12:45 AM
 *
 */
package fi.darkwood.party;

import fi.darkwood.Ability;
import fi.darkwood.Creature;
import fi.darkwood.Game;
import fi.darkwood.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothListener implements Runnable {

    private boolean listening = true;
    private LocalDevice local_device;
    private Party party;
    private String deviceName;
    //  private L2CAPConnection con;
    private StreamConnection con;
    private DataOutputStream dos;
    private DataInputStream dis;
    private StreamConnectionNotifier mServerNotifier;
    Thread t = null;
    private int memberId;
    
    /**
     * True if there is a client connected to this listener and init messages have been exchanged
     */
    private boolean connected = false;

    /**
     * Creates a new instance of BluetoothListener
     */
    public BluetoothListener(Party party) {
        this.party = party;
        t = new Thread(this);
        t.start();
    }

    public void run() {
        System.out.println("Starting server ? please wait...");
        try {
            local_device = LocalDevice.getLocalDevice();
            local_device.setDiscoverable(DiscoveryAgent.GIAC);
            String service_UUID = "00000000000010008000006057028A06";
            deviceName = local_device.getFriendlyName();
            System.out.println("Device address: " + local_device.getBluetoothAddress());

            String url = "btspp://localhost:" + service_UUID + ";name=" + "devicename" + ";authorize=false";
            mServerNotifier = (StreamConnectionNotifier) Connector.open(url);
            //L2CAPConnectionNotifier notifier = (L2CAPConnectionNotifier) Connector.open(url);
            con = mServerNotifier.acceptAndOpen();

            dis = con.openDataInputStream();
            dos = con.openDataOutputStream();
            while (listening) {
                //if (con.ready() && connected == false) {
                if (dis.available() > 0 && connected == false) {
                    String s = dis.readUTF();
                    Logger.getInstance().debug("Rx:" + s);
                    if (s.indexOf("Player:") == -1) {
                        Logger.getInstance().debug("Error in party init message");
                    }
                    String msg;
                    msg = s.substring(7);
                    String idstr = msg.substring(0, msg.indexOf(","));
                    int id = Integer.parseInt(idstr);
                    msg = msg.substring(msg.indexOf(",") + 1);
                    msg = msg.trim();
                    party.addPartyMember(msg, id);
                    memberId = id;

                    connected = true;

                    Logger.getInstance().debug("Syncing party with members");
                    party.syncParty();
                }

          /*      if (dis.available() > 0 && connected == true) {
                    String s = dis.readUTF();
                    s = s.trim();
                    System.out.println("Connected true, received: " + s);
                    parseMessage(s);
                } */

                Thread.sleep(100);
            }


        } catch (InterruptedIOException e) {
            Logger.getInstance().debug("Interrupted in BT init.");
        } catch (SecurityException e) {
            // bluetooth is not allowed by user
            Game.party.setBluetoothAvailable(false);
            Logger.getInstance().debug("Security ex in BT init.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connected = false;
            Logger.getInstance().debug("BL loop exited.");
        }
    }

    
    public void readMessages() {
        try {
            // if nothing to readMessages, return
            if (dis.available() == 0 || connected == false) { return; }
            String s = dis.readUTF();
            s = s.trim();
            System.out.println("ServerRX: " + s);
            parseMessage(s);

        } catch (IOException e) {
            // lost connection to party member
            System.out.println("IOException reading from client, kicking from party");
            party.memberLeftParty(memberId, this);
        }
    }
    
    public void send(String s) {
        Logger.getInstance().debug("tx: " + s);
        try {
            dos.writeUTF(s);
            dos.flush();
        } catch (IOException e) {
            System.out.println("IOException writing to client, kicking from party");
            party.memberLeftParty(memberId, this);
        }
    }

    private void parseMessage(String s) {
        if (s.indexOf("Leave:") != -1) {
            try {
                int playerid = Integer.parseInt(s.substring(6));

                System.out.println(playerid + " left party");

                party.memberLeftParty(playerid, this);

            } catch (NumberFormatException e) {
                System.out.println("Invalid message at parseCommand Leave (server)! " + s);
            }
        }

        if (s.indexOf("invoke:") != -1) {
            int playerId;
            String abilityClass = "(not yet parsed)";
            try {
                s = s.substring(7);
                String idStr = s.substring(0, s.indexOf("/"));
                playerId = Integer.parseInt(idStr);

                s = s.substring(s.indexOf("/") + 1);
                abilityClass = s.substring(0, s.indexOf(","));

                String abilityLvlStr = s.substring(s.indexOf(",") + 1);
                int abilityLevel = Integer.parseInt(abilityLvlStr);


                System.out.println("Id: " + playerId + " Class: " + abilityClass + " / " + abilityLevel);



                // message has been parsed, now instantiate the ability and invoke it.
                Class luokka = null;

                luokka = Class.forName(abilityClass);
                Ability ability = null;
                ability = (Ability) luokka.newInstance();
                ability.setLevel(abilityLevel);
                
                Creature invoker = party.getMemberById(playerId);
                System.out.println("Invoking " + ability.name);
                ability.invoke( invoker);

            } catch (NumberFormatException e) {
                System.out.println("Invalid message at parseCommand invoke (server)! " + s);
            } catch (ClassNotFoundException ex) {
                System.out.println("Class not found loading ability: " + abilityClass);
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                System.out.println("Illegal access loading ability: " + abilityClass);
                ex.printStackTrace();
            }





        }
    }

    public boolean isConnected() {
        return connected;
    }

    /**
     * Close this listener. Input and output streams are closed, aswell as the bluetooth connection.
     */
    public void closeListener() {
        // mServerNotifier.notify();
        System.out.println("Closing BluetoothListener.");
        try {
            if (mServerNotifier != null) {
                mServerNotifier.close();
            }
            
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (con != null) {
                con.close();
            }
            listening = false;
            connected = false;
        } catch (IOException e) {
            Logger.getInstance().debug("IOException while closing BT listener.");
            e.printStackTrace();
        }
    }
}