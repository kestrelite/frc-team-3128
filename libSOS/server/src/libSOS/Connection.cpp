/*
 * Connection.cpp
 *
 *  Created on: Jan 12, 2014
 *      Author: Jamie
 */

#include <libSOS/Connection.h>
#include <boost/lexical_cast.hpp>

#include "SOSProtocol.h"

Connection::Connection(std::shared_ptr<boost::asio::ip::tcp::socket> sock, std::shared_ptr<ThreadSafeQueue<std::vector<char>>> outputQueue)
:_outputQueue(outputQueue),
 _socket(sock),
 _threadPtr(),
 _id(sock->remote_endpoint().port())
{
	LOG_DEBUG("Connection", "My ID is " << std::dec << _id);
	//add ourselves to the registry of connections
	//so we can be looked up by return ID
	Options::instance()._returnCodeRegistry[_id] = this;

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
		std::cout << "Starting socket connection on " << _socket->local_endpoint().address().to_string() << ":" << _socket->local_endpoint().port() << std::endl;

		for (;;)
		{
			boost::optional<std::vector<char>> outputStream = readNextCode();

			if(outputStream.is_initialized())
			{
				_outputQueue->Enqueue(outputStream.get(), false);
			}

			//empty optional, so the read operation probably returned EOF.
			else
			{
				break;
			}
		}
	}
	catch (std::exception& e)
	{
		LOG_RECOVERABLE("Connection",  "Exception in Connection " << std::hex << (unsigned int)(this) << " thread: " << e.what() << "\n");
	}

	LOG_INFO("Connection",  "Closing connection on " << _socket->local_endpoint().address().to_string() << ":" << _socket->local_endpoint().port() << std::endl);

	//unregister us from the return code registry
	//and free our memory
	Options::instance()._returnCodeRegistry.erase(_id);

	delete this;

}

boost::system::error_code Connection::write(std::vector<char> & toSend)
{
        boost::system::error_code errorCode;

        boost::asio::write(*_socket, boost::asio::buffer(toSend), errorCode);

        return errorCode;
}

boost::optional<std::vector<char>> Connection::readNextCode()
{
        boost::system::error_code error;

        //  Discard characters until we see START_TRANSMISSION.
        while(true)
        {
                char data;

                // Read one incoming byte.
                _socket->read_some(boost::asio::buffer(&data, sizeof(data)), error);

                if (error == boost::asio::error::eof)
                {
                        return boost::optional<std::vector<char>>{};
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
                      printf("[Connection] Ignoring byte with value %x\n", data);
                }
        }

        // START_TRANSMISSION found.  Receive characters until the transmission ends



        std::vector<char> outputStream;

        //add back in the start character
        outputStream.push_back(START_TRANSMISSION);

        //add bytes to oututStream until we see the END_TRANSMISSION
        while(true)
        {
                char data;

                //get another byte
                _socket->read_some(boost::asio::buffer(&data, sizeof(data)), error);


#ifdef DEBUG_SOCKET_RECEPTION
                std::cout << "Recieved byte: " << std::hex << ((int)data) << std::endl;
#endif

                // Connection closed cleanly by peer.
                if (error == boost::asio::error::eof)
                {
                        return boost::optional<std::vector<char>>{};
                }

                else if (error)
                {
                        throw boost::system::system_error(error); // Some other error.
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

        return boost::optional<std::vector<char>>(outputStream);


}

