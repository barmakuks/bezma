package com.fairbg.io.bluetooth;

//import android.util.Log;
import com.fairbg.core.commands.Command;
import com.fairbg.core.commands.CommandFactory;
import com.fairbg.core.commands.CommanderImpl;
import com.fairbg.core.commands.ICommandListener;
import com.intel.bluetooth.BluetoothConnectionAccess;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import sun.security.util.Debug;

public class BluetoothCommander implements com.fairbg.core.commands.ICommander {

    UUID thatService = new UUID("00B0D00154EF", false);
    UUID uuid = new UUID("1101", true);

    public static void searchServices() {
        try {
            LocalDevice device = LocalDevice.getLocalDevice();
            System.out.println("ADDRESS : " + device.getBluetoothAddress());
            DiscoveryAgent agent = device.getDiscoveryAgent();
            DiscoveryListener listener = new DB_BT_Client();
            System.out.println("start searching devices");
            agent.startInquiry(DiscoveryAgent.GIAC, listener);
            agent.cancelInquiry(listener);
            System.out.println("device search complete");
            System.out.println("Start search services");
            for (int i = 0; i < FoundDevices.getDeviceCount(); i++) {
                try {
                    String name = FoundDevices.getDevice(i).getFriendlyName(false);
                    if (name.indexOf("LG KP500") != -1) {
                        System.out.println("Skip services on " + name);
                    } else {
                        System.out.println("Search services on " + i + " " + name);

                        //UUID[] uuidSet = new UUID[]{new UUID("00001101-0000-1000-8000-00805F9B34FB", true)};
                        UUID[] uuidSet = new UUID[]{new UUID(0x1101)};
                        int[] attrSet = null;
                        agent.searchServices(attrSet, uuidSet, FoundDevices.getDevice(i), listener);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(BluetoothCommander.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (BluetoothStateException ex) {
            Logger.getLogger(BluetoothCommander.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    CommanderImpl _commander = null;

    @Override
    public void addListener(ICommandListener listener) {
        if (_commander != null) {
            _commander.addListener(listener);
        }
    }

    @Override
    public void sendCommand(Command command) {
        System.out.println("Command sent");
        if (_commander != null) {
            _commander.sendCommand(command);
        }
    }

    public BluetoothCommander(ICommandListener listener) {
        _commander = new CommanderImpl(this);
        _commander.addListener(listener);
    }
    private volatile boolean _run_thread = false;
    private byte[] _buffer = new byte[1024];
    private int _buffer_index = 0;

    private void addByteToBuffer(byte b) {
        if (_buffer_index == 1024) {
            _buffer_index = 0;
        }
        if (b == 3) {
            _buffer[_buffer_index] = b;
            for (int i = 0; i < _buffer_index; i++) {
                if (_buffer[i] < 10) {
                    System.out.print("0x" + Integer.toHexString(_buffer[i]));
                } else {
                    System.out.print((char) _buffer[i]);
                }
            }
            System.out.println();
            Command cmd = CommandFactory.parse(_buffer);
            if (cmd != null) {
                sendCommand(cmd);
            }
            _buffer_index = 0;
        } else {
            if (b == 2) {
                _buffer_index = 0;
            }
            _buffer[_buffer_index++] = b;
        }
    }
    private Thread _thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Connection connection = null;
            InputStream is = null;

            StreamConnection stream_connection = null;
            try {
                final String url = "btspp://0080251290DC:1;authenticate=false;encrypt=false;master=false";
                byte[] buffer = new byte[200];
                connection = Connector.open(url, Connector.READ, true);                
                stream_connection = (StreamConnection) connection;
                is = stream_connection.openDataInputStream();
                while (_run_thread) {
                    // read data from client
                    int len = 0;                    
                    if (is.available() > 0) {
                        
                        len = is.read(buffer);
                        for (int i = 0; i < len; i++) {
                            addByteToBuffer(buffer[i]);
                        }
                    }
                }
                if (is != null) {
                    try {
                        Debug.println("STOP Bluetooth", "Try to close Bluetooth input stream");
                        is.close();
                        Debug.println("STOP Bluetooth", "done");
                    } catch (IOException ex) {
                        Debug.println("STOP Error", ex.getMessage());
                        Logger.getLogger(BluetoothCommander.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (stream_connection != null) {
                    try {
                        Debug.println("STOP Bluetooth", "Try to close connection");
                        stream_connection.close();
                        Debug.println("STOP Bluetooth", "done");
                    } catch (IOException ex) {
                        Debug.println("STOP Error", ex.getMessage());
                        Logger.getLogger(BluetoothCommander.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } catch (IOException ex) {
                Debug.println("", ex.getMessage());
                Logger.getLogger(BluetoothCommander.class.getName()).log(Level.SEVERE, null, ex);
            } 


        }
    });

    @Override
    public void start() {
        _run_thread = true;
        _thread.start();
    }

    @Override
    public void stop() {
        //System.out.println("Try to stop thread");
        _run_thread = false;
    }

    public void recieveCommand(Command command) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
