package main

import (
	"fmt"
	"math"
	"os"
	"strconv"
)

func main() {
	fmt.Println("n-Wheel Swerve Drive Calculations")
	var argsv = os.Args
	var n, _ = strconv.Atoi(argsv[1])
	var xPos = make([]float64, n)
	var yPos = make([]float64, n)
	var spd = make([]float64, n)
	var ang = make([]float64, n)
	var vel, theta, rot float64
	var xVel, yVel float64
	for i := range xPos {
		xPos[i], _ = strconv.ParseFloat(argsv[2*i+2], 64)
		yPos[i], _ = strconv.ParseFloat(argsv[2*i+3], 64)
	}
	vel, _ = strconv.ParseFloat(argsv[2*n+2], 64)
	theta, _ = strconv.ParseFloat(argsv[2*n+3], 64)
	rot, _ = strconv.ParseFloat(argsv[2*n+4], 64)
	xVel = vel * math.Cos(theta*math.Pi/180)
	yVel = vel * math.Sin(theta*math.Pi/180)
	for i := range spd {
		spd[i] = math.Sqrt(math.Pow(xVel+rot*yPos[i], 2) + math.Pow(yVel-rot*xPos[i], 2))
		ang[i] = 180 / math.Pi * (math.Atan2(xVel+rot*yPos[i], yVel-rot*xPos[i]))
		fmt.Println(i)
		fmt.Println("Speed:", spd[i])
		fmt.Println("Angle:", ang[i])
	}
}
