package frc3128.Util;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class Constants {
    public static final boolean DEBUGLOG_ENABLED = true; //Enables and disables the debug log
    public static final int     DEBUGLOG_DEFAULT_LOGLEVEL = 3; //Controls the standard logging level for the DebugLog
    public static final int     DEBUGLOG_INFO_DISPLAYFREQ = 100; //Controls how often the DebugLog displays diagnostic information
    public static boolean       INTRO_MESSAGE = true; //Whether to display the welcome message

    public static final boolean START_MCONTROL_ON_TARGETSET = true; //Whether a MotorLink should start its controller if the controller's value was set
    
    public static final boolean EVENT_DUPLICATE_CHECKS = true; //Whether or not the EventManager runs duplicate checks
    public static final boolean EVENT_PROCESS_WHILE_DISABLED = false; //Whether or not to run the EventManager while disabled
    public static final int     EVENT_CLEANUP_AFTER_ITER = 50; //Cleanup after iteration count
    public static final int     EVENT_CLEANUP_AFTER_DELEV = 5; //Cleanup after number of deleted events
    
    private Constants() {}
}
