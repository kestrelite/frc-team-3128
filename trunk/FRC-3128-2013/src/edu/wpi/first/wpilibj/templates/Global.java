package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.templates.Controller.*;
import edu.wpi.first.wpilibj.templates.EventManager.Event;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;
import edu.wpi.first.wpilibj.templates.PneumaticsManager.PneumaticsManager;

class WatchdogEvent extends Event {
    public void execute() {
        Watchdog.getInstance().feed();
    }
}

public class Global {
    public final static String referenceName = "Global";
    public final static EventManager eventManager = new EventManager();
    public final static WatchdogEvent WatchdogEvent = new WatchdogEvent();
    public final static Controller Controller = new Controller();
    public final static PneumaticsManager p = new PneumaticsManager(1,2,3,4);
    
    public static boolean shutdown = false;

    public static long getSystemTimeMillis() {
        return System.currentTimeMillis();
    }

    public static void initialize() {
        DebugLog.setLogLevel(4);
        WatchdogEvent.registerIterable(10);
        
        //SAMPLE CODE
        Controller.bindToController(1);
        PneumaticsManager.compressorStateOn();
    }

    public static void initializeAuto() {
        PneumaticsManager.lockPistons();
    }

    public static void initializeTeleop() {
        Controller.registerIterable(9);
    }
}
