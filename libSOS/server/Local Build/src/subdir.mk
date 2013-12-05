################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/Options.cpp \
../src/main.cpp 

OBJS += \
./src/Options.o \
./src/main.o 

CPP_DEPS += \
./src/Options.d \
./src/main.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -I/home/jamie/libboost-host/include -I"/home/jamie/eclipse/workspace/libSOS-repo/frc-team-3128/libSOS/server/src/libSOS" -I"/home/jamie/eclipse/workspace/libSOS-repo/frc-team-3128/libSOS/server/src/common" -I../frc-3128-beagleboard/frc-3128-beagleboard/io-headers -I"/home/jamie/eclipse/workspace/libSOS-repo/frc-team-3128/libSOS/server/src/robotmessagequeue" -I"/home/jamie/eclipse/workspace/libSOS-repo/frc-team-3128/libSOS/server/src" -O0 -g3 -gstabs -Wall -c -fmessage-length=0 -std=c++11 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


