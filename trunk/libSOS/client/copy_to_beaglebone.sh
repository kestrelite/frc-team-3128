#!/bin/bash

BASEDIR=$(cygpath -u $(dirname $0))
echo $BASEDIR

scp -r "$BASEDIR/../client" root@10.31.28.144:/root/libSOS

read -p "Press [Enter] key to continue..."