package frc3128.RobotMovement;

import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.Util.Constants;

public class CockArm extends Event {
    private boolean warned = false;
    private boolean cockArmActive = true;
    
    public void execute() {
        if(!cockArmActive || !Constants.SHOOTER_ENABLED) return;
        if(!Global.shooterTSensor.get()) //touch sensor pressed
            Global.mShooter.setSpeed(-1.0);
        else
            Global.mShooter.setSpeed(0);
    }
    
    public void stopArmCock() {cockArmActive = false;}
    public void startArmCock() {cockArmActive = true;}
}
