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

robot_command::robot_command(int opcode)
:_opcode(opcode),
 _data(),
 _extraString()
{

}

robot_command::robot_command(unsigned char opcode, signed short data)
:_opcode(opcode),
 _data(data),
 _extraString()
{

}

robot_command::robot_command(unsigned char opcode, signed short data, std::string extraString)
:_opcode(opcode),
 _data(data),
 _extraString(extraString)
{

}

boost::optional<robot_command> * robot_command::factory(std::vector<char> bytes)
{
	std::vector<char>::const_iterator bytesIterator = bytes.begin();

	if(*bytesIterator != START_TRANSMISSION)
	{
		boost::optional<robot_command> * command = new boost::optional<robot_command>();
		std::cerr << "RobotCommand: the header of the command is incorrect\nShould be:" << START_TRANSMISSION << " Was: " << *(++bytesIterator) << std::endl;
		return command;
	}
	++bytesIterator;
	boost::optional<robot_command> * command = new boost::optional<robot_command>(robot_command(*bytesIterator));
	bytesIterator + 2;

	//holds the shorts until we can construct the object
	std::vector<short> shortStorage;
	if(*bytesIterator == START_SHORT_TRANSMISSION)
	{
		std::vector<char> shortBytesStorage;
		while(*(++bytesIterator) != END_SHORT)
		{
			shortBytesStorage.push_back(*bytesIterator);
		}
		atoi(shortBytesStorage.begin());
	}
	else if(*bytesIterator == END_TRANSMISSION)
	{
		return command;
	}
	return command;
}

std::ostream & operator<<(std::ostream & leftOp, const robot_command rightOp)
{
	leftOp << "Opcode: 0x" << std::hex << ((int)rightOp._opcode) << std::endl;
	if(rightOp._data.is_initialized())
	{
		leftOp << "Data: ";
		BOOST_FOREACH(const short subdata, rightOp._data.get())
		{
			leftOp << subdata << " ";
		}
		leftOp << std::endl;
	}
	if(rightOp._extraString.is_initialized())
	{
		leftOp << "Extra String: \"" << rightOp._extraString.get() << "\"" << std::endl;
	}

	return leftOp;

}

