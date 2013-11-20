/*
 * SocketClient.h
 *
 * libSOS (Serial-Over-Socket) client
 *
 *  Created on: Nov 10, 2013
 *      Author: jamie
 */

#include "SOSProtocol.h"



/*
 * Usage:
 * sos_connect("hostname", port) inits the connection
 *
 * call sos_send_opcode(char some_opcode); to send the main opcode, then
 *
 * sos_send_short(signed short someShort); as many times as you want to send shorts as data,
 *
 * then sos_send_string(char* someString) if you need to send one
 *
 * MAKE SURE to call sos_end_transmission when you're done
 */

#ifndef SOCKETCLIENT_H_
#define SOCKETCLIENT_H_

#include <netinet/in.h>

struct sockaddr_in serv_addr;
struct hostent *server;

int portno;

int sos_connect(char* hostname, int port);

void sos_disconnect();

void sos_send_opcode(int sockfd, char toSend);

void sos_send_short(int sockfd, short toSend);

void sos_send_string(int sockfd, char* toSend);

void sos_end_transmission(int sockfd);


#endif /* SOCKETCLIENT_H_ */
