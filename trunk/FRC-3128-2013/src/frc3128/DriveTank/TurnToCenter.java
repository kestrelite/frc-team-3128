package frc3128.DriveTank;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import frc3128.EventManager.Event;
import frc3128.Global;

public class TurnToCenter extends Event {
    double xOff = 0, thresh = 5;
    
    public void execute() {
        xOff = NetworkTable.getTable("camera").getNumber("xoffset");
        if (Math.abs(xOff) > thresh) {
            Global.mLB.set(((-xOff) / 90) - .2);
            Global.mLF.set(((-xOff) / 90) - .2);
            Global.mRB.set(((-xOff) / -90) + .2);
            Global.mRF.set(((-xOff) / -90) + .2);
        } else {
            (new StopDrive()).registerSingleEvent(); this.cancelEvent();
        }
    }
}