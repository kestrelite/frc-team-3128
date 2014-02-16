/*
 * StackTrace.c
 *
 *  Created on: Feb 1, 2014
 *      Author: Jamie
 */
#include <iostream>
#include <execinfo.h>
#include <stdio.h>

#include "StackTrace.h"
void stackTraceHandler(int sig)
{
  void *array[10];
  size_t size;

  // get void*'s for all entries on the stack
  size = backtrace(array, 10);

  // print out all the frames to stderr
  std::cerr << "Error: signal " << sig << std::endl;
  backtrace_symbols_fd(array, size, fileno(stderr));
  exit(1);
}
