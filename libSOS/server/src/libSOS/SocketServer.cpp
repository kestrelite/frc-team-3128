/*
 * SocketServerManager.cpp
 *
 *  Created on: Nov 10, 2013
 *      Author: jamie
 */

#include "SocketServer.h"
#include "SOSProtocol.h" // get the protocol define's
#include <Configuration.h>
#include <boost/thread/thread.hpp>

const int max_length = 2048;



void SocketServer::server(unsigned short port)
{
	std::cout << "Starting acceptor thread on port " << port << std::endl;

	boost::asio::io_service io_service;

	tcp::acceptor acceptor(io_service, tcp::endpoint(tcp::v4(), port));

	while(!socketServerShouldStop)
	{
		auto sock = std::make_shared<tcp::socket>(io_service);
		acceptor.accept(*sock);

		//let this float off into space
		//it will delete itself when the connection closes
		Connection * connectionPtr = new Connection(sock, _outputQueue);
	}

	//shut down threads
}

//constructor, which starts the server manager thread
SocketServer::SocketServer(int port, std::shared_ptr<ThreadSafeQueue<std::vector<char>>> threadSafeQueue)
:_outputQueue(threadSafeQueue),
 socketServerShouldStop(false)
{

	boost::thread(boost::bind(&SocketServer::server, this, port));

}
