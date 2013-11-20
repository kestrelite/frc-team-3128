################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
O_SRCS += \
../libSOS/SOSClient.o \
../libSOS/TestSOS.o 

C_SRCS += \
../libSOS/SOSClient.c \
../libSOS/TestSOS.c 

OBJS += \
./libSOS/SOSClient.o \
./libSOS/TestSOS.o 

C_DEPS += \
./libSOS/SOSClient.d \
./libSOS/TestSOS.d 


# Each subdirectory must supply rules for building sources it contributes
libSOS/%.o: ../libSOS/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: Cross GCC Compiler'
	arm-linux-gnueabi-gcc -I../frc-3128-beagleboard/frc-3128-beagleboard/io-headers -I/home/jamie/libboost/include -O2 -g3 -gstabs -pedantic -Wall -c -fmessage-length=0 -v -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


