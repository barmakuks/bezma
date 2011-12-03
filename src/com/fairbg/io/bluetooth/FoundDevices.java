/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fairbg.io.bluetooth;

import java.util.ArrayList;
import java.util.List;
import javax.bluetooth.RemoteDevice;

/**
 *
 * @author Vitalik
 */
public class FoundDevices {
    private static List<RemoteDevice> _remote_devices = new ArrayList<RemoteDevice>();

    public static void addDevice(RemoteDevice device){
        _remote_devices.add(device);
    }
    public static int getDeviceCount(){
        return _remote_devices.size();
    }
    public static RemoteDevice getDevice(int index){
        if(index >= 0 && index < getDeviceCount())
            return _remote_devices.get(index);
        else 
            return null;
    }
}
