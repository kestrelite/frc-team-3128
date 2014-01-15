/*
 * RobotReciever.h
 *
 *  Created on: Jan 11, 2014
 *      Author: Jamie
 */

#ifndef ROBOTRECEIVER_H_
#define ROBOTRECEIVER_H_

#include <boost/asio.hpp>
#include <boost/thread/thread.hpp>
#include <map>

#include <ThreadSafeQueue/ThreadSafeQueue.h>
#include <libSOS/Socket.h>
#include "robot_command.h"
#include <Options.h>
#include <libSOS/Connection.h>

class RobotReceiver
{
private:

	volatile bool _shouldStop;

	//socket to read from
	Socket _socketWrapper;

	//thread for the run function
	boost::thread _thread;

	void run();

	std::vector<char> readCommand();

public:

	RobotReceiver(std::shared_ptr<boost::asio::ip::tcp::socket> socket);

	virtual ~RobotReceiver();
};

#endif /* ROBOTRECEIVER_H_ */
