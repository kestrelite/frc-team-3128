package frc3128.DriveTank;

import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.ListenerManager.ListenerManager;
import frc3128.TiltControl.TiltToY2;

class Drive extends Event {
    public void execute() {
        double x = Global.xControl1.x1;
        double y = Global.xControl1.y1;

        if (Math.abs(y) < 0.15) y = 0;
        if (Math.abs(x) < 0.15) x = 0;
        //Global.mTilt.set(y);
        
        Global.mLB.set(-1.0 * ((y - (x / 1.5)) / 1.3));
        Global.mRB.set(-1.0 * (-(y + (x / 1.5))));
        Global.mLF.set(-1.0 * ((y - (x / 1.5)) / 1.3));
        Global.mRF.set(-1.0 * (-(y + (x / 1.5))));
    }
}

class TiltUp extends Event {
    public void execute() {
        Global.mTilt.set(0.4);
    }
}

class TiltDown extends Event {
    public void execute() {
        Global.mTilt.set(-0.4);
    }
}

class TiltStop extends Event {
    public void execute() {
        Global.mTilt.set(0);
    }
}

public class DriveTank {
    private static Drive drive = new Drive();

    public DriveTank() {
        drive.registerIterableEvent();
        ListenerManager.addListener(new TiltUp(), "buttonRBDown");
        ListenerManager.addListener(new TiltStop(), "buttonRBUp");
        ListenerManager.addListener(new TiltDown(), "buttonLBDown");
        ListenerManager.addListener(new TiltStop(), "buttonLBUp");
        ListenerManager.addListener(new TiltToY2(), "updateJoy2");
    }
}