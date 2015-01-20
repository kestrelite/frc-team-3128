package frc3128.HardwareLink.Motor.SpeedControl;

import com.sun.squawk.util.MathUtils;
import frc3128.HardwareLink.Motor.MotorControl;
import frc3128.Util.RobotMath;

/**
 *
 * @author Yousuf Soliman
 */

public class LinearAngleTarget extends MotorControl {
    private double minSpeed;
    private double targetAngle, threshold, kP;
    private double backlash;
    
    public LinearAngleTarget(double minSpeed, double threshold, double kP, double backlash) {
        if (!RobotMath.isValidPower(minSpeed)) {
            throw new IllegalArgumentException("The minimum power is incorrect!");
        }
        this.minSpeed = Math.abs(minSpeed);
        this.threshold = threshold;
        this.kP = kP;
        this.backlash = backlash;
    }

    public void setControlTarget(double val) {
        this.targetAngle = (val % 180 == 0 ? this.targetAngle : val);
    }

    public double speedControlStep(double dt) {
        double error = RobotMath.angleDistance(this.getLinkedEncoderAngle(), this.targetAngle);
        if(Math.abs(error) < threshold) {return 0;}
        error += (error > 0 ? backlash : -backlash);
        
        double sgn = RobotMath.sgn(error);
        double pGain = sgn*(Math.abs(error))*((1-this.minSpeed)/90.0)+this.minSpeed;
        double t = this.getLastRuntimeDist();
        pGain = (Math.abs(pGain) > this.minSpeed ? pGain : RobotMath.getMotorDirToTarget(this.getLinkedEncoderAngle(), this.targetAngle)*this.minSpeed);
        
        double pow = (pGain - this.getLinkedMotorSpeed())*(MathUtils.pow(2, t/225.0)-1)+this.getLinkedMotorSpeed();
        if(t > 0 || t > 225) pow = pGain;
        
        //DebugLog.log(DebugLog.LVL_INFO, this, ""+pow+"; mS:"+t+"; diff:"+(pGain-this.getLinkedMotorSpeed()));
        return pow;
    }

    public void clearControlRun() {}

    public boolean isComplete() {
        double x =  Math.abs(RobotMath.angleDistance(this.getLinkedEncoderAngle(), this.targetAngle));
        return x < threshold;
    }
}
