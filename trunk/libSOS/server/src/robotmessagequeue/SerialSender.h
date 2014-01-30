/*
 * SerialSender.h
 *
 *  Created on: Nov 15, 2013
 *      Author: jamie
 */

#ifndef SERIALSENDER_H_
#define SERIALSENDER_H_

#include <ThreadSafeQueue/ThreadSafeQueue.h>
#include <vector>
#include <fstream>
#include <boost/thread/thread.hpp>
#include <boost/asio.hpp>

#include <libSOS/Connection.h>
#include "RobotReciever.h"

class SerialSender
{

	boost::asio::io_service _io_service;

public:
	void operator()(std::shared_ptr<ThreadSafeQueue<std::vector<char>>>);

	//creates a socket server and returns as soon as it has  connection
	std::shared_ptr<Connection> makeConnection(std::shared_ptr<ThreadSafeQueue<std::vector<char>>> queue);

	SerialSender(std::shared_ptr<ThreadSafeQueue<std::vector<char>>>);

	volatile bool shouldStop;

	SerialSender(const SerialSender &) = delete;
};

#endif /* SERIALSENDER_H_ */
