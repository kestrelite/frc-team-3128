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

struct robot_command
{
public:
	unsigned char _opcode;
	boost::optional<std::vector<signed short> > _data;
	boost::optional<std::string> _extraString;

	friend std::ostream & operator<<(std::ostream & leftOp, const robot_command rightOp);

	robot_command(int opcode);

	robot_command(unsigned char opcode, boost::optional<std::vector<signed short> >);

	robot_command(unsigned char opcode,boost::optional<std::vector<signed short> >, boost::optional<std::string> extraString);

	static boost::optional<robot_command> * factory(std::vector<char> bytes);

	//reads one char from the message pointed at by the iterator
	//assumes that the iterator is at the start
	static char parse_opcode(std::vector<char>::const_iterator & currentBytePtr);

	//reads a list of shorts from the message pointed at by the iterator
	static boost::optional<std::vector<signed short> > parse_shorts(std::vector<char>::const_iterator & currentBytePtr);

	static boost::optional<std::string> parse_string(std::vector<char>::const_iterator & currentBytePtr);


};

std::ostream & operator<<(std::ostream & leftOp, const robot_command rightOp);


#endif /* ROBOT_COMMAND_H_ */
