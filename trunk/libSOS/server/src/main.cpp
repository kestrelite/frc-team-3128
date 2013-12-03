/*
 * main.cpp
 *
 *  Created on: Sep 28, 2013
 *      Author: jamie
 */
//don't define void main if we're running unit tests.
#ifndef UNIT_TESTS
#include <iostream>
//#include "ProgramOptions.h"
#include <ThreadSafeQueue.h>
#include <SerialSender.h>
#include "libSOS/SocketServer.h"

int main()
{
	std::cout << "Hello World\n";

	ThreadSafeQueue<std::vector<char> > * threadSafeQueue = new ThreadSafeQueue<std::vector<char> >();

	SerialSender serialSender(threadSafeQueue);

	SocketServer socketServer(SOCKET_PORT, threadSafeQueue);

	std::cout << "Hit any key followed by enter to stop program." << std::endl;

	char foo;
	std::cin >> foo;

	//serialSender will shutdown
	serialSender.shouldStop = true;

	socketServer.socketServerShouldStop = true;

	//let things do their stuff
	sleep(250);
}
#endif
