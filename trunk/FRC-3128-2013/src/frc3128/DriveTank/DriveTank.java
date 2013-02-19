package frc3128.DriveTank;

import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.ListenerManager.ListenerManager;
import frc3128.PneumaticsManager.PneumaticsManager;

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

class TiltLock extends Event {
    private double angle = 0;
    private boolean isLocked = false;
    
    public void lockTo(double angle) {
        this.angle = angle;
        this.isLocked = true;
    }
    
    public void lockReturn() {
        this.isLocked = true;
    }    
    
    public void disableLock() {
        this.isLocked = false;
    }

    public void execute() {
        if(isLocked && Math.abs(angle - Global.gTilt.getAngle()) > 1.5) 
                Global.mTilt.set((angle - Global.gTilt.getAngle())/80.0);
        else if(isLocked) Global.mTilt.set(.20);
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
        Global.mTilt.set(0);
        DriveTank.tLock.lockTo(Global.gTilt.getAngle());
    }
}

class ShootDisc extends Event {
    public void execute() {
        Global.mShoot.set(-0.95);
    }
}

class IdleDisc extends Event {
    public void execute() {
        Global.mShoot.set(0);
    }
}

class PistonFlip extends Event {
    public void execute() {
        PneumaticsManager.setPistonInvertState(Global.pst1);
    }
}


class SpinOn extends Event {
    public void execute() {
        Global.mShoot.set(-1.0);
        Global.mShoot2.set(-1.0);
    }
}

class SpinOff extends Event {
    public void execute() {
        Global.mShoot.set(0);
        Global.mShoot2.set(0);
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

        ListenerManager.addListener(new SpinOn(), "buttonBDown");
        ListenerManager.addListener(new SpinOff(), "buttonXDown");
        (new PistonFlip()).registerSingleEvent();
        Global.gTilt.reset(); //MUST be taken out for Autonomous on full game
        DriveTank.tLock.lockTo(10); tLock.registerIterableEvent();
    }
}
