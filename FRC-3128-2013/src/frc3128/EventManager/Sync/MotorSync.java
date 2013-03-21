package frc3128.EventManager.Sync;

import edu.wpi.first.wpilibj.Jaguar;
import frc3128.DebugLog;

public class MotorSync {
    private final Jaguar mtr;
    private boolean isLocked = false;
    private Object lockedBy = new Object();
    
    public MotorSync(int a, int b) {
        this.mtr = new Jaguar(a,b);
    }
    
    public boolean getLock(Object o) {
        if(this.isLocked) {return false;}
        
        this.isLocked = true;
        this.lockedBy = o;
        DebugLog.log(4, this.toString(), "MotorLock was free - taken by " + o + ".");
        return true;
    }
    
    public void getLock(Object o, boolean override) {
        if(override) {
            if(this.isLocked) DebugLog.log(4, this.toString(), "MotorLock was stolen by " + o + "!");
            else DebugLog.log(4, this.toString(), "MotorLock was taken by " + o + ".");
        } else if(this.isLocked) {
            DebugLog.log(3, this.toString(), "MotorLock was attempted by " + o + " but it was not available!");
            return;
        }
        
        this.isLocked = true;
        this.lockedBy = o;
    }
    
    public Object getLockingObject() {return this.lockedBy;}
    
    public boolean hasLock(Object o) {
        return (this.lockedBy.equals(o));
    }
    
    public void releaseLock(Object o) {
        if(this.lockedBy.equals(o)) {
            DebugLog.log(4, this.toString(), "MotorLock was released by " + o + "."); 
            this.isLocked = false; this.lockedBy = null;
        }
        else DebugLog.log(1, this.toString(), "MotorLock was released by " + o + ", but it did not have the lock!");
    }
    
    public void overridePower(double spd) {
        this.mtr.set(spd);
        this.lockedBy = new Object();
        this.isLocked = false;
        DebugLog.log(5, this.toString(), "MotorLock was broken by an override statement! Locks released.");
    }
    
    public void set(double spd, Object o) {
        if(this.lockedBy.equals(o)) this.mtr.set(spd);
        else DebugLog.log(4, this.toString(), "MotorLock had speed set by " + o + ", but it did not have the lock!");
    }
    public double get() {return this.mtr.get();}
}
