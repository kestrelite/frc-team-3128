/*
 * robot_command.h
 *
 *  Created on: Nov 1, 2013
 *      Author: jamie
 */

#include "robot_command.h"
#include <vector>
#include <boost/foreach.hpp>
#include <SOSProtocol.h>
#include <sstream>

robot_command::robot_command(int opcode)
:_opcode(opcode),
 _data(),
 _extraString()
{

}

robot_command::robot_command(unsigned char opcode, boost::optional<std::vector<signed short> > data)
:_opcode(opcode),
 _data(data),
 _extraString()
{

}

robot_command::robot_command(unsigned char opcode, boost::optional<std::vector<signed short> > data, boost::optional<std::string> extraString)
:_opcode(opcode),
 _data(data),
 _extraString(extraString)
{

}

boost::optional<robot_command> * robot_command::factory(std::vector<char> bytes)
{
	std::vector<char>::const_iterator 		currentBytePtr(bytes.begin());
	char 									opcode;
	boost::optional<std::vector<short> > 	shorts;
	boost::optional<std::string> 			string;


	if(*currentBytePtr != START_TRANSMISSION)
	{
		// Bail and return empty value.
		boost::optional<robot_command> * command = new boost::optional<robot_command>();
		std::cerr << "RobotCommand: the header of the command is incorrect\nShould be:" << START_TRANSMISSION << " Was: " << *currentBytePtr << std::endl;
		return command;
	}


	// Have start of command.  Begin parsing.

	// Get opcode.
	++currentBytePtr;
	opcode = *currentBytePtr;


	// Move into shorts if any.
	++currentBytePtr;
	++currentBytePtr;

	if(*currentBytePtr == START_SHORT_TRANSMISSION)
	{
		//give the optional type a value
		shorts.reset(std::vector<short>());

		 // Loop until we don't recieve the start of a short
		while(*currentBytePtr == START_SHORT_TRANSMISSION)
		{
			std::vector<char> shortBytesStorage;
			while(*(++currentBytePtr) != END_SHORT)
			{
				shortBytesStorage.push_back(*currentBytePtr);
			}
			shorts.get().push_back(static_cast<short>(atoi(shortBytesStorage.data())));
			++currentBytePtr;
		}
	}


	if(*currentBytePtr == START_STRING_TRANSMISSION)
	{
		std::stringstream stringstream;
		while(*(++currentBytePtr) != END_STRING)
		{

			stringstream << *currentBytePtr;
		}
		string.reset(std::string(stringstream.str()));
	}

	return new boost::optional<robot_command>(robot_command(opcode, shorts, string));

}

std::ostream & operator<<(std::ostream & leftOp, const robot_command rightOp)
{
	leftOp << "robot_command dump: --------------------------------------" << std::endl;
	leftOp << "Opcode: 0x" << std::hex << ((int)rightOp._opcode) << std::endl;
	if(rightOp._data.is_initialized())
	{
		leftOp << "Data: ";
		for(short subdata : rightOp._data.get())
		{
			leftOp << std::dec << (int)(subdata) << " ";
		}
		leftOp << std::endl;
	}
	if(rightOp._extraString.is_initialized())
	{
		leftOp << "Extra String: \"" << rightOp._extraString.get() << "\"" << std::endl;
	}
	leftOp << std::endl;

	return leftOp;

}

