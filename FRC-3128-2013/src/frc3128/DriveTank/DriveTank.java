package frc3128.DriveTank;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.ListenerManager.ListenerManager;
import frc3128.PneumaticsManager.PneumaticsManager;

class Drive extends Event {
    public void execute() {
        try {
            SmartDashboard.putNumber("Init", 5);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        double x = Global.xControl1.x1;
        double y = Global.xControl1.y1;

        if (Math.abs(y) < 0.15) y = 0;
        if (Math.abs(x) < 0.15) x = 0;
                
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
 class TiltY2 extends Event {
     
 
    public void execute() {
        double y2 = -35 * (joy.getRawAxis(5));
        
        if(y2 > angle && angle >= 0) angle = y2;
        else if(angle > 0 && y2 < 0) angle += y2;
        else if(angle < 0) angle = 0;
        if(Math.abs(angle - gTilt.getAngle()) > 1) mTilt.set((angle - gTilt.getAngle())/35.0);
        
        else mTilt.set(0);
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
        Global.mShoot.set(1.0);
    }
}

class SpinOff extends Event {
    public void execute() {
        Global.mShoot.set(0);
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
        ListenerManager.addListener(new PistonFlip(), "buttonADown");
        ListenerManager.addListener(new PistonFlip(), "buttonAUp");
        ListenerManager.addListener(new SpinOn(), "buttonBDown");
        ListenerManager.addListener(new SpinOff(), "buttonBUp");
        ListenerManager.addListener(new TiltY2(), "UpdateJoy2");
    }
}