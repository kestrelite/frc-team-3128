package frc3128;

import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerManager;

import frc3128.Util.Connection.Beaglebone;

public class Global {
    public static void initializeRobot() {}
    public static void initializeDisabled() {}
    public static void initializeAuto() {}
    public static void initializeTeleop() 
    {
        
        Beaglebone beaglebone = new Beaglebone();
        beaglebone.registerIterableEvent();
    }
 
    public static void robotKill() {
        Watchdog.getInstance().kill();
    }

    public static void robotStop() {
        EventManager.dropAllEvents();
        ListenerManager.dropAllListeners();
    }

    private Global() {
    }
}
