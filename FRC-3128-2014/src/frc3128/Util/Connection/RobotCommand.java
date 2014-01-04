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
public class RobotCommand 
{
    byte _opcode;
    
    Vector _shorts;
    
    String _extraString;
    
    public RobotCommand(byte opcode)
    {
        _opcode = opcode;
    }
    
    public RobotCommand(byte opcode, Vector shorts)
    {
        _opcode = opcode;
        
        _shorts = shorts;
    }
    
    public RobotCommand(byte opcode, Vector shorts, String extraString)
    {
        _opcode = opcode;
        
        _shorts = shorts;
        
        _extraString = extraString;
    }
    
    public RobotCommand(byte opcode, String extraString)
    {
        _opcode = opcode;
        
        _extraString = extraString;
    }
    
    public String toString()
    {
        String string = "Robot Command dump:---------------\n";
        
        string += "Opcode: " + Integer.toHexString(_opcode) + "\n";
        
        //print out each short
        if(_shorts != null)
        {
            string += "Shorts: ";
            for(int counter = _shorts.size(); counter >= 1; --counter)
            {
                string += _shorts.elementAt(counter) + " ";
            }
            string += "\n";
        }
        
        string += "Extra String: " + _extraString + "\n";
        return string;
    }
    
    //Parsing code originally written in c++, where it was a lot... better
    
    public static RobotCommand factory(byte[] bytes)
    {
        //stone knives and bearskins...
        Vector currentBytePtr = new Vector();
        for(int counter = 0; counter < bytes.length; counter++)
        {
            currentBytePtr.addElement(Byte.valueOf(bytes[counter]));
        }
        
        byte opcode = parseOpcode(currentBytePtr);
        Vector shorts = parseShorts(currentBytePtr);
        String extraString = parseString(currentBytePtr);
        
        iterator = 0;
        return new RobotCommand(opcode, shorts, extraString);
    }
    
    //needs to be class scope so that it can keep its state after a function call
    static int iterator = 0;
    
    static byte parseOpcode(Vector currentBytePtr)
{
	if(((Byte)currentBytePtr.elementAt(iterator)).byteValue() != SOSProtocol.START_TRANSMISSION)
	{
		// Bail and return empty value.
            DebugLog.log(DebugLog.LVL_ERROR, null, "RobotCommand: the header of the command is incorrect\nShould be:" + SOSProtocol.START_TRANSMISSION + " Was: " + currentBytePtr.elementAt(iterator));
	    return 0x0;
	}


	// Have start of command.  Begin parsing.

	// Get opcode.
	++iterator;
	byte opcode = ((Byte)currentBytePtr.elementAt(iterator)).byteValue();
	++iterator;

	if(((Byte)currentBytePtr.elementAt(iterator)).byteValue() != SOSProtocol.END_OPCODE)
	{
		// Bail and return empty value.
		DebugLog.log(DebugLog.LVL_ERROR, null, "RobotCommand: the end of the opcode command is incorrect\nShould be:" + SOSProtocol.END_OPCODE + " Was: " + ((Byte)currentBytePtr.elementAt(iterator)).byteValue());
		return 0x0;
	}

	return opcode;
}

static Vector parseShorts(Vector currentBytePtr)
{
	if(((Byte)currentBytePtr.elementAt(iterator)).byteValue() == SOSProtocol.START_SHORT_TRANSMISSION)
	{
		Vector shortVector = new Vector();

		 // Loop until we don't recieve the start of a short
		while(((Byte)currentBytePtr.elementAt(iterator)).byteValue() == SOSProtocol.START_SHORT_TRANSMISSION)
		{
                    //max of 10 ASCII digit short
			char[] shortBytesStorage = new char[10];
                        int shortBytesIterator = 0;
                        
			while(((Byte)currentBytePtr.elementAt(++iterator)).byteValue() != SOSProtocol.END_SHORT)
			{
				shortBytesStorage[shortBytesIterator] = (char)(((Byte)currentBytePtr.elementAt(iterator)).byteValue());
                                ++shortBytesIterator;
			}
                        
			shortVector.addElement(Integer.valueOf(String.valueOf(shortBytesStorage)));
			++iterator;
		}

		return shortVector;
	}
	else
	{
		return null;
	}

}

static String parseString(Vector currentBytePtr)
{
	if(((Byte)currentBytePtr.elementAt(iterator)).byteValue() == SOSProtocol.START_STRING_TRANSMISSION)
	{
		String stringstream = new String();
		while(((Byte)currentBytePtr.elementAt(++iterator)).byteValue()!= SOSProtocol.END_STRING)
		{
        		stringstream += ((Byte)currentBytePtr.elementAt(iterator)).byteValue();
		}

		return stringstream;
	}
        
        return null;
}

}
