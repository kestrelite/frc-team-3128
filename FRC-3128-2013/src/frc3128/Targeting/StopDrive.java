package frc3128.Targeting;

import frc3128.EventManager.Event;
import frc3128.Global;

class StopDrive extends Event {
    public void execute() {
        Global.mLF.set(0);
        Global.mRF.set(0);
        Global.mLB.set(0);
        Global.mRB.set(0);
    }
}
