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


class RobotTransmitter
{
	boost::asio::io_service _io_service;
	boost::asio::ip::tcp::socket _socket;
public:

	RobotTransmitter();

	void send(std::string toSend);
	void send(std::vector<char> toSend);

	~RobotTransmitter();
};

#endif /* ROBOT_TRANSMITTER_H_ */
