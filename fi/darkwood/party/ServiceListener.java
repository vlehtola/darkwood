/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.party;

import fi.darkwood.Logger;
import java.io.IOException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 *
 * @author TKivimaki
 */

class ServiceListener implements DiscoveryListener {
    public ServiceRecord service;
    public ServiceListener(){}
    public void servicesDiscovered( int transID, ServiceRecord[] servRecord ){
        Logger.getInstance().debug("Services found: " + servRecord.length);
        for (int i = 0; i < servRecord.length; i++) {
            RemoteDevice rd = servRecord[i].getHostDevice();
 
            try {
            Logger.getInstance().debug(rd.getFriendlyName(false) + " - " + servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false));
                
            } catch (IOException e) { Logger.getInstance().debug("IOException printing service name"); }
        }
        service = servRecord[0];
    }
    public void serviceSearchCompleted( int transID, int respCode ){
        synchronized( this ){this.notify();}
    }
    public void deviceDiscovered( RemoteDevice btDevice, DeviceClass cod ){}
    public void inquiryCompleted( int discType ){}
}
