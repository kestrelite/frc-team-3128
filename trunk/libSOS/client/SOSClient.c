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
#define MAX_LEN 10

/*these functions from http://www.linuxhowtos.org/C_C++/socket.htm*/
void error(const char *msg)
{
    perror(msg);
    exit(0);
}

// get port, IPv4 or IPv6:
in_port_t get_in_port(struct sockaddr *sa)
{
    if (sa->sa_family == AF_INET) {
        return (((struct sockaddr_in*)sa)->sin_port);
    }

    return (((struct sockaddr_in6*)sa)->sin6_port);
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
	//before we do anything, we have to encode the id of this connection in the message
	struct sockaddr remote_endpoint = {0};
	socklen_t remote_endpoint_length = sizeof(remote_endpoint);
	
	getpeername(sockfd, (struct sockaddr *) &remote_endpoint, &remote_endpoint_length);
	
	in_port_t remote_port = get_in_port(&remote_endpoint);
	
	//turn the integer port into a string
	char id_string[MAX_LEN];
	snprintf(id_string, MAX_LEN, "%d", remote_port);
	int length = strlen(id_string);
	
	char transmission[6 + MAX_LEN];
	transmission[0] = START_TRANSMISSION;
	transmission[1] = START_ID;
	
	strcpy(transmission + 2, id_string);
	
	//now send the opcode
	transmission[length + 2] = END_ID;
	transmission[length + 3] = START_OPCODE;
	transmission[length + 4] = toSend;
	transmission[length + 5] = END_OPCODE;
	if(write(sockfd, transmission, sizeof(transmission)) != sizeof(transmission))
	{
		error("SOSClient: Write Error");
	}
}

void sos_send_short(int sockfd, short toSend)
{
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

