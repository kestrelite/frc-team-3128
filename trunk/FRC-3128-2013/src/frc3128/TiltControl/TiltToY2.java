package frc3128.TiltControl;

import edu.wpi.first.wpilibj.Gyro;
import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.ListenerManager.ListenerManager;

public class TiltToY2 extends Event {
    private static final double thresh = 1;
    
    public void setTiltAngle() {
        double y2 = Global.xControl1.y2;
        
        if(1==1) throw new Error("Must set gyro polarization");
        if(Math.abs(Global.tiltGyro.getAngle() - y2*35.0) > thresh)
            Global.mTilt.set((Global.tiltGyro.getAngle() - y2*35.0)/35.0);
        else
            Global.mTilt.set(0);
    }

    public void execute() {        
        DebugLog.log(4, referenceName, "Angle: " + Global.tiltGyro.getAngle());
        setTiltAngle();
        
    }
    
}
