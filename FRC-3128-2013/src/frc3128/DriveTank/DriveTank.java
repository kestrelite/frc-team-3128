package frc3128.DriveTank;

import frc3128.EventManager.Event;
import frc3128.EventManager.ListenerManager;
import frc3128.Global;
import frc3128.PneumaticsManager.PneumaticsManager;
import frc3128.Targeting.TiltLock;

/*class Drive extends Event {
    private static boolean updateDrive = true;

    public static void pauseDrive() {
        Drive.updateDrive = false;
        Global.mLB.set(0);
        Global.mRB.set(0);
        Global.mLF.set(0);
        Global.mRF.set(0);
    }

    public static void startDrive() {
        Drive.updateDrive = true;
    }

    public void execute() {
        if (!Drive.updateDrive) return;

        double x = Global.xControl1.x1;
        double y = Global.xControl1.y1;

        if (Math.abs(y) < 0.15) y = 0;
        if (Math.abs(x) < 0.15) x = 0;

        Global.mLB.set(-1.0 * ((y - (x / 2.5))));
        Global.mRB.set(-1.0 * (-(y + (x / 2.5))));
        Global.mLF.set(-1.0 * ((y - (x / 2.5))));
        Global.mRF.set(-1.0 * (-(y + (x / 2.5))));
    }
}

class TiltY2 extends Event {
    public void execute() {
        if (Math.abs(Global.xControl1.y2) > .15)
            Global.msTilt.overridePower((Global.xControl1.y2 < 0 ? 0.05 : 0.27));
        else
            Global.msTilt.overridePower(.15);
    }
}

class JoltTilt extends Event {
    public void execute() {
        Global.msTilt.overridePower(-0.1);
        (new Event(true) {
            public void execute() {
                Global.msTilt.overridePower(0.15);
            }
        }).registerTimedEvent(500);
    }
}

class PistonFlip extends Event {
    public void execute() {
        if (Global.mShoot1.get() == -1.0) {
            PneumaticsManager.setPistonInvertState(Global.pstFire);
        }
    }
}

class SpinToggle extends Event {

    boolean spinRunning = false;

    public void execute() {
        Global.mShoot1.set((spinRunning) ? 0.0 : -1.0);
        Global.mShoot2.set((spinRunning) ? 0.0 : -1.0);
        spinRunning = !spinRunning;
    }
}

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


class LockOnToggle extends Event {
    private boolean lockEnabled = false;
    private TargetLockSequence targetLock = new TargetLockSequence();

    public void execute() {
        if (!lockEnabled) {
            this.targetLock.startSequence();
            this.lockEnabled = true;

            DriveAttack.pauseDrive();
            DriveTank.tLock.disableLock();
        } else {
            this.targetLock.stopSequence();
            this.targetLock.resetSequence();
            this.lockEnabled = false;

            DriveAttack.startDrive();
            DriveTank.tLock.lockTo(Global.gTilt.getAngle());
            Global.msTilt.releaseLock(targetLock);
        }
    }
}

public class DriveTank {

    public static TiltLock tLock = new TiltLock();

    public DriveTank() {
        ListenerManager.addListener(new DriveAttack(), "updateJoy1");
        ListenerManager.addListener(new TiltY2(), "updateJoy2");

        ListenerManager.addListener(new JoltTilt(), "buttonLBDown");
        ListenerManager.addListener(new PistonFlip(), "buttonRBDown");
        ListenerManager.addListener(new PistonFlip(), "buttonRBUp");
        //ListenerManager.addListener(new SpinToggle(), "buttonADown");
        ListenerManager.addListener(new SpinOn(), "buttonADown");
        ListenerManager.addListener(new SpinOff(), "buttonBDown");
        ListenerManager.addListener(new CompressorOff(), "buttonBackDown");
        ListenerManager.addListener(new CompressorOn(), "buttonStartDown");
       

        PneumaticsManager.setPistonStateOn(Global.pstFire);
        tLock.registerIterableEvent();
    }
}*/