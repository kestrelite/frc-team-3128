################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/main.cpp 

OBJS += \
./src/main.o 

CPP_DEPS += \
./src/main.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -I/home/jamie/libboost-host/include -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/src/libSOS" -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/src/qualcomm" -I../frc-3128-beagleboard/frc-3128-beagleboard/io-headers -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/src/robotmessagequeue" -I"/home/jamie/eclipse/workspace/frc-3128-beagleboard/src" -O2 -g3 -gstabs -Wall -c -fmessage-length=0 -std=c++11 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


