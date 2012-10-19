package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.templates.EventManager.Event;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;

class WatchdogEvent extends Event {
    public void execute() {
        Watchdog.getInstance().feed();
    }
}

class DriveEvent extends Event {
    public void execute() {
        double x = Global.joy.getAxis(Joystick.AxisType.kX);
        double y = Global.joy.getAxis(Joystick.AxisType.kY);
        Global.mLB.set(y);
        Global.mRB.set(y);
        Global.mLF.set(y);
        Global.mRF.set(y);
    }
}

public class Global {
    public final static String referenceName = "Global";
    public final static EventManager eventManager = new EventManager();
    public final static WatchdogEvent WatchdogEvent = new WatchdogEvent();
    public final static Jaguar mLB = new Jaguar(1, 3);
    public final static Jaguar mRB = new Jaguar(1, 6);
    public final static Jaguar mLF = new Jaguar(1, 2); //Works
    public final static Jaguar mRF = new Jaguar(1, 1); //Works
    //public final static Jaguar mBAD1 = new Jaguar(1, 9);
    public final static Jaguar mBAD2 = new Jaguar(1, 4); //Works
    public final static Joystick joy = new Joystick(1);
    //public final static PneumaticsManager p = new PneumaticsManager(1,2,3,4);
    
    public static boolean shutdown = false;

    public static long getSystemTimeMillis() {
        return System.currentTimeMillis();
    }

    public static void initialize() {
        DebugLog.setLogLevel(4);
        //EventManager.startIPSCounter();
        WatchdogEvent.registerIterable();
        
    }

    public static void initializeAuto() {
    }

    public static void initializeTeleop() {
        //Controller.registerIterable(9);
        mLF.set(1);
        mRF.set(1);
        //mBAD1.set(.5);
        mBAD2.set(.5);
    }
}
