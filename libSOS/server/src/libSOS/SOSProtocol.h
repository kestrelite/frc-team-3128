/*
 * SOSProtocol.h
 *
 *  Created on: Nov 16, 2013
 *      Author: jamie
 */

#ifndef SOSPROTOCOL_H_
#define SOSPROTOCOL_H_

/*
 * Description of the commnication protocol:
 *
 *	0x01 (start of transmission)
 *	0x05 (start of id)
 *	0x??... (ASCII-encoded signed short that is used by the robot to pass messages back to the host)
 *	0x0A (end of ID)
 *	0x0D (start of opcode)
 *	0x?? (any byte between 0x1F and 0x80 - opcode that tells the robot what to do)
 *	0x04 (end of opcode)
 *	0x09 (start of short transmission)
 *	0x??... (short as ASCII string)
 *	0x0B (end of short)
 *	0x02 (start of string transmission)
 *	0x??... (ASCII string)
 *	0x03 (end of string transmission)
 *	0x06 (end or transmission)
 */

#define START_TRANSMISSION 			0x01
#define START_ID 					0x05
#define END_ID	 					0x0A
#define START_OPCODE				0x0D
#define END_OPCODE 					0x04
#define START_SHORT_TRANSMISSION 	0x09
#define END_SHORT 					0x0B
#define START_STRING_TRANSMISSION 	0x02
#define END_STRING 					0x03
#define END_TRANSMISSION 			0x06



#endif /* SOSPROTOCOL_H_ */
