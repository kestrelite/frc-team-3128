package frc3128.HardwareLink.Motor.SpeedControl;

import frc3128.HardwareLink.Motor.MotorControl;

/**
 *
 * @author Noah Sutton-Smolin
 */
//TODO: Write and test PIDSpeedTarget
public class PIDSpeedTarget extends MotorControl {
    public void setControlTarget(double d) {}

    public double speedControlStep(double dt) {return 0;}

    public void clearControlRun() {}
    
    public boolean isComplete() {return false;}
}
