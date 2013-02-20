package frc3128.Targeting;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.Global;

public class TiltLock extends Event {
    private double angle = 0;
    private boolean isLocked = false;
    private double max = -35.0;
    
    public TiltLock() {}
    public TiltLock(double angle) {this.angle = angle; this.isLocked = true;}
    
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
        System.out.println("angle: "+angle+" cAng: "+Global.gTilt.getAngle() + " pow:" + Global.mTilt.get());
        if(this.angle < this.max) {DebugLog.log(1, referenceName, "Tilt targeted to invalid angle! " + this.angle); this.angle = this.max;} 
        if(this.angle > -1) {DebugLog.log(1, referenceName, "Tilt targeted to invalid angle! " + this.angle); this.angle = 0;}
        if(isLocked && Math.abs(angle - Global.gTilt.getAngle()) > 1.0) 
            Global.mTilt.set(-1.0*(angle - Global.gTilt.getAngle())/55.0+0.15);
        else if(isLocked) Global.mTilt.set(.15);
    }
}