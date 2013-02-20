package frc3128.AutoTarget;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import frc3128.Connection.Connection;
import frc3128.DebugLog;
import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.EventManager.EventSequence.SingleSequence;
import frc3128.Global;
import frc3128.PneumaticsManager.PneumaticsManager;

public class AutoAim {
    private static EventSequencer aAim;
    
    public static void initialize() {
        aAim = new EventSequencer();
        aAim.addEvent(new AutoTurn());
        aAim.addEvent(new SingleSequence() {
            public void execute() {
                Global.mShoot.set(-1.0);
                Global.mShoot2.set(-1.0);
            }
        });
        aAim.addEvent(new ATiltLock());
        aAim.addEvent(new AutoFire());
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
        System.out.println("xOff:"+xOff+", pow:"+Global.mLF.get());
        if (Math.abs(xOff) > thresh) {
            Global.mLF.set(-0.95*((xOff) / 75));
            Global.mRF.set(-0.95*((xOff) / 75));
        } else {
            Global.mLF.set(0);
            Global.mRF.set(0);
            Global.mRB.set(0);
            Global.mLB.set(0);
        }
    }

    public boolean exitConditionMet() {
        return (Math.abs(xOff) < thresh);
    }
}

class ATiltLock extends SequenceEvent {
    private double angle = 0;
    private boolean isLocked = false;
    private boolean taskDone = false;
    private double max = -35.0;
    
    public void execute() {
        if(!isLocked) {
            this.angle = -180*(MathUtils.atan2(92, Connection.distToGoal))/(Math.PI)*0.9;
            if(Connection.distToGoal != 0) isLocked = true;
            else return;
        }
        
        System.out.println("angle: "+angle+" cAng: "+Global.gTilt.getAngle() + " pow:" + Global.mTilt.get());
        if(this.angle < this.max) {DebugLog.log(1, referenceName, "Tilt targeted to invalid angle! " + this.angle); this.angle = this.max;} 
        if(this.angle > -1) {DebugLog.log(1, referenceName, "Tilt targeted to invalid angle! " + this.angle); this.angle = 0;}
        if(isLocked && Math.abs(angle - Global.gTilt.getAngle()) > 1.0) 
            Global.mTilt.set(-1.0*(angle - Global.gTilt.getAngle())/55.0+0.15);
        else {if(isLocked) Global.mTilt.set(.15); taskDone = true;}
    }

    public boolean exitConditionMet() {
        return taskDone;
    }
}

class AutoFire extends SequenceEvent {
    public boolean exitConditionMet() {
        return (this.getRunTimeMillis() > 2750);
    }

    private long lastTime = -1;
    public void execute() {
        if(lastTime < 1250  && this.getRunTimeMillis() >=  1250) PneumaticsManager.setPistonInvertState(Global.pstFire);
        if(lastTime < 2250 && this.getRunTimeMillis() >= 2250) PneumaticsManager.setPistonInvertState(Global.pstFire);
        lastTime = this.getRunTimeMillis();
    }
}