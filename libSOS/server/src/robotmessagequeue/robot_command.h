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
#include <arpa/inet.h>

struct robot_command
{
public:

	in_port_t _return_id;
	unsigned char _opcode;
	boost::optional<std::vector<signed short> > _data;
	boost::optional<std::string> _extraString;

	friend std::ostream & operator<<(std::ostream & leftOp, const robot_command rightOp);

	robot_command(in_port_t return_id, unsigned char opcode);

	robot_command(in_port_t return_id, unsigned char opcode, boost::optional<std::vector<signed short> >);

	robot_command(in_port_t return_id, unsigned char opcode,boost::optional<std::vector<signed short> >, boost::optional<std::string> extraString);

	static boost::optional<robot_command> factory(std::vector<char> bytes);

	//reads the return_id from the message
	//assumes the iterator is at the start
	static in_port_t parse_return_id(std::vector<char>::const_iterator & currentBytePtr);

	//reads one char from the message pointed at by the iterator
	//assumes that the iterator is at START_OPCODE
	static char parse_opcode(std::vector<char>::const_iterator & currentBytePtr);

	//reads a list of shorts from the message pointed at by the iterator
	static boost::optional<std::vector<signed short> > parse_shorts(std::vector<char>::const_iterator & currentBytePtr);

	static boost::optional<std::string> parse_string(std::vector<char>::const_iterator & currentBytePtr);


};

std::string string_to_hex(const std::string& input);

std::ostream & operator<<(std::ostream & leftOp, const robot_command rightOp);


#endif /* ROBOT_COMMAND_H_ */
