package frc3128;

import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerManager;

public class Global {
    public final static String referenceName = "Global";
    public final static EventManager eventManager = new EventManager();

	public static void initializeRobot() {
        DebugLog.setLogLevel(DebugLog.LVL_INFO);
    }

    public static void initializeDisabled() {}

    public static void initializeAuto() {
        EventManager.dropEvents(); ListenerManager.dropAllListeners();
    }

    public static void initializeTeleop() {
        EventManager.dropEvents(); ListenerManager.dropAllListeners();
    }

    public static void robotKill() {
        EventManager.dropEvents();
        ListenerManager.dropAllListeners();
        Watchdog.getInstance().kill();
    }
    
    public static void robotStop() {
        EventManager.dropEvents();
        ListenerManager.dropAllListeners();
    }
    
    public static void robotPause() {
        EventManager.toggleEventProcessing();
    }
    
	private Global() {}
}