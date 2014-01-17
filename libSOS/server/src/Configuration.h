#include <iostream>
//file name for SPI device
#define SPI_DEV "/dev/spidev1.0"

//port numbers of sockets to open
//must be the same length as NUM_SOCKETS
#define SOCKET_PORT 5952

#define CRIO_PORT 4545

//debug thread management
#define DEBUG_THREADS
//debug socket reception
//#define DEBUG_SOCKET_RECEPTION
//debug spi transmission
//#define DEBUG_SERIAL_OUTPUT
//build an object from the binary transmissions - does small amounts of error checking and tells you the content of the command
#define MAKE_ROBOT_COMMANDS
//-----------------------------------------------------------------------------------
//MACRO AREA - no configs here
//-----------------------------------------------------------------------------------

#define debug_log(t, s) std::cout << t << ": " << s << std::endl;
