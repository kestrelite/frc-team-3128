/*
 * robot_spi.cpp
 *
 *  Created on: Oct 15, 2013
 *      Author: jamie
 */

#include "robot_spi.h"
#include "stdlib.h"
#include <sstream>
#include <iomanip>
#include "Configuration.h"
#include "../Options.h"

RoboSPI::RoboSPI()
:_fstream()
{
	debug_log("RoboSPI", "Turning on SPI driver...");
	system("echo BB-SPI0-01 > /sys/devices/bone_capemgr.8/slots");
	debug_log("RoboSPI", "Setting up SPI...");
	_fstream.open(SPI_DEV);
	if(!_fstream)
	{
		std::cerr << "RoboSPI: Error opening spi device" << std::endl;
	}
}




RoboSPI::~RoboSPI()
{
	_fstream.close();
}

void RoboSPI::send(char toSend)
{
	if(Options::instance()._fake)
	{
		std::cout << std::hex << std::setw(2) << ((int) toSend) << " ";
	}

	else
	{
		_fstream << toSend;
	}

}

void RoboSPI::send(std::vector<char> toSend)
{
	for(char byteToSend : toSend)
	{
		send(byteToSend);
	}
#ifdef DEBUG_SERIAL_OUTPUT
	std::cout << std::endl;
#endif
}

