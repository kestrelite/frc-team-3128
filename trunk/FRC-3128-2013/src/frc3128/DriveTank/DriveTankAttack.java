package frc3128.DriveTank;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.EventManager.ListenerManager;
import frc3128.Global;
import frc3128.PneumaticsManager.PneumaticsManager;

class SpinOn extends Event {
    public void execute() {
        Global.mShoot.set(1.0);
    }
}

class TiltBack extends Event {
    public void execute() {
        Global.mTilt.set(Global.getAngleFrom(Global.enc) > 180 ? 0.20 : 0);
    }
}

class TiltStop extends Event {
    public void execute() {
        Global.mTilt.set(0);
    }
}

class TiltForward extends Event {
    public void execute() {
        Global.mTilt.set(Global.getAngleFrom(Global.enc) < 200 ? -0.20 : 0);
    }
}

class SpinOff extends Event {
    public void execute() {
        Global.mShoot.set(0);
    }
}

class PistonFlip extends Event {
    public void execute() {
        if (Global.mShoot.get() == 1.0)
            PneumaticsManager.setPistonInvertState(Global.pstFire);
    }
}

class LiftPistonFlip extends Event {
    public void execute() {
        PneumaticsManager.setPistonInvertState(Global.pstLift);
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

class Drive extends Event {
    public void execute() {
        double y1 = (Math.abs(Global.aControl1.y) < 0.1 ? 0 : Global.aControl1.y);
        double y2 = (Math.abs(Global.aControl2.y) < 0.1 ? 0 : Global.aControl2.y);
        /*y1 = Global.signum(y1) / ((1.0 - MathUtils.pow(Math.E, -1.35)) + MathUtils.pow(Math.E, -5.4 * (Math.abs(y1) - 0.75)));
        y2 = Global.signum(y2) / ((1.0 - MathUtils.pow(Math.E, -1.35)) + MathUtils.pow(Math.E, -5.4 * (Math.abs(y2) - 0.75)));*/
        y1 /= 1.25; y2 /= 1.25;
        Global.mLF.set(y1 * -1.0);
        Global.mLB.set(y1 * -1.0);
        Global.mRF.set(y2 * 1.0);
        Global.mRB.set(y2 * 1.0);
        DebugLog.log(3, referenceName, "Angle: " + Global.getAngleFrom(Global.enc));
        DebugLog.log(5, referenceName, "a1Y: " + Global.aControl1.y + ", a2Y: " + Global.aControl2.y);
    }
}

public class DriveTankAttack {
    public DriveTankAttack() {
        ListenerManager.addListener(new Drive(), "updateDrive");

        ListenerManager.addListener(new TiltBack(), "buttonLBDown");
        ListenerManager.addListener(new TiltStop(), "buttonLBUp");
        ListenerManager.addListener(new TiltForward(), "buttonRBDown");
        ListenerManager.addListener(new TiltStop(), "buttonRBUp");

        ListenerManager.addListener(new SpinOn(), "buttonADown");
        ListenerManager.addListener(new SpinOff(), "buttonBDown");

        ListenerManager.addListener(new PistonFlip(), "buttonXDown");
        ListenerManager.addListener(new PistonFlip(), "buttonXUp");

        ListenerManager.addListener(new LiftPistonFlip(), "buttonYDown");
        ListenerManager.addListener(new LiftPistonFlip(), "button210Down");


        PneumaticsManager.setPistonStateOn(Global.pstFire);
    }
}
