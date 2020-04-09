package com.peter;

import java.io.InputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialComm implements SerialPortEventListener {
    SerialPort serialPort;
    /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = { "COM9" };
    private InputStream in;
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 115200;
    
    public void initialize()
    {
        CommPortIdentifier portId = null;
        Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
        
        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
                    .nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }
        
        try {
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);
            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            
            // open the streams
            in = serialPort.getInputStream();
            
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    
    public synchronized void close()
    {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }
    
    static int calibrate = 0;
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent)
    {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                final int MESSAGE_SIZE = 8;
                byte[] buf = new byte[2*MESSAGE_SIZE]; // ensure we don't miss anything
                int read = 0, total = 0;
                while (total < 2*MESSAGE_SIZE && (read = in.read(buf, total,
                        2*MESSAGE_SIZE - total)) >= 0) {
                    total += read;
                }
                
                int off;
                for (off = buf.length - 1; off >= 0; --off) {
                    if (((int)buf[off]&0xFF) == 0xfe)
                        break;
                }
                off -= 8;
                if (off < 0)
                    return;
                
                int a = (buf[off] >> 0) & 1;
                int b = (buf[off] >> 1) & 1;
                int x = (buf[off] >> 2) & 1;
                int y = (buf[off] >> 3) & 1;
                int s = (buf[off] >> 4) & 1;
                /*
                int dl = (buf[off+1] >> 0 & 1);
                int dr = (buf[off+1] >> 1 & 1);
                int dd = (buf[off+1] >> 2 & 1);
                int du = (buf[off+1] >> 3 & 1);
                */
                int z  = (buf[off+1] >> 4 & 1);
                
                int r  = (buf[off+1] >> 5 & 1);
                int l  = (buf[off+1] >> 6 & 1);
                
                int xA = (int)buf[2 + off] &0xFF;
                int yA = (int)buf[3 + off] & 0xFF;
                int xC = (int)buf[4 + off] & 0xFF;
                int yC = (int)buf[5 + off] & 0xFF;
                int lT = (int)buf[6 + off] & 0xFF;
                int rT = (int)buf[7 + off] & 0xFF;
                
                if (Viewer.lock)
                    return;
                
                Viewer.lock = true;
                Viewer.values[0] = a;
                Viewer.values[1] = b;
                Viewer.values[2] = x;
                Viewer.values[3] = y;
                Viewer.values[4] = s;
                Viewer.values[5] = z;
                Viewer.values[6] = r;
                Viewer.values[7] = l;
                Viewer.values[8] = xA;
                Viewer.values[9] = yA;
                Viewer.values[10] = xC;
                Viewer.values[11] = yC;
                Viewer.values[12] = lT;
                Viewer.values[13] = rT;
                
                if (calibrate == 0) {
                    Viewer.xC = xC;
                    Viewer.yC = yC;
                    Viewer.xA = xA;
                    Viewer.yA = yA;
                    calibrate = 1;
                }
                Viewer.lock = false;
                
                if (Viewer.DEBUG) {
                    System.out.printf("a: %d b: %d x: %d y: %d s: %d z: %d ", a, b, x, y, s, z);
                    System.out.printf("xA: %d\tyA: %d\txC: %d\tyC: %d\tlT: %d\trT %d\n", xA, yA, xC, yC, lT, rT);
                }
                
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }
}
