package frc3128.DriveTank;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.EventManager.ListenerManager;
import frc3128.Global;
import frc3128.PneumaticsManager.PneumaticsManager;
import frc3128.Targeting.TiltLock;

class Drive extends Event {
    private boolean updateDrive = true;
    
    public void pauseDrive() {this.updateDrive = false;}
    public void startDrive() {this.updateDrive = true;}
    
    public void execute() {
        if(!this.updateDrive) return;
        
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

class TiltTriggers extends Event {
    public void execute() {
        if(Global.msTilt.hasLock(this)) {
            if(Math.abs(Global.xControl1.triggers) > 100) {
                DriveTank.tLock.disableLock();
                Global.msTilt.set(1.0*0.4*(Global.xControl1.triggers/Math.abs(Global.xControl1.triggers)), this);
            } else
                DriveTank.tLock.lockTo(Global.gTilt.getAngle());
        } else Global.msTilt.getLock(this);
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
        Global.mShoot1.set((spinRunning) ? -0.11 : -1.0);
        Global.mShoot2.set((spinRunning) ? -0.11 : -1.0);
        spinRunning = !spinRunning;
    }
}

class LockOnToggle extends Event {
    private boolean lockEnabled = false;
    private TargetLockSequence targetLock = new TargetLockSequence();
    
    public void execute() {
        if(!lockEnabled) {
            this.lockEnabled = true;
            this.targetLock.startSequence();
        } else {
            this.targetLock.stopSequence();
            Global.msTilt.releaseLock(targetLock);
            this.lockEnabled = false;
        }
    }
}

public class DriveTank {
    public static TiltLock tLock = new TiltLock();
    
    public DriveTank() {
        ListenerManager.addListener(new Drive(), "updateJoy1");
        ListenerManager.addListener(new TiltTriggers(), "updateTriggers");
        ListenerManager.addListener(new PistonFlip(), "buttonRBDown");
        ListenerManager.addListener(new PistonFlip(), "buttonRBUp");
        ListenerManager.addListener(new SpinToggle(), "buttonADown");
        
        ListenerManager.addListener(new LockOnToggle(), "buttonLBDown");
        
        DebugLog.log(2, "DriveTank", "setPistonState on of Global.pstFire in DriveTank init - verify On is correct!");
        PneumaticsManager.setPistonStateOn(Global.pstFire); 
        Global.gTilt.reset(); DebugLog.log(2, "DriveTank", "GTilt reset starting manual! **Remove when running autonomous**");
        //(new TiltTarget()).registerIterableEvent(); 
        tLock.registerIterableEvent();
    }
}