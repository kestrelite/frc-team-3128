/*
 * Socket.cpp
 *
 *
 */

#include "Socket.h"
#include "SOSProtocol.h"
#include <LogMacros.h>

//called by SocketServer after it has gotten a client
Socket::Socket(std::shared_ptr<boost::asio::ip::tcp::socket> socket)
:_socket(socket)
{

}

Socket::~Socket()
{

}

boost::system::error_code Socket::write(std::vector<char> & toSend)
{
	boost::system::error_code errorCode;

	boost::asio::write(*_socket, boost::asio::buffer(toSend), errorCode);

	return errorCode;
}

boost::optional<std::vector<char>> Socket::readNextCode()
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
			LOG_UNUSUAL("Socket", "Ignoring byte with value " << std::hex << data)
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
