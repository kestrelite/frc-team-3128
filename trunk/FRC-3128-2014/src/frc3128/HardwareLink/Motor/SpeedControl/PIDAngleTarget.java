package frc3128.HardwareLink.Motor.SpeedControl;

import frc3128.HardwareLink.Motor.MotorControl;
import frc3128.Util.DebugLog;

/**
 *
 * @author Noah Sutton-Smolin
 */
//TODO: Write and test PIDAngleTarget
public class PIDAngleTarget extends MotorControl {

    private final double kP, kI, kD;
    private final double secs;
    private double targetAngle, threshold, minSpeed;
    private double intGain;

    public PIDAngleTarget(double minSpeed, double threshold, int msec, double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.secs = ((double) msec) / 1000.0;
        this.minSpeed = minSpeed;
        this.threshold = threshold;
        this.intGain = 0;
    }

    public void setControlTarget(double d) {
        this.targetAngle = d;
    }

    public double speedControlStep(double dt) {
        double error = this.getLinkedEncoderAngle() - this.targetAngle;
        double pGain = this.kP * (error);
        DebugLog.log(DebugLog.LVL_STREAM, this, "dt: " + dt);
        DebugLog.log(DebugLog.LVL_STREAM, this, "Error: " + error);
        DebugLog.log(DebugLog.LVL_STREAM, this, "P: " + (pGain));
        DebugLog.log(DebugLog.LVL_STREAM, this, "I: " + (this.kI * intGain));
        DebugLog.log(DebugLog.LVL_STREAM, this, "PI: " + (pGain + intGain));
        DebugLog.log(DebugLog.LVL_STREAM, this, "Current Angle: " + this.getLinkedEncoderAngle());
        return pGain;
    }

    public void clearControlRun() {
        intGain = 0;
    }

    public boolean isComplete() {
        return Math.abs(this.getLinkedEncoderAngle() - this.targetAngle) < threshold;
    }
}
