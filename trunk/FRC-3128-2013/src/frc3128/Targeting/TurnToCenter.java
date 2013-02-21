package frc3128.Targeting;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.Global;

public class TurnToCenter extends Event {
    double xOff = 0, thresh = 5;
    
    public void execute() {
        xOff = NetworkTable.getTable("camera").getNumber("xoffset");
        if(Math.abs(xOff) > thresh) {
            Global.mLF.set(-1.0*((xOff) / 120));
            Global.mRF.set(-1.0*((xOff) / 120));
        } else {
            (new StopDrive()).registerSingleEvent(); this.cancelEvent();
            DebugLog.log(4, referenceName, "Center alignment reached!");
        }
    }
}