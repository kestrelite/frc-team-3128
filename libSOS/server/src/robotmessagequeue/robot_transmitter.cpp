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
{
	auto socket = std::make_shared<boost::asio::ip::tcp::socket>(_io_service);
	boost::system::error_code error;


	LOG_INFO("RobotTransmitter", "Connecting to crio on " << Options::instance()._crio_ip << ":" << Options::instance()._crio_port);

	boost::asio::ip::tcp::resolver resolver(_io_service); // 2
	boost::asio::ip::tcp::resolver::query query(boost::asio::ip::tcp::v4(), Options::instance()._crio_ip, Options::instance()._crio_port); // 3
	boost::asio::ip::tcp::resolver::iterator endpoint_iterator = resolver.resolve(query);
	boost::asio::ip::tcp::resolver::iterator end;

    socket->close();
    socket->connect(*endpoint_iterator++, error);
    if(error)
    {
    	LOG_FATAL("RobotTransmitter", "Could not connect to crio. " << error);
    	exit(-1);
    }

    //re-init the unique pointer now that we can construct a Socket
    _socketWrapper.reset(new Socket(socket));

	LOG_INFO("RobotTransmitter", "Done.")
}




RobotTransmitter::~RobotTransmitter()
{
}

void RobotTransmitter::send(std::vector<char> toSend)
{
	if(Options::instance()._fake)
	{
		std::string messageString(toSend.begin(), toSend.end());
		std::cout << std::hex << std::setw(2) << messageString << " ";
	}

	else
	{
		boost::system::error_code error = _socketWrapper->write(toSend);
	    if(error)
	    {
	    	std::string messageString(toSend.begin(), toSend.end());
	    	LOG_RECOVERABLE("RobotTransmitter", "Could not transmit byte array\"" << std::hex << std::setw(2) << messageString.c_str() << "\", " << error);
	    }
	}

}
