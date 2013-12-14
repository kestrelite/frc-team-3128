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
#include <thread>
#include <utility>
#include <boost/asio.hpp>
#include <ThreadSafeQueue.h>
#include <vector>

using boost::asio::ip::tcp;

class SocketServer
{
private:
	ThreadSafeQueue<std::vector<char> > * _outputQueue;
public:
	SocketServer(int port, ThreadSafeQueue<std::vector<char> > * threadSafeQueue);

	volatile bool socketServerShouldStop;

	void operator()(tcp::socket * sock);

	void server(unsigned short port);
};

#endif /* SOCKETSERVERMANAGER_H_ */
