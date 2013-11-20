/*
 * ProgramOptions.h
 *
 *  Created on: Oct 15, 2013
 *      Author: jamie
 */

#ifndef PROGRAMOPTIONS_H_
#define PROGRAMOPTIONS_H_
#include <boost/program_options.hpp>
#include <iostream>
#include <iterator>

namespace po = boost::program_options;

int init_cmdline_options(int ac, char* av[])
{
	// Declare the supported options.
	po::options_description desc("Allowed options");
	desc.add_options()
		("help", "produce help message")
		("compression", po::value<int>(), "set compression level")
	;

	po::variables_map vm;
	po::store(po::parse_command_line(ac, av, desc), vm);
	po::notify(vm);

	if (vm.count("help")) {
		std::cout << desc << "\n";
		return 1;
	}

	if (vm.count("compression")) {
		std::cout << "Compression level was set to "
	 << vm["compression"].as<int>() << ".\n";
	} else {
		std::cout << "Compression level was not set.\n";
	}
}


#endif /* PROGRAMOPTIONS_H_ */
