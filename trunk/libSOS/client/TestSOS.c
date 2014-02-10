
/**TestSOS.c
 * attempts to use SOSClient to communicate with the server
 */

#include "RobotCommand.h"
#include "SOSClient.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(int argc, char** argv)
{
  if(argc != 3)
  {
	printf("Usage: testsos <hostname> <port>\nex: testsos localhost 2987\n");
	return 1;
  }
  
  printf("trying to connect on %s:%s...\n", argv[1], argv[2]);
  int sockfd = sos_connect(argv[1], atoi(argv[2]));
  
  //test that the basic communucations protocol works
  printf("sending just an opcode (0x26)...\n");
  sos_send_opcode(sockfd, 0x26);
  sos_end_transmission(sockfd);

  //test that we can send short data
  printf("sending an opcode (0x27) with data \"1\"\n");
  sos_send_opcode(sockfd, 0x27);
  sos_send_param(sockfd, 1);
  sos_end_transmission(sockfd);

  //test that we can send multiple shorts
  printf("sending an opcode (0x28) with data \"1, 2, 3\"\n");
  sos_send_opcode(sockfd, 0x28);
  sos_send_param(sockfd, 1);
  sos_send_param(sockfd, 2);
  sos_send_param(sockfd, 3);
  sos_end_transmission(sockfd);

  //test that we can send two-digit numbers
  printf("sending an opcode (0x29) with data \"24\"\n");
  sos_send_opcode(sockfd, 0x29);
  sos_send_param(sockfd, 24);
  sos_end_transmission(sockfd);

  //test that we can send negative numbers
  printf("sending an opcode (0x2A) with data \"-27, -1, 8.975\"\n");
  sos_send_opcode(sockfd, 0x2A);
  sos_send_param(sockfd, -27);
  sos_send_param(sockfd, -1);
  sos_send_param(sockfd, 8.975);
  sos_end_transmission(sockfd);

  //test that we can send a string
  printf("sending an opcode (0x2B) with data \"4\" and string \"foo bar\"\n");
  sos_send_opcode(sockfd, 0x2B);
  sos_send_param(sockfd, 4);
  sos_send_string(sockfd, "foo bar");
  sos_end_transmission(sockfd);
  
  printf("opening another connection...\n");
  int sec_sockfd = sos_connect(argv[1], atoi(argv[2]));
  
  printf("sending an opcode (0x2C) on BOTH connections near-simultaneously\n");
  sos_send_opcode(sockfd, 0x2C);
  sos_send_opcode(sec_sockfd, 0x2C);
  sos_end_transmission(sockfd);
  sos_end_transmission(sec_sockfd);
    
  sleep(1);
  
  for(int commands_printed = 0; commands_printed < 8; commands_printed++)
  {
	  char* commandBytes = sos_read_next_command(sockfd);
	  
	  printf("parsing command %d", commands_printed);
	  
	  RobotCommand * commandPtr = rc_factory(commandBytes);
	  free(commandBytes);
	  
	  rc_print(commandPtr);
	  
	  rc_free(commandPtr);
  }
  
  printf("closing second connection\n");
  sos_disconnect(sec_sockfd);

  printf("closing connection...\n");
  sos_disconnect(sockfd);
  
  return 0;
  
}
