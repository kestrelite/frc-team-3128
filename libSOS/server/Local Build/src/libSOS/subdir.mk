################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/libSOS/SocketServer.cpp 

C_SRCS += \
../src/libSOS/SOSClient.c 

OBJS += \
./src/libSOS/SOSClient.o \
./src/libSOS/SocketServer.o 

C_DEPS += \
./src/libSOS/SOSClient.d 

CPP_DEPS += \
./src/libSOS/SocketServer.d 


# Each subdirectory must supply rules for building sources it contributes
src/libSOS/%.o: ../src/libSOS/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: Cross GCC Compiler'
	gcc -I../frc-3128-beagleboard/frc-3128-beagleboard/io-headers -I/home/jamie/libboost/include -O2 -g3 -gstabs -pedantic -Wall -c -fmessage-length=0 -v -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

src/libSOS/%.o: ../src/libSOS/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -I/home/jamie/libboost-host/include -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/src/libSOS" -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/src/qualcomm" -I../frc-3128-beagleboard/frc-3128-beagleboard/io-headers -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/src/robotmessagequeue" -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/src" -O0 -g3 -gstabs -Wall -c -fmessage-length=0 -std=c++11 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


