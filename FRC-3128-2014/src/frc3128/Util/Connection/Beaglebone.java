package frc3128.Util.Connection;

import frc3128.EventManager.Event;
import frc3128.Util.DebugLog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

/**
 * Connects over SPI with the Beaglebone
 *
 * @author Jamie
 */
public final class Beaglebone extends Event {
    private static final String BBURL = "socket://10.31.28.13:4545";
    private byte[] commandInProgress;
    private short placeInCommandInProgress;
    private SocketConnection socket;
    private InputStream inStream;
    private OutputStream outStream;

    public Beaglebone() {
        //hard 200 byte limit on command size
        commandInProgress = new byte[200];
        placeInCommandInProgress = -1;

        DebugLog.log(DebugLog.LVL_INFO, this, "Connecting to " + BBURL);
        connect();
        this.registerIterableEvent();
    }
    
    public synchronized void connect() {
        (new Thread() {
            public void run() {
                try {
                    socket = (SocketConnection) Connector.open(BBURL);
                    inStream = socket.openInputStream();
                    outStream = socket.openOutputStream();
                    DebugLog.log(DebugLog.LVL_INFO, "Beaglebone", "The Beaglebone connection has been established.");
                } catch (IOException ex) {
                    if(ex.getMessage().startsWith("ConnectException"))
                        DebugLog.log(DebugLog.LVL_ERROR, "Beaglebone", "The Beaglebone connection could NOT be established!");
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * read bytes from serial port into commandInProgress stops if it finds an
     * SOSProtocol.END_TRANSMISSION
     *
     * @return
     */
    void readCommandBytes() {
        try {
            if (inStream.available() > 0) {
                int data = inStream.read();
                //check for EOF
                if (data == -1) {
                    //DebugLog.log(DebugLog.LVL_DEBUG, this, "Got EOF.");
                    return;
                }
                commandInProgress[placeInCommandInProgress + 1] = (byte) data;
                //DebugLog.log(DebugLog.LVL_DEBUG, this, "Beaglebone read byte " + Integer.toHexString(data) + " into slot " + (placeInCommandInProgress + 1));
                ++placeInCommandInProgress;
            }
        } catch (IOException ex) {
            DebugLog.log(DebugLog.LVL_ERROR, ex, "Connection Error!");
            ex.printStackTrace();
        }
    }

    public RobotCommand getCmd() {
        readCommandBytes();
        //if there's one complete command in storage, parse it and use it
        if ((placeInCommandInProgress > -1) && (commandInProgress[placeInCommandInProgress] == SOSProtocol.END_TRANSMISSION)) {
            RobotCommand command = RobotCommand.factory(commandInProgress);
            placeInCommandInProgress = -1;
            return command;
        }
        return null;
    }
    
    /**
     * Sends the byte array over the socket connection
     * 
     * @param command 
     */
    public void sendCmd(byte[] command) {
        try {
            outStream.write(command);
        } catch (IOException ex) {
            DebugLog.log(DebugLog.LVL_ERROR, this, "Error writing to output stream: " + ex.getMessage());
        }
    }
    
    /**
     * Converts the command to bytes and then sends it.
     * Equivalent to calling sendCmd(someCommand.reencodeCommand());
     * 
     * @param command 
     */
    public void sendCmd(RobotCommand command) {sendCmd(command.reencodeCommand());}

    public void execute() {
        if(socket == null) return;
        readCommandBytes();
        //if there's one complete command in storage, parse it and use it
        if ((placeInCommandInProgress > -1) && (commandInProgress[placeInCommandInProgress] == SOSProtocol.END_TRANSMISSION)) {
            RobotCommand command = RobotCommand.factory(commandInProgress);
            this.sendCmd(command);
            //DebugLog.log(DebugLog.LVL_DEBUG, this, "Beaglebone read " + command.toString());
            placeInCommandInProgress = -1;
            //DebugLog.log(DebugLog.LVL_DEBUG, this, "Done parsing.");
        }
    }
}
