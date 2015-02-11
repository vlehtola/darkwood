/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mirake;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

/**
 * Localization
 * Usage: call initialize at start off app with suitable locale.
 * Call get to get texts for key
 * @author Teemu
 */
public class Local {

    private static Hashtable strings;
    /** The character used for separating the key from the value. */
    private static final char SEPARATOR_CHAR = '=';
/**
 * initilize the localization for a locale, can be called many times, latest call will precede
 * @param locale en, fi, fr, pl etc.
 */
    public static void initialize(String locale) {
        loadStrings("messages_"+ locale + ".properties");
    }

   /**
     * Localizes a string.
     * @param key the key used for identifying a specific value in the text file
     * @return the message localized
     */
    public static String get(String key) {
        // Obtain the localized string from
        String value = (String)strings.get(key);
        if (value == null) {
            value = "["+key+"]";
        }
        return value;
    }




    /**
     * Stores the strings from the text file into a hash table as key-value
     * pairs.
     * @param filename
     * @throws java.lang.Exception
     */
    private static void loadStrings(final String filename) {
        strings = new Hashtable(50);

        System.out.println("Loading localefile: " + filename);
        InputStream is = Local.class.getResourceAsStream("/res/"+filename);
        try {
            InputStreamReader reader = new InputStreamReader(is, "UTF-8");

            String line = null;


            // Read a single line from the file. null represents the EOF.
            while ((line = readLine(reader)) != null) {
                System.out.println(line);

                // Empty lines are ignored
                if (line.trim().equals("")) {
                    continue;
                }
                // ignore comment lines (start with #)
                if (line.startsWith("#")) {
                    continue;
                }

                // Separate the key from the value and put the strings into the
                // hash table
                int separatorPos = line.indexOf(SEPARATOR_CHAR);
                if (separatorPos == -1) {
                    System.out.println("Error reading localefile: " + filename + " line:" + line + " - No separator found");
                    continue;
                }
                String key = line.substring(0, separatorPos);
                String value = line.substring(separatorPos + 1);
                strings.put(key, value);
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("IOException reading locale file: " + filename + " : " + e.getMessage());
        }
    }

    /**
     * Reads a single line using the specified reader.
     * @throws java.io.IOException if an exception occurs when reading the line
     */
    private static String readLine(InputStreamReader reader) throws IOException {
        // Test whether the end of file has been reached. If so, return null.
        int readChar = reader.read();
        if (readChar == -1) {
            return null;
        }
        StringBuffer string = new StringBuffer("");
        // Read until end of file or new line
        while (readChar != -1 && readChar != '\n') {
            // Append the read character to the string. Some operating systems
            // such as Microsoft Windows prepend newline character ('\n') with
            // carriage return ('\r'). This is part of the newline character and
            // therefore an exception that should not be appended to the string.
            if (readChar != '\r') {
                string.append((char) readChar);
            }
            // Read the next character
            readChar = reader.read();
        }
        return string.toString();
    }
}
