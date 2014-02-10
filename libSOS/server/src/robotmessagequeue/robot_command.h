/*
 * robot_command.h
 *
 * Object to represent the transmissions being sent
 *
 *  Created on: Nov 19, 2013
 *      Author: jamie
 */

#ifndef ROBOT_COMMAND_H_
#define ROBOT_COMMAND_H_

#include <boost/optional/optional.hpp>
#include <ostream>
#include <vector>
#ifdef _WIN32_WINNT
typedef uint16_t 	in_port_t;
#else
#include <arpa/inet.h>
#endif

struct robot_command
{
public:

	typedef double param_t;

	in_port_t _return_id;
	unsigned char _opcode;
	boost::optional<std::vector<param_t> > _params;
	boost::optional<std::string> _extraString;

	friend std::ostream & operator<<(std::ostream & leftOp, const robot_command rightOp);

	robot_command(in_port_t return_id, unsigned char opcode);

	robot_command(in_port_t return_id, unsigned char opcode, boost::optional<std::vector<param_t> >);

	robot_command(in_port_t return_id, unsigned char opcode,boost::optional<std::vector<param_t> >, boost::optional<std::string> extraString);

	static boost::optional<robot_command> factory(std::vector<char> bytes);

	//reads the return_id from the message
	//assumes the iterator is at the start
	static in_port_t parse_return_id(std::vector<char>::const_iterator & currentBytePtr, std::vector<char> & bytes);

	//reads one char from the message pointed at by the iterator
	//assumes that the iterator is at START_OPCODE
	static char parse_opcode(std::vector<char>::const_iterator & currentBytePtr, std::vector<char> & bytes);

	//reads a list of shorts from the message pointed at by the iterator
	static boost::optional<std::vector<param_t> > parse_params(std::vector<char>::const_iterator & currentBytePtr, std::vector<char> & bytes);

	static boost::optional<std::string> parse_string(std::vector<char>::const_iterator & currentBytePtr, std::vector<char> & bytes);


};

std::string string_to_hex(const std::string& input);

std::ostream & operator<<(std::ostream & leftOp, const robot_command rightOp);


#endif /* ROBOT_COMMAND_H_ */
