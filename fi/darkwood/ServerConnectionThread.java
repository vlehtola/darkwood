/*
 * Copyright Mirake Ltd. All Rights reserved.
 */
package fi.darkwood;

import fi.darkwood.network.HttpClient;
import fi.darkwood.network.HttpConnectionNotOkException;
import fi.darkwood.util.Properties;
import java.io.IOException;
import javax.microedition.lcdui.Display;
import nanoxml.kXMLElement;
import nanoxml.kXMLParseException;

/**
 *
 * @author Administrator
 */
public class ServerConnectionThread implements Runnable {

    DarkwoodMidlet parent;

    public ServerConnectionThread(DarkwoodMidlet midlet) {
        parent = midlet;
        new Thread(this).start();
    }

    public void run() {
        try {
            // load characters xml from server
            String charactersXml = "";
            try {
                String account = (String) Properties.getApplicationProperties().get("Account-Name");
                String password = (String) Properties.getApplicationProperties().get("Account-Password");

                String version = parent.getAppProperty("MIDlet-Version");
                charactersXml = HttpClient.loadCharactersXml(account, password, version);
            } catch (IOException e) {
                // failed to connect
                e.printStackTrace();
                // in testing mode, enter game even with no connection
                if (GameConstants.TESTINGMODE == false) {
                    parent.showConfirmation("Could not connect to server", "Please check phone internet connection.", "Ok");
                    return;
                }
            } catch (SecurityException e) {
                // failed to connect, because user did not allow app to use net
                e.printStackTrace();
                // in testing mode, enter game even with no connection
                if (GameConstants.TESTINGMODE == false) {
                    parent.showConfirmation("Could not connect to server", "Please check that internet connection is allowed.", "Ok");
                    return;
                }
            } catch (HttpConnectionNotOkException e) {
                if (GameConstants.TESTINGMODE == false) {
                    parent.showConfirmation("Could not connect to server", "Server response not ok: " + e.getMessage(), "Ok");
                    return;
                }
                
            }

            // parse assigned character id from the response
            System.out.println("Parsing: " + charactersXml);
            kXMLElement response = new kXMLElement();
            try {
                response.parseString(charactersXml);
            } catch (kXMLParseException xmlerror) {
                // could not parse the xml from server, this is bad, real bad.. means server is broken or account xml data has been corrupted
                parent.closeAlert();
                parent.showConfirmation("Error reading server data", "Try reconnecting later. If problem persists, contact Darkwood support at http://www.darkwood.cc.", "Ok");
                return;

            }
            // root element property "id" is the status code of the server response
            // if characterxml was returned ok, root element has no "id" property
            String responseId = response.getProperty("id");
            int responseCode = 0;
            if (responseId != null && responseId.length() > 0) {
                responseCode = Integer.parseInt(responseId);
                if (responseCode == HttpClient.ERROR_AUTHENTICATION_FAILURE) {
                    System.out.println("RETURNED -1 (auth failed)");
                    parent.closeAlert();
                    parent.showConfirmation("Authentication failed", "Wrong account/password. Register an account at http://www.darkwood.cc and play for free!", "Ok");
                    return;
                }
                if (responseCode == HttpClient.ERROR_WRONG_VERSION) {
                    System.out.println("RETURNED "+HttpClient.ERROR_WRONG_VERSION + " (wrong version)");
                    parent.closeAlert();
                    parent.showConfirmation("Update game", "You need to update your game. Open your mobile phone browser to http://www.darkwood.cc/darkwood.jad to start update.", "Ok");
                    return;
                }

            }

            // check if this account is paid one (unregistered can play only until level 10)
            String registered = response.getProperty("registered");
            if ("true".equals(registered)) {
                Game.registered = true;
                System.out.println("Registered account.");
            } else {
                Game.registered = false;
                System.out.println("Unregistered account.");
            }
            
            // show connect screen atleast for 1 sec
            Thread.sleep(1000);

            // this call tells the client to proceed to title screen, also passes characterxml to the game
            parent.notifyFetchComplete(charactersXml);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
