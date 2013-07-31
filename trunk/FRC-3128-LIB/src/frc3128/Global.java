package frc3128;

import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerManager;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class Global {
    public static void initializeRobot() {}
    public static void initializeDisabled() {}
    public static void initializeAuto() {}
    public static void initializeTeleop() {}

	public static void robotKill() {Watchdog.getInstance().kill();}
    public static void robotStop() {EventManager.dropAllEvents(); ListenerManager.dropAllListeners();}
	
	private Global() {}
}