package frc3128;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class DebugLog {
    public static final int LVL_STREAM = 5;
    public static final int LVL_DEBUG = 4;
    public static final int LVL_INFO = 3;
    public static final int LVL_WARN = 2;
    public static final int LVL_SEVERE = 1;
    public static final int LVL_ERROR = 0;
    
    private static int logDetail = 2;
    private static int maxTagLength = 0;
    private static final int initTagLength = 32;

	/**
	 * This sets the log detail level. This is used to filter based on severity.
	 * Severity is the following:
	 * <p>0: Error; 1: Severe; 2: Warning; 3: Info; 4: Debug; 5: Stream
	 * 
	 * @param level The level to be logged. 
	 */
    public static void setLogLevel(int level) {
        DebugLog.log(DebugLog.LVL_INFO, "DebugLog", "Log detail set to " + level);
        DebugLog.logDetail = level;
    }

	/**
	 * Logs a message to the NetBeans console. The log keeps all statements in 
	 * alignment with the longest message. It will hide any information whose
	 * log level is higher than the provided maximum detail. It uses an object
	 * (typically a string) to create a tag for the message.
	 * 
	 * @param level The severity level of the message to be logged.
	 * @param obj   The object (typically String) used to identify the message source.
	 * @param text  The message to be logged.
	 */
    public static void log(int level, Object obj, String text) {
        String strLv = "[UNKN]  ";
        if(level <= 0) strLv = "[ERROR" + Math.abs(level) + "] ";
        if(level == 1) strLv = "[SEVERE]";
        if(level == 2) strLv = "[**WARN]";
        if(level == 3) strLv = "[INFO]  ";
        if(level == 4) strLv = "[DEBUG] ";
        if(level == 5) strLv = "[STREAM]";
        
        if(obj.toString().length() > DebugLog.maxTagLength && level <= DebugLog.logDetail)
            DebugLog.maxTagLength = obj.toString().length();
        
        if(level <= DebugLog.logDetail) {
            System.out.print("[" + System.currentTimeMillis() + "] " + 
                    strLv + " [" + 
                    (obj.toString().substring(0, 4).equals("edu.")?obj.toString().substring(initTagLength):obj.toString()) + "] ");
            for(int i = 0; i < maxTagLength - obj.toString().length(); i++)
                System.out.print(" ");
            System.out.println(text);
        }
    }

	private DebugLog() {}
}