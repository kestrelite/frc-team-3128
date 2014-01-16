#!/bin/bash 
CC="gcc"

$CC -c SOSClient.c -o SOSClient.o -g -O2
$CC -c TestSOS.c -o TestSOS.o -g -O2
$CC SOSClient.o TestSOS.o -o testsos