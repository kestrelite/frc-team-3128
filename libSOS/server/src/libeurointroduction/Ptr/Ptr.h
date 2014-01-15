/*
 * Ptr.h
 *
 *  Created on: Jan 11, 2014
 *      Author: Jamie
 */

#ifndef PTR_H_
#define PTR_H_

class Ptr
{
public:
	Ptr();

	//returns an object's pointer as an int
	//useful for generating guid's
	static unsigned int getValue(void* ptr);

	virtual ~Ptr();
};

#endif /* PTR_H_ */
