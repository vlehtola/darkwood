/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.network;

import fi.darkwood.Game;
import fi.darkwood.Logger;
import fi.darkwood.Player;
import fi.darkwood.util.LocalDatabase;
import fi.darkwood.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import nanoxml.kXMLElement;
import nanoxml.kXMLParseException;

/**
 *
 * @author Teemu
 */
public class HttpClient {

    final static String serverUrl = "http://darkwood.cc/";
    final static String loadUrl = serverUrl + "load.php";
    final static String saveUrl = serverUrl + "save.php";
    final static String deleteUrl = serverUrl + "delete.php";
    
    /**
     * Authentication to game server failed, wrong username or password
     */
    public final static int ERROR_AUTHENTICATION_FAILURE = -1;
    public final static int ERROR_SERVER_DATABASE_CONNECTION_ERROR = -2;
    public final static int ERROR_SAVE_UPDATE_ERROR = -3;
    public final static int ERROR_SAVE_INSERT_ERROR = -4;
    public final static int ERROR_WRONG_VERSION = -5;
    public final static int ERROR_WRONG_XML_LENGTH = -6;
    public final static int ERROR_SAVE_RESPONSE_PARSING = -10;


    /**
     * Load characters belonging to account from server
     * @param account
     * @param password
     * @return Characters listed in XML format
     * @throws java.io.IOException Could not connect
     * @throws java.lang.SecurityException Was not allowed to use internet
     */
    public static String loadCharactersXml(String account, String password, String version) throws IOException, SecurityException, HttpConnectionNotOkException {
        System.out.println("connecting to " + loadUrl);
        HttpConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        StringBuffer b = new StringBuffer();
        try {
            conn = (HttpConnection) Connector.open(loadUrl);

            conn.setRequestMethod(HttpConnection.POST);
            conn.setRequestProperty("IF-Modified-Since", "25 Nov 2001 15:17:19 GMT");
            conn.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
            conn.setRequestProperty("Content-Language", "en-EN");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            os = conn.openOutputStream();

            String parameters = "name=" + account + "&password=" + password;

//#ifdef PRODUCTION_VERSION
//#                     // If this is production release, add version to parameters being sent to server.
//#                     // Allows the game to check wherther you need to update.
//#                     parameters = parameters + "&version="+version;
//#endif


            byte[] bytes = parameters.getBytes();

            os.write(bytes);

//            os.flush();
            os.close();

            System.out.println("Response: " + conn.getResponseCode());


            is = conn.openDataInputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
                System.out.print((char) ch);
            }

            if (conn.getResponseCode() == HttpConnection.HTTP_OK) {
                System.out.println("Connection ok.");
            } else {
                throw new HttpConnectionNotOkException("HTTP connection not ok: " +conn.getResponseCode());
            }

            
        } finally {
            System.out.println("Closing connection.");
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return b.toString();
    }
    
    /**
     * Save the player given as argument to server using account details
     * from ApplicationProperties (utils)
     * @param player player instance to be saved
     * @return
     */
    public static boolean saveCharacter(Player player) {
        String xmlString = CharacterSerializer.createXmlString(player);

        int newId = 0;
        try {

//#if DARKWOOD_OFFLINE
//#             // save character to local database if this is offline version
//#             Random random = new Random(System.currentTimeMillis());
//# 
//#             // save character to local database if this is offline version
//#             LocalDatabase.saveCharacterXML(xmlString, Game.characterSaveSlot);
//# 
//#             // give a random id so that party will work.. not a global id obviously..
//#             player.setGlobalCharacterId(random.nextInt(1000000) + 100);
//# 
//#             return true;
//#else
            // save the character and read assigned id from the response
            newId = saveCharacterXml(xmlString, player.getId());

            if (newId == ERROR_WRONG_XML_LENGTH) {
                // the xml was somehow corrupted during transfer (xml size did not match at server), retry once..
                newId = HttpClient.saveCharacterXml(xmlString, player.getId());
            }

            // if player doesnt have an id, set the id returned in save
            if (player.getId() < 1 && newId > 0) {
                player.setGlobalCharacterId(newId);
            }
            if (newId < 0) {
                // server returned other than char id (some error code)
                Logger.getInstance().logToFile("Error saving: " + newId);
                return false;
            } else {
                return true;
            }
//#endif
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getInstance().debug("Error: " + e.getMessage());
            return false;
        }

    }

    private static int saveCharacterXml(String xml, int characterId) throws IOException {
        System.out.println("connecting to " + saveUrl);
        HttpConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        StringBuffer b = new StringBuffer();
        try {
            conn = (HttpConnection) Connector.open(saveUrl);

            conn.setRequestMethod(HttpConnection.POST);
            conn.setRequestProperty("IF-Modified-Since", "25 Nov 2001 15:17:19 GMT");
            conn.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.1");
            conn.setRequestProperty("Content-Language", "en-EN");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            os = conn.openOutputStream();

            String account = (String) Properties.getApplicationProperties().get("Account-Name");
            String password = (String) Properties.getApplicationProperties().get("Account-Password");

            String out = "";

            // if character has already been saved before and has an id, add it
            if (characterId > 0) {
                out = out + "id=" + characterId + "&";
            }

            // add account details
            out = out + "name=" + account + "&password=" + password;

            // add the character xml
            out = out + "&xml=" + xml;

            // add the character xml
            // 10.6 update Teemu: xml string is different size at server, because " escapes and maybe line breaks..
            // Changed xml checking at server so that if it ends with </character>, it's ruled as valid :)
//            out = out + "&xml_length=" + xml.length();


            System.out.println("Sending to server: " + out);

            os.write(out.getBytes());
            os.close();

            if (conn.getResponseCode() == HttpConnection.HTTP_OK) {
                System.out.println("Connection ok.");
            }

            is = conn.openDataInputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
                System.out.print((char) ch);
            }

        } finally {
            System.out.println("Closing connection.");
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (conn != null) {
                conn.close();
            }
        }


        // parse assigned character id from the response
        kXMLElement response = new kXMLElement();
        int charId = 0;

        try {
            System.out.println("Response to save: " + b.toString());
            response.parseString(b.toString());
            String responseId = response.getProperty("id");
            if (responseId != null && responseId.length() > 0) {
                charId = Integer.parseInt(responseId);
                System.out.println("Parsed id from response: " + charId);
            }
        } catch (kXMLParseException ex) {
            charId = ERROR_SAVE_RESPONSE_PARSING; // parse error
        }
        return charId;
    }

    /**
     * Sends a message to server to delete character by id.
     * @param account
     * @param password
     * @param charId
     * @throws java.io.IOException
     * @throws java.lang.SecurityException
     */
    public void deleteCharacter(String account, String password, int charId) throws IOException, SecurityException {
        System.out.println("connecting to " + deleteUrl);
        HttpConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        StringBuffer b = new StringBuffer();
        try {
            conn = (HttpConnection) Connector.open(deleteUrl);

            conn.setRequestMethod(HttpConnection.POST);
            conn.setRequestProperty("IF-Modified-Since", "25 Nov 2001 15:17:19 GMT");
            conn.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
            conn.setRequestProperty("Content-Language", "en-EN");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            os = conn.openOutputStream();

            os.write(("name=" + account + "&password=" + password + "&id=" + charId).getBytes());
            os.flush();

            if (conn.getResponseCode() == HttpConnection.HTTP_OK) {
                System.out.println("Connection ok.");
            }

        /*    System.out.println("Response: " + conn.getResponseCode());

            is = conn.openDataInputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
                System.out.print((char) ch);
            } */

        } finally {
            System.out.println("Closing connection.");
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    // return b.toString();
    }
}
