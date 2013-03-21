package frc3128.DriveTank;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.EventManager.ListenerManager;
import frc3128.Global;
import frc3128.PneumaticsManager.PneumaticsManager;

class SpinOn extends Event {
    public void execute() {
        Global.mShoot1.set(-1.0);
        Global.mShoot2.set(-1.0);
    }
}

class SpinOff extends Event {
    public void execute() {
        Global.mShoot1.set(0);
        Global.mShoot2.set(0);
    }
}

class PistonFlip extends Event {
    public void execute() {
        if (Global.mShoot1.get() == -1.0) PneumaticsManager.setPistonInvertState(Global.pstFire);
    }
}

class CompressorOff extends Event {
    public void execute() {
        PneumaticsManager.setCompressorStateOff();
    }
}

class CompressorOn extends Event {
    public void execute() {
        PneumaticsManager.setCompressorStateOn();
    }
}

class UpdateThrottleAngle extends Event {
    public void execute() {
        
    }
}

class Drive extends Event {
    public void execute() {
        //Global.mLF.set(Global.aControl1.y * -1.0);
        //Global.mLB.set(Global.aControl1.y * -1.0);
        //Global.mRF.set(Global.aControl2.y * -1.0);
        //Global.mRB.set(Global.aControl2.y * -1.0);
        DebugLog.log(5, referenceName, "a1Y: " + Global.aControl1.y + ", a2Y: " + Global.aControl2.y);
    }
}

class TiltThrottle extends Event {
    public void execute() {
        DebugLog.log(5, referenceName, "Throttle: " + Global.aControl3.throttle);
        Global.mPIDTilt.setTargetAngle(Global.aControl3.throttle * (1/*scalar*/));
    }
}

public class DriveTankAttack {
    public DriveTankAttack() {
        ListenerManager.addListener(new Drive(), "updateDrive");
        
        ListenerManager.addListener(new SpinOn(), "buttonADown");
        ListenerManager.addListener(new SpinOff(), "buttonBDown");
        ListenerManager.addListener(new PistonFlip(), "buttonRBDown");
        ListenerManager.addListener(new PistonFlip(), "buttonRBUp");        
        ListenerManager.addListener(new CompressorOff(), "buttonBackDown");
        ListenerManager.addListener(new CompressorOn(), "buttonStartDown");
        
        PneumaticsManager.setPistonStateOn(Global.pstFire);
    }
}
