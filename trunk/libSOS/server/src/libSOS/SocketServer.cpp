/*
 * SocketServerManager.cpp
 *
 *  Created on: Nov 10, 2013
 *      Author: jamie
 */

#include "SocketServer.h"
#include "SOSProtocol.h" // get the protocol define's
#include <Configuration.h>
#include <boost/thread.hpp>

const int max_length = 2048;

void SocketServer::operator()(tcp::socket * sock)
{
	try
	{
		std::cout << "Starting socket server on port " << sock->local_endpoint().address().to_string() << std::endl;
		for (;;)
		{
			//  Discard characters until we see START_TRANSMISSION.
			while(true)
			{
				//are these error_code objects reuseable?
				//I'm assuming they aren't
				boost::system::error_code error;
				char data;

				// Read one incoming byte.
				sock->read_some(boost::asio::buffer(&data, sizeof(data)), error);

				if (error == boost::asio::error::eof)
				{
					std::cout << "Closing connection on port " << sock->local_endpoint().address().to_string() << std::endl;
					return;
				}

				else if (error)
				{
					throw boost::system::system_error(error); // Some other error.
				}


				// If start transmission character has been read
				if(data == START_TRANSMISSION)
				{
#ifdef DEBUG_SOCKET_RECEPTION
					std::cout << "Beginning recieved message..." << std::endl;
#endif
					break;
				}
				else
				{
#ifdef DEBUG_SOCKET_RECEPTION
					std::cout << "Ignoring byte with value " << std::hex << data << std::endl;
#endif
				}
			}

			// START_TRANSMISSION found.  Recieve characters until the transmission ends

			boost::system::error_code second_error;

			std::vector<char> outputStream;

			//add back in the start character
			outputStream.push_back(START_TRANSMISSION);

			//add bytes to oututStream uuntil we see the END_TRANSMISSION
			while(true)
			{
				char data;

				//get another byte
				sock->read_some(boost::asio::buffer(&data, sizeof(data)), second_error);


#ifdef DEBUG_SOCKET_RECEPTION
				std::cout << "Recieved byte: " << std::hex << ((int)data) << std::endl;
#endif

				// Connection closed cleanly by peer.
				if (second_error == boost::asio::error::eof)
				{
					std::cout << "Closing connection on port " << sock->local_endpoint().address().to_string() << std::endl;
					return;
				}

				else if (second_error)
				{
					throw boost::system::system_error(second_error); // Some other error.
				}

				//after we've checked it for errors, put it in the output
				outputStream.push_back(data);

				// Terminate byte reads at end of command.
				if(data == END_TRANSMISSION)
				{
#ifdef DEBUG_SOCKET_RECEPTION
					std::cout << "Recived a " << outputStream.size() << " byte transmission." << std::endl;
#endif
					break;
				}
			}


			//put the byte vector into output queue
			_outputQueue->Enqueue(outputStream);
		}
	}
	catch (std::exception& e)
	{
		std::cerr << "Exception in thread: " << e.what() << "\n";
	}

	delete sock;
}

void SocketServer::server(unsigned short port)
{
	std::cout << "Starting acceptor thread on port " << port << std::endl;

	boost::asio::io_service io_service;

	tcp::acceptor acceptor(io_service, tcp::endpoint(tcp::v4(), port));

	while(!socketServerShouldStop)
	{
		tcp::socket * sock = new tcp::socket(io_service);
		acceptor.accept(*sock);
		boost::thread(boost::bind(&SocketServer::operator(), this, _1), sock);
	}

	//shut down threads
}

//constructor, which starts the server manager thread
SocketServer::SocketServer(int port, ThreadSafeQueue<std::vector<char> > * threadSafeQueue)
:_outputQueue(threadSafeQueue),
 socketServerShouldStop(false)
{

	boost::thread(boost::bind(&SocketServer::server, this, port));

}

