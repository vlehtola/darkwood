/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.darkwood.party;

import java.util.Vector;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 *
 * @author TKivimaki
 */
public class InquiryListener implements DiscoveryListener {
    public Vector cached_devices;
    public InquiryListener() {
        cached_devices = new Vector();
    }
    public void deviceDiscovered( RemoteDevice btDevice, DeviceClass cod ){
        int major = cod.getMajorDeviceClass();
        if( ! cached_devices.contains( btDevice ) ){
            cached_devices.addElement( btDevice );
        }
    }
    public void inquiryCompleted( int discType ){
        synchronized(this){this.notify(); }
    }
    public void servicesDiscovered( int transID, ServiceRecord[] servRecord ){}
    public void serviceSearchCompleted( int transID, int respCode ){}
}

