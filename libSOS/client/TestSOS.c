
/**TestSOS.c
 * attempts to use SOSClient to communicate with the server
 */

#include "SOSClient.h"
#include <stdio.h>

void main(int argc, char** argv)
{
  if(argc != 3)
  {
    error("Usage: testsos <hostname> <port>\nex: testsos localhost 2987\n");
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
  sos_send_short(sockfd, 1);
  sos_end_transmission(sockfd);

  //test that we can send multiple shorts
  printf("sending an opcode (0x28) with data \"1, 2, 3\"\n");
  sos_send_opcode(sockfd, 0x28);
  sos_send_short(sockfd, 1);
  sos_send_short(sockfd, 2);
  sos_send_short(sockfd, 3);
  sos_end_transmission(sockfd);

  //test that we can send two-digit numbers
  printf("sending an opcode (0x29) with data \"24\"\n");
  sos_send_opcode(sockfd, 0x29);
  sos_send_short(sockfd, 24);
  sos_end_transmission(sockfd);

  //test that we can send negative numbers
  printf("sending an opcode (0x2A) with data \"-27, -1, 8\"\n");
  sos_send_opcode(sockfd, 0x2A);
  sos_send_short(sockfd, -27);
  sos_send_short(sockfd, -1);
  sos_send_short(sockfd, 8);
  sos_end_transmission(sockfd);

  //test that we can send a string
  printf("sending an opcode (0x2B) with data \"4\" and string \"foo bar\"\n");
  sos_send_opcode(sockfd, 0x2B);
  sos_send_short(sockfd, 4);
  sos_send_string(sockfd, "foo bar");
  sos_end_transmission(sockfd);

  printf("closing connection...\n");
  sos_disconnect(sockfd);
  
}
