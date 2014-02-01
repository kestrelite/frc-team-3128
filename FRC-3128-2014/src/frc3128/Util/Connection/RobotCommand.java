/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frc3128.Util.Connection;

import frc3128.Util.DebugLog;
import java.util.Vector;

/**
 *
 * @author jamie
 */
public class RobotCommand {
    //this is an unsigned int in the c++ program
    //but we need to round up here
    //java, folks

    public int _return_id;
    public byte _opcode;
    public Vector _params;
    public String _extraString;

    public RobotCommand(int return_id, byte opcode) {
        _return_id = return_id;
        _opcode = opcode;
    }

    public RobotCommand(int return_id, byte opcode, Vector shorts) {
        _return_id = return_id;
        _opcode = opcode;
        _params = shorts;
    }

    public RobotCommand(int return_id, byte opcode, Vector shorts, String extraString) {
        _return_id = return_id;
        _opcode = opcode;
        _params = shorts;
        _extraString = extraString;
    }

    public RobotCommand(int return_id, byte opcode, String extraString) {
        _opcode = opcode;
        _extraString = extraString;
    }

    public String toString() {
        String string = "Robot Command dump:---------------\n";
        string += "Return ID: " + _return_id + "\n";
        string += "Opcode: " + Integer.toHexString(_opcode) + "\n";
        //Print out each short
        if (_params != null) {
            string += "Shorts: ";
            for (int counter = 0; counter < _params.size(); ++counter) {
                string += _params.elementAt(counter) + " ";
            }
            string += "\n";
        }

        if (_extraString != null) {
            string += "Extra String: " + _extraString + "\n";
        }
        return string;
    }

    //Parsing code originally written in c++, where it was a lot... better
    public static RobotCommand factory(byte[] currentBytePtr) {
        int return_id = parseReturnID(currentBytePtr);
        ++iterator;
        byte opcode = parseOpcode(currentBytePtr);
        iterator++;
        Vector shorts = parseShorts(currentBytePtr);
        String extraString = parseString(currentBytePtr);
        iterator = 0;
        return new RobotCommand(return_id, opcode, shorts, extraString);
    }
    //needs to be class scope so that it can keep its state after a function call
    static int iterator = 0;

    static byte parseOpcode(byte[] currentBytePtr) {
        // Get opcode.
        ++iterator;
        byte opcode = currentBytePtr[iterator];
        ++iterator;

        if (currentBytePtr[iterator] != SOSProtocol.END_OPCODE) {
            // Bail and return empty value.
            DebugLog.log(DebugLog.LVL_ERROR, null, "RobotCommand: the end of the opcode command is incorrect\nShould be:" + SOSProtocol.END_OPCODE + " Was: " + currentBytePtr[iterator]);
            return 0x0;
        }

        return opcode;
    }

    static int parseReturnID(byte[] currentBytePtr) {
        if (currentBytePtr[iterator] != SOSProtocol.START_TRANSMISSION) {
            // Bail and return empty value.
            DebugLog.log(DebugLog.LVL_ERROR, "RobotCommand", "the header of the command is incorrect\nShould be:" + SOSProtocol.START_TRANSMISSION + " Was: " + currentBytePtr[iterator]);
            return 0x0;
        }
        // Have start of command.  Begin parsing.
        ++iterator;
        //should now be the first byte of the ASCII-encoded id

        //max of 10 ASCII digit short
        char[] returnBytesStorage = new char[10];
        int returnBytesIterator = 0;

        while (currentBytePtr[++iterator] != SOSProtocol.END_ID) {
            returnBytesStorage[returnBytesIterator] = (char) currentBytePtr[iterator];
            ++returnBytesIterator;
        }
        return Integer.parseInt(String.valueOf(returnBytesStorage, 0, returnBytesIterator));
    }

    static Vector parseShorts(byte[] currentBytePtr) {
        if (currentBytePtr[iterator] == SOSProtocol.START_SHORT_TRANSMISSION) {
            Vector shortVector = new Vector();

            // Loop until we don't recieve the start of a short
            while (currentBytePtr[iterator] == SOSProtocol.START_SHORT_TRANSMISSION) {
                //max of 10 ASCII digit short
                char[] shortBytesStorage = new char[10];
                int shortBytesIterator = 0;
                ++iterator;
                while (currentBytePtr[iterator] != SOSProtocol.END_SHORT) {
                    shortBytesStorage[shortBytesIterator++] = (char) currentBytePtr[iterator++];
                }
                shortVector.addElement(Integer.valueOf(String.valueOf(shortBytesStorage, 0, shortBytesIterator)));
                ++iterator;
            }
            return shortVector;
        } else {
            return null;
        }

    }

    static String parseString(byte[] currentBytePtr) {
        if (currentBytePtr[iterator] == SOSProtocol.START_STRING_TRANSMISSION) {
            String stringstream = new String();
            while (currentBytePtr[++iterator] != SOSProtocol.END_STRING) {
                stringstream += currentBytePtr[iterator];
            }
            return stringstream;
        }
        return null;
    }
    
    public String params() {
        return (this._params == null ? "" : this._params.toString());
    }
}
