/*
 * main.cpp
 *
 *  Created on: Sep 28, 2013
 *      Author: jamie
 */
//don't define void main if we're running unit tests.

#ifdef _WIN32_WINNT
//     Set the proper SDK version before including boost/Asio
#include <SDKDDKVer.h>
//     Note boost/ASIO includes Windows.h.
# include <boost/asio.hpp>
#endif // _WIN32

#ifndef UNIT_TESTS
#include <iostream>
#include <memory>
#include <boost/program_options.hpp>
#include <ThreadSafeQueue/ThreadSafeQueue.h>
#include <EzLogger/output/LogOutput.h>
#include <LogMacros.h>
#include <EzLogger/output/acceptors/BasicAcceptor.h>
#include <EzLogger/output/formatters/JamiesPrettyFormatter.h>
#include <EzLogger/output/writers/BasicWriter.h>



#include "robotmessagequeue/SerialSender.h"
#include "libSOS/SocketServer.h"
#include "Options.h"
#include "Configuration.h"

namespace po = boost::program_options;

void init_program_options(int argc, char ** argv)
{
	po::options_description desc("Allowed options");

	desc.add_options()
	    ("verbose", "show contents of commands")
	    ("fake", "just print things instead of writing them to the serial port")
	    ("port", po::value<int>(), "set port to start on\ndefault: 5952")
	    ("crioport", po::value<int>(), "set port to start crio server on")
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
	    int port = vm["port"].as<int>();

	    std::cout << "Port was set to " << port  << "." << std::endl;
	    Options::instance()._port = port;
	}

	if (vm.count("crioport"))
	{
	    int crioport = vm["port"].as<int>();

	    std::cout << "Crio port was set to " << crioport << "." << std::endl;
	    Options::instance()._crio_port = crioport;
	}

	else
	{
		std::cout << "Port defaulting to " << SOCKET_PORT << std::endl;

		//setting the port to SOCKET_PORT is done by options
	}

}

void init_EzLogger()
{
		auto logOutput = std::make_shared<LogOutput<BasicAcceptor, JamiesPrettyFormatter, BasicWriter>>();
		LogCore::instance().addOutput("stdio", logOutput);
		LOG_INFO("Main", "Start.")
}

int main(int argc, char ** argv)
{
	init_program_options(argc, argv);

	auto threadSafeQueue = std::make_shared<ThreadSafeQueue<std::vector<char>>>();

	SerialSender serialSender(threadSafeQueue);

	SocketServer socketServer(Options::instance()._port, threadSafeQueue);

	std::cout << "Done initializing\nHit any key followed by enter to stop program." << std::endl;

	//wait for the user to tell us to stop
	std::cin.get();

	//serialSender will shutdown
	serialSender.shouldStop = true;

	socketServer.socketServerShouldStop = true;

	//let things do their stuff
#ifdef _WIN32
	Sleep(150);
#else
	sleep(1);
#endif

	return 0;
}
#endif
