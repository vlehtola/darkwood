/*
 * Network.java
 *
 * Created on May 18, 2007, 4:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fi.darkwood.network;

import fi.darkwood.util.Properties;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;

/**
 *
 * @author Tommi Laukkanen
 */
public class Network {

    public static void sendGameBeginNotification(String characterName,String characterClass) {
        sendMessage("GB "+characterClass+" "+characterName);
    }
    
    public static void sendGameOverNotification(String characterName,String characterClass,int level,int exp, int gold,String helmet, String breastArmour,String shield,String weapon) {
        sendMessage("GO "+characterClass+" "+level+" "+exp+" "+gold+" "+helmet+" "+breastArmour+" "+shield+" "+weapon+" "+characterName);
    }
    
    public static void sendMessage(String message) {
        getSingleton().pushOutputMessage(message);
    }
    
    public static int getMessageOutputQueueSize() {
        return getSingleton().getOutputQueueSize();
    }
    
    
    private static Network singleton;
    private static Network getSingleton() {
        if(singleton==null) {
            singleton=new Network();
        }
        
        return singleton;
    }

    private boolean isOperational=true;
    private Thread startThread=null;
    private Thread sendThread=null;
    private Thread receiveThread=null;
    private DatagramConnection connection;
    private Vector outputMessageQueue=new Vector();
    private Vector inputMessageQueue=new Vector();
    
    /** Creates a new instance of Network */
    public Network() {
        
        startThread=new Thread(new Runnable() {
        
            public void run() {
                try {
                    connection=(DatagramConnection)Connector.open("datagram://");
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    isOperational=false;
                }
                
                sendThread=new Thread(new Runnable() {
                    public void run() {
                        sendLoop();
                    }
                });
                
                receiveThread=new Thread(new Runnable() {
                    public void run() {
                        receiveLoop();
                    }
                });
                
                sendThread.start();
                receiveThread.start();
                
            }
        });
        
        startThread.start();
    }
    
    public synchronized void pushOutputMessage(String message) {
        outputMessageQueue.addElement(message);
    }
    
    public synchronized String popOutputMessage() {
        if(outputMessageQueue.size()>0) {
            String message=(String)outputMessageQueue.elementAt(0);
            outputMessageQueue.removeElementAt(0);
            return message;
        } else {
            return null;
        }
    }
    
    public synchronized void pushInputMessage(String message) {
        inputMessageQueue.addElement(message);
    }
    
    public synchronized String popInputMessage() {
        if(inputMessageQueue.size()>0) {
            String message=(String)inputMessageQueue.elementAt(0);
            inputMessageQueue.removeElementAt(0);
            return message;
        } else {
            return null;
        }
    }
    
    public synchronized int getOutputQueueSize() {
        return outputMessageQueue.size();
    }
    
    public synchronized int getInputQueueSize() {
        return inputMessageQueue.size();
    }
    
    public void sendLoop() {
        while(true) {
            
            String outMessage=popOutputMessage();
            if(outMessage!=null) {
                byte[] outMessageBytes=outMessage.getBytes();
                if(isOperational) {
                    try {
                        Datagram outDatagram=connection.newDatagram(outMessageBytes,outMessageBytes.length);
                        outDatagram.setAddress("datagram://"+Properties.getApplicationProperties().get("Server-Address")+":"+Properties.getApplicationProperties().get("Server-Port"));
                        connection.send(outDatagram);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
            
            try {
                Thread.sleep(300);
            } catch (Exception e) {}
        }
    }
    
    public void receiveLoop() {
        Datagram datagram=null;
        
        try {
            datagram=connection.newDatagram(connection.getMaximumLength());
        } catch (Throwable ex) {
            ex.printStackTrace();
            isOperational=false;
            return;
        }
        
        while(true) {
            
            try {
                connection.receive(datagram);
                String inputMessage=new String(datagram.getData(),0,datagram.getLength());
                pushInputMessage(inputMessage);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
            
            try {
                Thread.sleep(300);
            } catch (Exception e) {}
            
        }
    }
    
}
