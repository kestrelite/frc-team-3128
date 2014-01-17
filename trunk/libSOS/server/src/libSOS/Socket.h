/*
 * Socket.h
 *
 *  Created on: Jan 11, 2014
 *      Author: Jamie
 */

#ifndef SOCKET_H_
#define SOCKET_H_

#include <boost/optional.hpp>
#include <boost/asio.hpp>
#include <ThreadSafeQueue/ThreadSafeQueue.h>

class Socket
{
public:

	std::shared_ptr<boost::asio::ip::tcp::socket> _socket;

	Socket(std::shared_ptr<boost::asio::ip::tcp::socket> socket);
	virtual ~Socket();

	//reads one RobotCommand's bytes from the socket and returns them
	boost::optional<std::vector<char>> readNextCode();

	//writes the vector<char> of bytes to the socket
	//returns the error_code produced
	boost::system::error_code write(std::vector<char> & toSend);

	//no copy constructor
	Socket(Socket&) = delete;

	//no operator=
	Socket & operator=(Socket& socket) = delete;
};

#endif /* SOCKET_H_ */
