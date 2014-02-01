/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frc3128.Util.Connection;

import com.sun.squawk.util.Arrays;
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
 * @author jamie
 */
public class Beaglebone extends Event {

    private static final String BBURL = "socket://10.31.28.144:4545";
    private byte[] commandInProgress;
    private short placeInCommandInProgress;
    private SocketConnection socket;
    private InputStream inStream;
    private OutputStream outStream;

    public Beaglebone() {
        //hard 200 byte limit on command size
        commandInProgress = new byte[200];
        placeInCommandInProgress = -1;

        try {
            DebugLog.log(DebugLog.LVL_INFO, this, "Connecting to " + BBURL);
            connect();
        } catch (IOException ex) {
            ex.printStackTrace();
            DebugLog.log(DebugLog.LVL_SEVERE, this, "Error connecting: " + ex.getMessage());
        }
        DebugLog.log(DebugLog.LVL_INFO, this, "Connected to beaglebone.");
        this.registerIterableEvent();
    }

    public synchronized void connect() throws IOException {
        socket = (SocketConnection) Connector.open(BBURL);//, Connector.READ_WRITE, true);
        inStream = socket.openInputStream();
        outStream = socket.openOutputStream();
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
                    DebugLog.log(DebugLog.LVL_STREAM, this, "Got EOF.");
                    return;
                }
                commandInProgress[placeInCommandInProgress + 1] = (byte) data;
                DebugLog.log(DebugLog.LVL_STREAM, this, "Beaglebone read byte " + Integer.toHexString(data) + " into slot " + (placeInCommandInProgress + 1));
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

    public void execute() {
        readCommandBytes();
        //if there's one complete command in storage, parse it and use it
        if ((placeInCommandInProgress > -1) && (commandInProgress[placeInCommandInProgress] == SOSProtocol.END_TRANSMISSION)) {
            RobotCommand command = RobotCommand.factory(commandInProgress);
            DebugLog.log(DebugLog.LVL_STREAM, this, "Beaglebone read " + command.toString());
            placeInCommandInProgress = -1;
            DebugLog.log(DebugLog.LVL_STREAM, this, "Done parsing.");
        }
    }
}
