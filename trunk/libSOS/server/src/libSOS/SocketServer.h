/*
 * SocketServerManager.h
 *
 *  Created on: Nov 10, 2013
 *      Author: jamie
 */

#ifndef SOCKETSERVERMANAGER_H_
#define SOCKETSERVERMANAGER_H_

#include <cstdlib>
#include <iostream>
#include <utility>
#include <boost/asio.hpp>
#include <vector>

#include "Connection.h"
#include <ThreadSafeQueue/ThreadSafeQueue.h>

using boost::asio::ip::tcp;

class SocketServer
{
private:
	std::shared_ptr<ThreadSafeQueue<std::vector<char>>> _outputQueue;
public:
	SocketServer(int port, std::shared_ptr<ThreadSafeQueue<std::vector<char>>> threadSafeQueue);

	volatile bool socketServerShouldStop;

	void server(unsigned short port);
};

#endif /* SOCKETSERVERMANAGER_H_ */
