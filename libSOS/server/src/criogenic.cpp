#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/tcp.h>
#include <iostream>
#include <vector>
#include <boost/thread/thread.hpp>

#include <LogMacros.h>
#include <ThreadSafeQueue/ThreadSafeQueue.h>
#include <EzLogger/output/acceptors/BasicAcceptor.h>
#include <EzLogger/output/formatters/JamiesPrettyFormatter.h>
#include <EzLogger/output/writers/BasicWriter.h>
#include <libSOS/SocketServer.h>

void messageEchoer(std::shared_ptr<ThreadSafeQueue<std::vector<char>>> queue)
{
	while(true)
	{
		queue->Dequeue();
		LOG_DEBUG("Criogenic", "Received a message!");
	}
}

int main(int argc, char** argv)
{
	if(argc != 2)
	{
		std::cerr << "Usage: criogenic <port>\nex: criogenic 2987\n";
		exit(0);
	}
	
	//set up logging
	auto logOutput = std::make_shared<LogOutput<BasicAcceptor, JamiesPrettyFormatter, BasicWriter>>();
	LogCore::instance().addOutput("stdio", logOutput);

	auto queue = std::make_shared<ThreadSafeQueue<std::vector<char>>>();
	SocketServer socketServer(atoi(argv[1]), queue);
	boost::thread(&messageEchoer, queue);
	
	std::cout << "Hit any key followed by enter to stop program." << std::endl;

	std::cin.get();

	socketServer.socketServerShouldStop = true;
}
