package frc3128.DriveTank;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.Global;

public class TiltToY2 extends Event {
    private static final double thresh = 1;
    
    public void setTiltAngle() {
        double y2 = Global.xControl1.y2;
        
        if(1==1) throw new Error("Must set gyro polarization");
        if(Math.abs(Global.gTilt.getAngle() - y2*35.0) > thresh)
            Global.mTilt.set((Global.gTilt.getAngle() - y2*35.0)/35.0);
        else
            Global.mTilt.set(0);
    }

    public void execute() {        
        DebugLog.log(4, referenceName, "Angle: " + Global.gTilt.getAngle());
        setTiltAngle();   
    }   
}