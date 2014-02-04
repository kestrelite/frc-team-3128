// Utility services for creating and manipulating RobotCommand objects.

#ifndef ROBOTCOMMAND_H_
#define ROBOTCOMMAND_H_

//get in_port_t
#include <arpa/inet.h>

// Params in RobotCommand are this type.
typedef double param_type;


// Defines the structure of a command sent from here to the robot.
typedef struct RobotCommand
{
	// Identifies the sender of the command.
	in_port_t 		return_id;
	
	// Identifies the command.
	// 0 if parsing error
	char 			opcode;
	
	// Number of values in the |params| array.  Zero if no parameters are included.
	size_t 			params_count;
	
	// Pointer to array of one or more double values representing command parameters.
	// NULL if no parameters.  Target memory resides on the heap.
	param_type *	params; 
	
	// Number of bytes on the string parameter.  Zero if no string parameter.
	size_t			string_length;
	
	// Pointer to array of bytes comprising a string parameter.  Null if no string
	// parameter present.  Must be NULL terminated.  String memory resides on heap.
	char * 			string;
	
} RobotCommand;


// De-serializes an over-the-wire command to an in-memory representation.
// Caller takes ownership of returned object.  (Use rc_free() to clean up!)
RobotCommand * rc_factory(char const * const current_byte_ptr);


// Displays a RobotCommand to the console in human-readable form.
void rc_print(RobotCommand * commandPtr);


// Frees the RobotCommand at the specified location, including both
// the contained strings and the pointed-to object itself.
void rc_free(RobotCommand * commandPtr);

#endif /* ROBOTCOMMAND_H_ */