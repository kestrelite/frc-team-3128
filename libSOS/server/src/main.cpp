/*
 * main.cpp
 *
 *  Created on: Sep 28, 2013
 *      Author: jamie
 */
//don't define void main if we're running unit tests.
#ifndef UNIT_TESTS
#include <iostream>
#include <boost/program_options.hpp>
#include <common/ThreadSafeQueue.h>
#include "robotmessagequeue/SerialSender.h"
#include "libSOS/SocketServer.h"
#include "Options.h"

namespace po = boost::program_options;

void init_program_options(int argc, char ** argv)
{
	po::options_description desc("Allowed options");

	desc.add_options()
	    ("verbose", "show contents of commands")
	    ("fake", "just print things instead of writing them to the serial port")
	    ("port", po::value<int>(), "set port to start on\ndefault: 5952")
	    ("help", "print this message")
	;

	po::variables_map vm;

	po::store(po::parse_command_line(argc, argv, desc), vm);

	po::notify(vm);

	if (vm.count("help"))
	{
	    std::cout <<  desc << "\n";
	    exit(1);
	}

	if (vm.count("verbose"))
	{
		Options::instance()._verbose = true;
	}

	if (vm.count("fake"))
	{
		Options::instance()._fake = true;
	}

	if (vm.count("port"))
	{
	    std::cout << "Port was set to " << vm["port"].as<int>() << "." << std::endl;

	    int port = vm["port"].as<int>();
	    Options::instance()._port = port;
	}
	else
	{
		std::cout << "Port defaulting to " << SOCKET_PORT << std::endl;

		//setting the port to SOCKET_PORT is done by options
	}

}

int main(int argc, char ** argv)
{
	init_program_options(argc, argv);

	ThreadSafeQueue<std::vector<char> > * threadSafeQueue = new ThreadSafeQueue<std::vector<char> >();

	SerialSender serialSender(threadSafeQueue);

	SocketServer socketServer(Options::instance()._port, threadSafeQueue);

	std::cout << "Done initializing\nHit any key followed by enter to stop program." << std::endl;

	char foo;
	std::cin >> foo;

	//serialSender will shutdown
	serialSender.shouldStop = true;

	socketServer.socketServerShouldStop = true;

	//let things do their stuff
	sleep(250);

	return 0;
}
#endif
