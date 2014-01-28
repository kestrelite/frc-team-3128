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
:_io_service()
{
	shouldStop = false;
	boost::thread(boost::ref(*this),  inputQueue);
}

std::shared_ptr<Connection> SerialSender::makeConnection(std::shared_ptr<ThreadSafeQueue<std::vector<char>>> queue)
{
	//stealing some code from SocketServer

	boost::asio::ip::tcp::acceptor acceptor(_io_service, boost::asio::ip::tcp::endpoint(boost::asio::ip::tcp::v4(), Options::instance()._crio_port));

	auto socket = std::make_shared<boost::asio::ip::tcp::socket>(_io_service);

	LOG_INFO("SerialSender", "Waiting for crio on port " << Options::instance()._crio_port );
	acceptor.accept(*socket);
	LOG_INFO("SerialSender", "Connected to crio" );

	//will read from port and put stuff in queue
	return std::shared_ptr<Connection>(new Connection(socket, queue));
}

void SerialSender::operator()(std::shared_ptr<ThreadSafeQueue<std::vector<char>>> inputQueue)
{

	//construct queue for Connection to write to
	auto queue = std::make_shared<ThreadSafeQueue<std::vector<char>>>();

	//will read from queue and send things where they should go
	RobotRouter router(queue);

	//the code breaks the loop when a connection closes and this loop restarts
	while(true)
	{
			LOG_INFO("SerialSender", "Initializing socket...");

			//create a socket server and get a connection
			std::shared_ptr<Connection> connectionPtr = makeConnection(queue);


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

				if(connectionPtr->_socket._socket->is_open())
				{
					connectionPtr->_socket.write(currentBytes);
				}
				else
				{
					//stop connection and re-open server
					break;
				}

			}
			//everything should be autodestructed
			std::cout << "SerialSender shutting down...";

	}
	}

