package frc3128.DriveTank;

import com.sun.squawk.util.MathUtils;
import frc3128.Connection.Connection;
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
    private double max = -31.0;
    
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
        System.out.println("angle: "+angle+" cAng: "+Global.gTilt.getAngle());
        if(this.angle < this.max) this.angle = this.max; if(this.angle > -1) this.angle = 0;
        if(isLocked && Math.abs(angle - Global.gTilt.getAngle()) > 1) 
                Global.mTilt.set(-1.0*(angle - Global.gTilt.getAngle())/40.0+0.1);
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
        //DriveTank.tLock.lockTo(Global.gTilt.getAngle());
        DriveTank.tLock.lockReturn();
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


class TargetTilt extends Event {
    double thShift = 9.0;
    double targetTh = 0;
    public void execute() {
        targetTh = ((targetTh > 45) ? 23 : 180*(MathUtils.atan2(92, Connection.distToGoal))/(Math.PI)+thShift);
        DriveTank.tLock.lockTo(-targetTh);
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
        (new TargetTilt()).registerIterableEvent(); tLock.registerIterableEvent();
    }
}
