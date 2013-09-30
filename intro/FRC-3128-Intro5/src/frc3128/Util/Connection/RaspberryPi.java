package frc3128.Util.Connection;

import com.sun.squawk.util.StringTokenizer;
import edu.wpi.first.wpilibj.Timer;
import frc3128.Util.DebugLog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

/**
 *
 * @author Yousuf Soliman
 */
//TODO Clean up connection logic and follow synchronization methods
//TODO Remove "synchronzied" from all methods and exhcange with synced blocks
//TODO Test Raspberry Pi communication
public final class RaspberryPi {
    private static final String RPiURL = "socket://10.31.28.12:4242";
    private static final int BUFFERSIZE = 64;
    private static final char DELIMITER = ',';
    private SocketConnection socket;
    private InputStream inStream;
    private OutputStream outStream;
    String rawData;
    byte[] receivedData;
    private boolean connected = false;
    private boolean enabled = false;
    private boolean running = true;

    public static class DataKeeper {
        private static int distance = 0;
        private static int offset = 0;
        private static double time = 0;
        private static boolean report = false;

        public static synchronized void setReport(boolean rprt) {
            report = rprt;
            SDWrite.writeToDashboard("RPi::Report", rprt);
        }

        public static synchronized void setDistance(int dist) {
            distance = dist;
            SDWrite.writeToDashboard("RPi::Distance", distance);
        }
        
        public static synchronized void setOffset(int offst) {
            offset = offst;
            SDWrite.writeToDashboard("RPi::Offset", offset);
        }
        
        public static synchronized void setTime(double t) {
            time = t;
            SDWrite.writeToDashboard("RPi::Time", time);
        }
        
        public static synchronized boolean getReport() {return report;}
        public static synchronized int getDistance() {return distance;}
        public static synchronized int getOffset() {return offset;}
        public static synchronized double getTime() {return time;}

        private DataKeeper() {}
    }

    private class RaspberryPiThread extends Thread {
        RaspberryPi rPi;
        public int distance;
        public int offset;
        public double time;
        private boolean report;
        
        public RaspberryPiThread(RaspberryPi rPi) {
            super("PiSocket");
            this.rPi = rPi;
        }
        
        public void run() {
            while (running) {
                if (rPi.isEnabled())
                    if (rPi.isConnected()) {
                        report = true;
                        try {
                            String[] data = rPi.tokenizeData(rPi.getRawData());
                            time = Timer.getFPGATimestamp();
                            if (data.length < 2) 
                                report = false;
                            else {
                                try {
                                    distance = Integer.parseInt(data[1]);
                                    offset = Integer.parseInt(data[0]);
                                } catch (NumberFormatException ex) {report = false;}
                            }
                        } catch (IOException ex) {report = false;}
                        DataKeeper.setReport(report);

                        if (report) {
                            DataKeeper.setDistance(distance);
                            DataKeeper.setOffset(offset);
                            DataKeeper.setTime(time);
                        }
                    } else
                        try {rPi.connect();} catch (IOException ex) {DataKeeper.setReport(false);}
                try {Thread.sleep(375);} catch (InterruptedException ex) {}
            }
        }
    }
    
    public RaspberryPi() {
        Thread thread = new RaspberryPiThread(this);
        try {connect();} catch (IOException ex) {
            ex.printStackTrace();
            DebugLog.log(DebugLog.LVL_SEVERE, this, ex.getMessage());
        }
        thread.start();
    }
    
    public synchronized void connect() throws IOException {
        socket = (SocketConnection) Connector.open(RPiURL);//, Connector.READ_WRITE, true);
        inStream = socket.openInputStream();
        outStream = socket.openOutputStream();
        connected = true;
    }
    public synchronized void disconnect() throws IOException {
        socket.close();
        inStream.close();
        outStream.close();
        connected = false;
    }
    public synchronized boolean isConnected() {
        //need to actually test the connection 
        //to figure out if we're connected or not
        try {
            outStream.write('\n'); //request Data
            connected = true;
        } catch (IOException ex) {connected = false;} catch (Exception ex) {connected = false;}

        return connected;
    }
    
    public synchronized boolean isEnabled() {return enabled;}
    public int getOffset() {return DataKeeper.getOffset();}
    public int getDistance() {return DataKeeper.getDistance();}
    public double getTime() {return DataKeeper.getTime();}
    public boolean getReport() {return DataKeeper.getReport();}
    public synchronized void start() {enabled = true;}
    public synchronized void stop() {enabled = false;}
    
    public synchronized String getRawData() throws IOException {
        byte[] input;

        if (connected) {
            outStream.write('G'); //request Data
            DebugLog.log(DebugLog.LVL_DEBUG, this, "Requested Data");

            if (inStream.available() <= BUFFERSIZE) {
                input = new byte[inStream.available()]; //storage space sized to fit!
                receivedData = new byte[inStream.available()];
                inStream.read(input);
                for (int i = 0; (i < input.length) && (input != null); i++)
                    receivedData[i] = input[i]; //transfer input to full size storage
            } else {
                DebugLog.log(DebugLog.LVL_WARN, this, "Data buffer overflow!");
                inStream.skip(inStream.available()); //reset if more is stored than buffer
                return null;
            }

            rawData = ""; //String to transfer received data to
            DebugLog.log(DebugLog.LVL_DEBUG, this, "Raw data size: " + receivedData.length);
            for (int i = 0; i < receivedData.length; i++)
                rawData += (char) receivedData[i]; //Cast bytes to chars and concatenate them to the String\
            DebugLog.log(DebugLog.LVL_DEBUG, this, "Raw data: " + rawData);
            return rawData;
        } else {connect(); return null;}
    }

    public synchronized String[] tokenizeData(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input, String.valueOf(DELIMITER));
        String output[] = new String[tokenizer.countTokens()];
        
        for (int i = 0; i < output.length; i++)
            output[i] = tokenizer.nextToken();
        return output;
    }
}
