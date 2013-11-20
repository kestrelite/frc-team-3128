
#!/bin/bash -x
for entry in *.dtbo
do
  cp $entry /lib/firmware 
  filename=$(echo $entry |sed 's/.\{10\}$//')
  echo "activating $filename"
  echo $filename > /sys/devices/bone_capemgr.8/slots
done
