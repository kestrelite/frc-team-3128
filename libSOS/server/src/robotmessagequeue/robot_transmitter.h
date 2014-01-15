/*
 * robot_spi.h
 *
 *  Created on: Oct 15, 2013
 *      Author: jamie
 */

#ifndef ROBOT_TRANSMITTER_H_
#define ROBOT_TRANSMITTER_H_

#include <vector>
#include <string>
#include <fstream>
#include <Configuration.h>
#include <boost/asio.hpp>
#include <LogMacros.h>

#include <libSOS/Socket.h>

class RobotTransmitter
{
	boost::asio::io_service _io_service;

	std::unique_ptr<Socket> _socketWrapper;
public:

	RobotTransmitter();

	//writes the bytes to the socket
	void send(std::vector<char> toSend);

	void send(std::string toSend);

	~RobotTransmitter();
};

#endif /* ROBOT_TRANSMITTER_H_ */
