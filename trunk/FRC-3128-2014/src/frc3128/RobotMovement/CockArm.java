package frc3128.RobotMovement;

import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.Util.DebugLog;

public class CockArm extends Event {
    private boolean warned = false;
    
    public void execute() {
        if(!warned) DebugLog.log(DebugLog.LVL_SEVERE, this, "Do not run this code without enabling the touch sensor!"); warned = true;
        if(!false/*touch sensor pressed*/)
            Global.mShooter.setSpeed(-1.0);
        else
            Global.mShooter.setSpeed(0);
    }
}
