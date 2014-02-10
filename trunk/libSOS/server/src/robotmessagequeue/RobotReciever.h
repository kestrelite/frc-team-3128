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
#include "robot_command.h"
#include <Options.h>
#include <libSOS/Connection.h>

class RobotRouter
{
private:

	volatile bool _shouldStop;

	//socket to read from
	std::shared_ptr<ThreadSafeQueue<std::vector<char>>> _inputQueuePtr;

	//thread for the run function
	boost::thread _thread;

	void run();

public:

	RobotRouter(std::shared_ptr<ThreadSafeQueue<std::vector<char>>> inputQueuePtr);

	virtual ~RobotRouter();
};

#endif /* ROBOTRECEIVER_H_ */
