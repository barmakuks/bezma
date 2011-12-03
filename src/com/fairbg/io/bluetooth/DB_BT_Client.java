/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fairbg.io.bluetooth;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.obex.ClientSession;


public class DB_BT_Client implements DiscoveryListener {

    public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
        try {
            System.out.println("deviceDiscovered : " + rd.getFriendlyName(true));
            FoundDevices.addDevice(rd);
        } catch (IOException ex) {
            Logger.getLogger(DB_BT_Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void servicesDiscovered(int index, ServiceRecord[] srs) {
        System.out.println("servicesDiscovered");
        for(ServiceRecord rec : srs){
            System.out.println(rec.getConnectionURL(rec.NOAUTHENTICATE_NOENCRYPT, false));
        }
    }

    private void sendData(ClientSession session, byte[] data) {
        System.out.println("TRY TO SEND DATA");
        return;
        /*DataOutputStream netStream = null;
        Operation op = null;

        try {
            HeaderSet headerSet = session.createHeaderSet();
            headerSet.setHeader(HeaderSet.LENGTH, new Long(data.length));
            op = session.put(headerSet);
            netStream = op.openDataOutputStream();
            netStream.write(data);
            netStream.flush();
            System.out.println("DATA SENT");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                netStream.close();
                op.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void serviceSearchCompleted(int i, int i1) {
        System.out.println("serviceSearchCompleted on " + i );
    }

    public void inquiryCompleted(int i) {
        System.out.println("inquiryCompleted");
    }
}
