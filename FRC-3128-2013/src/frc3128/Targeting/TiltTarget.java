package frc3128.Targeting;

import com.sun.squawk.util.MathUtils;
import frc3128.Connection.CameraCon;
import frc3128.DebugLog;
import frc3128.EventManager.Event;

public class TiltTarget extends Event {
    double thShift = -12.0;
    double targetTh = 0;
    private static TiltLock tLock = new TiltLock();
    double distToGoal = 0;
    boolean firstIter = true;
    
    public void execute() {
        if(firstIter) {tLock.registerIterableEvent(); firstIter = false;}
        if(distToGoal == 0) {
            distToGoal = CameraCon.distToGoal;
            return;
        }
        
        DebugLog.log(5, referenceName, "tgt:" + targetTh + ", dst:"+distToGoal);
        targetTh = ((targetTh > 40) ? 5 : 180*(MathUtils.atan2(92, distToGoal))/(Math.PI)+thShift);
        TiltTarget.tLock.lockTo(-targetTh);
    }
}