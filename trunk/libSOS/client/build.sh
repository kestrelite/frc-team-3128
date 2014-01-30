#!/bin/bash 
CC="gcc"

if [ $(uname -o) = "Cygwin" ] ; then
	CC="i686-pc-cygwin-gcc"
fi

CFLAGS="-g -O2 -std=c99 -D_GNU_SOURCE"

#go to the current directoory if on Cygwin
if [ $(uname -o) = "Cygwin" ] ; then
	cd $(cygpath -u $(dirname "$0"))
fi
echo "$CC -c SOSClient.c -o SOSClient.o $CFLAGS"
$CC -c SOSClient.c -o SOSClient.o $CFLAGS
echo "$CC -c TestSOS.c -o TestSOS.o $CFLAGS"
$CC -c TestSOS.c -o TestSOS.o $CFLAGS
echo "$CC -c RobotCommand.c -o RobotCommand.o $CFLAGS"
$CC -c RobotCommand.c -o RobotCommand.o $CFLAGS
echo "$CC SOSClient.o TestSOS.o RobotCommand.o -o testsos"
$CC SOSClient.o TestSOS.o RobotCommand.o -o testsos

if [ $(uname -o) = "Cygwin" ] ; then
	read -p "Press [Enter] key to continue..." # only bug the user if running in GUI shell
fi