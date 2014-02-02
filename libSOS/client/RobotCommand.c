#include "RobotCommand.h"
#include "SOSProtocol.h"

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#define member_size(type, member) sizeof(((type *)0)->member)

in_port_t parse_return_id(char * currentBytePtr, int * iterator)
{
	if(*(currentBytePtr + *(iterator)) != START_TRANSMISSION)
	{
		// Bail and return empty value.
		printf("RobotCommand: the header of the command is incorrect\nShould be: %x Was: %x\n", START_TRANSMISSION, *(currentBytePtr + *(iterator)));
		return 0x0;
	}

	// Have start of command.  Begin parsing.


	++*(iterator);
	//should now equal START_ID
	++*(iterator);
	//should now be the first byte of the ASCII-encoded id

	char* IDStorage = malloc(11);
	int IDStorageIterator = 0;
	
	while(*(currentBytePtr + (++*(iterator))) != END_ID)
	{
		*(IDStorage + IDStorageIterator) = *(currentBytePtr + *(iterator));
		++IDStorageIterator;
	}
	
	//insert a null to stop atoi from
	//reading into unknown memory
	IDStorage[10] = 0x0;
	
	//do this here so we can free IDStorage
	in_port_t return_id = atoi(IDStorage);

	free(IDStorage);
	
	//should now equal END_ID
	return return_id;
}
char parse_opcode(char * currentBytePtr, int * iterator)
{
	// Get opcode.
	++*(iterator);
	char opcode = *(currentBytePtr + *(iterator));
	++*(iterator);

	if(*(currentBytePtr + *(iterator)) != END_OPCODE)
	{
		// Bail and return empty value.
		printf("RobotCommand: the end of the opcode command is incorrect\nShould be: %x Was: %x\n", END_OPCODE, *(currentBytePtr + *(iterator)));
		return 0x0;
	}
	return opcode;
}

//reads as many shorts as possible from the command
//if it can't find any, it returns NULL
short * parse_shorts(char * currentBytePtr, int * iterator, unsigned int * shorts_len)
{
	*(shorts_len) = 0;

	if(*(currentBytePtr + *(iterator)) == START_SHORT_TRANSMISSION)
	{
		short * shortVector = malloc(sizeof(short));
		*(shorts_len) = 1;
		
		 // Loop until we don't receive the start of a short
		while(*(currentBytePtr + *(iterator)) == START_SHORT_TRANSMISSION)
		{
			char shortBytesStorage[11];
			unsigned int shortBytesIterator = 0;
			
			while(*(currentBytePtr + (++(*(iterator)))) != END_SHORT)
			{
				*(shortBytesStorage + shortBytesIterator) = *(currentBytePtr + *(iterator));
			}
			
			//increase size of shortVector by 1
			shortVector = (short *) realloc(shortVector, (*(shorts_len) + 1) * sizeof(short));
			++shorts_len;
			
			//provide an endstop for atoi
			shortBytesStorage[10] = 0x0;
			
			*(shortVector + *(shorts_len)) = atoi(shortBytesStorage);
			++(*(iterator));
		}

		return shortVector;
	}
	else
	{
		return NULL;
	}

}

//tries to read a string from the bytes given
//returns null if it fails
char* parse_string(char * currentBytePtr, int * iterator)
{
	if(*(currentBytePtr + *(iterator)) == START_STRING_TRANSMISSION)
	{
		char * string;
		int counter = 0;
		while(*(currentBytePtr + (++(*(iterator)))) != END_STRING)
		{
			*(string + counter) = *(currentBytePtr + *(iterator));
			++counter;
		}

		return string;
	}
	else
	{
		return NULL;
	}

}

RobotCommand * rc_factory(char * currentBytePtr)
{
	int 									iterator = 0;
	in_port_t								return_id;
	char 									opcode;
	short *			 						shorts = NULL;
	size_t									shorts_len = 0;
	char* 									string = NULL;


	return_id = parse_return_id(currentBytePtr, &iterator);

	++iterator;
	opcode = parse_opcode(currentBytePtr, &iterator);

	// Move into shorts if any.
	++iterator;
	// should NOW equal END_TRANSMISSION or START_SHORT_TRANSMISSION

	//returns an empty optional if there isn't a short
	//we take ownership of returned buffer
	shorts = parse_shorts(currentBytePtr, &iterator, &shorts_len);
	
	//we take ownership of returned pointer
	string = parse_string(currentBytePtr, &iterator);
	
	//maybe someday we'll find a way to utilise our variable-length short parser
	assert(shorts_len <= sizeof(member_size(RobotCommand, shorts)));
	
	RobotCommand * commandPtr = malloc(sizeof(RobotCommand));
	
	commandPtr->return_id = return_id;
	commandPtr->opcode = opcode;
	commandPtr->shorts = shorts;
	commandPtr->shorts_len = shorts_len;
	commandPtr->string = string;
	
	return commandPtr;
}

void rc_print(RobotCommand * commandPtr)
{
	printf("Robot Command Dump:\n");
	printf("return_id: %u\n", commandPtr->return_id);
	printf("opcode: %x\n", commandPtr->opcode);
	if(commandPtr->shorts_len > 0)
	{
		printf("shorts: ");
		for(int counter = 0; counter < commandPtr->shorts_len; ++counter)
		{
			printf("%d ", commandPtr->shorts[counter]);
		}	
		printf("\n");
	}
	
	if(commandPtr->string != NULL)
	{
		printf("string: %s\n", commandPtr->string);
	}
	
	printf("\n");
	
}

void rc_free(RobotCommand * commandPtr)
{
	free(commandPtr->string);
	free(commandPtr->shorts);
	free(commandPtr);
}