/*
 * robot_spi.h
 *
 *  Created on: Oct 15, 2013
 *      Author: jamie
 */

#ifndef ROBOT_SPI_H_
#define ROBOT_SPI_H_

#include <vector>
#include <string>
#include <fstream>
#include <Configuration.h>


class RoboSPI
{

public:

	std::fstream _fstream;

	RoboSPI();
public:

	void send(char toSend);
	void send(std::vector<char> toSend);

	~RoboSPI();
};

//template<typename T>
//void spiSend( T const & arg);

#endif /* ROBOT_SPI_H_ */
