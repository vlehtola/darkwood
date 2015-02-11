/*
 * Logger.java
 *
 * Created on April 3, 2008, 8:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.mirake.logger.MIDPLogger;
import java.util.Date;
import java.util.Vector;

/**
 * Logger class. Shows messages in System.Out and ingame console.
 * 
 * @author Ville
 */
public class Logger {

    private static Logger instance = null;
    public Vector messages = new Vector();
//    FileConnection fconn = null;
//    PrintStream printStream = null;

    private MIDPLogger log;
    
    protected Logger() {
       // try {
            // Current problem: What is a valid file system path where to write?
            // Not ok: file:///C:/Users/Ville/Desktop/fileconnection_spec_1.00/FileConnection.html

            //fconn = (FileConnection) Connector.open("file:///root1/darkwood.txt", Connector.READ_WRITE);



            // If no exception is thrown, then the URI is valid, but the file may or may not exist.
            //    Enumeration e = fconn.list();
            //      while (e.hasMoreElements()) {
            //        FileConnection f = (FileConnection) e.nextElement();
            //        System.out.println("LOGGERFILE: " + f.getName());
            //    }


            /*   Enumeration e = FileSystemRegistry.listRoots();
            
            while (e.hasMoreElements()) {
            String str = (String) e.nextElement();
            System.out.println(str);
            } */

            /* if (!fconn.exists()) {
            fconn.create();  // create the file if it doesn't exist
            } */
//            fconn.close();


            try {
                log = new MIDPLogger(MIDPLogger.DEBUG, true, false);
            } catch (Exception e) {
                System.out.println("Exception creating MIDPLogger");
                e.printStackTrace();
            }

            System.out.println("Logger file ok!");

     /*       OutputStream out = fconn.openOutputStream();
            printStream = new PrintStream(out);
*/
            logToFile("Log file initiated.");
/*        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
        }*/ 
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    // We do not try to catch here.
    public void debug(String message) {
        if (message == null) {
            message = "Logger called with null message.";
        }
        //   Date date= new Date();
        //   Calendar cal = Calendar.getInstance();
        //   cal.setTime(date);
        //    String strDate = cal.get(Calendar.HOUR_OF_DAY)+":"+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);


        //message = strDate + ": " + message;

        // add to console log
        messages.addElement(message);
        // if console messagebuffer is full, remove the oldest message
        if (messages.size() > 15) {
            messages.removeElementAt(0);
        }

        // also print to system.out
        System.out.println(message);



    }

    public void logToFile(String message) {
        Date d = new Date();
        log.write(d.toString() + ": " + message, MIDPLogger.DEBUG);
     //   System.out.println(d.toString() + ": " + message);
        /*       if (printStream == null) {
            return;
        }

        // print to file
        printStream.println(message);  */
    }
}
