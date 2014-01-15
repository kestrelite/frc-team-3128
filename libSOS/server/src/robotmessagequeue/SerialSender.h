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
#include <Configuration.h>
#include <boost/thread/thread.hpp>
#include "robot_transmitter.h"

class SerialSender
{

public:
	void operator()(std::shared_ptr<ThreadSafeQueue<std::vector<char>>>);

	SerialSender(std::shared_ptr<ThreadSafeQueue<std::vector<char>>>);

	volatile bool shouldStop;
};

#endif /* SERIALSENDER_H_ */
