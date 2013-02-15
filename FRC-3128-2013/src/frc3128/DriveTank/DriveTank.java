package frc3128.DriveTank;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.ListenerManager.ListenerManager;

class Drive extends Event {

    public void execute() {
        double x = Global.xControl1.x1;
        double y = Global.xControl1.y1;

        if (Math.abs(y) < 0.15) {
            y = 0;
        }
        if (Math.abs(x) < 0.15) {
            x = 0;
        }

        DebugLog.log(4, referenceName, "Y: " + y + ", X:" + x);
        Global.mLB.set(((y - (x / 1.5)) / 1.3));
        Global.mRB.set((-(y + (x / 1.5))));
        Global.mLF.set(((y - (x / 1.5)) / 1.3));
        Global.mRF.set((-(y + (x / 1.5))));
    }
}

class BtnFire extends Event {
    public void execute() {
        System.out.println("Button A Pressed!");
    }
}

public class DriveTank {

    private static Drive drive = new Drive();

    public DriveTank() {
        ListenerManager.addListener(drive, "updateDrive");
        ListenerManager.addListener(new BtnFire(), "buttonADown");
    }
}