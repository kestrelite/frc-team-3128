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
    
    public void lockAt(double angle) {
        this.angle = angle;
        this.registerIterableEvent();
    }
    
    public void lockToGyro() {
        this.angle = Global.gTilt.getAngle();
        this.registerIterableEvent();
    }
    
    public void execute() {
        if(Math.abs(angle - Global.gTilt.getAngle()) > 1) Global.mTilt.set((angle - Global.gTilt.getAngle())/25.0);
        else Global.mTilt.set(0);
    }
}

class TiltUp extends Event {
    public void execute() {
        DriveTank.tLock.cancelEvent();
        Global.mTilt.set(0.4);
    }
}


class TiltDown extends Event {
    public void execute() {
        DriveTank.tLock.cancelEvent();
        Global.mTilt.set(-0.4);
    }
}

class TiltStop extends Event {
    public void execute() {
        Global.mTilt.set(0);
        DriveTank.tLock.lockToGyro();
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


class SpinToggle extends Event {
    private boolean spinOn = false;
    public void execute() {
        Global.mShoot.set(spinOn ? 0.0 : 1.0);
        this.spinOn = !this.spinOn;
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
        DriveTank.tLock.lockToGyro();
    }
}
