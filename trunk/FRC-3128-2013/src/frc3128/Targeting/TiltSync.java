package frc3128.Targeting;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.Global;

public class TiltSync {
    private static boolean tiltIsLocked = false;
    private static Event tiltLockedBy = null;
    
    public static boolean getLock(Event e) {
        if(tiltIsLocked) return false;

        TiltSync.tiltIsLocked = true;
        TiltSync.tiltLockedBy = e;
        return true;
    }
    
    public static void getLock(Event e, boolean override) {
        if(override || !tiltIsLocked) {
            TiltSync.tiltIsLocked = true;
            TiltSync.tiltLockedBy = e;
        }
    }
    
    public static void setTiltPow(Event e, double pow) {
        if(tiltLockedBy.equals(e)) {
            Global.mTilt.set(pow);
        } else {
            DebugLog.log(1, "TiltSync", "Tilt power was modified by " + e + " but the request was rejected! Currently held by: " + TiltSync.tiltLockedBy);
        }
    }
    
    public static boolean hasLock(Event e) {
       return e.equals(TiltSync.tiltLockedBy); 
    }
    
    public static void releaseLock(Event e) {
        TiltSync.tiltLockedBy = null;
        TiltSync.tiltIsLocked = false;
    }
}
