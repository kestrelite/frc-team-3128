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


class RobotTransmitter
{
	boost::asio::io_service io_service;

	boost::asio::ip::tcp::resolver resolver(io_service); /
	boost::asio::ip::tcp::resolver::query query(host, Options::);
	boost::asio::ip::tcp::resolver::iterator endpoint_iterator = resolver.resolve(query);
	boost::asio::ip::tcp::resolver::iterator end; // 4

public:

	RobotTransmitter();

	void send(std::string toSend);
	void send(std::vector<char> toSend);

	~RobotTransmitter();
};

#endif /* ROBOT_TRANSMITTER_H_ */
