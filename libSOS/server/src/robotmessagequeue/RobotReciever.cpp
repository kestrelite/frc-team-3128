/*
 * RobotReciever.cpp
 *
 *  Created on: Jan 11, 2014
 *      Author: Jamie
 */

#include <robotmessagequeue/RobotReciever.h>
#include <LogMacros.h>

RobotReceiver::RobotReceiver(std::shared_ptr<boost::asio::ip::tcp::socket> socket)
:_shouldStop(false),
 _socketWrapper(socket),
_thread(&RobotReceiver::run, this)
{

}

RobotReceiver::~RobotReceiver()
{

}

void RobotReceiver::run()
{
	LOG_INFO("RobotReciever", "Thread starting up...")
	boost::system::error_code error;

	while(!_shouldStop)
	{
		boost::optional<std::vector<char>> currentBytes = _socketWrapper.readNextCode();

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
			std::cout << "Received command: " << command.get();
		}

		//find the connection denoted by this id and send the data on its socket
		Options::ReturnCodeMapType::iterator returnConnectionIterator = Options::instance()._returnCodeRegistry.find(command.get()._return_id);

		//check if the return code is in the lookup table
		if(returnConnectionIterator == Options::instance()._returnCodeRegistry.end())
		{
			LOG_RECOVERABLE("RobotReceiver", "return id " << command.get()._return_id << " not in registry.\nThe previous code isn't going to get where it was going.")
		}

		(*returnConnectionIterator).second->_socket.write(*currentBytes);
	}
	LOG_INFO("RobotReciever", "Thread shutting down...")
}
