/*
 * robot_spi.cpp
 *
 *  Created on: Oct 15, 2013
 *      Author: jamie
 */

#include "robot_transmitter.h"
#include "stdlib.h"
#include <sstream>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <iomanip>
#include <cstring>

#include "Configuration.h"
#include "../Options.h"

RobotTransmitter::RobotTransmitter()
:_io_service(),
 _socket(_io_service)
{
	debug_log("RoboSPI", "Turning on SPI driver...");

	boost::asio::ip::tcp::resolver resolver(_io_service); // 2
	boost::asio::ip::tcp::resolver::query query(Options::instance()._crio_hostname); // 3
	boost::asio::ip::tcp::resolver::iterator endpoint_iterator = resolver.resolve(query);
	boost::asio::ip::tcp::resolver::iterator end;

	boost::system::error_code error = boost::asio::error::host_not_found;
	while(error && endpoint_iterator != end) // loop until we find a working endpoint
	{
	  _socket.close();
	  _socket.connect(*endpoint_iterator++, error);
	}

	debug_log("RoboSPI", "Done.")
}




RobotTransmitter::~RobotTransmitter()
{
}

void RobotTransmitter::send(std::string toSend)
{
	if(Options::instance()._fake)
	{
		std::cout << std::hex << std::setw(2) << toSend.c_str() << " ";
	}

	else
	{

	}

}

void RobotTransmitter::send(std::vector<char> toSend)
{
	send(std::string(toSend.begin(), toSend.end()));
#ifdef DEBUG_SERIAL_OUTPUT
	std::cout << std::endl;
#endif
}

