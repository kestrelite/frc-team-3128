package frc3128.AutoTarget;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.Global;

public class AutoTurn extends SequenceEvent {
    double xOff = 0, thresh = 5.0;
    
    public void execute() {
        xOff = NetworkTable.getTable("camera").getNumber("xoffset");
        System.out.println("xOff:"+xOff+", pow:"+Global.mLF.get());
        if (Math.abs(xOff) > thresh) {
            Global.mLF.set(-0.95*((xOff) / 75));
            Global.mRF.set(-0.95*((xOff) / 75));
        } else {
            Global.mLF.set(0);
            Global.mRF.set(0);
            Global.mRB.set(0);
            Global.mLB.set(0);
        }
    }

    public boolean exitConditionMet() {
        return (Math.abs(xOff) < thresh);
    }
}

