// Utility services for creating and manipulating RobotCommand objects.

#include "RobotCommand.h"
#include "SOSProtocol.h"

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

// returns a buffer on the heap containing a series of bytes extracted
// from the specified buffer.  Caller owns buffer,
char * extract_bytes_until
(
	char const * const 	start_ptr,				// Byte to start looking in.
	char				terminating_value,		// Stop extracting when we see this value.
	size_t      		max_length,				// Error if we try to extract more than this.
	size_t *			length_ptr				// Pointer to location to receive extracted length.
)
{
	char const *	current_byte_ptr = start_ptr;
	char * 			return_buf = malloc(max_length);
	int 			return_buf_index = 0;
	*length_ptr = 0;
	
	while(*current_byte_ptr != terminating_value)
	{
		return_buf[return_buf_index] = *current_byte_ptr;
		++return_buf_index;
		++current_byte_ptr;
		++(*length_ptr);
		
		assert(*length_ptr <= max_length);
		
	}
	
	*length_ptr =- 1;
	
	return return_buf;
}


// Parses and returns the return_id field from the specified serialized RobotCommand message.
// Supplied byte_index indicates the first byte of the serialized message.
in_port_t parse_return_id(char const * const current_byte_ptr, int * byte_index_ptr)
{
	if(current_byte_ptr[*byte_index_ptr] != START_TRANSMISSION)
	{
		// Bail and return empty value.
		printf("RobotCommand: the header of the command is incorrect\nShould be: %x Was: %x\n", START_TRANSMISSION, current_byte_ptr[*byte_index_ptr]);
		return 0x0;
	}

	// Have start of command.  Begin parsing.

	++*(byte_index_ptr);
	assert(current_byte_ptr[*byte_index_ptr] == START_ID);
	++*(byte_index_ptr);
	//should now be the first byte of the ASCII-encoded id

	// We are expecting up to 10 ASCII digits to represent a |in_port_t| value, plus trailing NULL.
	#define MAX_RETURN_ID_STRING_LENGTH 10
	int id_storage_length;
	
	// Copy bytes until END_ID encountered.
	char* id_storage = extract_bytes_until((current_byte_ptr + *byte_index_ptr), END_ID, MAX_RETURN_ID_STRING_LENGTH, &id_storage_length);
	
	printf("id_storage_length: %d\n", id_storage_length);
	(*byte_index_ptr) += id_storage_length;
	
	// NULL-terminate our string for atoi().
	id_storage[id_storage_length + 1] = 0x0;
	
	//do this here so we can free id_storage
	in_port_t return_id = atoi(id_storage);

	// Release buffer.
	free(id_storage);

	return return_id;
}

// Parses and returns the opcode field from the specified serialized RobotCommand message.
// Supplied byte_index indicates the index of START_OPCODE.
// Returns 0 on error.
char parse_opcode(char const * const current_byte_ptr, int * byte_index_ptr)
{
	printf("current_byte_ptr: %x\n", current_byte_ptr[*byte_index_ptr]);
	assert(current_byte_ptr[*byte_index_ptr] == START_OPCODE);
	++(*byte_index_ptr);

	char opcode = current_byte_ptr[*byte_index_ptr];
	++(*byte_index_ptr);

	if(current_byte_ptr[*byte_index_ptr] != END_OPCODE)
	{
		// Bail and return empty value.
		printf("RobotCommand: the end of the opcode command is incorrect\nShould be: %x Was: %x\n", END_OPCODE, current_byte_ptr[*byte_index_ptr]);
		return 0x0;
	}
	
	return opcode;
}

// Reads as many params as possible from the command and returns a pointer to an array of them,
// as well as the count of params found.  If it can't find any parameters, it returns NULL.
// |byte_index| assumed to indicate START_PARAMS; if not, assumes no params present.
double * parse_params(char const * const current_byte_ptr, int * byte_index_ptr, unsigned int * params_count_ptr)
{
	if(current_byte_ptr[*byte_index_ptr] != START_PARAMS)
	{
		// No parameters in this message.
		*params_count_ptr = 0;
		return NULL;
	}

	// Allocate buffer for one parameter.
	// A double should fit within a 50-byte string.  We will want a null-terminated representation.
	#define MAX_PARAM_STRING_LENGTH 50
	param_type * param_vector = malloc(MAX_PARAM_STRING_LENGTH + 1);
	*params_count_ptr = 1;
	
	 // Loop until we don't receive the start of a parameter
	while(current_byte_ptr[*byte_index_ptr] == START_PARAMS)
	{
		unsigned int param_bytes_length;
		*byte_index_ptr += 1;
		
		// Copy bytes of parameter's ASCII representation to a separate buffer.
		char* param_bytes_buffer = extract_bytes_until((current_byte_ptr + *byte_index_ptr), END_PARAMS, MAX_PARAM_STRING_LENGTH, &param_bytes_length);
		
		//increase size of param_vector by 1
		++(*params_count_ptr);
		param_vector = (param_type *) realloc(param_vector, ((*params_count_ptr) * sizeof(param_type)));
		
		
		//provide an endstop for strtod
		param_bytes_buffer[param_bytes_length + 1] = 0x0;
		
		
		// Extract |double| into the array we're returning.
		param_vector[*params_count_ptr] = strtod(param_bytes_buffer, NULL);
		
		
		// Advance to next input byte.
		++(*byte_index_ptr);
	}

	return param_vector;
}

// Reads a string parameter from the command and returns a pointer to a heap buffer
// containing the string (caller takes ownership of buffer).
// If it can't find any string, it returns NULL.
char* parse_string(char const * const current_byte_ptr, int * byte_index_ptr, size_t * string_length_ptr)
{
	//size of buffer to hold string
	#define MAX_STRING_LENGTH 50
	
	if(current_byte_ptr[*byte_index_ptr] != START_STRING)
	{
		// No string parameter found.
		*string_length_ptr = 0;
		return NULL;
	}
	
	// Extract and return the string.
	return extract_bytes_until((current_byte_ptr + *byte_index_ptr), END_STRING, MAX_STRING_LENGTH, string_length_ptr);
}


RobotCommand * rc_factory(char const * const current_byte_ptr)
{
	int 									byte_index = 0;
	in_port_t								return_id;
	char 									opcode;
	param_type *			 				params = NULL;
	size_t									params_count = 0;
	size_t									string_length = 0;
	char* 									string = NULL;


	return_id = parse_return_id(current_byte_ptr, &byte_index);

	++byte_index;
	opcode = parse_opcode(current_byte_ptr, &byte_index);

	// Move into params if any.
	++byte_index;
	// should NOW equal END_TRANSMISSION or START_PARAMS

	//returns an empty optional if there isn't a param_type
	//we take ownership of returned buffer
	params = parse_params(current_byte_ptr, &byte_index, &params_count);
	
	//we take ownership of returned pointer
	string = parse_string(current_byte_ptr, &byte_index, &string_length);
	
	RobotCommand * command_ptr = malloc(sizeof(RobotCommand));
	
	command_ptr->return_id 			= return_id;
	command_ptr->opcode 			= opcode;
	command_ptr->params 			= params;
	command_ptr->params_count 		= params_count;
	command_ptr->string 			= string;
	command_ptr->string_length 		= string_length;
	
	return command_ptr;
}

void rc_print(RobotCommand * command_ptr)
{
	printf("Robot Command Dump:\n");
	printf("return_id: %u\n", command_ptr->return_id);
	printf("opcode: %x\n", command_ptr->opcode);
	if(command_ptr->params_count > 0)
	{
		printf("params: ");
		for(int counter = 0; counter < command_ptr->params_count; ++counter)
		{
			printf("%d ", command_ptr->params[counter]);
		}	
		printf("\n");
	}
	
	if(command_ptr->string != NULL)
	{
		printf("string: %s\n", command_ptr->string);
	}
	
	printf("\n");
	
}

void rc_free(RobotCommand * command_ptr)
{
	free(command_ptr->string);
	free(command_ptr->params);
	free(command_ptr);
}