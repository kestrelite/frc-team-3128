package frc3128.Targeting;

import frc3128.EventManager.Event;
import frc3128.Global;

public class TiltLock extends Event {
    private double angle = 0;
    private boolean isLocked = false;
    private double max = -31.0;
    
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
        System.out.println("angle: "+angle+" cAng: "+Global.gTilt.getAngle());
        if(this.angle < this.max) this.angle = this.max; if(this.angle > -1) this.angle = 0;
        if(isLocked && Math.abs(angle - Global.gTilt.getAngle()) > 1) 
                Global.mTilt.set(-1.0*(angle - Global.gTilt.getAngle())/40.0+0.1);
        else if(isLocked) Global.mTilt.set(.20);
    }
}