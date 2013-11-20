/*
 * libSOS (Serial-Over-Socket) client
 *
 *  Created on: Nov 10, 2013
 *      Author: jamie
 */


#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/tcp.h>
#include "SOSClient.h"

const char end_transmission = END_TRANSMISSION;

/*these functions from http://www.linuxhowtos.org/C_C++/socket.htm*/
void error(const char *msg)
{
    perror(msg);
    exit(0);
}

int sos_connect(char* hostname, int port)
{
	 portno = port;
	 int flag = 1;

	 int sockfd;
	 sockfd = socket(AF_INET, SOCK_STREAM, 0);
	 if (sockfd < 0)
	 {
		 error("ERROR opening socket");
	 }

	 setsockopt(sockfd, IPPROTO_TCP, TCP_NODELAY, (char *) &flag, sizeof(int));

	 server = gethostbyname(hostname);
	 if (server == NULL)
	 {
		 	fprintf(stderr,"ERROR, no such host\n");
		 	exit(0);
	 }

	 bzero((char *) &serv_addr, sizeof(serv_addr));

	 serv_addr.sin_family = AF_INET;

	 bcopy((char *)server->h_addr, (char *)&serv_addr.sin_addr.s_addr, server->h_length);

	 serv_addr.sin_port = htons(portno);

	 if (connect(sockfd,(struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0)
	 {
         error("ERROR connecting");
	 }

	 return sockfd;

}

void sos_disconnect(int sockfd)
{
	close(sockfd);
}

void sos_send_opcode(int sockfd, char toSend)
{
	char transmission[3];
	transmission[0] = START_TRANSMISSION;
	transmission[1] = toSend;
	transmission[2] = END_OPCODE;
	if(write(sockfd, transmission, sizeof(transmission)) != sizeof(transmission))
	{
		error("SOSClient: 72: Write Error");
	}
}

void sos_send_short(int sockfd, short toSend)
{
#define MAX_LEN 10
	/*output for the ascii short*/
	char 	outputString[MAX_LEN];
	int 	length;
	char 	transmission[MAX_LEN + sizeof(char) + sizeof(char)];  /* Add space for opcodes.*/

	snprintf(outputString, sizeof(outputString), "%d", toSend);
	length = strlen(outputString);

	transmission[0] = START_SHORT_TRANSMISSION;
	strcpy(transmission + 1, outputString);
	transmission[length + 1] = END_SHORT;
	if(write(sockfd, transmission, length + 2) != length + 2)
	{
		error("SOSClient: 92: Write Error");
	}
}

void sos_send_string(int sockfd, char* toSend)
{
	int length = strlen(toSend);
	char transmission[128 + 2];

	transmission[0] = START_STRING_TRANSMISSION;
	strcpy(transmission + 1, toSend);
	transmission[length + 1] = END_STRING;

	if(write(sockfd, transmission, length + 2) != (length + 2))
	{
		error("SOSClient: 112: Write Error");

	}
}


void sos_end_transmission(int sockfd)
{
	if(write(sockfd, &end_transmission, 1) != 1)
	{
		error("SOSClient: 120: Write Error");
	}
}

