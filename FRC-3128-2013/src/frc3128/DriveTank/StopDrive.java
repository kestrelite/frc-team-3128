package frc3128.DriveTank;

import frc3128.EventManager.Event;
import frc3128.Global;

    public class StopDrive extends Event {
        public void execute() {
            Global.mLB.set(0);
            Global.mLF.set(0);
            Global.mRB.set(0);
            Global.mRF.set(0);
        }
    }