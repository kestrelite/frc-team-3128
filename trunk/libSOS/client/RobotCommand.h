#ifndef ROBOTCOMMAND_H_
#define ROBOTCOMMAND_H_

//get in_port_t
#include <arpa/inet.h>

typedef struct RobotCommand
{
	in_port_t return_id;
	char opcode;
	char string[25];
	short shorts[50];
} RobotCommand;


#endif /* ROBOTCOMMAND_H_ */