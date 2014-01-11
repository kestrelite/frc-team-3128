/*
 * This class holds tions for the program.  They are initialized from init_program_options in
 * main.cpp, and can be changed at runtime.
 *
 *  Created on: Dec 3, 2013
 *      Author: jamie
 */

#ifndef OPTIONS_H_
#define OPTIONS_H_

class Options
{
private:
	Options();

    Options(Options const&);              // Don't Implement
    void operator=(Options const&); // Don't implement

public:

	int _port;

	int _crio_port;

	bool _fake;

	bool _verbose;

	static Options & instance();


};

#endif /* OPTIONS_H_ */
