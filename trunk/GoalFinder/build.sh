#!/bin/bash 
CC="gcc"
CXX="g++"

if [ $(uname -o) = "Cygwin" ] ; then
	CC="i686-pc-cygwin-gcc"
	CXX="i686-pc-cygwin-g++"
fi

CFLAGS="-g -O2 -std=c99 -D_GNU_SOURCE"
CPPFLAGS="-g -O2 --std=gnu++11 -I/home/Jamie/local/opencv/include"
LDFLAGS="-L/home/Jamie/local/opencv/lib -lopencv_core -lopencv_highgui -lopencv_video -lopencv_imgproc"

#go to the current directory if on Cygwin
if [ $(uname -o) = "Cygwin" ] ; then
	cd $(cygpath -u $(dirname "$0"))
fi
cmd="$CC -c ../libSOS/client/SOSClient.c -o SOSClient.o $CFLAGS"
echo $cmd
$cmd

cmd="$CC -c ../libSOS/client/RobotCommand.c -o RobotCommand.o $CFLAGS"
echo $cmd
$cmd

cmd="$CXX -c GoalFinder.cpp -o GoalFinder.o $CPPFLAGS"
echo $cmd
$cmd

cmd="$CXX SOSClient.o GoalFinder.o RobotCommand.o -o goalfinder $LDFLAGS"
echo $cmd
$cmd

./goalfinder