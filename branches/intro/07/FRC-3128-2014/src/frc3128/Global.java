package frc3128;

import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SingleSequence;
import frc3128.EventManager.ListenerManager;
import frc3128.HardwareLink.Controller.AttackControl;
import frc3128.Util.DebugLog;
import frc3128.Util.ListenerConst;

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