#!/bin/bash 
CC="arm-angstrom-linux-gnueabi-gcc"

$CC -c SOSClient.c -o SOSClient.o -g -O2
$CC -c TestSOS.c -o TestSOS.o -g -O2
$CC SOSClient.o TestSOS.o -o testsos