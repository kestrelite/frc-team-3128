/*
 * RobotReciever.cpp
 *
 *  Created on: Jan 11, 2014
 *      Author: Jamie
 */

#include <robotmessagequeue/RobotReciever.h>
#include <LogMacros.h>

RobotRouter::RobotRouter(std::shared_ptr<ThreadSafeQueue<std::vector<char>>> inputQueuePtr)
:_shouldStop(false),
 _inputQueuePtr(inputQueuePtr),
_thread(&RobotRouter::run, this)
{

}

RobotRouter::~RobotRouter()
{
	_thread.interrupt();
}

void RobotRouter::run()
{
	LOG_INFO("RobotReciever", "Thread starting up...")
	boost::system::error_code error;

	while(!_shouldStop)
	{
		boost::optional<std::vector<char>> currentBytes;

		try
		{
				currentBytes = _inputQueuePtr->Dequeue();
		}
		catch(boost::thread_interrupted & error)
		{
				//thread stopped
				break;
		}

		if(error)
		{
			LOG_FATAL("RobotReceiver", "Failed to read from robot.  Shutting down...")
			return;
		}


		boost::optional<robot_command> command = robot_command::factory(currentBytes.get());

		if(!(command.is_initialized()))
		{
			LOG_RECOVERABLE("SerialSender", "could not construct robot_command, possibly bad data?")
		}
		if(Options::instance()._verbose)
		{
			LOG_INFO( "SerialSender",  "Received command: " << command.get())
		}

		//find the connection denoted by this id and send the data on its socket
		Options::ReturnCodeMapType::iterator returnConnectionIterator = Options::instance()._returnCodeRegistry.find(command.get()._return_id);

		//check if the return code is in the lookup table
		if(returnConnectionIterator == Options::instance()._returnCodeRegistry.end())
		{
			LOG_RECOVERABLE("RobotReceiver", "return id " << command.get()._return_id << " not in registry.\nThe previous code isn't going to get where it was going.")
			for(Options::ReturnCodeMapType::value_type currentPair : Options::instance()._returnCodeRegistry)
			{
				LOG_DEBUG("RobotRouter", "registry: " << std::dec << currentPair.first)
			}

			//if not found, get next command and try again
			break;
		}

		(*returnConnectionIterator).second->_socket.write(*currentBytes);
	}
	LOG_INFO("RobotReciever", "Thread shutting down...")
}
