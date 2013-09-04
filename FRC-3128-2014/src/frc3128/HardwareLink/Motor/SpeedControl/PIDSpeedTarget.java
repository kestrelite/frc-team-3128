package frc3128.HardwareLink.Motor.SpeedControl;

import frc3128.HardwareLink.Motor.MotorSpeedControl;

/**
 *
 * @author Noah Sutton-Smolin
 */
//TODO: Write and test PIDSpeedTarget
public class PIDSpeedTarget extends MotorSpeedControl {
    public void setControlTarget(double d) {}

    public double speedTimestep(double dt) {return 0;}

    public void clearControlRun() {}
    
    public boolean isComplete() {return false;}
}
