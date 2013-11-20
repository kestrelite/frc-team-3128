#!/bin/bash 
gcc -c SOSClient.c -o SOSClient.o -g -O2
gcc -c TestSOS.c -o TestSOS.o -g -O2
gcc SOSClient.o TestSOS.o -o testsos