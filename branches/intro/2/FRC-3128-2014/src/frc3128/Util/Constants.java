package frc3128.Util;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class Constants {
    public static final int DEBUGLOG_DEFAULT_LOGLEVEL = 3; //Controls the standard logging level for the DebugLog
    public static final int DEBUGLOG_INFO_DISPLAYFREQ = 100; //Controls how often the DebugLog displays diagnostic information
    public static final boolean EVENT_DUPLICATE_CHECKS = true; //Whether or not the EventManager runs duplicate checks
    public static final boolean START_MCONTROL_ON_TARGETSET = true; //Whether a MotorLink should start its controller if the controller's value was set
    public static final boolean PROCESS_WHILE_DISABLED = false; //Whether or not to run the EventManager while disabled
    
    private Constants() {}
}
