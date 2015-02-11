/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Administrator
 */
public class LocalDatabase {

    static final int RECORD_ID_ACCOUNT_NAME = 1;
    static final int RECORD_ID_PASSWORD = 2;
    static final int RECORD_ID_CHARACTER_NAME = 3;
    static final int RECORD_ID_TERMS_OF_SERVICE_ACCEPTED = 5;

    /**
     * Character xml slots are 10-14 (5 slots)
     */
    static final int RECORD_ID_CHARACTER_XML = 10;

    static final String RECORD_FILENAME = "darkwood";

    public static String readRecord(RecordStore db, int id) throws
            IOException, RecordStoreException {
        byte[] record1 = null;
        try {
            record1 = db.getRecord(id);
        } catch (InvalidRecordIDException e) {
            System.out.println("No record found for id " + id + " returning blank string.");
            return "";
        }

        if (record1 == null) { return ""; }

        ByteArrayInputStream bais = new ByteArrayInputStream(record1);
        DataInputStream dis = new DataInputStream(bais);
        String in = dis.readUTF();
        return in;
    }

    public static void writeRecord(RecordStore db, String data, int id) throws
            IOException, RecordStoreException {

        // if recordstore is not initialized, create new empty records
        while (db.getNumRecords() < 16) {
            db.addRecord(null, 0, 0);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeUTF(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = baos.toByteArray();

        try {
            // try to read record with id
            db.getRecord(id);
            // if record existed, overwrite it
            db.setRecord(id, b, 0, b.length);
        } catch (InvalidRecordIDException e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }

    }

    public static void saveAccountInfo(String accountName, String password, boolean savePwd) {
        try {

            RecordStore db = RecordStore.openRecordStore(RECORD_FILENAME, true);

            writeRecord(db, accountName, RECORD_ID_ACCOUNT_NAME);

            if (savePwd) {

                writeRecord(db, password, RECORD_ID_PASSWORD);
                //writeRecord(db, charName, RECORD_ID_CHARACTER_NAME);
            } else {
                writeRecord(db, "", RECORD_ID_PASSWORD);
            }
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void populateLoginFields(TextField accountName, TextField password, ChoiceGroup savePassword) {
        try {


            RecordStore db = RecordStore.openRecordStore(RECORD_FILENAME, true);
            String accountName_saved = readRecord(db, RECORD_ID_ACCOUNT_NAME);
            String password_saved = readRecord(db, RECORD_ID_PASSWORD);

            //   String characterName_saved = readRecord(db, RECORD_ID_CHARACTER_NAME);

            accountName.setString(accountName_saved);
            password.setString(password_saved);

            // if there was a password saved, also set "save password" checkbox checked
            if ("".equals(password_saved) == false) {
                savePassword.setSelectedIndex(0, true);
            }

            //     characterName.setString(characterName_saved);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveCharacterXML(String xmlString, int slot) throws IOException {
        try {
            RecordStore db = RecordStore.openRecordStore(RECORD_FILENAME, true);

            writeRecord(db, xmlString, RECORD_ID_CHARACTER_XML + slot);
            System.out.println("character saved.. " + loadCharacterXML(slot));
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw e;
        }
    }

    public static String loadCharacterXML(int slot) {
        try {
            RecordStore db = RecordStore.openRecordStore(RECORD_FILENAME, true);
            return readRecord(db, RECORD_ID_CHARACTER_XML + slot);

        } catch (RecordStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Check if the terms of service have been accepted on this phone
     * @return
     */
    public static boolean isTermsOfServiceAccepted() {

        try {

            RecordStore db = RecordStore.openRecordStore(RECORD_FILENAME, true);

            String record = readRecord(db, RECORD_ID_TERMS_OF_SERVICE_ACCEPTED);
            if ("true".equals(record)) {
                return true;
            } else {
                return false;
            }
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setTermsOfServiceAccepted() {
        try {

            RecordStore db = RecordStore.openRecordStore(RECORD_FILENAME, true);

            writeRecord(db, "true", RECORD_ID_TERMS_OF_SERVICE_ACCEPTED);
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
