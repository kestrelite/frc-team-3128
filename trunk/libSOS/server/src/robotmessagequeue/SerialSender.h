/*
 * SerialSender.h
 *
 *  Created on: Nov 15, 2013
 *      Author: jamie
 */

#ifndef SERIALSENDER_H_
#define SERIALSENDER_H_

#include <ThreadSafeQueue.h>
#include <vector>
#include <fstream>
#include <Configuration.h>
#include <boost/thread/thread.hpp>
#include "robot_spi.h"

class SerialSender
{

public:
	void operator()(ThreadSafeQueue<std::vector<char> > * inputQueue);


	SerialSender(ThreadSafeQueue<std::vector<char> > * inputQueue);

	volatile bool shouldStop;
};

#endif /* SERIALSENDER_H_ */
