package frc3128.Util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class Constants {
    public static final boolean DEBUGLOG_ENABLED = true; //Enables and disables the debug log
    public static final int     DEBUGLOG_DEFAULT_LOGLEVEL = 3; //Controls the standard logging level for the DebugLog
    public static final int     DEBUGLOG_INFO_DISPLAYFREQ = 350; //Controls how often the DebugLog displays diagnostic information
    public static boolean       INTRO_MESSAGE = true; //Whether to display the welcome message

    public static final boolean START_MCONTROL_ON_TARGETSET = true; //Whether a MotorLink should start its controller if the controller's value was set
    
    public static final boolean EVENT_DUPLICATE_CHECKS = true; //Whether or not the EventManager runs duplicate checks
    public static final boolean EVENT_PROCESS_WHILE_DISABLED = false; //Whether or not to run the EventManager while disabled
    public static final boolean EVENT_SHOW_STREAM_MSG = false; //Whether to show the stream messages from EventManager
    public static final boolean EVENT_DIAGNOSE_RUNTIMES = false; //Whether to diagnose event running and active times
    public static final int     EVENT_CLEANUP_AFTER_ITER = 50; //Cleanup after iteration count
    public static final int     EVENT_CLEANUP_AFTER_DELEV = 5; //Cleanup after number of deleted events
    public static final int     EVENT_DISPLAY_DIAG_AFTER = 500; //Display diagnostics after iterations...
    
    public static final boolean LISTENER_SHOW_DEBUG_CALLS = false; //Display ListenerManager calls
    public static final boolean SHOOTER_ENABLED = true; //Enables or disables the arm
    
    public static Alliance getAlliance() {return DriverStation.getInstance().getAlliance();}
    
    private Constants() {}
}
