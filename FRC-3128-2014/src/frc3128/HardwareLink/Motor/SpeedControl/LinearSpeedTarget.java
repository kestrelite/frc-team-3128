package frc3128.HardwareLink.Motor.SpeedControl;

import frc3128.HardwareLink.Motor.MotorSpeedControl;
import frc3128.Util.RobotMath;

/**
 * Used for computing a target power with a linear shift.
 * 
 * @author Noah Sutton-Smolin
 */
//TODO Test LinearSpeedTarget
public class LinearSpeedTarget extends MotorSpeedControl {
    private double tgtSpeed;
    private double dtAccum;
    private double refreshTime = 50;
    private double lastEncoderAngle = -1;

    /**
     * 
     * @param tgtSpeed    target speed in deg/sec
     * @param refreshTime speed update rate in msec
     */
    public LinearSpeedTarget(double tgtSpeed, double refreshTime) {
        this.tgtSpeed = tgtSpeed; this.refreshTime = refreshTime/1000.0;
        lastEncoderAngle = this.getLinkedEncoderAngle();
    }
    
    /**
     * Uses a default refreshTime of 50msec
     * 
     * @param tgtSpeed target speed in deg/sec
     */
    public LinearSpeedTarget(double tgtSpeed) {this(tgtSpeed, 0.050);}
    
    public void setControlTarget(double d) {tgtSpeed = d; lastEncoderAngle = this.getLinkedEncoderAngle();}

    public double speedTimestep(double dt) {
        dtAccum += dt; if(dtAccum < refreshTime) return this.getLinkedMotorSpeed();

        // Power * (current rate of change) / (target rate of change) => pow * (deg/sec) / (deg/sec) => pow
        double retVal = RobotMath.makeValidPower((this.getLinkedMotorSpeed() / tgtSpeed) * ((this.getLinkedEncoderAngle()-lastEncoderAngle)/dtAccum));
        dtAccum = 0; lastEncoderAngle = this.getLinkedEncoderAngle(); return retVal;
    }

    /**
     * Sets the speed update time in msec
     * 
     * @param refreshTime speed update rate in msec
     */
    public void setRefreshTime(double refreshTime) {this.refreshTime = refreshTime/1000.0;}
    
    public void clearControlRun() {this.tgtSpeed = 0; this.lastEncoderAngle = this.getLinkedEncoderAngle();}

    public boolean isComplete() {return false;}
}
