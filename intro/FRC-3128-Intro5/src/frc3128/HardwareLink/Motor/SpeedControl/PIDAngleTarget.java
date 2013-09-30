package frc3128.HardwareLink.Motor.SpeedControl;

import frc3128.HardwareLink.Motor.MotorSpeedControl;

/**
 *
 * @author Noah Sutton-Smolin
 */
//TODO: Write and test PIDAngleTarget
public class PIDAngleTarget extends MotorSpeedControl {
    private final double kP, kI, kD;
    private final double secs;
    private       double targetAngle, threshold;
    
    public PIDAngleTarget(double targetAngle, double threshold, int msec, int kP, int kI, int kD) {
        this.kP = kP; this.kI = kI; this.kD = kD;
        this.secs = ((double)msec)/1000.0;
        this.targetAngle = targetAngle; this.threshold = threshold;
    }
    
    public void setControlTarget(double d) {}

    public double speedTimestep(double dt) {return 0;}

    public void clearControlRun() {}
    
    public boolean isComplete() {return Math.abs(this.getLinkedEncoderAngle() - this.targetAngle)<threshold;}
}
