package frc3128.DriveTank;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.EventManager.ListenerManager;
import frc3128.PneumaticsManager.PneumaticsManager;
import frc3128.Targeting.TiltLock;

class Drive extends Event {
    public void execute() {
        double x = Global.xControl1.x1;
        double y = Global.xControl1.y1;

        if (Math.abs(y) < 0.15) y = 0;
        if (Math.abs(x) < 0.15) x = 0;
                
        Global.mLB.set(-1.0 * ((y - (x / 1.5))));
        Global.mRB.set(-1.0 * (-(y + (x / 1.5))));
        Global.mLF.set(-1.0 * ((y - (x / 1.5))));
        Global.mRF.set(-1.0 * (-(y + (x / 1.5))));
    }
}

class TiltUp extends Event {
    public void execute() {
        DriveTank.tLock.disableLock();
        Global.mTilt.set(0.4);
    }
}

class TiltDown extends Event {
    public void execute() {
        DriveTank.tLock.disableLock();
        Global.mTilt.set(-0.4);
    }
}

class TiltStop extends Event {
    public void execute() {
        Global.mTilt.set(.15);
        DriveTank.tLock.lockReturn();
    }
}

class PistonFlip extends Event {
    public void execute() {
        PneumaticsManager.setPistonInvertState(Global.pstFire);
    }
}

class SpinToggle extends Event {
    boolean spinRunning = false;
    public void execute() {
        Global.mShoot.set((spinRunning) ? -0.11 : -1.0);
        Global.mShoot2.set((spinRunning) ? -0.11 : -1.0);
        spinRunning = !spinRunning;
    }
}

public class DriveTank {
    public static TiltLock tLock = new TiltLock();
    
    public DriveTank() {
        ListenerManager.addListener(new Drive(), "updateJoy1");
        ListenerManager.addListener(new TiltDown(), "buttonRBDown");
        ListenerManager.addListener(new TiltStop(), "buttonRBUp");
        ListenerManager.addListener(new TiltUp(), "buttonLBDown");
        ListenerManager.addListener(new TiltStop(), "buttonLBUp");
        
        ListenerManager.addListener(new PistonFlip(), "buttonADown");
        ListenerManager.addListener(new PistonFlip(), "buttonAUp");

        ListenerManager.addListener(new SpinToggle(), "buttonBDown");
        (new PistonFlip()).registerSingleEvent();
        Global.gTilt.reset(); DebugLog.log(2, "DriveTank", "GTilt reset starting manual! **Remove for autonomous**");
        //(new TiltTarget()).registerIterableEvent(); tLock.registerIterableEvent();
    }
}