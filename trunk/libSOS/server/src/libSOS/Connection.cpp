/*
 * Connection.cpp
 *
 *  Created on: Jan 12, 2014
 *      Author: Jamie
 */

#include <libSOS/Connection.h>
#include <boost/lexical_cast.hpp>

Connection::Connection(std::shared_ptr<boost::asio::ip::tcp::socket> sock, std::shared_ptr<ThreadSafeQueue<std::vector<char>>> outputQueue)
:_outputQueue(outputQueue),
 _socket(sock),
 _threadPtr(),
 _id(sock->local_endpoint().port())
{
	//add ourselves to the registry of connections
	//so we can be looked up by return ID
	Options::instance()._returnCodeRegistry[_id] = std::shared_ptr<Connection>(this);

	_threadPtr = std::unique_ptr<boost::thread>(new boost::thread(boost::ref(*this)));
}

Connection::~Connection()
{
	LOG_DEBUG("Connection", "Destructor Running for Connection" << std::hex << (unsigned int)(this));
}

void Connection::operator()()
{
	try
	{
		std::cout << "Starting socket connection on " << _socket._socket->local_endpoint().address().to_string() << ":" << _socket._socket->local_endpoint().port() << std::endl;

		for (;;)
		{
			boost::optional<std::vector<char>> outputStream = _socket.readNextCode();

			if(outputStream.is_initialized())
			{
				_outputQueue->Enqueue(outputStream.get());
			}

			//empty optional, so the read operation probably returned EOF.
			else
			{
				std::cout << "Closing connection on port " << _socket._socket->local_endpoint().address().to_string() << std::endl;
				break;
			}
		}
	}
	catch (std::exception& e)
	{
		std::cerr << "Exception in Connection " << std::hex << (unsigned int)(this) << " thread: " << e.what() << "\n";
	}

	//unregister us from the return code registry
	//and free our memory
	Options::instance()._returnCodeRegistry.erase(_id);

}
