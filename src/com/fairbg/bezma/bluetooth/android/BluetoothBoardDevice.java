package com.fairbg.bezma.bluetooth.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

import com.fairbg.bezma.bluetooth.Datagram;
import com.fairbg.bezma.bluetooth.IDatagramObservable;
import com.fairbg.bezma.bluetooth.IDatagramObserver;
import com.fairbg.bezma.log.BezmaLog;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

/**
 * Communicates with board. Can listen and send datagrams through
 * Bluetooth СОМ-port. Android implementation of IDatagramObservable
 */
public class BluetoothBoardDevice implements IDatagramObservable
{

    /** Communication with Bluetooth */
    private BluetoothDevice m_Device = null;
    /** СОМ-service identifier */
    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /** read-write thread */
    CommunicationThread m_CommunicationThread = null;

    public BluetoothBoardDevice(BluetoothDevice device)
    {
        m_Device = device;
    }

    /** Datagram listeners list*/
    private ArrayList<IDatagramObserver> m_Observers = new ArrayList<IDatagramObserver>();

    @Override
    public void addObserver(IDatagramObserver observer)
    {
        m_Observers.add(observer);
    }

    @Override
    public void removeObserver(IDatagramObserver observer)
    {
        m_Observers.remove(observer);
    }

    @Override
    public void notifyAll(Datagram datagram)
    {
        for (IDatagramObserver observer : m_Observers)
        {
            observer.handleEvent(datagram);
        }
    }

    /** Runs read-write data thread*/
    public boolean startListen() throws IOException
    {
        // BluetoothSocket socket =
        // m_Device.createRfcommSocketToServiceRecord(MY_UUID);
        Method m;
        BluetoothSocket socket = null;
        try
        {
            m = m_Device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
            socket = (BluetoothSocket) m.invoke(m_Device, 1);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        try
        {
            if (socket != null)
            {
                socket.connect();
                m_CommunicationThread = new CommunicationThread(socket);
                m_CommunicationThread.start();
            }
            else
            {
                return false;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /** Stop data read-write thread */
    public void stopListen()
    {
        if (m_CommunicationThread != null)
        {
            m_CommunicationThread.stopListen();
            m_CommunicationThread = null;
        }
    }

    /**
     * Read-write thread callback functions. Is called when datagram data received.
     * @details Try to parse incoming data into datagram. If parse success notifies all listeners about datagram received.
     * */
    private void datagramReceived(byte[] array)
    {
        Datagram dg = Datagram.parse(array);

        if (dg != null)
        {
            notifyAll(dg);
        }
    }

    /**
     * Sends datagram as byte array into outgoing stream
     * 
     * @details Converts datagram into byte array and sends it into COM port
     */
    public void sendDatagram(Datagram datagram)
    {
        if (datagram != null && m_CommunicationThread != null)
        {
            byte[] bytes = datagram.toByteArray();
            
            m_CommunicationThread.write(bytes);
        }

    }

    private static final int MESSAGE_READ = 1;
    private static final int INSIDE_THREAD = 2;

    private StringBuilder current_message = new StringBuilder();
    ArrayList<Byte> buffer = new ArrayList<Byte>();

    /** Handler for received data */
    private Handler mHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {

            synchronized (current_message)
            {

                if (msg.what == MESSAGE_READ)
                {

                    byte[] buf = (byte[]) msg.obj;

                    for (int i = 0; i < msg.arg1; i++)
                    {

                        if (buf[i] == (byte) 0xFE)
                        {
                            current_message.setLength(0);
                        }

                        current_message.append(String.format("%1$02X", buf[i]));
                        buffer.add(buf[i]);

                        if (buf[i] == (byte) 0xFF)
                        {
                            byte[] array = new byte[buffer.size()];
                            for (int j = 0; j < buffer.size(); array[j] = buffer.get(j++))
                                ;
                            datagramReceived(array);
                            buffer.clear();
                            current_message.setLength(0);
                        }
                    }
                }
                return false;
            }
        }
    });

    /** Read-write data thread*/
    private class CommunicationThread extends Thread
    {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public CommunicationThread(BluetoothSocket socket)
        {
            // this.setDaemon(true);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try
            {
                if (socket != null)
                {
                    tmpIn = socket.getInputStream();
                    tmpOut = socket.getOutputStream();
                }
            }
            catch (IOException e)
            {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        /** Main thread for read data*/
        public void run()
        {
            byte[] buffer = new byte[1024]; // buffer store for the stream
            byte[] out_buffer = new byte[1024]; // buffer to send data to
                                                // handler
            int index = 0; // current index in out_buffer;

            int bytes = 0; // bytes returned from read()
            mHandler.obtainMessage(INSIDE_THREAD);
            // Keep listening to the InputStream until an exception occurs
            while (true)
            {
                try
                {
                    // Read from the InputStream
                    if (mmInStream != null)
                    {
                        BezmaLog.i("AVAILABLE BYTES", Integer.toString(mmInStream.available()));
                        bytes = mmInStream.read(buffer);
                    }

                    for (int i = 0; i < bytes; i++)
                    {
                        out_buffer[index++] = buffer[i];
                        if (buffer[i] == (byte) 0xFF || index >= out_buffer.length)
                        {
                            mHandler.obtainMessage(MESSAGE_READ, index, -1, out_buffer).sendToTarget();
                            index = 0;
                        }
                    }
                }
                catch (IOException e)
                {
                    break;
                }
            }
        }

        /** Writes byte array into outgoing stream*/
        public void write(byte[] bytes)
        {
            try
            {
                mmOutStream.write(bytes);
            }
            catch (IOException e)
            {
            }
        }

        /** Stop read-write thread*/
        public void stopListen()
        {
            try
            {
                mmSocket.close();
            }
            catch (IOException e)
            {
            }
        }
    }

}
