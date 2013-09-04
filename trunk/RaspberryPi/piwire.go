package piwire

/*
#cgo LDFLAGS: -lwiringPi -Wall
#include "wiringPi.h"
*/
import "C"

//*/
const NUM_PINS = 17

const WPI_MODE_PINS = 0
const WPI_MODE_GPIO = 1
const WPI_MODE_GPIO_SYS = 2
const WPI_MODE_PHYS = 3
const WPI_MODE_PIFACE = 4
const WPI_MODE_UNINITIALISED = -1

// Pin modes

const INPUT = 0
const OUTPUT = 1
const PWM_OUTPUT = 2
const GPIO_CLOCK = 3

const LOW = 0
const HIGH = 1

// Pull up/down/none

const PUD_OFF = 0
const PUD_DOWN = 1
const PUD_UP = 2

// PWM

const PWM_MODE_MS = 0
const PWM_MODE_BAL = 1

// Interrupt levels

const INT_EDGE_SETUP = 0
const INT_EDGE_FALLING = 1
const INT_EDGE_RISING = 2
const INT_EDGE_BOTH = 3

// Threads

// Failure modes

const WPI_FATAL = (1 == 1)
const WPI_ALMOST = (1 == 2)

//*/

func WiringPiSetup() {
	C.wiringPiSetup()
}

func WiringPiSetupSys() {
	C.wiringPiSetupSys()
}

func WiringPiSetupGPIO() {
	C.wiringPiSetupGpio()
}

func WiringPiSetupPhys() {
	C.wiringPiSetupPhys()
}

func PinModeAlt(pin, mode int) {
	C.pinModeAlt(C.int(pin), C.int(mode))
}

func PinMode(pin, mode int) {
	C.pinMode(C.int(pin), C.int(mode))
}

func PullUpDnControl(pin, pud int) {
	C.pullUpDnControl(C.int(pin), C.int(pud))
}

func DigitalRead(pin int) int {
	return int(C.digitalRead(C.int(pin)))
}

func DigitalWrite(pin, pud int) {
	C.digitalWrite(C.int(pin), C.int(pud))
}

func PwmWrite(pin, pud int) {
	C.pwmWrite(C.int(pin), C.int(pud))
}

func AnalogRead(pin int) int {
	return int(C.analogRead(C.int(pin)))
}

func AnalogWrite(pin, pud int) {
	C.analogWrite(C.int(pin), C.int(pud))
}

func Delay(e uint) {
	C.delay(C.uint(e))
}

func DelayMicroseconds(e uint) {
	C.delayMicroseconds(C.uint(e))
}
