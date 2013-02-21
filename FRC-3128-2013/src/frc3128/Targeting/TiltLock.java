package frc3128.Targeting;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.Global;

public class TiltLock extends Event {
    private double angle = 0;
    private boolean isLocked = false;
    private double max = -35.0;
    
    public TiltLock() {}
    public TiltLock(double angle) {
        Global.msTilt.getLock(this); 
        this.angle = angle; this.isLocked = true;
    }
    
    public void lockTo(double angle) {
        if(!Global.msTilt.getLock(this)) {
            DebugLog.log(1, referenceName, "TiltLock could not get TiltSync Lock when lockTo was explicitly called!");
            return;
        }
        this.angle = angle;
        this.isLocked = true;
    }
    
    public void enableLock() {
        if(!Global.msTilt.getLock(this)) {
            DebugLog.log(1, referenceName, "TiltLock could not get the TiltSync Lock when enableLock was explicitly called!");
            return;
        }
        this.isLocked = true;
    }    
    
    public void disableLock() {
        Global.msTilt.releaseLock(this);
        this.isLocked = false;
    }
    
    public void execute() {
        if(!this.isLocked) return;
        if(!Global.msTilt.hasLock(this)) {
            DebugLog.log(2, referenceName, "TiltLock lost the TiltSync lock! Disabling...");
            this.isLocked = false; return;
        }
        
        System.out.println("angle: "+angle+" cAng: "+Global.gTilt.getAngle() + " pow:" + Global.msTilt.get());
        if(this.angle < this.max) {DebugLog.log(1, referenceName, "Tilt targeted to invalid angle! " + this.angle); this.angle = this.max;} 
        if(this.angle > -1) {DebugLog.log(1, referenceName, "Tilt targeted to invalid angle! " + this.angle); this.angle = 0;}
        
        if(isLocked && Math.abs(angle - Global.gTilt.getAngle()) > 1.0) 
            Global.msTilt.set(-1.0*(angle - Global.gTilt.getAngle())/55.0+0.15, this);
        else if(isLocked) Global.msTilt.set(.15, this);
    }
}