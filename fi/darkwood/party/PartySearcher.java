/*
 * Copyright Mirake Ltd. All Rights reserved.
 */
package fi.darkwood.party;

import fi.darkwood.Logger;
import fi.darkwood.ui.view.TavernView;
import java.io.IOException;
import java.util.Enumeration;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;

/**
 *
 * @author Administrator
 */
public class PartySearcher implements Runnable {

    Party party;
    private InquiryListener inq_listener;
    private ServiceListener serv_listener;
    private TavernView view;

    public PartySearcher() {
//        this.party = party;
    }

    public void search(TavernView view) {
        this.view = view;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            Logger.getInstance().logToFile("Starting BT search..");
            try {
                LocalDevice local_device = LocalDevice.getLocalDevice();
                DiscoveryAgent disc_agent = local_device.getDiscoveryAgent();
//            local_device.setDiscoverable(DiscoveryAgent.GIAC);
                inq_listener = new InquiryListener();
                synchronized (inq_listener) {
                    disc_agent.startInquiry(DiscoveryAgent.GIAC, inq_listener);
                    try {
                        inq_listener.wait();
                    } catch (InterruptedException e) {
                        Logger.getInstance().debug("Interrupted in device discovery.");
                    }
                }


                Enumeration devices = inq_listener.cached_devices.elements();
                UUID[] u = new UUID[]{new UUID("00000000000010008000006057028A06", false)};
                int attrbs[] = {0x0100};
                serv_listener = new ServiceListener();
                while (devices.hasMoreElements()) {
                    RemoteDevice device = (RemoteDevice) devices.nextElement();
                    try {
                        Logger.getInstance().logToFile("Name: " + device.getFriendlyName(true) + " Addr: " + device.getBluetoothAddress());
                    } catch (IOException e) {
                        Logger.getInstance().logToFile(e.getMessage());
                        Logger.getInstance().logToFile("BT: IOException reading RemoteDevice");
                    }
                    synchronized (serv_listener) {
                        try {

                            disc_agent.searchServices(attrbs, u, device, serv_listener);

                            try {
                                serv_listener.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        } catch (NullPointerException e) {
                            // If a device is found but it doesnt offer any services, a null pointer might be thrown
                            // http://java.sun.com/products/sjwtoolkit/wtk2.5.2/docs/BinaryReleaseNotes.html#knownbugs
                        }
                    }

                    if (serv_listener.service != null) {
                        Logger.getInstance().logToFile("Service: " + serv_listener.service.toString());
                        view.addAvailableParty(serv_listener.service);
                    }
                }
            } catch (BluetoothStateException e) {
                Logger.getInstance().debug("BTStateEx:" + e.getMessage());
                e.printStackTrace();
            }

            // notify search complete
            view.notifySearchComplete();

            if (serv_listener.service != null) {
                /*            try {
                String url;
                url = serv_listener.service.getConnectionURL(0, false);
                deviceName = LocalDevice.getLocalDevice().getFriendlyName();
                con = (L2CAPConnection) Connector.open(url);
                } */
            }



        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
