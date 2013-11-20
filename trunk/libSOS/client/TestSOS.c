
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
  
  printf("sending opcode 0x26...\n");
  sos_send_opcode(sockfd, 0x26);
  
  printf("closing connection...\n");
  sos_end_transmission(sockfd);
  sos_disconnect(sockfd);
  
}
