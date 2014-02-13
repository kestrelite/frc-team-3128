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

    public int _return_id;
    public byte _opcode;
    public Vector _params;
    public String _string;

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
        _string = extraString;
    }

    public RobotCommand(int return_id, byte opcode, String extraString) {
        _opcode = opcode;
        _string = extraString;
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

        if (_string != null) {
            string += "Extra String: " + _string + "\n";
        }
        return string;
    }

    //Parsing code originally written in c++, where it was a lot... better
    public static RobotCommand factory(byte[] currentBytePtr) {
        int return_id = parseReturnID(currentBytePtr);
        ++iterator;
        byte opcode = parseOpcode(currentBytePtr);
        iterator++;
        Vector shorts = parseParams(currentBytePtr);
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

    static Vector parseParams(byte[] currentBytePtr) {
        if (currentBytePtr[iterator] == SOSProtocol.START_PARAM) {
            Vector shortVector = new Vector();

            // Loop until we don't recieve the start of a short
            while (currentBytePtr[iterator] == SOSProtocol.START_PARAM) {
                //max of 10 ASCII digit short
                char[] paramBytesStorage = new char[10];
                int paramBytesIterator = 0;
                ++iterator;
                while (currentBytePtr[iterator] != SOSProtocol.END_PARAM) {
                    paramBytesStorage[paramBytesIterator++] = (char) currentBytePtr[iterator++];
                }
                shortVector.addElement(Double.valueOf(String.valueOf(paramBytesStorage, 0, paramBytesIterator)));
                ++iterator;
            }
            return shortVector;
        } else {
            return null;
        }

    }

    static String parseString(byte[] currentBytePtr) {
        if (currentBytePtr[iterator] == SOSProtocol.START_STRING) {
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
/**
 * Encodes this RobotCCommand encodes it back into a binary transmission
 * @param command
 * @return 
 */
    byte[] reencodeCommand()
    {
        byte[] returnIDBytes = reencodeReturnID();
        
        byte[] opcodeBytes = reencodeOpcode();
        
        byte[] paramBytes = null;
        
        if(_params != null)
        {
            paramBytes = reencodeParams();
        }
        
        byte[] stringBytes = {0};
        
        if(_string != null) 
        {
            stringBytes = reencodeString();
        }
        
        //create the final array, adding up the total lengths of all the parts if they exist
        byte[] commandBytes = new byte
        [
                returnIDBytes.length + 
                opcodeBytes.length + 
                (_params != null ? paramBytes.length : 0) + 
                (_string != null ? stringBytes.length : 0) +
                1 /* for the end transmission character*/
        ];
        
        System.arraycopy(returnIDBytes, 0, commandBytes, 0, returnIDBytes.length);
        System.arraycopy(opcodeBytes, 0, commandBytes, returnIDBytes.length, opcodeBytes.length);
        
        if(_params != null)
        {
            System.arraycopy(paramBytes, 0, commandBytes, (returnIDBytes.length + opcodeBytes.length), paramBytes.length);
        }
        
        if(_string != null)
        {
            System.arraycopy(stringBytes, 0, commandBytes, (returnIDBytes.length + opcodeBytes.length + paramBytes.length), stringBytes.length);
        }
        
        //add end transmission character
        commandBytes[commandBytes.length - 1] = SOSProtocol.END_TRANSMISSION;
        
        return commandBytes;
    }
    
    private byte[] reencodeReturnID() 
    {
        String returnID = SOSProtocol.START_TRANSMISSION + 
                SOSProtocol.START_ID +
                Integer.toString(_return_id) +
                SOSProtocol.END_ID;
        
        return returnID.getBytes();
    }
    
    private byte[] reencodeOpcode() 
    {
        byte[] opcodeBytes = new byte[]{SOSProtocol.START_OPCODE, _opcode, SOSProtocol.END_OPCODE};
        
        return opcodeBytes;
    }
    
    private byte[] reencodeParams() 
    {
        String paramBytes = Integer.toString(SOSProtocol.START_PARAM);
        
        for(int counter = 0; counter < _params.size(); --counter)
        {
            paramBytes += _params.elementAt(counter).toString();
        }
        
        paramBytes += SOSProtocol.END_PARAM;
        
        return paramBytes.getBytes();
    }
    
    private byte[] reencodeString() 
    {
        String string = SOSProtocol.START_STRING + 
                _string +
                SOSProtocol.END_STRING;
        
        return string.getBytes();
    }


}