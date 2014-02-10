#!/bin/bash

BASEDIR=$(cygpath -u $(dirname $0))
echo $BASEDIR

scp -r "$BASEDIR/../client" root@192.168.1.10:/root/libSOS

read -p "Press [Enter] key to continue..."