/*
 * SOSProtocol.h
 *
 *  Created on: Nov 16, 2013
 *      Author: jamie
 */

#ifndef SOSPROTOCOL_H_
#define SOSPROTOCOL_H_

/*
 * Description of the communication protocol:
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

/* Range of allowable control codes.  All codes below
   must be in this range.
*/
#define MIN_CONTROL_CODE			0x00
#define MAX_CONTROL_CODE			0x10

/* Start and end of overall message. */
#define START_TRANSMISSION 			0x01
#define END_TRANSMISSION 			0x06

/* Start and end of sender id field, which must be unique for each sender.
   Encoded as an |unsigned int| in ASCII.
*/
#define START_ID 					0x05
#define END_ID	 					0x0A

/* Start and end of opcode, which is unique to each message and which
   tells the robot what command is being issued.
   Encoded as a byte.
*/
#define START_OPCODE				0x0D
#define END_OPCODE 					0x04

/* Start and end of one or more parameter values. (Omitted if no parameters
    are included.)  Each parameter is an ASCII-encoded double value.
*/
#define START_PARAMS 				0x09
#define END_PARAMS 					0x0B

/* Start and end of string parameter.  (Omitted if no string included.)
    If present, consists of one or more ASCII bytes.  Must be NULL terminated.
*/
#define START_STRING 				0x02
#define END_STRING 					0x03

#endif /* SOSPROTOCOL_H_ */
