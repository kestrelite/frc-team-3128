/*
 * SerialSender.cpp
 *
 *  Created on: Nov 15, 2013
 *      Author: jamie
 */

#include "SerialSender.h"
#include "robot_command.h"
#include "Configuration.h"
#include "../Options.h"
#include <LogMacros.h>

SerialSender::SerialSender(std::shared_ptr<ThreadSafeQueue<std::vector<char>>> inputQueue)
{
	shouldStop = false;
	boost::thread(*this, inputQueue);
}

void SerialSender::operator()(std::shared_ptr<ThreadSafeQueue<std::vector<char>>> inputQueue)
{
	LOG_INFO("SerialSender", "Initializing...");

	//stealing some code from SocketServer
	boost::asio::io_service io_service;

	boost::asio::ip::tcp::acceptor acceptor(io_service, boost::asio::ip::tcp::endpoint(boost::asio::ip::tcp::v4(), Options::instance()._crio_port));

	auto socket = std::make_shared<boost::asio::ip::tcp::socket>(io_service);

	LOG_INFO("SerialSender", "Waiting for crio on port " << Options::instance()._crio_port );
	acceptor.accept(*socket);
	LOG_INFO("SerialSender", "Connected to crio" );

	//now that we have our socket, construct input and output
	auto queue = std::make_shared<ThreadSafeQueue<std::vector<char>>>();

	//will read from port and put stuff in queue
	Connection connection(socket, queue);

	//will read from queue and send things where they should go
	RobotRouter router(queue);


	while(!shouldStop)
	{
		std::vector<char> currentBytes = inputQueue->Dequeue();

		if(Options::instance()._verbose)
		{
			boost::optional<robot_command> command = robot_command::factory(currentBytes);

			if(command.is_initialized())
			{
				std::cout << command.get();
			}
			else
			{
				std::cerr << "SerialSender: could not construct robot_command, possibly bad data?" << std::endl;
			}
		}

		connection._socket.write(currentBytes);
	}
	//everything should be autodestructed
	std::cout << "SerialSender shutting down...";
}

