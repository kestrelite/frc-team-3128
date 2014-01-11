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
	debug_log("RoboSPI", "Turning on SPI driver...");

	system("echo BB-SPI0-01 > /sys/devices/bone_capemgr.8/slots");

	//open the spi device
	_fp = fopen(SPI_DEV, "r+b");

	//and get a file descriptor for it
	_fd = fileno(_fp);

	//spi 0 = /dev/spidev1.0
	//spi 1 = /dev/spidev2.0
	debug_log("RoboSPI", "Setting up SPI...");

	debug_log("RoboSPI", "Done.")
}




RobotTransmitter::~RobotTransmitter()
{
	fclose(_fp);
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

