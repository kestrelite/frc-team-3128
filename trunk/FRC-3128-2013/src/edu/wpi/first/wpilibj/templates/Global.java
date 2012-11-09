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

public class Global {
    public final static String referenceName = "Global";
    public static boolean shutdown = false;
    public final static EventManager eventManager = new EventManager();    
    public static XControl xControl1 = new XControl(1);

    public static void initializeRobot() {
        DebugLog.setLogLevel(4);
        (new WatchdogEvent()).registerIterable();
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
