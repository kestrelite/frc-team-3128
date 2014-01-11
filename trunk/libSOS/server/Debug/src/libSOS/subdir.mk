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
	@echo 'Invoking: Cygwin C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

src/libSOS/%.o: ../src/libSOS/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cygwin C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


