################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/main.cpp \
../src/robot_socket.cpp 

OBJS += \
./src/main.o \
./src/robot_socket.o 

CPP_DEPS += \
./src/main.d \
./src/robot_socket.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	arm-linux-gnueabi-g++ -DUNIT_TESTS -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/io-headers" -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/src/qualcomm" -I/home/jamie/libboost/include -O2 -g3 -gstabs -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


