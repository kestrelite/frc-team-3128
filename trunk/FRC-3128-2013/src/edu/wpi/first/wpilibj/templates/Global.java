package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.templates.Controller.XButtonMap;
import edu.wpi.first.wpilibj.templates.EventManager.Event;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;

class WatchdogEvent extends Event {

    public void execute() {
        Watchdog.getInstance().feed();
    }
}

class DriveTank extends Event {
    public void execute() {
        double x = Global.joy.getRawAxis(1)/1.25;
        double y = -Global.joy.getRawAxis(2)/1.25;
        
        if (Math.abs(y) < 0.15) y = 0;
        if (Math.abs(x) < 0.15) x = 0;
        
        DebugLog.log(4, referenceName, "Y: "+y+", "+x);
        
        Global.mLB.set(((y-(x/1.5))/1.3));
        Global.mRB.set((-(y+(x/1.5))));
        Global.mLF.set(((y-(x/1.5))/1.3));
        Global.mRF.set((-(y+(x/1.5))));
        
        //DebugLog.log(4, referenceName, ""+Global.mRF.get());
    }
}

class CatapultOn extends Event {
    public void execute() {
        if (Global.joy.getRawButton(XButtonMap.Y)) {
            Global.catapultOne.set(true);
            Global.catapultTwo.set(false);
        }
    }
}

class CatapultOff extends Event {
    public void execute() {
        if (Global.joy.getRawButton(XButtonMap.A)) {
            Global.catapultOne.set(false);
            Global.catapultTwo.set(true);
        }
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
    public final static Compressor comp = new Compressor(1, 1, 1, 1);
    public final static Solenoid catapultOne = new Solenoid(1, 3);
    public final static Solenoid catapultTwo = new Solenoid(1, 4);
    public static boolean shutdown = false;

    public static long getSystemTimeMillis() {
        return System.currentTimeMillis();
    }

    public static void initialize() {
        DebugLog.setLogLevel(4);
        //EventManager.startIPSCounter();
        WatchdogEvent.registerIterable();
        (new DriveTank()).registerIterable();
        (new CatapultOn()).registerIterable();
        (new CatapultOff()).registerIterable();
        comp.start();
    }

    public static void initializeAuto() {
    }

    public static void initializeTeleop() {
        
    }
}
