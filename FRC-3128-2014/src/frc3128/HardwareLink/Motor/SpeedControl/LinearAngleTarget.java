package frc3128.HardwareLink.Motor.SpeedControl;

import frc3128.HardwareLink.Motor.MotorSpeedControl;
import frc3128.Util.DebugLog;
import frc3128.Util.RobotMath;

/**
 *
 * @author Noah Sutton-Smolin
 */
//TODO Test LinearAngleTarget
public class LinearAngleTarget extends MotorSpeedControl {

    private double minSpeed;
    private double targetAngle, tolerance;

    public LinearAngleTarget(double minSpeed, double tolerance) {
        if (!RobotMath.isValidPower(minSpeed)) {
            throw new IllegalArgumentException("The minimum power is incorrect!");
        }
        this.minSpeed = Math.abs(minSpeed);
        this.tolerance = tolerance;
    }

    public void setControlTarget(double val) {
        this.targetAngle = val;
    }

    public double speedControlStep(double dt) {
        double x = RobotMath.angleDistance(this.getLinkedEncoderAngle(), this.targetAngle);
        DebugLog.log(DebugLog.LVL_DEBUG, this, ""+this.getLinkedEncoderAngle());
        return -RobotMath.sgn(x)*(Math.abs(x)/180.0+this.minSpeed);
    }

    public void clearControlRun() {
    }

    public boolean isComplete() {
        return Math.abs(RobotMath.angleDistance(this.getLinkedEncoderAngle(), this.targetAngle)) < this.tolerance;
    }
}
