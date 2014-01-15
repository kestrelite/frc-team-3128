/*
 * Connection.h
 *
 * There is one of these objects for every client connected.
 * They are created by SocketServer, and they each have their own thread
 */

#ifndef CONNECTION_H_
#define CONNECTION_H_

#include <boost/thread/thread.hpp>
#include <boost/asio.hpp>
#include <arpa/inet.h>

#include "Socket.h"
#include <Options.h>
#include <LogMacros.h>

class Connection
{
	std::shared_ptr<ThreadSafeQueue<std::vector<char>>> _outputQueue;

public:
	Socket _socket;

private:
	std::unique_ptr<boost::thread> 	_threadPtr;

	in_port_t _id;

public:
	Connection(std::shared_ptr<boost::asio::ip::tcp::socket> sock, std::shared_ptr<ThreadSafeQueue<std::vector<char>>> outputQueue);

	//runs the internal reader thread
	void operator()();

	virtual ~Connection();
};

#endif /* CONNECTION_H_ */
