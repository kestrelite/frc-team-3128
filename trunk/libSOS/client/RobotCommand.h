#ifndef ROBOTCOMMAND_H_
#define ROBOTCOMMAND_H_

//get in_port_t
#include <arpa/inet.h>

typedef struct RobotCommand
{
	in_port_t 	return_id;
	char 		opcode;
	size_t 		shorts_len;
	short * 	shorts; //on the heap
	char * 		string; //on the heap
} RobotCommand;

RobotCommand * rc_factory(char * currentBytePtr);

void rc_print(RobotCommand * commandPtr);

void rc_free(RobotCommand * commandPtr);

#endif /* ROBOTCOMMAND_H_ */