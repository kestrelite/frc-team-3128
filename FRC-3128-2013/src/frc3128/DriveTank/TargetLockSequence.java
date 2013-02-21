package frc3128.DriveTank;

import com.sun.squawk.util.MathUtils;
import frc3128.AutoTarget.AutoTurn;
import frc3128.Connection.CameraCon;
import frc3128.DebugLog;
import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.EventManager.EventSequence.SingleSequence;
import frc3128.Global;

public class TargetLockSequence extends EventSequencer {
    public TargetLockSequence() {
        this.addEvent(new SingleSequence() {
            public void execute() {
                Global.mShoot1.set(0.0);
                Global.mShoot2.set(0.0);
            }
        });

        this.addEvent(new AutoTurn());
        
        this.addEvent(new SingleSequence() {
            public void execute() {
                Global.mShoot1.set(-1.0);
                Global.mShoot2.set(-1.0);
            }
        });
        
        this.addEvent(new MTiltLock());
    }
}

class MTiltLock extends SequenceEvent {
    private double angle = 0;
    private boolean isLocked = false;
    private boolean taskDone = false;
    private double max = -35.0;
    
    public void execute() {
        if(this.getRunTimeMillis() > 3000) Global.robotStop();
        
        if(!isLocked) {
            Global.msTilt.getLock(this, true);
            this.angle = -180*(MathUtils.atan2(92, CameraCon.distToGoal))/(Math.PI)*0.9;
            if(CameraCon.distToGoal != 0) isLocked = true;
            else return;
        }
        
        System.out.println("angle: "+angle+" cAng: "+Global.gTilt.getAngle() + " pow:" + Global.msTilt.get());
        
        if(this.angle < this.max) {DebugLog.log(1, referenceName, "Tilt targeted to invalid angle! " + this.angle); this.angle = this.max;} 
        if(this.angle > -1) {DebugLog.log(1, referenceName, "Tilt targeted to invalid angle! " + this.angle); this.angle = 0;}
        
        if(isLocked && Math.abs(angle - Global.gTilt.getAngle()) > 1.0) 
            Global.msTilt.set(-1.0*(angle - Global.gTilt.getAngle())/55.0+0.15, this);
        else {if(isLocked) Global.msTilt.set(.15, this); taskDone = true;}
    }

    public boolean exitConditionMet() {
        if(taskDone) {
            Global.msTilt.releaseLock(this);
            this.isLocked = false;
        }
        return taskDone;
    }
}