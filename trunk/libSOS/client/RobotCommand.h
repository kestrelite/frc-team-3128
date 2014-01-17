#ifndef ROBOTCOMMAND_H_
#define ROBOTCOMMAND_H_

typedef struct RobotCommand
{
	char opcode;
	char* string;
	short shorts[]; // here we use GCC's zero-length arrays
} RobotCommand;


#endif /* ROBOTCOMMAND_H_ */