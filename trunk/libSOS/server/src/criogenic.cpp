#include <sys/types.h>
#include <iostream>
#include <vector>
#include <boost/thread/thread.hpp>

#include <LogMacros.h>
#include <ThreadSafeQueue/ThreadSafeQueue.h>
#include <EzLogger/output/acceptors/BasicAcceptor.h>
#include <EzLogger/output/formatters/JamiesPrettyFormatter.h>
#include <EzLogger/output/writers/BasicWriter.h>
#include <libSOS/SocketServer.h>

void messageEchoer(std::shared_ptr<ThreadSafeQueue<std::vector<char>>> queue, std::shared_ptr<Connection> connection)
{
	unsigned int counter = 0;

	while(true)
	{
		std::vector<char> bytes = queue->Dequeue();
		LOG_DEBUG("Criogenic", "Received message " << counter++);
		connection->write(bytes);
	}
}

int main(int argc, char** argv)
{
	//set up logging
	auto logOutput = std::make_shared<LogOutput<BasicAcceptor, JamiesPrettyFormatter, BasicWriter>>();
	LogCore::instance().addOutput("stdio", logOutput);

	LOG_INFO("Criogenic", "Starting...");

	auto queue = std::make_shared<ThreadSafeQueue<std::vector<char>>>();

	boost::asio::io_service io_service;

	auto socket = std::make_shared<boost::asio::ip::tcp::socket>(io_service);

	boost::asio::ip::tcp::endpoint libSOS_endpoint(boost::asio::ip::address::from_string("127.0.0.1"), 4545);

	try
	{
		socket->connect(libSOS_endpoint);
	}
	catch(std::exception & error)
	{
		LOG_FATAL("Criogenic", "Error Connecting: " << error.what());
		sleep(1);
		return 1;
	}
	auto connection = std::make_shared<Connection>(socket, queue);

	boost::thread(&messageEchoer, queue, connection);
	
	std::cout << "Hit any key followed by enter to stop program." << std::endl;

	std::cin.get();

	connection->_socket->close();

}
