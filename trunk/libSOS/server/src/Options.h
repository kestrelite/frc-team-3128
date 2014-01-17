/*
 * This class holds tions for the program.  They are initialized from init_program_options in
 * main.cpp, and can be changed at runtime.
 *
 *  Created on: Dec 3, 2013
 *      Author: jamie
 */

#ifndef OPTIONS_H_
#define OPTIONS_H_

#include <string>
#include <arpa/inet.h>
#include <map>
#include <memory>

//forward-declare this so Connection can include it
class Connection;

class Options
{
private:
	Options();

    Options(Options const&);              // Don't Implement
    void operator=(Options const&); // Don't implement

public:

	int _port;

	in_port_t  _crio_port;

	bool _fake;

	bool _verbose;

	typedef std::map<in_port_t, Connection *> ReturnCodeMapType;

	ReturnCodeMapType _returnCodeRegistry;

	static Options & instance();


};

#endif /* OPTIONS_H_ */
