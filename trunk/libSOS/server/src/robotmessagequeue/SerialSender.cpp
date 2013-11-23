/*
 * SerialSender.cpp
 *
 *  Created on: Nov 15, 2013
 *      Author: jamie
 */

#include <SerialSender.h>
#include "robot_command.h"
#include "Configuration.h"

SerialSender::SerialSender(ThreadSafeQueue<std::vector<char> > * inputQueue)
{
	shouldStop = false;
	boost::thread(*this, inputQueue);
}

void SerialSender::operator()(ThreadSafeQueue<std::vector<char> > * inputQueue)
{
	std::cout << "SerialSender initializing..." << std::endl;
	RoboSPI roboSPI;
	while(!shouldStop)
	{
		std::vector<char> currentBytes = inputQueue->Dequeue();
#ifdef MAKE_ROBOT_COMMANDS
		boost::optional<robot_command> * command = robot_command::factory(currentBytes);

		if(command->is_initialized())
		{
			std::cout << command->get();
		}
		else
		{
			std::cout << "SerialSender: could not construct robot_command, possibly bad data?" << std::endl;
		}
#endif
		roboSPI.send(currentBytes);
	}
	//everything should be autodestructed
	std::cout << "SerialSender shutting down...";
}

