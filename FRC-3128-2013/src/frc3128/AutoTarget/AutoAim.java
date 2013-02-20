package frc3128.AutoTarget;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import frc3128.Connection.Connection;
import frc3128.DriveTank.StopDrive;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.EventManager.EventSequence.SingleSequence;
import frc3128.Global;
import frc3128.PneumaticsManager.PneumaticsManager;

public class AutoAim extends Event {
    EventSequencer aAim;
    public void execute() {
        aAim = new EventSequencer();
        aAim.addEvent(new SingleSequence() {
            public void execute() {
                Global.mShoot.set(-1.0);
                Global.mShoot2.set(-1.0);
            }
        });
        aAim.addEvent(new AutoTurn());
        //aAim.addEvent(new AutoLock());
        //aAim.addEvent(new AutoFire());
        aAim.addEvent(new SingleSequence() {
            public void execute() {
                Global.mShoot.set(0);
                Global.mShoot2.set(0);
            }
        });
        
        aAim.startSequence();
    }
}

class AutoTurn extends SequenceEvent {
    double xOff = 0, thresh = 5.0;
    
    public void execute() {
        xOff = NetworkTable.getTable("camera").getNumber("xoffset");
        if (Math.abs(xOff) > thresh) {
            Global.mLB.set(((-xOff) / 90) - .2);
            Global.mLF.set(((-xOff) / 90) - .2);
            Global.mRB.set(((-xOff) / -90) + .2);
            Global.mRF.set(((-xOff) / -90) + .2);
        } else (new StopDrive()).registerSingleEvent();
    }

    public boolean exitConditionMet() {
        return (Math.abs(xOff) < thresh);
    }
}

class AutoLock extends SequenceEvent {
    private double angle = 0;
    private double max = -31.0;
    private double thShift = 5.0;
    private double thresh = 3.0;
    
    public void execute() {
        this.angle = 180*(MathUtils.atan2(92, Connection.distToGoal))/(Math.PI)+thShift;
        
        if(this.angle < this.max) this.angle = this.max; if(this.angle > -1) this.angle = 0;
        if(Math.abs(angle - Global.gTilt.getAngle()) > 1) 
                Global.mTilt.set(-1.0*(angle - Global.gTilt.getAngle())/40.0+0.1);
        else Global.mTilt.set(.20);
    }

    public boolean exitConditionMet() {
        return (Math.abs(Global.gTilt.getAngle()-this.angle) < thresh);
    }
}

class AutoFire extends SequenceEvent {
    public boolean exitConditionMet() {
        return (this.getRunTimeMillis() > 2000);
    }

    private long lastTime = -1;
    public void execute() {
        if(lastTime < 750  && this.getRunTimeMillis() >=  750) PneumaticsManager.setPistonInvertState(Global.pstFire);
        if(lastTime < 1500 && this.getRunTimeMillis() >= 1500) PneumaticsManager.setPistonInvertState(Global.pstFire);
        lastTime = this.getRunTimeMillis();
    }
}