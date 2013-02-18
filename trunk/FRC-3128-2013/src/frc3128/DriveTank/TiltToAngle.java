package frc3128.DriveTank;

import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.ListenerManager.ListenerManager;

public class TiltToAngle extends Event {
    private final double  thresh = 1;
    private       double  targetAngle = -1;
    private       boolean restoreY2 = false;
    
    public void setTiltAngle(double desire) {
        targetAngle = desire;
        this.restoreY2 = ListenerManager.dropEvent(TiltToY2.class);
        this.registerIterableEvent();
    }

    public void execute() {        
        double y2 = Global.xControl1.y2;
        
        if(1==1) throw new Error("Must set gyro polarization");
        if(Math.abs(Global.gTilt.getAngle() - this.targetAngle) > thresh)
            Global.mTilt.set((Global.gTilt.getAngle() - this.targetAngle)/35.0);
        else {
            if(this.restoreY2) ListenerManager.addListener((new TiltToY2()), "updateJoy2");
            this.cancelEvent();
        }
    }
}    