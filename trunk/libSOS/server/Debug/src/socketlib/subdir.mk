################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/socketlib/myException.cpp \
../src/socketlib/myHostInfo.cpp \
../src/socketlib/myLog.cpp \
../src/socketlib/myLogTester.cpp \
../src/socketlib/mySocket.cpp \
../src/socketlib/winServer.cpp 

OBJS += \
./src/socketlib/myException.o \
./src/socketlib/myHostInfo.o \
./src/socketlib/myLog.o \
./src/socketlib/myLogTester.o \
./src/socketlib/mySocket.o \
./src/socketlib/winServer.o 

CPP_DEPS += \
./src/socketlib/myException.d \
./src/socketlib/myHostInfo.d \
./src/socketlib/myLog.d \
./src/socketlib/myLogTester.d \
./src/socketlib/mySocket.d \
./src/socketlib/winServer.d 


# Each subdirectory must supply rules for building sources it contributes
src/socketlib/%.o: ../src/socketlib/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	arm-linux-gnueabi-g++ -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/src/qualcomm" -I../frc-3128-beagleboard/frc-3128-beagleboard/io-headers -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/src/robotmessagequeue" -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/src" -I/home/jamie/libboost/include -O2 -g3 -gstabs -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


