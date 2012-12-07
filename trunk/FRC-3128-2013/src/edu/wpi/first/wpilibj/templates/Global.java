package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.templates.Controller.XControl;
import edu.wpi.first.wpilibj.templates.EventManager.Event;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;
import edu.wpi.first.wpilibj.templates.ListenerManager.ListenerManager;

class WatchdogEvent extends Event {
    public void execute() {
        Watchdog.getInstance().feed();
    }
}

class DriveTank extends Event {
    public void execute() {
        double x = Global.xControl1.x1;
        double y = Global.xControl1.y1;
        
        if (Math.abs(y) < 0.15) y = 0;
        if (Math.abs(x) < 0.15) x = 0;
        
        DebugLog.log(4, referenceName, "Y: "+y+", "+x);
        Global.mLB.set(((y-(x/1.5))/1.3));
        Global.mRB.set((-(y+(x/1.5))));
        Global.mLF.set(((y-(x/1.5))/1.3));
        Global.mRF.set((-(y+(x/1.5))));
    }
}

public class Global {
    public final static String referenceName = "Global";
    public static boolean shutdown = false;
    public final static EventManager eventManager = new EventManager();
    public final static Jaguar mLB = new Jaguar(1, 1);
    public final static Jaguar mRB = new Jaguar(1, 4);
    public final static Jaguar mLF = new Jaguar(1, 2);
    public final static Jaguar mRF = new Jaguar(1, 3);
    public static XControl xControl1 = new XControl(1);

    public static void initializeRobot() {
        DebugLog.setLogLevel(4);
        (new WatchdogEvent()).registerIterableEvent();
        ListenerManager.addListener((new DriveTank()), "updateJoy1");
    }

    public static void initializeDisabled() {
        
    }
    
    public static void initializeAuto() {
    
    }

    public static void initializeTeleop() {
        
    }
    
    public static void robotReset() {
        EventManager.dropEvents();
        ListenerManager.dropListeners();
    }
    
    public static void robotKill() {
        EventManager.dropEvents();
        ListenerManager.dropListeners();
        Watchdog.getInstance().kill();
    }
}
